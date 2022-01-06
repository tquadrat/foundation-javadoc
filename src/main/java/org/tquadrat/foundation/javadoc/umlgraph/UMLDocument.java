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

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVG.Usage.STANDALONE_DOCUMENT;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.cloneElement;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.closePath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createClipPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createMarker;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createPath;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createSVG;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.lineTo;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.moveToAbs;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.number;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.vLineTo;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGMarkerOrientation.AUTO;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLConnectorType.IMPLEMENTATION;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLConnectorType.INHERITANCE;

import javax.lang.model.element.Name;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVG;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGPath;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGRectangle;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUse;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGColor;

/**
 *  The container for a UML diagram.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLDocument.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLDocument.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public class UMLDocument
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of the CSS resource file: {@value}.
     */
    public static final String UML_CSS = "resources/uml.css";

    /**
     *  The colour 'black'.
     */
    public static final SVGColor SVG_COLOR_BLACK = new SVGColor( 0x00, 0x00, 0x00 );

    /**
     *  The colour 'white'.
     */
    public static final SVGColor SVG_COLOR_WHITE = new SVGColor( 0xFF, 0xFF, 0xFF );

    /**
     *  The spacing between UML graph elements: {@value} px.
     */
    public static final double SVG_ELEMENT_SPACING = 50.0;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  Width and height for this UML diagram.
     */
    @SuppressWarnings( {"unused", "InstanceVariableOfConcreteClass", "FieldCanBeLocal"} )
    private Dimension m_Dimension;

    /**
     *  The id counter.
     *
     *  @see #createId()
     */
    private int m_IdCounter = 0;

    /**
     *  The SVG that represents the diagram.
     */
    private final SVG m_Root;

    /**
     *  The SVG symbols representing the types.
     */
    private final Collection<TypeSymbol> m_TypeSymbols = new ArrayList<>();

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The CSS styles.
     */
    private static final String [] m_CSSStyles;

    static
    {
        try
        {
            m_CSSStyles = loadCSS();
        }
        catch( final IOException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLDocument} instance.
     */
    public UMLDocument()
    {
        m_Root = createSVG( STANDALONE_DOCUMENT );
        m_Root.setStyleSheet( m_CSSStyles );

        //---* Create the markers *--------------------------------------------
        final var height = 40;
        final var width = 40;
        SVGPath path;

        path = createPath( moveToAbs( width, height / 2 ), lineTo( -width, -height / 2 ), vLineTo( height ), closePath() );
        path.setFill( SVG_COLOR_WHITE );
        path.setStroke( SVG_COLOR_BLACK );
        path.setStrokeWidth( number( 3 ) );
        var marker = createMarker( INHERITANCE.getId(), m_Root );
        marker.addChild( path );
        marker.setViewBox( number( 0 ), number( 0 ), number( width ), number( height ) );
        marker.setReferencePoint( number( width ), number( height / 2 ) );
        marker.setMarkerUnits( false );
        marker.setMarkerDimensions( number( 6 ), number( 6 ) );
        marker.setOrientation( AUTO );

        path = createPath( moveToAbs( width, height / 2 ), lineTo( -width, -height / 2 ), vLineTo( height ), closePath() );
        path.setFill( SVG_COLOR_WHITE );
        path.setStroke( SVG_COLOR_BLACK );
        path.setStrokeWidth( number( 3 ) );
        marker = createMarker( IMPLEMENTATION.getId(), m_Root );
        marker.addChild( path );
        marker.setViewBox( number( 0 ), number( 0 ), number( width ), number( height ) );
        marker.setReferencePoint( number( width ), number( height / 2 ) );
        marker.setMarkerUnits( false );
        marker.setMarkerDimensions( number( 6 ), number( 6 ) );
        marker.setOrientation( AUTO );
    }   //  UMLDocument()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates an SVG {@code <clipPath>} element from the given rectangle and
     *  adds it to the SVG root element for this UML document.
     *
     *  @param  clipRectangle   The rectangle that defines the clip path.
     *  @return The id of the new SVG {@code <clipPath>} element.
     */
    public final String addClippath( final SVGRectangle clipRectangle )
    {
        requireNonNullArgument( clipRectangle, "clipRectangle" );

        final var retValue = createId();
        final var clippath = createClipPath( retValue, m_Root );
        clippath.addChild( cloneElement( clipRectangle ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  addClippath()

    /**
     *  Adds a UML connector for two type elements to this UML document.
     *
     *  @param  connector   The connector.
     */
    public final void addConnector( final UMLConnector connector )
    {
        m_Root.addChild( requireNonNullArgument( connector, "connector" ) );
    }   //  addConnector()

    /**
     *  Adds the symbol for a type element to the UML document
     *
     *  @param  symbol  The symbol to add.
     *  @return {@code true} if the symbol was added, {@code false} if not.
     */
    public final boolean addSymbol( final TypeSymbol symbol )
    {
        requireNonNullArgument( symbol, "symbol" );

        final var retValue = m_TypeSymbols.stream()
            .noneMatch( s -> s.equals( symbol ) );
        if( retValue )
        {
            m_Root.addDefinition( symbol );
            m_TypeSymbols.add( symbol );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  addSymbol()

    /**
     *  Places a symbol in the output diagram.
     *
     *  @param  use The reference to the {@code <symbol>} element represent a
     *      type.
     */
    public final void addUse( final SVGUse use )
    {
        m_Root.addChild( requireNonNullArgument( use, "use" ) );
    }   //  addUse()

    /**
     *  Creates a new id that is unique inside the current UML document
     *  instance.
     *
     *  @return The new id.
     */
    public final String createId()
    {
        final var retValue = format( "ID%06d", ++m_IdCounter );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createId()

    /**
     *  Returns the SVG root element for this UML document.
     *
     *  @return The SVG root element.
     */
    public final SVG getSVGRoot() { return m_Root; }

    /**
     *  Returns the symbol with for the type element with the given name from
     *  this UML document.
     *
     *  @param  name    The name of the type element.
     *  @return An instance of
     *      {@link Optional}
     *      that contains the symbol.
     */
    public final Optional<TypeSymbol> getSymbol( final Name name )
    {
        final var typeName = requireNonNullArgument( name, "name" ).toString();
        final var retValue = m_TypeSymbols.stream()
            .filter( s -> s.getTypeElement().getQualifiedName().toString().equals( typeName ) )
            .findFirst();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getSymbol()

    /**
     *  Loads the CSS from the resources.
     *
     *  @return The CSS styles.
     *  @throws IOException There is a problem on reading the resource with the
     *      CSS.
     */
    private static final String [] loadCSS() throws IOException
    {
        final String [] styles;
        final var module = UmlGraphLinkTaglet.class.getModule();
        final var cssResource = UmlGraphLinkTaglet.class.getResource( UML_CSS );
        if( isNull( cssResource ) )
        {
            throw new IOException( format( "Cannot locate resource '%2$s' in package '%1$s'", UmlGraphLinkTaglet.class.getPackageName(), UML_CSS ) );
        }
        try( final var cssInputStream = cssResource.openStream();
             final var cssReader = new BufferedReader( new InputStreamReader( cssInputStream, UTF_8 ) ) )
        {
            styles = cssReader.lines().toArray( String []::new );
        }

        final List<String> retValue = new ArrayList<>();
        var isComment = false;
        boolean endOfLine;
        final var line = new StringBuilder();
        int startPos, pos;
        StripCommentLoop: for( final var s : styles )
        {
            endOfLine = false;
            startPos = 0;
            LineScanLoop: while( !endOfLine )
            {
                if( isComment )
                {
                    pos = s.indexOf( "*/", startPos );
                    if( pos < 0 )
                    {
                        endOfLine = true;
                    }
                    else
                    {
                        isComment = false;
                        startPos = pos + 2;
                    }
                }
                else
                {
                    if( !line.isEmpty() ) line.append( ' ' );
                    pos = s.indexOf( "/*", startPos );
                    if( pos < 0 )
                    {
                        line.append( s.substring( startPos ) );
                        endOfLine = true;
                    }
                    else
                    {
                        if( pos > startPos )
                        {
                            line.append( s, startPos, pos );
                        }
                        isComment = true;
                        startPos = pos + 2;
                    }
                }
            }   //  LineScanLoop:

            if( isNotEmptyOrBlank( line ) ) retValue.add( line.toString() );
            line.setLength( 0 );
        }   //  StripCommentLoop:

        //---* Done *----------------------------------------------------------
        return retValue.toArray( EMPTY_String_ARRAY );
    }   //  loadCSS()

    /**
     *  Sets the width and height for the UML diagram.
     *
     *  @param  width   The width.
     *  @param  height  The height.
     */
    public final void setDimension( final double width, final double height )
    {
        m_Dimension = new Dimension( width, height );
        m_Root.setDimension( number( width ), number( height ) );
    }   //  setDimensions()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_Root.toString(); }
}
//  class UMLDocument

/*
 *  End of File
 */