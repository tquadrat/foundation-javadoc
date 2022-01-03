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

package org.tquadrat.foundation.javadoc.umlgraph;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The relevant stereotypes.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Stereotype.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Stereotype.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public enum Stereotype
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  A regular class.
     */
    CLASS( EMPTY_STRING ),

    /**
     *  An abstract class.
     */
    TYPE( "«Type»" ),

    /**
     *  An interface
     */
    INTERFACE( "«Interface»" ),

    /**
     *  An annotation
     */
    ANNOTATION( "«Annotation»" ),

    /**
     *  A utility class (a static class, a class with only static methods).
     */
    UTILITY( "«Utility»" ),

    /**
     *  An enum type.
     */
    ENUM( "«Enum»" ),

    /**
     *  An exception.
     */
    EXCEPTION( "«Exception»" ),

    /**
     *  An error.
     */
    ERROR( "«Error»" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The textual representation of the stereotype.
     */
    private final String m_Text;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Stereotype} instance.
     *
     *  @param  text    The text that should be returned on a call to
     *      {@link #toString()}
     */
    private Stereotype( final String text )
    {
        m_Text = text;
    }   //  Stereotype()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return m_Text; }
}
//  enum Stereotype

/*
 *  End of File
 */