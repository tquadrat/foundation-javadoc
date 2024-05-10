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

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGDegree;
import org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement;

/**
 *  The definition of the SVG {@code <tspan>} element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGTSpan.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "unused" )
@ClassVersion( sourceVersion = "$Id: SVGTSpan.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface SVGTSpan extends SVGElementWithChildren, AllowsConditionalProcessingAttributes, AllowsGraphicalEventAttributes, AllowsPresentationAttributes, AllowsStyleAttributes
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a {@code CDATA} element to this {@code <tspan>} element.
     *
     *  @param  text    The text.
     *  @return This instance.
     */
    public XMLElement addCDATA( final CharSequence text );

    /**
     *  Adds text to this {@code <tspan>} element. Special characters will be
     *  escaped.
     *
     *  @param  text    The text.
     *  @return This instance.
     *  @throws IllegalArgumentException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public XMLElement addText( final CharSequence text );

    /**
     *  Sets a list of lengths which move the characters relative to the
     *  absolute position of the last glyph drawn. The n<sup>th</sup> length is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, the last length is applied to
     *  them.
     *
     *  @param  values  The lengths.
     */
    public void setDx( final SVGNumber... values );

    /**
     *  Sets a list of heights which move the characters relative to the
     *  absolute position of the last glyph drawn. The n<sup>th</sup> height is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, the last height is applied to
     *  them.
     *
     *  @param  values  The heights.
     */
    public void setDy( final SVGNumber... values );

    /**
     *  Sets the way the text length will be adjusted in order to meet the
     *  target length set by
     *  {@link #setTextLength(SVGNumber)}.
     *
     *  @param  flag    {@code true} means that both, spacing and glyph size
     *      will be adjusted to match, {@code false} indicates that only the
     *      spacing will be changed.
     */
    public void setLengthAdjust( final boolean flag );

    /**
     *  Sets a list of rotations for the glyphs. The n<sup>th</sup> rotation is
     *  given to n<sup>th</sup> character in the text. Additional characters
     *  are <em>not</em> given the last rotation (although some browsers may
     *  handle that differently).
     *
     *  @param  values  The rotations.
     */
    public void setRotate( final SVGDegree... values );

    /**
     *  Sets the target length for the text that an SVG viewer will attempt to
     *  display the text between by adjusting the spacing and/or the glyphs.
     *
     *  @param  value   The intended text length.
     */
    public void setTextLength( final SVGNumber value );

    /**
     *  Sets a list of x-axis position. The n<sup>th</sup> x-axis position is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, they are placed after the last
     *  character.
     *
     *  @param  values  The x-axis positions.
     */
    public void setX( final SVGNumber... values );

    /**
     *  Sets a list of y-axis position. The n<sup>th</sup> y-axis position is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, they are placed after the last
     *  character.
     *
     *  @param  values  The y-axis positions.
     */
    public void setY( final SVGNumber... values );
}
//  interface SVGTSpan

/*
 *  End of File
 */