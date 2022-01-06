/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

import static java.lang.Double.min;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Language;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Whitespace;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_AlignmentBaseline;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_BaselineShift;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Class;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ExternalResourcesRequired;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Height;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ImageRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Kerning;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Lang;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LetterSpacing;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LightingColor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerEnd;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerMid;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_MarkerStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnAbort;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnActivate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCanPlay;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCanPlayThrough;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCancel;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnClick;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnClose;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCopy;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCueChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCut;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDblClick;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDrag;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragEnd;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragEnter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragExit;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragLeave;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragOver;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDrop;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDurationChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnEmptied;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnEnded;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnError;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocus;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusIn;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusOut;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnInput;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnInvalid;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyDown;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyPress;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyUp;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoad;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadedData;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadedMetadata;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseDown;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseEnter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseLeave;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseMove;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseOut;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseOver;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseUp;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseWheel;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPaste;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPause;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPlay;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPlaying;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnProgress;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnRateChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnReset;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnResize;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnScroll;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSeeked;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSeeking;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSelect;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnShow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnStalled;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSubmit;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSuspend;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnTimeUpdate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnToggle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnUnload;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnVolumeChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnWaiting;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Opacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Overflow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PathLength;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PointerEvents;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PreserveAspectRatio;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Reference;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_RequiredExtensions;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_RequiredFeatures;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_SystemLanguage;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TabIndex;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextAnchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextDecoration;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextRendering;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Transform;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_UnicodeBidi;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_VectorEffect;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ViewBox;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Visibility;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Width;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WordSpacing;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WritingMode;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Actuate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_ArcRole;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Reference;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Role;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Show;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Title;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_XLink_Type;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_rx;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ry;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Description;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Title;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.XMLATTRIBUTE_Base;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLBuilderUtils.getNMTokenValidator;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.ALLOWS_TEXT;

import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGElement;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGAlignmentBaseLine;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGColor;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGUserUnitValue;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPaint;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPreserveAspectRatio;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGTransform;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.XMLElementAdapter;

/**
 *  <p>{@summary The base class for all SVG elements.}</p>
 *  <p>Some internal elements are even implemented by this class only (that's
 *  why it is not abstract).</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGElementImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: SVGElementImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class SVGElementImpl extends XMLElementAdapter implements SVGElement
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  Flag that indicates whether a description was already provided for this
     *  element.
     */
    private boolean m_HasDescription;

    /**
     *  Flag that indicates whether a title was already provided for this
     *  element.
     */
    private boolean m_HasTitle;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGElementImpl} instance.
     *
     *  @param  elementName The name of the element.
     *  @param  flags   The configuration flags for the new element.
     */
    protected SVGElementImpl( final String elementName, final Flags... flags )
    {
        super( elementName, Set.of( requireNonNullArgument( flags, "flags" ) ) );

        m_HasDescription = false;
        m_HasTitle = false;
    }   //  SVGElementImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a child to this element.
     *
     *  @param  <E> The implementation type for the {@code children}.
     *  @param  child   The child to add.
     *  @throws IllegalArgumentException    The given child is not valid for
     *      this element or no children are allowed at all.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not this element.
     */
    public <E extends SVGElement> void addChild( final E child ) throws IllegalArgumentException, IllegalStateException
    {
        addChild( (XMLElement) child );
    }   //  addChild()

    /**
     *  Sets the abort handler for this SVG element.
     *
     *  @param  value   The abort handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnAbort
     */
    public void setAbortHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnAbort, value, NO_APPEND ); }

    /**
     *  Sets the activation handler for this SVG element.
     *
     *  @param  value   The activation handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnActivate
     */
    public void setActivationHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnActivate, value, NO_APPEND ); }

    /**
     *  Sets the presentation attribute {@code alignment-baseline} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_AlignmentBaseline
     */
    public void setAlignmentBaseline( final SVGAlignmentBaseLine value )
    {
        setAttribute( SVGATTRIBUTE_AlignmentBaseline, nonNull( value ) ? value.toString() : null, NO_APPEND );
    }   //  setAlignmentBaseline()

    /**
     *  Sets the attribute with the given name.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's type; if {@code null} the
     *      attribute will be removed.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    @SuppressWarnings( {"PublicMethodNotExposedInInterface", "UseOfConcreteClass"} )
    public void setAttribute( final String name, final SVGNumber value ) throws IllegalArgumentException
    {
        setAttribute( name, nonNull( value ) ? value.value() : null, NO_APPEND );
    }   //  setAttribute()

    /**
     *  Sets the presentation attribute {@code baseline-shift} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_BaselineShift
     */
    public void setBaselineShift( final String value ) { setAttribute( SVGATTRIBUTE_BaselineShift, value, NO_APPEND ); }

    /**
     *  Sets the cancel handler for this SVG element.
     *
     *  @param  value   The cancel handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCancel
     */
    public void setCancelHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCancel, value, NO_APPEND ); }

    /**
     *  Sets the can-play handler for this SVG element.
     *
     *  @param  value   The can-play handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCanPlay
     */
    public void setCanPlayHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCanPlay, value, NO_APPEND ); }

    /**
     *  Sets the can-play-through handler for this SVG element.
     *
     *  @param  value   The can-play-through handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCanPlayThrough
     */
    public void setCanPlayThroughHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCanPlayThrough, value, NO_APPEND ); }

    /**
     *  Sets the change handler for this SVG element.
     *
     *  @param  value   The change handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnChange
     */
    public void setChangeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnChange, value, NO_APPEND ); }

    /**
     *  Sets the CSS class for the SVG element.
     *
     *  @param  value   The name of a CSS class for this SVG element.
     */
    public void setClass( final CharSequence value ) { setAttribute( SVGATTRIBUTE_Class, value, Optional.of( " " ) ); }

    /**
     *  Sets the click handler for this SVG element.
     *
     *  @param  value   The click handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnClick
     */
    public void setClickHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnClick, value, NO_APPEND ); }

    /**
     *  Sets the presentation attribute {@code clip} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Clip
     */
    public void setClip( final String value ) { setAttribute( SVGATTRIBUTE_Clip, value, NO_APPEND ); }

    /**
     *  Sets the {@code clip-path} attribute for this SVG element.
     *
     *  @param  value   The URI for the clip path.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ClipPath
     */
    public void setClipPath( final URI value )
    {
        setAttribute( SVGATTRIBUTE_ClipPath, nonNull( value ) ? format( "url(%s)", value ) : null );
    }   //  setClipPath()

    /**
     *  Sets the presentation attribute {@code clip-rule} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ClipRule
     */
    public void setClipRule( final String value ) { setAttribute( SVGATTRIBUTE_ClipRule, value, NO_APPEND ); }

    /**
     *  Sets the global event attribute {@code onclose} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnClose
     */
    public void setCloseHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnClose, value ); }

    /**
     *  Sets the colour for this SVG element.
     *
     *  @param  value   The colour.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Color
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setColor( final SVGColor value )
    {
        setAttribute( SVGATTRIBUTE_Color, nonNull( value ) ? value.value() : null );
    }   //  setColor()

    /**
     *  Sets the presentation attribute {@code color-interpolation} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ColorInterpolation
     */
    public void setColorInterpolation( final String value ) { setAttribute( SVGATTRIBUTE_ColorInterpolation, value, NO_APPEND ); }

    /**
     *  Sets the presentation attribute {@code color-interpolation-filters} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ColorInterpolationFilters
     */
    public void setColorInterpolationFilters( final String value ) { setAttribute( SVGATTRIBUTE_ColorInterpolationFilters, value ); }

    /**
     *  Sets the presentation attribute {@code color-profile} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ColorProfile
     */
    public void setColorProfile( final String value ) { setAttribute( SVGATTRIBUTE_ColorProfile, value ); }

    /**
     *  Sets the presentation attribute {@code color-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ColorRendering
     */
    public void setColorRendering( final String value ) { setAttribute( SVGATTRIBUTE_ColorRendering, value ); }

    /**
     *  Sets the copy handler for this SVG element.
     *
     *  @param  value   The copy handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCopy
     */
    public void setCopyHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCopy, value ); }

    /**
     *  Sets the global event attribute {@code oncuechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCueChange
     */
    public void setCueChangeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCueChange, value ); }

    /**
     *  Sets the presentation attribute {@code cursor} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Cursor
     */
    public void setCursor( final String value ) { setAttribute( SVGATTRIBUTE_Cursor, value ); }

    /**
     *  Sets the cut handler for this SVG element.
     *
     *  @param  value   The cut handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnCut
     */
    public void setCutHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnCut, value ); }

    /**
     *  Sets the global event attribute {@code ondblclick} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDblClick
     */
    public void setDblClickHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDblClick, value ); }

    /**
     *  Sets the description for the SVG element.<br>
     *  <br>This is not an attribute, instead a
     *  <code>&lt;{@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGELEMENT_Description}&gt;</code>
     *  element will be added as a child.
     *
     *  @param  description The description; nothing happens if {@code null},
     *      empty, or blank.
     *  @throws IllegalStateException   The given description is not
     *      {@code null}, empty, or blank, and a title was applied already
     *      earlier.
     */
    public void setDescription( final CharSequence description )
    {
        if( isNotEmptyOrBlank( description ) )
        {
            if( m_HasDescription ) throw new IllegalStateException( "Description was already set" );

            final var element = new SVGElementImpl( SVGELEMENT_Description, ALLOWS_TEXT );
            element.updateRegistries( emptyList(), emptyList() );
            element.addText( description );
            addChild( (SVGElement) element );
            m_HasDescription = true;
        }
    }   //  setDescription()

    /**
     *  Sets the presentation attribute {@code direction} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Direction
     */
    public void setDirection( final String value ) { setAttribute( SVGATTRIBUTE_Direction, value ); }

    /**
     *  Sets the presentation attribute {@code display} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Display
     */
    public void setDisplay( final String value ) { setAttribute( SVGATTRIBUTE_Display, value ); }

    /**
     *  Sets the presentation attribute {@code dominant-baseline} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_DominantBaseline
     */
    public void setDominantBaseline( final String value ) { setAttribute( SVGATTRIBUTE_DominantBaseline, value ); }

    /**
     *  Sets the global event attribute {@code ondragend} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragEnd
     */
    public void setDragEndHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragEnd, value ); }

    /**
     *  Sets the global event attribute {@code ondragenter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragEnter
     */
    public void setDragEnterHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragEnter, value ); }

    /**
     *  Sets the global event attribute {@code ondragexit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragExit
     */
    public void setDragExitHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragExit, value ); }

    /**
     *  Sets the global event attribute {@code ondrag} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDrag
     */
    public void setDragHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDrag, value ); }

    /**
     *  Sets the global event attribute {@code ondragleave} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragLeave
     */
    public void setDragLeaveHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragLeave, value ); }

    /**
     *  Sets the global event attribute {@code ondragover} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragOver
     */
    public void setDragOverHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragOver, value ); }

    /**
     *  Sets the global event attribute {@code ondragstart} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDragStart
     */
    public void setDragStartHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDragStart, value ); }

    /**
     *  Sets the global event attribute {@code ondrop} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDrop
     */
    public void setDropHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDrop, value ); }

    /**
     *  Sets the global event attribute {@code ondurationchange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnDurationChange
     */
    public void setDurationChangeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnDurationChange, value ); }

    /**
     *  Sets the global event attribute {@code onemptied} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnEmptied
     */
    public void setEmptiedHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnEmptied, value ); }

    /**
     *  Sets the presentation attribute {@code enable-background} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_EnableBackground
     */
    public void setEnableBackground( final String value ) { setAttribute( SVGATTRIBUTE_EnableBackground, value ); }

    /**
     *  Sets the global event attribute {@code onended} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnEnded
     */
    public void setEndedHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnEnded, value ); }

    /**
     *  Sets the global event attribute {@code onerror} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnError
     */
    public void setErrorHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnError, value ); }

    /**
     *  Sets the attribute that indicates whether external resources are
     *  required to render this SVG element.
     *
     *  @param  flag    {@code true} if external resources are needed,
     *      {@code false} if all required resources are local to the current
     *      context.
     */
    public void setExternalResourcesRequired( final boolean flag )
    {
        setAttribute( SVGATTRIBUTE_ExternalResourcesRequired, flag );
    }   //  setExternalResourcesRequired()

    /**
     *  Sets the presentation attribute {@code fill} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Fill
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setFill( final SVGPaint value )
    {
        setAttribute( SVGATTRIBUTE_Fill, nonNull( value ) ? value.value() : null );
    }   //  setFill()

    /**
     *  Sets the presentation attribute {@code fill-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FillOpacity
     */
    public void setFillOpacity( final String value ) { setAttribute( SVGATTRIBUTE_FillOpacity, value ); }

    /**
     *  Sets the presentation attribute {@code fill-rule} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FillRule
     */
    public void setFillRule( final String value ) { setAttribute( SVGATTRIBUTE_FillRule, value ); }

    /**
     *  Sets the presentation attribute {@code filter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Filter
     */
    public void setFilter( final String value ) { setAttribute( SVGATTRIBUTE_Filter, value ); }

    /**
     *  Sets the presentation attribute {@code flood-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FloodColor
     */
    public void setFloodColor( final String value ) { setAttribute( SVGATTRIBUTE_FloodColor, value ); }

    /**
     *  Sets the presentation attribute {@code flood-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FloodOpacity
     */
    public void setFloodOpacity( final String value ) { setAttribute( SVGATTRIBUTE_FloodOpacity, value ); }

    /**
     *  Sets the global event attribute {@code onfocus} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnFocus
     */
    public void setFocusHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnFocus, value ); }

    /**
     *  Sets the focus-in handler for this SVG element.
     *
     *  @param  value   The focus-in handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnFocusIn
     */
    public void setFocusInHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnFocusIn, value ); }

    /**
     *  Sets the focus-out handler for this SVG element.
     *
     *  @param  value   The focus-out handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnFocusOut
     */
    public void setFocusOutHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnFocusOut, value ); }

    /**
     *  Sets the presentation attribute {@code font-family} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontFamily
     */
    public void setFontFamily( final String value ) { setAttribute( SVGATTRIBUTE_FontFamily, value ); }

    /**
     *  Sets the presentation attribute {@code font-size} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontSize
     */
    public void setFontSize( final String value ) { setAttribute( SVGATTRIBUTE_FontSize, value ); }

    /**
     *  Sets the presentation attribute {@code font-size-adjust} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontSizeAdjust
     */
    public void setFontSizeAdjust( final String value ) { setAttribute( SVGATTRIBUTE_FontSizeAdjust, value ); }

    /**
     *  Sets the presentation attribute {@code font-stretch} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontStretch
     */
    public void setFontStretch( final String value ) { setAttribute( SVGATTRIBUTE_FontStretch, value ); }

    /**
     *  Sets the presentation attribute {@code font-style} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontStyle
     */
    public void setFontStyle( final String value ) { setAttribute( SVGATTRIBUTE_FontStyle, value ); }

    /**
     *  Sets the presentation attribute {@code font-variant} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontVariant
     */
    public void setFontVariant( final String value ) { setAttribute( SVGATTRIBUTE_FontVariant, value ); }

    /**
     *  Sets the presentation attribute {@code font-weight} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_FontWeight
     */
    public void setFontWeight( final String value ) { setAttribute( SVGATTRIBUTE_FontWeight, value ); }

    /**
     *  Sets the presentation attribute {@code glyph-orientation-horizontal} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_GlyphOrientationHorizontal
     */
    public void setGlyphOrientationHorizontal( final String value ) { setAttribute( SVGATTRIBUTE_GlyphOrientationHorizontal, value ); }

    /**
     *  Sets the presentation attribute {@code glyph-orientation-vertical} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_GlyphOrientationVertical
     */
    public void setGlyphOrientationVertical( final String value ) { setAttribute( SVGATTRIBUTE_GlyphOrientationVertical, value ); }

    /**
     *  Sets the height of the element.
     *
     *  @param  value   The height.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setHeight( final SVGNumber value )
    {
        if( nonNull( value ) && value.isNegative() ) throw new IllegalArgumentException( "height is negative" );
        setAttribute( SVGATTRIBUTE_Height, value );
    }   //  setHeight()

    /**
     *  {@inheritDoc}
     */
    @Override
    public XMLElement setId( final String id ) throws IllegalArgumentException
    {
        if( !getNMTokenValidator().test( requireNotEmptyArgument( id, "id" ) ) ) throw new IllegalArgumentException( format( "Invalid id: %s", id ) );
        final var retValue = setAttribute( SVGATTRIBUTE_Id, id );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setId()

    /**
     *  Sets the presentation attribute {@code image-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ImageRendering
     */
    public void setImageRendering( final String value ) { setAttribute( SVGATTRIBUTE_ImageRendering, value ); }

    /**
     *  Sets the global event attribute {@code oninput} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnInput
     */
    public void setInputHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnInput, value ); }

    /**
     *  Sets the global event attribute {@code oninvalid} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnInvalid
     */
    public void setInvalidHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnInvalid, value ); }

    /**
     *  Sets the presentation attribute {@code kerning} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Kerning
     */
    public void setKerning( final String value ) { setAttribute( SVGATTRIBUTE_Kerning, value ); }

    /**
     *  Sets the global event attribute {@code onkeydown} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnKeyDown
     */
    public void setKeyDownHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnKeyDown, value ); }

    /**
     *  Sets the global event attribute {@code onkeypress} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnKeyPress
     */
    public void setKeyPressHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnKeyPress, value ); }

    /**
     *  Sets the global event attribute {@code onkeyup} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnKeyUp
     */
    public void setKeyUpHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnKeyUp, value ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setLang( final Locale value )
    {
        setAttribute( SVGATTRIBUTE_Lang, nonNull( value ) ? value.getLanguage() : null );
    }   //  setLang()

    /**
     *  Sets the presentation attribute {@code letter-spacing} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_LetterSpacing
     */
    public void setLetterSpacing( final String value ) { setAttribute( SVGATTRIBUTE_LetterSpacing, value ); }

    /**
     *  Sets the presentation attribute {@code lighting-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_LightingColor
     */
    public void setLightingColor( final String value ) { setAttribute( SVGATTRIBUTE_LightingColor, value ); }

    /**
     *  Sets the global event attribute {@code onloadeddata} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnLoadedData
     */
    public void setLoadedDataHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnLoadedData, value ); }

    /**
     *  Sets the global event attribute {@code onloadedmetadata} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnLoadedMetadata
     */
    public void setLoadedMetadataHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnLoadedMetadata, value ); }

    /**
     *  Sets the global event attribute {@code onload} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnLoad
     */
    public void setLoadHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnLoad, value ); }

    /**
     *  Sets the global event attribute {@code onloadstart} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnLoadStart
     */
    public void setLoadStartHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnLoadStart, value ); }

    /**
     *  Sets the {@code marker-end} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerEnd
     */
    public void setMarkerEnd( final URI value )
    {
        setAttribute( SVGATTRIBUTE_MarkerEnd, nonNull( value ) ? format( "url(%s)", value ) : null );
    }   //  setMarkerStart()

    /**
     *  Sets the {@code marker-mid} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerMid
     */
    public void setMarkerMid( final URI value )
    {
        setAttribute( SVGATTRIBUTE_MarkerMid, nonNull( value ) ? format( "url(%s)", value ) : null );
    }   //  setMarkerStart()

    /**
     *  Sets the {@code marker-start} attribute for this SVG element.
     *
     *  @param  value   The URI for the marker.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_MarkerStart
     */
    public void setMarkerStart( final URI value )
    {
        setAttribute( SVGATTRIBUTE_MarkerStart, nonNull( value ) ? format( "url(%s)", value ) : null );
    }   //  setMarkerStart()

    /**
     *  Sets the presentation attribute {@code mask} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Mask
     */
    public void setMask( final String value ) { setAttribute( SVGATTRIBUTE_Mask, value ); }

    /**
     *  Sets the global event attribute {@code onmousedown} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseDown
     */
    public void setMouseDownHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseDown, value ); }

    /**
     *  Sets the global event attribute {@code onmouseenter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseEnter
     */
    public void setMouseEnterHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseEnter, value ); }

    /**
     *  Sets the global event attribute {@code onmouseleave} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseLeave
     */
    public void setMouseLeaveHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseLeave, value ); }

    /**
     *  Sets the global event attribute {@code onmousemove} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseMove
     */
    public void setMouseMoveHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseMove, value ); }

    /**
     *  Sets the global event attribute {@code onmouseout} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseOut
     */
    public void setMouseOutHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseOut, value ); }

    /**
     *  Sets the global event attribute {@code onmouseover} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseOver
     */
    public void setMouseOverHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseOver, value ); }

    /**
     *  Sets the global event attribute {@code onmouseup} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseUp
     */
    public void setMouseUpHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseUp, value ); }

    /**
     *  Sets the global event attribute {@code onmousewheel} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnMouseWheel
     */
    public void setMouseWheelHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnMouseWheel, value ); }

    /**
     *  Sets the presentation attribute {@code opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Opacity
     */
    public void setOpacity( final String value ) { setAttribute( SVGATTRIBUTE_Opacity, value ); }

    /**
     *  Sets the presentation attribute {@code overflow} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Overflow
     */
    public void setOverflow( final String value ) { setAttribute( SVGATTRIBUTE_Overflow, value ); }

    /**
     *  Sets the paste handler for this SVG element.
     *
     *  @param  value   The paste handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnPaste
     */
    public void setPasteHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnPaste, value ); }

    /**
     *  Sets the length of the path represented by this SVG element.<br>
     *  <br>Various operations, including text on a path and motion animation
     *  and various stroke operations, require that the user agent compute the
     *  distance along the geometry of a graphics element, such as a
     *  ‘path’.<br>
     *  <br>Exact mathematics exist for computing distance along a path, but
     *  the formulas are highly complex and require substantial computation.
     *  It is recommended that authoring products and user agents employ
     *  algorithms that produce as precise results as possible; however, to
     *  accommodate implementation differences and to help distance
     *  calculations produce results that approximate author intent, the
     *  {@code pathLength} attribute can be used to provide the author's
     *  computation of the total length of the path so that the user agent can
     *  scale distance-along-a-path computations by the ratio of
     *  {@code pathLength} to the user agent's own computed type for total
     *  path length.
     *
     *  @param  length  The author's computation of the total length of the
     *      path, in user units. This type is used to calibrate the user
     *      agent's own distance-along-a-path calculations with that of the
     *      author. The user agent will scale all distance-along-a-path
     *      computations by the ratio of this type to the user agent's own
     *      computed type for total path length.<br>
     *      <br>A type of zero is valid, but a negative type is an error.
     *
     *  @throws IllegalArgumentException    The type is less than 0.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setPathLength( final SVGUserUnitValue length )
    {
        if( nonNull( length ) && length.isNegative() ) throw new IllegalArgumentException( format( "length is negative: %s", length.value() ) );

        setAttribute( SVGATTRIBUTE_PathLength, nonNull( length ) ? length.toString() : null );
    }   //  setPathLength()

    /**
     *  Sets the global event attribute {@code onpause} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnPause
     */
    public void setPauseHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnPause, value ); }

    /**
     *  Sets the global event attribute {@code onplay} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnPlay
     */
    public void setPlayHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnPlay, value ); }

    /**
     *  Sets the global event attribute {@code onplaying} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnPlaying
     */
    public void setPlayingHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnPlaying, value ); }

    /**
     *  Sets the presentation attribute {@code pointer-events} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_PointerEvents
     */
    public void setPointerEvents( final String value ) { setAttribute( SVGATTRIBUTE_PointerEvents, value ); }

    /**
     *  Sets the mode for the aspect ratio preservation for this element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    public void setPreserveAspectRatio( final SVGPreserveAspectRatio value )
    {
        setAttribute( SVGATTRIBUTE_PreserveAspectRatio, nonNull( value ) ? value.toString() : null );
    }   //  setPreserveAspectRatio()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setPreserveSpace( final boolean flag )
    {
        setAttribute( XMLATTRIBUTE_Whitespace, flag ? "preserve" : "default" );
    }   //  setPreserveSpace()

    /**
     *  Sets the global event attribute {@code onprogress} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnProgress
     */
    public void setProgressHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnProgress, value ); }

    /**
     *  Sets the global event attribute {@code onratechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnRateChange
     */
    public void setRateChangeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnRateChange, value ); }

    /**
     *  Sets the
     *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Reference}
     *  attribute that refers to another SVG element.
     *
     *  @param  reference   The reference.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public void setReference( final URI reference )
    {
        setAttribute( SVGATTRIBUTE_Reference, nonNull( reference ) ? requireNotEmptyArgument( reference, "reference" ).toString() : null );
    }   //  setReference()

    /**
     *  Sets a list of extensions that are required to render this SVG element.
     *
     *  @param  values  The URIs that identify the required extensions.
     */
    public void setRequiredExtensions( final URI... values )
    {
        setAttribute( SVGATTRIBUTE_RequiredExtensions, nonNull( values )
            ? stream( values )
                  .map( URI::toString )
                  .collect( joining( " " ) )
            : null );
    }   //  setRequiredExtensions

    /**
     *  Sets a list of features that are required to render this SVG element.
     *
     *  @param  values  The URIs that identify the required features.
     */
    public void setRequiredFeatures( final URI... values )
    {
        setAttribute( SVGATTRIBUTE_RequiredFeatures, nonNull( values )
            ? stream( values )
                  .map( URI::toString )
                  .collect( joining( " " ) )
            : null );
    }   //  setRequiredFeatures()

    /**
     *  Sets the global event attribute {@code onreset} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnReset
     */
    public void setResetHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnReset, value ); }

    /**
     *  Sets the resize handler for this SVG element.
     *
     *  @param  value   The resize handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnResize
     */
    public void setResizeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnResize, value ); }

    /**
     *  Sets the {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_rx} attribute for this SVG
     *  element.<br>
     *  <br>The attribute is a radius, but what kind of radius and where it is
     *  used depends on the type of the SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_rx
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setRx( final SVGNumber value )
    {
        if( nonNull( value ) && value.isNegative() ) throw new IllegalArgumentException( "rx is negative" );
        setAttribute( SVGATTRIBUTE_rx, value );
    }   //  setRx()

    /**
     *  Sets the {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ry} attribute for this SVG
     *  element.<br>
     *  <br>The attribute is a radius, but what kind of radius and where it is
     *  used depends on the type of the SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ry
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setRy( final SVGNumber value )
    {
        if( nonNull( value ) && value.isNegative() ) throw new IllegalArgumentException( "ry is negative" );
        setAttribute( SVGATTRIBUTE_ry, value );
    }   //  setRy()

    /**
     *  Sets the scroll handler for this SVG element.
     *
     *  @param  value   The scroll handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnScroll
     */
    public void setScrollHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnScroll, value ); }

    /**
     *  Sets the global event attribute {@code onseeked} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnSeeked
     */
    public void setSeekedHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnSeeked, value ); }

    /**
     *  Sets the global event attribute {@code onseeking} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnSeeking
     */
    public void setSeekingHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnSeeking, value ); }

    /**
     *  Sets the global event attribute {@code onselect} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnSelect
     */
    public void setSelectHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnSelect, value ); }

    /**
     *  Sets the presentation attribute {@code shape-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_ShapeRendering
     */
    public void setShapeRendering( final String value ) { setAttribute( SVGATTRIBUTE_ShapeRendering, value ); }

    /**
     *  Sets the global event attribute {@code onshow} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnShow
     */
    public void setShowHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnShow, value ); }

    /**
     *  Sets the global event attribute {@code onstalled} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnStalled
     */
    public void setStalledHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnStalled, value ); }

    /**
     *  Sets the presentation attribute {@code stop-color} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StopColor
     */
    public void setStopColor( final String value ) { setAttribute( SVGATTRIBUTE_StopColor, value ); }

    /**
     *  Sets the presentation attribute {@code stop-opacity} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StopOpacity
     */
    public void setStopOpacity( final String value ) { setAttribute( SVGATTRIBUTE_StopOpacity, value ); }

    /**
     *  Sets the presentation attribute {@code stroke} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Stroke
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setStroke( final SVGPaint value )
    {
        setAttribute( SVGATTRIBUTE_Stroke, nonNull( value ) ? value.value() : null );
    }   //  setStroke()

    /**
     *  Sets the presentation attribute {@code stroke-dasharray} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeDashArray
     */
    public void setStrokeDashArray( final String value ) { setAttribute( SVGATTRIBUTE_StrokeDashArray, value ); }

    /**
     *  Sets the presentation attribute {@code stroke-dashoffset} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeDashOffset
     */
    public void setStrokeDashOffset( final String value ) { setAttribute( SVGATTRIBUTE_StrokeDashOffset, value ); }

    /**
     *  Sets the presentation attribute {@code stroke-linecap} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeLineCap
     */
    public void setStrokeLineCap( final String value ) { setAttribute( SVGATTRIBUTE_StrokeLineCap, value ); }

    /**
     *  Sets the presentation attribute {@code stroke-linejoin} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeLineJoin
     */
    public void setStrokeLineJoin( final String value ) { setAttribute( SVGATTRIBUTE_StrokeLineJoin, value ); }

    /**
     *  Sets the presentation attribute {@code stroke-miterlimit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeMiterLimit
     */
    public void setStrokeMiterLimit( final String value ) { setAttribute( SVGATTRIBUTE_StrokeMiterLimit, value ); }

    /**
     *  Sets the presentation attribute {@code stroke-opacity} for this SVG
     *  element.<br>
     *  <br>Any type outside the range 0.0 (fully transparent) to 1.0 (fully
     *  opaque) will be clamped to this range.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeOpacity
     */
    public void setStrokeOpacity( final double value )
    {
        if( Double.isNaN( value ) ) throw new IllegalArgumentException( "type is NaN" );
        setAttribute( SVGATTRIBUTE_StrokeOpacity, value < 0.0 ? 0.0 : min( value, 1.0 ) );
    }   //  setStrokeOpacity()

    /**
     *  Sets the presentation attribute {@code stroke-width} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_StrokeWidth
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setStrokeWidth( final SVGNumber value )
    {
        setAttribute( SVGATTRIBUTE_StrokeWidth, nonNull( value ) ? value.toString() : null );
    }   //  setStrokeWidth()

    /**
     *  Sets the CSS style for the SVG element.
     *
     *  @param  value   A CSS style definition.
     */
    public void setStyle( final CharSequence value )
    {
        setAttribute( SVGATTRIBUTE_Style, value, Optional.of( "; " ) );
    }   //  setStyle()

    /**
     *  Sets the global event attribute {@code onsubmit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnSubmit
     */
    public void setSubmitHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnSubmit, value ); }

    /**
     *  Sets the global event attribute {@code onsuspend} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnSuspend
     */
    public void setSuspendHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnSuspend, value ); }

    /**
     *  Sets a list of languages; the current SVG element will be rendered
     *  only if the current system language matches one entry of this list.
     *
     *  @param  values  The allowed languages.
     */
    public void setSystemLanguage( final Locale... values )
    {
        setAttribute( SVGATTRIBUTE_SystemLanguage, nonNull( values )
            ? stream( values )
                  .map( Locale::getLanguage )
                  .collect( joining( "," ) )
            : null );
    }   //  setSystemLanguage()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setTabIndex( final int value ) { setAttribute( SVGATTRIBUTE_TabIndex, value ); }

    /**
     *  Sets the presentation attribute {@code text-anchor} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_TextAnchor
     */
    public void setTextAnchor( final String value ) { setAttribute( SVGATTRIBUTE_TextAnchor, value ); }

    /**
     *  Sets the presentation attribute {@code text-decoration} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_TextDecoration
     */
    public void setTextDecoration( final String value ) { setAttribute( SVGATTRIBUTE_TextDecoration, value ); }

    /**
     *  Sets the presentation attribute {@code text-rendering} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_TextRendering
     */
    public void setTextRendering( final String value ) { setAttribute( SVGATTRIBUTE_TextRendering, value ); }

    /**
     *  Sets the global event attribute {@code ontimeupdate} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnTimeUpdate
     */
    public void setTimeUpdateHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnTimeUpdate, value ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setTitle( final CharSequence title )
    {
        if( isNotEmptyOrBlank( title ) )
        {
            if( m_HasTitle ) throw new IllegalStateException( "Title was already set" );

            final var element = new SVGElementImpl( SVGELEMENT_Title, ALLOWS_TEXT );
            element.updateRegistries( emptyList(), emptyList() );
            element.addText( title );
            addChild( (SVGElement) element );
            m_HasTitle = true;
        }
    }   //  setTitle()

    /**
     *  Sets the global event attribute {@code ontoggle} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnToggle
     */
    public void setToggleHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnToggle, value ); }

    /**
     *  Sets the transformations for this SVG element.
     *
     *  @param  values   The transformations.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Transform
     */
    public void setTransform( final SVGTransform... values )
    {
        final var value = nonNull( values ) && (values.length > 0)
            ? stream( values )
                  .map( SVGTransform::toString )
                  .collect( joining( " " ) )
            : null;
        setAttribute( SVGATTRIBUTE_Transform, value, Optional.of( " " ) );
    }   //  setTransform()

    /**
     *  Sets the presentation attribute {@code unicode-bidi} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_UnicodeBidi
     */
    public void setUnicodeBidi( final String value ) { setAttribute( SVGATTRIBUTE_UnicodeBidi, value ); }

    /**
     *  Sets the &quot;unload&quot; handler for this SVG element.
     *
     *  @param  value   The unload handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnUnload
     */
    public void setUnloadHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnUnload, value ); }

    /**
     *  Sets the presentation attribute {@code vector-effect} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_VectorEffect
     */
    public void setVectorEffect( final String value ) { setAttribute( SVGATTRIBUTE_VectorEffect, value ); }

    /**
     *  Defines the visible area for this element.
     *
     *  @param  x   The x coordinate of top left corner of the area.
     *  @param  y   The y coordinate of top left corner of the area.
     *  @param  width   The width of the area.
     *  @param  height  The height of the area.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setViewBox( final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height )
    {
        final var value = format( "%s,%s,%s,%s", requireNonNullArgument( x, "x" ), requireNonNullArgument( y, "y" ), requireNonNullArgument( width, "width" ), requireNonNullArgument( height, "height" ) );
        setAttribute( SVGATTRIBUTE_ViewBox, value );
    }   //  setViewBox()

    /**
     *  Sets the presentation attribute {@code visibility} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Visibility
     */
    public void setVisibility( final String value ) { setAttribute( SVGATTRIBUTE_Visibility, value ); }

    /**
     *  Sets the global event attribute {@code onvolumechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnVolumeChange
     */
    public void setVolumeChangeHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnVolumeChange, value ); }

    /**
     *  Sets the wait handler for this SVG element.
     *
     *  @param  value   The wait handler.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_OnWaiting
     */
    public void setWaitHandler( final String value ) { setAttribute( SVGATTRIBUTE_OnWaiting, value ); }

    /**
     *  Sets the width of the element.
     *
     *  @param  value   The width.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setWidth( final SVGNumber value )
    {
        if( nonNull( value ) && value.isNegative() ) throw new IllegalArgumentException( "width is negative" );
        setAttribute( SVGATTRIBUTE_Width, value );
    }   //  setWidth()

    /**
     *  Sets the presentation attribute {@code word-spacing} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_WordSpacing
     */
    public void setWordSpacing( final String value ) { setAttribute( SVGATTRIBUTE_WordSpacing, value ); }

    /**
     *  Sets the presentation attribute {@code writing-mode} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_WritingMode
     */
    public void setWritingMode( final String value ) { setAttribute( SVGATTRIBUTE_WritingMode, value ); }

    /**
     *  Sets the x coordinate for the upper left corner of the element.
     *
     *  @param  value   The x coordinate.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setX( final SVGNumber value ) { setAttribute( SVGATTRIBUTE_x, value ); }

    /**
     *  Sets the XLink {@code actuate} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXlinkActuate( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Actuate, value ); }

    /**
     *  Sets the XLink {@code arcrole} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkArcRole( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_ArcRole, value ); }

    /**
     *  Sets the XLink {@code reference} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkReference( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Reference, value ); }

    /**
     *  Sets the XLink {@code role} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkRole( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Role, value ); }

    /**
     *  Sets the XLink {@code show} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkShow( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Show, value ); }

    /**
     *  Sets the XLink {@code title} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkTitle( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Title, value ); }

    /**
     *  Sets the XLink {@code type} attribute.
     *
     *  @param  value   The attribute type.
     */
    public void setXLinkType( final CharSequence value ) { setAttribute( SVGATTRIBUTE_XLink_Type, value ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setXMLBase( final URI value )
    {
        setAttribute( XMLATTRIBUTE_Base, nonNull( value ) ? value.toString() : null );
    }   //  setXMLBase()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setXMLId( final String id ) throws IllegalArgumentException { super.setId( id ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void setXMLLang( final Locale value )
    {
        setAttribute( XMLATTRIBUTE_Language, nonNull( value ) ? value.getLanguage() : null );
    }   //  setXMLLang()

    /**
     *  Sets the y coordinate for the upper left corner of the element.
     *
     *  @param  value   The y coordinate.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setY( final SVGNumber value ) { setAttribute( SVGATTRIBUTE_y, value ); }

    /**
     *  Updates the registries with the valid children and attributes for this
     *  element.
     *
     *  @param  childElements   The names of valid child elements.
     *  @param  attributes  The names of valid attributes <i>in the sequence
     *      they should have when the element written to output</i>.
     */
    protected final void updateRegistries( final Collection<String> childElements, final Collection<String> attributes )
    {
        registerValidChildren( requireNonNullArgument( childElements, "childElements" ).toArray( EMPTY_String_ARRAY ) );

        registerValidAttributes( requireNonNullArgument( attributes, "attributes" ).toArray( EMPTY_String_ARRAY ) );

        if( !attributes.isEmpty() )
        {
            final var attributeSequence = attributes.stream().distinct().toArray( String []::new );
            registerAttributeSequence( attributeSequence );
        }
    }   //  updateRegistries()
}
//  class SVGElementImpl

/*
 *  End of File
 */