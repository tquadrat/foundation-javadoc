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

package org.tquadrat.foundation.javadoc.internal;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import javax.lang.model.element.Element;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.FALSETaglet;
import org.tquadrat.foundation.javadoc.IgnoreTaglet;
import org.tquadrat.foundation.javadoc.NULLTaglet;
import org.tquadrat.foundation.javadoc.TRUETaglet;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;

/**
 *  <p>{@summary The base class for taglets that are just replaced with some
 *  constant output.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SimpleTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.25.1
 */
@ClassVersion( sourceVersion = "$Id: SimpleTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = INTERNAL, since = "0.25.1")
public sealed abstract class SimpleTagletBase extends CustomTagletBase
    permits FALSETaglet, IgnoreTaglet, NULLTaglet, TRUETaglet
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The output for the taglet.}</p>
     */
    private final String m_Output;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code SimpleTagletBase} instance.}</p>
     *
     *  @param  name    The name of the taglet.
     *  @param  output  The output for the taglet.
     */
    protected SimpleTagletBase( final String name, final String output )
    {
        super( name, true, Location.values() );
        m_Output = requireNonNullArgument( output, "output" );
    }   //  SimpleTagletBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element ) { return m_Output; }
}
//  class SimpleTagletBase

/*
 *  End of File
 */