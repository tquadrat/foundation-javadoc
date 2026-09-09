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

package org.tquadrat.foundation.javadoc.internal;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.createLineNumberFormatString;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.loadToString;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.DocumentationTool;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.tquadrat.foundation.javadoc.IncludeTaglet;
import org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary This class is the base class for taglets that allow to include
 *  the contents of an external file into the JavaDoc documentation.} This is
 *  particularly useful when the contents of a resource file (like a DTD or an
 *  XML Schema) should be shown in the documentation.</p>
 *  <p>Usually, that file is stored somewhere on the
 *  {@link #SOURCE_PATH SOURCE_PATH}; this means that a file is addressed
 *  by its path name on the source tree. For example to include this file, the
 *  path would be
 *  {@code org/tquadrat/foundation/javadoc/internal/OtherFileTagletBase.java}.</p>
 *  <p>But additional roots can be provided through system properties, where
 *  the name of the new root will be prefixed by
 *  {@value #PROPERTY_INCLUDE_ROOT_PREFIX}, like
 *  {@code org.tquadrat.foundation.include.root.resources}. To use this root,
 *  prefix the path in the tag with {@code ${resources}}.</p>
 *  <p>The file contents can be processed in some way before it will be
 *  included; refer to
 *  {@link ProcessMode}
 *  for the details.</p>
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
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: OtherFileTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: OtherFileTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = INTERNAL, since = "0.1.0" )
public sealed abstract class OtherFileTagletBase extends CustomTagletBase
    permits IncludeTaglet, UmlGraphLinkTaglet
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary The process modes for the included file.}</p>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: OtherFileTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $
     *  @since 0.0.5
     */
    @SuppressWarnings( "InnerClassTooDeeplyNested" )
    @ClassVersion( sourceVersion = "$Id: OtherFileTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.5", consumers = "org.tquadrat.foundation.javadoc" )
    public enum ProcessMode
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  The default mode that is used if no process mode is given; the same
         *  as
         *  {@link #ESCAPE}.
         */
        @API( status = INTERNAL, since = "0.0.5" )
        DEFAULT
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                return ESCAPE.processFile( fileName, reader, params );
            }   //  processFile()
        },

        /**
         *  HTML relevant characters will be escaped in the file contents, and
         *  line breaks are replaced by the HTML tag {@code <br>}.
         */
        @API( status = INTERNAL, since = "0.0.5" )
        ESCAPE
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                final String retValue;
                try( final var bufferedReader = new BufferedReader( reader ) )
                {
                    retValue = bufferedReader.lines()
                        .map( ToolKit::escapeHTML )
                        .collect( joining( "<br>" ) );
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( MSG_ProcessingProblem, fileName ), e );
                }

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  processFile()
        },

        /**
         *  <p>{@summary The file contents will be treated as Markdown.} This
         *  works means, it will be first parsed and then rendered to HTML that
         *  in turn is inserted as for
         *  {@link #PLAIN}.</p>
         */
        @API( status = INTERNAL, since = "0.25.0" )
        MARKDOWN
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                final String retValue;
                try
                {
                    //---* Set up the Markdown conversion *--------------------
                    final List<Extension> extensions = List.of( TablesExtension.create() );
                    final var markdownParser = Parser.builder()
                            .extensions( extensions )
                            .build();
                    final var htmlRenderer = HtmlRenderer.builder()
                            .extensions( extensions )
                            .build();

                    //---* Parse the Markdown *--------------------------------
                    final var markdown = loadToString( reader );
                    final var document = markdownParser.parse( markdown );

                    //---* Convert the Markdown to HTML *----------------------
                    retValue = htmlRenderer.render( document );
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( MSG_ProcessingProblem, fileName ), e );
                }

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  processFile()
        },

        /**
         *  <p>{@summary The file contents will be included as is; this works
         *  for really simple plain text files that do not contain any HTML
         *  relevant characters, or for valid HTML snippets.}</p>
         *  <p>Line breaks are ignored by HTML browsers; this means that a
         *  plain text file will appear as a long single line.</p>
         */
        @API( status = INTERNAL, since = "0.0.5" )
        PLAIN
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                final String retValue;
                try
                {
                    retValue = loadToString( reader );
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( MSG_ProcessingProblem, fileName ), e );
                }

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  processFile()
        },

        /**
         *  The file contents is source code of some kind. It will be treated
         *  as for
         *  {@link #ESCAPE},
         *  and additionally, each line will be prefixed with a line number.
         */
        @API( status = INTERNAL, since = "0.0.5" )
        SOURCE
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                final List<String> lines;
                try( final var bufferedReader = new BufferedReader( reader ) )
                {
                    lines = bufferedReader.lines()
                        .map( ToolKit::escapeHTML )
                        .toList();
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( MSG_ProcessingProblem, fileName ), e );
                }

                final var lineNumberFormat = createLineNumberFormatString( lines.size() );

                var lineNumber = 1;
                final var buffer = new StringBuilder( "<div class=\"source-container\"><pre>" );
                for( final var line : lines )
                {
                    buffer.append( format( lineNumberFormat, lineNumber++ ) )
                        .append( line )
                        .append( '\n' );
                }

                final var retValue = buffer.append( "</pre></div>" ).toString();

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  processFile()
        },

        /**
         *  <p>{@summary The file contents is source code of some kind.} It will
         *  be treated as for
         *  {@link #SOURCE},
         *  but only the lines between a line starting with
         *  {@value #SOURCE_SNIP_START}
         *  and another one beginning with
         *  {@value #SOURCE_SNIP_END}
         *  or the end of the file.</p>
         *  <p>The line numbers are as for the full file.</p>
         *  <p>Additionally, a snippet can be selected by its index if there
         *  are more than one snippet marked in the source file.</p>
         */
        @API( status = INTERNAL, since = "0.0.5" )
        SOURCE_SNIPPET
        {
            /**
             *  Returns all snippets.
             *
             *  @param  buffer  The output buffer.
             *  @param  lines   The lines of the file.
             *  @param  lineNumberFormat    The line number format String.
             */
            private final void allSnippets( final StringBuilder buffer, final Iterable<String> lines, final String lineNumberFormat )
            {
                var lineNumber = 1;
                var includeLine = false;
                AppendLoop:for( final var line : lines )
                {
                    if( line.startsWith( SOURCE_SNIP_START ) )
                    {
                        includeLine = true;
                        if( lineNumber > 1 ) buffer.append( "&hellip;\n" );
                        continue AppendLoop;
                    }
                    if( line.startsWith( SOURCE_SNIP_END ) )
                    {
                        includeLine = false;
                        buffer.append( "&hellip;\n" );
                        continue AppendLoop;
                    }
                    if( includeLine )
                    {
                        buffer.append( format( lineNumberFormat, lineNumber ) )
                            .append( line )
                            .append( '\n' );
                    }
                    ++lineNumber;
                }   //  AppendLoop:
            }   //  allSnippets()

            /**
             *  Returns one single snippet, identified by the index.
             *
             *  @param  buffer  The output buffer.
             *  @param  lines   The lines of the file.
             *  @param  lineNumberFormat    The line number format String.
             *  @param  index   The number of the snippet, with 1 for the first
             *      snippet.
             */
            private final void oneSnippet( final StringBuilder buffer, final Iterable<String> lines, final String lineNumberFormat, final int index )
            {
                var lineNumber = 1;
                final List<StringBuilder> snippets = new ArrayList<>();
                StringBuilder localBuffer = null;
                AppendLoop:for( final var line : lines )
                {
                    if( line.startsWith( SOURCE_SNIP_START ) )
                    {
                        localBuffer = new StringBuilder();
                        snippets.add( localBuffer );
                        if( lineNumber > 1 ) localBuffer.append( "&hellip;\n" );
                        continue AppendLoop;
                    }
                    if( line.startsWith( SOURCE_SNIP_END ) )
                    {
                        if( nonNull( localBuffer ) )
                        {
                            localBuffer.append( "&hellip;\n" );
                            //noinspection AssignmentToNull
                            localBuffer = null;
                        }
                        continue AppendLoop;
                    }
                    if( nonNull( localBuffer ) )
                    {
                        localBuffer.append( format( lineNumberFormat, lineNumber ) )
                            .append( line )
                            .append( '\n' );
                    }
                    ++lineNumber;
                }   //  AppendLoop:

                if( index - 1 <= snippets.size() )
                {
                    buffer.append( snippets.get( index - 1 ) );
                }
                else
                {
                    out.printf( "Cannot include snippet %d, only %d defined\n", index, snippets.size() );
                }
            }   //  oneSnippet()

            /**
             *  {@inheritDoc}
             */
            @Override
            public final String processFile( final String fileName, final Reader reader, final String... params )
            {
                final List<String> lines;
                try( final var bufferedReader = new BufferedReader( reader ) )
                {
                    lines = bufferedReader.lines()
                        .map( ToolKit::escapeHTML )
                        .collect( toList() );
                }
                catch( final IOException e )
                {
                    throw new JavadocError( format( MSG_ProcessingProblem, fileName ), e );
                }

                final var lineNumberFormat = createLineNumberFormatString( lines.size() );

                final var buffer = new StringBuilder( "<div class=\"source-container\"><pre>" );
                if( params.length == 0 )
                {
                    allSnippets( buffer, lines, lineNumberFormat );
                }
                else
                {
                    try
                    {
                        final var index = Integer.parseInt( params [0] );
                        oneSnippet( buffer, lines, lineNumberFormat, index );
                    }
                    catch( @SuppressWarnings( "unused" ) final NumberFormatException e )
                    {
                        out.printf( "Invalid value for snippet index: %s\n", params [0] );
                        allSnippets( buffer, lines, lineNumberFormat );
                    }
                }

                final var retValue = buffer.append( "</pre></div>" ).toString();

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  processFile()
        };

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Processes the given file.
         *
         *  @param  fileName    The name of the file to process; used for error
         *      messages only.
         *  @param  reader    The file to process.
         *  @param  params  Optional parameters for the processing.
         *  @return  The processed file contents.
         *
         *  @since 0.1.0
         */
        @API( status = INTERNAL, since ="0.1.0" )
        public abstract String processFile( final String fileName, final Reader reader, String ... params );

        /**
         *  Processes the given file.
         *
         *  @param  file    The file to process.
         *  @param  params  Optional parameters for the processing.
         *  @return  The processed file contents.
         */
        public final String processFile( final FileObject file, final String ... params )
        {
            final String retValue;
            try( final var reader = file.openReader( true ) )
            {
                retValue = processFile( file.getName(), reader, params );
            }
            catch( final IOException e )
            {
                throw new JavadocError( format( "Cannot open Reader on file '%s'", file.getName() ), e );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  processFile()
    }
    //  enum ProcessMode

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The error message indicating a problem when processing the
     *  include file.}</p>
     */
    public static final String MSG_ProcessingProblem = "Problems on processing '%s'";

    /**
     *  <p>{@summary The prefix for the name of a property that holds the root
     *  for path of an include file: {@value}.} It will be set on the
     *  {@code javadoc} command line like this:</p>
     *  <pre><code>-J-Dorg.tquadrat.foundation.include.root.&lt;<i>name</i>&gt;=&lt;<i>path</i>&gt;</code></pre>
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String PROPERTY_INCLUDE_ROOT_PREFIX = "org.tquadrat.foundation.include.root";

    /**
     *  <p>{@summary The snippet end marker for source code: {@value}.}</p>
     */
    @SuppressWarnings( "UnnecessaryUnicodeEscape" )
    public static final String SOURCE_SNIP_END = "/\u002ASNIP_END\u002A/";

    /**
     *  <p>{@summary The snippet start marker for source code: {@value}.}</p>
     */
    @SuppressWarnings( "UnnecessaryUnicodeEscape" )
    public static final String SOURCE_SNIP_START = "/\u002ASNIP_START\u002A/";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary Location to search for modules containing annotation
     *  processors.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location ANNOTATION_PROCESSOR_MODULE_PATH;

    /**
     *  <p>{@summary Location to search for annotation processors.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location ANNOTATION_PROCESSOR_PATH;

    /**
     *  <p>{@summary Location of new class files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location CLASS_OUTPUT;

    /**
     *  <p>{@summary Location to search for user class files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location CLASS_PATH;

    /**
     * <p>{@summary Location to search for doclets.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location DOCLET_PATH;

    /**
     * <p>{@summary Location of new documentation files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location DOCUMENTATION_OUTPUT;

    /**
     *  <p>{@summary Location to search for precompiled user modules.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location MODULE_PATH;

    /**
     *  <p>{@summary Location to search for the source code of modules.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location MODULE_SOURCE_PATH;

    /**
     *  <p>{@summary Location of new native header files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location NATIVE_HEADER_OUTPUT;

    /**
     *  <p>{@summary Location to search for module patches.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location PATCH_MODULE_PATH;

    /**
     *  <p>{@summary Location to search for platform classes. Sometimes called
     *  the boot class path.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location PLATFORM_CLASS_PATH;

    /**
     *  <p>{@summary Location of new source files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SOURCE_OUTPUT;

    /**
     *  <p>{@summary Location to search for existing source files.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SOURCE_PATH;

    /**
     *  <p>{@summary Location to search for system modules.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location SYSTEM_MODULES;

    /**
     * <p>{@summary Location to search for taglets.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location TAGLET_PATH;

    /**
     *  <p>{@summary Location to search for upgradeable system modules.}</p>
     */
    @API( status = MAINTAINED, since = "0.0.5")
    public static final JavaFileManager.Location UPGRADE_MODULE_PATH;

    static
    {
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
     *  <p>{@summary Creates a new {@code OtherFileTagletBase} instance.}</p>
     *
     *  @param  name    The name of the taglet.
     *  @param  isInlineTag {@code true} if the tag implemented by this taglet
     *      is an inline tag, {@code false} if it is a block tag.
     *  @param  allowedLocations    The locations that are allowed for this
     *      taglet.
     *
     *  @see jdk.javadoc.doclet.Taglet#getName()
     *  @see jdk.javadoc.doclet.Taglet#isInlineTag()
     *  @see jdk.javadoc.doclet.Taglet#getAllowedLocations()
     */
    protected OtherFileTagletBase( final String name, final boolean isInlineTag, final Location... allowedLocations )
    {
        super( name, isInlineTag, allowedLocations );
    }   //  OtherFileTagletBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Returns the file object for the file with the given name
     *  that is associated with the given type element.} Use this to determine
     *  to storage location for a documentation file for a class or alike.</p>
     *
     *  @param  typeElement The type element that determines the storage
     *      location.
     *  @param  fileName    The file name; it may contain a relative path.
     *  @return The output file object.
     *  @throws IOException A problem showed up when determining the output
     *      file object.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    protected final FileObject getOutputFileObject( final TypeElement typeElement, final String fileName ) throws IOException
    {
        final var elementUtils = getDocletEnvironment().getElementUtils();
        final var fileManager = getDocletEnvironment().getJavaFileManager();

        final var packageName = elementUtils.getPackageOf( requireNonNullArgument( typeElement, "typeElement" ) ).getQualifiedName().toString();
        final var module = elementUtils.getModuleOf( typeElement );
        final var location = isNull( module ) ? DOCUMENTATION_OUTPUT : fileManager.getLocationForModule( DOCUMENTATION_OUTPUT, module.getQualifiedName().toString() );
        final var retValue = fileManager.getFileForOutput( location, packageName, fileName, null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getOutputFileObject()

    /**
     *  <p>{@summary Looks up the system properties for a root path for include
     *  files with the given name.} The full name would be the prefix
     *  {@value #PROPERTY_INCLUDE_ROOT_PREFIX}
     *  appended by the argument, separated by a '.'.</p>
     *
     *  @param  variable    The name of the root path.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the path.
     */
    protected static final Optional<String> rootPathRetriever( final String variable )
    {
        Optional<String> retValue = Optional.empty();
        if( isNotEmptyOrBlank( variable ) )
        {
            final var key = format( "%s.%s", PROPERTY_INCLUDE_ROOT_PREFIX, variable );
            final var fileName = getProperty( key );
            if( nonNull( fileName ) ) retValue = Optional.of( new File( fileName ).getAbsolutePath() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  rootPathRetriever()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract String toString( final List<? extends DocTree> tags, final Element element );
}
//  class OtherFileTagletBase

/*
 *  End of File
 */