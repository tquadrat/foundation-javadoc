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

import static org.apiguardian.api.API.Status.INTERNAL;

import javax.lang.model.element.ExecutableElement;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The wrapper for an
 *  {@link ExecutableElement}
 *  instance that represents a constructor, enhanced by information needed for
 *  the creation of the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLConstructorElement.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLConstructorElement.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLConstructorElement extends UMLExecutableElement
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLConstructorElement} instance.
     *
     *  @param  element The wrapped executable element for the constructor.
     */
    public UMLConstructorElement( final ExecutableElement element )
    {
        super( element );
        if( !isConstructor( element ) ) throw new IllegalArgumentException( "Element does not represent a constructor" );
    }   //  UMLConstructorElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var owningClass = getEnclosingElement();

        final var retValue = determineVisibility() +
            owningClass.getSimpleName() +
            getParametersList();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class UMLConstructorElement

/*
 *  End of File
 */