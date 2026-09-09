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
 *  <p>{@summary This inline tag inserts a hyperlink to an external URL into
 *  the documentation.} It can be used as this:</p>
 *  <pre><code>&hellip; {&#64;href &lt;<i>url</i>&gt; &lt;<i>text</i>&gt;} &hellip;</code></pre>
 *  <p>That marks &lt;text&gt; as a link to the given URL; if &lt;text&gt; is
 *  omitted, the URL itself is shown instead.</p>
 *  <p>&lt;text&gt; is written to the output as is; that means that HTML tags
 *  do work, but Javadoc tags are not parsed properly.</p>
 *  <p>The {@code {@docRoot}} is supported for the URL.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HRefTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: HRefTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class HRefTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "href";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary The pattern for the tag with both URL and contents.}</p>
     */
    private static final Pattern m_ParsePattern;

    static
    {
        try
        {
            m_ParsePattern = Pattern.compile( "(?<url>.*?) (?<contents>.*)" );
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
     *  <p>{@summary Creates a new {@code HRefTaglet} instance.}</p>
     */
    public HRefTaglet()
    {
        super( TAGLET_NAME, true, Location.values() );
    }   //  HRefTaglet()

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
                final var matcher = m_ParsePattern.matcher( arguments );
                if( matcher.matches() )
                {
                    final var url = processTagContent( parseText( matcher.group( "url" ) ), element );
                    final var label = processTagContent( parseText( matcher.group( "contents" ) ), element );
                    buffer.add( composeLink( url, label ) );
                }
                else
                {
                    final var url = processTagContent( parseText( arguments ), element );
                    buffer.add( composeLink( url, url ) );
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
//  class HRefTaglet

/*
 *  End of File
 */