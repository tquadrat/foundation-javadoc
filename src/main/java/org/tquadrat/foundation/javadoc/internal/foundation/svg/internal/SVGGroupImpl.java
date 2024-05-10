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

package org.tquadrat.foundation.javadoc.internal.foundation.svg.internal;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphDef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Anchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ColorProfile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Cursor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Font;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFace;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ForeignObject;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Group;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Image;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Marker;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Pattern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Script;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Switch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Text;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_View;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.ANIMATION;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.DESCRIPTIVE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.GRADIENT;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.SHAPE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.STRUCTURAL;
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
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGGroup;

/**
 *  The implementation of the interface
 *  {@link SVGGroup}
 *  for the SVG {@code <g>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGGroupImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGGroupImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGGroupImpl extends SVGElementImpl implements SVGGroup
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGGroupImpl} instance.
     */
    public SVGGroupImpl()
    {
        super( SVGELEMENT_Group, ALLOWS_CHILDREN, VALIDATES_ATTRIBUTES, VALIDATES_CHILDREN );

        //---* The children and attributes for the <g> element *---------------
        final Collection<String> childElements = new HashSet<>();
        childElements.addAll( ANIMATION.getElements() );
        childElements.addAll( DESCRIPTIVE.getElements() );
        childElements.addAll( SHAPE.getElements() );
        childElements.addAll( STRUCTURAL.getElements() );
        childElements.addAll( GRADIENT.getElements() );
        childElements.addAll( List.of( SVGELEMENT_Anchor,
            SVGELEMENT_AltGlyphDef, SVGELEMENT_ClipPath,
            SVGELEMENT_ColorProfile, SVGELEMENT_Cursor, SVGELEMENT_Filter,
            SVGELEMENT_Font, SVGELEMENT_FontFace, SVGELEMENT_ForeignObject,
            SVGELEMENT_Image, SVGELEMENT_Marker, SVGELEMENT_Mask,
            SVGELEMENT_Pattern, SVGELEMENT_Script, SVGELEMENT_Style,
            SVGELEMENT_Switch, SVGELEMENT_Text, SVGELEMENT_View ) );

        final Collection<String> attributes = new ArrayList<>();
        attributes.add( SVGATTRIBUTE_Id );
        attributes.addAll( CORE_ATTRIBUTES );
        attributes.addAll( AllowsStyleAttributes.STYLE_ATTRIBUTES );
        attributes.addAll( AllowsConditionalProcessingAttributes.CONDITIONALPROCESSING_ATTRIBUTES );
        attributes.addAll( AllowsGlobalEventAttributes.GLOBALEVENT_ATTRIBUTES );
        attributes.addAll( AllowsGraphicalEventAttributes.GRAPHICALEVENT_ATTRIBUTES );
        attributes.addAll( AllowsPresentationAttributes.PRESENTATION_ATTRIBUTES );

        updateRegistries( childElements, attributes );
    }   //  SVGGroupImpl()
}
//  class SVGGroupImpl

/*
 *  End of File
 */