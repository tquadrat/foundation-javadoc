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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.type.TypeKind.NONE;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.umlgraph.Stereotype.ANNOTATION;
import static org.tquadrat.foundation.javadoc.umlgraph.Stereotype.ENUM;
import static org.tquadrat.foundation.javadoc.umlgraph.Stereotype.ERROR;
import static org.tquadrat.foundation.javadoc.umlgraph.Stereotype.EXCEPTION;
import static org.tquadrat.foundation.javadoc.umlgraph.Stereotype.TYPE;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.ToolKit;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The wrapper for a
 *  {@link TypeElement}
 *  instance, enhanced by information needed for the creation of the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLTypeElement.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLTypeElement.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class UMLTypeElement extends UMLElement implements TypeElement
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code UMLTypeElement} objects.
     */
    public static final UMLTypeElement [] EMPTY_UMLTypeElement_ARRAY = new UMLTypeElement [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The types that extend this class or implements this interface.
     */
    private final Set<UMLTypeElement> m_ChildTypes = new HashSet<>();

    /**
     *  Flag that indicates whether this type element is included somehow in
     *  the output of this run of JavaDoc.
     */
    private final boolean m_IsIncludedInOutput;

    /**
     *  The type utilities that are used by this type element to configure the
     *  output.
     */
    private final Types m_TypeUtils;

    /**
     *  The wrapped type element.
     */
    private final TypeElement m_Wrapped;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLTypeElement} instance.
     *
     *  @param  element The wrapped type element.
     *  @param  isIncluded  {@code true} if this type element is included
     *      somehow in the output of this run of JavaDoc, {@code false}
     *      otherwise.
     *  @param  typeUtils   The type utilities.
     */
    public UMLTypeElement( final TypeElement element, final boolean isIncluded, final Types typeUtils )
    {
        super( element );
        m_Wrapped = element; // Null check is done by super constructor.
        m_IsIncludedInOutput = isIncluded;
        m_TypeUtils = requireNonNullArgument( typeUtils, "typeUtils" );
    }   //  UMLTypeElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a type that extends or implements this one.
     *
     *  @param  child   The
     *      {@link UMLTypeElement}
     *      instance for the child type.
     */
    public final void addChildType( final UMLTypeElement child ) { m_ChildTypes.add( requireNonNullArgument( child, "child" ) ); }

    /**
     *  Creates the SVG symbol that represents this type element for the UML
     *  diagram.
     *
     *  @param  layout    The instance of
     *      {@link UMLGraphLayout}
     *      that is used to render the document that finally contains the
     *      {@link org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGSymbol}
     *      instance that is created by this method.
     *  @param  details The level of detail to show:<ul>
     *      <li>0: no details, only class name and package name; for an enum
     *      also the enum constants</li>
     *      <li>1: only elements should be shown that are relevant for the
     *      API</li>
     *      <li>2: all elements should be shown.</li></ul>
     *  @param  isFocusClass    {@code true} if this class is the focus
     *      class of the graph.
     *  @return The SVG symbol instance that represents the UML diagram for
     *      this class.
     */
    public final TypeSymbol createSymbol( final UMLGraphLayout layout, final int details, final boolean isFocusClass )
    {
        //---* Analyse the arguments *-----------------------------------------
        final var apiOnly = (details <= 1) || !m_IsIncludedInOutput;

        //---* Render the class *----------------------------------------------
        final var retValue = new TypeSymbol( layout.getDocument(), this, isFocusClass, apiOnly, m_IsIncludedInOutput );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createSymbol()

    /**
     *  Determines the stereotypes for this UML class doc. It will be inferred
     *  from the type of the class. The stereotype
     *  {@linkplain Stereotype#INTERFACE «Interface»}
     *  is used for an interface,
     *  {@linkplain Stereotype#ENUM «Enum»}
     *  for an {@code enum} type, and
     *  {@linkplain Stereotype#ANNOTATION «Annotation»}
     *  for an annotation.<br>
     *  <br>A <i>regular</i> class that is derived from
     *  {@link Error}
     *  will have the stereotype
     *  {@linkplain Stereotype#ERROR «Error»},
     *  or
     *  {@linkplain Stereotype#EXCEPTION «Exception»}
     *  if derived from
     *  {@link Exception}.<br>
     *  <br>An abstract class will have the stereotype
     *  {@linkplain Stereotype#TYPE «Type»},
     *  a class with only static methods (or no methods at all) the stereotype
     *  {@linkplain Stereotype#UTILITY «Utility»}.<br>
     *  <br>If none of the conditions above will fit, we have a simple class
     *  without a particular stereotype.
     *
     *  @return The list of stereotypes defined for this UML type element; it
     *      may be empty, but will never be {@code null}.
     */
    final String [] determineStereotypes()
    {
        //---* Determine the stereotype *----------------------------------
        final Set<Stereotype> stereotype = EnumSet.noneOf( Stereotype.class );
        KindSwitch:
        switch( m_Wrapped.getKind() )
        {
            case ANNOTATION_TYPE -> stereotype.add( ANNOTATION );
            case ENUM -> stereotype.add( ENUM );
            case INTERFACE -> stereotype.add( Stereotype.INTERFACE );
            //$CASES-OMITTED$
            default ->
            {
                stereotype.add( Stereotype.CLASS );
                if( getModifiers().contains( ABSTRACT ) )
                {
                    stereotype.add( TYPE );
                }
                else
                {
                    var superclass = getSuperclass();
                    Element element;
                    while( !(isNull( superclass ) || (superclass.getKind() == NONE)) )
                    {
                        if( "java.lang.Exception".equals( superclass.toString() ) )
                        {
                            stereotype.add( EXCEPTION );
                            //noinspection AssignmentToNull
                            superclass = null;
                            continue;
                        }

                        if( "java.lang.Error".equals( superclass.toString() ) )
                        {
                            stereotype.add( ERROR );
                            //noinspection AssignmentToNull
                            superclass = null;
                            continue;
                        }

                        element = m_TypeUtils.asElement( superclass );
                        if( nonNull( element ) )
                        {
                            superclass = ((TypeElement) element).getSuperclass();
                        }
                        else
                        {
                            //noinspection AssignmentToNull
                            superclass = null;
                        }
                    }
                }
            }
        }   //  KindSwitch:

        final var retValue = stereotype.stream()
            .map( Stereotype::toString )
            .filter( ToolKit::isNotEmpty )
            .toArray( String []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineStereotypes()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "TypeMayBeWeakened" )
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && (obj instanceof UMLTypeElement other ) )
        {
            retValue = getQualifiedName().equals( other.getQualifiedName() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  Returns a list with the attributes for this type. Enum constants will
     *  be ignored.
     *
     *  @param  apiOnly Flag that indicates that only attributes that are
     *      relevant for the API will be returned.
     *  @return The attributes; the list can be empty if the type is an
     *      interface or there is no relevant field.
     */
    final List<UMLVariableElement> getAttributes( final boolean apiOnly )
    {
        final var retValue = getEnclosedElements().stream()
            .filter( UMLElement::isAttribute )
            .filter( e -> !apiOnly || isAPI( e ) )
            .map( e -> (VariableElement) e )
            .map( UMLVariableElement::new )
            .collect( toList() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getAttributes()

    /**
     *  Returns the children of this type. These are the types that implements
     *  or extends it.
     *
     *  @return The children.
     *
     *  @see #addChildType(UMLTypeElement)
     */
    public final UMLTypeElement [] getChildTypes() { return m_ChildTypes.toArray( EMPTY_UMLTypeElement_ARRAY ); }

    /**
     *  Returns a list with the constructors for this type.
     *
     *  @param  apiOnly Flag that indicates that only constructors that are
     *      relevant for the API will be returned.
     *  @return The constructors; the list can be empty if the type is an
     *      interface or there is no relevant constructor.
     */
    public final List<UMLConstructorElement> getConstructors( final boolean apiOnly )
    {
        final var retValue = getEnclosedElements().stream()
            .filter( UMLElement::isConstructor )
            .filter( e -> !apiOnly || isAPI( e ) )
            .map( e -> (ExecutableElement) e )
            .map( UMLConstructorElement::new )
            .collect( toList() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getConstructors()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends TypeMirror> getInterfaces() { return m_Wrapped.getInterfaces(); }

    /**
     *  Returns a list with the methods for this type.
     *
     *  @param  apiOnly Flag that indicates that only methods that are
     *      relevant for the API will be returned.
     *  @return The methods; the list can be empty if there is no relevant
     *      method.
     */
    public final List<UMLMethodElement> getMethods( final boolean apiOnly )
    {
        final var retValue = getEnclosedElements().stream()
            .filter( UMLElement::isMethod )
            .filter( e -> !apiOnly || isAPI( e ) )
            .map( e -> (ExecutableElement) e )
            .map( UMLMethodElement::new )
            .collect( toList() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getMethods()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final NestingKind getNestingKind() { return m_Wrapped.getNestingKind(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Name getQualifiedName() { return m_Wrapped.getQualifiedName(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final TypeMirror getSuperclass() { return m_Wrapped.getSuperclass(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends TypeParameterElement> getTypeParameters() { return m_Wrapped.getTypeParameters(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return getQualifiedName().toString().hashCode(); }

    /**
     *  Returns {@code true} if this type is abstract, either an abstract class
     *  or an interface.
     *
     *  @return {@code true} if this type is abstract.
     */
    public final boolean isAbstract() { return isInterface() || getModifiers().contains( ABSTRACT ); }

    /**
     *  Returns {@code true} if this type element represents a class. Different
     *  from
     *  {@link javax.lang.model.element.ElementKind#isClass()},
     *  this method will return {@code false} if this type element is an
     *  {@code enum}.
     *
     *  @return {@code true} if this type element represents a class.
     */
    public final boolean isClass() { return getKind() == CLASS; }

    /**
     *  Returns {@code true} if this type represents an interface. Different
     *  from
     *  {@link javax.lang.model.element.ElementKind#isInterface()},
     *  this method will return {@code false} if this type element is an
     *  {@code annotation}.
     *
     *  @return {@code true} if this type represents an interface.
     */
    public final boolean isInterface() { return getKind() == INTERFACE; }
}
//  class UMLTypeElement

/*
 *  End of File
 */