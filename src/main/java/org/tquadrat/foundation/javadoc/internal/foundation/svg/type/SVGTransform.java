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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The abstract base class for the SVG transformations that can be applied to
 *  some elements with the attribute
 *  {@value org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils#SVGATTRIBUTE_Transform}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "AbstractClassWithoutAbstractMethods" )
@ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public abstract sealed class SVGTransform extends ValueBase
    permits SVGTransform.SVGMatrix, SVGTransform.SVGRotate,
        SVGTransform.SVGScale, SVGTransform.SVGSkewX, SVGTransform.SVGSkewY,
        SVGTransform.SVGTranslate
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The SVG {@code matrix} transformation. <br>
     *  <br>This transform definition specifies a transformation in the form of
     *  a transformation matrix of six values. {@code matrix(a,b,c,d,e,f)} is
     *  equivalent to applying the transformation matrix
     *  <pre><code>    <i>a</i> <i>c</i> <i>e</i>
     *  ( <i>b</i> <i>d</i> <i>f</i> )
     *    0 0 1</code></pre>which maps coordinates from a previous coordinate
     *  system into a new coordinate system by the following matrix equalities:
     *  <pre><code>      <i>x<sub>newCoordSys</sub></i>       <i>a</i> <i>c</i> <i>e</i>     <i>x<sub>prevCoordSys</sub></i>       <i>ax<sub>prevCoordSys</sub></i> + <i>cy<sub>prevCoordSys</sub></i> + <i>e</i>
     *    ( <i>y<sub>newCoordSys</sub></i> ) = ( <i>b</i> <i>d</i> <i>f</i> ) ( <i>y<sub>prevCoordSys</sub></i> ) = ( <i>bx<sub>prevCoordSys</sub></i> + <i>dy<sub>prevCoordSys</sub></i> + <i>f</i> )
     *           1           0 0 1           1                           1</code></pre>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGMatrix extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGMatrix} instance.
         *
         *  @param  a   Parameter {@code a}.
         *  @param  b   Parameter {@code b}.
         *  @param  c   Parameter {@code c}.
         *  @param  d   Parameter {@code d}.
         *  @param  e   Parameter {@code e}.
         *  @param  f   Parameter {@code f}.
         */
        public SVGMatrix( final long a, final long b, final long c, final long d, final long e, final long f)
        {
            super( SVGTRANSFORM_Matrix, longToString( ' ', a, b, c, d, e, f ) );
        }   //  SVGMatrix()

        /**
         *  Creates a new {@code SVGMatrix} instance.
         *
         *  @param  a   Parameter {@code a}.
         *  @param  b   Parameter {@code b}.
         *  @param  c   Parameter {@code c}.
         *  @param  d   Parameter {@code d}.
         *  @param  e   Parameter {@code e}.
         *  @param  f   Parameter {@code f}.
         */
        public SVGMatrix( final double a, final double b, final double c, final double d, final double e, final double f)
        {
            super( SVGTRANSFORM_Matrix, doubleToString( ' ', a, b, c, d, e, f ) );
        }   //  SVGMatrix()
    }
    //  class SVGMatrix

    /**
     *  The SVG {@code rotate} transformation. <br>
     *  <br>This transformation definition specifies a rotation by {@code a}
     *  degrees about a given point. If the optional parameters {@code x} and
     *  {@code y} are not supplied, the rotation is about the origin of the
     *  current user coordinate system. The operation corresponds to the matrix
     *  <pre><code>    cos <i>a</i>  -sin <i>a</i>  0
     *  ( sin <i>a</i>   cos <i>a</i>  0 )
     *      0       0    1</code></pre>If the optional parameters {@code x} and
     *  {@code y} are supplied, the rotation is about the point {@code (x,y)}.
     *  Then the operation represents the equivalent of the following transform
     *  definition list:<ol>
     *  <li><code>translate( <i>x</i>, <i>y</i> )</code></li>
     *  <li><code>rotate( <i>a</i> )</code></li>
     *  <li><code>translate( -<i>x</i>, -<i>y</i> )</code></li>
     *  </ol>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGRotate extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGRotate} instance.
         *
         *  @param  a   The rotation angle.
         *  @param  x   The x coordinate of the rotation point.
         *  @param  y   The y coordinate of the rotation point.
         */
        public SVGRotate( final long a, final long x, final long y )
        {
            super( SVGTRANSFORM_Rotate, longToString( ' ', a, x, y ) );
        }   //  SVGRotate()

        /**
         *  Creates a new {@code SVGRotate} instance.
         *
         *  @param  a   The rotation angle.
         *  @param  x   The x coordinate of the rotation point.
         *  @param  y   The y coordinate of the rotation point.
         */
        public SVGRotate( final double a, final double x, final double y )
        {
            super( SVGTRANSFORM_Rotate, doubleToString( ' ', a, x, y ) );
        }   //  SVGRotate()

        /**
         *  Creates a new {@code SVGRotate} instance.
         *
         *  @param  a   The rotation angle.
         */
        public SVGRotate( final long a )
        {
            super( SVGTRANSFORM_Rotate, Long.toString( a ) );
        }   //  SVGRotate()

        /**
         *  Creates a new {@code SVGRotate} instance.
         *
         *  @param  a   The rotation angle.
         */
        public SVGRotate( final double a )
        {
            super( SVGTRANSFORM_Rotate, formatDouble( a ) );
        }   //  SVGRotate()
    }
    //  class SVGRotate

    /**
     *  The SVG {@code scale} transformation. <br>
     *  <br>This transform definition specifies a scale operation by {@code x}
     *  and {@code y}. This is the equivalent to
     *  <pre><code>matrix(<i>x</i> 0 0 <i>y</i> 0 0)</code></pre>. If {@code y}
     *  is not provided, it is assumed to be equal to {@code x}.
     *
     *  @see SVGMatrix
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGScale extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGScale} instance.
         *
         *  @param  x   Parameter {@code x}.
         *  @param  y   Parameter {@code y}.
         */
        public SVGScale( final long x, final long y )
        {
            super( SVGTransform.SVGTRANSFORM_Scale, longToString( ' ', x, y ) );
        }   //  SVGScale()

        /**
         *  Creates a new {@code SVGScale} instance.
         *
         *  @param  x   Parameter {@code x}.
         *  @param  y   Parameter {@code y}.
         */
        public SVGScale( final double x, final double y )
        {
            super( SVGTransform.SVGTRANSFORM_Scale, doubleToString( ' ', x, y ) );
        }   //  SVGScale()

        /**
         *  Creates a new {@code SVGScale} instance. This is equivalent to
         *  {@link #SVGScale(long, long) SVGScale( x, x )}.
         *
         *  @param  x   Parameter {@code x}.
         */
        public SVGScale( final long x )
        {
            super( SVGTransform.SVGTRANSFORM_Scale, Long.toString( x ) );
        }   //  SVGScale()

        /**
         *  Creates a new {@code SVGScale} instance. This is equivalent to
         *  {@link #SVGScale(double, double) SVGScale( x, x )}.
         *
         *  @param  x   Parameter {@code x}.
         */
        public SVGScale( final double x )
        {
            super( SVGTransform.SVGTRANSFORM_Scale, formatDouble( x ) );
        }   //  SVGScale()
    }
    //  class SVGScale

    /**
     *  The SVG {@code skewX} transformation. <br>
     *  <br>This transformation definition specifies a skew transformation
     *  along the x-axis by {@code a} degrees. The operation corresponds to the
     *  matrix<pre><code>    1  tan <i>a</i>  0
     *  ( 0    1    0 )
     *    0    0    1</code></pre>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGSkewX extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGSkewX} instance.
         *
         *  @param  a   The angle.
         */
        public SVGSkewX( final long a ) { super( SVGTRANSFORM_SkewX, Long.toString( a ) ); }

        /**
         *  Creates a new {@code SVGSkewX} instance.
         *
         *  @param  a   The angle.
         */
        public SVGSkewX( final double a ) { super( SVGTRANSFORM_SkewX, formatDouble( a ) ); }
    }
    //  class SVGSkewX

    /**
     *  The SVG {@code skewY} transformation. <br>
     *  <br>This transformation definition specifies a skew transformation
     *  along the y-axis by {@code a} degrees. The operation corresponds to the
     *  matrix<pre><code>      1    0  0
     *  ( tan <i>a</i>  1  0 )
     *      0    0  1</code></pre>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGSkewY extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGSkewY} instance.
         *
         *  @param  a   The angle.
         */
        public SVGSkewY( final long a ) { super( SVGTRANSFORM_SkewY, Long.toString( a ) ); }

        /**
         *  Creates a new {@code SVGSkewY} instance.
         *
         *  @param  a   The angle.
         */
        public SVGSkewY( final double a ) { super( SVGTRANSFORM_SkewY, formatDouble( a ) ); }
    }
    //  class SVGSkewY

    /**
     *  The SVG {@code translate} transformation. <br>
     *  <br>This transform definition specifies a translation by {@code x} and
     *  {@code y}. This is equivalent to
     *  <pre><code>matrix(1 0 0 1 <i>x</i> <i>y</i>)</code></pre>. If {@code y}
     *  is not provided, it is assumed to be zero.
     *
     *  @see SVGMatrix
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $
     *  @since 0.0.5
     */
    @ClassVersion( sourceVersion = "$Id: SVGTransform.java 977 2022-01-06 11:41:03Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static final class SVGTranslate extends SVGTransform
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SVGTranslate} instance.
         *
         *  @param  x   Parameter {@code x}.
         *  @param  y   Parameter {@code y}.
         */
        public SVGTranslate( final long x, final long y ) { super( SVGTRANSFORM_Translate, longToString( ' ', x, y ) ); }

        /**
         *  Creates a new {@code SVGTranslate} instance.
         *
         *  @param  x   Parameter {@code x}.
         *  @param  y   Parameter {@code y}.
         */
        public SVGTranslate( final double x, final double y ) { super( SVGTRANSFORM_Translate, doubleToString( ' ', x, y ) ); }

        /**
         *  Creates a new {@code SVGTranslate} instance.
         *
         *  @param  x   Parameter {@code x}.
         */
        public SVGTranslate( final long x ) { super( SVGTRANSFORM_Translate, Long.toString( x ) ); }

        /**
         *  Creates a new {@code SVGTranslate} instance.
         *
         *  @param  x   Parameter {@code x}.
         */
        public SVGTranslate( final double x ) { super( SVGTRANSFORM_Translate, formatDouble( x ) ); }
    }
    //  class SVGTranslate

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name for the {@code matrix} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_Matrix = "matrix";

    /**
     *  The name for the {@code rotate} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_Rotate = "rotate";

    /**
     *  The name for the {@code scale} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_Scale = "scale";

    /**
     *  The name for the {@code skewX} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_SkewX = "skewX";

    /**
     *  The name for the {@code skewY} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_SkewY = "skewY";

    /**
     *  The name for the {@code translate} transformation: {@value}.
     */
    public static final String SVGTRANSFORM_Translate = "translate";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the transformation.
     */
    private final String m_Name;

    /**
     *  The parameters for the transformation.
     */
    private final String m_Parameters;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SVGTransform} instance.
     *
     *  @param  name    The name of the transformation.
     *  @param  parameters  The parameters for the transformation.
     */
    protected SVGTransform( final String name, final String parameters )
    {
        m_Name = requireNotEmptyArgument( name, "name" );
        m_Parameters = requireNotEmptyArgument( parameters, "parameters" );
    }   //  SVGTransform()

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
        if( !retValue && (obj instanceof SVGTransform other) && (getClass() == other.getClass()) )
        {
            retValue = m_Name.equals( other.m_Name ) && m_Parameters.equals( other.m_Parameters );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return hash( m_Name, m_Parameters ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return format( "%s(%s)", m_Name, m_Parameters ); }
}
//  class SVGTransform

/*
 *  End of File
 */