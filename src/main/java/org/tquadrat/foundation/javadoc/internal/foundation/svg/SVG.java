/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGPreserveAspectRatio;
import jakarta.activation.MimeType;

/**
 *  <p>{@summary The definition of the SVG element
 *  <code>&lt;svg&gt;</code>.}</p>
 *  <p>The <code>&lt;svg&gt;</code> element is the root element for any SVG
 *  document, either stand-alone (in a file with the {@code *.svg} extension)
 *  or embedded in an HTML file (or elsewhere - including another SVG
 *  document). Therefore the definition for the namespaces is different for
 *  each usage. This is reflected by the type
 *  {@link Usage}.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVG.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 *
 *  @see "https://www.w3.org/TR/SVG/single-page.html#struct-SVGElement"
 */
@ClassVersion( sourceVersion = "$Id: SVG.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVG extends SVGElementWithChildren, AllowsConditionalProcessingAttributes, AllowsDocumentEventAttributes, AllowsDocumentElementEventAttributes, AllowsGraphicalEventAttributes, AllowsPresentationAttributes, AllowsStyleAttributes
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The different usages for an {@code <svg>} element.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVG.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVG.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static enum Usage
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  The {@code <svg>} element is used as the root for a stand-alone
         *  document.
         */
        @API( status = STABLE, since = "0.0.5" )
        STANDALONE_DOCUMENT,

        /**
         *  The {@code <svg>} element will be embedded into HTML (but not
         *  HTML&nbsp;5 - see
         *  {@link #EMBED_HTML5}).
         */
        @API( status = STABLE, since = "0.0.5" )
        EMBED_HTML,

        /**
         *  The {@code <svg>} element will be embedded into HTML&nbsp;5; as
         *  SVG is somehow part of the definition of HTML&nbsp;5, no further
         *  namespace declarations are required.
         */
        @API( status = STABLE, since = "0.0.5" )
        EMBED_HTML5,

        /**
         *  The {@code <svg>} element will be embedded into another
         *  {@code <svg>} element.
         */
        @API( status = STABLE, since = "0.0.5" )
        EMBED_SVG
    }
    //  enum Usage

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a child to the definitions of this {@code <svg>} element.
     *
     *  @param  <E> The implementation type for the {@code children}.
     *  @param  child   The child to add.
     *  @throws IllegalArgumentException    The given child is not valid to be
     *      added to a {@code <defs>} element, or it does not have an id.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not this element.
     */
    public <E extends SVGElement> void addDefinition( final E child ) throws IllegalArgumentException, IllegalStateException;

    /**
     *  Adds an SVG {@code <style>} element to the definitions of this
     *  {@code <svg>} element. If there exists already a {@code <style>}
     *  element, the new one will be merged into the existing one.
     *
     *  @param  style   The style to add.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not this element.
     */
    public void addStyle( final SVGStyle style ) throws IllegalStateException;

    /**
     *  Sets the default scripting language used to process the type strings
     *  in event attributes. This language must be used for all instances of
     *  script that do not specify their own scripting language.<br>
     *  <br>The type specifies a media type as a MIME document identifier; the
     *  default type is {@code application/ecmascript}.
     *
     *  @param  value   The default scripting language for this {@code <svg>}
     *      element.
     */
    public void setContentScriptType( final MimeType value );

    /**
     *  Sets the dimension for this {@code <svg>} element.
     *
     *  @param  width   The width of the element.
     *  @param  height  The height of the element.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public default void setDimension( final SVGNumber width, final SVGNumber height )
    {
        setHeight( requireNonNullArgument( height, "height" ) );
        setWidth( requireNonNullArgument( width, "width" ) );
    }   //  setDimension()

    /**
     *  Sets the height of the embedded {@code <svg>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setHeight( final SVGNumber value );

    /**
     *  Sets the mode for the aspect ratio preservation for this {@code <svg>}
     *  element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    public void setPreserveAspectRatio( final SVGPreserveAspectRatio value );

    /**
     *  <p>{@summary Sets the given CSS style definitions as a style sheet in
     *  an SVG {@code style} element to the <code>&lt;defs&gt;</code> element
     *  of this <code>&lt;svg&gt;</code> element.}</p>
     *  <p>This is <i>not</i> an attribute; instead an SVG {@code <style}
     *  element will be created.</p>
     *  <p>Consecutive calls to this method will not create additional
     *  {@code <style>} elements, instead the new styles will be added to the
     *  existing ones.</p>
     *
     *  @param  styles  The CSS style definitions.
     *  @return The SVG {@code <style>} element with the style sheet
     *      definitions.
     */
    public SVGStyle setStyleSheet( final CharSequence... styles );

    /**
     *  Defines the visible area in this SVG drawing area.
     *
     *  @param  x   The x coordinate of top left corner of the area.
     *  @param  y   The y coordinate of top left corner of the area.
     *  @param  width   The width of the area.
     *  @param  height  The height of the area.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setViewBox( final SVGNumber x, final SVGNumber y, final SVGNumber width, final SVGNumber height );

    /**
     *  Sets the width of the embedded {@code <svg>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setWidth( final SVGNumber value );

    /**
     *  Sets the x coordinate for the top left corner of the embedded
     *  {@code <svg>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setX( final SVGNumber value );

    /**
     *  Sets the y coordinate for the top left corner of the embedded
     *  {@code <svg>} element.
     *
     *  @param  value   The type; if {@code null} the
     *      attribute will be removed.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public void setY( final SVGNumber value );

    /**
     *  Sets the 'Zoom-and-Pan' flag; if enabled on a stand-alone instance of
     *  the {@code <svg>} element, it allows panning and zooming the image.
     *
     *  @param  flag    {@code true} enables the feature by setting
     *      &quot;{@code magnify}&quot; to the attribute
     *      {@value SVGUtils#SVGATTRIBUTE_ZoomAndPan}, {@code false} disables
     *      it by setting the attribute to &quot;{@code disable}&quot;.
     */
    public void setZoomAndPan( final boolean flag );
}
//  interface SVG

/*
 *  End of File
 */