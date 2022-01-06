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

import java.net.URI;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The types for a
 *  {@link UMLConnector}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLConnectorType.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLConnectorType.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public enum UMLConnectorType
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  The connector represents an implementation.
     */
    IMPLEMENTATION( "implementation", "implementation" ),

    /**
     *  The connector represents an inheritance.
     */
    INHERITANCE( "inheritance", "inheritance" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The CSS class for the connector representation.
     */
    private final String m_CSSClass;

    /**
     *  The SVG element id for the respective SVG {@code <marker>} element.
     */
    private final String m_Id;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLConnectorType} instance.
     *
     *  @param  id  The id for the SVG {@code <marker>} element.
     *  @param  cssClass    The CSS class for the connector representation.
     */
    private UMLConnectorType( final String id, final String cssClass )
    {
        m_CSSClass = cssClass;
        m_Id = id;
    }   //  UMLConnectorType()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the CSS class for the SVG element representing the UML
     *  connector.
     *
     *  @return The CSS class.
     */
    public final String getCSSClass() { return m_CSSClass; }

    /**
     *  Returns the SVG element id for the respective SVG {@code <marker>}
     *  element.
     *
     *  @return The id.
     */
    public final String getId() { return m_Id; }

    /**
     *  Returns the
     *  {@link URI}
     *  that can be used to reference the respective SVG {@code <marker>}
     *  element from inside the same SVG document.
     *
     *  @return The URI.
     */
    public final URI getURI() { return URI.create( "#" + getId() ); }
}
//  enum UMLConnectorType

/*
 *  End of File
 */