/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.lang.System.setProperty;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.IncludeTaglet.PROPERTY_INCLUDE_ROOT_PREFIX;
import static org.tquadrat.foundation.javadoc.ToDoTaglet.PROPERTY_TODO_BASE;

import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ProgramClass;
import org.tquadrat.foundation.javadoc.internal.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  If a program will be started from IntelliJ IDEA, the {@code args} argument
 *  to
 *  {@link #main(String...)}
 *  has always values.
 *
 *  @version $Id: IdeaStarter.java 977 2022-01-06 11:41:03Z tquadrat $
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 */
@ProgramClass
@ClassVersion( sourceVersion = "$Id: IdeaStarter.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class IdeaStarter
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private IdeaStarter() { throw new PrivateConstructorForStaticClassCalledError( IdeaStarter.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     * The program entry point.
     *
     * @param args The command line arguments.
     */
    @SuppressWarnings( "CallToPrintStackTrace" )
    public static final void main( final String... args )
    {
        //noinspection TryWithIdenticalCatches
        try
        {
            final var currentDir = new File( "." ).getCanonicalFile().getAbsoluteFile();
            out.printf( "Current Directory: %s%n", currentDir.getAbsolutePath() );
            final var workDir = new File( currentDir, "org.tquadrat.foundation.javadoc" ).getCanonicalFile().getAbsoluteFile();
            out.printf( "Work Directory   : %s%n", workDir.getAbsolutePath() );

            var rootDirKey = format( "%s.%s", PROPERTY_INCLUDE_ROOT_PREFIX, "source" );
            setProperty( rootDirKey, new File( currentDir, "org.tquadrat.foundation.showcase/src/main/java" ).getAbsolutePath() );

            rootDirKey = format( "%s.%s", PROPERTY_INCLUDE_ROOT_PREFIX, "javadoc" );
            setProperty( rootDirKey, new File( currentDir, "org.tquadrat.foundation.showcase/src/main/javadoc" ).getAbsolutePath() );

            rootDirKey = format( "%s.%s", PROPERTY_INCLUDE_ROOT_PREFIX, "resources" );
            setProperty( rootDirKey, new File( currentDir, "org.tquadrat.foundation.showcase/src/main/resources" ).getAbsolutePath() );

            setProperty( PROPERTY_TODO_BASE, new File( currentDir, "org.tquadrat.foundation.showcase" ).getAbsolutePath() );
            JavadocStarter.main( new File( workDir, "data/javadoc.options" ).getCanonicalFile().getAbsolutePath() );
        }
        catch( final IllegalFormatException | IOException e )
        {
            e.printStackTrace( err );
        }
        catch( final Throwable t )
        {
            //---* Handle previously unhandled exceptions *--------------------
            t.printStackTrace( err );
        }
    }  //  main()
}
//  class IdeaStarter

/*
 *  End of File
 */