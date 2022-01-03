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

package org.tquadrat.foundation.javadoc.umlgraph;

import static org.apiguardian.api.API.Status.INTERNAL;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The dimensions for a
 *  {@link org.tquadrat.foundation.javadoc.umlgraph.TypeSymbol}
 *  in user units.
 *
 *  @param  width   The width.
 *  @param  height  The height.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Dimension.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"hiding", "javadoc"} )
@ClassVersion( sourceVersion = "$Id: Dimension.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final record Dimension( double width, double height )
{ /* Empty */ }
//  record Dimension

/*
 *  End of File
 */