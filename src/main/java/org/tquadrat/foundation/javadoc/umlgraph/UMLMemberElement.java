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

package org.tquadrat.foundation.javadoc.umlgraph;

import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.svg.SVGUtils.createText;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.svg.SVGText;

/**
 *  The wrapper for an
 *  {@link Element}
 *  instance that is contained in a
 *  {@link TypeElement},
 *  enhanced by information needed for the creation of the UML graph.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLMemberElement.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
*
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: UMLMemberElement.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public abstract sealed class UMLMemberElement extends UMLElement
    permits UMLExecutableElement, UMLVariableElement
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code UMLMemberElement} objects.
     */
    public static final UMLMemberElement [] EMPTY_UMLMemberElement_ARRAY = new UMLMemberElement [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The
     *  {@link SVGText}
     *  element that display this member in the UML class diagram.
     */
    private final Lazy<SVGText> m_Text;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLMemberElement} instance.
     *
     *  @param  element The wrapped element.
     */
    protected UMLMemberElement( final Element element )
    {
        super( element );
        m_Text = Lazy.use( this::renderSVGText );
    }   //  UMLMemberElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Determines the visibility token for this UML element.
     *
     *  @return The visibility token: '+' for a public element, '#' for a
     *      protected one, '-' for a private, and '~' for package private
     *      element.
     */
    protected final String determineVisibility()
    {
        var retValue = EMPTY_STRING;
        final var modifiers = getModifiers();

        //noinspection IfStatementWithTooManyBranches
        if( modifiers.contains( PUBLIC ) )
        {
            retValue = modifiers.contains( DEFAULT ) ? "\u2A22 " : "+ ";
        }
        else if( modifiers.contains( PROTECTED ) )
        {
            retValue = "# ";
        }
        else if( modifiers.contains( PRIVATE ) )
        {
            retValue = "- ";
        }
        else
        {
            //---* The element is package private *----------------------------
            retValue = "~ ";
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineVisibility()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final TypeElement getEnclosingElement() { return (TypeElement) super.getEnclosingElement(); }

    /**
     *  Renders
     *  {@link SVGText}
     *  element that will display this member.
     *
     *  @return The text element.
     */
    protected SVGText renderSVGText()
    {
        final var retValue = createText( toString() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  renderSVGText()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract String toString();

    /**
     *  Returns the
     *  {@link SVGText}
     *  element that will display this member.
     *
     *  @return The text element.
     */
    public final SVGText toText() { return m_Text.get(); }
}
//  class UMLMemberElement

/*
 *  End of File
 */