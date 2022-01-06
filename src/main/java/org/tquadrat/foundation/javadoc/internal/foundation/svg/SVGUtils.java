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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Class;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVG.Usage.EMBED_SVG;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGColor.COLOR_INHERIT;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGElementCategory.retrieveElementCategory;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.CENTIMETER;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.EM;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.EX;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.INCH;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.PICA;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit.POINT;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLBuilderUtils.createXMLDocument;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.ToolKit;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.UnsupportedEnumError;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVG.Usage;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGClipPathImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGGroupImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGLineImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGMarkerImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGPathImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGPositionedMarkerImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGRectangleImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGStyleImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGSymbolImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGTSpanImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGTextImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGUseImpl;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGColor;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGDegree;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGMillimeter;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGPercent;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGPixel;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGUserUnitValue;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGArcTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGClosePath;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGCubicCurveTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGHLineTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGLineTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGMoveTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGQuadraticCurveTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGVLineTo;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGMatrix;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGRotate;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGScale;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGSkewX;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGSkewY;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform.SVGTranslate;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLDocument;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.XMLElementAdapter;

/**
 *  A collection of SVG related utility methods and factory methods for SVG
 *  elements.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGUtils.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass", "OverlyCoupledClass"} )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: SVGUtils.java 977 2022-01-06 11:41:03Z tquadrat $" )
public final class SVGUtils
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The default namespace prefix for SVG: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVG_PREFIX = "svg";

    /**
     *  The name for the SVG presentation attribute {@code alignment-baseline}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_AlignmentBaseline = "alignment-baseline";

    /**
     *  The name for the SVG presentation attribute {@code baseline-shift}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_BaselineShift = "baseline-shift";

    /**
     *  The name for the SVG base profile attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_BaseProfile = "baseProfile";

    /**
     *  The name for the attribute for the CSS class of an SVG element:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Class = XMLATTRIBUTE_Class;

    /**
     *  The name for the SVG presentation attribute {@code clip}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Clip = "clip";

    /**
     *  The name for the SVG presentation attribute {@code clip-path}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ClipPath = "clip-path";

    /**
     *  The name for the SVG presentation attribute {@code clip-rule}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ClipRule = "clip-rule";

    /**
     *  The name for the SVG {@code <clipPath>} element attribute
     *  {@code clipPathUnits} that defines the coordinate system used by the
     *  element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ClipPathUnits = "clipPathUnits";

    /**
     *  The name for the SVG presentation attribute {@code color}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Color = "color";

    /**
     *  The name for the SVG presentation attribute {@code color-interpolation}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ColorInterpolation = "color-interpolation";

    /**
     *  The name for the SVG presentation attribute {@code color-interpolation-filters}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ColorInterpolationFilters = "color-interpolation-filters";

    /**
     *  The name for the SVG presentation attribute {@code color-profile}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ColorProfile = "color-profile";

    /**
     *  The name for the SVG presentation attribute {@code color-rendering}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ColorRendering = "color-rendering";

    /**
     *  The name for the default script language attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ContentScriptType = "contentScriptType";

    /**
     *  The name for the SVG presentation attribute {@code cursor}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Cursor = "cursor";

    /**
     *  The name for the SVG presentation attribute {@code direction}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Direction = "direction";

    /**
     *  The name for the SVG presentation attribute {@code display}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Display = "display";

    /**
     *  The name for the SVG presentation attribute {@code dominant-baseline}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_DominantBaseline = "dominant-baseline";

    /**
     *  The name for the SVG {@code dx} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_dx = "dx";

    /**
     *  The name for the SVG {@code dy} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_dy = "dy";

    /**
     *  The name for the SVG presentation attribute {@code enable-background}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_EnableBackground = "enable-background";

    /**
     *  The name for the SVG attribute {@code externalResourcesRequired}:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ExternalResourcesRequired = "externalResourcesRequired";

    /**
     *  The name for the SVG presentation attribute {@code fill}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Fill = "fill";

    /**
     *  The name for the SVG presentation attribute {@code fill-opacity}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FillOpacity = "fill-opacity";

    /**
     *  The name for the SVG presentation attribute {@code fill-rule}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FillRule = "fill-rule";

    /**
     *  The name for the SVG presentation attribute {@code filter}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Filter = "filter";

    /**
     *  The name for the SVG presentation attribute {@code flood-color}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FloodColor = "flood-color";

    /**
     *  The name for the SVG presentation attribute {@code flood-opacity}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FloodOpacity = "flood-opacity";

    /**
     *  The name for the SVG presentation attribute {@code font-family}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontFamily = "font-family";

    /**
     *  The name for the SVG presentation attribute {@code font-size}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontSize = "font-size";

    /**
     *  The name for the SVG presentation attribute {@code font-size-adjust}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontSizeAdjust = "font-size-adjust";

    /**
     *  The name for the SVG presentation attribute {@code font-stretch}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontStretch = "font-stretch";

    /**
     *  The name for the SVG presentation attribute {@code font-style}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontStyle = "font-style";

    /**
     *  The name for the SVG presentation attribute {@code font-variant}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontVariant = "font-variant";

    /**
     *  The name for the SVG presentation attribute {@code font-weight}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_FontWeight = "font-weight";

    /**
     *  The name for the SVG presentation attribute {@code glyph-orientation-horizontal}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_GlyphOrientationHorizontal = "glyph-orientation-horizontal";

    /**
     *  The name for the SVG presentation attribute {@code glyph-orientation-vertical}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_GlyphOrientationVertical = "glyph-orientation-vertical";

    /**
     *  The name for the SVG {@code height} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Height = "height";

    /**
     *  The name for the SVG {@code id} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Id = "id";

    /**
     *  The name for the SVG presentation attribute {@code image-rendering}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ImageRendering = "image-rendering";

    /**
     *  The name for the SVG presentation attribute {@code kerning}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Kerning = "kerning";

    /**
     *  The name for the SVG {@code lang} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Lang = "lang";

    /**
     *  The name for the SVG {@code lengthAdjust} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_LengthAdjust = "lengthAdjust";

    /**
     *  The name for the SVG presentation attribute {@code letter-spacing}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_LetterSpacing = "letter-spacing";

    /**
     *  The name for the SVG presentation attribute {@code lighting-color}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_LightingColor = "lighting-color";

    /**
     *  The name for the SVG presentation attribute {@code marker-end}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerEnd = "marker-end";

    /**
     *  The name for the SVG marker attribute {@code markerHeight}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerHeight = "markerHeight";

    /**
     *  The name for the SVG presentation attribute {@code marker-mid}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerMid = "marker-mid";

    /**
     *  The name for the SVG presentation attribute {@code marker-start}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerStart = "marker-start";

    /**
     *  The name for the SVG marker attribute {@code markerUnits}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerUnits = "markerUnits";

    /**
     *  The name for the SVG marker attribute {@code markerWidth}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_MarkerWidth = "markerWidth";

    /**
     *  The name for the SVG presentation attribute {@code mask}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Mask = "mask";

    /**
     *  The name for the SVG event attribute {@code onabort}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnAbort = "onabort";

    /**
     *  The name for the SVG event attribute {@code onactivate}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnActivate = "onactivate";

    /**
     *  The name for the SVG event attribute {@code oncancel}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCancel = "oncancel";

    /**
     *  The name for the SVG event attribute {@code oncanplay}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCanPlay = "oncanplay";

    /**
     *  The name for the SVG event attribute {@code oncanplaythrough}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCanPlayThrough = "oncanplaythrough";

    /**
     *  The name for the SVG event attribute {@code onchange}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnChange = "onchange";

    /**
     *  The name for the SVG event attribute {@code onclick}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnClick = "onclick";

    /**
     *  The name for the SVG event attribute {@code onclose}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnClose = "onclose";

    /**
     *  The name for the SVG document element event attribute {@code oncopy}:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCopy = "oncopy";

    /**
     *  The name for the SVG event attribute {@code oncuechange}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCueChange = "oncuechange";

    /**
     *  The name for the SVG document element event attribute {@code oncut}:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnCut = "oncut";

    /**
     *  The name for the SVG event attribute {@code ondblclick}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDblClick = "ondblclick";

    /**
     *  The name for the SVG event attribute {@code ondrag}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDrag = "ondrag";

    /**
     *  The name for the SVG event attribute {@code ondragend}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragEnd = "ondragend";

    /**
     *  The name for the SVG event attribute {@code ondragenter}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragEnter = "ondragenter";

    /**
     *  The name for the SVG event attribute {@code ondragexit}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragExit = "ondragexit";

    /**
     *  The name for the SVG event attribute {@code ondragleave}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragLeave = "ondragleave";

    /**
     *  The name for the SVG event attribute {@code ondragover}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragOver = "ondragover";

    /**
     *  The name for the SVG event attribute {@code ondragstart}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDragStart = "ondragstart";

    /**
     *  The name for the SVG event attribute {@code ondrop}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDrop = "ondrop";

    /**
     *  The name for the SVG event attribute {@code ondurationchange}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnDurationChange = "ondurationchange";

    /**
     *  The name for the SVG event attribute {@code onemptied}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnEmptied = "onemptied";

    /**
     *  The name for the SVG event attribute {@code onended}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnEnded = "onended";

    /**
     *  The name for the SVG event attribute {@code onerror}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnError = "onerror";

    /**
     *  The name for the SVG event attribute {@code onfocus}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnFocus = "onfocus";

    /**
     *  The name for the SVG event attribute {@code onfocusin}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnFocusIn = "onfocusin";

    /**
     *  The name for the SVG event attribute {@code onfocusout}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnFocusOut = "onfocusout";

    /**
     *  The name for the SVG event attribute {@code oninput}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnInput = "oninput";

    /**
     *  The name for the SVG event attribute {@code oninvalid}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnInvalid = "oninvalid";

    /**
     *  The name for the SVG event attribute {@code onkeydown}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnKeyDown = "onkeydown";

    /**
     *  The name for the SVG event attribute {@code onkeypress}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnKeyPress = "onkeypress";

    /**
     *  The name for the SVG event attribute {@code onkeyup}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnKeyUp = "onkeyup";

    /**
     *  The name for the SVG event attribute {@code onload}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnLoad = "onload";

    /**
     *  The name for the SVG event attribute {@code onloadeddata}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnLoadedData = "onloadeddata";

    /**
     *  The name for the SVG event attribute {@code onloadedmetadata}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnLoadedMetadata = "onloadedmetadata";

    /**
     *  The name for the SVG event attribute {@code onloadstart}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnLoadStart = "onloadstart";

    /**
     *  The name for the SVG event attribute {@code onmousedown}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseDown = "onmousedown";

    /**
     *  The name for the SVG event attribute {@code onmouseenter}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseEnter = "onmouseenter";

    /**
     *  The name for the SVG event attribute {@code onmouseleave}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseLeave = "onmouseleave";

    /**
     *  The name for the SVG event attribute {@code onmousemove}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseMove = "onmousemove";

    /**
     *  The name for the SVG event attribute {@code onmouseout}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseOut = "onmouseout";

    /**
     *  The name for the SVG event attribute {@code onmouseover}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseOver = "onmouseover";

    /**
     *  The name for the SVG event attribute {@code onmouseup}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseUp = "onmouseup";

    /**
     *  The name for the SVG event attribute {@code onmousewheel}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnMouseWheel = "onmousewheel";

    /**
     *  The name for the SVG document element event attribute {@code onpaste}:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnPaste = "onpaste";

    /**
     *  The name for the SVG event attribute {@code onpause}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnPause = "onpause";

    /**
     *  The name for the SVG event attribute {@code onplay}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnPlay = "onplay";

    /**
     *  The name for the SVG event attribute {@code onplaying}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnPlaying = "onplaying";

    /**
     *  The name for the SVG event attribute {@code onprogress}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnProgress = "onprogress";

    /**
     *  The name for the SVG event attribute {@code onratechange}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnRateChange = "onratechange";

    /**
     *  The name for the SVG event attribute {@code onreset}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnReset = "onreset";

    /**
     *  The name for the SVG event attribute {@code onresize}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnResize = "onresize";

    /**
     *  The name for the SVG event attribute {@code onscroll}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnScroll = "onscroll";

    /**
     *  The name for the SVG event attribute {@code onseeked}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnSeeked = "onseeked";

    /**
     *  The name for the SVG event attribute {@code onseeking}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnSeeking = "onseeking";

    /**
     *  The name for the SVG event attribute {@code onselect}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnSelect = "onselect";

    /**
     *  The name for the SVG event attribute {@code onshow}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnShow = "onshow";

    /**
     *  The name for the SVG event attribute {@code onstalled}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnStalled = "onstalled";

    /**
     *  The name for the SVG event attribute {@code onsubmit}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnSubmit = "onsubmit";

    /**
     *  The name for the SVG event attribute {@code onsuspend}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnSuspend = "onsuspend";

    /**
     *  The name for the SVG event attribute {@code ontimeupdate}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnTimeUpdate = "ontimeupdate";

    /**
     *  The name for the SVG event attribute {@code ontoggle}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnToggle = "ontoggle";

    /**
     *  The name for the SVG event attribute {@code onunload}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnUnload = "onunload";

    /**
     *  The name for the SVG event attribute {@code onvolumechange}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnVolumeChange = "onvolumechange";

    /**
     *  The name for the SVG event attribute {@code onwaiting}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_OnWaiting = "onwaiting";

    /**
     *  The name for the SVG presentation attribute {@code opacity}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Opacity = "opacity";

    /**
     *  The name for the SVG marker attribute {@code orient}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Orientation = "orient";

    /**
     *  The name for the SVG presentation attribute {@code overflow}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Overflow = "overflow";

    /**
     *  The name for the SVG attribute {@code d} that defines a path: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_PathDefinition = "d";

    /**
     *  The name for the SVG attribute {@code pathLength}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_PathLength = "pathLength";

    /**
     *  The name for the SVG presentation attribute {@code pointer-events}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_PointerEvents = "pointer-events";

    /**
     *  The name for the SVG marker attribute {@code position}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Position = "position";

    /**
     *  The name for the SVG {@code preserveAspectRatio} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_PreserveAspectRatio = "preserveAspectRatio";

    /**
     *  The name for the attribute that is used to set references in SVG:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Reference = "xlink:href";

    /**
     *  The name for the SVG marker attribute {@code refX}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ReferenceX = "refX";

    /**
     *  The name for the SVG marker attribute {@code refY}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ReferenceY = "refY";

    /**
     *  The name for the SVG attribute that defines the extensions that are
     *  required to render an element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_RequiredExtensions = "requiredExtensions";

    /**
     *  The name for the SVG attribute that defines the features that are
     *  required to render an element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_RequiredFeatures = "requiredFeatures";

    /**
     *  The name for the SVG {@code rotate} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Rotate = "rotate";

    /**
     *  The name for the SVG {@code rx} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_rx = "rx";

    /**
     *  The name for the SVG {@code ry} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ry = "ry";

    /**
     *  The name for the SVG presentation attribute {@code shape-rendering}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ShapeRendering = "shape-rendering";

    /**
     *  The name for the SVG presentation attribute {@code stop-color}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StopColor = "stop-color";

    /**
     *  The name for the SVG presentation attribute {@code stop-opacity}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StopOpacity = "stop-opacity";

    /**
     *  The name for the SVG presentation attribute {@code stroke}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Stroke = "stroke";

    /**
     *  The name for the SVG presentation attribute {@code stroke-dasharray}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeDashArray = "stroke-dasharray";

    /**
     *  The name for the SVG presentation attribute {@code stroke-dashoffset}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeDashOffset = "stroke-dashoffset";

    /**
     *  The name for the SVG presentation attribute {@code stroke-linecap}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeLineCap = "stroke-linecap";

    /**
     *  The name for the SVG presentation attribute {@code stroke-linejoin}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeLineJoin = "stroke-linejoin";

    /**
     *  The name for the SVG presentation attribute {@code stroke-miterlimit}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeMiterLimit = "stroke-miterlimit";

    /**
     *  The name for the SVG presentation attribute {@code stroke-opacity}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeOpacity = "stroke-opacity";

    /**
     *  The name for the SVG presentation attribute {@code stroke-width}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_StrokeWidth = "stroke-width";

    /**
     *  The name for the SVG CSS {@code style} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Style = "style";

    /**
     *  The name for the SVG attribute that defines the languages that are
     *  supported by an SVG element: {@value}.<br>
     *  <br>It will be rendered only when the current system language matches
     *  one entry in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_SystemLanguage = "systemLanguage";

    /**
     *  The name for the SVG {@code tabindex} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_TabIndex = "tabindex";

    /**
     *  The name for the SVG presentation attribute {@code text-anchor}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_TextAnchor = "text-anchor";

    /**
     *  The name for the SVG presentation attribute {@code text-decoration}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_TextDecoration = "text-decoration";

    /**
     *  The name for the SVG {@code textLength} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_TextLength = "textLength";

    /**
     *  The name for the SVG presentation attribute {@code text-rendering}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_TextRendering = "text-rendering";

    /**
     *  The name for the SVG presentation attribute {@code transform}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Transform = "transform";

    /**
     *  The name for the SVG presentation attribute {@code unicode-bidi}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_UnicodeBidi = "unicode-bidi";

    /**
     *  The name for the SVG presentation attribute {@code vector-effect}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_VectorEffect = "vector-effect";

    /**
     *  The name for the SVG {@code version} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Version = "version";

    /**
     *  The name for the SVG {@code viewBox} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ViewBox = "viewBox";

    /**
     *  The name for the SVG presentation attribute {@code visibility}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Visibility = "visibility";

    /**
     *  The name for the SVG {@code width} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_Width = "width";

    /**
     *  The name for the SVG presentation attribute {@code word-spacing}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_WordSpacing = "word-spacing";

    /**
     *  The name for the SVG presentation attribute {@code writing-mode}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_WritingMode = "writing-mode";

    /**
     *  The name for the SVG {@code x} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_x = "x";

    /**
     *  The name for the SVG {@code x1} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_x1 = "x1";

    /**
     *  The name for the SVG {@code x2} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_x2 = "x2";

    /**
     *  The name for the XLink {@code actuate} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Actuate = "xlink:actuate";

    /**
     *  The name for the XLink {@code arcrole} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_ArcRole = "xlink:arcrole";

    /**
     *  The name for the XLink attribute that is used to set references in SVG:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Reference = "xlink:href";

    /**
     *  The name for the XLink {@code role} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Role = "xlink:role";

    /**
     *  The name for the XLink {@code show} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Show = "xlink:show";

    /**
     *  The name for the XLink {@code title} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Title = "xlink:title";

    /**
     *  The name for the XLink {@code type} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_XLink_Type = "xlink:type";

    /**
     *  The name for the SVG {@code y} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_y = "y";

    /**
     *  The name for the SVG {@code y1} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_y1 = "y1";

    /**
     *  The name for the SVG {@code y2} coordinate attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_y2 = "y2";

    /**
     *  The name for the SVG {@code zoomAndPan} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGATTRIBUTE_ZoomAndPan = "zoomAndPan";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AltGlyph = "altGlyph";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AltGlyphDef = "altGlyphDef";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AltGlyphItem = "altGlyphItem";

    /**
     *  The name for the SVG anchor element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Anchor = "a";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Animate = "animate";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AnimateColor = "animateColor";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AnimateMotion = "animateMotion";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_AnimateTransform = "animateTransform";

    /**
     *  The name for the SVG element representing a circle shape: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Circle = "circle";

    /**
     *  The name for the SVG element that defines a clipping path: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_ClipPath = "clipPath";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_ColorProfile = "colorProfile";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Cursor = "cursor";

    /**
     *  The name for the SVG element that holds definitions of elements that
     *  will be referenced elsewhere: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Defs = "defs";

    /**
     *  The name for the SVG element that can be added to any other element to
     *  assign a description to it: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Description = "desc";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Discard = "discard";

    /**
     *  The name for the SVG element representing an ellipse shape: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Ellipse = "ellipse";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter = "filter";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feBlend = "feBlend";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feColorMatrix = "feColorMatrix";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feComponentTransfer = "feComponentTransfer";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feComposite = "feComposite";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feConvolveMatrix = "feConvolveMatrix";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feDiffuseLighting = "feDiffuseLighting";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feDisplacementMap = "feDisplacementMap";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feDropShadow = "feDropShadow";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feFlood = "feFlood";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feFuncA = "feFuncA";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feFuncB = "feFuncB";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feFuncG = "feFuncG";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feFuncR = "feFuncR";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feGaussianBlur = "feGaussianBlur";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feImage = "feImage";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feMerge = "feMerge";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feMergeNode = "feMergeNode";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feMorphology = "feMorphology";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feOffset = "feOffset";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feSpecularLighting = "feSpecularLighting";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feTile = "feTile";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Filter_feTurbulence = "feTurbulence";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Font = "font";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_FontFace = "font-face";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_FontFaceFormat = "font-face-format";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_FontFaceName = "font-face-name";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_FontFaceSource = "font-face-src";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_FontFaceURI = "font-face-uri";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_ForeignObject = "foreignObject";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Glyph = "glyph";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_GlyphRef = "glyphRef";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_GradientLinear = "linearGradient";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_GradientMesh = "meshGradient";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_GradientRadial = "radialGradient";

    /**
     *  The name for the SVG group element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Group = "g";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Hatch = "hatch";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_HatchPath = "hatchPath";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_HKern = "hkern";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Image = "image";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Light_feDistantLight = "feDistantLight";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Light_fePointLight = "fePointLight";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Light_feSpotLight = "feSpotLight";

    /**
     *  The name for the SVG element representing a line: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Line = "line";

    /**
     *  The name for the SVG {@code <marker>} element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Marker = "marker";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Mask = "mask";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Mesh = "mesh";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_MeshPatch = "meshPatch";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_MeshRow = "meshRow";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Metadata = "metadata";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_MissingGlyph = "missing-glyph";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_MPath = "mpath";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Path = "path";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Pattern = "pattern";

    /**
     *  The name for the SVG element representing a polygon shape: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Polygon = "polygon";

    /**
     *  The name for the SVG element representing a poly line: {@value}.
     */
    @SuppressWarnings( "SpellCheckingInspection" )
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_PolyLine = "polyline";

    /**
     *  The name for the SVG element representing a rectangle shape: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Rectangle = "rect";

    /**
     *  The name for the SVG root element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Root = "svg";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Script = "script";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Set = "set";

    /**
     *  The name for the SVG element {@code <solidcolor>}: {@value}.<br>
     *  <br>The form {@code <solidColor>} had been abandoned before the final
     *  specification was published.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_SolidColor = "solidcolor";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Stop = "stop";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Style = "style";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Switch = "switch";

    /**
     *  The name for the SVG {@code <symbol>} element that allows to create
     *  reusable parts of an image: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Symbol = "symbol";

    /**
     *  The name for an SVG element holding text:
     *  {@value ToolKit#XMLELEMENT_Text}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Text = ToolKit.XMLELEMENT_Text;

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_TextPath = "textPath";

    /**
     *  The name for the SVG element that can be added to any other element to
     *  assign a title to it: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Title = "title";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_TRef = "tref";

    /**
     *  The name for the SVG {@code <tspan>} element: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_TSpan = "tspan";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Unknown = "unknown";

    /**
     *  The name for the SVG element that is used to clone other elements:
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_Use = "use";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_View = "view";

    /**
     *  The name for the SVG element
     *  TODO - Add the proper description!
     *  {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String SVGELEMENT_VKern = "vkern";

    /**
     *  The name for the XML {@code base} attribute: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String XMLATTRIBUTE_Base = "xml:base";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private SVGUtils() { throw new PrivateConstructorForStaticClassCalledError( SVGUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates an instance of the
     *  {@linkplain org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement path element}
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGArcTo}
     *  that uses relative coordinates.
     *
     *  @param  rx  The x radius for the ellipsis.
     *  @param  ry  The y radius for the ellipsis.
     *  @param  rotation    The rotation of the x-axis.
     *  @param  largeArc    {@code true} to draw the larger arc,
     *      {@code false} for the smaller arc.
     *  @param  sweep   {@code true} to draw the arc in
     *      &quot;positive-angle&quot; direction, {@code false} for
     *      drawing it in a &quot;negative-angle&quot; direction.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @SuppressWarnings( "BooleanParameter" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement arcTo( final double rx, final double ry, final double rotation, final boolean largeArc, final boolean sweep, final double x, final double y )
    {
        final var retValue = new SVGArcTo( false, rx, ry, rotation, largeArc, sweep, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  arcTo()

    /**
     *  Creates an instance of the
     *  {@linkplain org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement path element}
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGArcTo}
     *  that uses relative coordinates.
     *
     *  @param  rx  The x radius for the ellipsis.
     *  @param  ry  The y radius for the ellipsis.
     *  @param  rotation    The rotation of the x-axis.
     *  @param  largeArc    {@code true} to draw the larger arc,
     *      {@code false} for the smaller arc.
     *  @param  sweep   {@code true} to draw the arc in
     *      &quot;positive-angle&quot; direction, {@code false} for
     *      drawing it in a &quot;negative-angle&quot; direction.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @SuppressWarnings( "BooleanParameter" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement arcTo( final long rx, final long ry, final long rotation, final boolean largeArc, final boolean sweep, final long x, final long y )
    {
        final var retValue = new SVGArcTo( false, rx, ry, rotation, largeArc, sweep, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  arcTo()

    /**
     *  Creates an instance of the
     *  {@linkplain org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement path element}
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGArcTo}
     *  that uses absolute coordinates.
     *
     *  @param  rx  The x radius for the ellipsis.
     *  @param  ry  The y radius for the ellipsis.
     *  @param  rotation    The rotation of the x-axis.
     *  @param  largeArc    {@code true} to draw the larger arc,
     *      {@code false} for the smaller arc.
     *  @param  sweep   {@code true} to draw the arc in
     *      &quot;positive-angle&quot; direction, {@code false} for
     *      drawing it in a &quot;negative-angle&quot; direction.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @SuppressWarnings( "BooleanParameter" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement arcToAbs( final double rx, final double ry, final double rotation, final boolean largeArc, final boolean sweep, final double x, final double y )
    {
        final var retValue = new SVGArcTo( true, rx, ry, rotation, largeArc, sweep, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  arcToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement path element}
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPathElement.SVGArcTo}
     *  that uses absolute coordinates.
     *
     *  @param  rx  The x radius for the ellipsis.
     *  @param  ry  The y radius for the ellipsis.
     *  @param  rotation    The rotation of the x-axis.
     *  @param  largeArc    {@code true} to draw the larger arc,
     *      {@code false} for the smaller arc.
     *  @param  sweep   {@code true} to draw the arc in
     *      &quot;positive-angle&quot; direction, {@code false} for
     *      drawing it in a &quot;negative-angle&quot; direction.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @SuppressWarnings( "BooleanParameter" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement arcToAbs( final long rx, final long ry, final long rotation, final boolean largeArc, final boolean sweep, final long x, final long y )
    {
        final var retValue = new SVGArcTo( true, rx, ry, rotation, largeArc, sweep, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  arcToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#CENTIMETER}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGNumber centimeter( final double value ) { return new SVGNumber( value, CENTIMETER ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#CENTIMETER}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGNumber centimeter( final long value ) { return new SVGNumber( value, CENTIMETER ); }

    /**
     *  Clones the given element.<br>
     *  <br>The clone will be semantically identical to the given element (it
     *  looks the same in the final output), but it will not be of the same
     *  Java type.
     *
     *  @param  element The element to clone.
     *  @return The clone.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGElement cloneElement( final SVGElement element )
    {
        final var elementName = requireNonNullArgument( element, "element" ).getElementName();
        final var xmlElement = ((XMLElementAdapter) element);
        final var flags = xmlElement.getFlags().toArray( Flags[]::new );
        final var retValue = new SVGGenericElement( elementName, flags );

        retValue.registerValidAttributes( xmlElement.retrieveValidAttributes().toArray( String []::new ) );
        retValue.registerValidChildren( xmlElement.retrieveValidChildren().toArray( String []::new ) );

        element.getAttributes().forEach( retValue::setAttribute );
        element.getChildren().forEach( c -> retValue.addChild( cloneElement( (SVGElement) c ) ) );
        element.getNamespaces().forEach( retValue::setNamespace );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cloneElement()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGClosePath}
     *  that uses relative coordinates.
     *
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement closePath()
    {
        final var retValue = new SVGClosePath();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  closePath()

    /**
     *  Returns the
     *  {@link SVGColor}
     *  instance with the type &quot;inherit&quot;.
     *
     *  @return The instance of {@code SVGColor}.
     *
     *  @see SVGColor#COLOR_INHERIT
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGColor color() { return COLOR_INHERIT; }

    /**
     *  Creates a new
     *  {@link SVGColor}
     *  instance from the given colour values.<br>
     *  <br>Allowed are the values from 0 to 255, other values will be
     *  normalised accordingly.
     *
     *  @param  red The red component for the colour.
     *  @param  green   The green component for the colour.
     *  @param  blue    The blue component for the colour.
     *  @return The new instance of {@code SVGColor}.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGColor color( final int red, final int green, final int blue )
    {
        final var retValue = new SVGColor( red, green, blue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  color()

    /**
     *  Creates a new
     *  {@link SVGColor}
     *  instance from the given colour values.<br>
     *  <br>Allowed are the values from 0 to 255, or 0% to 100% respectively,
     *  other values will be normalised accordingly.
     *
     *  @param  flag    {@code true} if the given values are percentages,
     *      {@code false} if they are absolute values.
     *  @param  red The red component for the colour.
     *  @param  green   The green component for the colour.
     *  @param  blue    The blue component for the colour.
     *  @return The new instance of {@code SVGColor}.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGColor color( final boolean flag, final int red, final int green, final int blue )
    {
        final var retValue = new SVGColor( flag, red, green, blue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  color()

    /**
     *  Creates a new
     *  {@link SVGColor}
     *  instance, using the given argument as a CSS colour name.<br>
     *  <br>The given argument may not be {@code null} nor the empty String,
     *  but it will not undergo any further validation.
     *
     *  @param  color   The CSS colour name.
     *  @return The new instance of {@code SVGColor}.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGColor color( final String color ) { return new SVGColor( color ); }

    /**
     *  Creates the namespace instance that is used for all Creative Commons
     *  stuff.
     *
     *  @return The new namespace instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private static final Namespace createCCNamespace()
    {
        final var identifier = URI.create( "http://creativecommons.org/ns#" );
        final var retValue = new Namespace( "cc", identifier );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCCNamespace()

    /**
     *  Creates an SVG {@code <clipPath>} element instance.
     *
     *  @param  id  The id for the new {@code <marker>} element.
     *  @return The new {@code <clipPath>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGClipPath createClipPath( final String id )
    {
        final var retValue = new SVGClipPathImpl( id );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createClipPath()

    /**
     *  Creates an SVG {@code <clipPath>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  id  The id for the new {@code <marker>} element.
     *  @param  parent  The parent {@code <svg>} element.
     *  @return The new {@code <clipPath>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGClipPath createClipPath( final String id, final SVG parent )
    {
        final var retValue = createClipPath( id );
        requireNonNullArgument( parent, "parent" ).addDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createClipPath()

    /**
     *  Creates a generic SVG element.<br>
     *  <br>This method should be used to create an SVG element that does not
     *  have (yet) its specific implementation class. It does not work to
     *  introduce completely new, still unknown SVG elements, as it checks the
     *  given element name.<br>
     *  <br>The returned instance will also check which attributes are allowed
     *  for the element it represents as well as which children are allowed to
     *  it.
     *
     *  @param  elementName The name for the new element.
     *  @return The new generic SVG element.
     *  @throws IllegalArgumentException    The given element name does not
     *      denote a define SVG element.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGGenericElement createGenericElement( final String elementName )
    {
        if( retrieveElementCategory( elementName ).isEmpty() )
        {
            throw new IllegalArgumentException( format( "Unknown element name: %s", elementName ) );
        }

        final var retValue = new SVGGenericElement( elementName );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createGenericElement()

    /**
     *  Creates an SVG {@code <g>} element instance.
     *
     *  @return The new {@code <g>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGGroup createGroup() { return new SVGGroupImpl(); }

    /**
     *  Creates an SVG {@code <g>} element instance.
     *
     *  @param  parent  The parent for the new {@code <g>} element.
     *  @return The new {@code <g>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGGroup createGroup( final SVGElementWithChildren parent )
    {
        final var retValue = createGroup();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createGroup()

    /**
     *  Creates an SVG {@code <line>} element instance.
     *
     *  @return The new {@code <line>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGLine createLine() { return new SVGLineImpl(); }

    /**
     *  Creates an SVG {@code <line>} element instance and adds it to the
     *  given parent element.
     *
     *  @param  parent  The parent element.
     *  @return The new {@code <line>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGLine createLine( final SVGElementWithChildren parent )
    {
        final var retValue = createLine();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createLine()

    /**
     *  Creates an SVG {@code <line>} element instance with the given start and
     *  end points.
     *
     *  @param  x1  The x coordinate for the starting point of the line.
     *  @param  y1  The y coordinate for the starting point of the line.
     *  @param  x2  The x coordinate for the ending point of the line.
     *  @param  y2  The y coordinate for the ending point of the line.
     *  @return The new {@code <line>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGLine createLine( final SVGNumber x1, final SVGNumber y1, final SVGNumber x2, final SVGNumber y2 )
    {
        final var retValue = createLine();
        retValue.setX1( requireNonNullArgument( x1, "x1" ) );
        retValue.setY1( requireNonNullArgument( y1, "y1" ) );
        retValue.setX2( requireNonNullArgument( x2, "x2" ) );
        retValue.setY2( requireNonNullArgument( y2, "y2" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createLine()

    /**
     *  Creates an SVG {@code <line>} element instance with the given start and
     *  end points and adds it to the given parent element.
     *
     *  @param  parent  The parent element.
     *  @param  x1  The x coordinate for the starting point of the line.
     *  @param  y1  The y coordinate for the starting point of the line.
     *  @param  x2  The x coordinate for the ending point of the line.
     *  @param  y2  The y coordinate for the ending point of the line.
     *  @return The new {@code <line>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGLine createLine( final SVGElementWithChildren parent, final SVGNumber x1, final SVGNumber y1, final SVGNumber x2, final SVGNumber y2 )
    {
        final var retValue = createLine();
        retValue.setX1( requireNonNullArgument( x1, "x1" ) );
        retValue.setY1( requireNonNullArgument( y1, "y1" ) );
        retValue.setX2( requireNonNullArgument( x2, "x2" ) );
        retValue.setY2( requireNonNullArgument( y2, "y2" ) );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createLine()

    /**
     *  Creates an SVG {@code <marker>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  id  The id for the new {@code <marker>} element.
     *  @param  parent  The parent {@code <svg>} element.
     *  @return The new {@code <marker>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGMarker createMarker( final String id, final SVG parent )
    {
        final var retValue = new SVGMarkerImpl();
        retValue.setId( id );
        requireNonNullArgument( parent, "parent" ).addDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createMarker()

    /**
     *  Creates an SVG {@code <path>} element instance.
     *
     *  @return The new {@code <path>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPath createPath() { return new SVGPathImpl(); }

    /**
     *  Creates an SVG {@code <Path>} element instance and adds it to the
     *  given parent element.
     *
     *  @param  parent  The parent element.
     *  @return The new {@code <path>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPath createPath( final SVGElementWithChildren parent )
    {
        final var retValue = createPath();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPath()

    /**
     *  Creates an SVG {@code <path>} element instance with the given path
     *  definition.
     *
     *  @param  pathDefinition  The path definition.
     *  @return The new {@code <path>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPath createPath( final SVGPathElement... pathDefinition )
    {
        final var retValue = createPath();
        retValue.setPathDefinition( requireNonNullArgument( pathDefinition, "pathDefinition" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPath()

    /**
     *  Creates an SVG {@code <path>} element instance with the given path
     *  definition and adds it to the given parent element.
     *
     *  @param  parent  The parent element.
     *  @param  pathDefinition  The path definition.
     *  @return The new {@code <path>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPath createPath( final SVGElementWithChildren parent, final SVGPathElement... pathDefinition )
    {
        final var retValue = createPath( parent );
        retValue.setPathDefinition( requireNonNullArgument( pathDefinition, "pathDefinition" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPath()

    /**
     *  Creates an SVG {@code <marker>} element instance that is used as a
     *  <i>positioned</i> marker.
     *
     *  @return The new {@code <marker>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPositionedMarker createPositionedMarker()
    {
        final var retValue = new SVGPositionedMarkerImpl();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPositionedMarker()

    /**
     *  Creates an SVG {@code <marker>} element instance that is used as a
     *  <i>positioned</i> marker and adds it to the given parent.
     *
     *  @param  id  The id for the new {@code <marker>} element.
     *  @param  parent  The parent element.
     *  @return The new {@code <marker>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPositionedMarker createPositionedMarker( final String id, final SVGElementWithChildren parent )
    {
        final var retValue = createPositionedMarker();
        retValue.setId( id );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPositionedMarker()

    /**
     *  Creates the namespace instance that is used for all RDF stuff.
     *
     *  @return The new namespace instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private static final Namespace createRDFNamespace()
    {
        final var identifier = URI.create( "http://www.w3.org/1999/02/22-rdf-syntax-ns#" );
        final var retValue = new Namespace( "rdf", identifier );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createRDFNamespace()

    /**
     *  Creates an SVG {@code <rect>} element instance.
     *
     *  @return The new {@code <rect>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGRectangle createRectangle() { return new SVGRectangleImpl(); }

    /**
     *  Creates an SVG {@code <rect>} element instance and adds it to the
     *  given parent element.
     *
     *  @param  parent  The parent element.
     *  @return The new {@code <rect>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGRectangle createRectangle( final SVGElementWithChildren parent )
    {
        final var retValue = createRectangle();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createRectangle()

    /**
     *  Creates an SVG {@code <rect>} element instance with the given start and
     *  end points.
     *
     *  @param  x   The x coordinate for the upper left corner of the
     *      rectangle.
     *  @param  y   The x coordinate for the upper left corner of the
     *      rectangle.
     *  @param  width   The width of the rectangle.
     *  @param  height  The height of the rectangle.
     *  @return The new {@code <rect>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRectangle createRectangle( final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height )
    {
        final var retValue = createRectangle();
        retValue.defineRectangle( x, y, width, height );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createRectangle()

    /**
     *  Creates an SVG {@code <rect>} element instance with the given start and
     *  end points and adds it to the given parent element.
     *
     *  @param  parent  The parent element.
     *  @param  x   The x coordinate for the upper left corner of the
     *      rectangle.
     *  @param  y   The x coordinate for the upper left corner of the
     *      rectangle.
     *  @param  width   The width of the rectangle.
     *  @param  height  The height of the rectangle.
     *  @return The new {@code <rect>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRectangle createRectangle( final SVGElementWithChildren parent, final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height )
    {
        final var retValue = createRectangle();
        retValue.defineRectangle( x, y, width, height );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createRectangle()

    /**
     *  Creates an SVG {@code <svg>} element instance.
     *
     *  @param  usage  How the new {@code <svg>} element should be used.
     *  @return The new {@code <svg>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVG createSVG( final Usage usage )
    {
        final var retValue = new SVGImpl();

        switch( requireNonNullArgument( usage, "usage" ) )
        {
            case EMBED_HTML ->
            {
                retValue.setNamespace( createSVGNamespace( SVG_PREFIX ) );
                retValue.setNamespace( createXLINKNamespace() );
                retValue.setAttribute( SVGATTRIBUTE_BaseProfile, "full" );
                retValue.setAttribute( SVGATTRIBUTE_Version, "1.1" );
            }

            case EMBED_HTML5 ->
            {
                retValue.setAttribute( SVGATTRIBUTE_BaseProfile, "full" );
                retValue.setAttribute( SVGATTRIBUTE_Version, "1.1" );
            }

            case EMBED_SVG -> { /* Does nothing */ }

            case STANDALONE_DOCUMENT ->
            {
                /*
                 * This configuration was taken more or less (more <i>less</i>)
                 * from an SVG document created with Inkscape.
                 */
                retValue.setNamespace( createSVGNamespace( SVG_PREFIX ) );
                retValue.setNamespace( createSVGNamespace( EMPTY_STRING ) );
                retValue.setNamespace( createXLINKNamespace() );
                retValue.setNamespace( createCCNamespace() );
                retValue.setNamespace( createRDFNamespace() );
                retValue.setAttribute( SVGATTRIBUTE_BaseProfile, "full" );
                retValue.setAttribute( SVGATTRIBUTE_Version, "1.1" );
            }

            default -> throw new UnsupportedEnumError( usage );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSVG()

    /**
     *  Creates an SVG {@code <svg>} element instance and adds it to the given
     *  parent.
     *
     *  @param  parent  The parent for the new {@code <svg>} element.
     *  @return The new {@code <svg>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVG createSVG( final SVGElementWithChildren parent )
    {
        final var retValue = createSVG( EMBED_SVG );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSVG()

    /**
     *  Creates an XML document instance with the given {@code <svg>} element
     *  as the root element.
     *
     *  @param  rootElement The root element.
     *  @return The new SVG document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createSVGDocument( final SVG rootElement )
    {
        final var retValue = createXMLDocument( (XMLElement) rootElement, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSVGDocument()

    /**
     *  Creates the namespace instance that is used for all SVG stuff.
     *
     *  @param  prefix  The prefix; can be empty or {@code null}.
     *  @return The new namespace instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private static final Namespace createSVGNamespace( final String prefix )
    {
        final var identifier = URI.create( "http://www.w3.org/2000/svg" );
        final var retValue = isNotEmptyOrBlank( prefix ) ? new Namespace( prefix, identifier ) : new Namespace( identifier );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSVGNamespace()

    /**
     *  Creates an SVG {@code <style>} element instance.
     *
     *  @return The new {@code <symbol>} element instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGStyle createStyle() { return new SVGStyleImpl(); }

    /**
     *  Creates an SVG {@code <style>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  parent  The parent for the new {@code <style>} element.
     *  @return The new {@code <symbol>} element instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGStyle createStyle( final SVG parent )
    {
        final var retValue = createStyle();
        requireNonNullArgument( parent, "parent" ).addDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createStyle()

    /**
     *  Creates an SVG {@code <style>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  styles  The CSS style definitions.
     *  @return The new {@code <symbol>} element instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGStyle createStyle( final CharSequence... styles )
    {
        final var retValue = new SVGStyleImpl( styles );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createStyle()

    /**
     *  Creates an SVG {@code <style>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  parent  The parent for the new {@code <style>} element.
     *  @param  styles  The CSS style definitions.
     *  @return The new {@code <symbol>} element instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGStyle createStyle( final SVG parent, final CharSequence... styles )
    {
        final var retValue = createStyle( styles );
        requireNonNullArgument( parent, "parent" ).addDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createStyle()

    /**
     *  Creates an SVG {@code <symbol>} element instance.
     *
     *  @param  id  The id for the new {@code <symbol>}.
     *  @return The new {@code <symbol>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGSymbol createSymbol( final String id ) { return new SVGSymbolImpl( id ); }

    /**
     *  Creates an SVG {@code <symbol>} element instance and adds it to the
     *  given {@code <svg>} element.
     *
     *  @param  id  The id for the new {@code <symbol>}.
     *  @param  parent  The parent for the new {@code <symbol>} element.
     *  @return The new {@code <symbol>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGSymbol createSymbol( final String id, final SVG parent )
    {
        final var retValue = new SVGSymbolImpl( id );
        requireNonNullArgument( parent, "parent" ).addDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSymbol()

    /**
     *  Creates an SVG {@code <text>} element instance.
     *
     *  @return The new {@code <text>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGText createText() { return new SVGTextImpl(); }

    /**
     *  Creates an SVG {@code <text>} element instance and adds it to the given
     *  parent.
     *
     *  @param  text    The text for the new element.
     *  @return The new {@code <text>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGText createText( final CharSequence text )
    {
        final var retValue = createText();
        retValue.addText( text );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createText()

    /**
     *  Creates an SVG {@code <text>} element instance and adds it to the given
     *  parent.
     *
     *  @param  parent  The parent for the new {@code <text>} element.
     *  @return The new {@code <text>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGText createText( final SVGElementWithChildren parent )
    {
        final var retValue = createText();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createText()

    /**
     *  Creates an SVG {@code <text>} element instance and adds it to the given
     *  parent.
     *
     *  @param  parent  The parent for the new {@code <text>} element.
     *  @param  text    The text for the new element.
     *  @return The new {@code <text>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGText createText( final SVGElementWithChildren parent, final CharSequence text )
    {
        final var retValue = createText( text );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createText()

    /**
     *  Creates an SVG {@code <tspan>} element instance.
     *
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan() { return new SVGTSpanImpl(); }

    /**
     *  Creates an SVG {@code <tspan>} element as child of the given
     *  {@code <text>}.
     *
     *  @param  text    The text for the new {@code <tspan>} element.
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan( final CharSequence text )
    {
        final var retValue = createTSpan();
        retValue.addText( text );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTSpan()

    /**
     *  Creates an SVG {@code <tspan>} element as child of the given
     *  {@code <text>}.
     *
     *  @param  parent  The parent for the new {@code <tspan>} element.
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan( final SVGText parent )
    {
        final var retValue = createTSpan();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTSpan()

    /**
     *  Creates an SVG {@code <tspan>} element as child of the given
     *  {@code <text>}.
     *
     *  @param  parent  The parent for the new {@code <tspan>} element.
     *  @param  text    The text for the new {@code <tspan>} element.
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan( final SVGText parent, final CharSequence text )
    {
        final var retValue = createTSpan( text );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTSpan()

    /**
     *  Creates an SVG {@code <tspan>} element as child of the given
     *  {@code <tspan>}.
     *
     *  @param  parent  The parent for the new {@code <tspan>} element.
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan( final SVGTSpan parent )
    {
        final var retValue = createTSpan();
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTSpan()

    /**
     *  Creates an SVG {@code <tspan>} element as child of the given
     *  {@code <tspan>}.
     *
     *  @param  parent  The parent for the new {@code <tspan>} element.
     *  @param  text    The text for the new {@code <tspan>} element.
     *  @return The new {@code <tspan>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGTSpan createTSpan( final SVGTSpan parent, final CharSequence text )
    {
        final var retValue = createTSpan( text );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTSpan()

    /**
     *  Creates an SVG {@code <use>} element instance.
     *
     *  @param  reference   The reference to the element to clone.
     *  @return The new {@code <use>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGUse createUse( final URI reference ) { return new SVGUseImpl( reference ); }

    /**
     *  Creates an SVG {@code <use>} element instance and adds it to the given
     *  parent.
     *
     *  @param  parent  The parent for the new {@code <use>} element.
     *  @param  reference   The reference to the element to clone.
     *  @return The new {@code <use>} element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGUse createUse( final SVGElementWithChildren parent, final URI reference )
    {
        final var retValue = createUse( reference );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createUse()

    /**
     *  Creates the namespace instance that is used for all XLINK stuff.
     *
     *  @return The new namespace instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private static final Namespace createXLINKNamespace()
    {
        final var identifier = URI.create( "http://www.w3.org/1999/xlink" );
        final var retValue = new Namespace( "xlink", identifier );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXLINKNamespace()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveTo( final double x2, final double y2, final double x, final double y )
    {
        final var retValue = new SVGCubicCurveTo( false, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x1   The x coordinate for the first control point.
     *  @param  y1   The y coordinate for the first control point.
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveTo( final double x1, final double y1, final double x2, final double y2, final double x, final double y )
    {
        final var retValue = new SVGCubicCurveTo( false, x1, y1, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveTo( final long x2, final long y2, final long x, final long y )
    {
        final var retValue = new SVGCubicCurveTo( false, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x1   The x coordinate for the first control point.
     *  @param  y1   The y coordinate for the first control point.
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveTo( final long x1, final long y1, final long x2, final long y2, final long x, final long y )
    {
        final var retValue = new SVGCubicCurveTo( false, x1, y1, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveToAbs( final double x2, final double y2, final double x, final double y )
    {
        final var retValue = new SVGCubicCurveTo( true, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x1   The x coordinate for the first control point.
     *  @param  y1   The y coordinate for the first control point.
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveToAbs( final double x1, final double y1, final double x2, final double y2, final double x, final double y )
    {
        final var retValue = new SVGCubicCurveTo( true, x1, y1, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveToAbs( final long x2, final long y2, final long x, final long y )
    {
        final var retValue = new SVGCubicCurveTo( true, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGCubicCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x1   The x coordinate for the first control point.
     *  @param  y1   The y coordinate for the first control point.
     *  @param  x2   The x coordinate for the second control point
     *  @param  y2   The y coordinate for the second control point
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement cubicCurveToAbs( final long x1, final long y1, final long x2, final long y2, final long x, final long y )
    {
        final var retValue = new SVGCubicCurveTo( true, x1, y1, x2, y2, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  cubicCurveToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGDegree}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGDegree degree( final double value ) { return new SVGDegree( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGDegree}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGDegree degree( final long value ) { return new SVGDegree( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#EM}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber em( final double value ) { return new SVGNumber( value, EM ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#EM}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber em( final long value ) { return new SVGNumber( value, EM ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#EX}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber ex( final double value ) { return new SVGNumber( value, EX ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#EX}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber ex( final long value ) { return new SVGNumber( value, EX ); }

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement hLineTo( final double x )
    {
        final var retValue = new SVGHLineTo( false, x );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hLineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement hLineTo( final long x )
    {
        final var retValue = new SVGHLineTo( false, x );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hLineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement hLineToAbs( final double x )
    {
        final var retValue = new SVGHLineTo( true, x );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hLineToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement hLineToAbs( final long x )
    {
        final var retValue = new SVGHLineTo( true, x );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hLineToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#INCH}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber inch( final double value ) { return new SVGNumber( value, INCH ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#INCH}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber inch( final long value ) { return new SVGNumber( value, INCH ); }

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGLineTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement lineTo( final double x, final double y )
    {
        final var retValue = new SVGLineTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGLineTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement lineTo( final long x, final long y )
    {
        final var retValue = new SVGLineTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement lineToAbs( final double x, final double y )
    {
        final var retValue = new SVGLineTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lineToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement lineToAbs( final long x, final long y )
    {
        final var retValue = new SVGLineTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lineToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGMatrix}.
     *
     *  @param  a   Parameter {@code a}.
     *  @param  b   Parameter {@code b}.
     *  @param  c   Parameter {@code c}.
     *  @param  d   Parameter {@code d}.
     *  @param  e   Parameter {@code e}.
     *  @param  f   Parameter {@code f}.
     *  @return The {@code matrix} transform.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGMatrix matrix( final long a, final long b, final long c, final long d, final long e, final long f )
    {
        return new SVGMatrix( a, b, c, d, e, f );
    }   //  matrix()

    /**
     *  Creates an instance of
     *  {@link SVGMatrix}.
     *
     *  @param  a   Parameter {@code a}.
     *  @param  b   Parameter {@code b}.
     *  @param  c   Parameter {@code c}.
     *  @param  d   Parameter {@code d}.
     *  @param  e   Parameter {@code e}.
     *  @param  f   Parameter {@code f}.
     *  @return The {@code matrix} transform.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGMatrix matrix( final double a, final double b, final double c, final double d, final double e, final double f )
    {
        return new SVGMatrix( a, b, c, d, e, f );
    }   //  matrix()

    /**
     *  Creates an instance of
     *  {@link SVGMillimeter}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGMillimeter millimeter( final double value ) { return new SVGMillimeter( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGMillimeter}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGMillimeter millimeter( final long value ) { return new SVGMillimeter( value ); }

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGMoveTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement moveTo( final double x, final double y )
    {
        final var retValue = new SVGMoveTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  moveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGMoveTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement moveTo( final long x, final long y )
    {
        final var retValue = new SVGMoveTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  moveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGMoveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement moveToAbs( final double x, final double y )
    {
        final var retValue = new SVGMoveTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  moveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGMoveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate.
     *  @param  y   The y coordinate.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement moveToAbs( final long x, final long y )
    {
        final var retValue = new SVGMoveTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  moveToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGUserUnitValue}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGUserUnitValue number( final double value ) { return new SVGUserUnitValue( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGUserUnitValue}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGUserUnitValue number( final long value ) { return new SVGUserUnitValue( value ); }

    /**
     *  Creates an array of
     *  {@link SVGNumber}
     *  instances.
     *
     *  @param  values  The values.
     *  @return The {@code SVGNumber} array.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber [] numbers( final SVGNumber... values ) { return requireNonNullArgument( values, "values" ); }

    /**
     *  Creates a
     *  {@link List}
     *  of
     *  {@link SVGNumber}
     *  instances.<br>
     *  <br>The list is <i>not</i> immutable!
     *
     *  @param  values  The values.
     *  @return The {@code SVGNumber} list.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final List<SVGNumber> numberList( final SVGNumber... values ) { return new ArrayList<>( asList( requireNonNullArgument( values, "values" ) ) ); }

    /**
     *  Creates an instance of
     *  {@link SVGPercent}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGPercent percent( final double value ) { return new SVGPercent( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGPercent}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGPercent percent( final long value ) { return new SVGPercent( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#PICA}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber pica( final double value ) { return new SVGNumber( value, PICA ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#PICA}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber pica( final long value ) { return new SVGNumber( value, PICA ); }

    /**
     *  Creates an instance of
     *  {@link SVGPixel}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGPixel pixel( final double value ) { return new SVGPixel( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGPixel}
     *  with the given numeric type.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGPixel pixel( final long value ) { return new SVGPixel( value ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#POINT}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber point( final double value ) { return new SVGNumber( value, POINT ); }

    /**
     *  Creates an instance of
     *  {@link SVGNumber}
     *  with the given numeric type and with the unit
     *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGUnit#POINT}.
     *
     *  @param  value   The type.
     *  @return The new {@code SVGNumber} instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGNumber point( final long value ) { return new SVGNumber( value, POINT ); }

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveTo( final double x, final double y )
    {
        final var retValue = new SVGQuadraticCurveTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x1   The x coordinate for the control point.
     *  @param  y1   The y coordinate for the control point.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveTo( final double x1, final double y1, final double x, final double y )
    {
        final var retValue = new SVGQuadraticCurveTo( false, x1, y1, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveTo( final long x, final long y )
    {
        final var retValue = new SVGQuadraticCurveTo( false, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses relative coordinates.
     *
     *  @param  x1   The x coordinate for the control point.
     *  @param  y1   The y coordinate for the control point.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveTo( final long x1, final long y1, final long x, final long y )
    {
        final var retValue = new SVGQuadraticCurveTo( false, x1, y1, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveToAbs( final double x, final double y )
    {
        final var retValue = new SVGQuadraticCurveTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x1   The x coordinate for the control point.
     *  @param  y1   The y coordinate for the control point.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveToAbs( final double x1, final double y1, final double x, final double y )
    {
        final var retValue = new SVGQuadraticCurveTo( true, x1, y1, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveToAbs( final long x, final long y )
    {
        final var retValue = new SVGQuadraticCurveTo( true, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGQuadraticCurveTo}
     *  that uses absolute coordinates.
     *
     *  @param  x1   The x coordinate for the control point.
     *  @param  y1   The y coordinate for the control point.
     *  @param  x   The x coordinate for the end point of the line.
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement quadraticCurveToAbs( final long x1, final long y1, final long x, final long y )
    {
        final var retValue = new SVGQuadraticCurveTo( true, x1, y1, x, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quadraticCurveToAbs()

    /**
     *  Creates an instance of
     *  {@link SVGRotate}.
     *
     *  @param  a   The rotation angle.
     *  @return The {@code rotate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRotate rotate( final long a ) { return new SVGRotate( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGRotate}.
     *
     *  @param  a   The rotation angle.
     *  @return The {@code rotate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRotate rotate( final double a ) { return new SVGRotate( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGRotate}.
     *
     *  @param  a   The rotation angle.
     *  @param  x   The x coordinate of the rotation point.
     *  @param  y   The y coordinate of the rotation point.
     *  @return The {@code rotate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRotate rotate( final long a, final long x, final long y ) { return new SVGRotate( a, x, y ); }

    /**
     *  Creates an instance of
     *  {@link SVGRotate}.
     *
     *  @param  a   The rotation angle.
     *  @param  x   The x coordinate of the rotation point.
     *  @param  y   The y coordinate of the rotation point.
     *  @return The {@code rotate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGRotate rotate( final double a, final double x, final double y ) { return new SVGRotate( a, x, y ); }

    /**
     *  Creates an instance of
     *  {@link SVGScale}.
     *  This is equivalent to
     *  {@link #scale(long, long) scale( x, x )}.
     *
     *  @param  x   Parameter {@code x}.
     *  @return The {@code scale} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGScale scale( final long x ) { return new SVGScale( x ); }

    /**
     *  Creates an instance of
     *  {@link SVGScale}.
     *  This is equivalent to
     *  {@link #scale(double, double) scale( x, x )}.
     *
     *  @param  x   Parameter {@code x}.
     *  @return The {@code scale} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGScale scale( final double x ) { return new SVGScale( x ); }

    /**
     *  Creates an instance of
     *  {@link SVGScale}.
     *
     *  @param  x   Parameter {@code x}.
     *  @param  y   Parameter {@code y}.
     *  @return The {@code scale} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGScale scale( final long x, final long y ) { return new SVGScale( x, y ); }

    /**
     *  Creates an instance of
     *  {@link SVGScale}.
     *
     *  @param  x   Parameter {@code x}.
     *  @param  y   Parameter {@code y}.
     *  @return The {@code scale} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGScale scale( final double x, final double y ) { return new SVGScale( x, y ); }

    /**
     *  Creates an instance of
     *  {@link SVGSkewX}.
     *
     *  @param  a   The angle.
     *  @return The {@code skewX} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGSkewX skewX( final long a ) { return new SVGSkewX( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGSkewX}.
     *
     *  @param  a   The angle.
     *  @return The {@code skewX} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGSkewX skewX( final double a ) { return new SVGSkewX( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGSkewY}.
     *
     *  @param  a   The angle.
     *  @return The {@code skewY} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGSkewY skewY( final long a ) { return new SVGSkewY( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGSkewY}.
     *
     *  @param  a   The angle.
     *  @return The {@code skewY} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGSkewY skewY( final double a ) { return new SVGSkewY( a ); }

    /**
     *  Creates an instance of
     *  {@link SVGTranslate}.
     *
     *  @param  x   Parameter {@code x}.
     *  @return The {@code translate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGTranslate translate( final long x ) { return new SVGTranslate( x ); }

    /**
     *  Creates an instance of
     *  {@link SVGTranslate}.
     *
     *  @param  x   Parameter {@code x}.
     *  @return The {@code translate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGTranslate translate( final double x ) { return new SVGTranslate( x ); }

    /**
     *  Creates an instance of
     *  {@link SVGTranslate}.
     *
     *  @param  x   Parameter {@code x}.
     *  @param  y   Parameter {@code y}.
     *  @return The {@code translate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGTranslate translate( final long x, final long y ) { return new SVGTranslate( x, y ); }

    /**
     *  Creates an instance of
     *  {@link SVGTranslate}.
     *
     *  @param  x   Parameter {@code x}.
     *  @param  y   Parameter {@code y}.
     *  @return The {@code translate} transformation.
     */
    @API( status = STABLE, since = "0.0.5" )
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final SVGTranslate translate( final double x, final double y ) { return new SVGTranslate( x, y ); }

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses relative coordinates.
     *
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement vLineTo( final double y )
    {
        final var retValue = new SVGVLineTo( false, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  vLineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGHLineTo}
     *  that uses relative coordinates.
     *
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement vLineTo( final long y )
    {
        final var retValue = new SVGVLineTo( false, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  vLineTo()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGVLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement vLineToAbs( final double y )
    {
        final var retValue = new SVGVLineTo( true, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  vLineToAbs()

    /**
     *  Creates an instance of the
     *  {@linkplain SVGPathElement path element}
     *  {@link SVGVLineTo}
     *  that uses absolute coordinates.
     *
     *  @param  y   The y coordinate for the end point of the line.
     *  @return The new path element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGPathElement vLineToAbs( final long y )
    {
        final var retValue = new SVGVLineTo( true, y );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  vLineToAbs()
}
//  class SVGUtils

/*
 *  End of File
 */