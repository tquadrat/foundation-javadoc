/*
 * ============================================================================
 * Copyright © 2002-2026 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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
import static java.lang.System.arraycopy;
import static java.util.Objects.nonNull;
import static javax.tools.Diagnostic.Kind.MANDATORY_WARNING;
import static javax.tools.Diagnostic.Kind.WARNING;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.OtherFileTagletBase.ProcessMode.DEFAULT;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.splitString;

import javax.lang.model.element.Element;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.JavadocError;
import org.tquadrat.foundation.javadoc.internal.OtherFileTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.util.Template;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary This taglet allows to include the contents of an external file
 *  into the JavaDoc documentation.} This is particularly useful when the
 *  contents of a resource file (like a DTD or an XML Schema) should be shown
 *  in the documentation.</p>
 *  <p>Usually, that file is stored somewhere on the
 *  {@link #SOURCE_PATH SOURCE_PATH}; this means that a file is addressed
 *  by its path name on the source tree. For example to include this file, the
 *  path would be
 *  {@code org/tquadrat/foundation/javadoc/IncludeTaglet.java}.</p>
 *  <p>But additional roots can be provided through system properties, where
 *  the name of the new root will be prefixed by
 *  {@value #PROPERTY_INCLUDE_ROOT_PREFIX}, like
 *  {@systemProperty org.tquadrat.foundation.include.root.resources}. To use this root,
 *  prefix the path in the tag with {@code ${resources}}.</p>
 *  <p>The file contents can be processed in some way before it will be
 *  included; refer to
 *  {@link ProcessMode}
 *  for the details.</p>
 *  <p>The use of this tag will look like this:</p>
 *  <pre><code>&hellip; {&#64;include &lt;<i>filename</i>&gt;:&lt;<i>processMode</i>&gt;} &hellip;</code></pre>
 *  <p>where the &lt;processMode&gt; can be omitted. If the given file does not
 *  exist or is empty, nothing will be included, and no error message will be
 *  issued.</p>
 *  <p>Javadoc tags in the included files are not processed, even if the files
 *  resides in a {@code doc-files} folder.</p>
 *  <p><b>Notes:</b></p>
 *  <ul>
 *      <li>If Maven is used, and the include file is not placed at the
 *      {@code java} path (but on the {@code resources} path, for example), it
 *      is required to add the parameter <code>&lt;sourcepath&gt;</code> to the
 *      configuration of the {@code maven-javadoc-plugin}, where the path of
 *      the include file is added; otherwise, it will not be found.
 *      Alternatively an external root can be used, as described above.</li>
 *      <li>At default, Gradle adds only {@code *.java} files as sources for
 *      the Javadoc task. To make other files available for the
 *      {@code @include} tag, an external root should be used. This is
 *      mandatory for files that are stored at the {@code resources} path.</li>
 *      <li>In general, both Maven and Gradle may have funny ideas about the
 *      source path, so the recommendation is to use an external root
 *      always.</li>
 *      <li>If the source of a class should be included, the path to the source
 *      file is relative to the module directory, if one exists.</li>
 *      <li>The {@code {@include}} tag is an inline tag; that means that the
 *      contents of the included file will be seamlessly integrated into the
 *      other text of the respective Javadoc comment.</li>
 *  </ul>
 *  <p>Alternatively, you can use the {@code @snippet} tag.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IncludeTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: IncludeTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class IncludeTaglet extends OtherFileTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "include";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code IncludeTaglet} instance.}</p>
     */
    public IncludeTaglet()
    {
        super( TAGLET_NAME, true, Location.values() );
    }   //  IncludeTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Determines the name of an element for output.}</p>
     *
     *  @param  element The element.
     *  @return The name of the element for output.
     */
    private final String determineElementName( final Element element )
    {
        final var retValue = switch( element.getKind() )
            {
                case PACKAGE -> element.toString().replace( '.', '/' ) + "/package-info.java";
                case MODULE -> "module-info.java";
                case ANNOTATION_TYPE, CLASS, ENUM, INTERFACE, RECORD -> element.toString().replace( '.', '/' ) + ".java";
                case CONSTRUCTOR, ENUM_CONSTANT, FIELD, METHOD -> format( "%s::%s", determineElementName( element.getEnclosingElement() ), element );
                default -> format( "Don't know yet how to get the output name for '%1$s' of kind '%2$s'", element, element.getKind() );
            };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineElementName()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        var retValue = EMPTY_STRING;

        final var tag = tags.getFirst().toString().trim();
        final var len = tag.length() - 1;
        final var prefixLen = getName().length() + 3;
        final var contents = tag.substring( prefixLen, len ).trim();

        final var parts = splitString( contents, ':' );
        final String fileName;
        final ProcessMode processMode;
        final String [] parameters;

        switch( parts.length )
        {
            case 0 ->
            {
                //---* No argument for the tag *-------------------------------
                fileName = EMPTY_STRING;
                //noinspection AssignmentToNull
                processMode = null;
                //noinspection AssignmentToNull
                parameters = null;
            }
            case 1 ->
            {
                //---* Just the file name *------------------------------------
                fileName = new Template( parts[0].trim() ).replaceVariable( IncludeTaglet::rootPathRetriever );
                processMode = DEFAULT;
                parameters = EMPTY_String_ARRAY;
            }
            case 2 ->
            {
                //---* A file name and the process mode *----------------------
                fileName = new Template( parts[0].trim() ).replaceVariable( IncludeTaglet::rootPathRetriever );
                ProcessMode p;
                try
                {
                    p = ProcessMode.valueOf( parts[1].trim() );
                }
                catch( final IllegalArgumentException _ )
                {
                    p = DEFAULT;
                }
                processMode = p;
                parameters = EMPTY_String_ARRAY;
            }
            default ->
            {
                //---* A file name and the process mode plus some parameters *-
                fileName = new Template( parts[0].trim() ).replaceVariable( IncludeTaglet::rootPathRetriever );
                ProcessMode p;
                try
                {
                    p = ProcessMode.valueOf( parts[1].trim() );
                }
                catch( final IllegalArgumentException _ )
                {
                    p = DEFAULT;
                }
                processMode = p;
                final var length = parts.length - 2;
                parameters = new String[length];
                arraycopy( parts, 2, parameters, 0, length );
            }
        }

        if( isNotEmptyOrBlank( fileName ) )
        {
            if( fileName.startsWith( "/" ) )
            {
                //---* An absolute path based on an external root *------------
                final var file = new File( fileName );
                if( !file.exists() )
                {
                    final var elementName = determineElementName( element );
                    printf( WARNING,"Cannot locate the include file '%2$s' for '%1$s'%n", elementName, fileName );
                }
                //noinspection OverlyBroadCatchBlock
                try( final Reader reader = new FileReader( file ) )
                {
                    //noinspection ConstantConditions
                    retValue = processMode.processFile( fileName, reader, parameters );
                }
                catch( final IOException e )
                {
                    //---* Should never happen *-------------------------------
                    final var elementName = determineElementName( element );
                    printf( MANDATORY_WARNING,"Failed to include the file '%2$s' for '%1$s'%n", elementName, fileName );
                }
            }
            else if( !fileName.startsWith( "$" ) )
            {
                //---* A relative path *---------------------------------------
                try
                {
                    final var inputFile = getDocletEnvironment().getJavaFileManager().getFileForInput( SOURCE_PATH, EMPTY_STRING, fileName );
                    if( nonNull( inputFile) )
                    {
                        //noinspection ConstantConditions
                        retValue = processMode.processFile( inputFile, parameters );
                    }
                    else
                    {
                        final var elementName = determineElementName( element );
                        printf( MANDATORY_WARNING, "Failed to include the file '%2$s' for '%1$s'%n", elementName, fileName );
                    }
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( "Problems getting FileObject for '%s'", fileName ), e );
                }
            }
            else
            {
                //---* Retrieval for the root path failed *--------------------
                final var elementName = determineElementName( element );
                printf( WARNING, "Failed to resolve include file '%2$s' for '%1$s'%n", elementName, fileName );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class IncludeTaglet

/*
 *  End of File
 */