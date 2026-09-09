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
 *  <p>With Java&nbsp;15 it was not possible to &quot;overwrite&quot; the
 *  default {@code @author} tag by a custom implementation: when providing a
 *  custom tag with the same name, no author was added to the output at all. To
 *  circumvent that, the {@code @extauthor} tag was introduced, implemented by
 *  {@link ExtAuthorTaglet}.
 *  With Java&nbsp;, &quot;overwriting&quot; is working again, and
 *  {@code @extauthor} exists only for historical reasons.</p>
 *  <p>You should not use both tags together, this would lead to two
 *  &quot;<b>Author:</b>&quot; sections in the documentation.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AuthorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: AuthorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed class AuthorTaglet extends CustomTagletBase
    permits ExtAuthorTaglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "author";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code AuthorTaglet} instance.}</p>
     */
    public AuthorTaglet()
    {
        this( TAGLET_NAME );
    }   //  AuthorTaglet()

    /**
     *  <p>{@summary Creates a new {@code AuthorTaglet} instance for the given
     *  taglet name.}</p>
     *
     *  @param  name    The name of the taglet.
     */
    protected AuthorTaglet( final String name )
    {
        super( name, false, Location.values() );
    }   //  AuthorTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
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
        final var retValue = format( template, caption, parseNameAndEmail( tags ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class AuthorTaglet

/*
 *  End of File
 */