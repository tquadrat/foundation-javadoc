/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.javadoc.internal.foundation.exception;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;

import java.io.Serial;
import java.lang.reflect.Method;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This implementation of
 *  {@link RuntimeException}
 *  will be thrown when an attempted operation is not valid for the current
 *  context.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IllegalOperationException.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.1.0
 */
@SuppressWarnings( {"ClassWithTooManyConstructors", "unused"} )
@ClassVersion( sourceVersion = "$Id: IllegalOperationException.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class IllegalOperationException extends RuntimeException
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message for the illegal attempt to perform an operation: {@value}.
     */
    public static final String MSG_IllegalOperation = "Illegal attempt to perform the operation '%s'";

    /**
     *  The message for the illegal attempt to perform an operation, enhanced
     *  with an explanation: {@value}.
     */
    public static final String MSG_IllegalOperationWithExplanation = "Illegal attempt to perform the operation '%s' (Reason: %s)";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 2233464685783981770L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the given operation name. The cause is not
     *  initialized, and may subsequently be initialized by a call to
     *  {@link #initCause}.
     *
     *  @param  operationName   The name of the illegally attempted operation;
     *      usually, this is the method name. The detail message constructed
     *      from it is saved for later retrieval by the
     *      {@link #getMessage()}
     *      method.
     */
    public IllegalOperationException( final String operationName )
    {
        super( format( MSG_IllegalOperation, requireNotEmptyArgument( operationName, "operationName" ) ) );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the name of the given operation. The cause is not
     *  initialized, and may subsequently be initialized by a call to
     *  {@link #initCause}.
     *
     *  @param  operation   The illegally attempted operation. The detail
     *      message constructed from its name is saved for later retrieval by
     *      the
     *      {@link #getMessage()}
     *      method.
     */
    public IllegalOperationException( final Method operation )
    {
        this( requireNonNullArgument( operation, "operation" ).toGenericString() );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the given operation name and the given
     *  explanation. The cause is not initialized, and may subsequently be
     *  initialized by a call to
     *  {@link #initCause}.<br>
     *  <br>The detail message constructed from the operation name and the
     *  explanation is saved for later retrieval by the
     *  {@link #getMessage()}
     *  method.
     *
     *  @param  operationName   The name of the illegally attempted operation;
     *      usually, this is the method name.
     *  @param  explanation The additional explanation.
     */
    public IllegalOperationException( final String operationName, final String explanation )
    {
        super( format( MSG_IllegalOperationWithExplanation, requireNotEmptyArgument( operationName, "operationName" ), requireNotEmptyArgument( explanation, "explanation" ) ) );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the name of the given operation name and the
     *  given explanation. The cause is not initialized, and may subsequently
     *  be initialized by a call to
     *  {@link #initCause}.<br>
     *  <br>The detail message constructed from the operation name and the
     *  explanation is saved for later retrieval by the
     *  {@link #getMessage()}
     *  method.
     *
     *  @param  operation   The illegally attempted operation.
     *  @param  explanation The additional explanation.
     */
    public IllegalOperationException( final Method operation, final String explanation )
    {
        this( requireNonNullArgument( operation, "operation" ).toGenericString(), explanation );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the given operation name, and the cause.
     *
     *  @param  operationName   The name of the illegally attempted operation;
     *      usually, this is the method name. The detail message constructed
     *      from it is saved for later retrieval by the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause, which is saved for later retrieval by the
     *     {@link #getCause()}
     *     method. A {@code null} value is <i>not</i> permitted!
     */
    public IllegalOperationException( final String operationName, final Throwable cause )
    {
        super( format( MSG_IllegalOperation, requireNotEmptyArgument( operationName, "operationName" ) ), requireNonNullArgument( cause, "cause" ) );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with a detail
     *  message composed of the name of the given operation, and the cause.
     *
     *  @param  operation   The illegally attempted operation. The detail
     *      message constructed from its name is saved for later retrieval by
     *      the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause, which is saved for later retrieval by the
     *     {@link #getCause()}
     *     method. A {@code null} value is <i>not</i> permitted!
     */
    public IllegalOperationException( final Method operation, final Throwable cause )
    {
        this( requireNonNullArgument( operation, "operation" ).toGenericString(), cause );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with the cause and a
     *  detail message composed of the given operation name and the given
     *  explanation.<br>
     *  <br>The detail message constructed from the operation name and the
     *  explanation is saved for later retrieval by the
     *  {@link #getMessage()}
     *  method.
     *
     *  @param  operationName   The name of the illegally attempted operation;
     *      usually, this is the method name.
     *  @param  explanation The additional explanation.
     *  @param  cause   The cause, which is saved for later retrieval by the
     *     {@link #getCause()}
     *     method. A {@code null} value is <i>not</i> permitted!
     */
    public IllegalOperationException( final String operationName, final String explanation, final Throwable cause )
    {
        super(
            format( MSG_IllegalOperationWithExplanation,
                requireNotEmptyArgument( operationName, "operationName" ),
                requireNotEmptyArgument( explanation, "explanation" ) ),
            requireNonNullArgument( cause, "cause" ) );
    }   //  IllegalOperationException()

    /**
     *  Constructs a new {@code IllegalOperationException} with the cause and a
     *  detail message composed of the name of the given operation and the
     *  given explanation.<br>
     *  <br>The detail message constructed from the operation name and the
     *  explanation is saved for later retrieval by the
     *  {@link #getMessage()}
     *  method.
     *
     *  @param  operation   The illegally attempted operation.
     *  @param  explanation The additional explanation.
     *  @param  cause   The cause, which is saved for later retrieval by the
     *     {@link #getCause()}
     *     method. A {@code null} value is <i>not</i> permitted!
     */
    public IllegalOperationException( final Method operation, final String explanation, final Throwable cause )
    {
        this( requireNonNullArgument( operation, "operation" ).toGenericString(), explanation, cause );
    }   //  IllegalOperationException()
}
//  class IllegalOperationException

/*
 *  End of File
 */