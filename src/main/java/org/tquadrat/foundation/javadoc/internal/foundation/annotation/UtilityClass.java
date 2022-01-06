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

package org.tquadrat.foundation.javadoc.internal.foundation.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 *  <p>{@summary This is a marker annotation for a utility class.}</p>
 *  <p>A utility class is a class that …</p>
 *  <ul>
 *      <li>… is final</li>
 *      <li>… has only a private constructor that throws a
 *      {@link org.tquadrat.foundation.javadoc.internal.foundation.exception.PrivateConstructorForStaticClassCalledError}
 *      when called (that works only through reflection …) and therefore does
 *      not allow any instances</li>
 *      <li>… has only methods that are {@code static final}</li>
 *      <li>… has only static final attributes (constants) and therefore should
 *      not have any status</li>
 *  </ul>
 *  <p>The class {@code org.tquadrat.foundation.testutil.TestBaseClass} for the
 *  {@code org.tquadrat.foundation.testutil} module provides a method
 *  {@code validateAsStaticClass()} that tests most of these conditions.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UtilityClass.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: UtilityClass.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@Documented
@Retention( SOURCE )
@Target( TYPE )
public @interface UtilityClass
{ /* Empty */ }
//  @interface UtilityClass

/*
 *  End of File
 */