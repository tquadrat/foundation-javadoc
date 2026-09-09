/*
 * ============================================================================
 * Copyright © 2002-2026 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.WARNING;
import static jdk.javadoc.doclet.Taglet.Location.TYPE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.CHAR_ZWNBSP;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.JavadocError;
import org.tquadrat.foundation.javadoc.internal.OtherFileTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.lang.Lazy;
import org.tquadrat.foundation.javadoc.umlgraph.UMLDocument;
import org.tquadrat.foundation.javadoc.umlgraph.UMLGraphLayout;
import org.tquadrat.foundation.javadoc.umlgraph.UMLTypeElement;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.DocletEnvironment.ModuleMode;

/**
 *  <p>{@summary When this tag is added to the documentation of a class, a UML
 *  graph will be created for this class and added to the documentation.}</p>
 *  <p>The Javadoc generation will be serialised on this taglet.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UmlGraphLinkTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.1.0
 */
/*
 * Update the version in the method init!!
 */
@ClassVersion( sourceVersion = "$Id: UmlGraphLinkTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class UmlGraphLinkTaglet extends OtherFileTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "UMLGraph.link";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary The registry for the type elements; it is used as a cache
     *  for the already processed classes.}</p>
     */
    private static final Map<Name,UMLTypeElement> m_ClassRegistry;

    /**
     *  <p>{@summary The lock that controls the processing for this taglet.}</p>
     */
    private static final Lock m_Lock;

    static
    {
        m_ClassRegistry = new TreeMap<>( comparing( Name::toString ) );

        m_Lock = new ReentrantLock();
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code UmlGraphLinkTaglet} instance.}</p>
     */
    public UmlGraphLinkTaglet()
    {
        super( TAGLET_NAME, false, TYPE );
    }   //  UmlGraphLinkTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final void customInit()
    {
        final var typeUtils = getTypeUtils();
        final var env = getDocletEnvironment();

        //---* Fill the cache *------------------------------------------------
        try
        {
            m_Lock.lock();
            if( m_ClassRegistry.isEmpty() )
            {
                //---* Load all the included type elements *-------------------
                env.getIncludedElements().stream()
                    .filter( e -> e instanceof TypeElement )
                    .map( e -> (TypeElement) e )
                    .map( t -> new UMLTypeElement( t, env.isIncluded( t ), typeUtils ) )
                    .forEach( u -> m_ClassRegistry.put( u.getQualifiedName(), u ) );

                //---* Assign the children to the parents *--------------------
                /*
                 * We need a new collection because we will modify the cache
                 * during this process.
                 */
                List.copyOf( m_ClassRegistry.values() )
                    .forEach( this::initInheritance );
            }
        }
        finally
        {
            m_Lock.unlock();
        }
    }   //  customInit()

    /**
     *  <p>{@summary Determines the image file name based on the name of the
     *  given type element.} The returned value is not the fully qualified file
     *  name, just the last part of it.</p>
     *  <p>For the class {@code com.bar.MyClass}, this method would return the
     *  file name {@code doc-files/MyClass.svg}.</p>
     *
     *  @param  typeElement The type element.
     *  @return The name of the image file that belongs to the given type
     *      element.
     */
    private static final String determineImageFileName( final TypeElement typeElement )
    {
        final List<Name> names = new ArrayList<>();
        var element = typeElement;
        while( nonNull( element ) )
        {
            names.addFirst( element.getSimpleName() );
            final var nestingKind = element.getNestingKind();
            if( nestingKind.isNested() )
            {
                /*
                 * As long as the current element is nested, the enclosing
                 * element is a TypeElement.
                 */
                element = (TypeElement) element.getEnclosingElement();
            }
            else
            {
                //noinspection AssignmentToNull
                element = null;
            }
        }
        final var name = names.stream()
            .map( Object::toString )
            .collect( joining( "$" ) );
        final var retValue = format( "doc-files/%s.svg", name );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineImageFileName()

    /**
     *  <p>{@summary Checks whether the given type element has at least one
     *  parent.}</p>
     *  <p>Interfaces that do not extend another interface are parent-less;
     *  {@link Object}
     *  is the only class that does not have a parent class.</p>
     *
     *  @param  typeElement The type element to examine.
     *  @return {@code true} if the element has at least one parent,
     *      {@code false} otherwise.
     */
    private final boolean hasParent( final UMLTypeElement typeElement )
    {
        /*
         * The class java.lang.Object does not have a parent class.
         */
        var retValue = !Object.class.getName().equals( requireNonNullArgument( typeElement, "typeElement" ).getQualifiedName().toString() );

        /*
         * All other classes do extend java.lang.Object, so all other classes
         * do have a parent - either Object or a specific class.
         */
        if( retValue && typeElement.isInterface() )
        {
            final var typeUtils = getTypeUtils();
            final var extendedInterfaces = typeUtils.directSupertypes( typeElement.asType() );
            retValue = extendedInterfaces.size() > 1;

            /*
             * An interface with more than one direct supertype will have a
             * parent for sure.
             */
            if( !retValue )
            {
                /*
                 * If the only supertype of the interface is java.lang.Object,
                 * the interface does not have a parent, per definition.
                 */
                final var typeMirror = extendedInterfaces.getFirst();
                final var element = typeUtils.asElement( typeMirror );
                //noinspection TypeMayBeWeakened
                if( element instanceof TypeElement parentTypeElement )
                {
                    retValue = parentTypeElement.getKind() != ElementKind.CLASS;
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasParent()

    /**
     *  <p>{@summary Assigns the given type element to its parents.}</p>
     *  <p>This method modifies the cache for the type elements, therefore it
     *  requires the lock.</p>
     *
     *  @param  typeElement The type element.
     *
     *  @see #m_ClassRegistry
     *  @see #m_Lock
     *  @see #retrieveParents(UMLTypeElement)
     */
    private final void initInheritance( final UMLTypeElement typeElement ) { retrieveParents( typeElement ); }

    /**
     *  <p>{@summary Do the layout for the graph.}</p>
     *
     *  @param  document    The UML document.
     *  @param  focusClass  The class doc for the focus class.
     *  @param  details The level of details for the UMLGraph:<ul>
     *      <li>1: only elements should be shown that are relevant for the
     *      API</li>
     *      <li>2: all elements should be shown.</li></ul>
     */
    private final void layout( final UMLDocument document, final UMLTypeElement focusClass, final int details )
    {
        //---* Calculates the layout rows *------------------------------------
        final var layout = new UMLGraphLayout( document );
        final var noParentRow = layout.getNoParentRow();

        //---* Create the symbol for the focusClass *--------------------------
        final var focusClassSymbol = focusClass.createSymbol( layout, details, true );
        document.addSymbol( focusClassSymbol );

        //---* Add the focus class to a row *----------------------------------
        if( hasParent( focusClass ) )
        {
            layout.newRow().addSymbol( focusClassSymbol );
            layout.finishRow( true );
        }
        else
        {
            noParentRow.addSymbol( focusClassSymbol );
        }

        //---* Add the row for the direct children *---------------------------
        var row = layout.newRow();
        for( final var child : focusClass.getChildTypes() )
        {
            var symbol = child.createSymbol( layout, details, false );
            if( document.addSymbol( symbol ) )
            {
                row.addSymbol( symbol );
            }
            else
            {
                //noinspection OptionalGetWithoutIsPresent
                symbol = document.getSymbol( child.getQualifiedName() ).get();
            }
            layout.createConnector( focusClassSymbol, symbol );
        }
        layout.finishRow( true );

        //---* Create the row for direct parents of the focus class *----------
        var optionalRow = Lazy.use( layout::newRow );
        try
        {
            m_Lock.lock();
            for( final var parent : retrieveParents( focusClass ) )
            {
                var symbol = parent.createSymbol( layout, details, false );
                row = hasParent( parent ) ? optionalRow.get() : noParentRow;
                if( document.addSymbol( symbol ) )
                {
                    row.addSymbol( symbol );
                }
                else
                {
                    //noinspection OptionalGetWithoutIsPresent
                    symbol = document.getSymbol( parent.getQualifiedName() ).get();
                }
                layout.createConnector( symbol, focusClassSymbol );
            }
            while( optionalRow.isPresent() )
            {
                //---* Add the row to the layout *-----------------------------
                layout.finishRow( false );

                //---* Get the current row *-----------------------------------
                row = optionalRow.get();
                optionalRow = Lazy.use( layout::newRow );

                //---* Scan the classes for their parents *--------------------
                final var rowContents = row.getContents();
                for( final var currentSymbol : rowContents )
                {
                    final var currentClass = currentSymbol.getTypeElement();
                    for( final var parent : retrieveParents( currentClass ) )
                    {
                        var parentSymbol = parent.createSymbol( layout, details, false );
                        row = hasParent( parent ) ? optionalRow.get() : noParentRow;
                        if( document.addSymbol( parentSymbol ) )
                        {
                            row.addSymbol( parentSymbol );
                        }
                        else
                        {
                            //noinspection OptionalGetWithoutIsPresent
                            parentSymbol = document.getSymbol( parent.getQualifiedName() ).get();
                        }
                        layout.createConnector( parentSymbol, currentSymbol );
                    }
                }
            }
        }
        finally
        {
            m_Lock.unlock();
        }

        //---* Distribute the elements in the layout *-------------------------
        layout.distribute();
    }   //  layout()

    /**
     *  <p>{@summary Retrieves the direct parents for the given type.}</p>
     *  <p>As this method potentially modifies the cache, it requires the
     *  lock.</p>
     *
     *  @param  typeElement The type element.
     *  @return The direct parents for the given type element.
     *
     *  @see #m_ClassRegistry
     *  @see #m_Lock
     */
    private final Collection<UMLTypeElement> retrieveParents( final UMLTypeElement typeElement )
    {
        final var typeUtils = getTypeUtils();
        final var env = getDocletEnvironment();

        final Collection<UMLTypeElement> retValue = new ArrayList<>();

        try
        {
            m_Lock.lock();

            for( final var typeMirror : typeUtils.directSupertypes( typeElement.asType() ) )
            {
                final var element = typeUtils.asElement( typeMirror );
                if( element instanceof TypeElement parentTypeElement )
                {
                    if( (typeElement.getKind() != ElementKind.INTERFACE) || (parentTypeElement.getKind() != ElementKind.CLASS) )
                    {
                        var parentUMLTypeElement = m_ClassRegistry.get( parentTypeElement.getQualifiedName() );
                        if( isNull( parentUMLTypeElement ) )
                        {
                            parentUMLTypeElement = new UMLTypeElement( parentTypeElement, env.isIncluded( parentTypeElement ), env.getTypeUtils() );
                            m_ClassRegistry.put( parentUMLTypeElement.getQualifiedName(), parentUMLTypeElement );
                            parentUMLTypeElement.addChildType( typeElement );
                            retrieveParents( parentUMLTypeElement );
                        }
                        else
                        {
                            parentUMLTypeElement.addChildType( typeElement );
                        }

                        retValue.add( parentUMLTypeElement );
                    }
                }
            }
        }
        finally
        {
            m_Lock.unlock();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveParent()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        var retValue = EMPTY_STRING;
        if( element instanceof TypeElement typeElement )
        {
            final var qualifiedName = typeElement.getQualifiedName().toString();

            //---* Get the target file object *--------------------------------
            final var imageFileName = determineImageFileName( typeElement );
            final FileObject imageFile;
            try
            {
                imageFile = getOutputFileObject( typeElement, imageFileName );
            }
            catch( final IOException e )
            {
                throw new JavadocError( format( "Problems on obtaining the output file for the UML Graph for %s", qualifiedName ), e );
            }

            //---* Create the return value *-----------------------------------
            /*
             * Basically, this is the image tag for the SVG file that we will
             * create below.
             */
            final var caption1 = "UML Diagram";
            final var caption2 = format( "%2$s for \"%1$s\"", qualifiedName, caption1 );

            final var dtElement = createXMLElement( "dt" );
            var spanElement = createXMLElement( "span", dtElement, caption1 )
                .setAttribute( "class", "simpleTagLabel" );

            final var ddElement = createXMLElement( "dd" );
            final var aElement = createXMLElement( "a", ddElement )
                .setAttribute( "class", "module-graph" )
                .setAttribute( "href", imageFileName );
            //noinspection MagicNumber
            createXMLElement( "img", aElement, Character.toString( CHAR_ZWNBSP ) )
                .setAttribute( "style", "vertical-align:top" )
                .setAttribute( "alt", caption2 )
                .setAttribute( "src", imageFileName )
                .setAttribute( "width", 250 );
            spanElement = createXMLElement( "span", aElement )
                .setAttribute( "style", "background:white; border: solid black 2px;" );
            createXMLElement( "h2", spanElement, caption2 );
            createXMLElement( "img", spanElement, Character.toString( CHAR_ZWNBSP ) )
                .setAttribute( "style", "border: solid black 1px; vertical-align:top" )
                .setAttribute( "alt", caption2 )
                .setAttribute( "src", imageFileName );

            retValue = dtElement.toString().concat( ddElement.toString() );

            //---* Create the UML document *-----------------------------------
            final var umlDocument = new UMLDocument();

            try
            {
                m_Lock.lock();
                final var details = getDocletEnvironment().getModuleMode() == ModuleMode.API ? 1 : 2;

                //---* Get the UML type element *------------------------------
                var umlTypeElement = m_ClassRegistry.get( typeElement.getQualifiedName() );
                if( isNull( umlTypeElement ) )
                {
                    umlTypeElement = new UMLTypeElement( typeElement, getDocletEnvironment().isIncluded( typeElement ), getDocletEnvironment().getTypeUtils() );
                    m_ClassRegistry.put( typeElement.getQualifiedName(), umlTypeElement );
                    initInheritance( umlTypeElement );
                }

                //---* Layout the image *--------------------------------------
                layout( umlDocument, umlTypeElement, details );
            }
            finally
            {
                m_Lock.unlock();
            }

            //---* Write the new picture *-------------------------------------
            print( NOTE, "Generating %s".formatted( imageFile.getName() ) );
            try( final var outputStream = imageFile.openOutputStream() )
            {
                outputStream.write( umlDocument.toString().getBytes( UTF_8 ) );
            }
            catch( final IOException e )
            {
                final var message = "Problems on writing the UMLGraph for %s to %s";
                print( WARNING, element, message.formatted( typeElement.getSimpleName(), imageFile.getName() ) );
                throw new JavadocError( message.formatted( typeElement.getSimpleName(), imageFile.getName() ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class UmlGraphLinkTaglet

/*
 *  End of File
 */