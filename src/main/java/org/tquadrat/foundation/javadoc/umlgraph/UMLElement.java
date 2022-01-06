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

package org.tquadrat.foundation.javadoc.umlgraph;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The wrapper for an
 *  {@link Element}
 *  instance, enhanced by information needed for the creation of the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLElement.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLElement.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public abstract class UMLElement implements Element
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The wrapped element.
     */
    private final Element m_Wrapped;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLElement} instance.
     *
     *  @param  element The wrapped element.
     */
    protected UMLElement( final Element element )
    {
        m_Wrapped = requireNonNullArgument( element, "element" );
    }   //  UMLElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final <R,P> R accept( final ElementVisitor<R,P> visitor, final P payload )
    {
        return m_Wrapped.accept( visitor, payload );
    }   //  accept()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final TypeMirror asType() { return m_Wrapped.asType(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <A extends Annotation> A getAnnotation( final Class<A> annotationType )
    {
        return m_Wrapped.getAnnotation( annotationType );
    }   //  getAnnotation()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <A extends Annotation> A [] getAnnotationsByType( final Class<A> annotationType )
    {
        return m_Wrapped.getAnnotationsByType( annotationType );
    }   //  getAnnotationsByType()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends AnnotationMirror> getAnnotationMirrors() { return m_Wrapped.getAnnotationMirrors(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends Element> getEnclosedElements() { return m_Wrapped.getEnclosedElements(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public Element getEnclosingElement() { return m_Wrapped.getEnclosingElement(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final ElementKind getKind() { return m_Wrapped.getKind(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Modifier> getModifiers() { return m_Wrapped.getModifiers(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Name getSimpleName() { return m_Wrapped.getSimpleName(); }

    /**
     *  Tests whether the given element is part of an API or not. The
     *  consideration is, that only element that are either {@code public} or
     *  {@code protected} are part of the API of a class.
     *
     *  @param  element The element to test.
     *  @return {@code true} if the element is part of the API, {@code false}
     *      otherwise.
     */
    public static final boolean isAPI( final Element element )
    {
        final var modifiers = requireNonNullArgument( element, "element" ).getModifiers();
        final var retValue = modifiers.contains( PUBLIC ) || modifiers.contains( PROTECTED );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAPI()

    /**
     *  Tests whether the given element represents a field.
     *
     *  @param  element The element to test.
     *  @return {@code true} if the element represents a field (but not an
     *      enum value constant), {@code false} otherwise.
     */
    public static final boolean isAttribute( final Element element ) { return requireNonNullArgument( element, "element" ).getKind() == FIELD; }

    /**
     *  Tests whether the given element represents a constructor.
     *
     *  @param  element The element to test.
     *  @return {@code true} if the element represents a constructor,
     *      {@code false} otherwise.
     */
    public static final boolean isConstructor( final Element element ) { return requireNonNullArgument( element, "element" ).getKind() == CONSTRUCTOR; }

    /**
     *  Tests whether the given element represents a method.
     *
     *  @param  element The element to test.
     *  @return {@code true} if the element represents a method,
     *      {@code false} otherwise.
     */
    public static final boolean isMethod( final Element element ) { return requireNonNullArgument( element, "element" ).getKind() == METHOD; }
}
//  class UMLElement

/*
 *  End of File
 */