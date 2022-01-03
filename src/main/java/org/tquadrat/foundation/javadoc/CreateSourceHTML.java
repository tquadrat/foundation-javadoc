/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static java.lang.System.err;
import static java.lang.System.out;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.Common.createLineNumberFormatString;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.ProgramClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  A tool that can be used to create an HTML file suitable as a documentation
 *  extension for a JavaDoc from an arbitrary text file. The processing of the
 *  input is as for
 *  {@link IncludeTaglet.ProcessMode#SOURCE}. <br>
 *  <br>The program takes two command line arguments:
 *  <ol>
 *  <li>The file name for the input file.</li>
 *  <li>The destination path for the output file.</li>
 *  </ol>
 *  <br>The name of the output file will be determined by the name of the input
 *  file, suffixed with &quot;{@code .html}&quot;. This means, the input file
 *  &quot;{@code MyClass.java}&quot; is written to
 *  &quot;{@code MyClass.java.html}&quot;.<br>
 *  <br>An already existing file will be overwritten without warning.
 *
 *  @note The output file should be stored into a {@code doc-files} folder in
 *      the source tree of the project from where JavaDoc will pick it up and
 *      amends it appropriately. This is similar to the handling of the HTML
 *      file containing the project overview that is addressed with the
 *      {@code -overview} option on the JavaDoc command line.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CreateSourceHTML.java 825 2021-01-03 17:50:44Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ProgramClass
@ClassVersion( sourceVersion = "$Id: CreateSourceHTML.java 825 2021-01-03 17:50:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.5")
public final class CreateSourceHTML
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private CreateSourceHTML() { throw new PrivateConstructorForStaticClassCalledError( CreateSourceHTML.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Generates a documentation extension HTML file from the given source
     *  file.
     *
     *  @param  inputFile   The input file.
     *  @param  outputPath  The output path.
     *  @throws IOException Reading or writing failed.
     */
    @SuppressWarnings( "resource" )
    public static final void execute( final File inputFile, final File outputPath ) throws IOException
    {
        if ( !requireNonNullArgument( inputFile, "inputFile" ).exists() )
        {
            throw new FileNotFoundException( inputFile.getAbsolutePath() );
        }
        out.printf( "Input File : %s\n", inputFile.getAbsolutePath() );

        if( requireNonNullArgument( outputPath, "outputPath" ).exists() && !outputPath.isDirectory() )
        {
            throw new IOException( format( "%s is not a directory", outputPath.getAbsolutePath() ) );
        }
        if( !outputPath.exists() )
        {
            if( !outputPath.mkdirs() )
            {
                throw new IOException( format( "Cannot create output directory: %s", outputPath.getAbsolutePath() ) );
            }
        }

        final var fileName = inputFile.getName() + ".html";
        final var outputFile = new File( outputPath, fileName );
        out.printf( "Output File: %s\n", outputFile.getAbsolutePath() );

        final List<String> lines = new ArrayList<>();
        try( final var reader = new BufferedReader( new FileReader( inputFile ) ) )
        {
            reader.lines()
                .map( StringUtils::escapeHTML )
                .forEach( lines::add );
        }

        final var lineNumberFormat = createLineNumberFormatString( lines.size() );
        var lineNumber = 1;

        final var buffer = new StringBuilder(
            """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="UTF-8">
            <title>Source Code</title>
            </head>
            <body>
            <h1>Source Code</h1>
            <p><div class="source-container"><pre>""" );
        for( final var line : lines )
        {
            buffer.append( format( lineNumberFormat, lineNumber++ ) )
                .append( line )
                .append( '\n' );
        }
        buffer.append(
            """
            </pre></div></p>
            </body>
            </html>""" );

        try( final var writer = new FileWriter( outputFile ) )
        {
            writer.append( buffer ).flush();
        }

        out.println( "Done!" );
    }   //  execute()

    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    @SuppressWarnings( {"ThrowCaughtLocally", "OverlyBroadCatchBlock"} )
    public static void main( final String... args )
    {
        try
        {
            if( args.length != 2 )
            {
                throw new IllegalArgumentException( format( "Usage: %s <InputFile> <OutputPath>", CreateSourceHTML.class.getSimpleName() ) );
            }
            final var inputFile = new File( args [0] ).getCanonicalFile().getAbsoluteFile();
            final var outputPath = new File( args [1] ).getCanonicalFile().getAbsoluteFile();

            execute( inputFile, outputPath );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()
}
//  class CreateSourceHTML

/*
 *  End of File
 */