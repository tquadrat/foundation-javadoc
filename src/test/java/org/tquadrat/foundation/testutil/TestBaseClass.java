/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.testutil;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.getProperties;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.System.setErr;
import static java.lang.System.setIn;
import static java.lang.System.setOut;
import static java.lang.Thread.getAllStackTraces;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apiguardian.api.API;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *  A base class for JUnit test classes.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBaseClass.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"AbstractClassExtendsConcreteClass", "AbstractClassWithoutAbstractMethods"} )
@API( status = STABLE, since = "0.0.5" )
public abstract class TestBaseClass extends EasyMockSupport
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message for an exception that was not thrown: {@value}.
     */
    @SuppressWarnings( "ConstantDeclaredInAbstractClass" )
    public static final String MSG_ExceptionNotThrown = "Expected Exception '%s' was not thrown";

    /**
     *  The message for an unexpected exception that was thrown: {@value}.
     */
    @SuppressWarnings( "ConstantDeclaredInAbstractClass" )
    public static final String MSG_ExceptionThrown = "Unexpected Exception '%s' was thrown";

    /**
     *  The message that another than the expected exception was thrown: {@value}.
     */
    @SuppressWarnings( "ConstantDeclaredInAbstractClass" )
    public static final String MSG_WrongExceptionThrown = "Wrong Exception type; caught '%2$s' but '%1$s' was expected";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The default error output stream.
     */
    private PrintStream m_DefaultErrorStream;

    /**
     *  The default input stream.
     */
    private InputStream m_DefaultInputStream;

    /**
     *  The default
     *  {@link Locale}.
     */
    private Locale m_DefaultLocale;

    /**
     *  The default output stream.
     */
    private PrintStream m_DefaultOutputStream;

    /**
     *  The default
     *  {@link TimeZone}.
     */
    @SuppressWarnings( "UseOfObsoleteDateTimeApi" )
    private TimeZone m_DefaultTimeZone;

    /**
     *  The default
     *  {@link ZoneId}.
     */
    @SuppressWarnings( {"FieldCanBeLocal", "unused"} )
    private ZoneId m_DefaultZoneId;

    /**
     *  The flag that tracks the assertion on/off status for this package.
     */
    private static boolean m_AssertionOn;

    /**
     *  Threads that are alive before the test is started.
     */
    private Collection<Thread> m_LiveThreadsBeforeSetup = null;

    /**
     *  Flag that controls whether the test regarding additional threads will
     *  be performed or not.
     *
     *  @see #assertThatThereAreNoMoreLiveThreadsThanBeforeSetup()
     */
    private boolean m_SkipThreadTest = false;

    /**
     *  The system properties.
     */
    private static Properties m_SystemProperties;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    static
    {
        //---* Determine the assertion status *--------------------------------
        m_AssertionOn = false;
        /*
         * As the JUnit tests will always be executed with the flag
         * "-ea" (assertions enabled), this code sequence is not tested in all
         * branches.
         */
        //noinspection AssertWithSideEffects,PointlessBooleanExpression,NestedAssignment,ConstantConditions
        assert (m_AssertionOn = true) == true : "Assertion is switched off";
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code TestBaseClass} instance.
     */
    protected TestBaseClass()
    {
        //  Just exists!
    }   //  TestBaseClass()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  As the name of the method indicates, it asserts that the JDK assertions
     *  are enabled.<br>
     *  <br>The method uses
     *  {@link #isAssertionOn()} to check whether JDK assertion is
     *  currently activated, meaning that the program was started with the
     *  command line flags {@code -ea} or {@code -enableassertions}.<br>
     *  <br>Unfortunately, it is possible to activate assertions for some
     *  selected packages only (and {@code org.tquadrat.test} may not be
     *  amongst these), or {@code org.tquadrat.test} is explicitly disabled
     *  with {@code -da} or {@code -disableassertions} (or – most mean! –
     *  assertions are <i>only</i> activated for {@code org.tquadrat.test}}.
     *  Therefore, the outcome of this method will not guarantee for one
     *  hundred percent that assertion are always thrown where necessary.
     */
    @BeforeAll
    protected static final void assertThatJDKAssertionAreEnabled()
    {
        if( !isAssertionOn() )
        {
            fail( "JDK Assertions are not enabled" );
        }
    }   //  assertThatJDKAssertionAreEnabled()

    /**
     *  As the name of the method indicates, it asserts that the system
     *  properties were not modified.
     */
    @AfterEach
    @SuppressWarnings( "static-method" )
    protected final void assertThatSystemPropertiesWereNotChanged()
    {
        final var currentProperties = getProperties();
        final Collection<String> names = new HashSet<>( currentProperties.stringPropertyNames() );
        if( currentProperties.size() < m_SystemProperties.size() )
        {
            final Collection<String> storedNames = new HashSet<>( m_SystemProperties.stringPropertyNames() );
            storedNames.removeAll( names );
            fail( format( "System Property '%s' was removed", String.join( ", ", storedNames ) ) );
        }

        String value;
        for( final var name : names )
        {
            value = currentProperties.getProperty( name );
            if( nonNull( value) )
            {
                if( !value.equals( m_SystemProperties.getProperty( name ) ) )
                {
                    fail( format( "System Property '%s' was changed", name ) );
                }
            }
            else
            {
                if( nonNull( m_SystemProperties.getProperty( name ) ) )
                {
                    fail( format( "System Property '%s' was added", name ) );
                }
            }
        }
    }   //  assertThatSystemPropertiesWereNotChanged()

    /**
     *  As the name of the method indicates, it asserts that there are no more
     *  living threads than there had been before setup.
     */
    @AfterEach
    protected final void assertThatThereAreNoMoreLiveThreadsThanBeforeSetup()
    {
        if( !m_SkipThreadTest )
        {
            if( isNull( m_LiveThreadsBeforeSetup ) )
            {
                fail( "'obtainLiveThreads()' was not executed" );
            }

            final Collection<Thread> liveThreads = new HashSet<>( getLiveThreads() );
            liveThreads.removeAll( m_LiveThreadsBeforeSetup );
            if( !liveThreads.isEmpty() )
            {
                final var failed = new AtomicBoolean( false );
                final var message = liveThreads.stream()
                    .filter( Thread::isAlive )
                    .map( t ->
                    {
                        failed.set( true );
                        final var buffer = new StringBuilder( t.getName() )
                            .append( " (" )
                            .append( t.getId() );
                        if( t.isDaemon() )
                        {
                            buffer.append( "/DAEMON" );
                        }
                        buffer.append( ")" );
                        return buffer.toString();
                    } )
                    .collect( joining( ", ", "Detected unexpected living threads after test: ", EMPTY_STRING ) );
                if( failed.get() ) fail( message );
            }
        }
    }   //  assertThatThereAreNoMoreLiveThreadsThanBeforeSetup()

    /**
     *  Returns the default error stream.
     *
     *  @return The default error stream.
     */
    protected final PrintStream getDefaultErrorStream() { return m_DefaultErrorStream; }

    /**
     *  Returns the default output stream.
     *
     *  @return The default output stream.
     */
    protected final PrintStream getDefaultOutputStream() { return m_DefaultOutputStream; }

    /**
     *  Returns all threads that are currently alive.
     *
     *  @return The living threads.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Collection<Thread> getLiveThreads()
    {
        final var retValue = getAllStackTraces().keySet();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getLiveThreads()

    /**
     *  Checks whether the machine, that runs the current test, has network
     *  configured.
     *
     *  @return {@code true} if the current machine has a network configured,
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "static-method" )
    protected final boolean hasNetwork()
    {
        var retValue = true;
        try
        {
            retValue = getNetworkInterfaces().hasMoreElements();
        }
        catch( @SuppressWarnings( "unused" ) final SocketException e )
        {
            retValue = false;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasNetwork()

    /**
     *  Checks whether JDK assertion is currently activated, meaning that the
     *  program was started with the command line flags {@code -ea} or
     *  {@code -enableassertions}. If assertions are activated for some
     *  selected packages only and {@code org.tquadrat.test} is not amongst
     *  these, or {@code org.tquadrat.test} is explicitly disabled with
     *  {@code -da} or {@code -disableassertions}, this method will return
     *  {@code false}. But even it may return {@code true}, it is possible that
     *  assertions are still not activated for some packages.
     *
     *  @return {@code true} if assertions are activated for the
     *      package {@code org.tquadrat.foundation.test} and hopefully also for
     *      any other package, {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isAssertionOn() { return m_AssertionOn; }

    /**
     *  References to all currently running threads will be stored.
     *
     *  @see #m_LiveThreadsBeforeSetup
     *  @see #assertThatThereAreNoMoreLiveThreadsThanBeforeSetup()
     */
    @BeforeEach
    protected final void obtainLiveThreads()
    {
        m_LiveThreadsBeforeSetup = getLiveThreads();
    }   //  obtainLiveThreads()

    /**
     *  Resets the default settings for
     *  {@link Locale},
     *  {@link TimeZone},
     *  and
     *  {@link ZoneId}.
     *
     *  @see Locale#setDefault(Locale)
     *  @see TimeZone#setDefault(TimeZone)
     *  @see ZoneId#systemDefault()
     */
    @SuppressWarnings( "UseOfObsoleteDateTimeApi" )
    @BeforeEach
    protected final void resetDefaultSettings()
    {
        Locale.setDefault( m_DefaultLocale );
        TimeZone.setDefault( m_DefaultTimeZone );
    }   //  resetDefaultSettings()

    /**
     *  Resets the default streams.
     */
    @AfterEach
    protected final void resetDefaultStreams()
    {
        setErr( m_DefaultErrorStream );
        setIn( m_DefaultInputStream );
        setOut( m_DefaultOutputStream );
    }   //  resetDefaultStreams()

    /**
     *  Resets the thread test flag.
     */
    @BeforeEach
    protected final void resetSkipThreadTest() { m_SkipThreadTest = false; }

    /**
     *  Saves the default settings for
     *  {@link Locale},
     *  {@link TimeZone},
     *  and
     *  {@link ZoneId}.
     *
     *  @see Locale#getDefault()
     *  @see TimeZone#getDefault()
     *  @see ZoneId#systemDefault()
     */
    @SuppressWarnings( "UseOfObsoleteDateTimeApi" )
    @BeforeEach
    protected final void saveDefaultSettings()
    {
        m_DefaultLocale = Locale.getDefault();
        m_DefaultTimeZone = TimeZone.getDefault();
        m_DefaultZoneId = ZoneId.systemDefault();
    }   //  saveDefaultSettings()

    /**
     *  Saves the default streams.
     */
    @BeforeEach
    protected final void saveDefaultStreams()
    {
        m_DefaultErrorStream = err;
        m_DefaultInputStream = in;
        m_DefaultOutputStream = out;
    }   //  saveDefaultStreams()

    /**
     *  Sets the thread test flag to {@code true}, disabling that test.
     */
    public final void skipThreadTest() { m_SkipThreadTest = true; }

    /**
     *  Keep the
     *  {@linkplain System#getProperties() system properties}.
     */
    @BeforeAll
    protected static void storeSystemProperties()
    {
        m_SystemProperties = new Properties( getProperties() );
    }   //  storeSystemProperties()

    /**
     *  Translates the escape sequences in a String. Refer to
     *  {@link String#translateEscapes()}
     *  for the details.<br>
     *  <br>Use this method to convert the input from CSV files or alike.
     *
     *  @param  input   The input String; can be {@code null}.
     *  @return The processed String; will be {@code null} if the input was
     *      already {@code null}.
     *
     *  @since 0.1.0
     */
    @SuppressWarnings( "static-method" )
    protected final String translateEscapes( final CharSequence input )
    {
        final var retValue = isNull( input ) ? null : input.toString().translateEscapes();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translateEscapes()

    /**
     *  Validates whether a class is really a static class.<br>
     *  <br>A class is <i>static</i> when it has the following characteristics:
     *  <ul>
     *  <li>The class is final.</li>
     *  <li>All methods are static.</li>
     *  <li>Only the default constructor may exists, this has to be private,
     *  and it has to throw an
     *  {@link Error}
     *  on invocation.</li>
     *  </ul>
     *  If a test fails, the result is written to
     *  {@link System#out}.<br>
     *  <br>The method will not check for the {@code &#64;UtilityClass}
     *  annotation as this has only the retention level {@code SOURCE}.
     *
     *  @param  candidate   The class to inspect.
     *  @return {@code true} if the class is in fact static, {@code false}
     *      otherwise.
     */
    @SuppressWarnings( {"ProhibitedExceptionThrown", "RedundantStreamOptionalCall"} )
    protected final boolean validateAsStaticClass( final Class<?> candidate )
    {
        skipThreadTest();

        //---* The class must be final *---------------------------------------
        var retValue = isFinal( requireNonNull( candidate, "candidate must not be null" ).getModifiers() );
        if( !retValue )
        {
            out.printf( "Class '%s' is not final\n", candidate.getName() );
        }

        //---* All methods must be static *------------------------------------
        final Collection<Method> methods = new ArrayList<>( asList( candidate.getMethods() ) );
        methods.addAll( asList( candidate.getDeclaredMethods() ) );
        //noinspection SimplifyStreamApiCallChains
        retValue &= !methods.stream()
            //---* Eliminate all methods from Object *-------------------------
            .filter( method -> !method.getDeclaringClass().equals( Object.class ) )
            //---* Eliminate all static methods *------------------------------
            .filter( method -> !isStatic( method.getModifiers() ) )
            //---* Map the remaining methods to their names *------------------
            .map( Method::toString )
            //---* Eliminate duplicates *--------------------------------------
            .distinct()
            //---* Print all remaining methods *-------------------------------
            .peek( method -> out.printf( "Method '%2$s' of class '%1$s' is not static\n", candidate.getName(), method ) )
            /*
             * This was just added to avoid a shortcut; otherwise, only the
             * first offending method would be printed.
             */
            .sorted()
            //---* Any entry will match - if any at all *----------------------
            .anyMatch( m -> true );

        //---* No public or protected constructors are allowed *---------------
        var constructors = candidate.getConstructors();
        if( constructors.length > 0 )
        {
            retValue = false;
            out.printf( "Class '%s' has public constructor\n", candidate.getName() );
        }

        //---* Only one private constructor is allowed *-----------------------
        constructors = candidate.getDeclaredConstructors();
        if( constructors.length != 1 )
        {
            retValue = false;
            out.printf( "Class '%s' defines more than one constructor\n", candidate.getName() );
        }

        /*
         * This one and only constructor has to be the default constructor.
         */
        final var constructor = constructors [0];
        if( constructor.getParameterTypes().length != 0 )
        {
            retValue = false;
            out.printf( "Constructor of class '%s' is not the default constructor\n", candidate.getName() );
        }

        //---* The default constructor has to be private *---------------------
        if( !isPrivate( constructor.getModifiers() ) )
        {
            retValue = false;
            out.printf( "Constructor of class '%s' is not private\n", candidate.getName() );
        }

        /*
         * The constructor has to throw an Error (in fact, a
         * PrivateConstructorForStaticClassCalledError).
         */
        if( retValue )
        {
            final var message = "Constructor of class '%s' does not throw a PrivateConstructorForStaticClassCalledError\n";

            /*
             * To avoid exceptions, this part of the test will be executed
             * only when the one and only constructor is the default
             * constructor - meaning the return value is still true.
             */
            constructor.setAccessible( true );
            try
            {
                constructor.newInstance();
                retValue = false;
                out.printf( message, candidate.getName() );
            }
            catch( final InvocationTargetException e )
            {
                final var t = e.getCause();
                if( isNull( t ) || !t.getClass().getName().endsWith( "PrivateConstructorForStaticClassCalledError" ) )
                {
                    retValue = false;
                    out.printf( message, candidate.getName() );
                }
            }
            catch( final InstantiationException | IllegalAccessException | IllegalArgumentException e )
            {
                throw new RuntimeException( format( "Instantiation of class '%s' failed", candidate.getName() ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  validateAsStaticClass()
}
//  class TestBaseClass

/*
 *  End of File
 */