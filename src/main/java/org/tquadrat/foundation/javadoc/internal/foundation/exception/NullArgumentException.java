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

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where {@code null} is
 *  provided as an illegal argument value.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NullArgumentException.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: NullArgumentException.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed class NullArgumentException extends ValidationException
    permits EmptyArgumentException
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
     *  Creates a new instance of {@code NullArgumentException}.
     */
    public NullArgumentException() { this( null ); }

    /**
     *  Creates a new instance of {@code NullArgumentException}.
     *
     *  @param  argName The name of the argument whose value was provided as
     *      {@code null}; if {@code null} or the empty String, a
     *      default message is used that does not use the name of the argument.
     */
    public NullArgumentException( final String argName )
    {
        this( argName, "Argument '%1$s' must not be null", "Argument must not be null" );
    }   //  NullArgumentException()

    /**
     *  Creates a new instance of {@code NullArgumentException}.
     *
     *  @param  argName1    The name of the first argument whose value was
     *      provided as {@code null}.
     *  @param  argName2    The name of the second argument whose value was
     *      provided as {@code null}.
     */
    @API( status = STABLE, since = "0.0.7" )
    public NullArgumentException( final String argName1, final String argName2 )
    {
        this( format( "'%s' and '%s'", argName1, argName2 ), "%s are both null", EMPTY_STRING );
    }   //  NullArgumentException()

    /**
     *  Creates a new instance of {@code NullArgumentException}.<br>
     *  <br>This constructor was introduced for the
     *  {@link EmptyArgumentException}.
     *
     *  @param  argName The name of the argument whose value was provided as
     *      {@code null}; if {@code null} or the empty String, a
     *      default message is used that does not use the name of the argument.
     *  @param  msgName The regular message.
     *  @param  msgNone The default message.
     */
    protected NullArgumentException( final String argName, final String msgName, final String msgNone )
    {
        super( nonNull( argName ) && !argName.isEmpty() ? format( msgName, argName ) : msgNone );
    }   //  NullArgumentException()
}
//  class NullArgumentException

/*
 *  End of File
 */