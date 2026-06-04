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
import org.tquadrat.foundation.javadoc.internal.SimpleTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary Replaces {&#64;null} with
 *  <code>&lt;code&gt;null&lt;/code&gt;</code>.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CodeTaglet.java 1165 2026-03-22 19:30:59Z tquadrat $
 *  @since 0.25.1
 */
@ClassVersion( sourceVersion = "$Id: CodeTaglet.java 1165 2026-03-22 19:30:59Z tquadrat $" )
@API( status = STABLE, since = "0.25.1")
public final class NULLTaglet extends SimpleTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "null";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code NULLTaglet} instance.
     */
    public NULLTaglet()
    {
        super( TAGLET_NAME, "<code>%s</code>".formatted( TAGLET_NAME ) );
    }   //  NULLTaglet()
}
//  class NULLTaglet

/*
 *  End of File
 */