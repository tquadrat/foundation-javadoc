/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  This inline tag inserts a hyperlink to an external URL into the
 *  documentation.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HRefTaglet.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: HRefTaglet.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class HRefTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "href";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern for the tag with both URL and contents.
     */
    private static final Pattern PATTERN_FULL;

    /**
     *  The pattern for the tag with only the URL.
     */
    private static final Pattern PATTERN_SHORT;

    static
    {
        try
        {
            //noinspection RegExpRedundantEscape
            PATTERN_FULL = Pattern.compile( "\\{@" + TAGLET_NAME + " (?<url>.*?) (?<contents>.*)\\}" );
            //noinspection RegExpRedundantEscape
            PATTERN_SHORT = Pattern.compile( "\\{@" + TAGLET_NAME + " (?<url>.*)\\}" );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code HRefTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public HRefTaglet() { /* Just exists */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Location> getAllowedLocations() { return EnumSet.allOf( Location.class ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getName() { return TAGLET_NAME; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isInlineTag() { return true; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var tag = tags.get( 0 ).toString();
        var matcher = PATTERN_FULL.matcher( tag );
        final String retValue;
        if( matcher.matches() )
        {
            retValue = format( "<a href=\"%2$s\">%1$s</a>", matcher.group( "contents" ), matcher.group( "url" ) );
        }
        else
        {
            matcher = PATTERN_SHORT.matcher( tag );
            retValue = matcher.matches() ? format( "<a href=\"%1$s\"><code>%1$s</code></a>", matcher.group( "url" ) ) : tag;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class HRefTaglet

/*
 *  End of File
 */