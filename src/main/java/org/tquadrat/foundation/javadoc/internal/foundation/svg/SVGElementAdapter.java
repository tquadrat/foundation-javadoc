/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static java.lang.String.join;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.stream;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ClipPathUnits;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LengthAdjust;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerHeight;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerUnits;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerWidth;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Orientation;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PathDefinition;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Position;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ReferenceX;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ReferenceY;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Rotate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextLength;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_dx;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_dy;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x1;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x2;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y1;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y2;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGElementImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGMarkerOrientation;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGDegree;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement;

/**
 *  This is the base class for a custom type that wants to extend an SVG
 *  element with additional features.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGElementAdapter.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"AbstractClassExtendsConcreteClass", "OverlyCoupledClass"} )
@ClassVersion( sourceVersion = "$Id: SVGElementAdapter.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public abstract class SVGElementAdapter extends SVGElementImpl
    implements AllowsDocumentElementEventAttributes, AllowsDocumentEventAttributes, SVGClipPath, SVGGroup, SVGLine, SVGPath, SVGPositionedMarker, SVGRectangle, SVGStyle, SVGSymbol, SVGText, SVGTSpan, SVGUse
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The lines of the CSS style definitions.
     */
    private final List<String> m_StyleDefinitions = new ArrayList<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGElementAdapter} instance.
     *
     *  @param  elementName The name of the element.
     *  @param  flags   The flags that determine the behaviour of the new
     *      element.
     */
    protected SVGElementAdapter( final String elementName, final Flags... flags )
    {
        super( elementName, flags );
    }   //  SVGElementAdapter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public void addStyle( final CharSequence... styles )
    {
        for( final var style : requireNonNullArgument( styles, "styles" ) )
        {
            if( isNotEmptyOrBlank( style ) )
            {
                stream( style, '\n' ).forEach( m_StyleDefinitions::add );
            }
            else
            {
                m_StyleDefinitions.add( EMPTY_STRING );
            }
        }
    }   //  addStyle()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void defineLine( final SVGNumber x1, final SVGNumber y1, final SVGNumber x2, final SVGNumber y2 )
    {
        setStartPoint( x1, y1 );
        setEndPoint( x2, y2 );
    }   //  defineLine()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setPathLength( final double length )
    {
        setPathLength( number( length ) );
    }   //  setPathLength()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setPathLength( final long length )
    {
        setPathLength( number( length ) );
    }   //  setPathLength()

    /**
     *  {@inheritDoc}
     */
    @Override
    public String getStyleSheet()
    {
        final var retValue = join( "\n", m_StyleDefinitions );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getStyleSheet()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void merge( final SVGStyle other )
    {
        addStyle( other.getStyleSheet() );
    }   //  merge()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setClipPathUnits( final boolean flag )
    {
        setAttribute( SVGATTRIBUTE_ClipPathUnits, flag ? "objectBoundingBox" : "userSpaceOnUse" );
    }   //  setClipPathUnits()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setDx( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0)
            ? Arrays.stream( values )
                .map( SVGNumber::toString )
                .collect( joining( "," ) )
            : null;
        setAttribute( SVGATTRIBUTE_dx, value );
    }   //  setDx()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setDy( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0)
            ? Arrays.stream( values )
                .map( SVGNumber::toString )
                .collect( joining( "," ) )
            : null;
        setAttribute( SVGATTRIBUTE_dy, value );
    }   //  setDy()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setLengthAdjust( final boolean flag )
    {
        final var value = flag ? "spacingAndGlyphs" : "spacing";
        setAttribute( SVGATTRIBUTE_LengthAdjust, value );
    }   //  setLengthAdjust()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setMarkerHeight( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_MarkerHeight, value );
    }   //  setMarkerHeight()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setMarkerUnits( final boolean flag )
    {
        setAttribute( SVGATTRIBUTE_MarkerUnits, flag ? "userSpaceOnUse" : "strokeWidth" );
    }   //  setMarkerUnits()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setMarkerWidth( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_MarkerWidth, value );
    }   //  setMarkerWidth()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setOrientation( final SVGMarkerOrientation value )
    {
        setAttribute( SVGATTRIBUTE_Orientation, nonNull( value ) ? value.toString() : null );
    }   //  setOrientation()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setOrientation( final SVGNumber.SVGDegree value )
    {
        setAttribute( SVGATTRIBUTE_Orientation, nonNull( value ) ? value.toString() : null );
    }   //  setOrientation()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setPathDefinition( final SVGPathElement... pathElements )
    {
        final var value = nonNull( pathElements ) ? SVGPathElement.toString( pathElements ) : null;
        setAttribute( SVGATTRIBUTE_PathDefinition, value, Optional.of( " " ) );
    }   //  setPathDefinition()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setPosition( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_Position, value );
    }   //  setPosition()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setReferenceX( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_ReferenceX, value );
    }   //  setReferenceX()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setReferenceY( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_ReferenceY, value );
    }   //  setReferenceY()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setRotate( final SVGNumber.SVGDegree... values )
    {
        final var value = nonNull( values ) && (values.length != 0)
            ? Arrays.stream( values )
                .map( SVGDegree::toString )
                .collect( joining( "," ) )
            : null;
        setAttribute( SVGATTRIBUTE_Rotate, value );
    }   //  setRotate()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setTextLength( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_TextLength, value );
    }   //  setTextLength()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setX( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0)
            ? Arrays.stream( values )
                .map( SVGNumber::toString )
                .collect( joining( "," ) )
            : null;
        setAttribute( SVGATTRIBUTE_x, value );
    }   //  setX()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setX1( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_x1, value );
    }   //  setX1()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setX2( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_x2, value );
    }   //  setX2()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setY( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0)
            ? Arrays.stream( values )
                .map( SVGNumber::toString )
                .collect( joining( "," ) )
            : null;
        setAttribute( SVGATTRIBUTE_y, value );
    }   //  setY()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setY1( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_y1, value );
    }   //  setY1()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public void setY2( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_y2, value );
    }   //  setY2()

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString( final int indentationLevel, final boolean prettyPrint )
    {
        return super.toString( indentationLevel, prettyPrint );
    }   //  toString()
}
//  class SVGElementAdapter

/*
 *  End of File
 */