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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This implementation of
 *  {@link Error}
 *  should be thrown in all cases where an unexpected exception was caught.
 *  Unlike an
 *  {@link ImpossibleExceptionError},
 *  the exception is <i>possible</i>, but not expected – probably because the
 *  programmer has taken (at least thought to have &hellip;) appropriate
 *  precautions against its occurrence. An example for this could be an
 *  implementation of
 *  {@link Object#clone() clone()}
 *  for a particular class: with implementing that method (and declaring the
 *  interface
 *  {@link Cloneable}
 *  &hellip;), instances of that class do support cloning and therefore that
 *  exception should not be thrown. Unfortunately, changes somewhere else in
 *  the source might prevent one of the attributes of the respective class from
 *  being cloned, so that exception could be thrown again &hellip;<br>
 *  <br>Another example could be this code sequence:<pre><code>  …
 *  File file = new File( … );
 *  if( file.exists() )
 *  {
 *      try( var inputStream = new FileInputStream( file ) )
 *      {
 *          …
 *      }
 *      catch( FileNotFoundException e )
 *      {
 *          throw new UnexpectedExceptionError( e );
 *      }
 *  }</code></pre>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UnexpectedExceptionError.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UnexpectedExceptionError.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed class UnexpectedExceptionError extends AssertionError
    permits ImpossibleExceptionError
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message for this exception.
     */
    private static final String MSG_UnexpectedException = "The Exception '%1$s' was not expected to occur in this context";

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
     *  Creates a new instance of the Error. The message is a constant, only
     *  the causing exception can be given.
     *
     *  @param  cause   The causing exception.
     */
    public UnexpectedExceptionError( final Throwable cause )
    {
        super( format( MSG_UnexpectedException, requireNonNullArgument( cause, "cause" ).getClass().getName() ) , cause );
    }   //  UnexpectedExceptionError()

    /**
     *  Creates a new instance of the Error. This constructor should be used in
     *  cases where an enhanced message is useful or necessary.
     *
     *  @param  message The message for the error.
     *  @param  cause   The causing exception.
     */
    public UnexpectedExceptionError( final String message, final Throwable cause )
    {
        super( nonNull( message ) && !message.isEmpty() ? message : format( MSG_UnexpectedException, requireNonNullArgument( cause, "cause" ).getClass().getName() ), requireNonNullArgument( cause, "cause" ) );
    }   //  UnexpectedExceptionError()
}
//  class UnexpectedExceptionError

/*
 *  End of File
 */