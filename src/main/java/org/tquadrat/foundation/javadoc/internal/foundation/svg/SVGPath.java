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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.number;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGUserUnitValue;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement;

/**
 *  The definition of the SVG {@code <path>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGPath.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGPath.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGPath extends SVGElementWithChildren, AllowsConditionalProcessingAttributes, AllowsGlobalEventAttributes, AllowsGraphicalEventAttributes, AllowsPresentationAttributes, AllowsStyleAttributes
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the path definition for this SVG {@code <path>} element.
     *
     *  @param  pathElements    The elements of the path.
     */
    public void setPathDefinition( final SVGPathElement... pathElements );

    /**
     *  Sets the length of the path represented by this SVG {@code <path>}
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
     *  Sets the length of the path represented by this SVG {@code <path>}
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
     *  Sets the length of the path represented by this SVG {@code <path>}
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
}
//  interface SVGPath

/*
 *  End of File
 */