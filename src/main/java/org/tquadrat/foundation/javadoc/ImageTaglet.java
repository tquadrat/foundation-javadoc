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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.NOT_FOUND;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.first;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownInlineTagTree;

/**
 *  <p>{@summary This inline tag inserts an image reference to the
 *  documentation.} It can be used as this:</p>
 *  <pre><code>&hellip; {&#64;image &lt;<i>url</i>&gt; &lt;<i>attributes</i>&gt;} &hellip;</code></pre>
 *  <p>The URL points to the picture, and &lt;text&gt; contains addition
 *  attributes that will be added to the {@code <img>} tag.</p>
 *  <p>The {@code {@docRoot}} is supported for the URL.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ImageTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.25.2
 */
@ClassVersion( sourceVersion = "$Id: ImageTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.25.2" )
public final class ImageTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "image";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code ImageTaglet} instance.}</p>
     */
    public ImageTaglet()
    {
        super( TAGLET_NAME, true, Location.values() );
    }   //  ImageTaglet()

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
                final var pos = arguments.indexOf( ' ' );
                if( pos == NOT_FOUND )
                {
                    buffer.add( "<img src=\"%s\">".formatted( processTagContent( parseText( arguments ), element ) ) );
                }
                else
                {
                    final var url = processTagContent( parseText( arguments.substring( 0, pos ) ), element );
                    final var attributes = arguments.substring( pos + 1 );
                    buffer.add( "<img src=\"%1$s\" %2$s>".formatted( url, attributes ) );
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
//  class ImageTaglet

/*
 *  End of File
 */