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

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The abstract base class for SVG type class, providing some helper methods.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ValueBase.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: ValueBase.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
abstract sealed class ValueBase
    permits SVGPaint, SVGPathElement, SVGTransform
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ValueBase} instance.
     */
    protected ValueBase() { /* Just exists */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts a list of {@code double} values to a single String.
     *
     *  @param  separator   The separator.
     *  @param  values  The {@code double} values.
     *  @return The String.
     */
    protected static final String doubleToString( final char separator, final double... values )
    {
        final var retValue = stream( requireNonNullArgument( values, "values" ) )
            .mapToObj( ValueBase::formatDouble  )
            .collect( joining( Character.toString( separator ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  doubleToString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract boolean equals( Object obj );

    /**
     *  Converts a {@code double} to a String.
     *
     *  @param  value   The {@code double} type.
     *  @return The string.
     */
    protected static final String formatDouble( final double value ) { return format( ROOT, "%1.3f", value ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract int hashCode();

    /**
     *  Converts a list of {@code long} values to a single String.
     *
     *  @param  separator   The separator.
     *  @param  values  The {@code long} values.
     *  @return The String.
     */
    protected static final String longToString( final char separator, final long... values )
    {
        final var retValue = stream( requireNonNullArgument( values, "values" ) )
            .mapToObj( Long::toString )
            .collect( joining( Character.toString( separator ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  longToString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract String toString();
}
//  class ValueBase

/*
 *  End of File
 */