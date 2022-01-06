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

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The definition for the SVG {@code <style>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGStyle.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: SVGStyle.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGStyle extends SVGElement
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code SVGStyle} objects.
     */
    public static final SVGStyle [] EMPTY_SVGStyle_ARRAY = new SVGStyle [0];

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a CSS style definition to this style.
     *
     *  @param  styles  The style definitions to add.
     */
    public void addStyle( final CharSequence... styles );

    /**
     *  Returns the style sheet.
     *
     *  @return The style definitions.
     */
    public String getStyleSheet();

    /**
     *  Merges the given SVG {@code <style>} element into this one.<br>
     *  <br>Only the CSS style definitions are taken from the other element,
     *  no attributes or comments.
     *
     *  @param  other   The other {@code <style>} element.
     */
    public void merge( final SVGStyle other );

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString( final int indentationLevel, final boolean prettyPrint );
}
//  class SVGStyle

/*
 *  End of File
 */