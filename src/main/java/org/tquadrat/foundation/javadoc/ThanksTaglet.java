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
 *  <p>{@summary This taglet is used to refer to the author of the model for
 *  the current piece of code or the person who provided the idea for that
 *  code.} It requires that reference to that person in the format below:</p>
 *  <pre><code>  &#x40;thanks &lt;<i>name</i>&gt; <b>-</b> &lt;<i>email address</i>&gt;</code></pre>
 *  <p>Basically, this is the name of the author, followed by their email
 *  address, separated by a hyphen (&quot;&#x2d;&quot; &amp;#x2d), surrounded
 *  by blanks.</p>
 *  <p>If there is no email address, just the name will be shown in the
 *  output.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ThanksTaglet.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: ThanksTaglet.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class ThanksTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "thanks";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ThanksTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public ThanksTaglet() { /* Just exists */ }

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

            <dt><span class="simpleTagLabel">Thanks to:</span></dt>
              <dd>%s</dd>
            """;

        final var retValue = format( template, parseNameAndEmail( this, tags ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ThanksTaglet

/*
 *  End of File
 */