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

import static java.lang.String.format;
import static java.lang.String.join;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.first;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.CustomTagletBase;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownInlineTagTree;

/**
 *  <p>{@summary This inline tag allows to underline a sequence of text.} For
 *  example, &quot;<code>{&#64;{@value %s #TAGLET_NAME} UNDERLINE ME}</code>&quot;
 *  would be shown as <u>UNDERLINE ME</u>.</p>
 *  <p>The tag does not escape HTML tags, while it will not regard most other
 *  inline Javadoc tags.</p>
 *  <p>The tag will not use
 *  &quot;<code>&lt;u&gt;&hellip;&lt;/u&gt;</code>&quot; but
 *  &quot;<code>&lt;span style='text-decoration: underline;'&gt;&hellip;&lt;/span&gt;</code>&quot;.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UnderlineTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UnderlineTaglet.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class UnderlineTaglet extends CustomTagletBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The name of this taglet: {@value}.}</p>
     */
    public static final String TAGLET_NAME = "underline";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code UnderlineTaglet} instance.}</p>
     */
    public UnderlineTaglet()
    {
        super( TAGLET_NAME, true, Location.values() );
    }   //  UnderlineTaglet()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final List<String> buffer = new ArrayList<>();
        for( final var tag : tags )
        {
            if( tag instanceof UnknownInlineTagTree inlineTagTree )
            {
                final var body = processTagContent( inlineTagTree.getContent(), element );
                final var contents = processTagContent( parseText( body ), element );
                buffer.add( isNotEmptyOrBlank( contents ) ? format( "<span style=\"text-decoration: underline;\">%1$s</span>", contents ) : EMPTY_STRING );
            }
            else
            {
                printf( ERROR, "Cannot process tag of '%s' (class '%s'): %s", tag.getKind().name(), tag.getClass().getName(), first( 20, tag.toString() ) );
            }
        }
        final var retValue = join( EMPTY_STRING, buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class UnderlineTaglet

/*
 *  End of File
 */