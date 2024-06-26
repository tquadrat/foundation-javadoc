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

package org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi;

import static org.apiguardian.api.API.Status.MAINTAINED;

import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.internal.XMLElementImpl;

/**
 *  The abstract base class for specialised implementations of
 *  {@link org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement}.
 *  Because it is derived from
 *  {@link XMLElementImpl},
 *  it supports attributes, namespaces, children, text, {@code CDATA} and
 *  comments.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementAdapter.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"AbstractClassNeverImplemented", "AbstractClassExtendsConcreteClass"} )
@ClassVersion( sourceVersion = "$Id: XMLElementAdapter.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public abstract class XMLElementAdapter extends XMLElementImpl
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code XMLElementAdapter} instance.
     *
     *  @param  elementName The element name.
     */
    @SuppressWarnings( "unused" )
    protected XMLElementAdapter( final String elementName ) { super( elementName ); }

    /**
     *  Creates a new {@code XMLElementAdapter} instance.
     *
     *  @param  elementName The element name.
     *  @param  flags   The configuration flags for the new element.
     */
    protected XMLElementAdapter( final String elementName, final Set<Flags> flags )
    {
        super( elementName, flags );
    }   //  XMLElementAdapter()
}
//  class XMLElementAdapter

/*
 *  End of File
 */