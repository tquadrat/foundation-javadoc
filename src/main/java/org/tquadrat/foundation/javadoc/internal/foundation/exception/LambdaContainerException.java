/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.javadoc.internal.foundation.exception;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.io.Serial;
import java.util.Collection;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary A &quot;container&quot; exception for exception thrown within
 *  lambda expressions.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LambdaContainerException.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: LambdaContainerException.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class LambdaContainerException extends RuntimeException
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The exception that is thrown when the container holds an unexpected
     *  exception.
     */
    private class UnexpectedException extends RuntimeException
    {
            /*------------------------*\
        ====** Static Initialisations **=======================================
            \*------------------------*/
        /**
         *  The serial version UID for objects of this class: {@value}.
         */
        @Serial
        private static final long serialVersionUID = 1L;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code UnexpectedException} instance.
         */
        public UnexpectedException()
        {
            super( format( "The Exception '%s' was not expected", LambdaContainerException.this.getCause().getClass().getName() ) );
            setStackTrace( LambdaContainerException.this.getCause().getStackTrace() );
        }   //  UnexpectedException()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         *
         *  @see Throwable#getCause()
         */
        @Override
        public final synchronized Throwable getCause() { return LambdaContainerException.this.getCause(); }
    }
    //  class UnexpectedException

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LambdaContainerException} instance for the given
     *  exception.
     *
     *  @param  e   The exception to wrap; <i>cannot</i> be {@code null}.
     */
    public LambdaContainerException( final Exception e )
    {
        super( null, requireNonNullArgument( e, "e" ) );
    }   //  LambdaContainerException()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Checks whether the contained Exception is somehow expected.
     *
     *  @param  expected    The expected exceptions.
     *  @return {@code true} if the contained Exception is among the list of
     *      expected exceptions, {@code false} otherwise.
     */
    public final boolean checkIfExpected( final Stream<Class<? extends Exception>> expected )
    {
        final var cause = getCause();
        final var retValue = requireNonNullArgument( expected, "expected" ).anyMatch( e -> e.isInstance( cause ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  checkIfExpected()

    /**
     *  Checks whether the contained Exception is somehow expected.
     *
     *  @param  expected    The expected exceptions.
     *  @return {@code true} if the contained Exception is among the list of
     *      expected exceptions, {@code false} otherwise.
     */
    public final boolean checkIfExpected( final Collection<Class<? extends Exception>> expected )
    {
        final var retValue = checkIfExpected( requireNonNullArgument( expected, "expected" ).stream() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  checkIfExpected()

    /**
     *  Checks whether the contained Exception is somehow expected.
     *
     *  @param  expected    The expected exceptions.
     *  @return {@code true} if the contained Exception is among the list of
     *      expected exceptions, {@code false} otherwise.
     */
    @SafeVarargs
    public final boolean checkIfExpected( final Class<? extends Exception>... expected )
    {
        final var retValue = checkIfExpected( stream( requireNonNullArgument( expected, "expected" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  checkIfExpected()

    /**
     *  Returns the contained Exception if it is of the given type; otherwise
     *  a
     *  {@link RuntimeException}
     *  is thrown.
     *
     *  @param  <T> The expected Exception type.
     *  @param  exceptionType   The expected type.
     *  @return The contained Exception.
     *  @throws RuntimeException    This is in fact an
     *      {@link UnexpectedException} that is thrown when the contained
     *      Exception was not expected.
     */
    @SuppressWarnings( "ProhibitedExceptionDeclared" )
    public final <T> T getCheckedCause( final Class<T> exceptionType ) throws RuntimeException
    {
        final var cause = getCause();
        if( !requireNonNullArgument( exceptionType, "exceptionType" ).isInstance( cause ) )
        {
            throw new UnexpectedException();
        }

        final var retValue = exceptionType.cast( cause );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getCheckedCause()
}
//  class LambdaContainerException

/*
 *  End of File
 */