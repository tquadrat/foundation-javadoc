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

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_AlignmentBaseline;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_BaselineShift;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Clip;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ClipRule;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Color;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ColorInterpolation;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ColorInterpolationFilters;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ColorProfile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ColorRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Cursor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Direction;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Display;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_DominantBaseline;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_EnableBackground;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Fill;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FillOpacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FillRule;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Filter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FloodColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FloodOpacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontFamily;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontSize;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontSizeAdjust;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontStretch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontStyle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontVariant;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_FontWeight;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_GlyphOrientationHorizontal;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_GlyphOrientationVertical;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ImageRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Kerning;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LetterSpacing;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LightingColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerEnd;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerMid;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Opacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Overflow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PointerEvents;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ShapeRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StopColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StopOpacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Stroke;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeDashArray;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeDashOffset;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeLineCap;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeLineJoin;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeMiterLimit;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeOpacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_StrokeWidth;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextAnchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextDecoration;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Transform;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_UnicodeBidi;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_VectorEffect;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Visibility;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WordSpacing;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WritingMode;

import java.net.URI;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGAlignmentBaseLine;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGColor;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPaint;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform;

/**
 *  SVG elements that allow the presentation attributes
 *  {@value SVGUtils#SVGATTRIBUTE_AlignmentBaseline},
 *  {@value SVGUtils#SVGATTRIBUTE_BaselineShift},
 *  {@value SVGUtils#SVGATTRIBUTE_Clip},
 *  {@value SVGUtils#SVGATTRIBUTE_ClipPath},
 *  {@value SVGUtils#SVGATTRIBUTE_ClipRule},
 *  {@value SVGUtils#SVGATTRIBUTE_Color},
 *  {@value SVGUtils#SVGATTRIBUTE_ColorInterpolation},
 *  {@value SVGUtils#SVGATTRIBUTE_ColorInterpolationFilters},
 *  {@value SVGUtils#SVGATTRIBUTE_ColorProfile},
 *  {@value SVGUtils#SVGATTRIBUTE_ColorRendering},
 *  {@value SVGUtils#SVGATTRIBUTE_Cursor},
 *  {@value SVGUtils#SVGATTRIBUTE_Direction},
 *  {@value SVGUtils#SVGATTRIBUTE_Display},
 *  {@value SVGUtils#SVGATTRIBUTE_DominantBaseline},
 *  {@value SVGUtils#SVGATTRIBUTE_EnableBackground},
 *  {@value SVGUtils#SVGATTRIBUTE_Fill},
 *  {@value SVGUtils#SVGATTRIBUTE_FillOpacity},
 *  {@value SVGUtils#SVGATTRIBUTE_FillRule},
 *  {@value SVGUtils#SVGATTRIBUTE_Filter},
 *  {@value SVGUtils#SVGATTRIBUTE_FloodColor},
 *  {@value SVGUtils#SVGATTRIBUTE_FloodOpacity},
 *  {@value SVGUtils#SVGATTRIBUTE_FontFamily},
 *  {@value SVGUtils#SVGATTRIBUTE_FontSize},
 *  {@value SVGUtils#SVGATTRIBUTE_FontSizeAdjust},
 *  {@value SVGUtils#SVGATTRIBUTE_FontStretch},
 *  {@value SVGUtils#SVGATTRIBUTE_FontStyle},
 *  {@value SVGUtils#SVGATTRIBUTE_FontVariant},
 *  {@value SVGUtils#SVGATTRIBUTE_FontWeight},
 *  {@value SVGUtils#SVGATTRIBUTE_GlyphOrientationHorizontal},
 *  {@value SVGUtils#SVGATTRIBUTE_GlyphOrientationVertical},
 *  {@value SVGUtils#SVGATTRIBUTE_ImageRendering},
 *  {@value SVGUtils#SVGATTRIBUTE_Kerning},
 *  {@value SVGUtils#SVGATTRIBUTE_LetterSpacing},
 *  {@value SVGUtils#SVGATTRIBUTE_LightingColor},
 *  {@value SVGUtils#SVGATTRIBUTE_MarkerEnd},
 *  {@value SVGUtils#SVGATTRIBUTE_MarkerMid},
 *  {@value SVGUtils#SVGATTRIBUTE_MarkerStart},
 *  {@value SVGUtils#SVGATTRIBUTE_Mask},
 *  {@value SVGUtils#SVGATTRIBUTE_Opacity},
 *  {@value SVGUtils#SVGATTRIBUTE_Overflow},
 *  {@value SVGUtils#SVGATTRIBUTE_PointerEvents},
 *  {@value SVGUtils#SVGATTRIBUTE_ShapeRendering},
 *  {@value SVGUtils#SVGATTRIBUTE_StopColor},
 *  {@value SVGUtils#SVGATTRIBUTE_StopOpacity},
 *  {@value SVGUtils#SVGATTRIBUTE_Stroke},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeDashArray},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeDashOffset},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeLineCap},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeLineJoin},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeMiterLimit},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeOpacity},
 *  {@value SVGUtils#SVGATTRIBUTE_StrokeWidth},
 *  {@value SVGUtils#SVGATTRIBUTE_TextAnchor},
 *  {@value SVGUtils#SVGATTRIBUTE_TextDecoration},
 *  {@value SVGUtils#SVGATTRIBUTE_TextRendering},
 *  {@value SVGUtils#SVGATTRIBUTE_Transform},
 *  {@value SVGUtils#SVGATTRIBUTE_UnicodeBidi},
 *  {@value SVGUtils#SVGATTRIBUTE_VectorEffect},
 *  {@value SVGUtils#SVGATTRIBUTE_Visibility},
 *  {@value SVGUtils#SVGATTRIBUTE_WordSpacing},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_WritingMode}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsPresentationAttributes.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "unused"} )
@ClassVersion( sourceVersion = "$Id: AllowsPresentationAttributes.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsPresentationAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The presentation attributes.
     */
    public static final List<String> PRESENTATION_ATTRIBUTES = List.of(
        SVGATTRIBUTE_AlignmentBaseline,
        SVGATTRIBUTE_BaselineShift,
        SVGATTRIBUTE_Clip,
        SVGATTRIBUTE_ClipPath,
        SVGATTRIBUTE_ClipRule,
        SVGATTRIBUTE_Color,
        SVGATTRIBUTE_ColorInterpolation,
        SVGATTRIBUTE_ColorInterpolationFilters,
        SVGATTRIBUTE_ColorProfile,
        SVGATTRIBUTE_ColorRendering,
        SVGATTRIBUTE_Cursor,
        SVGATTRIBUTE_Direction,
        SVGATTRIBUTE_Display,
        SVGATTRIBUTE_DominantBaseline,
        SVGATTRIBUTE_EnableBackground,
        SVGATTRIBUTE_Fill,
        SVGATTRIBUTE_FillOpacity,
        SVGATTRIBUTE_FillRule,
        SVGATTRIBUTE_Filter,
        SVGATTRIBUTE_FloodColor,
        SVGATTRIBUTE_FloodOpacity,
        SVGATTRIBUTE_FontFamily,
        SVGATTRIBUTE_FontSize,
        SVGATTRIBUTE_FontSizeAdjust,
        SVGATTRIBUTE_FontStretch,
        SVGATTRIBUTE_FontStyle,
        SVGATTRIBUTE_FontVariant,
        SVGATTRIBUTE_FontWeight,
        SVGATTRIBUTE_GlyphOrientationHorizontal,
        SVGATTRIBUTE_GlyphOrientationVertical,
        SVGATTRIBUTE_ImageRendering,
        SVGATTRIBUTE_Kerning,
        SVGATTRIBUTE_LetterSpacing,
        SVGATTRIBUTE_LightingColor,
        SVGATTRIBUTE_MarkerEnd,
        SVGATTRIBUTE_MarkerMid,
        SVGATTRIBUTE_MarkerStart,
        SVGATTRIBUTE_Mask,
        SVGATTRIBUTE_Opacity,
        SVGATTRIBUTE_Overflow,
        SVGATTRIBUTE_PointerEvents,
        SVGATTRIBUTE_ShapeRendering,
        SVGATTRIBUTE_StopColor,
        SVGATTRIBUTE_StopOpacity,
        SVGATTRIBUTE_Stroke,
        SVGATTRIBUTE_StrokeDashArray,
        SVGATTRIBUTE_StrokeDashOffset,
        SVGATTRIBUTE_StrokeLineCap,
        SVGATTRIBUTE_StrokeLineJoin,
        SVGATTRIBUTE_StrokeMiterLimit,
        SVGATTRIBUTE_StrokeOpacity,
        SVGATTRIBUTE_StrokeWidth,
        SVGATTRIBUTE_TextAnchor,
        SVGATTRIBUTE_TextDecoration,
        SVGATTRIBUTE_TextRendering,
        SVGATTRIBUTE_Transform,
        SVGATTRIBUTE_UnicodeBidi,
        SVGATTRIBUTE_VectorEffect,
        SVGATTRIBUTE_Visibility,
        SVGATTRIBUTE_WordSpacing,
        SVGATTRIBUTE_WritingMode
    );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the presentation attribute {@code alignment-baseline} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_AlignmentBaseline
     */
    public void setAlignmentBaseline( final SVGAlignmentBaseLine value );

    /**
     *  Sets the presentation attribute {@code baseline-shift} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_BaselineShift
     */
    public void setBaselineShift( final String value );

    /**
     *  Sets the presentation attribute {@code clip} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Clip
     */
    public void setClip( final String value );

    /**
     *  Sets the {@code clip-path} attribute for this SVG element.
     *
     *  @param  value   The URI for the clip path.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ClipPath
     */
    public void setClipPath( final URI value );

    /**
     *  Sets the presentation attribute {@code clip-rule} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ClipRule
     */
    public void setClipRule( final String value );

    /**
     *  Sets the colour for this SVG element.
     *
     *  @param  value   The colour.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Color
     */
    public void setColor( final SVGColor value );

    /**
     *  Sets the presentation attribute {@code color-interpolation} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ColorInterpolation
     */
    public void setColorInterpolation( final String value );

    /**
     *  Sets the presentation attribute {@code color-interpolation-filters} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ColorInterpolationFilters
     */
    public void setColorInterpolationFilters( final String value );

    /**
     *  Sets the presentation attribute {@code color-profile} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ColorProfile
     */
    public void setColorProfile( final String value );

    /**
     *  Sets the presentation attribute {@code color-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ColorRendering
     */
    public void setColorRendering( final String value );

    /**
     *  Sets the presentation attribute {@code cursor} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Cursor
     */
    public void setCursor( final String value );

    /**
     *  Sets the presentation attribute {@code direction} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Direction
     */
    public void setDirection( final String value );

    /**
     *  Sets the presentation attribute {@code display} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Display
     */
    public void setDisplay( final String value );

    /**
     *  Sets the presentation attribute {@code dominant-baseline} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_DominantBaseline
     */
    public void setDominantBaseline( final String value );

    /**
     *  Sets the presentation attribute {@code enable-background} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_EnableBackground
     */
    public void setEnableBackground( final String value );

    /**
     *  Sets the presentation attribute {@code fill} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Fill
     */
    public void setFill( final SVGPaint value );

    /**
     *  Sets the presentation attribute {@code fill-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FillOpacity
     */
    public void setFillOpacity( final String value );

    /**
     *  Sets the presentation attribute {@code fill-rule} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FillRule
     */
    public void setFillRule( final String value );

    /**
     *  Sets the presentation attribute {@code filter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Filter
     */
    public void setFilter( final String value );

    /**
     *  Sets the presentation attribute {@code flood-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FloodColor
     */
    public void setFloodColor( final String value );

    /**
     *  Sets the presentation attribute {@code flood-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FloodOpacity
     */
    public void setFloodOpacity( final String value );

    /**
     *  Sets the presentation attribute {@code font-family} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontFamily
     */
    public void setFontFamily( final String value );

    /**
     *  Sets the presentation attribute {@code font-size} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontSize
     */
    public void setFontSize( final String value );

    /**
     *  Sets the presentation attribute {@code font-size-adjust} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontSizeAdjust
     */
    public void setFontSizeAdjust( final String value );

    /**
     *  Sets the presentation attribute {@code font-stretch} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontStretch
     */
    public void setFontStretch( final String value );

    /**
     *  Sets the presentation attribute {@code font-style} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontStyle
     */
    public void setFontStyle( final String value );

    /**
     *  Sets the presentation attribute {@code font-variant} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontVariant
     */
    public void setFontVariant( final String value );

    /**
     *  Sets the presentation attribute {@code font-weight} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_FontWeight
     */
    public void setFontWeight( final String value );

    /**
     *  Sets the presentation attribute {@code glyph-orientation-horizontal} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_GlyphOrientationHorizontal
     */
    public void setGlyphOrientationHorizontal( final String value );

    /**
     *  Sets the presentation attribute {@code glyph-orientation-vertical} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_GlyphOrientationVertical
     */
    public void setGlyphOrientationVertical( final String value );

    /**
     *  Sets the presentation attribute {@code image-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ImageRendering
     */
    public void setImageRendering( final String value );

    /**
     *  Sets the presentation attribute {@code kerning} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Kerning
     */
    public void setKerning( final String value );

    /**
     *  Sets the presentation attribute {@code letter-spacing} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_LetterSpacing
     */
    public void setLetterSpacing( final String value );

    /**
     *  Sets the presentation attribute {@code lighting-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_LightingColor
     */
    public void setLightingColor( final String value );

    /**
     *  Sets the {@code marker-end} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see SVGUtils#SVGATTRIBUTE_MarkerEnd
     */
    public void setMarkerEnd( final URI value );

    /**
     *  Sets the {@code marker-mid} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see SVGUtils#SVGATTRIBUTE_MarkerMid
     */
    public void setMarkerMid( final URI value );

    /**
     *  Sets the {@code marker-start} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see SVGUtils#SVGATTRIBUTE_MarkerStart
     */
    public void setMarkerStart( final URI value );

    /**
     *  Sets the presentation attribute {@code mask} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Mask
     */
    public void setMask( final String value );

    /**
     *  Sets the presentation attribute {@code opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Opacity
     */
    public void setOpacity( final String value );

    /**
     *  Sets the presentation attribute {@code overflow} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Overflow
     */
    public void setOverflow( final String value );

    /**
     *  Sets the presentation attribute {@code pointer-events} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_PointerEvents
     */
    public void setPointerEvents( final String value );

    /**
     *  Sets the presentation attribute {@code shape-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_ShapeRendering
     */
    public void setShapeRendering( final String value );

    /**
     *  Sets the presentation attribute {@code stop-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StopColor
     */
    public void setStopColor( final String value );

    /**
     *  Sets the presentation attribute {@code stop-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StopOpacity
     */
    public void setStopOpacity( final String value );

    /**
     *  Sets the presentation attribute {@code stroke} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Stroke
     */
    public void setStroke( final SVGPaint value );

    /**
     *  Sets the presentation attribute {@code stroke-dasharray} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeDashArray
     */
    public void setStrokeDashArray( final String value );

    /**
     *  Sets the presentation attribute {@code stroke-dashoffset} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeDashOffset
     */
    public void setStrokeDashOffset( final String value );

    /**
     *  Sets the presentation attribute {@code stroke-linecap} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeLineCap
     */
    public void setStrokeLineCap( final String value );

    /**
     *  Sets the presentation attribute {@code stroke-linejoin} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeLineJoin
     */
    public void setStrokeLineJoin( final String value );

    /**
     *  Sets the presentation attribute {@code stroke-miterlimit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeMiterLimit
     */
    public void setStrokeMiterLimit( final String value );

    /**
     *  Sets the presentation attribute {@code stroke-opacity} for this SVG
     *  element.<br>
     *  <br>Any type outside the range 0.0 (fully transparent) to 1.0 (fully
     *  opaque) will be clamped to this range.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeOpacity
     */
    public void setStrokeOpacity( final double value );

    /**
     *  Sets the presentation attribute {@code stroke-width} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_StrokeWidth
     */
    public void setStrokeWidth( final SVGNumber value );

    /**
     *  Sets the presentation attribute {@code text-anchor} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_TextAnchor
     */
    public void setTextAnchor( final String value );

    /**
     *  Sets the presentation attribute {@code text-decoration} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_TextDecoration
     */
    public void setTextDecoration( final String value );

    /**
     *  Sets the presentation attribute {@code text-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_TextRendering
     */
    public void setTextRendering( final String value );

    /**
     *  Sets the transformations for this SVG element.
     *
     *  @param  values   The transformations.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Transform
     */
    public void setTransform( final SVGTransform... values );

    /**
     *  Sets the presentation attribute {@code unicode-bidi} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_UnicodeBidi
     */
    public void setUnicodeBidi( final String value );

    /**
     *  Sets the presentation attribute {@code vector-effect} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_VectorEffect
     */
    public void setVectorEffect( final String value );

    /**
     *  Sets the presentation attribute {@code visibility} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_Visibility
     */
    public void setVisibility( final String value );

    /**
     *  Sets the presentation attribute {@code word-spacing} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_WordSpacing
     */
    public void setWordSpacing( final String value );

    /**
     *  Sets the presentation attribute {@code writing-mode} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_WritingMode
     */
    public void setWritingMode( final String value );
}
//  interface AllowsPresentationAttributes

/*
 *  End of File
 */