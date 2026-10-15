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

import static jdk.javadoc.doclet.Taglet.Location.CONSTRUCTOR;
import static jdk.javadoc.doclet.Taglet.Location.FIELD;
import static jdk.javadoc.doclet.Taglet.Location.METHOD;
import static jdk.javadoc.doclet.Taglet.Location.TYPE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.escapeHTML;

import javax.lang.model.element.Element;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary Replaces {&#64;class} with the name of the current class
 *  surrounded by <code>&lt;code&gt;</code>.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ClassTaglet.java 1291 2026-10-15 19:27:02Z tquadrat $
 *  @since 0.25.5
 */
@ClassVersion( sourceVersion = "$Id: ClassTaglet.java 1291 2026-10-15 19:27:02Z tquadrat $" )
@API( status = STABLE, since = "0.25.5")
public final class ClassTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "class";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code ClassTaglet} instance.}</p>
     */
    public ClassTaglet()
    {
        super( TAGLET_NAME, true, TYPE, CONSTRUCTOR, METHOD, FIELD );
    }   //  ClassTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var className = retrieveClassNameForElement( element ).orElse( "<Undetermined Class>" );
        final var retValue = "<code>%s</code>".formatted( escapeHTML( className ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ClassTaglet

/*
 *  End of File
 */