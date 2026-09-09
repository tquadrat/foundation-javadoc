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

import static java.util.stream.Collectors.joining;
import static jdk.javadoc.doclet.Taglet.Location.METHOD;
import static jdk.javadoc.doclet.Taglet.Location.PACKAGE;
import static jdk.javadoc.doclet.Taglet.Location.TYPE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import javax.lang.model.element.Element;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownBlockTagTree;

/**
 *  <p>{@summary This taglet is used to add a reference to a document of some
 *  kind that inspired the current piece of code, either in its functionality
 *  or its implementation.}</p>
 *  <p>The {@code @inspired} tag is allowed on {@code package} level,
 *  {@code class} level, and for methods.</p>
 *  <p>This tag does not allow the inline tags</p>
 *  <ul>
 *      <li>{@code @docRoot}</li>
 *      <li>{@code @index}</li>
 *      <li>{@code @inheritDoc}</li>
 *      <li>{@code @link}</li>
 *      <li>{@code @linkplain}</li>
 *      <li>{@code @snippet}</li>
 *      <li>{@code @summary}</li>
 *      <li>{@code @systemProperty}</li>
 *      <li>{@code @value}</li>
 *  </ul>
 *  <p>from the standard doclet. Custom tags that are not defined by this
 *  taglet library will not work either.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InspiredTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: InspiredTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class InspiredTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "inspired";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code InspiredTaglet} instance.}</p>
     */
    public InspiredTaglet()
    {
        super( TAGLET_NAME, false, PACKAGE, TYPE, METHOD );
    }   //  InspiredTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    protected final void customInit()
    {
        forceInit();
    }   //  customInit()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var retValue = tags.stream()
            .map( v -> (UnknownBlockTagTree) v )
            .map( UnknownBlockTagTree::getContent )
            .map( v -> processTagContent( v, element ))
            .map( """
                
                <p>%s</p>"""::formatted
            )
            .collect( joining(
                EMPTY_STRING,
                """
                
                <dt><span class="simpleTagLabel">Inspired by:</span></dt>
                  <dd>
                """,
                """
                  </dd>
                """ ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class InspiredTaglet

/*
 *  End of File
 */