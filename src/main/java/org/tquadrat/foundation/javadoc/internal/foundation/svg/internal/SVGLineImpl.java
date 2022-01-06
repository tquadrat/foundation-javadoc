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

package org.tquadrat.foundation.javadoc.internal.foundation.svg.internal;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Class;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PathLength;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x1;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x2;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y1;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y2;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Line;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.ANIMATION;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.DESCRIPTIVE;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.ALLOWS_CHILDREN;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.VALIDATES_ATTRIBUTES;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.VALIDATES_CHILDREN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsConditionalProcessingAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsGlobalEventAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsGraphicalEventAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsPresentationAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsStyleAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGLine;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;

/**
 *  The implementation of the
 *  {@link SVGLine}
 *  interface for the SVG {@code <line>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGLineImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGLineImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGLineImpl extends SVGElementImpl implements SVGLine
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGLineImpl} instance.
     */
    public SVGLineImpl()
    {
        super( SVGELEMENT_Line, ALLOWS_CHILDREN, VALIDATES_ATTRIBUTES, VALIDATES_CHILDREN );

        //---* The children and attributes for the <line> element *------------
        final Collection<String> childElements = new HashSet<>();
        childElements.addAll( ANIMATION.getElements() );
        childElements.addAll( DESCRIPTIVE.getElements() );

        final Collection<String> attributes = new ArrayList<>();
        attributes.addAll( List.of( SVGATTRIBUTE_Id,
            SVGATTRIBUTE_x1, SVGATTRIBUTE_y1, SVGATTRIBUTE_x2, SVGATTRIBUTE_y2,
            SVGATTRIBUTE_PathLength, SVGATTRIBUTE_Class, SVGATTRIBUTE_Style ) );
        attributes.addAll( CORE_ATTRIBUTES );
        attributes.addAll( AllowsStyleAttributes.STYLE_ATTRIBUTES );
        attributes.addAll( AllowsConditionalProcessingAttributes.CONDITIONALPROCESSING_ATTRIBUTES );
        attributes.addAll( AllowsGlobalEventAttributes.GLOBALEVENT_ATTRIBUTES );
        attributes.addAll( AllowsGraphicalEventAttributes.GRAPHICALEVENT_ATTRIBUTES );
        attributes.addAll( AllowsPresentationAttributes.PRESENTATION_ATTRIBUTES );

        updateRegistries( childElements, attributes );
    }   //  SVGLineImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void setX1( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_x1, value );
    }   //  setX1()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setX2( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_x2, value );
    }   //  setX2()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setY1( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_y1, value );
    }   //  setY1()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setY2( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_y2, value );
    }   //  setY2()
}
//  class SVGLineImpl

/*
 *  End of File
 */