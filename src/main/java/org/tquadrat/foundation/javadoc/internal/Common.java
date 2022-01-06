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

package org.tquadrat.foundation.javadoc.internal;

import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.round;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmpty;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.DocumentationTool;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.AnchorTaglet;
import org.tquadrat.foundation.javadoc.HRefTaglet;
import org.tquadrat.foundation.javadoc.UnderlineTaglet;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.ValidationException;
import com.sun.source.doctree.DocTree;
import com.sun.source.util.DocTreeFactory;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

/**
 *  Helper methods for the new taglet implementations.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Common.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( "ClassWithTooManyFields" )
@ClassVersion( sourceVersion = "$Id: Common.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5")
@UtilityClass
public final class Common
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The taglet collection.
     */
    private static List<Taglet> m_InlineTagletCollection;

    /**
     *  The guard for the taglet collection.
     */
    private static final Object m_InlineTagletCollectionGuard = new Object();

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  Location to search for modules containing annotation processors.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location ANNOTATION_PROCESSOR_MODULE_PATH;

    /**
     *  Location to search for annotation processors.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location ANNOTATION_PROCESSOR_PATH;

    /**
     *  Location of new class files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location CLASS_OUTPUT;

    /**
     *  Location to search for user class files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location CLASS_PATH;

    /**
     * Location to search for doclets.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location DOCLET_PATH;

    /**
     * Location of new documentation files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location DOCUMENTATION_OUTPUT;

    /**
     *  Location to search for precompiled user modules.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location MODULE_PATH;

    /**
     *  Location to search for the source code of modules.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location MODULE_SOURCE_PATH;

    /**
     *  Location of new native header files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location NATIVE_HEADER_OUTPUT;

    /**
     *  Location to search for module patches.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location PATCH_MODULE_PATH;

    /**
     *  Location to search for platform classes. Sometimes called the boot
     *  class path.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location PLATFORM_CLASS_PATH;

    /**
     *  Location of new source files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SOURCE_OUTPUT;

    /**
     *  Location to search for existing source files.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SOURCE_PATH;

    /**
     *  Location to search for system modules.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SYSTEM_MODULES;

    /**
     *  The lead-in sequence for a tag.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final String TAG_LEADIN;

    /**
     * Location to search for taglets.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location TAGLET_PATH;

    /**
     *  Location to search for upgradeable system modules.
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location UPGRADE_MODULE_PATH;

    static
    {
        TAG_LEADIN = "{@";

        //---* Initialise the locations *--------------------------------------
        ANNOTATION_PROCESSOR_MODULE_PATH = StandardLocation.ANNOTATION_PROCESSOR_MODULE_PATH;
        ANNOTATION_PROCESSOR_PATH = StandardLocation.ANNOTATION_PROCESSOR_PATH;
        CLASS_OUTPUT = StandardLocation.CLASS_OUTPUT;
        CLASS_PATH = StandardLocation.CLASS_PATH;
        DOCLET_PATH = DocumentationTool.Location.DOCLET_PATH;
        DOCUMENTATION_OUTPUT = DocumentationTool.Location.DOCUMENTATION_OUTPUT;
        MODULE_PATH = StandardLocation.MODULE_PATH;
        MODULE_SOURCE_PATH = StandardLocation.MODULE_SOURCE_PATH;
        NATIVE_HEADER_OUTPUT = StandardLocation.NATIVE_HEADER_OUTPUT;
        PATCH_MODULE_PATH = StandardLocation.PLATFORM_CLASS_PATH;
        PLATFORM_CLASS_PATH = StandardLocation.PLATFORM_CLASS_PATH;
        SOURCE_OUTPUT = StandardLocation.SOURCE_OUTPUT;
        SOURCE_PATH = StandardLocation.SOURCE_PATH;
        SYSTEM_MODULES = StandardLocation.SYSTEM_MODULES;
        TAGLET_PATH = DocumentationTool.Location.TAGLET_PATH;
        UPGRADE_MODULE_PATH = StandardLocation.UPGRADE_MODULE_PATH;
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private Common() { throw new PrivateConstructorForStaticClassCalledError( Common.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates the format String for a line number, based on the highest
     *  possible value.
     *
     *  @param  maxValue    The highest possible value.
     *  @return The format String.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String createLineNumberFormatString( final int maxValue )
    {
        if( maxValue < 0 ) throw new ValidationException( format( "Invalid value: %d", maxValue ) );
        final var retValue = format( "<span class=\"source-line-no\">%%0%dd</span>", round( floor( log10( maxValue ) + 1.0 ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createLineNumberFormatString()

    /**
     *  Determines the name of an element for output.
     *
     *  @param  element The element.
     *  @return The name of the element for output.
     */
    public static final String determineElementName( final Element element )
    {
        final var retValue = switch( element.getKind() )
            {
                case PACKAGE -> element.toString().replace( '.', '/' ) + "/package-info.java";
                case MODULE -> "module-info.java";
                case ANNOTATION_TYPE, CLASS, ENUM, INTERFACE, RECORD -> element.toString().replace( '.', '/' ) + ".java";
                case CONSTRUCTOR, ENUM_CONSTANT, FIELD, METHOD -> format( "%s::%s", determineElementName( element.getEnclosingElement() ), element );
                default -> format( "Don't know yet how to get the output name for '%1$s' of kind '%2$s'", element, element.getKind().toString() );
            };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineElementName()

    /**
     *  Finds the end of a tag in the given text.
     *
     *  @param  text    The text.
     *  @return The position for closing bracket for the tag, or -1 if there is
     *      no closing bracket, or the text is not a tag.
     */
    private static final int findEnd( final String text )
    {
        var retValue = -1;
        if( text.startsWith( TAG_LEADIN ) )
        {
            var level = 1;
            retValue = 2;
            while( (level > 0) && (retValue < text.length()) )
            {
                switch( text.charAt( retValue ) )
                {
                    case '}' -> { if( --level > 0 ) ++retValue; }
                    case '{' -> { if( (retValue++ < text.length()) && (text.charAt( retValue ) == '@') ) ++level; }
                    default -> ++retValue;
                }
            }
            if( level > 0 ) retValue = -1;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findEnd()

    /**
     *  Returns the file object for the file with the given name that is
     *  associated with the given type element. Use this to determine to
     *  storage location for a documentation file for a class or alike.
     *
     *  @param  environment The doclet environment, providing the necessary
     *      settings and helpers.
     *  @param  typeElement The type element that determines the storage
     *      location.
     *  @param  fileName    The file name; it may contain a relative path.
     *  @return The output file object.
     *  @throws IOException A problem showed up when determining the output
     *      file object.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    @SuppressWarnings( "resource" )
    public static final FileObject getOutputFileObject( final DocletEnvironment environment, final TypeElement typeElement, final String fileName ) throws IOException
    {
        final var elementUtils = requireNonNullArgument( environment, "environment" ).getElementUtils();
        final var fileManager = environment.getJavaFileManager();

        final var packageName = elementUtils.getPackageOf( requireNonNullArgument( typeElement, "typeElement" ) ).getQualifiedName().toString();
        final var module = elementUtils.getModuleOf( typeElement );
        final var location = isNull( module ) ? DOCUMENTATION_OUTPUT : fileManager.getLocationForModule( DOCUMENTATION_OUTPUT, module.getQualifiedName().toString() );
        final var retValue = fileManager.getFileForOutput( location, packageName, fileName, null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getOutputFileObject()

    /**
     *  The taglets that are internally used to handle embedded tags needs to
     *  be initialised with references to the
     *  {@link DocletEnvironment}
     *  and the
     *  {@link Doclet}.
     *
     *  @param  docletEnvironment   The doclet environment.
     *  @param  doclet  The doclet.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final void initHelperTaglets( final DocletEnvironment docletEnvironment, final Doclet doclet )
    {
        synchronized( m_InlineTagletCollectionGuard )
        {
            if( isNull( m_InlineTagletCollection ) )
            {
                //---* Create the collection of inline taglets *---------------
                m_InlineTagletCollection = List.of
                    (
                        new AnchorTaglet(),
                        new org.tquadrat.foundation.javadoc.internal.CodeTaglet(),
                        new HRefTaglet(),
                        new org.tquadrat.foundation.javadoc.internal.IndexTaglet(),
                        new org.tquadrat.foundation.javadoc.internal.LiteralTaglet(),
                        new UnderlineTaglet()
                    );

                //---* Initialise the taglets *--------------------------------
                m_InlineTagletCollection.forEach( t -> t.init( docletEnvironment, doclet ) );
            }
        }
    }   //  initHelperTaglets()

    /**
     *  Parses the given tags for a name and an email address.<br>
     *  <br>The tag is prefixed with the tag text, and name and email address
     *  are separated by a hyphen ("-").
     *
     *  @param  taglet  The originating taglet.
     *  @param  tags    The tags.
     *  @return The formatted output.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String parseNameAndEmail( final Taglet taglet, final Collection<? extends DocTree> tags )
    {
        final var prefix = format( "@%s", taglet.getName() );
        final var prefixLen = prefix.length();
        final var retValue = tags.stream()
            .map( Object::toString )
            .map( s -> s.substring( prefixLen ).trim() )
            .map( s ->
            {
                final var pos = s.indexOf( " - " );
                if( pos < 0 )
                {
                    if( s.endsWith( " -" ) ) return s.substring( 0, s.length() - 1 ).trim();
                    return s.trim();
                }

                final var emailAddress = s.substring( pos + 3 ).trim();
                if( emailAddress.isEmpty() ) return s.substring( 0, pos ).trim();

                final var buffer = s.substring( 0, pos ).trim() +
                    " (<a href=\"mailto:" +
                    emailAddress +
                    "\">" +
                    emailAddress +
                    "</a>)";
                return buffer;
            } )
            .collect( joining( ", ") );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseNameAndEmail()

    /**
     *  Processes inline tags in the given text.<br>
     *  <br>This method will consider only the inline tags
     *  <ul>
     *  <li>{@link org.tquadrat.foundation.javadoc.AnchorTaglet @anchor}</li>
     *  <li>{@code @code}</li>
     *  <li>{@link org.tquadrat.foundation.javadoc.HRefTaglet @href}</li>
     *  <li>{@code @index}</li>
     *  <li>{@code @literal}</li>
     *  <li>{@link org.tquadrat.foundation.javadoc.UnderlineTaglet @underline}</li>
     *  </ul>
     *
     *  @param  text    The text to process.
     *  @param  docTreeFactory  The factory for the doc tree instances.
     *  @param  element The element where this tag lives.
     *  @param  nameGenerator   A function that generates a name from a String.
     *  @return The text after the tags are processed.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String processInlineTags( final CharSequence text, final DocTreeFactory docTreeFactory, final Element element, final Function<? super CharSequence, ? extends Name> nameGenerator )
    {
        final var retValue = new StringBuilder();
        synchronized( m_InlineTagletCollectionGuard )
        {
            if( isNull( m_InlineTagletCollection ) ) throw new IllegalStateException( "Common.initHelperTaglets() was not called" );

            if( isNotEmpty( text ) )
            {
                var currentText = text.toString();
                var proceed = true;
                int pos;
                while( proceed )
                {
                    pos = currentText.indexOf( TAG_LEADIN );
                    if( pos < 0 )
                    {
                        retValue.append( currentText );
                        proceed = false;
                    }
                    else
                    {
                        retValue.append( currentText, 0, pos );
                        currentText = currentText.substring( pos );
                        pos = findEnd( currentText );
                        if( pos < 0 )
                        {
                            retValue.append( currentText );
                            proceed = false;
                        }
                        else
                        {
                            String prefix;
                            DocTree docTree;
                            for( final var taglet : m_InlineTagletCollection )
                            {
                                final var tagName = taglet.getName();
                                prefix = format( "%s%s ", TAG_LEADIN, tagName );
                                if( currentText.startsWith( prefix ) )
                                {
                                    final var textTree = docTreeFactory.newTextTree( currentText.substring( prefix.length(), pos ) );
                                    switch( tagName )
                                    {
                                        case "code" -> docTree = docTreeFactory.newCodeTree( textTree );
                                        case "index" ->
                                        {
                                            final var body = textTree.getBody();
                                            final String term;
                                            final String description;
                                            if( body.startsWith( "\"" ) )
                                            {
                                                final var p = body.indexOf( "\" ", 1 );
                                                if( p < 0 )
                                                {
                                                    term = body.trim();
                                                    description = term;
                                                }
                                                else
                                                {
                                                    term = body.substring( 1, p );
                                                    if( body.length() > p )
                                                    {
                                                        description = body.substring( p + 2 ).trim();
                                                    }
                                                    else
                                                    {
                                                        description = term.trim();
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                final var p = body.indexOf( " " );
                                                if( p < 0 )
                                                {
                                                    term = body.trim();
                                                    description = term;
                                                }
                                                else
                                                {
                                                    term = body.substring( 0, p );
                                                    if( body.length() > p )
                                                    {
                                                        description = body.substring( p + 1 ).trim();
                                                    }
                                                    else
                                                    {
                                                        description = term;
                                                    }
                                                }
                                            }
                                            docTree = docTreeFactory.newIndexTree( docTreeFactory.newTextTree( term ), singletonList( docTreeFactory.newTextTree( description ) ) );
                                        }
                                        case "literal" -> docTree = docTreeFactory.newLiteralTree( textTree );
                                        default -> docTree = docTreeFactory.newUnknownInlineTagTree( nameGenerator.apply( tagName ), singletonList( textTree ) );
                                    }
                                    retValue.append( taglet.toString( singletonList( docTree ), element ) );
                                }
                            }

                            if( currentText.length() > pos )
                            {
                                currentText = currentText.substring( pos + 1 );
                            }
                            else
                            {
                                proceed = false;
                            }
                        }
                    }
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  processInlineTags()
}
//  class Common

/*
 *  End of File
 */