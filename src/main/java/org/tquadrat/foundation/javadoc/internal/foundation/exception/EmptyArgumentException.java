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

package org.tquadrat.foundation.javadoc.internal.foundation.exception;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where an empty String,
 *  array or
 *  {@link java.util.Collection}
 *  argument is provided as an illegal argument value.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EmptyArgumentException.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 *
 */
@ClassVersion( sourceVersion = "$Id: EmptyArgumentException.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class EmptyArgumentException extends NullArgumentException
{
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
     *  Creates a new instance of {@code EmptyArgumentException}.
     */
    public EmptyArgumentException() { this( null ); }

    /**
     *  Creates a new instance of {@code EmptyArgumentException}.
     *
     *  @param  argName The name of the argument whose value was provided as
     *      empty; if {@code null} or the empty String, a default message
     *      is used that does not use the name of the argument.
     */
    public EmptyArgumentException( final String argName )
    {
        super( argName, "Argument '%1$s' must not be empty", "Argument must not be empty" );
    }   //  EmptyArgumentException()
}
//  class EmptyArgumentException

/*
 *  End of File
 */