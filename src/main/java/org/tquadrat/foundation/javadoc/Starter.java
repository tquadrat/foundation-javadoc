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

import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.splitString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.spi.ToolProvider;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.ProgramClass;
import org.tquadrat.foundation.exception.ApplicationError;
import org.tquadrat.foundation.exception.LambdaContainerException;

/**
 *  Executes the {@code JavaDoc} tool with a bunch of default parameters.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Starter.java 821 2020-12-31 00:05:27Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 *
 *  @deprecated This program was replaced by
 *      {@link JavadocStarter}.
 */
@ProgramClass
@ClassVersion( sourceVersion = "$Id: Starter.java 821 2020-12-31 00:05:27Z tquadrat $" )
@API( status = DEPRECATED, since = "0.0.5" )
@Deprecated( since = "0.1.0", forRemoval = true )
public final class Starter
{
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
        final Map<String,String> defaultArguments = new TreeMap<>();
        defaultArguments.put( "-author", null );
        defaultArguments.put( "-charset", "\"UTF-8\"" );
        defaultArguments.put( "-docfilessubdirs", null );
        defaultArguments.put( "-encoding", "UTF-8" );
        defaultArguments.put( "--expand-requires", "all" );
        defaultArguments.put( "--frames", null );
        defaultArguments.put( "-html5", null );
        defaultArguments.put( "-javafx", null );
        defaultArguments.put( "-keywords", null );
        defaultArguments.put( "-link", "https://doc.oracle.com/javase/15/docs/api/" );
        defaultArguments.put( "-linksource", null );
        defaultArguments.put( "-serialwarn", null );
        defaultArguments.put( "-source", "10" );
        defaultArguments.put( "-sourcetab", "4" );
        defaultArguments.put( "-splitindex", null );
        defaultArguments.put( "-use", null );
        defaultArguments.put( "-version", null );
        defaultArguments.put( "-J-Duser.language=en", null );
        /*
         * We cannot use Map.copyOf() here because some of the values are null.
         */
        m_DefaultArguments = unmodifiableMap( defaultArguments );

        final Map<String,String> replaceableArguments = new TreeMap<>();
        replaceableArguments.put( "-d", "./doc" );
        replaceableArguments.put( "--show-members", "private" );
        replaceableArguments.put( "--show-module-contents", "all" );
        replaceableArguments.put( "--show-packages", "all" );
        replaceableArguments.put( "--show-types", "private" );
        replaceableArguments.put( "-locale", "en_GB" );
        m_ReplaceableArguments = Map.copyOf( replaceableArguments );

        m_Tags = List.of
        (
            "-taglet org.tquadrat.javadoc.AnchorTaglet",
            "-taglet org.tquadrat.javadoc.HRefTaglet",
            "-taglet org.tquadrat.javadoc.UnderlineTaglet",
            "-taglet org.tquadrat.javadoc.NoteTaglet",
            "-tag param",
            "-tag return",
            "-tag throws",
            "-tag author",
            "-taglet org.tquadrat.javadoc.AuthorTaglet",
            "-taglet org.tquadrat.javadoc.ThanksTaglet",
            "-taglet org.tquadrat.javadoc.ModifiedTaglet",
            "-tag version",
            "-tag since",
            "-tag see",
            "-taglet org.tquadrat.javadoc.InspiredTaglet",
            "-taglet org.tquadrat.javadoc.UmlGraphLinkTaglet",
            "-taglet org.tquadrat.javadoc.ToDoTaglet"
        );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Starter} instance.
     *
     *  @param  toolProvider    The tool provider.
     */
    private Starter( final ToolProvider toolProvider )
    {
        m_ToolProvider = toolProvider;
    }   //  Starter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Does the program's work.
     *
     *  @param  cmdLineArgs The command line arguments.
     *  @throws IOException A problem occurred when determine the project
     *      location.
     */
    public final void execute( final Collection<String> cmdLineArgs ) throws IOException
    {
        final List<String> localArgs = new ArrayList<>();

        final Map<String,String> replaceableArguments = new TreeMap<>( m_ReplaceableArguments );
        for( final var arg : cmdLineArgs ) replaceableArguments.remove( arg );

        m_DefaultArguments.forEach( (k,v) ->
        {
            localArgs.add( k );
            if( isNotEmptyOrBlank( v ) ) localArgs.add( v );
        } );

        replaceableArguments.forEach( (k,v) ->
        {
            localArgs.add( k );
            localArgs.add( v );
        } );

        m_Tags.stream()
            .map( t -> splitString( t, ' ' ) )
            .map( List::of )
            .forEach( localArgs::addAll );

        localArgs.addAll( cmdLineArgs );

        //---* Show the command line *-----------------------------------------
        out.println( localArgs.stream().collect( joining( " ", "javadoc ", EMPTY_STRING ) ) );

        //---* Execute the tool *----------------------------------------------
        m_ToolProvider.run( out, err, localArgs.toArray( EMPTY_String_ARRAY ) );
    }   //  execute()

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
                .ifPresentOrElse( t ->
                {
                    final var application = new Starter( t );
                    try
                    {
                        application.execute( asList( args ) );
                    }
                    catch( final IOException e )
                    {
                        throw new LambdaContainerException( e );
                    }
                },
                () -> { throw new ApplicationError( "Cannot find Tool 'javadoc'" ); } );
        }
        catch( final LambdaContainerException e )
        {
            e.getCause().printStackTrace( err );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()
}
//  class Starter

/*
 *  End of File
 */