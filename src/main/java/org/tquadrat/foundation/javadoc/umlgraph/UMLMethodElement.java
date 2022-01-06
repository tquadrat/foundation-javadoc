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

import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.STATIC;
import static org.apiguardian.api.API.Status.INTERNAL;

import javax.lang.model.element.ExecutableElement;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGText;

/**
 *  The wrapper for an
 *  {@link ExecutableElement}
 *  instance that represents a constructor, enhanced by information needed for
 *  the creation of the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLMethodElement.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLMethodElement.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLMethodElement extends UMLExecutableElement
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLMethodElement} instance.
     *
     *  @param  element The wrapped executable element for the method.
     */
    public UMLMethodElement( final ExecutableElement element )
    {
        super( element );
        if( !isMethod( element ) ) throw new IllegalArgumentException( "Element does not represent a method" );
    }   //  UMLMethodElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns {@code true} if this method is abstract, either because it has
     *  that modifier or it is contained in an interface.
     *
     *  @return {@code true} if this method is abstract.
     */
    public final boolean isAbstract()
    {
        var retValue = getModifiers().contains( ABSTRACT );
        if( !retValue )
        {
            final var typeElement = getEnclosingElement();
            final var elementKind = typeElement.getKind();
            retValue = (elementKind == INTERFACE) && !getModifiers().contains( DEFAULT );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAbstract()

    /**
     *  Returns {@code true} if this method is static.
     *
     *  @return {@code true} if this method is static.
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
        if( isAbstract() )
        {
            retValue.setClass( "abstract" );
        }
        if( isStatic() )
        {
            retValue.setClass( "static" );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  renderSVGText()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue = new StringBuilder();
        final var returnType = getReturnType();
        retValue.append( determineVisibility() )
            .append( getSimpleName() )
            .append( getParametersList() )
            .append( ":" )
            .append( returnType.toString() );

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  toString()
}
//  class UMLMethodElement

/*
 *  End of File
 */