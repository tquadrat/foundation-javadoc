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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PathDefinition;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Path;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.cubicCurveTo;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.lineToAbs;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.moveToAbs;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.vLineTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGElementAdapter;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGPath;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGUserUnitValue;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement;

/**
 *  The representation of the lines that connects the type elements in a UML
 *  diagram. <br>
 *  <br>Instances of this class are used as if they are an instance of
 *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGPath SVGPath}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLConnector.java 991 2022-01-16 16:58:29Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLConnector.java 991 2022-01-16 16:58:29Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLConnector extends SVGElementAdapter
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The type for this connector.
     */
    @SuppressWarnings( "FieldCanBeLocal" )
    private final UMLConnectorType m_ConnectorType;

    /**
     *  The type symbol for the child (where the connector starts).
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final TypeSymbol m_Child;

    /**
     *  The end point for the path representing this connector.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private Point m_EndPoint = null;

    /**
     *  The type symbol for the parent (where the connector ends).
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final TypeSymbol m_Parent;

    /**
     *  The start point for the path representing this connector.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private Point m_StartPoint = null;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLConnector} instance.
     *
     *  @param  type    The type for this connector.
     *  @param  parent  The type symbol for the parent (where the connector
     *      ends).
     *  @param  child   The type symbol for the child (where the connector
     *      starts).
     */
    public UMLConnector( final UMLConnectorType type, final TypeSymbol parent, final TypeSymbol child )
    {
        super( SVGELEMENT_Path );

        m_ConnectorType = requireNonNullArgument( type, "type" );
        m_Child = requireNonNullArgument( child, "child" );
        m_Parent = requireNonNullArgument( parent, "parent" );
        setClass( type.getCSSClass() );
        super.setMarkerEnd( m_ConnectorType.getURI() );
    }   //  UMLConnector()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns this {@code UMLConnector} instance as an instance of
     *  {@link SVGPath}.
     *
     *  @return This instance.
     */
    public final SVGPath asSVGPath() { return this; }

    /**
     *  Composes the path that represents this UML connector.
     *
     *  @return The path definition.
     */
    private final SVGPathElement [] composePath()
    {
        final var startX = m_StartPoint.x();
        final var startY = m_StartPoint.y();
        final var endX = m_EndPoint.x();
        final var endY = m_EndPoint.y();

        final Collection<SVGPathElement> pathElements = new ArrayList<>();

        pathElements.add( moveToAbs( startX, startY ) );
        if( startX == endX )
        {
            //---* We have just a simple vertical line *-----------------------
            pathElements.add( lineToAbs( endX, endY ) );
        }
        else
        {
            final var lenFix = 15;
            final var deltaX = endX - startX;
            final var deltaY = endY - startY;

            //---* The line is a bit more complex *----------------------------
            pathElements.add( cubicCurveTo( 0.0, deltaY + lenFix, deltaX, 0.0, deltaX, deltaY + lenFix ) );
            pathElements.add( vLineTo( -lenFix ) );
        }

        final var retValue = pathElements.toArray( SVGPathElement []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composePath()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Map<String,String> getAttributes()
    {
        //---* Update the path definition *------------------------------------
        /*
         * The original implementation of SVGPath.setPatDefinition() appends
         * the new path elements to the already existing ones, but we want to
         * replace an existing path with the new one. Therefore, we use here
         * NO_APPEND in the call to setAttribute().
         */
        setAttribute( SVGATTRIBUTE_PathDefinition, SVGPathElement.toString( composePath() ), NO_APPEND );

        //---* Now get the attributes *----------------------------------------
        final var retValue = super.getAttributes();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getAttributes()

    /**
     *  Returns the type symbol where the connector starts.
     *
     *  @return The child's type symbol.
     */
    public final TypeSymbol getChildTypeSymbol() { return m_Child; }

    /**
     *  Returns the type symbol where the connector ends.
     *
     *  @return The parent's type symbol.
     */
    public final TypeSymbol getParentTypeSymbol() { return m_Parent; }

    /**
     *  Sets the end point for the path representing this connector.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     */
    public final void setEndPoint( final double x, final double y ) { m_EndPoint = new Point( x, y ); }

    /**
     *  The markers for the path representing this UML connector are set
     *  internally, so this method will do nothing.
     *
     *  @param  ignored Ignored!
     */
    @Override
    public final void setMarkerEnd( final URI ignored ) { /* Does nothing */ }

    /**
     *  The markers for the path representing this UML connector are set
     *  internally, so this method will do nothing.
     *
     *  @param  ignored Ignored!
     */
    @Override
    public final void setMarkerMid( final URI ignored ) { /* Does nothing */ }

    /**
     *  The markers for the path representing this UML connector are set
     *  internally, so this method will do nothing.
     *
     *  @param  ignored Ignored!
     */
    @Override
    public final void setMarkerStart( final URI ignored ) { /* Does nothing */ }

    /**
     *  The definition of the path for this UML connector is done internally,
     *  so calling this method does not have any effect.
     *
     *  @param  ignored Ignored!
     */
    @Override
    public final void setPathDefinition( final SVGPathElement... ignored ) { /* Does nothing */ }

    /**
     *  The definition of the path for this UML connector is done internally,
     *  so calling this method does not have any effect.
     *
     *  @param  ignored Ignored!
     */
    @Override
    public final void setPathLength( final SVGUserUnitValue ignored ) { /* Does nothing */ }

    /**
     *  Sets the start point for the path representing this connector.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     */
    public final void setStartPoint( final double x, final double y ) { m_StartPoint = new Point( x, y ); }
}
//  class UMLConnector

/*
 *  End of File
 */