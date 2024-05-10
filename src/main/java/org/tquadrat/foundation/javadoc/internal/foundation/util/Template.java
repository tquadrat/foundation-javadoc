/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc.internal.foundation.util;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.regex.Pattern.compile;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.ImpossibleExceptionError;

/**
 *  <p>{@summary An instance of this class is basically a wrapper around a
 *  String that contains placeholders (&quot;Variables&quot;) in the form
 *  <code>${&lt;<i>name</i>&gt;}</code>, where &lt;<i>name</i> is the variable
 *  name.}</p>
 *  <p>The variables names are case-sensitive.</p>
 *  <p>Valid variable name may not contain other characters than the letters
 *  from 'a' to 'z' (upper case and lower case), the digits from '0' to '9' and
 *  the special characters underscore ('_') and dot ('.'), after an optional
 *  prefix character.</p>
 *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal sign
 *  ('='), the colon (':'), the percent sign ('%'), and the ampersand
 *  ('&amp;').</p>
 *  <p>The prefix character is part of the name.</p>
 *  <p>Finally, there is the single underscore that is allowed as a special
 *  variable.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Template.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.1.0
 */
@SuppressWarnings( "unused" )
@ClassVersion( sourceVersion = "$Id: Template.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class Template implements Serializable
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The regular expression to identify a variable in a char sequence:
     *  {@value}.
     *
     *  @see #findVariables(CharSequence)
     *  @see #findVariables()
     *  @see #isValidVariableName(CharSequence)
     *  @see #replaceVariable(CharSequence,Map...)
     *  @see #replaceVariable(Map...)
     *  @see #replaceVariable(CharSequence, Function)
     *  @see #replaceVariable(Function)
     *
     *  @since 0.1.0
     */
    @SuppressWarnings( "RegExpUnnecessaryNonCapturingGroup" )
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARIABLE_PATTERN = "\\$\\{((?:_)|(?:[~/=%:&]?\\p{IsAlphabetic}(?:\\p{IsAlphabetic}|\\d|_|.)*?))}";

    /**
     *  The template for variables: {@value}. The argument is the name of the
     *  variable itself; after an optional prefix character, it may not contain
     *  other characters than the letters from 'a' to 'z' (upper case and lower
     *  case), the digits from '0' to '9' and the special characters underscore
     *  ('_') and dot ('.').<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARIABLE_TEMPLATE = "${%1$s}";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The template text.
     */
    private final String m_TemplateText;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern that is used to identify a variable in a char sequence.
     *
     *  @see #replaceVariable(CharSequence, Map...)
     *  @see #replaceVariable(Map...)
     *  @see #findVariables(CharSequence)
     *  @see #findVariables()
     *  @see #VARIABLE_PATTERN
     */
    private static final Pattern m_VariablePattern;

    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

    static
    {
        //---* The regex patterns *--------------------------------------------
        try
        {
            m_VariablePattern = compile( VARIABLE_PATTERN );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ImpossibleExceptionError( "The patterns are constant values that have been tested", e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code Template}.
     *
     *  @param  templateText    The template text, containing variable in the
     *      form <code>${&lt;<i>name</i>&gt;}</code>.
     */
    public Template( final CharSequence templateText )
    {
        m_TemplateText = requireNonNullArgument( templateText, "templateText" ).toString();
    }   //  Template()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Escapes backslash ('\') and dollar sign ('$') for regex replacements.
     *
     *  @param  s   The source string.
     *  @return The string with the escaped characters.
     *
     *  @see java.util.regex.Matcher#appendReplacement(StringBuffer,String)
     *
     *  @since 0.1.0
     */
    @API( status = INTERNAL, since = "0.1.0" )
    private static String escapeRegexReplacement( final CharSequence s )
    {
        assert nonNull( s ) : "s is null";

        //---* Escape the backslashes and dollar signs *-------------------
        final var len = s.length();
        final var retValue = new StringBuilder( (len * 12) / 10 );
        char c;
        EscapeLoop: for( var i = 0; i < len; ++i )
        {
            c = s.charAt( i );
            switch( c )
            {
                case '\\':
                case '$':
                    retValue.append( '\\' ); // The fall through is intended here!
                    //$FALL-THROUGH$
                default: // Do nothing ...
            }
            retValue.append( c );
        }   //  EscapeLoop:

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  escapeRegexReplacement()

    /**
     *  Collects all the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the given String.<br>
     *  <br>If there are not any variables in the given String, an empty
     *  {@link Set}
     *  will be returned.<br>
     *  <br>A valid variable name may not contain any other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @return A {@code Collection} with the variable (names).
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Set<String> findVariables( final CharSequence text )
    {
        final Collection<String> buffer = new HashSet<>();
        if( nonNull( text ) )
        {
            final var matcher = m_VariablePattern.matcher( text );
            String found;
            while( matcher.find() )
            {
                found = matcher.group( 1 );
                buffer.add( found );
            }
        }
        final var retValue = Set.copyOf( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findVariables()

    /**
     *  Collects all the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the adjusted template.<br>
     *  <br>If there are not any variables in there, an empty
     *  {@link Collection}
     *  will be returned.<br>
     *  <br>A valid variable name may not contain any other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @return A {@code Collection} with the variable (names).
     *
     *  @see #VARIABLE_PATTERN
     */
    public final Set<String> findVariables()
    {
        final var retValue = findVariables( m_TemplateText );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findVariables()

    /**
     *  Checks whether the adjusted template contains the variable of
     *  the form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern
     *  given in
     *  {@link #VARIABLE_PATTERN})
     *  with the given name.
     *
     *  @param  name    The name of the variable to look for.
     *  @return {@code true} if the template contains the variable,
     *      {@code false} otherwise.
     *  @throws IllegalArgumentException    The given argument is not valid as
     *      a variable name.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final boolean hasVariable( final String name )
    {
        if( !isValidVariableName( name ) ) throw new IllegalArgumentException( format( "%s is not a valid variable name", name ) );

        final var retValue = findVariables().contains( name );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasVariable()

    /**
     *  Checks whether the given String contains at least one variable of the
     *  form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern given in
     *  {@link #VARIABLE_PATTERN}).
     *
     *  @param  s   The String to test; can be {@code null}.
     *  @return {@code true} if the String contains at least one variable,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean hasVariables( final CharSequence s )
    {
        final var retValue = isNotEmptyOrBlank( s ) && m_VariablePattern.matcher( s ).find();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasVariables()

    /**
     *  Checks whether the adjusted template contains at least one variable of
     *  the form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern
     *  given in
     *  {@link #VARIABLE_PATTERN}).
     *
     *  @return {@code true} if the template contains at least one variable,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final boolean hasVariables() { return hasVariables( m_TemplateText ); }

    /**
     *  Test whether the given String is a valid variable name.
     *
     *  @param  name    The bare variable name, without the surrounding
     *      &quot;${&hellip;}&quot;.
     *  @return {@code true} if the given name is valid for a variable name,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     *  @see #findVariables(CharSequence)
     *  @see #replaceVariable(CharSequence, Map...)
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean isValidVariableName( final CharSequence name )
    {
        var retValue = isNotEmptyOrBlank( requireNonNullArgument( name, "name" ) );
        if( retValue )
        {
            final var text = format( VARIABLE_TEMPLATE, name );
            retValue = m_VariablePattern.matcher( text ).matches();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidVariableName()

    /**
     *  Checks whether the given String is a variable in the form
     *  <code>${<i>&lt;name&gt;</i>}</code>, according to the pattern provided
     *  in
     *  {@link #VARIABLE_PATTERN}.
     *
     *  @param  s   The String to test; can be {@code null}.
     *  @return {@code true} if the given String is not {@code null}, not the
     *      empty String, and it matches the given pattern, {@code false}
     *      otherwise.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean isVariable( final CharSequence s )
    {
        final var retValue = isNotEmptyOrBlank( s ) && m_VariablePattern.matcher( s ).matches();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isVariable()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the given String with values from the given maps. The method will
     *  try the maps in the given sequence.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.1.0" )
    public static final String replaceVariable( final CharSequence text, final Map<String,?>... sources )
    {
        requireNonNullArgument( sources, "sources" );

        final var retValue = replaceVariable( text, variable -> retrieveVariableValue( variable, sources ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the adjusted template with values from the given maps and returns it
     *  after formatting the result. The method will try the maps in the given
     *  sequence.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     */
    @SafeVarargs
    public final String replaceVariable( final Map<String,?>... sources )
    {
        final var retValue = replaceVariable( m_TemplateText, sources );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the adjusted template with values returned by the given retriever
     *  function for the variable name, and returns it after formatting the
     *  result.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If the retriever function returns a value that contains a variable
     *  itself, this will not be replaced.<br>
     *  <br>The retriever function will be called only once for each variable
     *  name; if the text contains the same variable multiple times, it will
     *  always be replaced with the same value.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable name may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  retriever   The function that will retrieve the replacement
     *      values for the given variable names.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final String replaceVariable( final Function<? super String, Optional<String>> retriever )
    {

        final var retValue = replaceVariable( m_TemplateText, retriever );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the given String with values returned by the given retriever
     *  function for the variable name.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If the retriever function returns a value that contains a variable
     *  itself, this will not be replaced.<br>
     *  <br>The retriever function will be called only once for each variable
     *  name; if the text contains the same variable multiple times, it will
     *  always be replaced with the same value.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable name may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  retriever   The function that will retrieve the replacement
     *      values for the given variable names.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String replaceVariable( final CharSequence text, final Function<? super String, Optional<String>> retriever )
    {
        requireNonNullArgument( retriever, "retriever" );

        final Map<String,String> cache = new HashMap<>();

        String retValue = null;
        if( nonNull( text ) )
        {
            final var matcher = m_VariablePattern.matcher( text );
            final var buffer = new StringBuilder();
            while( matcher.find() )
            {
                final var variable = matcher.group( 0 );
                final var replacement = cache.computeIfAbsent( variable, v -> escapeRegexReplacement( retriever.apply( matcher.group( 1 ) ).orElse( v ) ) );
                matcher.appendReplacement( buffer, replacement );
            }
            matcher.appendTail( buffer );
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  Tries to obtain a value for the given key from one of the given
     *  sources that will be searched in the given sequence order.
     *
     *  @param  name    The name of the value.
     *  @param  sources The maps with the values.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the value from one of the sources.
     */
    @SafeVarargs
    private static final Optional<String> retrieveVariableValue( final String name, final Map<String,?>... sources )
    {
        assert nonNull( name ) : "name is null";
        assert nonNull( sources ) : "sources is null";

        Optional<String> retValue = Optional.empty();

        //---* Search the sources *--------------------------------------------
        Object value = null;
        SearchLoop: for( final var map : sources )
        {
            value = map.get( name );
            if( nonNull( value ) ) break SearchLoop;
        }   //  SearchLoop:

        if( nonNull( value ) )
        {
            //---* Escape the backslashes and dollar signs *-------------------
            retValue = Optional.of( value.toString() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveVariableValue()
}
//  class Template

/*
 *  End of File
 */