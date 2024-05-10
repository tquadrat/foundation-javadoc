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

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGMarkerOrientation;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGDegree;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPreserveAspectRatio;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform;

/**
 *  The definition for the SVG {@code <marker>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGMarker.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "unused" )
@ClassVersion( sourceVersion = "$Id: SVGMarker.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGMarker extends SVGElementWithChildren, AllowsPresentationAttributes
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the attribute that indicates whether external resources are
     *  required to render this {@code <marker>} element.
     *
     *  @param  flag    {@code true} if external resources are needed,
     *      {@code false} if all required resources are local to the current
     *      context.
     */
    public void setExternalResourcesRequired( final boolean flag );

    /**
     *  Sets the width and the height of the viewport into which the
     *  {@code <marker>} is to be fitted when it is rendered.
     *
     *  @param  width   The width.
     *  @param  height   The height.
     */
    public default void setMarkerDimensions( final SVGNumber width, final SVGNumber height )
    {
        setMarkerHeight( requireNonNullArgument( height, "height" ) );
        setMarkerWidth( requireNonNullArgument( width, "width" ) );
    }   //  setMarkerDimensions()

    /**
     *  Sets the height of the viewport into which the {@code <marker>} is to
     *  be fitted when it is rendered.
     *
     *  @param  value   The height.
     */
    public void setMarkerHeight( final SVGNumber value );

    /**
     *  Sets the marker units attribute that defines the coordinate system for
     *  the attributes
     *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerWidth}
     *  and
     *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerHeight}.
     *
     *  @param  flag    {@code true} if the user space should be used,
     *      {@code false} for the stroke width.
     */
    public void setMarkerUnits( final boolean flag );

    /**
     *  Sets the width of the viewport into which the {@code <marker>} is to be
     *  fitted when it is rendered.
     *
     *  @param  value   The width.
     */
    public void setMarkerWidth( final SVGNumber value );

    /**
     *  Sets the orientation of the marker. Basically, this means how it is
     *  rotated when it is placed at its position on the markable element.
     *
     *  @param  value   The orientation.
     */
    public void setOrientation( final SVGMarkerOrientation value );

    /**
     *  Sets the orientation of the marker. Basically, this means how it is
     *  rotated when it is placed at its position on the markable element.
     *
     *  @param  value   The orientation.
     */
    public void setOrientation( final SVGDegree value );

    /**
     *  Sets the mode for the aspect ratio preservation for this
     *  {@code <marker>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    public void setPreserveAspectRatio( final SVGPreserveAspectRatio value );

    /**
     *  Sets the reference point for the marker.<br>
     *  <br>This is the location on the marker where it will be joined to its
     *  markable element. Coordinates are relative to the marker's coordinate
     *  system, and not the markable element it is placed on.
     *
     *  @param  x   The x coordinate for the point.
     *  @param  y   The y coordinate for the point.
     *
     *  @see #setMarkerUnits(boolean)
     */
    public default void setReferencePoint( final SVGNumber x, final SVGNumber y )
    {
        setReferenceX( requireNonNullArgument( x, "x" ) );
        setReferenceY( requireNonNullArgument( y, "y" ) );
    }   //  setReferencePoint()

    /**
     *  Sets the x coordinate of the marker reference point.
     *
     *  @param  value   The coordinate type.
     *
     *  @see #setReferencePoint(SVGNumber, SVGNumber)
     */
    public void setReferenceX( final SVGNumber value );

    /**
     *  Sets the y coordinate of the marker reference point.
     *
     *  @param  value   The coordinate type.
     *
     *  @see #setReferencePoint(SVGNumber, SVGNumber)
     */
    public void setReferenceY( final SVGNumber value );

    /**
     *  Sets the transformations for this {@code <marker>} element.
     *
     *  @param  values  The transformations; if {@code null} or empty, the
     *      attribute will be removed.
     */
    @SuppressWarnings( "AbstractMethodOverridesAbstractMethod" )
    @Override
    public void setTransform( final SVGTransform... values );

    /**
     *  Defines the visible area for this {@code <marker>} element.
     *
     *  @param  x   The x coordinate of top left corner of the area.
     *  @param  y   The y coordinate of top left corner of the area.
     *  @param  width   The width of the area.
     *  @param  height  The height of the area.
     */
    public void setViewBox( final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height );
}
//  interface SVGMarker

/*
 *  End of File
 */