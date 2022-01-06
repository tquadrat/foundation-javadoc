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

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPreserveAspectRatio;

/**
 *  The definition of the SVG element {@code <symbol>}. <br>
 *  <br>A {@code <symbol>} element is useless without the attribute {@code id}
 *  set; therefore it is required already on creation and will not appear in
 *  this interface.
 *
 *  @see SVGUtils#createSymbol(String)
 *  @see SVGUtils#createSymbol(String,SVG)
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGSymbol.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 *
 *  @see "https://www.w3.org/TR/SVG/single-page.html#struct-SVGElement"
 */
@ClassVersion( sourceVersion = "$Id: SVGSymbol.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGSymbol extends SVGElementWithChildren, AllowsGraphicalEventAttributes, AllowsPresentationAttributes, AllowsStyleAttributes
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the attribute that indicates whether external resources are
     *  required to render this {@code <symbol>} element.
     *
     *  @param  flag    {@code true} if external resources are needed,
     *      {@code false} if all required resources are local to the current
     *      context.
     */
    public void setExternalResourcesRequired( final boolean flag );

    /**
     *  Sets the mode for the aspect ratio preservation for this
     *  {@code <symbol>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    public void setPreserveAspectRatio( final SVGPreserveAspectRatio value );

    /**
     *  Defines the visible area for this {@code <symbol>} element.
     *
     *  @param  x   The x coordinate of top left corner of the area.
     *  @param  y   The y coordinate of top left corner of the area.
     *  @param  width   The width of the area.
     *  @param  height  The height of the area.
     */
    public void setViewBox( final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height );
}
//  interface SVGSymbol

/*
 *  End of File
 */