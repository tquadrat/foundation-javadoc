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

package org.tquadrat.foundation.javadoc.internal.foundation.svg.type;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.NULL_CHAR;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The abstract base class for the elements of a path (a <i>path
 *  definition</i>) that can be applied to some elements with the attribute
 *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_PathDefinition}. <br>
 *  <br>The following is the BNF for such an SVG path definition:<br>
 *  <pre><code>  svg-path:
 *      wsp* moveto-drawto-command-groups? wsp*
 *  moveto-drawto-command-groups:
 *      moveto-drawto-command-group
 *      | moveto-drawto-command-group wsp* moveto-drawto-command-groups
 *  moveto-drawto-command-group:
 *      moveto wsp* drawto-commands?
 *  drawto-commands:
 *      drawto-command
 *      | drawto-command wsp* drawto-commands
 *  drawto-command:
 *      closepath
 *      | lineto
 *      | horizontal-lineto
 *      | vertical-lineto
 *      | curveto
 *      | smooth-curveto
 *      | quadratic-bezier-curveto
 *      | smooth-quadratic-bezier-curveto
 *      | elliptical-arc
 *  moveto:
 *      ( "M" | "m" ) wsp* moveto-argument-sequence
 *  moveto-argument-sequence:
 *      coordinate-pair
 *      | coordinate-pair comma-wsp? lineto-argument-sequence
 *  closepath:
 *      ("Z" | "z")
 *  lineto:
 *      ( "L" | "l" ) wsp* lineto-argument-sequence
 *  lineto-argument-sequence:
 *      coordinate-pair
 *      | coordinate-pair comma-wsp? lineto-argument-sequence
 *  horizontal-lineto:
 *      ( "H" | "h" ) wsp* horizontal-lineto-argument-sequence
 *  horizontal-lineto-argument-sequence:
 *      coordinate
 *      | coordinate comma-wsp? horizontal-lineto-argument-sequence
 *  vertical-lineto:
 *      ( "V" | "v" ) wsp* vertical-lineto-argument-sequence
 *  vertical-lineto-argument-sequence:
 *      coordinate
 *      | coordinate comma-wsp? vertical-lineto-argument-sequence
 *  curveto:
 *      ( "C" | "c" ) wsp* curveto-argument-sequence
 *  curveto-argument-sequence:
 *      curveto-argument
 *      | curveto-argument comma-wsp? curveto-argument-sequence
 *  curveto-argument:
 *      coordinate-pair comma-wsp? coordinate-pair comma-wsp? coordinate-pair
 *  smooth-curveto:
 *      ( "S" | "s" ) wsp* smooth-curveto-argument-sequence
 *  smooth-curveto-argument-sequence:
 *      smooth-curveto-argument
 *      | smooth-curveto-argument comma-wsp? smooth-curveto-argument-sequence
 *  smooth-curveto-argument:
 *      coordinate-pair comma-wsp? coordinate-pair
 *  quadratic-bezier-curveto:
 *      ( "Q" | "q" ) wsp* quadratic-bezier-curveto-argument-sequence
 *  quadratic-bezier-curveto-argument-sequence:
 *      quadratic-bezier-curveto-argument
 *      | quadratic-bezier-curveto-argument comma-wsp?
 *          quadratic-bezier-curveto-argument-sequence
 *  quadratic-bezier-curveto-argument:
 *      coordinate-pair comma-wsp? coordinate-pair
 *  smooth-quadratic-bezier-curveto:
 *      ( "T" | "t" ) wsp* smooth-quadratic-bezier-curveto-argument-sequence
 *  smooth-quadratic-bezier-curveto-argument-sequence:
 *      coordinate-pair
 *      | coordinate-pair comma-wsp? smooth-quadratic-bezier-curveto-argument-sequence
 *  elliptical-arc:
 *      ( "A" | "a" ) wsp* elliptical-arc-argument-sequence
 *  elliptical-arc-argument-sequence:
 *      elliptical-arc-argument
 *      | elliptical-arc-argument comma-wsp? elliptical-arc-argument-sequence
 *  elliptical-arc-argument:
 *      nonnegative-number comma-wsp? nonnegative-number comma-wsp?
 *          number comma-wsp flag comma-wsp? flag comma-wsp? coordinate-pair
 *  coordinate-pair:
 *      coordinate comma-wsp? coordinate
 *  coordinate:
 *      number
 *  nonnegative-number:
 *      integer-constant
 *      | floating-point-constant
 *  number:
 *      sign? integer-constant
 *      | sign? floating-point-constant
 *  flag:
 *      "0" | "1"
 *  comma-wsp:
 *      (wsp+ comma? wsp*) | (comma wsp*)
 *  comma:
 *      ","
 *  integer-constant:
 *      digit-sequence
 *  floating-point-constant:
 *      fractional-constant exponent?
 *      | digit-sequence exponent
 *  fractional-constant:
 *      digit-sequence? "." digit-sequence
 *      | digit-sequence "."
 *  exponent:
 *      ( "e" | "E" ) sign? digit-sequence
 *  sign:
 *      "+" | "-"
 *  digit-sequence:
 *      digit
 *      | digit digit-sequence
 *  digit:
 *      "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
 *  wsp:
 *      (#x20 | #x9 | #xD | #xA)</code></pre>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"AbstractClassWithoutAbstractMethods", "PublicInnerClass"} )

@ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public abstract sealed class SVGPathElement extends ValueBase
    permits SVGPathElement.SVGArcTo, SVGPathElement.SVGClosePath,
        SVGPathElement.SVGCubicCurveTo, SVGPathElement.SVGHLineTo,
        SVGPathElement.SVGLineTo, SVGPathElement.SVGMoveTo,
        SVGPathElement.SVGQuadraticCurveTo, SVGPathElement.SVGVLineTo
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The implementation of the SVG path {@code ArcTo} element. <br>
     *  <br>The <i>elliptical arc</i> command draws a section of an ellipse
     *  which meets the following constraints:
     *  <ul>
     *  <li>the arc starts at the current point</li>
     *  <li>the arc ends at point (<i>x</i>, <i>y</i>)</li>
     *  <li>the ellipse has the two radii (<i>rx</i>, <i>ry</i>)</li>
     *  <li>the x-axis of the ellipse is rotated by <i>x-axis-rotation</i>
     *  degrees relative to the x-axis of the current coordinate system</li>
     *  </ul>
     *  For most situations, there are actually four different arcs (two
     *  different ellipses, each with two different arc sweeps) that satisfy
     *  these constraints. {@code largeArc} (<i>large-arc-flag</i> in SVG) and
     *  {@code sweep} (<i>sweep-flag</i>) indicate which one of the four arcs
     *  are drawn, as follows:
     *  <ul>
     *  <li>Of the four candidate arc sweeps, two will represent an arc sweep
     *  of greater than or equal to 180 degrees (the &quot;large-arc&quot;),
     *  and two will represent an arc sweep of less than or equal to 180
     *  degrees (the &quot;small-arc&quot;). If {@code largeArc} is
     *  {@code true}, then one of the two larger arc sweeps will be chosen;
     *  otherwise, if {@code largeArc}&nbsp;==&nbsp;{@code false}, one of the
     *  smaller arc sweeps will be chosen</li>
     *  <li>If {@code sweep}&nbsp;==&nbsp;{@code true}, then the arc will be
     *  drawn in a &quot;positive-angle&quot; direction (i.e., the ellipse
     *  formula <code>x&nbsp;=&nbsp;<i>cx</i>&nbsp;+&nbsp;<i>rx</i>&nbsp;*&nbsp;cos(&nbsp;theta&nbsp;)</code>
     *  and
     *  <code>y&nbsp;=&nbsp;<i>cy</i>&nbsp;+&nbsp;<i>ry</i>&nbsp;*&nbsp;sin(&nbsp;theta&nbsp;)</code>
     *  is evaluated such that {@code theta} starts at an angle corresponding
     *  to the current point and increases positively until the arc reaches
     *  (<i>x</i>,<i>y</i>)). A type of {@code false}) causes the arc to be
     *  drawn in a &quot;negative-angle&quot; direction (i.e., {@code theta}
     *  starts at an angle type corresponding to the current point and
     *  decreases until the arc reaches (<i>x</i>,<i>y</i>)).</li>
     *  </ul>
     *  The following illustrates the four combinations of
     *  {@code largeArc} (<i>large-arc-flag</i>) and {@code sweep}
     *  (<i>sweep-flag</i>) and the four different arcs that will be drawn
     *  based on the values of these flags. For each case, the following path
     *  data command was used:<pre><code>  &lt;path d=&quot;M 125,75 a100,50 0 ?,? 100,50&quot;
     *        style=&quot;fill:none; stroke:red; stroke-width:6&quot;/&gt;</code></pre>
     *  In the SVG code, {@code true} is represented as {@code 1} and
     *  {@code false} as {@code 0}, so &quot;{@code ?,?}&quot; is replaced by
     *  &quot;{@code 0,0}&quot;, &quot;{@code 0,1}&quot;,
     *  &quot;{@code 1,0}&quot; and &quot;{@code 1,1}&quot; to generate the
     *  four possible cases.
     *  <img src="doc-files/arcs02.svg" alt="Sample Images">
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGArcTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGArcTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  rx  The x radius for the ellipsis.
         *  @param  ry  The y radius for the ellipsis.
         *  @param  rotation    The rotation of the x-axis.
         *  @param  largeArc    {@code true} to draw the larger arc,
         *      {@code false} for the smaller arc.
         *  @param  sweep   {@code true} to draw the arc in
         *      &quot;positive-angle&quot; direction, {@code false} for
         *      drawing it in a &quot;negative-angle&quot; direction.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        @SuppressWarnings( "BooleanParameter" )
        public SVGArcTo( final boolean isAbsolute, final double rx, final double ry, final double rotation, final boolean largeArc, final boolean sweep, final double x, final double y )
        {
            super( isAbsolute ? 'A' : 'a', format( "%s,%d,%d,%s", doubleToString( ',', rx, ry, rotation ), largeArc ? 1 : 0,sweep ?  1 : 0,  doubleToString( ',', x, y ) ) );
        }   //  SVGArcTo()

        /**
         *  Creates a new {@code SVGArcTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  rx  The x radius for the ellipsis.
         *  @param  ry  The y radius for the ellipsis.
         *  @param  rotation    The rotation of the x-axis.
         *  @param  largeArc    {@code true} to draw the larger arc,
         *      {@code false} for the smaller arc.
         *  @param  sweep   {@code true} to draw the arc in
         *      &quot;positive-angle&quot; direction, {@code false} for
         *      drawing it in a &quot;negative-angle&quot; direction.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        @SuppressWarnings( "BooleanParameter" )
        public SVGArcTo( final boolean isAbsolute, final long rx, final long ry, final long rotation, final boolean largeArc, final boolean sweep, final long x, final long y )
        {
            super( isAbsolute ? 'A' : 'a', longToString( ',', rx, ry, rotation, largeArc ? 1 : 0,sweep ?  1 : 0,  x, y ) );
        }   //  SVGArcTo()
    }
    //  class SVGArcTo

    /**
     *  The implementation of the SVG path {@code ClosePath} element.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGClosePath extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGClosePath} instance.
         */
        public SVGClosePath() { super( 'Z', EMPTY_STRING ); }
    }
    //  class SVGClosePath

    /**
     *  The implementation of the SVG cubic B&eacute;zier {@code CurveTo} curve
     *  path element. In its shorthand or smooth variant, the first control
     *  point is assumed to be the reflection of the second control point on
     *  the previous path element relative to the current point (if there is no
     *  previous path element or if the previous path element was not a cubic
     *  B&eacute;zier curve, it is assumed that the first control point is
     *  coincident with the current point.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGCubicCurveTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGCubicCurveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x1   The x coordinate for the first control point.
         *  @param  y1   The y coordinate for the first control point.
         *  @param  x2   The x coordinate for the second control point
         *  @param  y2   The y coordinate for the second control point
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGCubicCurveTo( final boolean isAbsolute, final double x1, final double y1, final double x2, final double y2, final double x, final double y )
        {
            super( isAbsolute ? 'C' : 'c', doubleToString( ',', x1, y1, x2, y2, x, y ) );
        }   //  SVGCubicCurveTo()

        /**
         *  Creates a new {@code SVGCubicCurveTo} instance for the smooth
         *  variant.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x2   The x coordinate for the second control point
         *  @param  y2   The y coordinate for the second control point
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGCubicCurveTo( final boolean isAbsolute, final double x2, final double y2, final double x, final double y )
        {
            super( isAbsolute ? 'S' : 's', doubleToString( ',', x2, y2, x, y ) );
        }   //  SVGCubicCurveTo()

        /**
         *  Creates a new {@code SVGCubicCurveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x1   The x coordinate for the first control point.
         *  @param  y1   The y coordinate for the first control point.
         *  @param  x2   The x coordinate for the second control point
         *  @param  y2   The y coordinate for the second control point
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGCubicCurveTo( final boolean isAbsolute, final long x1, final long y1, final long x2, final long y2, final long x, final long y )
        {
            super( isAbsolute ? 'C' : 'c', longToString( ',', x1, y1, x2, y2, x, y ) );
        }   //  SVGCubicCurveTo()

        /**
         *  Creates a new {@code SVGCubicCurveTo} instance for the smooth
         *  variant.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x2   The x coordinate for the second control point
         *  @param  y2   The y coordinate for the second control point
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGCubicCurveTo( final boolean isAbsolute, final long x2, final long y2, final long x, final long y )
        {
            super( isAbsolute ? 'S' : 's', longToString( ',', x2, y2, x, y ) );
        }   //  SVGCubicCurveTo()
    }
    //  class SVGCubicCurveTo

    /**
     *  The implementation of the SVG path {@code LineTo} element for a
     *  horizontal line.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGHLineTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGHLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinate is
         *      absolute, {@code false} if it is relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         */
        public SVGHLineTo( final boolean isAbsolute, final double x )
        {
            super( isAbsolute ? 'H' : 'h', formatDouble( x ) );
        }   //  SVGHLineTo()

        /**
         *  Creates a new {@code SVGHLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinate is
         *      absolute, {@code false} if it is relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         */
        public SVGHLineTo( final boolean isAbsolute, final long x )
        {
            super( isAbsolute ? 'H' : 'h', Long.toString( x ) );
        }   //  SVGHLineTo()
    }
    //  class SVGHLineTo

    /**
     *  The implementation of the SVG path {@code LineTo} element.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGLineTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGLineTo( final boolean isAbsolute, final double x, final double y )
        {
            super( isAbsolute ? 'L' : 'l', doubleToString( ',', x, y ) );
        }   //  SVGLineTo()

        /**
         *  Creates a new {@code SVGLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGLineTo( final boolean isAbsolute, final long x, final long y )
        {
            super( isAbsolute ? 'L' : 'l', longToString( ',', x, y ) );
        }   //  SVGLineTo()
    }
    //  class SVGLineTo

    /**
     *  The implementation of the SVG path {@code MoveTo} element.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGMoveTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGMoveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate.
         *  @param  y   The y coordinate.
         */
        public SVGMoveTo( final boolean isAbsolute, final double x, final double y )
        {
            super( isAbsolute ? 'M' : 'm', doubleToString( ',', x, y ) );
        }   //  SVGMoveTo()

        /**
         *  Creates a new {@code SVGMoveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate.
         *  @param  y   The y coordinate.
         */
        public SVGMoveTo( final boolean isAbsolute, final long x, final long y )
        {
            super( isAbsolute ? 'M' : 'm', longToString( ',', x, y ) );
        }   //  SVGMoveTo()
    }
    //  class SVGMoveTo

    /**
     *  The implementation of the SVG quadratic B&eacute;zier {@code CurveTo}
     *  curve path element. In its shorthand or smooth variant,  the control
     *  point is assumed to be the reflection of the control point on the
     *  previous path element relative to the current point (if there is no
     *  previous path element or if the previous path element was not a
     *  quadratic B&eacute;zier curve, it is assumed that the control point is
     *  coincident with the current point.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGQuadraticCurveTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGQuadraticCurveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x1   The x coordinate for the control point.
         *  @param  y1   The y coordinate for the control point.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGQuadraticCurveTo( final boolean isAbsolute, final double x1, final double y1, final double x, final double y )
        {
            super( isAbsolute ? 'Q' : 'q', doubleToString( ',', x1, y1, x, y ) );
        }   //  SVGQuadraticCurveTo()

        /**
         *  Creates a new {@code SVGQuadraticCurveTo} instance for the smooth
         *  variant.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGQuadraticCurveTo( final boolean isAbsolute, final double x, final double y )
        {
            super( isAbsolute ? 'T' : 't', doubleToString( ',', x, y ) );
        }   //  SVGQuadraticCurveTo()

        /**
         *  Creates a new {@code SVGQuadraticCurveTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x1   The x coordinate for the control point.
         *  @param  y1   The y coordinate for the control point.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGQuadraticCurveTo( final boolean isAbsolute, final long x1, final long y1, final long x, final long y )
        {
            super( isAbsolute ? 'Q' : 'q', longToString( ',', x1, y1, x, y ) );
        }   //  SVGQuadraticCurveTo()

        /**
         *  Creates a new {@code SVGQuadraticCurveTo} instance for the smooth
         *  variant.
         *
         *  @param  isAbsolute  {@code true} if the given coordinates are
         *      absolute, {@code false} if they are relative to the last point
         *      on the path.
         *  @param  x   The x coordinate for the end point of the line.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGQuadraticCurveTo( final boolean isAbsolute, final long x, final long y )
        {
            super( isAbsolute ? 'T' : 't', longToString( ',', x, y ) );
        }   //  SVGQuadraticCurveTo()
    }
    //  class SVGQuadraticCurveTo

    /**
     *  The implementation of the SVG path {@code LineTo} element for a
     *  vertical line.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGPathElement.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGVLineTo extends SVGPathElement
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGVLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinate is
         *      absolute, {@code false} if it is relative to the last point
         *      on the path.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGVLineTo( final boolean isAbsolute, final double y )
        {
            super( isAbsolute ? 'V' : 'v', formatDouble( y ) );
        }   //  SVGVLineTo()

        /**
         *  Creates a new {@code SVGVLineTo} instance.
         *
         *  @param  isAbsolute  {@code true} if the given coordinate is
         *      absolute, {@code false} if it is relative to the last point
         *      on the path.
         *  @param  y   The y coordinate for the end point of the line.
         */
        public SVGVLineTo( final boolean isAbsolute, final long y )
        {
            super( isAbsolute ? 'V' : 'v', Long.toString( y ) );
        }   //  SVGVLineTo()
    }
    //  class SVGVLineTo

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the path element.
     */
    private final char m_Name;

    /**
     *  The parameters for the transformation.
     */
    private final String m_Parameters;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGPathElement} instance.
     *
     *  @param  name    The name of the path element.
     *  @param  parameters  The parameters for the path element.
     */
    protected SVGPathElement( final char name, final String parameters )
    {
        m_Name = name;
        m_Parameters = requireNonNullArgument( parameters, "parameters" );
    }   //  SVGPathElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "AccessingNonPublicFieldOfAnotherObject" )
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && (obj instanceof SVGPathElement other) && (getClass() == other.getClass()) )
        {
            retValue = (m_Name == other.m_Name) && m_Parameters.equals( other.m_Parameters );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return hash( Character.valueOf( m_Name ), m_Parameters ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return format( "%c%s", m_Name, m_Parameters ); }

    /**
     *  Converts the given array of {@code SVGPathElement} instances into a
     *  String that can be used with the SVG
     *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_PathDefinition}
     *  attribute.
     *
     *  @param  elements    The path elements.
     *  @return The resulting String.
     */
    @SuppressWarnings( "AccessingNonPublicFieldOfAnotherObject" )
    public static final String toString( final SVGPathElement... elements )
    {
        final var buffer = new StringBuilder();
        var lastCommand = NULL_CHAR;

        for( final var element : requireNonNullArgument( elements, "elements" ) )
        {
            if( Character.toUpperCase( element.m_Name ) == 'Z' ) lastCommand = NULL_CHAR;
            if( !buffer.isEmpty() ) buffer.append( ' ' );
            if( lastCommand != element.m_Name )
            {
                lastCommand = element.m_Name;
                buffer.append( lastCommand );
            }
            buffer.append( element.m_Parameters );
        }

        final var retValue = buffer.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class SVGPathElement

/*
 *  End of File
 */