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

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.STATIC;
import static org.apiguardian.api.API.Status.INTERNAL;

import javax.lang.model.element.VariableElement;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.svg.SVGText;

/**
 *  The wrapper for a
 *  {@link VariableElement}
 *  instance, enhanced by information needed for the creation of the UML graph.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLVariableElement.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: UMLVariableElement.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLVariableElement extends UMLMemberElement implements VariableElement
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code UMLVariableElement} objects.
     */
    public static final UMLVariableElement [] EMPTY_UMLVariableElement_ARRAY = new UMLVariableElement [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The wrapped variable element.
     */
    private final VariableElement m_Wrapped;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLVariableElement} instance.
     *
     *  @param  element The wrapped variable element.
     */
    public UMLVariableElement( final VariableElement element )
    {
        super( element );
        m_Wrapped = element; // Null check is done by super constructor.
    }   //  UMLVariableElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Object getConstantValue() { return m_Wrapped.getConstantValue(); }

    /**
     *  Returns {@code true} if this variable is final.
     *
     *  @return {@code true} if this variable is final.
     */
    public final boolean isFinal()
    {
        final var retValue = getModifiers().contains( FINAL );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isFinal()

    /**
     *  Returns {@code true} if this variable is static.
     *
     *  @return {@code true} if this variable is static.
     */
    public final boolean isStatic()
    {
        final var retValue = getModifiers().contains( STATIC );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isStatic()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final SVGText renderSVGText()
    {
        final var retValue = super.renderSVGText();
        if( isStatic() ) retValue.setClass( "static" );
        if( isFinal() ) retValue.setClass( "final" );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  renderSVGText()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var type = asType();

        final var retValue = determineVisibility() +
            getSimpleName() +
            ":" +
            type.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class UMLVariableElement

/*
 *  End of File
 */