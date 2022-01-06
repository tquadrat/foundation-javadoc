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

package org.tquadrat.foundation.javadoc.internal.foundation.svg.internal;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_LengthAdjust;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_Rotate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_TextLength;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_dx;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_dy;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_x;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_y;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.ALLOWS_CHILDREN;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.ALLOWS_TEXT;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLElement.Flags.VALIDATES_ATTRIBUTES;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber;
import org.tquadrat.foundation.javadoc.internal.foundation.svg.type.SVGNumber.SVGDegree;

/**
 *  The abstract common base class for the implementation of interfaces
 *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGText}
 *  and
 *  {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGTSpan}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGTextBase.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "AbstractClassExtendsConcreteClass" )
@ClassVersion( sourceVersion = "$Id: SVGTextBase.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public abstract class SVGTextBase extends SVGElementImpl
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGTextBase} instance.
     *
     *  @param  elementName The element name.
     */
    protected SVGTextBase( final String elementName )
    {
        super( elementName, ALLOWS_CHILDREN, ALLOWS_TEXT, VALIDATES_ATTRIBUTES );
    }   //  SVGTextBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets a list of lengths which move the characters relative to the
     *  absolute position of the last glyph drawn. The n<sup>th</sup> length is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, the last length is applied to
     *  them.
     *
     *  @param  values  The lengths.
     */
    public final void setDx( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0) ? stream( values ).map( SVGNumber::toString ).collect( joining( "," ) ) : null;
        setAttribute( SVGATTRIBUTE_dx, value );
    }   //  setDx()

    /**
     *  Sets a list of heights which move the characters relative to the
     *  absolute position of the last glyph drawn. The n<sup>th</sup> height is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, the last height is applied to
     *  them.
     *
     *  @param  values  The heights.
     */
    public final void setDy( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0) ? stream( values ).map( SVGNumber::toString ).collect( joining( "," ) ) : null;
        setAttribute( SVGATTRIBUTE_dy, value );
    }   //  setDy()

    /**
     *  Sets the way the text length will be adjusted in order to meet the
     *  target length set by
     *  {@link #setTextLength(SVGNumber)}.
     *
     *  @param  flag    {@code true} means that both, spacing and glyph size
     *      will be adjusted to match, {@code false} indicates that only the
     *      spacing will be changed.
     */
    public final void setLengthAdjust( final boolean flag )
    {
        final var value = flag ? "spacingAndGlyphs" : "spacing";
        setAttribute( SVGATTRIBUTE_LengthAdjust, value );
    }   //  setLengthAdjust()

    /**
     *  Sets a list of rotations for the glyphs. The n<sup>th</sup> rotation is
     *  given to n<sup>th</sup> character in the text. Additional characters
     *  are <em>not</em> given the last rotation (although some browsers may
     *  handle that differently).
     *
     *  @param  values  The rotations.
     */
    public final void setRotate( final SVGDegree... values )
    {
        final var value = nonNull( values ) && (values.length != 0) ? stream( values ).map( SVGDegree::toString ).collect( joining( "," ) ) : null;
        setAttribute( SVGATTRIBUTE_Rotate, value );
    }   //  setRotate()

    /**
     *  Sets the target length for the text that an SVG viewer will attempt to
     *  display the text between by adjusting the spacing and/or the glyphs.
     *
     *  @param  value   The intended text length.
     */
    public final void setTextLength( final SVGNumber value ) { setAttribute( SVGATTRIBUTE_TextLength, value ); }

    /**
     *  Sets a list of x-axis position. The n<sup>th</sup> x-axis position is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, they are placed after the last
     *  character.
     *
     *  @param  values  The x-axis positions.
     */
    public final void setX( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0) ? stream( values ).map( SVGNumber::toString ).collect( joining( "," ) ) : null;
        setAttribute( SVGATTRIBUTE_x, value );
    }   //  setX()

    /**
     *  Sets a list of y-axis position. The n<sup>th</sup> y-axis position is
     *  given to n<sup>th</sup> character in the text. If there are additional
     *  characters after the positions run out, they are placed after the last
     *  character.
     *
     *  @param  values  The y-axis positions.
     */
    public final void setY( final SVGNumber... values )
    {
        final var value = nonNull( values ) && (values.length != 0) ? stream( values ).map( SVGNumber::toString ).collect( joining( "," ) ) : null;
        setAttribute( SVGATTRIBUTE_y, value );
    }   //  setY()
}
//  class SVGTextBase

/*
 *  End of File
 */