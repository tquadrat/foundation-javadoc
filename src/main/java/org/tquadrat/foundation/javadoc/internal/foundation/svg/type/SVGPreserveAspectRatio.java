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
 *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_PreserveAspectRatio}.
 *
 *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_PreserveAspectRatio
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGPreserveAspectRatio.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGPreserveAspectRatio.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public enum SVGPreserveAspectRatio
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  Do not force uniform scaling. Scale the graphic content of the given
     *  element non-uniformly if necessary such that the element's bounding box
     *  exactly matches the viewport rectangle.
     */
    @API( status = STABLE, since = "0.0.5" )
    NONE( "none" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} of the element's viewBox with the
     *  smallest x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} of the element's viewBox with the
     *  smallest y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMIN_YMIN( "xMinYMin" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the midpoint x type of the element's viewBox with the
     *  midpoint x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} of the element's viewBox with the
     *  smallest y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMID_YMIN( "xMidYMin" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} +{@code <width>} of the element's viewBox
     *  with the maximum x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} of the element's viewBox with the
     *  smallest y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMAX_YMIN( "xMaxYMin" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} of the element's viewBox with the
     *  smallest x type of the viewport.<br>
     *  <br>Align the midpoint y type of the element's viewBox with the
     *  midpoint y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMIN_YMID( "xMinYMid" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the midpoint x type of the element's viewBox with the
     *  midpoint x type of the viewport.<br>
     *  <br>Align the midpoint y type of the element's viewBox with the
     *  midpoint y type of the viewport.<br>
     *  <br>This is the default setting.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMID_YMID( "xMidYMid" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} + {@code <width>} of the element's
     *  viewBox with the maximum x type of the viewport.<br>
     *  <br>Align the midpoint y type of the element's viewBox with the
     *  midpoint y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMAX_YMID( "xMaxYMid" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} of the element's viewBox with the
     *  smallest x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} + {@code <height>} of the element's
     *  viewBox with the maximum y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMIN_YMAX( "xMinYMax" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the midpoint x type of the element's viewBox with the
     *  midpoint x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} + {@code <height>} of the element's
     *  viewBox with the maximum y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMID_YMAX( "xMidYMax" ),

    /**
     *  Force uniform scaling.<br>
     *  <br>Align the {@code <min-x>} + {@code <width>} of the element's
     *  viewBox with the maximum x type of the viewport.<br>
     *  <br>Align the {@code <min-y>} + {@code <height>} of the element's
     *  viewBox with the maximum y type of the viewport.
     */
    @API( status = STABLE, since = "0.0.5" )
    XMAX_YMAX( "xMaxYMax" );

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code SVGPreserveAspectRatio} objects.
     */
    public static final SVGPreserveAspectRatio [] EMPTY_SVGPreserveAspectRatio_ARRAY = new SVGPreserveAspectRatio [0];

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
     *  Creates a new {@code SVGPreserveAspectRatio} instance.
     *
     *  @param  attributeValue  The attribute type.
     */
    private SVGPreserveAspectRatio( final String attributeValue )
    {
        m_AttributeValue = attributeValue;
    }   //  SVGPreserveAspectRatio()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_AttributeValue; }
}
//  enum SVGPreserveAspectRatio

/*
 *  End of File
 */