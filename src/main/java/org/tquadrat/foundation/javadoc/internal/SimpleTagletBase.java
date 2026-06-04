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

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary The base class for some taglets that are replaced with some
 *  constant output.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CodeTaglet.java 1165 2026-03-22 19:30:59Z tquadrat $
 *  @since 0.25.1
 */
@ClassVersion( sourceVersion = "$Id: CodeTaglet.java 1165 2026-03-22 19:30:59Z tquadrat $" )
@API( status = INTERNAL, since = "0.25.1")
public /*sealed*/ abstract class SimpleTagletBase implements Taglet
    /*permits FALSETaglet, NULLTaglet, TRUETaglet*/
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the taglet.
     */
    private final String m_Name;

    /**
     *  The output for the taglet.
     */
    private final String m_Output;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SimpleTagletBase} instance.
     *
     *  @param  name    The name of the taglet.
     *  @param  output  The output for the taglet.
     */
    protected SimpleTagletBase( final String name, final String output )
    {
        m_Name = name;
        m_Output = output;
    }   //  SimpleTagletBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Location> getAllowedLocations() { return EnumSet.allOf( Location.class ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getName() { return m_Name; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isInlineTag() { return true; }

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