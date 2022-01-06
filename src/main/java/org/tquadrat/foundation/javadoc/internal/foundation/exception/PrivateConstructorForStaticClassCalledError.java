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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This implementation of
 *  {@link Error}
 *  should be called from {@code private} constructors of static classes
 *  (classes that does not allow any instances because they have only static
 *  methods or constants). The message that is stored with the error is the
 *  name of the respective class.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PrivateConstructorForStaticClassCalledError.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 *
 *  @see AssertionError
 */
@ClassVersion( sourceVersion = "$Id: PrivateConstructorForStaticClassCalledError.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public class PrivateConstructorForStaticClassCalledError extends AssertionError
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
     *  Creates a new {@code ErrorPrivateConstructorForStaticClassCalled} instance.
     *
     *  @param  message Should be the name of the respective static class.
     */
    public PrivateConstructorForStaticClassCalledError( final String message ) { super( requireNonNullArgument( message, "message" ) ); }

    /**
     *  Creates a new {@code ErrorPrivateConstructorForStaticClassCalled} instance.
     *
     *  @param  theClass    The respective static class.
     */
    public PrivateConstructorForStaticClassCalledError( final Class<?> theClass ) { this( requireNonNullArgument( theClass, "theClass" ).getName() ); }
}
//  class PrivateConstructorForStaticClassCalledError

/*
 *  End of File
 */