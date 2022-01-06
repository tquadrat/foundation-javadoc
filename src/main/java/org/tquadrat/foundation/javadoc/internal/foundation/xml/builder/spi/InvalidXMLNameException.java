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

package org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi;

import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  This
 *  {@link RuntimeException Exception}
 *  will be thrown when an invalid XML name is used for the creation of an XML
 *  element, attribute, or namespace.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InvalidXMLNameException.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: InvalidXMLNameException.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public class InvalidXMLNameException extends IllegalArgumentException
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
     *  Creates a new {@code InvalidXMLNameException} instance.
     *
     *  @param  name    The invalid name.
     */
    public InvalidXMLNameException( final String name ) { super( requireNonNullArgument( name, "name" ) ); }
}
//  class InvalidXMLNameException

/*
 *  End of File
 */