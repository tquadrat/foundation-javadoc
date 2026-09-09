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
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.SimpleTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary The contents of this inline tag will be ignored for the
 *  generation of the Javadoc output.} Different from the
 *  &quot;<code>&#64;hidden</code>&quot; tag that excludes the whole comment
 *  block from the generated documentation, this tag will just exclude the part
 *  within the brackets.</p>
 *  <p>So for example, the sequence &quot;<i><code>{&#64;{@value %s #TAGLET_NAME}
 *  This text will not appear in the documentation}</code></i>&quot; would just not
 *  show up. This can be used to add a raw, human-readable description to the
 *  documentation for something that needs to be prepared in some special way
 *  for the documentation output that makes it nearly unreadable in the source
 *  code.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IgnoreTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: IgnoreTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class IgnoreTaglet extends SimpleTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "ignore";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code IgnoreTaglet} instance.}</p>
     */
    public IgnoreTaglet()
    {
        super( TAGLET_NAME, EMPTY_STRING );
    }   //  IgnoreTaglet()
}
//  class IgnoreTaglet

/*
 *  End of File
 */