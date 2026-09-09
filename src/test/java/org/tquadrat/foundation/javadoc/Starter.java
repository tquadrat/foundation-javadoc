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

import static java.io.File.pathSeparatorChar;
import static java.lang.String.join;
import static java.lang.System.err;
import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.lang.System.setProperty;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.UK;
import static org.tquadrat.foundation.javadoc.internal.OtherFileTagletBase.PROPERTY_INCLUDE_ROOT_PREFIX;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.spi.ToolProvider;

import org.tquadrat.foundation.javadoc.internal.ToolKit;

/**
 *  <p>{@summary Starts the Javadoc tool for testing purposes.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "UtilityClass" )
public final class Starter
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of the Javadoc tool: {@value}.}</p>
     */
    public static final String TOOL_NAME = "javadoc";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private Starter() { throw new AssertionError( "Illegal call to constructor" ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Builds the taglet path.}</p>
     *
     *  @param  classpathEntries    The {@code CLASSPATH} entries.
     *  @param  currentFolder   The current folder.
     *  @return The taglet path entry.
     */
    private static final List<String> buildTagletPath( final List<String> classpathEntries, final Path currentFolder )
    {
        final var apiguardian = classpathEntries.stream()
            .filter( v -> v.contains( "apiguardian" ) )
            .findFirst()
            .orElseThrow( () -> new IllegalStateException( "No apiguardian on the CLASSPATH" ) );

        final var activation = classpathEntries.stream()
            .filter( v -> v.contains( "angus" ) )
            .findFirst()
            .orElseThrow( () -> new IllegalStateException( "No activation on the CLASSPATH" ) );

        final List<String> path = List.of( apiguardian, activation, currentFolder.resolve( "build", "classes", "java", "main" ).toString() );
        final var retValue = List.of( "-tagletpath", join( File.pathSeparator, path ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  buildTagletPath()

    /**
     *  <p>{@summary The program entry point.}</p>
     *
     *  @param  args    The command line arguments.
     */
    @SuppressWarnings( "unused" )
    public static final void main( final String... args )
    {
        try
        {
            final List<String> classpathEntries = retrieveClasspathEntries();
            final var currentFolder = Path.of( "." ).toAbsolutePath().normalize();
            final var inputFolder = currentFolder.resolve( "input" );
            final var sourceFolder = inputFolder.resolve( "src" );
            final var includeFolder = inputFolder.resolve( "includes" );
            final var rootPackage = "org.tquadrat.javadoc.playground";
            final var encoding = UTF_8;

            //---* Load the Javadoc tool *-------------------------------------
            final var javadocTool = ToolProvider.findFirst( "javadoc" )
                .orElseThrow( () -> new AssertionError( "Cannot find tool '%s'".formatted( TOOL_NAME ) ) );
            out.printf( """
                Class ........: %1$s
                Name .........: %2$s
                Description ..: %3$s
                Current Folder: %4$s
                Input Folder .: %5$s
                Source Folder : %6$s
                Include Folder: %7$s
                Encoding .....: %8$s
                
                Root Package .: %9$s
                """
                .formatted(
                    javadocTool.getClass().getName(),
                    javadocTool.name(),
                    javadocTool.description().orElse( "n/a" ),
                    currentFolder.toString(),
                    inputFolder.toString(),
                    sourceFolder.toString(),
                    includeFolder.toString(),
                    encoding.displayName(),
                    rootPackage ) );

            //classpathEntries.forEach( out::println );

            //---* Run the Javadoc tool *--------------------------------------
            final List<String> arguments = new ArrayList<>();

            final var locale = UK;
            Locale.setDefault( locale );
            setProperty( "user.language", locale.getLanguage() );
            setProperty( "user.country", locale.getCountry() );
            setProperty( "%1$s.%2$s".formatted( PROPERTY_INCLUDE_ROOT_PREFIX, "sources" ), sourceFolder.toString() );
            setProperty( "%1$s.%2$s".formatted( PROPERTY_INCLUDE_ROOT_PREFIX, "includes" ), includeFolder.toString() );
            setProperty( "%1$s.%2$s".formatted( PROPERTY_INCLUDE_ROOT_PREFIX, "tagletSources" ), currentFolder.resolve( "src", "main", "java" ).toString() );

            arguments.add( "-quiet" );
            arguments.addAll( List.of( "-d", currentFolder.resolve( "output" ).toString() ) );
            arguments.addAll( List.of( "--source-path", sourceFolder.toString() ) );

            arguments.addAll( List.of( "-locale", locale.toString() ) );
            arguments.addAll( List.of( "--source", "25" ) );
            arguments.add( "--enable-preview" );
            arguments.add( "--syntax-highlight" );

            arguments.addAll( List.of( "-encoding", encoding.toString() ) );
            arguments.addAll( List.of( "-charset", encoding.toString() ) );
            arguments.addAll( List.of( "-docencoding", encoding.toString() ) );

            arguments.addAll( List.of( "-overview", inputFolder.resolve( "javadoc", "overview.html" ).toString() ) );
            arguments.add( "-docfilessubdirs" );

            //---* The tags *--------------------------------------------------
            arguments.addAll( buildTagletPath( classpathEntries, currentFolder ) );
            arguments.addAll( retrieveTagletArguments() );

            //---* Visibility *------------------------------------------------
            arguments.add( "-private" );
            arguments.addAll( List.of( "--show-members", "private" ) );
            arguments.addAll( List.of( "--show-module-contents", "all" ) );
            arguments.addAll( List.of( "--show-packages", "all" ) );
            arguments.addAll( List.of( "--show-types", "private" ) );
            arguments.addAll( List.of( "-subpackages", rootPackage ) );
            arguments.add( "-linksource" );
            arguments.add( "-use" );
            arguments.addAll( List.of( "-sourcetab", Integer.toString( 4 ) ) );

            //---* The packages and modules *----------------------------------
            arguments.add( rootPackage );

            out.printf( "Command Line .: %s%n%n", join( " ", arguments ) );
            final var result = javadocTool.run( out, err, arguments.toArray( String []::new ) );
            if( result == 0 )
            {
                out.println( "Done!" );
            }
        }
        catch( final Throwable t )
        {
            /*
             *  Handle previously not caught exceptions here!
             */
            t.printStackTrace( err );
        }
    }   //  main()

    /**
     *  <p>{@summary Get the {@code CLASSPATH} entries.}</p>
     *
     *  @return The entries on the {@code CLASSPATH} and the
     *      {@code MODULE-PATH}.
     */
    private static final List<String> retrieveClasspathEntries()
    {
        final Set<String> buffer = new HashSet<>();

        for( final var entry : ToolKit.splitString( getProperty( "java.class.path", EMPTY_STRING ), pathSeparatorChar ) )
        {
            if( !entry.isBlank() ) buffer.add( entry );
        }
        for( final var entry : ToolKit.splitString( getProperty( "jdk.module.path", EMPTY_STRING ), pathSeparatorChar ) )
        {
             if( !entry.isBlank() ) buffer.add( entry );
        }

        final var retValue = List.copyOf( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveClasspathEntries()

    /**
     *  Retrieves the custom taglet arguments.
     */
    private static final List<String> retrieveTagletArguments()
    {
        final Map<String,Class<?>> map = new LinkedHashMap<>();
        map.put( "*anchor", AnchorTaglet.class );
        map.put( "*false", FALSETaglet.class );
        map.put( "*href", HRefTaglet.class );
        map.put( "*ignore", IgnoreTaglet.class );
        map.put( "*image", ImageTaglet.class );
        map.put( "*include", IncludeTaglet.class );
        map.put( "*null", NULLTaglet.class );
        map.put( "*true", TRUETaglet.class );
        map.put( "*underline", UnderlineTaglet.class );

        map.put( "hidden", null );
        map.put( "note", NoteTaglet.class );
        map.put( "param", null );
        map.put( "return", null );
        map.put( "throws", null );
        map.put( "author",  AuthorTaglet.class );
        map.put( "extauthor", ExtAuthorTaglet.class );
        map.put( "thanks", ThanksTaglet.class );
        map.put( "modified", ModifiedTaglet.class );
        map.put( "version", null );
        map.put( "since", null );
        map.put( "see", null );
        map.put( "inspired", InspiredTaglet.class );
        map.put( "spec", null );
        map.put( "provides", null );
        map.put( "uses", null );
        map.put( "UMLGraph.link", UmlGraphLinkTaglet.class );
        map.put( "deprecated", null );
        map.put( "todo", ToDoTaglet.class );

        map.put( "apiNote:a:API Note:", null );
        map.put( "implSpec:a:Implementation Requirements:", null );
        map.put( "implNote:a:Implementation Note:", null );

        final List<String> buffer = new ArrayList<>();
        map.values()
            .stream()
            .filter( Objects::nonNull )
            .map( Class::getName)
            .forEach( v ->
            {
                buffer.add( "-taglet" );
                buffer.add( v );
            });
        map.keySet()
            .stream()
            .filter( v -> !v.startsWith( "*" ) )
            .forEach( v ->
            {
                buffer.add( "-tag" );
                buffer.add( v );
            });
        final var retValue = List.copyOf( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveTagletArguments()
}
//  class Starter

/*
 *  End of File
 */