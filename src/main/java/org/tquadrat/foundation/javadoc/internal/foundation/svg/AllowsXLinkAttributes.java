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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Actuate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_ArcRole;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Reference;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Role;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Show;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Title;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Type;

import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG elements that allow the XLink attributes
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Actuate},
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_ArcRole},
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Reference},
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Role},
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Show},
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Title},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_XLink_Type}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsXLinkAttributes.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: AllowsXLinkAttributes.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsXLinkAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The core attributes.
     */
    public static final List<String> XLINK_ATTRIBUTES = List.of(
        SVGATTRIBUTE_XLink_Actuate,
        SVGATTRIBUTE_XLink_ArcRole,
        SVGATTRIBUTE_XLink_Reference,
        SVGATTRIBUTE_XLink_Role,
        SVGATTRIBUTE_XLink_Show,
        SVGATTRIBUTE_XLink_Title,
        SVGATTRIBUTE_XLink_Type
    );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the XLink {@code actuate} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXlinkActuate( final CharSequence value );

    /**
     *  Sets the XLink {@code arcrole} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkArcRole( final CharSequence value );

    /**
     *  Sets the XLink {@code reference} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkReference( final CharSequence value );

    /**
     *  Sets the XLink {@code role} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkRole( final CharSequence value );

    /**
     *  Sets the XLink {@code show} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkShow( final CharSequence value );

    /**
     *  Sets the XLink {@code title} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkTitle( final CharSequence value );

    /**
     *  Sets the XLink {@code type} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkType( final CharSequence value );
}
//  interface AllowsStyleAttributes

/*
 *  End of File
 */