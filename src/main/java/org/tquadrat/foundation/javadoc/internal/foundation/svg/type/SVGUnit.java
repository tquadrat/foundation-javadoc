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
import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG allows different units for some attributes; this class defines these
 *  units and the transformations into Strings for those units.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGUnit.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGUnit.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public enum SVGUnit
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  No explicit unit; usually this is equivalent to
     *  {@link #PIXEL}.
     */
    @API( status = STABLE, since = "0.0.5" )
    NONE( EMPTY_STRING ),

    /**
     *  Centimeters.
     */
    @API( status = STABLE, since = "0.0.5" )
    CENTIMETER( "cm" ),

    /**
     *  Unit based on the width of the current font; usually the width of the
     *  letter 'm' in this font.
     */
    @API( status = STABLE, since = "0.0.5" )
    EM( "em" ),

    /**
     *  Unit based on the current x-height.
     */
    @API( status = STABLE, since = "0.0.5" )
    EX( "ex" ),

    /**
     *  Inch.
     */
    @API( status = STABLE, since = "0.0.5" )
    INCH( "in" ),

    /**
     *  Millimeters.
     */
    @API( status = STABLE, since = "0.0.5" )
    MILLIMETER( "mm" ),

    /**
     *  Percent.
     */
    @API( status = STABLE, since = "0.0.5" )
    PERCENT( "%" ),

    /**
     *  Picas.
     */
    @API( status = STABLE, since = "0.0.5" )
    PICA( "pc" ),

    /**
     *  Pixels.
     */
    @API( status = STABLE, since = "0.0.5" )
    PIXEL( "px" ),

    /**
     *  Points.
     */
    @API( status = STABLE, since = "0.0.5" )
    POINT( "pt" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The format string for the unit for floating point values.
     */
    private final String m_FloatingPointFormat;

    /**
     *  The format string for the unit for integer values.
     */
    private final String m_IntegerFormat;

    /**
     *  The unit identifier.
     */
    private final String m_Unit;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGUnit} instance.
     *
     *  @param  identifier  The unit identifier.
     */
    private SVGUnit( final String identifier )
    {
        m_Unit = identifier;
        if( "%".equals( m_Unit ) )
        {
            m_FloatingPointFormat = "%1.3f%%";
            m_IntegerFormat = "%d%%";
        }
        else
        {
            m_FloatingPointFormat = "%1.3f" + m_Unit;
            m_IntegerFormat = "%d" + m_Unit;
        }
    }   //  SVGUnit()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Formats the given type for this unit.
     *
     *  @param  value   The type.
     *  @return The formatted type.
     */
    public final String format( final long value ) { return String.format( ROOT, m_IntegerFormat, value ); }

    /**
     *  Formats the given type for this unit.
     *
     *  @param  value   The type.
     *  @return The formatted type.
     */
    public final String format( final double value ) { return String.format( ROOT, m_FloatingPointFormat, value ); }

    /**
     *  Returns the {@code SVGUnit} instance for the given unit String.
     *
     *  @param  unit    The unit String.
     *  @return The {@code SVGUnit} for the given unit.
     */
    @SuppressWarnings( "AccessingNonPublicFieldOfAnotherObject" )
    @API( status = STABLE, since = "0.0.5" )
    public static final SVGUnit valueForUnit( final String unit )
    {
        requireNonNullArgument( unit, "unit" );
        final var retValue = stream( values() ).filter( v -> v.m_Unit.equalsIgnoreCase( unit ) ).findAny().orElseThrow( () -> new IllegalArgumentException( unit ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueForUnit()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_Unit; }
}
//  enum SVGUnit

/*
 *  End of File
 */