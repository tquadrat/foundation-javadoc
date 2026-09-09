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
import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.util.Objects.nonNull;
import static jdk.javadoc.doclet.Taglet.Location.MODULE;
import static jdk.javadoc.doclet.Taglet.Location.PACKAGE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import javax.lang.model.element.Element;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

import org.apiguardian.api.API;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.JavadocError;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary With this taglet, it is possible to add a list of open points
 *  to the <i>documentation</i> of a module or a package.} The parameter of the
 *  tag is the (absolute) path to a text file in Markdown format; this path
 *  will be combined with the path provided by the
 *  {@linkplain System#getProperty(String,String) system property}
 *  {@value #PROPERTY_TODO_BASE}
 *  (if any).</p>
 *  <p>In the file, each open point consists of a sequence of lines that will
 *  end with an empty line (a <i>paragraph</i> in Markdown). HTML tags are
 *  taken as they are, Javadoc tags will be not interpreted.</p>
 *  <p>So the {@code module-info.java} file for a project may look like
 *  this:</p>
 *  <pre><code> &#47;**
 *  * &hellip;
 *  *
 *  * &#64;todo /task.list
 *  *&#47;
 *  module my.module
 *  {
 *      requires java.base;
 *      requires &hellip;
 *
 *      exports &hellip;
 *  }</code></pre>
 *  <p>With {@value #PROPERTY_TODO_BASE} set to
 *  &quot;{@code /home/programmer/project/}&quot;, the file will be searched at
 *  &quot;{@code /home/programmer/project/task.list}&quot;.</p>
 *  <p>Given the contents of that file looks like this:</p>
 *  <pre><code>  Cleanup the class comments
 *
 *  Re-think the parser implementation</code></pre>
 *  <p>the generated Module description will look like this:</p>
 *  <table border="1">
 *    <caption>Sample JavaDoc output</caption>
 *    <tr>
 *      <td>
 *        <div class="header">
 *          <h2 class="title">Module&nbsp;my.module</h2>
 *        </div>
 *        <div class="contentContainer">
 *          <div class="block">&hellip;</div>
 *          <dl>
 *            <dt>
 *              <span class="simpleTagLabel">Open Issues (The <i>ToDo</i>
 *                List):</span>
 *            </dt>
 *            <dd>
 *              <p>Clean-up the class comments</p>
 *              <p>Re-think the parser implementation</p>
 *            </dd>
 *          </dl>
 *          <div class="block">&hellip;</div>
 *        </div>
 *      </td>
 *    <tr>
 *  </table>
 *
 *  <p>With Maven or Gradle, the base path can be easily set to the project
 *  root.</p>
 *
 *  <p>The file is interpreted as Markdown since version 0.2.0, before it was a
 *  plain text file.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ToDoTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: ToDoTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class ToDoTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of the property that holds the base path for the
     *  todo list: {@value}.}</p>
     *  <p>It will be set on the {@code javadoc} command line like this:</p>
     *  &quot;<code>-J-Dorg.tquadrat.foundation.todo.base=&hellip;</code>&quot;
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String PROPERTY_TODO_BASE = "org.tquadrat.foundation.todo.base";

    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "todo";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The base path for the todo file.}</p>
     */
    private File m_BasePath;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code ToDoTaglet} instance.}</p>
     */
    public ToDoTaglet()
    {
        super( TAGLET_NAME, false, MODULE, PACKAGE );
    }   //  ToDoTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final void customInit()
    {
        m_BasePath = new File( getProperty( PROPERTY_TODO_BASE, "/" ) );
    }   //  customInit()

    /**
     *  <p>{@summary Parses the given file as a Markdown files and adds the
     *  result to the provided
     *  {@link StringBuilder}.}</p>
     *
     *  @param  file    The file with the todo list.
     *  @param  buffer  The destination for the output.
     */
    private static final void processFile( final File file, final StringBuilder buffer )
    {
        final var fileContents = new StringJoiner( "\n" );
        try( final var reader = new BufferedReader( new FileReader( file ) ) )
        {
            var line = EMPTY_STRING;
            ReadLineLoop: while( nonNull( line = reader.readLine() ) )
            {
                //---* Skip comments *-------------------------------------
                if( line.startsWith( "#" ) ) continue ReadLineLoop;

                fileContents.add( line );
            }   //  ReadLineLoop:
        }
        catch( final FileNotFoundException e )
        {
            //---* Should not happen … *---------------------------------------
            throw new JavadocError( format( "File '%s' does not exist", file.getName() ), e );
        }
        catch( final IOException e )
        {
            throw new JavadocError( format( "Problems on processing '%s'", file.getAbsolutePath() ), e );
        }

        //---* Get the Markdown Parser and Renderer *--------------------------
        final var parser = Parser.builder()
            .build();
        final var renderer = HtmlRenderer.builder()
            .build();

        //---* Parse the input and render it to HTML *-------------------------
        final var document = parser.parse( fileContents.toString() );
        buffer.append( renderer.render( document ) );
    }   //  processFile()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var prefixLen = getName().length() + 1;

        var retValue = EMPTY_STRING;
        final var buffer = new StringBuilder();
        tags.stream()
            .map( Object::toString )
            .map( t -> t.substring( prefixLen ).trim() )
            .map( t -> new File( m_BasePath, t ) )
            .filter( file ->
            {
                if( file.exists() ) return true;
                out.printf( "Todo List '%s' not found%n", file.getAbsolutePath() );
                return false;
            } )
            .forEach( file -> processFile( file, buffer ) );

        if( !buffer.isEmpty() )
        {
            final var template =
                """

                <dt><span class="simpleTagLabel">Open Issues (The <i>ToDo</i> List):</span></dt>
                <dd>
                   %s
                </dd>
                """;

            retValue = format( template, buffer );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ToDoTaglet

/*
 *  End of File
 */