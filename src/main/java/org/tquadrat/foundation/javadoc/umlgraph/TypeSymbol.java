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

package org.tquadrat.foundation.javadoc.umlgraph;

import static java.lang.Math.max;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_Class;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_ExternalResourcesRequired;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_Id;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_PreserveAspectRatio;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_Style;
import static org.tquadrat.foundation.svg.SVGUtils.SVGATTRIBUTE_ViewBox;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_AltGlyphDef;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Anchor;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_ClipPath;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_ColorProfile;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Cursor;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Filter;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Font;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_FontFace;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_ForeignObject;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Image;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Marker;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Mask;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Pattern;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Script;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Switch;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Symbol;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_Text;
import static org.tquadrat.foundation.svg.SVGUtils.SVGELEMENT_View;
import static org.tquadrat.foundation.svg.SVGUtils.createGroup;
import static org.tquadrat.foundation.svg.SVGUtils.createLine;
import static org.tquadrat.foundation.svg.SVGUtils.createRectangle;
import static org.tquadrat.foundation.svg.SVGUtils.createText;
import static org.tquadrat.foundation.svg.SVGUtils.number;
import static org.tquadrat.foundation.svg.SVGUtils.translate;
import static org.tquadrat.foundation.svg.type.SVGElementCategory.ANIMATION;
import static org.tquadrat.foundation.svg.type.SVGElementCategory.DESCRIPTIVE;
import static org.tquadrat.foundation.svg.type.SVGElementCategory.GRADIENT;
import static org.tquadrat.foundation.svg.type.SVGElementCategory.SHAPE;
import static org.tquadrat.foundation.svg.type.SVGElementCategory.STRUCTURAL;
import static org.tquadrat.foundation.svg.type.SVGPaint.PAINT_NONE;
import static org.tquadrat.foundation.util.StringUtils.maxContentLength;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.ALLOWS_CHILDREN;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.VALIDATES_ATTRIBUTES;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.VALIDATES_CHILDREN;

import javax.lang.model.element.Element;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.svg.AllowsGraphicalEventAttributes;
import org.tquadrat.foundation.svg.AllowsPresentationAttributes;
import org.tquadrat.foundation.svg.SVGElementAdapter;
import org.tquadrat.foundation.svg.SVGGroup;
import org.tquadrat.foundation.svg.SVGSymbol;
import org.tquadrat.foundation.svg.SVGText;
import org.tquadrat.foundation.svg.SVGUse;
import org.tquadrat.foundation.svg.SVGUtils;

/**
 *  A specialisation of
 *  {@link SVGSymbol}
 *  for the visualisation of
 *  {@link UMLTypeElement}
 *  instances.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TypeSymbol.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: TypeSymbol.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class TypeSymbol extends SVGElementAdapter
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name for the CSS class that defines the look for external classes:
     *  {@value}.
     */
    public static final String CSSClass_ExternalClass = "externalClass";

    /**
     *  The name for the CSS class that defines the look for the focus class:
     *  {@value}.
     */
    public static final String CSSClass_FocusClass = "focusClass";

    /**
     *  The name for the CSS class that defines the look for other classes:
     *  {@value}.
     */
    public static final String CSSClass_OtherClass = "otherClass";

    /**
     *  The width of a character for a SVG graph: {@value} px.
     */
    public static final double SVG_CHAR_WIDTH = 6.0;

    /**
     *  The height of a SVG text line: {@value} px.
     */
    public static final double SVG_LINE_HEIGHT = 11.0;

    /**
     *  The vertical border inside a rectangle: {@value} px.
     */
    public static final double SVG_LINE_SPACING = 2.0;

    /**
     *  The spacing before and after a SVG text: {@value} px.
     */
    public static final double SVG_TEXT_SPACING = 5.0;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The dimension of the base rectangle for UML graph representing this
     *  type element.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private Dimension m_BaseDimension;

    /**
     *  The dimension for the SVG element representing this type element in the
     *  UML diagram.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final Dimension m_Dimension;

    /**
     *  The local offset for the base rectangle.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final Point m_Offset;

    /**
     *  The instance of
     *  {@link UMLTypeElement}
     *  that is represented by this {@code TypeSymbol}.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final UMLTypeElement m_TypeElement;

    /**
     *  The X coordinate of the upper left corner of the symbol in the UML
     *  diagram.
     */
    private double m_X = 0.0;

    /**
     *  The Y coordinate of the upper left corner of the symbol in the UML
     *  diagram.
     */
    private double m_Y = 0.0;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code TypeSymbol} instance.
     *
     *  @param  parent  The UML document that contains the type element.
     *  @param  typeElement The type element that is represented by this
     *      instance of {@code TypeSymbol}.
     *  @param  isFocusClass    {@code true} if the given type element is the
     *      focus class of the UML graph.
     *  @param  apiOnly {@code true} to show only the API parts of the type,
     *      {@code false} if also the implementation internals are shown.
     *  @param  isIncludedInOutput  {@code true} if the type element is shown
     *      in the current output, {@code false} otherwise.
     */
    @SuppressWarnings( {"BooleanParameter", "MagicNumber"} )
    public TypeSymbol( final UMLDocument parent, final UMLTypeElement typeElement, final boolean isFocusClass, final boolean apiOnly, final boolean isIncludedInOutput )
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

        //---* Set the mandatory id *------------------------------------------
        setId( requireNonNullArgument( typeElement, "typeElement" ).getQualifiedName().toString() );
        m_TypeElement = typeElement;

        //---* Create the symbol *---------------------------------------------
        final var symbolGroup = createGroup( this );

        final var typeGroup = createGroup( symbolGroup );
        if( isFocusClass ) typeGroup.setClass( CSSClass_FocusClass );

        //---* Determine the stereotype *--------------------------------------
        final var stereotypes = typeElement.determineStereotypes();
        final Collection<CharSequence> texts = new ArrayList<>( List.of( stereotypes ) );

        //---* The simple name of the class *----------------------------------
        texts.add( typeElement.getSimpleName() );

        //---* The attributes *------------------------------------------------
        final var fields = typeElement.getAttributes( apiOnly );
        for( final var field : fields )
        {
            texts.add( field.toString() );
        }

        //---* The constructors *----------------------------------------------
        final var constructors = typeElement.getConstructors( apiOnly );
        for( final var constructor : constructors )
        {
            texts.add( constructor.toString() );
        }

        //---* The methods *---------------------------------------------------
        final var methods = typeElement.getMethods( apiOnly );
        for( final var method : methods )
        {
            texts.add( method.toString() );
        }

        //---* Get the maximum text length *-----------------------------------
        final var maxTextLen = maxContentLength( texts );

        //---* Determine the number of lines *---------------------------------
        final var lines = 1 + stereotypes.length + max( 1, fields.size() ) + max( 1, constructors.size() + methods.size() );

        //---* Is this a template class (with generics)? *---------------------
        final var typeParameters = typeElement.getTypeParameters();
        var yOffset = 0.0;
        var xAddon = 0.0;
        SVGGroup templateGroup = null;
        if( !typeParameters.isEmpty() )
        {
            templateGroup = createGroup();
            yOffset = 8.0;
            final var buffer = typeParameters.stream()
                .map( Element::getSimpleName )
                .collect( joining( "," ) );
            final var width = max( 50.0, 2.0 * SVG_TEXT_SPACING + buffer.length() * SVG_CHAR_WIDTH );
            xAddon = width / 2.0;
            final var height = 3.0 * SVG_LINE_SPACING + SVG_LINE_HEIGHT;

            var rect = createRectangle( number( 0.0 ), number( 1.0 ), number( width ), number( height ) );
            rect.setClass( "template" );
            templateGroup.addChild( rect );
            rect = createRectangle( number( 0.0 ), number( 1.0 ), number( width ), number( height ) );
            if( isFocusClass )
            {
                rect.setClass( CSSClass_FocusClass );
            }
            else if( isIncludedInOutput )
            {
                rect.setClass( CSSClass_OtherClass );
            }
            else
            {
                rect.setClass( CSSClass_ExternalClass );
            }
            rect.setStroke( PAINT_NONE );
            rect.setStrokeOpacity( 0.0 );
            templateGroup.addChild( rect );

            final var text = createText( buffer );
            text.setX( number( SVG_TEXT_SPACING ) );
            text.setY( number( SVG_LINE_SPACING + SVG_LINE_HEIGHT + 1.0 ) );
            templateGroup.addChild( text );
        }
        var y = yOffset;
        final var x = 0.0;
        m_Offset = new Point( x, y );

        //---* Calculate the dimensions *--------------------------------------
        final var width = 2.0 * SVG_TEXT_SPACING + maxTextLen * SVG_CHAR_WIDTH;
        var height = 9.0 * SVG_LINE_SPACING + lines * SVG_LINE_HEIGHT;
        m_Dimension = new Dimension( width + xAddon, height + yOffset );
        setBaseDimension( width, height );

        //---* The base rectangle *--------------------------------------------
        final var rect = createRectangle( number( x ), number( y ), number( width ), number( height ) );
        if( isFocusClass )
        {
            rect.setClass( "focusClass" );
        }
        else if( isIncludedInOutput )
        {
            rect.setClass( "otherClass" );
        }
        else
        {
            rect.setClass( "externalClass" );
        }
        typeGroup.addChild( rect );
        typeGroup.setClipPath( URI.create( "#" + parent.addClippath( rect ) ) );

        //---* The rectangle for the name and stereotype *---------------------
        height = 3.0 * SVG_LINE_SPACING + (1 + stereotypes.length) * SVG_LINE_HEIGHT;
        var line = createLine( number( x ), number( y + height ), number( x + width ), number( y + height ) );
        line.setClass( "separator" );
        typeGroup.addChild( line );
        SVGText text;
        var lineCounter = 0;
        for( final var stereotype : stereotypes )
        {
            text = createText( stereotype );
            text.setX( number( width / 2.0 - stereotype.length() * SVG_CHAR_WIDTH * 0.8 / 2.0 ) );
            text.setY( number( y + SVG_LINE_SPACING + ++lineCounter * SVG_LINE_HEIGHT ) );
            text.setClass( "stereotype" );
            typeGroup.addChild( text );
        }
        text = createText( typeElement.getSimpleName() );
        text.setX( number( width / 2.0 - typeElement.getSimpleName().length() * SVG_CHAR_WIDTH / 2.0 ) );
        text.setY( number( y + SVG_LINE_SPACING + ++lineCounter * SVG_LINE_HEIGHT ) );
        if( typeElement.isAbstract() ) text.setClass( "abstract" );
        typeGroup.addChild( text );
        y += height;

        //---* The rectangle for the attributes *------------------------------
        height = 3.0 * SVG_LINE_SPACING + max( 1, fields.size() ) * SVG_LINE_HEIGHT;
        line = createLine( number( x ), number( y + height ), number( x + width ), number( y + height ) );
        line.setClass( "separator" );
        typeGroup.addChild( line );
        if( !fields.isEmpty() )
        {
            lineCounter = 0;
            for( final var field : fields )
            {
                text = field.toText();
                text.setY( number( x + SVG_TEXT_SPACING ) );
                text.setY( number( y + SVG_LINE_SPACING + ++lineCounter * SVG_LINE_HEIGHT ) );
                typeGroup.addChild( text );
            }
        }
        else
        {
            final var t = typeElement.getAttributes( false ).isEmpty() ? EMPTY_STRING : "\u2026";
            text = createText( t );
            text.setX( number( x + SVG_TEXT_SPACING ) );
            text.setY( number(  y + SVG_LINE_SPACING + SVG_LINE_HEIGHT ) );
            typeGroup.addChild( text );
        }
        y += height;

        //---* The rectangle for the constructors and methods *----------------
        if( !constructors.isEmpty() || !methods.isEmpty() )
        {
            lineCounter = 0;
            for( final var constructor : constructors )
            {
                text = constructor.toText();
                text.setX( number( x + SVG_TEXT_SPACING ) );
                text.setY( number( y + SVG_LINE_SPACING + ++lineCounter * SVG_LINE_HEIGHT ) );
                typeGroup.addChild( text );
            }
            for( final var method : methods )
            {
                text = method.toText();
                text.setX( number( x + SVG_TEXT_SPACING ) );
                text.setY( number( y + SVG_LINE_SPACING + ++lineCounter * SVG_LINE_HEIGHT ) );
                typeGroup.addChild( text );
            }
        }
        else
        {
            final var t = typeElement.getConstructors( false ).isEmpty() && typeElement.getMethods( false ).isEmpty() ? EMPTY_STRING : "\u2026";
            text = createText( t );
            text.setX( number( x + SVG_TEXT_SPACING ) );
            text.setY( number( y + SVG_LINE_SPACING + SVG_LINE_HEIGHT ) );
            typeGroup.addChild( text );
        }

        //---* Add the template *----------------------------------------------
        if( nonNull( templateGroup ) )
        {
            templateGroup.setTransform( translate( width - xAddon, 0 ) );
            symbolGroup.addChild( templateGroup );
        }
    }   //  TypeSymbol()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns this {@code TypeSymbol} instance as an instance of
     *  {@link org.tquadrat.foundation.svg.SVGSymbol SVGPath}.
     *
     *  @return This instance.
     */
    public final SVGSymbol asSVGSymbol() { return this; }

    /**
     *  Returns a
     *  {@link SVGUse}
     *  element for this symbol and calculates the connector end points.
     *
     *  @param  startingConnectors  The connectors that will originate from
     *      this type element.
     *  @param  endingConnectors    The connectors that will end at this type
     *      element.
     *  @return The new use element.
     */
    public final SVGUse createUse( final Collection<UMLConnector> startingConnectors, final Collection<UMLConnector> endingConnectors )
    {
        final var retValue = SVGUtils.createUse( URI.create( "#" + getQualifiedName() ) );
        var y = m_Y;
        retValue.setX( number( m_X ) );
        retValue.setY( number( m_Y ) );

        //---* Calculate the connector endings *-------------------------------
        var count = 0;
        y += m_Offset.y();
        final var height = m_BaseDimension.height();
        if( nonNull( startingConnectors ) && !startingConnectors.isEmpty() )
        {
            final var distance = 50.0;
            final var startPos = m_X + (m_BaseDimension.width() - (startingConnectors.size() - 1) * distance) / 2.0;
            for( final var connector : startingConnectors )
            {
                connector.setStartPoint( startPos + count++ * distance, y );
            }
        }
        if( nonNull( endingConnectors ) && !endingConnectors.isEmpty() )
        {
            final var distance = 30.0;
            final var startPos = m_X + (m_BaseDimension.width() - (endingConnectors.size() - 1) * distance) / 2.0;
            count = 0;
            for( final var line : endingConnectors )
            {
                line.setEndPoint( startPos + count++ * distance, y + height );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createUse()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "AccessingNonPublicFieldOfAnotherObject" )
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = obj == this;
        if( !retValue && (obj instanceof TypeSymbol other ) )
        {
            retValue = m_TypeElement.equals( other.m_TypeElement );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  Returns the dimension for the SVG element representing the type element
     *  in the UML diagram.
     *
     *  @return The dimension.
     */
    public final Dimension getDimension() { return m_Dimension; }

    /**
     *  Returns the position of the SVG element representing this UML class.
     *
     *  @return The coordinates of the upper left corner of the class diagram.
     */
    public final Point getPosition() { return new Point( m_X, m_Y ); }

    /**
     *  Returns the qualified name of the type element that is represented by
     *  this symbol.
     *
     *  @return The qualified name.
     */
    public final String getQualifiedName() { return m_TypeElement.getQualifiedName().toString(); }

    /**
     *  Returns the type element that is represented by this symbol.
     *
     *  @return The type element.
     */
    public final UMLTypeElement getTypeElement() { return m_TypeElement; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_TypeElement.hashCode(); }

    /**
     *  Returns {@code true} if the type element represented by this type
     *  symbol is an interface. Different from
     *  {@link javax.lang.model.element.ElementKind#isInterface()},
     *  this method will return {@code false} if the type element is an
     *  {@code annotation}.
     *
     *  @return {@code true} if the type element represented by this type
     *      symbol is an interface, {@code false} otherwise.
     */
    public final boolean isInterface() { return m_TypeElement.isInterface(); }

    /**
     *  Sets the base dimension for the SVG element representing the type
     *  element in the UML diagram.
     *
     *  @param  width   The width for the SVG element.
     *  @param  height  The height for the SVG element.
     */
    public final void setBaseDimension( final double width, final double height ) { m_BaseDimension = new Dimension( width, height ); }

    /**
     *  Sets the X coordinate for the upper left corner of the symbol for the
     *  class diagram.
     *
     *  @param x    The X coordinate.
     */
    public final void setX( final double x) { m_X = x; }

    /**
     *  Sets the Y coordinate for the upper left corner of the symbol for the
     *  class diagram.
     *
     *  @param y    The Y coordinate.
     */
    public final void setY( final double y ) { m_Y = y; }
}
//  class TypeSymbol

/*
 *  End of File
 */