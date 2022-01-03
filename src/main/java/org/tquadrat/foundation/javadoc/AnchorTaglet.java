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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.util.StringUtils.format;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  This inline tag inserts an HTML anchor into the documentation that can be
 *  used for references to the location of that anchor. It can be used as this:
 *  <pre><code>&hellip; {&#64;anchor #&lt;<i>anchor</i>&gt; &lt;<i>text</i>&gt;} &hellip;</code></pre>
 *  That allows to reference the &lt;text&gt; by the anchor &lt;anchor&gt;
 *  like this:
 *  <pre><code>&hellip; &lt;a href=&quot;#anchor&quot;&gt;&hellip;&lt;/a&gt; &hellip;</code></pre>
 *  Or, with the {@code href} tag:
 *  <pre><code>&hellip; {&#64;href #anchor &hellip;}</code></pre>
 *
 *  @note   The hash symbol (&quot;#&quot;) before the anchor name is
 *      mandatory!
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AnchorTaglet.java 890 2021-04-01 21:15:48Z tquadrat $
 *  @since 0.0.5
 *
 *  @see HRefTaglet
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: AnchorTaglet.java 890 2021-04-01 21:15:48Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class AnchorTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "anchor";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern for the tag.
     */
    private static final Pattern PATTERN;

    static
    {
        try
        {
            //noinspection RegExpRedundantEscape
            PATTERN = Pattern.compile( "\\{@" + TAGLET_NAME + " #(?<name>.*?) (?<contents>.*)\\}" );
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
     *  Creates a new {@code AnchorTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public AnchorTaglet() { /* Just exists */ }

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
        final var matcher = PATTERN.matcher( tag );
        final var retValue = matcher.matches() ? format( "<a id=\"%2$s\">%1$s</a>", matcher.group( "contents" ), matcher.group( "name" ) ) : tag;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class AnchorTaglet

/*
 *  End of File
 */