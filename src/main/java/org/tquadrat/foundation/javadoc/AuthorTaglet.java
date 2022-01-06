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
import static org.tquadrat.foundation.javadoc.internal.Common.parseNameAndEmail;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary This taglet replaces the standard {@code @author} taglet. It
 *  is different in the way that it provides the author's email address as a
 *  hyperlink.}</p>
 *  <p>This means that the tag requires the reference to the author in the
 *  format below:</p>
 *  <pre><code>  &#x40;extauthor &lt;<i>name</i>&gt; <b>-</b> &lt;<i>email address</i>&gt;</code></pre>
 *  <p>Basically, this is the name of the author, followed by their email
 *  address, separated by a hyphen (&quot;&#x2d;&quot; or &amp;#x2d;),
 *  surrounded by blanks.</p>
 *  <p>If there is no email address, the output is the same as for the
 *  standard {@code @author} taglet.</p>
 *  <p>Previously (before Java&nbsp;15), it was possible to
 *  &quot;overwrite&quot; the default {@code @author} tag by a custom
 *  implementation. Unfortunately, this does not work any longer: when
 *  providing a custom tag with the same name, no author is added to the output
 *  at all.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AuthorTaglet.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: AuthorTaglet.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class AuthorTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "extauthor";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code AuthorTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public AuthorTaglet() { /* Just exists */ }

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
     *
     *  @see jdk.javadoc.doclet.Taglet#getName()
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
        final var caption = tags.size() > 1 ? "Authors" : "Author";
        final var template =
            """

            <dt><span class="simpleTagLabel">%1$s:</span></dt>
              <dd>%2$s</dd>
            """;
        final var retValue = format( template, caption, parseNameAndEmail( this, tags ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class AuthorTaglet

/*
 *  End of File
 */