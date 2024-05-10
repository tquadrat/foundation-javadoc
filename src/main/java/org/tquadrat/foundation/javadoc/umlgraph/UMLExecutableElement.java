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

package org.tquadrat.foundation.javadoc.umlgraph;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  The wrapper for an
 *  {@link ExecutableElement}
 *  instance, enhanced by information needed for the creation of the UML graph.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UMLExecutableElement.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UMLExecutableElement.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public abstract class UMLExecutableElement extends UMLMemberElement implements ExecutableElement
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The wrapped executable element.
     */
    private final ExecutableElement m_Wrapped;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UMLExecutableElement} instance.
     *
     *  @param  element The wrapped executable element.
     */
    protected UMLExecutableElement( final ExecutableElement element )
    {
        super( element );
        m_Wrapped = element; // Null check is done by super constructor.
    }   //  UMLExecutableElement()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final AnnotationValue getDefaultValue() { return m_Wrapped.getDefaultValue(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends VariableElement> getParameters() { return m_Wrapped.getParameters(); }

    /**
     *  Returns the parameters list as a String, including the parentheses.
     *
     *  @return The parameters list.
     */
    public final String getParametersList() { return retrieveParameters(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final TypeMirror getReceiverType() { return m_Wrapped.getReceiverType(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final TypeMirror getReturnType() { return m_Wrapped.getReturnType(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends TypeMirror> getThrownTypes() { return m_Wrapped.getThrownTypes(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<? extends TypeParameterElement> getTypeParameters() { return m_Wrapped.getTypeParameters(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isDefault() { return m_Wrapped.isDefault(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isVarArgs() { return m_Wrapped.isVarArgs(); }

    /**
     *  Composes the parameters list for this executable element to a String.
     *
     *  @return The parameters list.
     */
    private final String retrieveParameters()
    {
        var retValue = "()";
        final var parameters = getParameters();
        if( !parameters.isEmpty() )
        {
            final var buffer = new StringBuilder( "(" )
                .append( getParameters().stream()
                    .map( v -> format( "%s:%s", v.getSimpleName(), v.asType().toString() ) )
                    .collect( joining( "," ) )
                );

            if( isVarArgs() )
            {
                final var length = buffer.length();
                if( (length > 2) && "[]".equals( buffer.substring( length - 2 ) ) )
                {
                    buffer.setLength( length - 2 );
                }
                //noinspection UnnecessaryUnicodeEscape
                buffer.append( "\u2026" );
            }

            retValue = buffer.append( ')' ).toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveParameters()
}
//  class UMLExecutableElement

/*
 *  End of File
 */