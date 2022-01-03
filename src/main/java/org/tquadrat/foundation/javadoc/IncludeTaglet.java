/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

import static java.lang.System.arraycopy;
import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.IncludeTaglet.ProcessMode.DEFAULT;
import static org.tquadrat.foundation.javadoc.internal.Common.SOURCE_PATH;
import static org.tquadrat.foundation.javadoc.internal.Common.createLineNumberFormatString;
import static org.tquadrat.foundation.javadoc.internal.Common.determineElementName;
import static org.tquadrat.foundation.javadoc.internal.Common.initHelperTaglets;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.IOUtils.loadToString;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.splitString;

import javax.lang.model.element.Element;
import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.Common;
import org.tquadrat.foundation.javadoc.internal.JavadocError;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.util.Template;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary This taglet allows to include the contents of an external file
 *  into the JavaDoc documentation.} This is particularly useful when the
 *  contents of resource files (like a DTD or an XML Schema) should be shown in
 *  the documentation.</p>
 *  <p>Usually, that file is stored somewhere on the
 *  {@link Common#SOURCE_PATH SOURCE_PATH}; this means that a file is addressed
 *  by its path name on the source tree. For example to include this file, the
 *  path would be
 *  {@code org/tquadrat/foundation/javadoc/IncludeTaglet.java}.</p>
 *  <p>But additional roots can be provided through system properties, where
 *  the name of the new root will be prefixed by
 *  {@value #PROPERTY_INCLUDE_ROOT_PREFIX}, like
 *  {@code org.tquadrat.foundation.include.root.resources}. To use this root,
 *  prefix the path in the tag with {@code ${resources}}.</p>
 *  <p>The file contents can be processed in some way before it will be
 *  included; refer to
 *  {@link ProcessMode}
 *  for the details.</p>
 *  <p>The use of this tag will look like this:</p>
 *  <pre><code>&hellip; {&#64;include &lt;<i>filename</i>&gt;:&lt;<i>processMode</i>&gt;} &hellip;</code></pre>
 *  {@ignore {@include <filename>:<processMode>}}
 *  <p>where the &lt;processMode&gt; can be omitted. If the given file does not
 *  exist or is empty, nothing will be included, and no error message will be
 *  issued.</p>
 *
 *  @note   If Maven is used, and the include file is not placed at the
 *      {@code java} path (but on the {@code resources} path, for example}, it
 *      is required to add the parameter <code>&lt;sourcepath&gt;</code> to the
 *      configuration of the {@code maven-javadoc-plugin}, where the path of
 *      the include file is added; otherwise, it will not be found.
 *      Alternatively an external root can be used, as described above.
 *
 *  @note   At default, Gradle adds only {@code *.java} files as sources for
 *      the Javadoc task. To make other files available for the
 *      {@code @include} tag, an external root should be used. This is
 *      mandatory for files that are stored at the {@code resources} path.
 *
 *  @note   In general, both Maven and Gradle may have funny ideas about the
 *      source path, so the recommendation is to use an external root always.
 *
 *  @note   If the source of a class should be included, the path to the source
 *      file is relative to the module directory, if one exists.
 *
 *  @note   The {@code {@include}} tag is an inline tag; that means that the
 *      contents of the included file will be seamlessly integrated into the
 *      other text of the respective Javadoc comment.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IncludeTaglet.java 826 2021-01-04 12:17:23Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: IncludeTaglet.java 826 2021-01-04 12:17:23Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public class IncludeTaglet implements Taglet
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The process modes for the included file.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: IncludeTaglet.java 826 2021-01-04 12:17:23Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "InnerClassTooDeeplyNested" )
    @ClassVersion( sourceVersion = "$Id: IncludeTaglet.java 826 2021-01-04 12:17:23Z tquadrat $" )
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
                        .map( StringUtils::escapeHTML )
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
         *  The file contents will be included as is; this works for really
         *  simple plain text files that do not contain any HTML relevant
         *  characters, or for valid HTML snippets.
         *
         *  @note   Line breaks are ignored by HTML browsers; this means that a
         *      plain text file will appear as a long single line.
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
                        .map( StringUtils::escapeHTML )
                        .collect( toList() );
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
                        .map( StringUtils::escapeHTML )
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
     *  The error message indicating a problem when processing the include
     *  file.
     */
    public static final String MSG_ProcessingProblem = "Problems on processing '%s'";

    /**
     *  The prefix for the name of a property that holds the root for path of
     *  an include file: {@value}. It will be set on the {@code javadoc}
     *  command line like this:
     *  <pre><code>-J-Dorg.tquadrat.foundation.include.root.&lt;<i>name</i>&gt;=&lt;<i>path</i>&gt;</code></pre>
     *  {@ignore -J-Dorg.tquadrat.foundation.include.root.<name>=<path>}
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String PROPERTY_INCLUDE_ROOT_PREFIX = "org.tquadrat.foundation.include.root";

    /**
     *  The snippet end marker for source code: {@value}.
     */
    public static final String SOURCE_SNIP_END = "/\u002ASNIP_END\u002A/";

    /**
     *  The snippet start marker for source code: {@value}.
     */
    public static final String SOURCE_SNIP_START = "/\u002ASNIP_START\u002A/";

    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "include";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The doclet.
     */
    @SuppressWarnings( {"FieldCanBeLocal", "unused"} )
    private Doclet m_Doclet;

    /**
     *  The doclet environment.
     */
    private DocletEnvironment m_DocletEnvironment;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code IncludeTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public IncludeTaglet() { /* Just exists */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Location> getAllowedLocations() { return EnumSet.allOf( Location.class ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getName() { return TAGLET_NAME; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void init( final DocletEnvironment docletEnvironment, final Doclet doclet )
    {
        Taglet.super.init( docletEnvironment, doclet );
        m_Doclet = doclet;
        m_DocletEnvironment = docletEnvironment;
        initHelperTaglets( docletEnvironment, doclet );
    }   //  init()

    /**
     *  {@inheritDoc}
     */
    @Override
    public  final boolean isInlineTag() { return true; }

    /**
     *  Looks up the system properties for a root path for include files with
     *  the given name (the full name would be the prefix
     *  {@value #PROPERTY_INCLUDE_ROOT_PREFIX}
     *  appended by the argument, separated by a '.'.
     *
     *  @param  variable    The name of the root path.
     *  @return A instance of
     *      {@link Optional}
     *      that holds the path.
     */
    private static final Optional<String> rootPathRetriever( final String variable )
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
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        var retValue = EMPTY_STRING;

        final var tag = tags.get( 0 ).toString().trim();
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
                catch( @SuppressWarnings( "unused" ) final IllegalArgumentException e )
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
                catch( @SuppressWarnings( "unused" ) final IllegalArgumentException e )
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
                    out.printf( "Cannot locate the include file '%2$s' for '%1$s'%n", elementName, fileName );
                }
                //noinspection OverlyBroadCatchBlock
                try( final Reader reader = new FileReader( file ) )
                {
                    retValue = processMode.processFile( fileName, reader, parameters );
                }
                catch( final IOException e )
                {
                    //---* Should never happen *-------------------------------
                    final var elementName = determineElementName( element );
                    out.printf( "Failed to include the file '%2$s' for '%1$s'%n", elementName, fileName );
                }
            }
            else if( !fileName.startsWith( "$" ) )
            {
                //---* A relative path *---------------------------------------
                try
                {
                    @SuppressWarnings( "resource" )
                    final var inputFile = m_DocletEnvironment.getJavaFileManager().getFileForInput( SOURCE_PATH, EMPTY_STRING, fileName );
                    if( nonNull( inputFile) )
                    {
                        retValue = processMode.processFile( inputFile, parameters );
                    }
                    else
                    {
                        final var elementName = determineElementName( element );
                        out.printf( "Failed to include the file '%2$s' for '%1$s'%n", elementName, fileName );
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
                out.printf( "Failed to resolve include file '%2$s' for '%1$s'%n", elementName, fileName );
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