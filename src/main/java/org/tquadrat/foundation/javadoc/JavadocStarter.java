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

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Locale.UK;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.splitString;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.spi.ToolProvider;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ProgramClass;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.ApplicationError;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.LambdaContainerException;

/**
 *  <p>{@summary Executes the {@code JavaDoc} tool with a bunch of default
 *  parameters.}</p>
 *  <p>The program takes the name of a file as the only command line argument;
 *  that file contains the Javadoc options and parameters, as described in
 *  { href https://docs.oracle.com/en/java/javase/15/docs/specs/man/javadoc.html}
 *  and
 *  { href https://docs.oracle.com/en/java/javase/15/docs/specs/javadoc/doc-comment-spec.html}.</p>
 *  <p>The following default settings are made by the program:</p>
 *  <pre><code>
 *  -author
 *  -bottom Copyright © 2002-2021 by Thomas Thrien (tquadrat.org)
 *  -breakiterator
 *  -charset UTF-8
 *  -docencoding UTF-8
 *  -docfilessubdirs
 *  --enable-preview
 *  -encoding UTF-8
 *  --expand-requires all
 *  -footer ""
 *  -header ""
 *  -html5
 *  -javafx
 *  -keywords
 *  -link https://docs.oracle.com/en/java/javase/15/docs/api/
 *  -link https://apiguardian-team.github.io/apiguardian/docs/current/api/
 *  -linksource
 *  -locale en_GB
 *  -notimestamp
 *  -private
 *  -quiet
 *  -serialwarn
 *  --show-members private
 *  --show-module-contents all
 *  --show-packages all
 *  --show-types private
 *  -source 15
 *  -sourcetab 4
 *  -splitindex
 *  -tag note
 *  -tag param
 *  -tag return
 *  -tag throws
 *  -tag author
 *  -tag extauthor
 *  -tag thanks
 *  -tag modified
 *  -tag version
 *  -tag since
 *  -tag see
 *  -tag inspired
 *  -tag UMLGraph.link
 *  -tag todo
 *  -taglet org.tquadrat.foundation.javadoc.AuthorTaglet
 *  -taglet org.tquadrat.foundation.javadoc.AnchorTaglet
 *  -taglet org.tquadrat.foundation.javadoc.HRefTaglet
 *  -taglet org.tquadrat.foundation.javadoc.IgnoreTaglet
 *  -taglet org.tquadrat.foundation.javadoc.IncludeTaglet
 *  -taglet org.tquadrat.foundation.javadoc.InspiredTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ModifiedTaglet
 *  -taglet org.tquadrat.foundation.javadoc.NoteTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ThanksTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ToDoTaglet
 *  -taglet org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet
 *  -taglet org.tquadrat.foundation.javadoc.UnderlineTaglet
 *  -top &lt;div style='overflow:auto;'&gt;&lt;img src='{&#64;docRoot}/resources/tquadrat_logo.jpg' alt='tquadrat.org' style='float:right;''&gt;&lt;p style='font-family:sans-serif;font-size:40px;font-weight:bold;padding-left:30px;'&gt;tquadrat Foundation Library%lt;/p'&gt;&lt;/div&gt;'
 *  -use
 *  -version
 *  </code></pre>
 *  <p>The format must have the following format:</p>
 *  <ul>
 *      <li>Lines beginning with the '#' symbol are comments and will be
 *      ignored; same for empty lines.</li>
 *      <li>A line with an option starts with a '-' hyphen; the arguments for
 *      the option are separated with a blank from the option itself.</li>
 *      <li>Usually, the end of the line terminates an option, but a backslash
 *      as the last character of the line allows to extend the option into the
 *      next line.</li>
 *      <li>Each non-empty line that does not contain an option or a comment is
 *      either a package or a source file.</li>
 *  </ul>
 *
 *  @author  Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: JavadocStarter.java 976 2022-01-06 11:39:58Z tquadrat $
 *  @since 0.1.0
 */
@ProgramClass
@ClassVersion( sourceVersion = "$Id: JavadocStarter.java 976 2022-01-06 11:39:58Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class JavadocStarter
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of the logo file: {@value}.
     */
    public static final String LOGO = "tquadrat_logo.jpg";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The tool provider.
     */
    private final ToolProvider m_ToolProvider;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The default arguments.<br>
     *  <br>The map is immutable.
     */
    private static final Map<String,String> m_DefaultArguments;

    /**
     *  The list of the Link URLs.
     */
    private static final Collection<URL> m_Links = new ArrayList<>();

    /**
     *  The arguments that can be replaced by those on the command line.<br>
     *  <br>The key is the argument name, the value is the arguments value; it
     *  will never be empty.<br>
     *  <br>The map as such is immutable.
     */
    private static final Map<String,String> m_ReplaceableArguments;

    /**
     *  The tags and taglets.
     */
    private static final List<String> m_Tags;

    static
    {
        Locale.setDefault( UK );

        final Map<String,String> defaultArguments = new TreeMap<>();
        defaultArguments.put( "-author", EMPTY_STRING );
        defaultArguments.put( "-breakiterator", EMPTY_STRING );
        defaultArguments.put( "-charset", UTF_8.name() );
        defaultArguments.put( "-docencoding", UTF_8.name() );
        defaultArguments.put( "-docfilessubdirs", EMPTY_STRING );
        defaultArguments.put( "--enable-preview", EMPTY_STRING );
        defaultArguments.put( "-encoding", UTF_8.name() );
        defaultArguments.put( "-html5", EMPTY_STRING );
        defaultArguments.put( "-javafx", EMPTY_STRING );
        defaultArguments.put( "-keywords", EMPTY_STRING );
        defaultArguments.put( "-linksource", EMPTY_STRING );
        defaultArguments.put( "-locale", UK.toString() );
        defaultArguments.put( "-notimestamp", EMPTY_STRING );
        defaultArguments.put( "-quiet", EMPTY_STRING );
        defaultArguments.put( "-serialwarn", EMPTY_STRING );
        defaultArguments.put( "-source", Integer.toString( SourceVersion.latest().ordinal() ) );
        defaultArguments.put( "-sourcetab", "4" );
        defaultArguments.put( "-splitindex", EMPTY_STRING );
        defaultArguments.put( "-use", EMPTY_STRING );
        defaultArguments.put( "-version", EMPTY_STRING );
        //defaultArguments.put( "-J--enable-preview", EMPTY_STRING );
        m_DefaultArguments = Map.copyOf( defaultArguments );

        final Map<String,String> replaceableArguments = new TreeMap<>();
        replaceableArguments.put( "-bottom", format( "Copyright © 2002-%s by Thomas Thrien (tquadrat.org)", Year.now() ) );
        replaceableArguments.put( "-d", "./doc" );
        replaceableArguments.put( "--expand-requires", "all" );
        replaceableArguments.put( "-footer", " " );
        replaceableArguments.put( "-header", " " );
        replaceableArguments.put( "--show-members", "private" );
        replaceableArguments.put( "--show-module-contents", "all" );
        replaceableArguments.put( "--show-packages", "all" );
        replaceableArguments.put( "--show-types", "private" );
        m_ReplaceableArguments = Map.copyOf( replaceableArguments );

        m_Tags = List.of
        (
            //---* Register the Taglets *--------------------------------------
            "-taglet " + AuthorTaglet.class.getName(),
            "-taglet " + AnchorTaglet.class.getName(),
            "-taglet " + HRefTaglet.class.getName(),
            "-taglet " + IgnoreTaglet.class.getName(),
            "-taglet " + IncludeTaglet.class.getName(),
            "-taglet " + InspiredTaglet.class.getName(),
            "-taglet " + ModifiedTaglet.class.getName(),
            "-taglet " + NoteTaglet.class.getName(),
            "-taglet " + ThanksTaglet.class.getName(),
            "-taglet " + ToDoTaglet.class.getName(),
            "-taglet " + UmlGraphLinkTaglet.class.getName(),
            "-taglet " + UnderlineTaglet.class.getName(),

            //---* Define the sequence *---------------------------------------
            "-tag " + NoteTaglet.TAGLET_NAME,
            "-tag param",
            "-tag return",
            "-tag throws",
            "-tag author",
            "-tag " + AuthorTaglet.TAGLET_NAME,
            "-tag " + ThanksTaglet.TAGLET_NAME,
            "-tag " + ModifiedTaglet.TAGLET_NAME,
            "-tag version",
            "-tag since",
            "-tag see",
            "-tag " + InspiredTaglet.TAGLET_NAME,
            "-tag " + UmlGraphLinkTaglet.TAGLET_NAME,
            "-tag " + ToDoTaglet.TAGLET_NAME
        );

        //---* The links *-----------------------------------------------------
        try
        {
            m_Links.add( new URL( "https://docs.oracle.com/en/java/javase/15/docs/api/" ) );
            m_Links.add( new URL( "https://apiguardian-team.github.io/apiguardian/docs/current/api/" ) );
        }
        catch( final MalformedURLException e )
        {
            //---* Should never happen *---------------------------------------
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code JavadocStarter} instance.
     *
     *  @param  toolProvider    The tool provider for the Javadoc tool.
     */
    private JavadocStarter( final ToolProvider toolProvider )
    {
        m_ToolProvider = toolProvider;
    }   //  JavadocStarter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Does the program's work.
     *
     *  @param  args    The command line arguments.
     *  @throws IOException A problem occurred when determine the project
     *      location.
     */
    public final void execute( final String... args ) throws IOException
    {
        final var optionsFile = new File( requireNotEmptyArgument( args, "args" )[0] ).getAbsoluteFile();
        if( !optionsFile.exists() ) throw new FileNotFoundException( optionsFile.getAbsolutePath() );

        final List<String> localArgs = new ArrayList<>();
        final var currentOptions = loadOptions( optionsFile );

        out.println( "Options loaded" );

        m_Tags.stream()
            .map( t -> splitString( t, ' ' ) )
            .map( List::of )
            .forEach( localArgs::addAll );

        m_Links.stream()
            .map( link -> List.of( "-link", link.toExternalForm() ) )
            .forEach( localArgs::addAll );

        currentOptions.forEach( (k,v) ->
        {
            if( k.startsWith( "-" ) )
            {
                localArgs.add( k );
            }
            if( !v.isEmpty() )
            {
                if( v.isBlank() )
                {
                    localArgs.add( "\"\"" );
                }
                else
                {
                    localArgs.add( v );
                }
            }
        } );

        //---* Show the command line *-----------------------------------------
        out.println( localArgs.stream().collect( joining( " ", "javadoc ", EMPTY_STRING ) ) );

        //---* Execute the tool *----------------------------------------------
        if( m_ToolProvider.run( out, err, localArgs.toArray( EMPTY_String_ARRAY ) ) == 0 )
        {

            //---* Write the logo file *---------------------------------------
            final var targetFolder = new File( currentOptions.get( "-d" ), "resources" );
            writeLogo( targetFolder );
        }
    }   //  execute()

    /**
     *  Joins lines in the options file that ends with a backslash with the
     *  next line and return the new index.
     *
     *  @param  lines   The lines from the options file.
     *  @param  index   The current index.
     *  @param  currentLine The contents for the current line.
     *  @return The new index.
     */
    private final int joinLines( final String[] lines, final int index, final StringBuilder currentLine )
    {
        var retValue = index;
        var lastChar = currentLine.length() - 1;
        while( (retValue < lines.length) && (currentLine.charAt( lastChar ) == '\\') )
        {
            currentLine.setLength( lastChar );
            currentLine.append( lines [++retValue].trim() );
            lastChar = currentLine.length() - 1;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinLines()

    /**
     *  Reads the options file and returns the options from it.<br>
     *  <br>The source files and package names will get the key
     *  &quot;ARGUMENT&lt;<i>#</i>&gt;&quot; where <i>#</i> is a counter.
     *
     *  @param  optionsFile The options file.
     *  @return The options from the file, or from
     *      {@link #m_ReplaceableArguments}.
     *  @throws IOException A problem occurred when reading the options file.
     */
    private final Map<String,String> loadOptions( final File optionsFile ) throws IOException
    {
        final var lines = Files.readAllLines( optionsFile.toPath(), UTF_8 ).toArray( String[]::new );
        var counter = 0;
        var index = 0;
        final Map<String,String> retValue = new TreeMap<>( m_DefaultArguments );
        retValue.putAll( m_ReplaceableArguments );
        retValue.put( "-d", optionsFile.getParent() );

        ReadLoop: while( index < lines.length )
        {
            final var currentLine = new StringBuilder( lines [index].trim() );
            if( currentLine.isEmpty() || (currentLine.charAt( 0 ) == '#') )
            {
                //---* Comment *-----------------------------------------------
                ++index;
                continue ReadLoop;
            }

            if( currentLine.charAt( 0 ) == '-' )
            {
                index = joinLines( lines, index, currentLine );

                //---* An option *---------------------------------------------
                final var pos = currentLine.indexOf( " " );
                if( pos < 0 )
                {
                    //---* Option without argument *---------------------------
                    retValue.put( currentLine.toString(), EMPTY_STRING );
                }
                else
                {
                    //---* Option with an argument *---------------------------
                    final var key = currentLine.substring( 0, pos );
                    final var value = currentLine.substring( pos ).trim();
                    retValue.put( key, value );
                }
                ++index;
                continue ReadLoop;
            }

            index = joinLines( lines, index, currentLine ) + 1;
            retValue.put( format( "ARGUMENT<%d>", ++counter ), currentLine.toString() );
        }   //  ReadLoop:

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadOptions()

    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    public static void main( final String... args )
    {
        try
        {
            ToolProvider.findFirst( "javadoc" )
                .ifPresentOrElse( toolProvider ->
                {
                    final var application = new JavadocStarter( toolProvider );
                    try
                    {
                        application.execute( args );
                    }
                    catch( final EmptyArgumentException | IOException e )
                    {
                        throw new LambdaContainerException( e );
                    }
                },
                () -> { throw new ApplicationError( "Cannot find Tool 'javadoc'" ); } );
        }
        catch( final LambdaContainerException e )
        {
            if( e.checkIfExpected( EmptyArgumentException.class ) )
            {
                err.println( "No options file provided" );
            }
            else if( e.checkIfExpected( FileNotFoundException.class ) )
            {
                err.printf( "Options file '%s' not found%n", e.getCause().getMessage() );
            }
            else
            {
                e.getCause().printStackTrace( err );
            }
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()

    /**
     *  Writes the logo to the given folder.
     *
     *  @param  targetFolder    The destination folder.
     *  @throws IOException Cannot write the logo file.
     */
    private final void writeLogo( final File targetFolder ) throws IOException
    {
        final var logoResource = getClass().getResource( "resources/" + LOGO );
        if( isNull( logoResource ) ) throw new ApplicationError( format( "Resource '%s' is missing", LOGO ) );
        final var outputFile = new File( targetFolder, LOGO ).getAbsoluteFile().toPath();
        try( final var inputStream = logoResource.openStream() )
        {
            copy( inputStream, outputFile, REPLACE_EXISTING );
        }
    }   //  writeLogo()
}
//  class JavadocStarter

/*
 *  End of File
 */