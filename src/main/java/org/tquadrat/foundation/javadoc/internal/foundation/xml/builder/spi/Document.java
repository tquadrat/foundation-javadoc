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
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.spi.SGMLPrinter.composeDocumentString;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.Namespace;

/**
 *  The definition for an SGML document.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Document.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <E> The implementation for the elements of this document.
 */
@ClassVersion( sourceVersion = "$Id: Document.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public interface Document<E extends Element>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the value for the attribute with the given name.<br>
     *  <br>The attribute is assigned to the
     *  {@linkplain #getRootElement() root element}
     *  of the document as documents itself cannot have attributes.
     *
     *  @param  name    The attribute name.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the value for that attribute.
     */
    @SuppressWarnings( "unused" )
    public default Optional<String> getAttribute( final String name ) { return getRootElement().getAttribute( name ); }

    /**
     *  Provides read access to the attributes of the
     *  {@linkplain #getRootElement() root element}
     *  of the document as documents itself cannot have attributes.
     *
     *  @return A reference to the attributes.
     */
    public default Map<String,String> getAttributes() { return getRootElement().getAttributes(); }

    /**
     *  Provides access to the children for this document; the returned
     *  collection is not modifiable.<br>
     *  <br>The
     *  {@linkplain #getRootElement() root element}
     *  is the last entry in the returned collection.
     *
     *  @return A reference to the children of this document.
     */
    public default Collection<? extends Element> getChildren() { return List.of( getRootElement() ); }

    /**
     *  Returns the name of the root element.
     *
     *  @return The name of the root element.
     */
    public default String getElementName() { return getRootElement().getElementName(); }

    /**
     *  Provides access to the namespaces for this document (although in fact,
     *  it will be the namespaces for the root element); the returned
     *  collection is not modifiable.
     *
     *  @return A reference to the namespaces for the root element of this
     *      document; if it does not have namespaces assigned, an empty
     *      collection will be returned.
     *
     *  @see Element#getNamespaces()
     */
    public default Collection<Namespace> getNamespaces() { return getRootElement().getNamespaces(); }

    /**
     *  Return the root element for this document.
     *
     *  @return The root element.
     */
    public E getRootElement();

    /**
     *  Returns a String representation for this element instance.
     *
     *  @param  prettyPrint The pretty print flag.
     *  @return The String representation.
     */
    public default String toString( final boolean prettyPrint )
    {
        final var retValue = composeDocumentString( prettyPrint, this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  interface Document

/*
 *  End of File
 */