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

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Language;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.XMLATTRIBUTE_Whitespace;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_AlignmentBaseline;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_BaseProfile;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ContentScriptType;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnError;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusIn;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocusOut;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnResize;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnScroll;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnUnload;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Opacity;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Overflow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PointerEvents;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_PreserveAspectRatio;
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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Version;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ViewBox;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Visibility;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Width;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WordSpacing;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_WritingMode;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ZoomAndPan;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphDef;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Anchor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ColorProfile;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Cursor;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Defs;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Filter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Font;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_FontFace;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_ForeignObject;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Image;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Marker;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Mask;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Pattern;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Root;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Script;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Switch;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Text;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_View;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.XMLATTRIBUTE_Base;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createStyle;
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
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsDocumentElementEventAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.AllowsDocumentEventAttributes;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVG;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGElement;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGStyle;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.Element;
import jakarta.activation.MimeType;

/**
 *  The implementation for the interface
 *  {@link SVG}
 *  for the {@code <svg>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGImpl extends SVGElementImpl implements SVG
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The optional {@code <defs>} element for this {@code <svg>} element.
     */
    private SVGElementImpl m_Definitions = null;

    /**
     *  The style sheet for this {@code <svg>} element.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Optional<SVGStyle> m_StyleSheet = Optional.empty();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGImpl} instance.
     */
    public SVGImpl()
    {
        super( SVGELEMENT_Root, ALLOWS_CHILDREN, VALIDATES_CHILDREN, VALIDATES_ATTRIBUTES );

        //---* The children and attributes for the <svg> element *-------------
        final var childElements = validChildElements();

        final Collection<String> attributes = new ArrayList<>();
        attributes.addAll( List.of( SVGATTRIBUTE_Id, SVGATTRIBUTE_Version,
            SVGATTRIBUTE_BaseProfile, SVGATTRIBUTE_ContentScriptType,
            SVGATTRIBUTE_x, SVGATTRIBUTE_y, SVGATTRIBUTE_Width,
            SVGATTRIBUTE_Height, SVGATTRIBUTE_ViewBox,
            SVGATTRIBUTE_PreserveAspectRatio, SVGATTRIBUTE_ZoomAndPan,
            SVGATTRIBUTE_Class, SVGATTRIBUTE_Style, SVGATTRIBUTE_Lang,
            SVGATTRIBUTE_TabIndex, SVGATTRIBUTE_ExternalResourcesRequired,
            SVGATTRIBUTE_RequiredExtensions, SVGATTRIBUTE_RequiredFeatures,
            SVGATTRIBUTE_SystemLanguage, XMLATTRIBUTE_Id, XMLATTRIBUTE_Base,
            XMLATTRIBUTE_Language, XMLATTRIBUTE_Whitespace, SVGATTRIBUTE_OnAbort,
            SVGATTRIBUTE_OnActivate, SVGATTRIBUTE_OnError,
            SVGATTRIBUTE_OnFocusIn, SVGATTRIBUTE_OnFocusOut,
            SVGATTRIBUTE_OnResize, SVGATTRIBUTE_OnScroll,
            SVGATTRIBUTE_OnUnload, SVGATTRIBUTE_AlignmentBaseline,
            SVGATTRIBUTE_BaselineShift, SVGATTRIBUTE_Clip,
            SVGATTRIBUTE_ClipPath, SVGATTRIBUTE_ClipRule, SVGATTRIBUTE_Color,
            SVGATTRIBUTE_ColorInterpolation,
            SVGATTRIBUTE_ColorInterpolationFilters, SVGATTRIBUTE_ColorProfile,
            SVGATTRIBUTE_ColorRendering, SVGATTRIBUTE_Cursor,
            SVGATTRIBUTE_Direction, SVGATTRIBUTE_Display,
            SVGATTRIBUTE_DominantBaseline, SVGATTRIBUTE_EnableBackground,
            SVGATTRIBUTE_Fill, SVGATTRIBUTE_FillOpacity, SVGATTRIBUTE_FillRule,
            SVGATTRIBUTE_Filter, SVGATTRIBUTE_FloodColor,
            SVGATTRIBUTE_FloodOpacity, SVGATTRIBUTE_FontFamily,
            SVGATTRIBUTE_FontSize, SVGATTRIBUTE_FontSizeAdjust,
            SVGATTRIBUTE_FontStretch, SVGATTRIBUTE_FontStyle,
            SVGATTRIBUTE_FontVariant, SVGATTRIBUTE_FontWeight,
            SVGATTRIBUTE_GlyphOrientationHorizontal,
            SVGATTRIBUTE_GlyphOrientationVertical, SVGATTRIBUTE_ImageRendering,
            SVGATTRIBUTE_Kerning, SVGATTRIBUTE_LetterSpacing,
            SVGATTRIBUTE_LightingColor, SVGATTRIBUTE_MarkerEnd,
            SVGATTRIBUTE_MarkerMid, SVGATTRIBUTE_MarkerStart,
            SVGATTRIBUTE_Mask, SVGATTRIBUTE_Opacity, SVGATTRIBUTE_Overflow,
            SVGATTRIBUTE_PointerEvents, SVGATTRIBUTE_ShapeRendering,
            SVGATTRIBUTE_StopColor, SVGATTRIBUTE_StopOpacity,
            SVGATTRIBUTE_Stroke, SVGATTRIBUTE_StrokeDashArray,
            SVGATTRIBUTE_StrokeDashOffset, SVGATTRIBUTE_StrokeLineCap,
            SVGATTRIBUTE_StrokeLineJoin, SVGATTRIBUTE_StrokeMiterLimit,
            SVGATTRIBUTE_StrokeOpacity, SVGATTRIBUTE_StrokeWidth,
            SVGATTRIBUTE_TextAnchor, SVGATTRIBUTE_TextDecoration,
            SVGATTRIBUTE_TextRendering, SVGATTRIBUTE_Transform,
            SVGATTRIBUTE_UnicodeBidi, SVGATTRIBUTE_VectorEffect,
            SVGATTRIBUTE_Visibility, SVGATTRIBUTE_WordSpacing,
            SVGATTRIBUTE_WritingMode ) );
        attributes.addAll( AllowsDocumentEventAttributes.DOCUMENTEVENT_ATTRIBUTES );
        attributes.addAll( AllowsDocumentElementEventAttributes.DOCUMENTELEMENTEVENT_ATTRIBUTES );

        updateRegistries( childElements, attributes );
    }   //  SVGImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final <E extends SVGElement> void addDefinition( final E child ) throws IllegalArgumentException, IllegalStateException
    {
        /*
         * The class of the instance is only sufficient when it is a native
         * SVG object. But everything that is derived from SVGElementAdapter is
         * everything (including an SVGStyle), therefore we need to check the
         * element name, too.
         */
        if( requireNonNullArgument( child, "child" ) instanceof SVGStyle style && child.getElementName().equals( SVGELEMENT_Style ) )
        {
            addStyle( style );
        }
        else
        {
            child.getAttribute( SVGATTRIBUTE_Id ).orElseThrow( () -> new IllegalArgumentException( "Id is missing" ) );
            if( isNull( m_Definitions ) ) m_Definitions = createDefinitionsElement();
            m_Definitions.addChild( child );
        }
    }   //  addDefinition()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void addStyle( final SVGStyle style ) throws IllegalArgumentException, IllegalStateException
    {
        requireNonNullArgument( style, "style" );

        /*
         * <style> elements will be merged if possible, and they do not need an
         * id.
         */
        if( m_StyleSheet.isPresent() )
        {
            m_StyleSheet.get().merge( style );
        }
        else
        {
            m_StyleSheet = Optional.of( style );
            if( isNull( m_Definitions ) ) m_Definitions = createDefinitionsElement();
            m_Definitions.addChild( style );
        }
    }   //  addDefinition()

    /**
     *  Creates the element for the definitions.
     *
     *  @return The {@code <defs>} element.
     */
    private final  SVGElementImpl createDefinitionsElement()
    {
        final var retValue = new SVGElementImpl( SVGELEMENT_Defs, ALLOWS_CHILDREN );

        //---* The children and attributes for the <defs> element *-------------
        /*
         * The valid children for the <defs> element are the same as for the
         * <svg> element - excluding the <defs> element itself.
         */
        final var childElements = validChildElements();
        childElements.remove( SVGELEMENT_Defs );

        /*
         * The specification allows attributes for the <defs> element, but as
         * we don't provide an API to set them, we do not configure them ...
         */
        retValue.updateRegistries( childElements, emptyList() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createDefinitionsElement()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<? extends Element> getChildren()
    {
        final Collection<? extends Element> retValue;
        if( nonNull( m_Definitions ) )
        {
            final Collection<Element> children = new ArrayList<>();
            children.add( m_Definitions );
            children.addAll( super.getChildren() );
            retValue = List.copyOf( children );
        }
        else
        {
            retValue = super.getChildren();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getChildren()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean hasChildren()
    {
        final var retValue = nonNull( m_Definitions ) || super.hasChildren();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasChildren()
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void setContentScriptType( final MimeType value )
    {
        setAttribute( SVGATTRIBUTE_ContentScriptType, nonNull( value ) ? value.toString() : null );
    }   //  setContentScriptType()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "OptionalGetWithoutIsPresent" )
    @Override
    public final SVGStyle setStyleSheet( final CharSequence... styles )
    {
        /*
         * After this call, m_StyleSheet is definitely no longer empty; it
         * will be set as a side effect of SVG.addStyle(). Not nice, but
         * efficient.
         */
        m_StyleSheet.ifPresentOrElse( s -> s.addStyle( styles ), () -> addStyle( createStyle( styles ) ) );

        //---* Done *----------------------------------------------------------
        return m_StyleSheet.get();
    }   //  setStyleSheet()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void setZoomAndPan( final boolean flag )
    {
        setAttribute( SVGATTRIBUTE_ZoomAndPan, flag ? "magnify" : "disable" );
    }   //  setZoomAndPan()

    /**
     *  Composes the list of the valid child elements for the {@code <svg>}
     *  element.
     *
     *  @return The valid child elements.
     */
    private final Collection<String> validChildElements()
    {
        //---* The valid child elements the <svg> element *--------------------
        final Collection<String> retValue = new HashSet<>();
        retValue.addAll( ANIMATION.getElements() );
        retValue.addAll( DESCRIPTIVE.getElements() );
        retValue.addAll( SHAPE.getElements() );
        retValue.addAll( STRUCTURAL.getElements() );
        retValue.addAll( GRADIENT.getElements() );
        retValue.addAll( List.of( SVGELEMENT_AltGlyphDef,
            SVGELEMENT_Anchor, SVGELEMENT_ClipPath, SVGELEMENT_ColorProfile,
            SVGELEMENT_Cursor, SVGELEMENT_Filter, SVGELEMENT_Font,
            SVGELEMENT_FontFace, SVGELEMENT_ForeignObject, SVGELEMENT_Image,
            SVGELEMENT_Marker, SVGELEMENT_Mask, SVGELEMENT_Pattern,
            SVGELEMENT_Script, SVGELEMENT_Style, SVGELEMENT_Switch,
            SVGELEMENT_Text, SVGELEMENT_View ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  validChildElements()
}
//  class SVGImpl

/*
 *  End of File
 */