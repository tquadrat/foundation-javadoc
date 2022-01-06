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

package org.tquadrat.foundation.javadoc.internal.foundation.svg.type;

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  Possible values for the SVG attribute
 *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_AlignmentBaseline}.
 *
 *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_AlignmentBaseline
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGAlignmentBaseLine.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGAlignmentBaseLine.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public enum SVGAlignmentBaseLine
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  Auto.
     */
    @API( status = STABLE, since = "0.0.5" )
    AUTO( "auto" ),

    /**
     *  Inherit.
     */
    @API( status = STABLE, since = "0.0.5" )
    INHERIT( "inherit" ),

    /**
     *  After edge.
     */
    @API( status = STABLE, since = "0.0.5" )
    AFTER_EDGE( "after-edge" ),

    /**
     *  Alphabetic.
     */
    @API( status = STABLE, since = "0.0.5" )
    ALPHABETIC( "alphabetic" ),

    /**
     *  Baseline.
     */
    @API( status = STABLE, since = "0.0.5" )
    BASELINE( "baseline" ),

    /**
     *  Before edge.
     */
    @API( status = STABLE, since = "0.0.5" )
    BEFORE_EDGE( "before-edge" ),

    /**
     *  Central.
     */
    @API( status = STABLE, since = "0.0.5" )
    CENTRAL( "central" ),

    /**
     *  Middle.
     */
    @API( status = STABLE, since = "0.0.5" )
    MIDDLE( "middle" ),

    /**
     *  Hanging.
     */
    @API( status = STABLE, since = "0.0.5" )
    HANGING( "hanging" ),

    /**
     *  Ideographic.
     */
    @API( status = STABLE, since = "0.0.5" )
    IDEOGRAPHIC( "ideographic" ),

    /**
     *  Mathematical.
     */
    @API( status = STABLE, since = "0.0.5" )
    MATHEMATICAL( "mathematical" ),

    /**
     *  Text after edge.
     */
    @API( status = STABLE, since = "0.0.5" )
    TEXT_AFTER_EDGE( "text-after-edge" ),

    /**
     *  Text before edge.
     */
    @API( status = STABLE, since = "0.0.5" )
    TEXT_BEFORE_EDGE( "text-before-edge" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The attribute type.
     */
    private final String m_AttributeValue;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGAlignmentBaseLine} instance.
     *
     *  @param  attributeValue  The attribute type.
     */
    private SVGAlignmentBaseLine( final String attributeValue )
    {
        m_AttributeValue = attributeValue;
    }   //  SVGAlignmentBaseLine()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_AttributeValue; }
}
//  enum SVGAlignmentBaseLine

/*
 *  End of File
 */