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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ExternalResourcesRequired;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PreserveAspectRatio;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ViewBox;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphDef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Anchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ColorProfile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Cursor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Font;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFace;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ForeignObject;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Image;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Marker;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Pattern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Script;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Switch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Symbol;
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
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsGraphicalEventAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsPresentationAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGSymbol;

/**
 *  The implementation for the interface
 *  {@link SVGSymbol}
 *  for the {@code <symbol>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGSymbolImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGSymbolImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGSymbolImpl extends SVGElementImpl implements SVGSymbol
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGSymbolImpl} instance.
     *
     *  @param  id  The id for the new symbol.
     */
    public SVGSymbolImpl( final String id )
    {
        super( SVGELEMENT_Symbol, ALLOWS_CHILDREN, VALIDATES_ATTRIBUTES, VALIDATES_CHILDREN );

        //---* The children and attributes for the <symbol> element *----------
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
        attributes.addAll( List.of( SVGATTRIBUTE_Id,
            SVGATTRIBUTE_PreserveAspectRatio, SVGATTRIBUTE_ViewBox,
            SVGATTRIBUTE_Class, SVGATTRIBUTE_Style,
            SVGATTRIBUTE_ExternalResourcesRequired ) );
        attributes.addAll( CORE_ATTRIBUTES );
        attributes.addAll( AllowsGraphicalEventAttributes.GRAPHICALEVENT_ATTRIBUTES );
        attributes.addAll( AllowsPresentationAttributes.PRESENTATION_ATTRIBUTES );

        updateRegistries( childElements, attributes );

        setId( id );
    }   //  SVGSymbolImpl()
}
//  class SVGSymbolImpl

/*
 *  End of File
 */