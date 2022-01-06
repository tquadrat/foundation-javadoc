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
import static org.tquadrat.foundation.javadoc.internal.Common.processInlineTags;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary This taglet is used to add an important note to the
 *  documentation for an element.}</p>
 *  <p>Only the tags</p>
 *  <ul>
 *  <li>{@link org.tquadrat.foundation.javadoc.AnchorTaglet @anchor}</li>
 *  <li>{@code @code}</li>
 *  <li>{@link org.tquadrat.foundation.javadoc.HRefTaglet @href}</li>
 *  <li>{@code @index}</li>
 *  <li>{@code @literal}</li>
 *  <li>{@link org.tquadrat.foundation.javadoc.UnderlineTaglet @underline}</li>
 *  </ul>
 *  <p>are recognised in the text for this tag. If there are more than one
 *  occurrence for this tag in the comment, they are all merged into one single
 *  section.</p>
 *  <p>In particular the tags <code>{&#64;link}</code>,
 *  <code>{&#64;linkplain }</code> and <code>{&#64;value}</code> will
 *  <i>not</i> be interpreted inside a <code>{&#64;note}</code> block!</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NoteTaglet.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: NoteTaglet.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public class NoteTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "note";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The doclet.
     */
    @SuppressWarnings( {"unused", "FieldCanBeLocal"} )
    private Doclet m_Doclet;

    /**
     *  The doclet environment.
     */
    private DocletEnvironment m_DocletEnvironment;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code NoteTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public NoteTaglet() { /* Just exists */ }

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
        m_Doclet = doclet;
        m_DocletEnvironment = docletEnvironment;
        initHelperTaglets( docletEnvironment, doclet );
    }   //  init()

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
        final var docTreeFactory = m_DocletEnvironment.getDocTrees().getDocTreeFactory();
        final Function<CharSequence,Name> nameGenerator = m_DocletEnvironment.getElementUtils()::getName;

        final var prefixLen = getName().length() + 1;
        final var caption = tags.size() > 1 ? "Notes" : "Note";
        final var retValue = tags.stream()
            .map( Object::toString )
            .map( t -> t.substring( prefixLen ).trim() )
            .map( text -> "\n<li>" + processInlineTags( text, docTreeFactory, element, nameGenerator ) + "</li>" )
            .collect( Collectors.joining( "", "\n<dt><span class=\"simpleTagLabel\">" + caption + ":</span></dt>\n<dd>\n<ul>", "\n</ul>\n</dd>\n" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class NoteTaglet

/*
 *  End of File
 */