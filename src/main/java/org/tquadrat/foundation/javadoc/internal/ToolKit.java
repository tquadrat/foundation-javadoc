/*
 * ============================================================================
 * Copyright © 2002-2026 by Thomas Thrien.
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

import static java.lang.Integer.min;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.round;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.MAINTAINED;
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
import org.tquadrat.foundation.javadoc.internal.foundation.exception.ValidationException;
import org.tquadrat.foundation.javadoc.internal.foundation.util.ListBasedComparator;

/**
 *  <p>{@summary Tools and constants for the JavaDoc extension.}</p>
 *  <p>Originally, this tool was developed with lots of dependencies to the
 *  other <i>Foundation</i> libraries; the static stuff from
 *  {@code foundation-base} and {@code foundation-util} was placed into this
 *  class.</p>
 *
 *  @version $Id: ToolKit.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 */
@API( status = INTERNAL, since = "0.1.0" )
@ClassVersion( sourceVersion = "$Id: ToolKit.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@UtilityClass
public final class ToolKit
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary Implementations of this interface provides the sort order
     *  key from the given instance of the type.}</p>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: ToolKit.java 1282 2026-09-08 23:52:53Z tquadrat $
     *  @since 0.0.5
     *
     *  @param  <T> The type to order.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     */
    @API( status = STABLE, since = "0.0.5" )
    @ClassVersion( sourceVersion = "$Id: ToolKit.java 1282 2026-09-08 23:52:53Z tquadrat $" )
    @FunctionalInterface
    public interface KeyProvider<T,K>
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  <p>{@summary Returns the sort order key for the given instance.}</p>
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
     *  <p>{@summary The lead-in for a CDATA wrapped String: {@value}}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String CDATA_LEADIN = "<![CDATA[";

    /**
     *  <p>{@summary The lead-out for a CDATA wrapped String: {@value}}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String CDATA_LEADOUT = "]]>";

    /**
     *  <p>{@summary The hyphen character
     *  (&amp;#8208;/&amp;#x2010;/\u2010/HYPHEN).}</p>
     *  <p>This is different from the character '&#x002D;' (HYPHEN-MINUS),
     *  although it looks similar. This character can be used as a replacement
     *  for the HYPHEN-MINUS in contexts where HYPHEN-MINUS has a special
     *  meaning.</p>
     */
    /*
     * For some reason, Javadoc refuses to accept both &#8208; and &#x2010;
     * as valid entities.
     */
    @SuppressWarnings( "UnnecessaryUnicodeEscape" )
    @API( status = STABLE, since = "0.0.5" )
    public static final char CHAR_HYPHEN = '\u2010';

    /**
     *  <p>{@summary The zero-width non-breaking space character; in fact, the
     *  'word joiner' character that should be used instead of the original
     *  character.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final char CHAR_ZWNBSP = '\u2060';

    /**
     *  <p>{@summary Some methods in this class need a buffer; the size of this
     *  buffer is defined here: {@value}.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     *  <p>{@summary An empty array of
     *  {@link String}
     *  instances.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] EMPTY_String_ARRAY = new String [0];

    /**
     *  <p>{@summary The index value indicating that nothing was found:
     *  {@value}.} See for example
     *  {@link String#indexOf(int)}.</p>
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final int NOT_FOUND = -1;

    /**
     *  <p>{@summary The attribute name for an XML attribute holding a class
     *  name: {@value}.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLATTRIBUTE_Class = "class";

    /**
     *  <p>{@summary The attribute name for the XML id attribute: {@value}.}</p>
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
     *  <p>{@summary The attribute name for the XML space attribute:
     *  {@value}.}</p>
     *  <p>This reserved attribute indicates whether any whitespace inside the
     *  element is significant and should not be altered by the XML processor.
     *  The attribute can take one of two enumerated values:</p>
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
     *  <p>{@summary The element name for an XML element holding text: {@value}.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLELEMENT_Text = "text";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary The empty string.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String EMPTY_STRING;

    /**
     *  <p>{@summary The null character.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final char NULL_CHAR;

    /**
     *  <p>{@summary A String containing the sequence &quot;null&quot;.}</p>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String NULL_STRING;

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
     *  <p>{@summary No instance allowed for this class.}</p>
     */
    private ToolKit() { throw new PrivateConstructorForStaticClassCalledError( ToolKit.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Creates the format String for a line number, based on the
     *  highest possible value.}</p>
     *
     *  @param  maxValue    The highest possible value.
     *  @return The format String.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String createLineNumberFormatString( final int maxValue )
    {
        if( maxValue < 0 ) throw new ValidationException( format( "Invalid value: %d", maxValue ) );
        final var retValue = format( "<span class=\"source-line-no\">%%0%dd</span>", round( floor( log10( maxValue ) + 1.0 ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createLineNumberFormatString()

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
     *  For some unknown reasons, Javadoc will not accept the entities &#x7403;
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
     *  <p>{@summary Escapes the characters in a {@code String} using HTML
     *  entities and writes them to an
     *  {@link Appendable}.}
     *  For details, refer to
     *  {@link #escapeHTML(CharSequence)}.</p>
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
     *  <p>{@summary Returns the first characters from the given String.}</p>
     *
     *  @param  count   The number of characters to return.
     *  @param  s   The string.
     *  @return The first characters from the given String as specified by
     *      {@code count}.
     */
    public static final String first( final int count, final String s )
    {
        final var len = requireNonNullArgument( s, "s" ).length();
        if( count < 1 ) throw new IllegalArgumentException( "count is less than 1" );
        final var retValue = s.substring( 0, min( count, len ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  first()

    /**
     *  <p>{@summary Tests if the given String is {@code null} or the empty
     *  String.}</p>
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
     *  <p>{@summary Tests if the given String is {@code null}, the empty String, or just
     *  containing whitespace.}</p>
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
     *  <p>{@summary Tests if the given String is not {@code null} and not the
     *  empty String.}</p>
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
     *  <p>{@summary Tests if the given String is not {@code null}, not the
     *  empty String, and that it contains other characters than just
     *  whitespace.}</p>
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
     *  <p>{@summary Sometimes a special sort order is required that cannot be
     *  defined as a rule. Instead, a list defines the sequence. This method
     *  creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.} Values that are not on
     *  that list will be placed to the end, ordered according to their natural
     *  order if they or their keys implement the
     *  {@link Comparable}
     *  interface, or without any specific order.</p>
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
     *  <p>{@summary Sometimes a special sort order is required that cannot be
     *  defined as a rule. Instead, a list defines the sequence. This method
     *  creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.}</p>
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
     *  <p>{@summary Sometimes a special sort order is required that cannot be
     *  defined as a rule. Instead, a list defines the sequence. This method
     *  creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.}</p>
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
    @API( status = INTERNAL, since = "0.0.5" )
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
     *  <p>{@summary Returns the argument if that is not {@code null},
     *  otherwise the replacement value.}</p>
     *  <p>The method returns {@code null} if both {@code argument} and
     *  {@code replacement} are {@code null}.</p>
     *
     *  @param  <T> The type of the argument and the replacement value.
     *  @param  argument    The argument.
     *  @param  replacement The replacement value.
     *  @return The argument or the replacement if the argument is
     *      {@code null}.
     */
    public static final <T> T mapFromNull( final T argument, final T replacement )
    {
        final var retValue = nonNull( argument ) ? argument : replacement;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  mapFromNull()

    /**
     *  <p>{@summary Determines the maximum length over all Strings provided in the given
     *  {@link Stream}.}</p>
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
     *  <p>{@summary Determines the maximum length over all strings provided in the given
     *  {@link Collection}.}</p>
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
     *  <p>{@summary Checks if the given argument {@code a} is {@code null} and throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}.}</p>
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
     *  <p>{@summary Checks if the given argument {@code a} is {@code null} or
     *  empty and throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}, or a
     *  {@link EmptyArgumentException}
     *  if it is empty.}</p>
     *  <p>Strings, arrays,
     *  {@link Collection}s, and
     *  {@link Map}s
     *  will be checked on being empty; this includes instances of
     *  {@link StringBuilder},
     *  {@link StringBuffer},
     *  and
     *  {@link CharSequence}.</p>
     *  <p>For an instance of
     *  {@link Optional},
     *  the presence of a value is checked in order to determine whether it is
     *  empty or not.</p>
     *  <p>Because the interface
     *  {@link java.util.Enumeration}
     *  does not provide an API for the check on emptiness
     *  ({@link java.util.Enumeration#hasMoreElements() hasMoreElements()}
     *  will return {@code false} after all elements have been taken from
     *  the {@code Enumeration} instance), the result for arguments of this
     *  type has to be taken with caution.</p>
     *  <p>For instances of
     *  {@link java.util.stream.Stream},
     *  this method will only check for {@code null} (like
     *  {@link #requireNonNullArgument(Object,String)}).
     *  This is because any operation on the stream would render it unusable
     *  for later processing.</p>
     *  <p>In case the argument is of type
     *  {@link Optional},
     *  this method behaves different from
     *  {@link #requireNotEmptyArgument(Optional,String)};
     *  this one will return the {@code Optional} instance, while the other
     *  method will return its contents.</p>
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
     *  <p>{@summary Checks if the given argument {@code a} of type
     *  {@link Optional}
     *  is {@code null} or empty and
     *  throws a
     *  {@link NullArgumentException}
     *  if it is {@code null}, or a
     *  {@link EmptyArgumentException}
     *  if it is empty.}</p>
     *  <p>Otherwise it returns the value of the {@code Optional}.</p>
     *  <p>This is different from the behaviour of
     *  {@link #requireNotEmptyArgument(Object,String)}
     *  with an instance of {@code Optional} as the argument to test.</p>
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
     *  <p>{@summary Splits a String by the given separator character and
     *  returns an array of all parts.} In case a separator character is
     *  immediately followed by another separator char, an empty String will be
     *  placed to the array. Beginning and end of the String are treated as
     *  separators, so if the first character of the String is a separator, the
     *  returned array will start with an empty string. It will end with an
     *  empty String if the last character is a separator.</p>
     *  <p>In case the String is empty, the return value will be an array
     *  containing just the empty String.</p>
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
     *  <p>{@summary Splits a String by the given separator character,
     *  identified by its Unicode code point, and returns an array of all
     *  parts.} In case a separator character is immediately followed by
     *  another separator char, an empty String will be placed to the array.
     *  Beginning and end of the String are treated as separators, so if the
     *  first character of the String is a separator, the returned array will
     *  start with an empty String. It will end with an empty String if the
     *  last character is a separator.</p>
     *  <p>In case the String is empty, the return value will be an array
     *  containing just the empty String.</p>
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
     *  returned {@code Stream} will start with an empty String. It will end
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