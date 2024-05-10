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

package org.tquadrat.foundation.javadoc.internal.foundation.exception;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This is a specialized implementation for
 *  {@link Error}
 *  that is to be thrown especially from the {@code default} branch of a
 *  {@code switch} statement that uses an {@code enum} type as selector.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UnsupportedEnumError.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UnsupportedEnumError.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class UnsupportedEnumError extends Error
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message text.
     */
    private static final String MSG_UnsupportedEnum = "The value '%2$s' of enum class '%1$s' is not supported";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1174360235354917591L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of this class.
     *
     *  @param <T>  The type of the enum.
     *  @param  value   The unsupported value.
     */
    public <T extends Enum<T>> UnsupportedEnumError( final T value )
    {
        super( format( MSG_UnsupportedEnum, requireNonNullArgument( value, "value" ).getClass().getName(), value.name() ) );
    }   //  UnsupportedEnumError()

    /**
     *  Creates a new instance of this class.
     *
     *  @param  type    The class of the enum.
     *  @param  value   The unsupported value.
     */
    @SuppressWarnings( "unused" )
    public UnsupportedEnumError( final Class<? extends Enum<?>> type, final String value )
    {
        super( format( MSG_UnsupportedEnum, requireNonNullArgument( type, "type" ).getName(), requireNotEmptyArgument( value, "value" ) ) );
    }   //  UnsupportedEnumError()
}
//  class UnsupportedEnumError

/*
 *  End of File
 */