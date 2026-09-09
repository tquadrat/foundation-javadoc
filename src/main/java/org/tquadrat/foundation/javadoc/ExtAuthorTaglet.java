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

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary This taglet was meant as a replacement for the standard
 *  {@code @author} taglet when &quot;overwriting&quot; does not work.} For the
 *  details, refer to
 *  {@link AuthorTaglet}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ExtAuthorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.25.2
 */
@ClassVersion( sourceVersion = "$Id: ExtAuthorTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.25.2" )
public final class ExtAuthorTaglet extends AuthorTaglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "extauthor";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code ExtAuthorTaglet} instance.}</p>
     */
    public ExtAuthorTaglet()
    {
        super( TAGLET_NAME );
    }   //  ExtAuthorTaglet()
}
//  class ExtAuthorTaglet

/*
 *  End of File
 */