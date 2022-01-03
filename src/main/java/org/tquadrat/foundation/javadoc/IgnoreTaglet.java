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
import static org.tquadrat.foundation.javadoc.internal.Common.initHelperTaglets;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

/**
 *  The contents of this inline tag will be ignored for the generation of the
 *  Javadoc output. Different from the <code>&#64;hidden</code>&quot; tag that
 *  excludes the whole comment block from the generated documentation, this tag
 *  will just exclude the part with in the brackets.<br>
 *  <br>So for example, the sequence &quot;<code>{&#64;{@value #TAGLET_NAME}
 *  This text will not appear in the documentation}</code>&quot; would just not
 *  show up. This is sometimes useful when the documentation output needs to be
 *  prepared in some special way that makes it nearly unreadable in the source
 *  code.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IgnoreTaglet.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: IgnoreTaglet.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public class IgnoreTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "ignore";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code IgnoreTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public IgnoreTaglet() { /* Just exists */ }

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
    public final void init( final DocletEnvironment docletEnvironment, final Doclet doclet )
    {
        Taglet.super.init( docletEnvironment, doclet );
        initHelperTaglets( docletEnvironment, doclet );
    }   //  init()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isInlineTag() { return true; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        return EMPTY_STRING;
    }   //  toString()
}
//  class IgnoreTaglet

/*
 *  End of File
 */