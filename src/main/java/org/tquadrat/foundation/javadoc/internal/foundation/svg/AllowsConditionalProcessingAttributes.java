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

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_ExternalResourcesRequired;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_RequiredExtensions;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_RequiredFeatures;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_SystemLanguage;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG elements that allow the conditional processing attributes
 *  {@value SVGUtils#SVGATTRIBUTE_ExternalResourcesRequired},
 *  {@value SVGUtils#SVGATTRIBUTE_RequiredExtensions},
 *  {@value SVGUtils#SVGATTRIBUTE_RequiredFeatures},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_SystemLanguage}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsConditionalProcessingAttributes.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "unused" )
@ClassVersion( sourceVersion = "$Id: AllowsConditionalProcessingAttributes.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsConditionalProcessingAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The conditional processing attributes.
     */
    public static final List<String> CONDITIONALPROCESSING_ATTRIBUTES = List.of(
        SVGATTRIBUTE_ExternalResourcesRequired,
        SVGATTRIBUTE_RequiredExtensions,
        SVGATTRIBUTE_RequiredFeatures,
        SVGATTRIBUTE_SystemLanguage
    );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the attribute that indicates the requirement for external
     *  resources for rendering this SVG element.
     *
     *  @param  flag    {@code true} if external resources are needed,
     *      {@code false} if all required resources are local to the current
     *      context.
     */
    public void setExternalResourcesRequired( final boolean flag );

    /**
     *  Sets a list of extensions that are required to render this SVG element.
     *
     *  @param  values  The URIs that identify the required extensions.
     */
    public void setRequiredExtensions( final URI... values );

    /**
     *  Sets a list of features that are required to render this SVG element.
     *
     *  @param  values  The URIs that identify the required features.
     */
    public void setRequiredFeatures( final URI... values );

    /**
     *  Sets a list of languages; the current SVG element will be rendered
     *  only if the current system language matches one entry of this list.
     *
     *  @param  values  The allowed languages.
     */
    public void setSystemLanguage( final Locale... values );
}
//  interface AllowsConditionalProcessingAttributes

/*
 *  End of File
 */