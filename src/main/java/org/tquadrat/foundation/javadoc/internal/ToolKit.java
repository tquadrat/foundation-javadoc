/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc.internal;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.foundation.util.Entities.HTML50;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.javadoc.internal.foundation.util.ListBasedComparator;

/**
 *  <p>{@summary Tools and constants for the JavaDoc extension.}</p>
 *  <p>Originally, this tool was developed with lots of dependencies to the
 *  other <i>Foundation</i> libraries; the static stuff from
 *  {@code foundation-base} and {@code foundation-util} was placed into this
 *  class.</p>
 *
 *  @version $Id: ToolKit.java 991 2022-01-16 16:58:29Z tquadrat $
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 */
@API( status = INTERNAL, since = "0.1.0" )
@ClassVersion( sourceVersion = "$Id: ToolKit.java 991 2022-01-16 16:58:29Z tquadrat $" )
@UtilityClass
public class ToolKit
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  Implementations of this interface provides the sort order key from the
     *  given instance of the type.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: ToolKit.java 991 2022-01-16 16:58:29Z tquadrat $
     *  @since 0.0.5
     *
     *  @param  <T> The type to order.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     */
    @API( status = STABLE, since = "0.0.5" )
    @ClassVersion( sourceVersion = "$Id: ToolKit.java 991 2022-01-16 16:58:29Z tquadrat $" )
    @FunctionalInterface
    public interface KeyProvider<T,K>
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the sort order key for the given instance.
         *
         *  @param  instance    The instance; may be {@code null}.
         *  @return The respective sort order key; will be {@code null} if
         *      the {@code instance} was {@code null}.
         */
        public K getKey( T instance );
    }
    //  interface KeyProvider

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The lead-in for a CDATA wrapped String: {@value}
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String CDATA_LEADIN = "<![CDATA[";

    /**
     *  The lead-out for a CDATA wrapped String: {@value}
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String CDATA_LEADOUT = "]]>";

    /**
     *  <p>{@summary The hyphen character (&amp;#8208;/&amp;#x2010;/\u2010/HYPHEN).}</p>
     *  <p>This is different from the character '&#x002D;' (HYPHEN-MINUS),
     *  although it looks similar. This character can be used as a replacement
     *  for the HYPHEN-MINUS in contexts where HYPHEN-MINUS has a special
     *  meaning.</p>
     */
    /*
     * For some reason, JavaDoc refuses to accept both &#8208; and &#x2010;
     * as valid entities.
     */
    @SuppressWarnings( "UnnecessaryUnicodeEscape" )
    @API( status = STABLE, since = "0.0.5" )
    public static final char CHAR_HYPHEN = '\u2010';

    /**
     *  The zero-width non-breaking space character; in fact, the 'word joiner'
     *  character that should be used instead of the original character.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final char CHAR_ZWNBSP = '\u2060';

    /**
     *  Some methods in this class need a buffer; the size of this buffer is
     *  defined here: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     *  An empty array of
     *  {@link String}
     *  instances.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] EMPTY_String_ARRAY = new String [0];

    /**
     *  The index value indicating that nothing was found: {@value}. See for
     *  example
     *  {@link String#indexOf(int)}.
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final int NOT_FOUND = -1;

    /**
     *  A String containing the sequence &quot;null&quot;.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String NULL_STRING;

    /**
     *  The attribute name for an XML attribute holding a class name: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLATTRIBUTE_Class = "class";

    /**
     *  The attribute name for the XML id attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLATTRIBUTE_Id = "xml:id";

    /**
     *  <p>{@summary The attribute name for the XML language attribute:
     *  {@value}.}</p>
     *  <p>This reserved attribute takes an ISO639 language identifier as
     *  value. It indicates the language of the body of the element.</p>
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String XMLATTRIBUTE_Language = "xml:lang";

    /**
     *  The attribute name for the XML space attribute: {@value}.<br>
     *  This reserved attribute indicates whether any whitespace inside the
     *  element is significant and should not be altered by the XML processor.
     *  The attribute can take one of two enumerated values:
     *  <dl>
     *      <dt>{@code preserve}</dt>
     *          <dd>The XML application preserves all whitespace (newlines,
     *          spaces, and tabs) present within the element.</dd>
     *      <dt>{@code default}</dt>
     *          <dd>The XML processor uses its default processing rules when
     *          deciding to preserve or discard the whitespace inside the
     *          element.</dd>
     *  </dl>
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String XMLATTRIBUTE_Whitespace = "xml:space";

    /**
     *  The element name for an XML element holding text: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLELEMENT_Text = "text";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The empty string.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String EMPTY_STRING;

    /**
     *  The null character.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final char NULL_CHAR;

    static
    {
        EMPTY_STRING = "";
        NULL_CHAR = '\u0000';
        NULL_STRING = String.valueOf( (Object) null ).intern();
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private ToolKit() { throw new PrivateConstructorForStaticClassCalledError( ToolKit.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Escapes the non-ASCII and special characters in a
     *  {@code String} so that the result can be used in the context of HTML.}
     *  Wherever possible, the method will return the respective HTML&nbsp;5
     *  entity; only when there is no matching entity, it will use the Unicode
     *  escape.</p>
     *
     *  @param  str The {@code String} to escape, may be {@code null}.
     *  @return A new escaped {@code String}, or {@code null} if the
     *      argument was already {@code null}.
     *
     *  @since 0.0.5
     */
    /*
     *  For some unknown reasons, JavaDoc will not accept the entities &#x7403;
     *  and &#x4F53; (for '球' and '体'), therefore it was required to add the
     *  Chinese character directly into the comment above.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeHTML( final CharSequence str )
    {
        final var retValue = nonNull( str ) ? HTML50.escape( str ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeHTML()

    /**
     *  Escapes the characters in a {@code String} using HTML entities and
     *  writes them to an
     *  {@link Appendable}.
     *  For details, refer to
     *  {@link #escapeHTML(CharSequence)}.
     *
     *  @param  appendable  The appendable object receiving the escaped string.
     *  @param  str  The {@code String} to escape, may be {@code null}.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void escapeHTML( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( nonNull( str ) ) HTML50.escape( appendable, str );
    }   //  escapeHTML()

    /**
     *  Tests if the given String is {@code null} or the empty String.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is
     *      {@code null} or the empty String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isEmpty( final CharSequence s ) { return isNull( s ) || s.isEmpty(); }

    /**
     *  Tests if the given String is {@code null}, the empty String, or just
     *  containing whitespace.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String.
     *
     *  @see String#isBlank()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isEmptyOrBlank( final CharSequence s )
    {
        final var retValue = isNull( s ) || s.toString().isBlank();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEmptyOrBlank()

    /**
     *  Tests if the given String is not {@code null} and not the empty
     *  String.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isNotEmpty( final CharSequence s ) { return nonNull( s ) && !s.isEmpty(); }

    /**
     *  Tests if the given String is not {@code null}, not the empty String,
     *  and that it contains other characters than just whitespace.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String, and it contains other
     *      characters than just whitespace.
     *
     *  @see String#isBlank()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isNotEmptyOrBlank( final CharSequence s )
    {
        final var retValue = nonNull( s ) && !s.toString().isBlank();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isNotEmptyOrBlank()

    /**
     *  Sometimes a special sort order is required that cannot be defined as a
     *  rule. Instead, a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys. Values that are not on
     *  that list will be placed to the end, ordered according to their natural
     *  order if they or their keys implement the
     *  {@link Comparable}
     *  interface, or without any specific order.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final K... keys ) { return new ListBasedComparator<>( keys ); }

    /**
     *  Sometimes a special sort order is required that cannot be defined as a
     *  rule. Instead, a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys. Values that are not on
     *  that list will be placed to the end, ordered according to their natural
     *  order if they or their keys implement the
     *  {@link Comparable}
     *  interface, or without any specific order.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final List<K> keys ) { return new ListBasedComparator<>( keys ); }

    /**
     *  Sometimes a special sort order is required that cannot be defined as a
     *  rule. Instead, a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The implementation of
     *      {@link KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super T> comparator, final List<K> keys )
    {
        return new ListBasedComparator<>( keyProvider, comparator, keys );
    }   //  listBasedComparator()

    /**
     *  Sometimes a special sort order is required that cannot be defined as a
     *  rule. Instead, a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The implementation of
     *      {@link KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final KeyProvider<T, ? super K> keyProvider, final Comparator<? super T> comparator, final K... keys )
    {
        return new ListBasedComparator<>( keyProvider, comparator, keys );
    }   //  listBasedComparator()

    /**
     *  <p>{@summary Reads the complete content of the provided
     *  {@link Reader }
     *  into a
     *  {@link String}.}</p>
     *  <p>Obviously this method is feasible only for files with a limited
     *  size.</p>
     *
     *  @param  reader  The {@code Reader} instance.
     *  @return The content of the provided {@code Reader}.
     *  @throws IOException Problems on reading from the {@code Reader}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String loadToString( final Reader reader ) throws IOException
    {
        final var builder = new StringBuilder( DEFAULT_BUFFER_SIZE );
        final var buffer = new char [DEFAULT_BUFFER_SIZE];
        var bytesRead = requireNonNullArgument( reader, "reader" ).read( buffer );
        while( bytesRead > 0 )
        {
            builder.append( buffer, 0, bytesRead );
            bytesRead = reader.read( buffer );
        }
        final var retValue = builder.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadToString()

    /**
     *  Determines the maximum length over all Strings provided in the given
     *  {@link Stream}.
     *
     *  @param  stream  The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the given {@code stream} are {@code null}, and
     *      {@link Integer#MIN_VALUE}
     *      if the given {@code stream} is empty.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int maxContentLength( final Stream<? extends CharSequence> stream )
    {
        final var retValue = requireNonNullArgument( stream, "stream" )
            .mapToInt( s -> nonNull( s ) ? s.length() : -1 )
            .max()
            .orElse( Integer.MIN_VALUE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Determines the maximum length over all strings provided in the given
     *  {@link Collection}.
     *
     *  @param  list    The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the given {@code list} are {@code null}, and
     *      {@link Integer#MIN_VALUE}
     *      if the given {@code list} is empty.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int maxContentLength( final Collection<? extends CharSequence> list )
    {
        final var retValue = maxContentLength( requireNonNullArgument( list, "list" ).stream() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Checks if the given argument {@code a} is {@code null} and throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}.
     *
     *  @param  <T> The type of the argument to check.
     *  @param  a   The argument to check.
     *  @param  name    The name of the argument; this is used for the error
     *      message.
     *  @return The argument if it is not {@code null}.
     *  @throws NullArgumentException   {@code a} is {@code null}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T> T requireNonNullArgument( final T a, final String name )
    {
        if( isNull( name ) ) throw new NullArgumentException( "name" );
        if( name.isEmpty() ) throw new EmptyArgumentException( "name" );
        if( isNull( a ) ) throw new NullArgumentException( name );

        //---* Done *----------------------------------------------------------
        return a;
    }   //  requireNonNullArgument()

    /**
     *  Checks if the given argument {@code a} is {@code null} or empty and
     *  throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}, or a
     *  {@link EmptyArgumentException}
     *  if it is empty.<br>
     *  <br>Strings, arrays,
     *  {@link java.util.Collection}s, and
     *  {@link java.util.Map}s
     *  will be checked on being empty; this includes instances of
     *  {@link java.lang.StringBuilder},
     *  {@link java.lang.StringBuffer},
     *  and
     *  {@link java.lang.CharSequence}.<br>
     *  <br>For an instance of
     *  {@link java.util.Optional},
     *  the presence of a value is checked in order to determine whether it is
     *  empty or not.<br>
     *  <br>Because the interface
     *  {@link java.util.Enumeration}
     *  does not provide an API for the check on emptiness
     *  ({@link java.util.Enumeration#hasMoreElements() hasMoreElements()}
     *  will return {@code false} after all elements have been taken from
     *  the {@code Enumeration} instance), the result for arguments of this
     *  type has to be taken with caution.<br>
     *  <br>For instances of
     *  {@link java.util.stream.Stream},
     *  this method will only check for {@code null} (like
     *  {@link #requireNonNullArgument(Object,String)}.
     *  This is because any operation on the stream would render it unusable
     *  for later processing.<br>
     *  <br>In case the argument is of type
     *  {@link Optional},
     *  this method behaves different from
     *  {@link #requireNotEmptyArgument(Optional,String)};
     *  this one will return the {@code Optional} instance, while the other
     *  method will return its contents.
     *
     *  @param  <T> The type of the argument to check.
     *  @param  a   The argument to check; may be {@code null}.
     *  @param  name    The name of the argument; this is used for the error
     *      message.
     *  @return The argument if it is not {@code null}.
     *  @throws NullArgumentException   {@code a} is {@code null}.
     *  @throws EmptyArgumentException   {@code a} is empty.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T> T requireNotEmptyArgument( final T a, final String name )
    {
        if( isNull( name ) ) throw new NullArgumentException( "name" );
        if( name.isEmpty() ) throw new EmptyArgumentException( "name" );

        //---* Check for null *------------------------------------------------
        if( isNull( a ) ) throw new NullArgumentException( name );

        //---* Check the type *------------------------------------------------
        //noinspection IfStatementWithTooManyBranches
        if( a instanceof CharSequence charSequence )
        {
            if( charSequence.isEmpty() ) throw new EmptyArgumentException( name );
        }
        else if( a.getClass().isArray() )
        {
            if( Array.getLength( a ) == 0 ) throw new EmptyArgumentException( name );
        }
        else if( a instanceof Collection<?> collection )
            {
                if( collection.isEmpty() ) throw new EmptyArgumentException( name );
            }
            else if( a instanceof Map<?,?> )
                {
                    if( ((Map<?,?>) a).isEmpty() ) throw new EmptyArgumentException( name );
                }
                else if( a instanceof Enumeration<?> )
                    {
                        /*
                         * The funny thing with an Enumeration is that it could have been
                         * not empty in the beginning, but it may be empty (= having no
                         * more elements) now.
                         * The good thing is that Enumeration.hasMoreElements() will not
                         * change the state of the Enumeration - at least it should not do
                         * so.
                         */
                        if( !((Enumeration<?>) a).hasMoreElements() ) throw new EmptyArgumentException( name );
                    }
                    else //noinspection StatementWithEmptyBody
                        if( a instanceof Optional<?> optional)
                        {
                            /*
                             * This is different from the behaviour of
                             * requireNotEmptyArgument(Optional,String) as the Optional will be
                             * returned here.
                             */
                            if( optional.isEmpty() ) throw new EmptyArgumentException( name );
                        }
                        else
                        {
                            /*
                             * Other data types are not further processed; in particular,
                             * instances of Stream cannot be checked on being empty. This is
                             * because any operation on the Stream itself will change its state
                             * and may make the Stream unusable.
                             */
                        }

        //---* Done *----------------------------------------------------------
        return a;
    }   //  requireNotEmptyArgument()

    /**
     *  Checks if the given argument {@code a} of type
     *  {@link Optional}
     *  is {@code null} or empty and
     *  throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}, or a
     *  {@link EmptyArgumentException}
     *  if it is empty.<br>
     *  <br>Otherwise it returns the value of the {@code Optional}.<br>
     *  <br>This is different from the behaviour of
     *  {@link #requireNotEmptyArgument(Object,String)}
     *  with an instance of {@code Optional} as the argument to test.
     *
     *  @param  <T> The type of the given {@code Optional} to check.
     *  @param  optional    The argument to check; can be {@code null}.
     *  @param  name    The name of the argument; this is used for the error
     *      message.
     *  @return The value of the argument if {@code optional} is not
     *      {@code null}.
     *  @throws NullArgumentException   {@code optional} is {@code null}.
     *  @throws EmptyArgumentException   {@code optional} is empty.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T> T requireNotEmptyArgument( @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" ) final Optional<T> optional, final String name )
    {
        if( isNull( name ) ) throw new NullArgumentException( "name" );
        if( name.isEmpty() ) throw new EmptyArgumentException( "name" );

        //---* Check for null *------------------------------------------------
        if( isNull( optional ) ) throw new NullArgumentException( name );
        final var retValue = optional.orElseThrow( () -> new EmptyArgumentException( name ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  requireNotEmptyArgument()

    /**
     *  Splits a String by the given separator character and returns an array
     *  of all parts. In case a separator character is immediately followed by
     *  another separator char, an empty String will be place to the array.
     *  Beginning and end of the String are treated as separators, so if the
     *  first character of the String is a separator, the returned array will
     *  start with an empty string, as it will end with an empty String if the
     *  last character is a separator.<br>
     *  <br>In case the String is empty, the return value will be an array
     *  containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The separator character.
     *  @return The parts of the String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] splitString( final CharSequence string, final char separator )
    {
        return splitString( string, (int) separator );
    }   //  splitString()

    /**
     *  Splits a String by the given separator character, identified by its
     *  Unicode code point, and returns an array of all parts. In case a
     *  separator character is immediately followed by another separator char,
     *  an empty String will be placed to the array. Beginning and end of the
     *  String are treated as separators, so if the first character of the
     *  String is a separator, the returned array will start with an empty
     *  String, as it will end with an empty String if the last character is a
     *  separator.<br>
     *  <br>In case the String is empty, the return value will be an array
     *  containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The code point for the separator character.
     *  @return The parts of the String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] splitString( final CharSequence string, final int separator )
    {
        final var retValue = stream( string, separator ).toArray( String []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  splitString()

    /**
     *  <p>{@summary Splits a String by the given separator character, identified by its
     *  Unicode code point, and returns a
     *  {@link Stream}
     *  of all parts.} In case a separator character is immediately followed by
     *  another separator char, an empty String will be put to the
     *  {@code Stream}. Beginning and end of the String are treated as
     *  separators, so if the first character of the String is a separator, the
     *  returned {@code Stream} will start with an empty String, as it will end
     *  with an empty String if the last character is a separator.</p>
     *  <p>In case the String is empty, the return value will be a
     *  {@code Stream} containing just the empty String.</p>
     *
     *  @param  string  The String to split.
     *  @param  separator   The code point for the separator character.
     *  @return A {@code Stream} instance with the parts of the String.
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final Stream<String> stream( final CharSequence string, final int separator )
    {
        //---* Process the string *--------------------------------------------
        final var s = requireNonNullArgument( string, "string" ).codePoints().toArray();
        final var builder = Stream.<String>builder();
        var begin = -1;
        for( var i = 0 ; i < s.length; ++i )
        {
            if( begin == -1 )
            {
                begin = i;
            }
            if( s [ i ] == separator )
            {
                builder.add( new String( s, begin, i - begin ).intern() );
                begin = -1;
            }
        }

        //---* Add the rest *--------------------------------------------------
        //noinspection ConstantConditions
        if( (begin >= 0) && (begin < s.length) )
        {
            builder.add( new String( s, begin, s.length - begin ).intern() );
        }
        if( (s.length == 0) || (s [s.length - 1] == separator) )
        {
            builder.add( EMPTY_STRING );
        }

        //---* Create the return value *---------------------------------------
        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stream()
}
//  class ToolKit

/*
 *  End of File
 */