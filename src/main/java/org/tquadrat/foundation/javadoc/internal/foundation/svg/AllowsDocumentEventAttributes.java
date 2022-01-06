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
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnAbort;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnError;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnResize;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnScroll;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnUnload;

import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG elements that allow the document event attributes
 *  {@value SVGUtils#SVGATTRIBUTE_OnAbort},
 *  {@value SVGUtils#SVGATTRIBUTE_OnError},
 *  {@value SVGUtils#SVGATTRIBUTE_OnResize},
 *  {@value SVGUtils#SVGATTRIBUTE_OnScroll},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_OnUnload}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsDocumentEventAttributes.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: AllowsDocumentEventAttributes.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsDocumentEventAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The document event attributes.
     */
    public static final List<String> DOCUMENTEVENT_ATTRIBUTES = List.of(
        SVGATTRIBUTE_OnAbort,
        SVGATTRIBUTE_OnError,
        SVGATTRIBUTE_OnResize,
        SVGATTRIBUTE_OnScroll,
        SVGATTRIBUTE_OnUnload
    );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the abort handler for this SVG element.
     *
     *  @param  value   The abort handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnAbort
     */
    public void setAbortHandler( final String value );

    /**
     *  Sets the error handler for this SVG element.
     *
     *  @param  value   The error handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnError
     */
    public void setErrorHandler( final String value );

    /**
     *  Sets the resize handler for this SVG element.
     *
     *  @param  value   The resize handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnResize
     */
    public void setResizeHandler( final String value );

    /**
     *  Sets the scroll handler for this SVG element.
     *
     *  @param  value   The scroll handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnScroll
     */
    public void setScrollHandler( final String value );

    /**
     *  Sets the &quot;unload&quot; handler for this SVG element.
     *
     *  @param  value   The unload handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnUnload
     */
    public void setUnloadHandler( final String value );
}
//  interface AllowsDocumentEventAttributes

/*
 *  End of File
 */