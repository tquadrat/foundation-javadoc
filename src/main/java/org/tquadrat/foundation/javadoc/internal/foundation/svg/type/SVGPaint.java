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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The implementation for SVG values representing {@code paint} attribute
 *  values.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGPaint.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "ClassReferencesSubclass" )
@ClassVersion( sourceVersion = "$Id: SVGPaint.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed class SVGPaint extends ValueBase
    permits SVGColor
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The type.
     */
    private final String m_Value;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The type 'currentColor'.
     */
    public static final SVGPaint PAINT_CURRENTCOLOR;

    /**
     *  The type 'inherit'.
     */
    public static final SVGPaint PAINT_INHERIT;

    /**
     *  The type 'none'.
     */
    public static final SVGPaint PAINT_NONE;

    static
    {
        PAINT_CURRENTCOLOR = new SVGPaint( "currentColor" );
        PAINT_NONE = new SVGPaint();
        PAINT_INHERIT = new SVGPaint( "inherit" );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGPaint} instance with the type
     *  &quot;none&quot;.
     */
    private SVGPaint() { m_Value = "none"; }

    /**
     *  Creates a new {@code SVGPaint} instance with the given type.
     *
     *  @param  value   The type.
     */
    protected SVGPaint( final String value ) { m_Value = requireNotEmptyArgument( value, "type" ); }

    /**
     *  Creates a new {@code SVGPaint} instance from the given colour.
     *
     *  @param  color   The colour.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public SVGPaint( final SVGColor color )
    {
        m_Value = requireNonNullArgument( color, "color" ).value();
    }   //  SVGPaint()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "AccessingNonPublicFieldOfAnotherObject" )
    @Override
    public boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && (obj instanceof SVGPaint other) && (getClass() == other.getClass()) )
        {
            retValue = m_Value.equals( other.m_Value );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_Value.hashCode(); }

    /**
     *  Returns the type for this instance of {@code SVGPaint}.
     *
     *  @return The type.
     */
    public final String value() { return m_Value; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return value(); }
}
//  class SVGPaint

/*
 *  End of File
 */