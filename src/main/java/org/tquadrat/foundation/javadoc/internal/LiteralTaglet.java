/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.escapeHTML;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary Re-implementation of the <code>{&#64;literal}</code>
 *  taglet.}</p>
 *  <p>Unfortunately, it is not possible to access the original
 *  <code>{&#64;literal}</code> taglet to process any tags inside a custom tag.
 *  That's why I created this replacement.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LiteralTaglet.java 978 2022-01-06 12:47:52Z tquadrat $
 *  @since 0.0.5
 *
 *  @see CodeTaglet
 *  @see IndexTaglet
 */
@ClassVersion( sourceVersion = "$Id: LiteralTaglet.java 978 2022-01-06 12:47:52Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5")
public final class LiteralTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "literal";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern for the tag.
     */
    private static final Pattern PATTERN;

    static
    {
        try
        {
            //noinspection RegExpRedundantEscape
            PATTERN = Pattern.compile( "\\{@" + TAGLET_NAME + " (?<contents>.*)\\}" );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LiteralTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public LiteralTaglet() { /* Just exists */ }

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
    public final String getName() { return TAGLET_NAME; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isInlineTag() { return true; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<? extends DocTree> tags, final Element element )
    {
        final var tag = tags.get( 0 ).toString();
        final var matcher = PATTERN.matcher( tag );
        final var contents = matcher.matches() ? matcher.group( "contents" ) : EMPTY_STRING;
        final var retValue = isNotEmptyOrBlank( contents ) ? format( "%1$s", escapeHTML( contents ) ) : EMPTY_STRING;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class LiteralTaglet

/*
 *  End of File
 */