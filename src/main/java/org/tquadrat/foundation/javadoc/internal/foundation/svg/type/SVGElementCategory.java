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

import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyph;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphDef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphItem;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Anchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Animate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AnimateColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AnimateMotion;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AnimateTransform;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Circle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ColorProfile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Cursor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Defs;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Description;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Discard;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Ellipse;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feBlend;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feColorMatrix;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feComponentTransfer;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feComposite;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feConvolveMatrix;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feDiffuseLighting;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feDisplacementMap;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feDropShadow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feFlood;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feFuncA;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feFuncB;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feFuncG;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feFuncR;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feGaussianBlur;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feImage;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feMerge;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feMergeNode;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feMorphology;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feOffset;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feSpecularLighting;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feTile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter_feTurbulence;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Font;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFace;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFaceFormat;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFaceName;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFaceSource;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFaceURI;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ForeignObject;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Glyph;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_GlyphRef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_GradientLinear;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_GradientMesh;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_GradientRadial;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Group;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_HKern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Hatch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_HatchPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Image;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Light_feDistantLight;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Light_fePointLight;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Light_feSpotLight;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Line;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_MPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Marker;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Mesh;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_MeshPatch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_MeshRow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Metadata;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_MissingGlyph;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Path;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Pattern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_PolyLine;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Polygon;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Rectangle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Root;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Script;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Set;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_SolidColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Stop;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Switch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Symbol;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_TRef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_TSpan;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Text;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_TextPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Title;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Unknown;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Use;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_VKern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_View;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The categories for the SVG elements.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGElementCategory.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGElementCategory.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public enum SVGElementCategory
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  Animation element.
     */
    ANIMATION( "Animation Element", SVGELEMENT_Animate,
            SVGELEMENT_AnimateColor, SVGELEMENT_AnimateMotion,
            SVGELEMENT_AnimateTransform, SVGELEMENT_Discard,
            SVGELEMENT_MPath, SVGELEMENT_Set ),

    /**
     *  Basic shape element.
     */
    BASIC_SHAPE( "Basic Shape", SVGELEMENT_Circle, SVGELEMENT_Ellipse,
            SVGELEMENT_Line, SVGELEMENT_Polygon, SVGELEMENT_PolyLine,
            SVGELEMENT_Rectangle ),

    /**
     *  Animation element.
     */
    CONTAINER( "Container Element", SVGELEMENT_Anchor, SVGELEMENT_Defs,
            SVGELEMENT_Group, SVGELEMENT_Marker, SVGELEMENT_Mask,
            SVGELEMENT_MissingGlyph, SVGELEMENT_Pattern, SVGELEMENT_Root,
            SVGELEMENT_Switch, SVGELEMENT_Symbol, SVGELEMENT_Unknown ),

    /**
     *  Descriptive element.
     */
    DESCRIPTIVE( "Descriptive Element", SVGELEMENT_Description,
            SVGELEMENT_Metadata, SVGELEMENT_Title ),

    /**
     *  Filter primitive element.
     */
    FILTER_PRIMITIVE( "Filter Primitive Element", SVGELEMENT_Ellipse,
            SVGELEMENT_Filter_feBlend, SVGELEMENT_Filter_feColorMatrix,
            SVGELEMENT_Filter_feComponentTransfer,
            SVGELEMENT_Filter_feComposite, SVGELEMENT_Filter_feConvolveMatrix,
            SVGELEMENT_Filter_feDiffuseLighting,
            SVGELEMENT_Filter_feDisplacementMap,
            SVGELEMENT_Filter_feDropShadow, SVGELEMENT_Filter_feFlood,
            SVGELEMENT_Filter_feFuncA, SVGELEMENT_Filter_feFuncB,
            SVGELEMENT_Filter_feFuncG, SVGELEMENT_Filter_feFuncR,
            SVGELEMENT_Filter_feGaussianBlur, SVGELEMENT_Filter_feImage,
            SVGELEMENT_Filter_feMerge, SVGELEMENT_Filter_feMergeNode,
            SVGELEMENT_Filter_feMorphology, SVGELEMENT_Filter_feOffset,
            SVGELEMENT_Filter_feSpecularLighting, SVGELEMENT_Filter_feTile,
            SVGELEMENT_Filter_feTurbulence ),

    /**
     *  Font element.
     */
    FONT( "Font Element", SVGELEMENT_Font, SVGELEMENT_FontFace,
            SVGELEMENT_FontFaceFormat, SVGELEMENT_FontFaceName,
            SVGELEMENT_FontFaceSource, SVGELEMENT_FontFaceURI,
            SVGELEMENT_HKern, SVGELEMENT_VKern ),

    /**
     *  Gradient element.
     */
    GRADIENT( "Gradient Element", SVGELEMENT_GradientLinear,
            SVGELEMENT_GradientMesh, SVGELEMENT_GradientRadial,
            SVGELEMENT_Stop ),

    /**
     *  Graphics element.
     */
    GRAPHICS( "Graphics Element", SVGELEMENT_Circle, SVGELEMENT_Ellipse,
            SVGELEMENT_Image, SVGELEMENT_Line, SVGELEMENT_Mesh,
            SVGELEMENT_Path, SVGELEMENT_Polygon, SVGELEMENT_PolyLine,
            SVGELEMENT_Rectangle, SVGELEMENT_Text, SVGELEMENT_Use ),

    /**
     *  Graphics referencing element.
     */
    GRAPHICS_REFERENCE( "Graphics Referencing Element", SVGELEMENT_Mesh,
            SVGELEMENT_Use ),

    /**
     *  Light source element.
     */
    LIGHT_SOURCE( "Light Source Element", SVGELEMENT_Light_feDistantLight,
            SVGELEMENT_Light_fePointLight, SVGELEMENT_Light_feSpotLight ),

    /**
     *  Never-rendered element.
     */
    NEVER_RENDERED( "Never-Rendered Element", SVGELEMENT_ClipPath,
            SVGELEMENT_Defs, SVGELEMENT_Description, SVGELEMENT_GradientLinear,
            SVGELEMENT_GradientMesh, SVGELEMENT_GradientRadial,
            SVGELEMENT_Hatch, SVGELEMENT_Marker, SVGELEMENT_Mask,
            SVGELEMENT_Metadata, SVGELEMENT_Pattern, SVGELEMENT_Script,
            SVGELEMENT_Style, SVGELEMENT_Symbol, SVGELEMENT_Title ),

    /**
     *  Paint server element.
     */
    PAINT_SERVER( "Paint Server Element", SVGELEMENT_GradientLinear,
            SVGELEMENT_GradientMesh, SVGELEMENT_GradientRadial,
            SVGELEMENT_Hatch, SVGELEMENT_Pattern, SVGELEMENT_SolidColor ),

    /**
     *  Renderable element.
     */
    RENDERABLE( "Renderable Element", SVGELEMENT_Anchor, SVGELEMENT_Circle,
            SVGELEMENT_Ellipse, SVGELEMENT_ForeignObject, SVGELEMENT_Group,
            SVGELEMENT_Image, SVGELEMENT_Line, SVGELEMENT_Mesh,
            SVGELEMENT_Path, SVGELEMENT_Polygon, SVGELEMENT_PolyLine,
            SVGELEMENT_Rectangle, SVGELEMENT_Root, SVGELEMENT_Switch,
            SVGELEMENT_Symbol, SVGELEMENT_Text, SVGELEMENT_TextPath,
            SVGELEMENT_TSpan, SVGELEMENT_Unknown, SVGELEMENT_Use ),

    /**
     *  Shape element.
     */
    SHAPE( "Shape Element", SVGELEMENT_Circle, SVGELEMENT_Ellipse,
            SVGELEMENT_Line, SVGELEMENT_Mesh, SVGELEMENT_Path,
            SVGELEMENT_Polygon, SVGELEMENT_PolyLine, SVGELEMENT_Rectangle ),

    /**
     *  Structural element.
     */
    STRUCTURAL( "Structural Element", SVGELEMENT_Defs, SVGELEMENT_Group,
            SVGELEMENT_Root, SVGELEMENT_Symbol, SVGELEMENT_Use ),

    /**
     *  Text content element.
     */
    TEXT_CONTENT( "Text Content Element", SVGELEMENT_AltGlyph,
            SVGELEMENT_AltGlyphDef, SVGELEMENT_AltGlyphItem, SVGELEMENT_Glyph,
            SVGELEMENT_GlyphRef, SVGELEMENT_TextPath, SVGELEMENT_TRef,
            SVGELEMENT_TSpan ),

    /**
     *  Text content child element.
     */
    TEXT_CONTENT_CHILD( "Text Content Child Element", SVGELEMENT_AltGlyph,
            SVGELEMENT_TextPath, SVGELEMENT_TRef, SVGELEMENT_TSpan ),

    /**
     *  Text content element.
     */
    UNCATEGORIZED( "Uncategorised Element", SVGELEMENT_ClipPath,
            SVGELEMENT_ColorProfile, SVGELEMENT_Cursor, SVGELEMENT_Filter,
            SVGELEMENT_ForeignObject, SVGELEMENT_HatchPath,
            SVGELEMENT_MeshPatch, SVGELEMENT_MeshRow, SVGELEMENT_Script,
            SVGELEMENT_Style, SVGELEMENT_View );

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code SVGElementCategory} objects.
     */
    public static final SVGElementCategory [] EMPTY_SVGElementCategory_ARRAY = new SVGElementCategory [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The category description.
     */
    private final String m_Description;

    /**
     *  The elements that belong to this category.
     */
    private final Set<String> m_ElementNames;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The element categories by element names.
     */
    private static final Map<String,Set<SVGElementCategory>> m_CategoriesByElement;

    static
    {
        final var map = stream( values() )
            .flatMap( v -> v.getElements()
                    .stream()
                    .collect( toMap( identity(), e -> v, ( a, b ) -> b, HashMap::new ) )
                .entrySet()
                .stream()
            )
            .collect(
                toUnmodifiableMap( Map.Entry::getKey,
                    e -> EnumSet.of( e.getValue() ),
                    (final Set<SVGElementCategory> s1, final Set<SVGElementCategory> s2) ->
                    {
                        s1.addAll( s2 );
                        return s1;
                    }
                )
            );
        m_CategoriesByElement = Map.copyOf( map );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGElementCategory} instance.
     *
     *  @param  description The description.
     *  @param  elementNames    The names of the elements that belongs to this
     *      category.
     */
    private SVGElementCategory( final String description, final String... elementNames )
    {
        m_Description = description;
        m_ElementNames = Set.of( elementNames );
    }   //  SVGElementCategory()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the names of the elements that belong to this category.
     *
     *  @return The element names.
     */
    public final Collection<String> getElements() { return m_ElementNames; }

    /**
     *  Retrieves the element category for the given element name.
     *
     *  @param  elementName The name of the element.
     *  @return The categories for this element; will never be {@code null}.
     */
    public static final Collection<SVGElementCategory> retrieveElementCategory( final String elementName )
    {
        final var retValue = m_CategoriesByElement.getOrDefault( requireNotEmptyArgument( elementName, "elementName" ), emptySet() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveElementCategory()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_Description; }
}
//  class SVGElementCategory

/*
 *  End of File
 */