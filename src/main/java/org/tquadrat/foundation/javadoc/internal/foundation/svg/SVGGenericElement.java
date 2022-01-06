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

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.ALLOWS_CHILDREN;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.internal.SVGElementImpl;

/**
 *  This is a generic implementation for an SVG element. This should only be
 *  used for elements that are not (yet) implemented with their own
 *  classes.<br>
 *  <br>The element allows all possible children and attributes as well as
 *  text. But if it should be added to an already existing paren element, it
 *  must be registered to it first, before it could be added.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGGenericElement.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGGenericElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class SVGGenericElement extends SVGElementAdapter
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGGenericElement} instance.
     *
     *  @param  elementName The name of the element.
     *  @param  flags   The flags that determine the behaviour of the new
     *      element.
     */
    SVGGenericElement( final String elementName, final Flags... flags )
    {
        super( elementName, flags );
    }   //  SVGGenericElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/

    /**
     *  Registers this element as a valid child with the given parent. More
     *  precisely, it adds the
     *  {@linkplain #getElementName()}
     *  of this element to the parent's list of valid children. This means that
     *  it is not necessary to repeat this call for other elements with the
     *  same name.
     *
     *  @param  parent  The parent element.
     */
    @SuppressWarnings( {"PublicMethodNotExposedInInterface", "InstanceofConcreteClass"} )
    public final void registerWithParent( final SVGElement parent)
    {
        if( (requireNonNullArgument( parent, "parent" ) instanceof SVGElementImpl parentElement) )
        {
            if( !parentElement.getFlags().contains( ALLOWS_CHILDREN ))
            {
                throw new IllegalArgumentException( format( "Element '%s' does not allow children", parent.getElementName() ) );
            }
            parentElement.registerValidChildren( getElementName() );
        }
        else
        {
            throw new IllegalArgumentException( format( "Element class '%2$s' does not extend '%1$s'", SVGElementImpl.class.getName(), parent.getClass().getName() ) );
        }
    }   //  registerWithParent()
}
//  class SVGGenericElement

/*
 *  End of File
 */