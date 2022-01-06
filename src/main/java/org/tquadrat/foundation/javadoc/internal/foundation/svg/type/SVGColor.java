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

import static java.lang.Math.abs;
import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;

import java.util.function.IntUnaryOperator;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The implementation for SVG values representing colors.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGColor.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGColor.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class SVGColor extends SVGPaint
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  This constant is used to force the colour values into the range from
     *  0 to 255.
     */
    private static final int m_Divisor = 0x100;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The type 'inherit'.
     */
    public static final SVGColor COLOR_INHERIT;

    static
    {
        COLOR_INHERIT = new SVGColor();
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGColor} instance with the type
     *  &quot;inherit&quot;.
     */
    private SVGColor() { super( "inherit" ); }

    /**
     *  Creates a new {@code SVGColor} instance from the given colour
     *  values.<br>
     *  <br>Allowed are the values from 0 to 255, other values will be
     *  normalised accordingly.
     *
     *  @param  red The red component for the colour.
     *  @param  green   The green component for the colour.
     *  @param  blue    The blue component for the colour.
     */
    public SVGColor( final int red, final int green, final int blue )
    {
        super( format( "#%02x%02x%02x", abs( red ) % m_Divisor, abs( green ) % m_Divisor, abs( blue ) % m_Divisor ) );
    }   //  SVGColor()

    /**
     *  Creates a new {@code SVGColor} instance from the given colour
     *  values.<br>
     *  <br>Allowed are the values from 0 to 255, or 0% to 100% respectively,
     *  other values will be normalised accordingly.
     *
     *  @param  flag    {@code true} if the given values are percentages,
     *      {@code false} if they are absolute values.
     *  @param  red The red component for the colour.
     *  @param  green   The green component for the colour.
     *  @param  blue    The blue component for the colour.
     */
    public SVGColor( final boolean flag, final int red, final int green, final int blue )
    {
        super( composeColorValue( flag, red, green, blue ) );
    }   //  SVGColor()

    /**
     *  Creates a new {@code SVGColor} instance, using the given argument as a
     *  CSS colour name.<br>
     *  <br>The given argument may not be {@code null} nor the empty String,
     *  but it will not undergo any further validation.
     *
     *  @param  color   The CSS colour name.
     */
    public SVGColor( final String color ) { super( requireNotEmptyArgument( color, "color" ) ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Composes the colour type String from the given colour values.<br>
     *  <br>Allowed are the values from 0 to 255, or 0% to 100% respectively,
     *  other values will be normalised accordingly.
     *
     *  @param  flag    {@code true} if the given values are percentages,
     *      {@code false} if they are absolute values.
     *  @param  red The red component for the colour.
     *  @param  green   The green component for the colour.
     *  @param  blue    The blue component for the colour.
     *  @return The colour type String.
     */
    private static final String composeColorValue( final boolean flag, final int red, final int green, final int blue )
    {
        final IntUnaryOperator n = v -> abs( v ) > 100 ? abs( v ) % 101 : abs( v );
        final var retValue = flag
            ? format( "rgb(%d%%,%d%%,%d%%)", n.applyAsInt( red ), n.applyAsInt( green ), n.applyAsInt( blue ) )
            : format( "rgb(%d,%d,%d)", abs( red ) % m_Divisor, abs( green ) % m_Divisor, abs( blue ) % m_Divisor );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeColorValue()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && (obj instanceof SVGColor other) )
        {
            retValue = value().equals( other.value() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()
}
//  class SVGColor

/*
 *  End of File
 */