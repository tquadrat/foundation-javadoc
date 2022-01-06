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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ClipPathUnits;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ExternalResourcesRequired;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Transform;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Text;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Use;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.ANIMATION;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.DESCRIPTIVE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.SHAPE;
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
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsPresentationAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGClipPath;

/**
 *  The implementation of the interface
 *  {@link SVGClipPath}
 *  for the SVG {@code <clipPath>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGClipPathImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGClipPathImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGClipPathImpl extends SVGElementImpl implements SVGClipPath
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGClipPathImpl} instance.
     *
     *  @param  id  The mandatory id for the {@code <svg>} element.
     */
    public SVGClipPathImpl( final String id )
    {
        super( SVGELEMENT_ClipPath, ALLOWS_CHILDREN, VALIDATES_ATTRIBUTES, VALIDATES_CHILDREN );

        //---* The children and attributes for the <clipPatt> element *--------
        final Collection<String> childElements = new HashSet<>();
        childElements.addAll( ANIMATION.getElements() );
        childElements.addAll( DESCRIPTIVE.getElements() );
        childElements.addAll( SHAPE.getElements() );
        childElements.add( SVGELEMENT_Text );
        childElements.add( SVGELEMENT_Use );

        final Collection<String> attributes = new ArrayList<>();
        attributes.addAll( List.of( SVGATTRIBUTE_Id, SVGATTRIBUTE_Class,
            SVGATTRIBUTE_Style, SVGATTRIBUTE_ExternalResourcesRequired,
            SVGATTRIBUTE_Transform, SVGATTRIBUTE_ClipPathUnits ) );
        attributes.addAll( CORE_ATTRIBUTES );
        attributes.addAll( AllowsConditionalProcessingAttributes.CONDITIONALPROCESSING_ATTRIBUTES );
        attributes.addAll( AllowsPresentationAttributes.PRESENTATION_ATTRIBUTES );

        updateRegistries( childElements, attributes );

        setId( id );
    }   //  SVGClipPathImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void setClipPathUnits( final boolean flag )
    {
        setAttribute( SVGATTRIBUTE_ClipPathUnits, flag ? "objectBoundingBox" : "userSpaceOnUse" );
    }   //  setClipPathUnits()
}
//  class SVGClipPathImpl

/*
 *  End of File
 */