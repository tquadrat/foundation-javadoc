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
import static org.tquadrat.foundation.javadoc.internal.Common.parseNameAndEmail;
import static org.tquadrat.foundation.util.StringUtils.format;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  This taglet is used to refer to the author of the modifications to a piece
 *  of code that was originally written by somebody else (named in the
 *  {@code @author} or the
 *  {@link AuthorTaglet @extauthor}
 *  tag. It requires that reference to the editor in the format below:
 *  <pre><code>  &#x40;modified &lt;<i>name</i>&gt; <b>-</b> &lt;<i>email address</i>&gt;</code></pre>
 *  Basically, this is the name of the editor, followed by their email address,
 *  separated by a hyphen (&quot;&#x2d;&quot; &amp;#x2d), surrounded by
 *  blanks.<br>
 *  <br>If there is no email address, just the name will be shown in the
 *  output.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ModifiedTaglet.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ModifiedTaglet.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class ModifiedTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "modified";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ModifiedTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public ModifiedTaglet() { /* Just exists */ }

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
    public final boolean isInlineTag() { return false; }

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
        final var retValue = format( template, parseNameAndEmail( this, tags ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ModifiedTaglet

/*
 *  End of File
 */