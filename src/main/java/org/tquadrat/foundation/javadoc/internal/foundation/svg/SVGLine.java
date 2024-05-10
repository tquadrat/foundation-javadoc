/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.number;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGUserUnitValue;

/**
 *  The definition of an SVG {@code <line>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGLine.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "unused" )
@ClassVersion( sourceVersion = "$Id: SVGLine.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGLine extends SVGElementWithChildren, AllowsConditionalProcessingAttributes, AllowsGlobalEventAttributes, AllowsGraphicalEventAttributes, AllowsPresentationAttributes, AllowsStyleAttributes
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the start and end points for this line.
     *
     *  @param  x1  The x coordinate for the starting point of the line.
     *  @param  y1  The y coordinate for the starting point of the line.
     *  @param  x2  The x coordinate for the ending point of the line.
     *  @param  y2  The y coordinate for the ending point of the line.
     */
    public default void defineLine( final SVGNumber x1, final SVGNumber y1, final SVGNumber x2, final SVGNumber y2 )
    {
        setStartPoint( x1, y1 );
        setEndPoint( x2, y2 );
    }   //  defineLine()

    /**
     *  Sets the end point for this line.
     *
     *  @param  x  The x coordinate for the ending point of the line.
     *  @param  y  The y coordinate for the ending point of the line.
     */
    public default void setEndPoint( final SVGNumber x, final SVGNumber y )
    {
        setX2( requireNonNullArgument( x, "x" ) );
        setY2( requireNonNullArgument( y, "y" ) );
    }   //  setEndPoint()

    /**
     *  Sets the length of the path represented by this SVG {@code <line>}
     *  element.
     *
     *  @param  length  The author's computation of the total length of the
     *      path, in user units. This type is used to calibrate the user
     *      agent's own distance-along-a-path calculations with that of the
     *      author. The user agent will scale all distance-along-a-path
     *      computations by the ratio of this type to the user agent's own
     *      computed type for total path length.<br>
     *      <br>A type of zero is valid, but a negative type is an error.
     *
     *  @throws IllegalArgumentException    The type is less than 0.
     */
    public void setPathLength( final SVGUserUnitValue length );

    /**
     *  Sets the length of the path represented by this SVG {@code <line>}
     *  element.
     *
     *  @param  length  The author's computation of the total length of the
     *      path, in user units. This type is used to calibrate the user
     *      agent's own distance-along-a-path calculations with that of the
     *      author. The user agent will scale all distance-along-a-path
     *      computations by the ratio of this type to the user agent's own
     *      computed type for total path length.<br>
     *      <br>A type of zero is valid, but a negative type is an error.
     *
     *  @throws IllegalArgumentException    The type is less than 0.
     */
    public default void setPathLength( final double length ) { setPathLength( number( length ) ); }

    /**
     *  Sets the length of the path represented by this SVG {@code <line>}
     *  element.
     *
     *  @param  length  The author's computation of the total length of the
     *      path, in user units. This type is used to calibrate the user
     *      agent's own distance-along-a-path calculations with that of the
     *      author. The user agent will scale all distance-along-a-path
     *      computations by the ratio of this type to the user agent's own
     *      computed type for total path length.<br>
     *      <br>A type of zero is valid, but a negative type is an error.
     *
     *  @throws IllegalArgumentException    The type is less than 0.
     */
    public default void setPathLength( final long length ) { setPathLength( number( length ) ); }

    /**
     *  Sets the start point for this line.
     *
     *  @param  x   The x coordinate for the starting point of the line.
     *  @param  y   The y coordinate for the starting point of the line.
     */
    public default void setStartPoint( final SVGNumber x, final SVGNumber y )
    {
        setX1( requireNonNullArgument( x, "x" ) );
        setY1( requireNonNullArgument( y, "y" ) );
    }   //  setStartPoint()

    /**
     *  Sets the x coordinate of the starting point for this line.
     *
     *  @param  value   The x coordinate.
     */
    public void setX1( final SVGNumber value );

    /**
     *  Sets the x coordinate of the ending point for this line.
     *
     *  @param  value   The x coordinate.
     */
    public void setX2( final SVGNumber value );

    /**
     *  Sets the y coordinate of the starting point for this line.
     *
     *  @param  value   The y coordinate.
     */
    public void setY1( final SVGNumber value );

    /**
     *  Sets the y coordinate of the ending point for this line.
     *
     *  @param  value   The y coordinate.
     */
    public void setY2( final SVGNumber value );
}
//  interface SVGLine

/*
 *  End of File
 */