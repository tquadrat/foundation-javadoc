/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.tquadrat.foundation.javadoc.umlgraph.UMLDocument.UML_CSS;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  For some reasons the {@code uml.css} resource file with the CSS definitions
 *  was not found.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: BugHunt_20201229_001.java 976 2022-01-06 11:39:58Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.javadoc.BugHunt_20201229_001" )
public class BugHunt_20201229_001 extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Retrieves the resource {@code uml.css}, stored in the package
     *  {@code org.tquadrat.foundation.javadoc}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void retrieveResource() throws Exception
    {
        skipThreadTest();

        final var cssResource = UmlGraphLinkTaglet.class.getResource( UML_CSS );
        assertNotNull( cssResource );
        out.printf( "Resource URL for '%s': %s", UML_CSS, cssResource );
    }   //  retrieveResource()
}
//  class BugHunt_20201229_001

/*
 *  End of File
 */