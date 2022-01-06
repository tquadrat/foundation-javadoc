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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnActivate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusIn;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusOut;

import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG elements that allow the graphical event attributes
 *  {@value SVGUtils#SVGATTRIBUTE_OnActivate},
 *  {@value SVGUtils#SVGATTRIBUTE_OnFocusIn},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_OnFocusOut}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsGraphicalEventAttributes.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: AllowsGraphicalEventAttributes.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsGraphicalEventAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The graphical event attributes.
     */
    public static final List<String> GRAPHICALEVENT_ATTRIBUTES = List.of( SVGATTRIBUTE_OnActivate, SVGATTRIBUTE_OnFocusIn, SVGATTRIBUTE_OnFocusOut );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the activation handler for this SVG element.
     *
     *  @param  value   The activation handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnActivate
     */
    public void setActivationHandler( final String value );

    /**
     *  Sets the focus-in handler for this SVG element.
     *
     *  @param  value   The focus-in handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnFocusIn
     */
    public void setFocusInHandler( final String value );

    /**
     *  Sets the focus-out handler for this SVG element.
     *
     *  @param  value   The focus-out handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnFocusOut
     */
    public void setFocusOutHandler( final String value );
}
//  interface AllowsGraphicalEventAttributes

/*
 *  End of File
 */