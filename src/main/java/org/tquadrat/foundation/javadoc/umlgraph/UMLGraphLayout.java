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

package org.tquadrat.foundation.javadoc.umlgraph;

import static java.lang.Math.signum;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.createRectangle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.number;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLConnectorType.IMPLEMENTATION;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLConnectorType.INHERITANCE;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLDocument.SVG_ELEMENT_SPACING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.JavadocError;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This class is used to lay out the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLGraphLayout.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLGraphLayout.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLGraphLayout
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  This class is used to lay out the UML graph.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: UMLGraphLayout.java 976 2022-01-06 11:39:58Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: UMLGraphLayout.java 976 2022-01-06 11:39:58Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.5")
    public static final class UMLGraphLayoutRow
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The content of this row.
         */
        private final Collection<TypeSymbol> m_Contents = new LinkedHashSet<>();

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code UMLGraphLayoutRow} object.
         */
        UMLGraphLayoutRow() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Add an element to this row; this method will not touch the width
         *  and height attributes for this row.
         *
         *  @param  typeSymbol  The new symbol.
         */
        public final void addSymbol( final TypeSymbol typeSymbol )
        {
            m_Contents.add( requireNonNullArgument( typeSymbol, "typeSymbol" ) );
        }   //  addSymbol()

        /**
         *  Distributes the symbols in this row horizontally.
         *
         *  @param  maxWidth    The maximum width of the row.
         */
        @SuppressWarnings( "MagicNumber" )
        final void distributeContents( final double maxWidth )
        {
            if( m_Contents.size() == 1 )
            {
                //---* Place the symbol in the middle of the row *-------------
                for( final var symbol : m_Contents )
                {
                    symbol.setX( (maxWidth - symbol.getDimension().width()) / 2.0 );
                }
            }
            else
            {
                final var columnSize = maxWidth / m_Contents.size();
                if( m_Contents.stream().mapToDouble( s -> s.getDimension().width() ).anyMatch( w -> w > columnSize ) )
                {
                    //---* Distribute with equal gaps *------------------------
                    var pos = SVG_ELEMENT_SPACING / 2.0;
                    for( final var symbol : m_Contents )
                    {
                        symbol.setX( pos );
                        pos += SVG_ELEMENT_SPACING + symbol.getDimension().width();
                    }
                }
                else
                {
                    //---* Distribute to columns *-----------------------------
                    var pos = columnSize / 2.0;
                    for( final var symbol : m_Contents )
                    {
                        symbol.setX( pos - symbol.getDimension().width() / 2.0 );
                        pos += columnSize;
                    }
                }
            }
        }   //  distributeContents()

        /**
         *  Returns the type symbol for the type with the given name.
         *
         *  @param  name    The name for the element.
         *  @return An instance of
         *      {@link Optional}
         *      that holds the desired type symbol.
         */
        final Optional<TypeSymbol> findByName( final String name )
        {
            requireNotEmptyArgument( name, "name" );

            final var retValue = m_Contents.stream().filter( e -> e.getTypeElement().getQualifiedName().toString().equals( name ) ).findAny();

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  findByName()

        /**
         *  Returns the UML class doc in this row.
         *
         *  @return The contents of this row.
         */
        public final TypeSymbol[] getContents() { return m_Contents.toArray( TypeSymbol[]::new ); }

        /**
         *  Returns the minimum height of the row; this is the height of the
         *  highest element in the row.
         *
         *  @return The height.
         */
        final double getHeight()
        {
            final var retValue = m_Contents.stream()
                .mapToDouble( s -> s.getDimension().height() )
                .max()
                .orElse( 0.0 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  getHeight()

        /**
         *  Returns the minimum width of the row; this is calculated as the sum
         *  of the width of the elements plus the value of
         *  {@link org.tquadrat.foundation.javadoc.umlgraph.UMLDocument#SVG_ELEMENT_SPACING}
         *  ({@value org.tquadrat.foundation.javadoc.umlgraph.UMLDocument#SVG_ELEMENT_SPACING})
         *  for each gap (with the gaps before the first and after the last do
         *  have half the size).
         *
         *  @return The width.
         */
        final double getWidth()
        {
            final var retValue = m_Contents.stream()
                .mapToDouble( s -> s.getDimension().width() )
                .sum() + m_Contents.size() * SVG_ELEMENT_SPACING;

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  getWidth()

        /**
         *  Checks if elements were added to this row.
         *
         *  @return {@code true} if the row has elements,
         *      {@code false} otherwise.
         */
        public final boolean isNotEmpty() { return !m_Contents.isEmpty(); }

        /**
         *  Removes the given type symbol from the contents of this row.
         *
         *  @param  typeSymbol  The symbol for the type element to remove.
         *  @return An instance of
         *      {@link Optional}
         *      that holds the removed type symbol.
         */
        @SuppressWarnings( "UnusedReturnValue" )
        final Optional<TypeSymbol> removeSymbol( final TypeSymbol typeSymbol )
        {
            Optional<TypeSymbol> retValue = Optional.empty();
            if( m_Contents.remove( requireNonNullArgument( typeSymbol, "typeSymbol" ) ) )
            {
                retValue = Optional.of( typeSymbol );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  removeSymbol()
    }
    //  class UMLGraphLayoutRow

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The current row.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private UMLGraphLayoutRow m_CurrentRow = null;

    /**
     *  The UML document.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final UMLDocument m_Document;

    /**
     *  The registry of connectors that will be terminated by the class with
     *  the given name.
     */
    private final Map<String,List<UMLConnector>> m_EndingConnectors = new HashMap<>();

    /**
     *  The row for class objects that do not have parents. These are the class
     *  {@link java.lang.Object}
     *  and all interfaces that do not extend other interfaces.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final UMLGraphLayoutRow m_NoParentRow;

    /**
     *  The rows for this layout.
     */
    private final List<UMLGraphLayoutRow> m_Rows = new ArrayList<>();

    /**
     *  The registry of connectors that will originate at the class with the
     *  given name.
     */
    private final Map<String,List<UMLConnector>> m_StartingConnectors = new HashMap<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLGraphLayout} object.
     *
     *  @param  document    The UML document.
     */
    public UMLGraphLayout( final UMLDocument document )
    {
        m_Document = requireNonNullArgument( document, "document" );
        m_NoParentRow = new UMLGraphLayoutRow();
        m_Rows.add( m_NoParentRow );
    }   //  UMLGraphLayout()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a connector to the registry of connectors ending on the given type
     *  symbol.
     *
     *  @param  typeSymbol  The symbol.
     *  @param  connector   The connector.
     */
    public final void addEndingConnector( final TypeSymbol typeSymbol, final UMLConnector connector )
    {
        final var key = requireNonNullArgument( typeSymbol, "typeSymbol").getQualifiedName();

        final var connectors = m_EndingConnectors.computeIfAbsent( key, k -> new ArrayList<>() );
        connectors.add( requireNonNullArgument( connector, "connector") );
    }   //  addEndingConnector()

    /**
     *  Creates a clippath from the given coordinates and add that to the UML
     *  document.
     *
     *  @param  x   The x coordinate of the upper left corner of the rectangle.
     *  @param  y   The y coordinate of the upper left corner of the rectangle.
     *  @param  width   The width of the rectangle.
     *  @param  height  The height of the rectangle.
     *  @return The id for the clippath element.
     */
    @SuppressWarnings( "unused" )
    public final String addClippath( final double x, final double y, final double width, final double height )
    {
        final var clipRectangle = createRectangle( number( x ), number( y ), number( width ), number( height ) );
        final var retValue = m_Document.addClippath( clipRectangle );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  addClippath()

    /**
     *  Adds a connector to the registry of connectors starting at the given
     *  type symbol.
     *
     *  @param  typeSymbol  The class symbol.
     *  @param  connector   The connector.
     */
    public final void addStartingConnector( final TypeSymbol typeSymbol, final UMLConnector connector )
    {
        final var key = requireNonNullArgument( typeSymbol, "typeSymbol").getQualifiedName();

        final var connectors = m_StartingConnectors.computeIfAbsent( key, k -> new ArrayList<>() );
        connectors.add( requireNonNullArgument( connector, "connector") );
    }   //  addStartingConnector()

    /**
     *  Creates a connector for the given parent and child.
     *
     *  @param  parent  The parent.
     *  @param  child   The child.
     */
    public final void createConnector( final TypeSymbol parent, final TypeSymbol child )
    {
        requireNonNullArgument( parent, "parent" );
        requireNonNullArgument( child, "child" );

        final var type = !child.isInterface() && parent.isInterface() ? IMPLEMENTATION : INHERITANCE;
        final var connector = new UMLConnector( type, parent, child );
        m_Document.addConnector( connector );
        addEndingConnector( parent, connector );
        addStartingConnector( child, connector );
    }   //  createConnector()

    /**
     *  Distribute the elements in the layout.
     */
    @SuppressWarnings( "MagicNumber" )
    public final void distribute()
    {
        //---* Make sure that parents and children are not in the same row *---
        var changed = true;
        List<UMLGraphLayoutRow> rowMirror = new ArrayList<>( m_Rows );
        while( changed )
        {
            changed = false;
            final List<UMLGraphLayoutRow> rowBuffer = new ArrayList<>();
            for( final var row : rowMirror )
            {
                UMLGraphLayoutRow optionalRow = null;
                for( final var typeSymbol : row.getContents() )
                {
                    final var hasChildInThisRow = stream( typeSymbol.getTypeElement().getChildTypes() )
                        .anyMatch( c -> row.findByName( c.getQualifiedName().toString() ).isPresent() );
                    if( hasChildInThisRow )
                    {
                        if( isNull( optionalRow ) ) optionalRow = new UMLGraphLayoutRow();
                        optionalRow.addSymbol( typeSymbol );
                        changed = true;
                    }
                }
                if( nonNull( optionalRow ) )
                {
                    rowBuffer.add( optionalRow );
                    for( final var typeSymbol : optionalRow.getContents() )
                    {
                        row.removeSymbol( typeSymbol );
                    }
                }
                rowBuffer.add( row );
            }
            rowMirror = rowBuffer;
        }
        m_Rows.clear();
        m_Rows.addAll( rowMirror );

        //---* Determine the maximum row length *------------------------------
        //noinspection OptionalGetWithoutIsPresent
        final var graphWidth = m_Rows.stream()
            .mapToDouble( UMLGraphLayoutRow::getWidth )
            .max()
            .getAsDouble();

        //---* Distribute the symbols in the rows *----------------------------
        m_Rows.forEach( r -> r.distributeContents( graphWidth ) );

        for( final var row : m_Rows )
        {
            for( final var symbol : row.getContents() )
            {
                if( symbol.getPosition().x() == 0.0 ) throw new JavadocError( "Symbol x coordinate is 0.0" );
            }
        }

        //---* Calculate the positions for the elements *----------------------
        final Comparator<UMLConnector> endPosSorter = ( c1, c2) -> (int) signum( c1.getChildTypeSymbol().getPosition().x() - c2.getChildTypeSymbol().getPosition().x() );
        final Comparator<UMLConnector> startPosSorter = ( c1, c2) -> (int) signum( c1.getParentTypeSymbol().getPosition().x() - c2.getParentTypeSymbol().getPosition().x() );
        var y = SVG_ELEMENT_SPACING / 2.0;

        for( final var row : m_Rows )
        {
            for( final var symbol : row.getContents() )
            {
                final List<UMLConnector> startingConnectors = new ArrayList<>( m_StartingConnectors.getOrDefault( symbol.getQualifiedName(), emptyList() ) );
                final List<UMLConnector> endingConnectors = new ArrayList<>( m_EndingConnectors.getOrDefault( symbol.getQualifiedName(), emptyList() ) );
                startingConnectors.sort( startPosSorter );
                endingConnectors.sort( endPosSorter );
                symbol.setY( y );
                final var use = symbol.createUse( startingConnectors, endingConnectors );
                m_Document.addUse( use );
            }
            y += row.getHeight() + SVG_ELEMENT_SPACING * 2.0;
        }

        //---* Set the document dimensions *-----------------------------------
        m_Document.setDimension( graphWidth, y + SVG_ELEMENT_SPACING / 2.0 );
    }   //  distribute()

    /**
     *  Call this method if all elements were added to the current row.
     *
     *  @param  atEnd   {@code true} if the row should be added to the
     *      end, {@code false} if it should be added to the beginning of the
     *      row's list.
     */
    public final void finishRow( final boolean atEnd )
    {
        if( nonNull( m_CurrentRow ) && m_CurrentRow.isNotEmpty() )
        {
            if( atEnd )
            {
                m_Rows.add( m_CurrentRow );
            }
            else
            {
                m_Rows.add( 1, m_CurrentRow );
            }
        }
        m_CurrentRow = null;
    }   //  finishRow()

    /**
     *  Returns a reference to the
     *  {@link UMLDocument}
     *  this instance of layout was created with.
     *
     *  @return The UML document.
     */
    public final UMLDocument getDocument() { return m_Document; }

    /**
     *  Returns a reference to the row that holds the class objects that does
     *  not have parents.
     *
     *  @return The row for parent-less class objects.
     */
    public final UMLGraphLayoutRow getNoParentRow() { return m_NoParentRow; }

    /**
     *  Creates a new row for the layout; it is not yet added.
     *
     *  @return The new row.
     */
    public final UMLGraphLayoutRow newRow()
    {
        final var retValue = new UMLGraphLayoutRow();
        m_CurrentRow = retValue;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  newRow()
}
//  class UMLGraphLayout

/*
 *  End of File
 */