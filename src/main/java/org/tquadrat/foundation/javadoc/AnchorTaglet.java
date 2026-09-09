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

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static java.lang.String.join;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.first;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownInlineTagTree;

/**
 *  <p>{@summary This inline tag inserts an HTML anchor into the documentation
 *  that can be used for references to the location of that anchor.} It can be
 *  used as this:</p>
 *  <pre><code>&hellip; {&#64;anchor #&lt;<i>anchor</i>&gt; &lt;<i>text</i>&gt;} &hellip;</code></pre>
 *  <p>That allows to reference the &lt;text&gt; by the anchor &lt;anchor&gt;
 *  like this:</p>
 *  <pre><code>&hellip; &lt;a href=&quot;#anchor&quot;&gt;&hellip;&lt;/a&gt; &hellip;</code></pre>
 *  <p>Or, with the {@code href} tag:</p>
 *  <pre><code>&hellip; {&#64;href #anchor &hellip;}</code></pre>
 *  <p>The hash symbol (&quot;#&quot;) before the anchor name is mandatory!</p>
 *  <p>&lt;text&gt; will be written to the output as is; that means that HTML
 *  tags will work, but Javadoc tags are not parsed properly.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AnchorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 *
 *  @see HRefTaglet
 */
@ClassVersion( sourceVersion = "$Id: AnchorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class AnchorTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "anchor";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary The pattern for the tag.}</p>
     */
    private static final Pattern PATTERN;

    static
    {
        try
        {
            PATTERN = Pattern.compile( "#(?<name>.*?) (?<contents>.*)" );
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
     *  <p>{@summary Creates a new {@code AnchorTaglet} instance.}</p>
     */
    public AnchorTaglet()
    {
        super( TAGLET_NAME, true, Location.values() );
    }   //  AnchorTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final List<String> buffer = new ArrayList<>();
        for( final var tag : tags )
        {
            if( tag instanceof UnknownInlineTagTree inlineTagTree )
            {
                final var arguments = processTagContent( inlineTagTree.getContent(), element );
                final var matcher = PATTERN.matcher( arguments );
                if( matcher.matches() )
                {
                    final var id = matcher.group( "name" );
                    final var contents = processTagContent( parseText( matcher.group( "contents" ) ), element );
                    buffer.add( format( "<a id=\"%2$s\">%1$s</a>", contents, id ) );
                }
            }
            else
            {
                printf( ERROR, "Cannot process tag of '%s' (class '%s'): %s", tag.getKind().name(), tag.getClass().getName(), first( 20, tag.toString() ) );
            }
        }
        final var retValue = join( EMPTY_STRING, buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class AnchorTaglet

/*
 *  End of File
 */