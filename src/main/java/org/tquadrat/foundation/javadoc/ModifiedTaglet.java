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
import static org.apiguardian.api.API.Status.STABLE;

import javax.lang.model.element.Element;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary This taglet is used to refer to the author of the
 *  modifications for a piece of code that was originally written by somebody
 *  else (named in the {@code @author} or the
 * {@link AuthorTaglet @extauthor}
 * tag).} It requires that reference to the editor in the format below:</p>
 *  <pre><code>  &#x40;modified &lt;<i>name</i>&gt; <b>-</b> &lt;<i>email address</i>&gt;</code></pre>
 *  <p>Basically, this is the name of the editor, followed by their email
 *  address, separated by a hyphen (&quot;&#x2d;&quot; &amp;#x2d), surrounded
 *  by blanks.</p>
 *  <p>If there is no email address, just the name will be shown in the
 *  output.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ModifiedTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: ModifiedTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class ModifiedTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "modified";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code ModifiedTaglet} instance.}</p>
     */
    public ModifiedTaglet()
    {
        super( TAGLET_NAME, false, Location.values() );
    }   //  ModifiedTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var template =
            """
            
            <dt><span class="simpleTagLabel">Modified by:</span></dt>
              <dd>%s</dd>
            """;
        final var retValue = format( template, parseNameAndEmail( tags ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ModifiedTaglet

/*
 *  End of File
 */