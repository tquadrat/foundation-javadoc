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
 *  The values for the attribute
 *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Orientation} of
 *  the SVG element {@code <marker>}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGMarkerOrientation.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGMarkerOrientation.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public enum SVGMarkerOrientation
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  A type of {@code auto} indicates that the marker is oriented such that
     *  its positive x-axis is pointing in the direction of the path at the
     *  point it is place.
     */
    @API( status = STABLE, since = "0.0.5" )
    AUTO( "auto" ),

    /**
     *  A type of {@code auto-start-reverse} means the same as
     *  {@link #AUTO} except that for a marker placed by
     *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerStart},
     *  the orientation is 180° different from the orientation as determined
     *  by {@code auto}.
     */
    @API( status = STABLE, since = "0.0.5" )
    AUTO_START_REVERSE( "auto-start-reverse" );

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
     *  Creates a new {@code SVGMarkerOrientation} instance.
     *
     *  @param  attributeValue  The attribute type.
     */
    private SVGMarkerOrientation( final String attributeValue )
    {
        m_AttributeValue = attributeValue;
    }   //  SVGMarkerOrientation()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_AttributeValue; }
}
//  class SVGMarkerOrientation

/*
 *  End of File
 */