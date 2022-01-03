/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc.internal;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  This error will be thrown by the custom taglets in case an error condition
 *  is encountered.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: JavadocError.java 820 2020-12-29 20:34:22Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: JavadocError.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class JavadocError extends Error
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
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Constructs a new instance of {@code JavadocError} with the specified
     *  detail message. The cause is not initialized, and may subsequently be
     *  initialized by a call to
     *  {@link #initCause(Throwable)}.
     *
     *  @param  message The detail message. The detail message is saved for
     *      later retrieval by the
     *      {@link #getMessage()}
     *      method.
     */
    public JavadocError( final String message ) { super( message ); }

    /**
     *  Constructs a new instance of {@code JavadocError} with the specified
     *  detail message and a cause.
     *
     *  @note   The detail message associated with cause is not automatically
     *      incorporated in this error's detail message.
     *
     *  @param  message The detail message. The detail message is saved for
     *      later retrieval by the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause which is saved for later retrieval by the
     *      {@link #getCause()} method. A {@code null} value is permitted, and
     *      indicates that the cause is nonexistent or unknown.
     */
    public JavadocError( final String message, final Throwable cause ) { super( message, cause ); }
}
//  class JavadocError

/*
 *  End of File
 */