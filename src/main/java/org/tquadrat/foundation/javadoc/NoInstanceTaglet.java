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

import static jdk.javadoc.doclet.Taglet.Location.CONSTRUCTOR;
import static jdk.javadoc.doclet.Taglet.Location.METHOD;
import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.SimpleTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary Provides the standard documentation for the private
 *  constructor of a static class.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NoInstanceTaglet.java 1291 2026-10-15 19:27:02Z tquadrat $
 *  @since 0.25.5
 */
@ClassVersion( sourceVersion = "$Id: NoInstanceTaglet.java 1291 2026-10-15 19:27:02Z tquadrat $" )
@API( status = STABLE, since = "0.25.5")
public final class NoInstanceTaglet extends SimpleTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "noInstance";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code NoInstanceTaglet} instance.}</p>
     */
    public NoInstanceTaglet()
    {
        /*
         * For some reason, Javadoc thinks that (some) constructors are just
         * methods. There I added METHOD to the allowed locations.
         */
        super( TAGLET_NAME, "No instance allowed for this class!", CONSTRUCTOR, METHOD );
    }   //  NoInstanceTaglet()
}
//  class NoInstanceTaglet

/*
 *  End of File
 */