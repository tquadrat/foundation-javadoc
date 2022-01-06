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

import static java.lang.String.join;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.stream;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGELEMENT_Style;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLBuilderUtils.createXMLElement;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.VALIDATES_ATTRIBUTES;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.SGMLPrinter.repeat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGElement;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGStyle;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.Element;

/**
 *  The implementation of the interface
 *  {@link SVGStyle}
 *  for the SVG {@code <style>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGStyleImpl.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGStyleImpl.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class SVGStyleImpl extends SVGElementImpl implements SVGStyle
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The lines of the CSS style definitions.
     */
    private final List<String> m_StyleDefinitions = new ArrayList<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGStyleImpl} instance.
     */
    public SVGStyleImpl()
    {
        super( SVGELEMENT_Style, VALIDATES_ATTRIBUTES );

        //---* The attributes for the <style> element *------------------------
        updateRegistries( emptyList(), SVGElement.CORE_ATTRIBUTES );
    }   //  SVGStyleImpl()

    /**
     *  Creates a new {@code SVGStyleImpl} instance.
     *
     *  @param  styles  The style definitions to add.
     */
    public SVGStyleImpl( final CharSequence... styles )
    {
        this();
        addStyle( styles );
    }   //  SVGStyleImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void addStyle( final CharSequence... styles )
    {
        for( final var style : requireNonNullArgument( styles, "styles" ) )
        {
            if( isNotEmptyOrBlank( style ) )
            {
                stream( style, '\n' ).forEach( m_StyleDefinitions::add );
            }
            else
            {
                m_StyleDefinitions.add( EMPTY_STRING );
            }
        }
    }   //  addStyle()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<? extends Element> getChildren()
    {
        final Collection<? extends Element> retValue;
        if( m_StyleDefinitions.isEmpty() )
        {
            retValue = super.getChildren();
        }
        else
        {
            final var styleSheet = join( "\n", m_StyleDefinitions );

            final var element = createXMLElement( getElementName() );
            for( final var child : super.getChildren() ) element.addChild( (XMLElement) child );
            element.addCDATA( styleSheet );

            retValue = element.getChildren();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getChildren()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getStyleSheet()
    {
        final var retValue = join( "\n", m_StyleDefinitions );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getStyleSheet()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean hasChildren() { return !m_StyleDefinitions.isEmpty() || super.hasChildren(); }

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( {"InstanceofConcreteClass", "AccessingNonPublicFieldOfAnotherObject"} )
    @Override
    public final void merge( final SVGStyle other )
    {
        if( requireNonNullArgument( other, "other" ) instanceof SVGStyleImpl styleImpl )
        {
            m_StyleDefinitions.addAll( styleImpl.m_StyleDefinitions );
        }
        else
        {
            addStyle( other.getStyleSheet() );
        }
    }   //  merge()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final String retValue;
        if( m_StyleDefinitions.isEmpty() )
        {
            retValue = super.toString( indentationLevel, prettyPrint );
        }
        else
        {
            final var indentation = prettyPrint ? "\n" + repeat( indentationLevel + 1 ) : "\n";
            final var styleSheet = m_StyleDefinitions.stream().collect( joining( indentation, indentation, indentation ) );

            final var element = createXMLElement( getElementName() );
            for( final var child : super.getChildren() ) element.addChild( (XMLElement) child );
            for( final var attribute : getAttributes().entrySet() ) element.setAttribute( attribute.getKey(), attribute.getValue() );
            element.addCDATA( styleSheet );

            retValue = element.toString( indentationLevel, prettyPrint );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class SVGStyleImpl

/*
 *  End of File
 */