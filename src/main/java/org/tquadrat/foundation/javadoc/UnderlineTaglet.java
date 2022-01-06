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

package org.tquadrat.foundation.javadoc;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.Common.initHelperTaglets;
import static org.tquadrat.foundation.javadoc.internal.Common.processInlineTags;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmptyOrBlank;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

/**
 *  This inline tag allows to underline a sequence of text. For example,
 *  &quot;<code>{&#64;{@value #TAGLET_NAME} UNDERLINE ME}</code>&quot; would be
 *  shown as <u>UNDERLINE ME</u>.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UnderlineTaglet.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@ClassVersion( sourceVersion = "$Id: UnderlineTaglet.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public class UnderlineTaglet implements Taglet
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of this taglet: {@value}.
     */
    public static final String TAGLET_NAME = "underline";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The doclet.
     */
    @SuppressWarnings( {"unused", "FieldCanBeLocal"} )
    private Doclet m_Doclet;

    /**
     *  The doclet environment.
     */
    private DocletEnvironment m_DocletEnvironment;

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
     *  Creates a new {@code UnderlineTaglet} instance.
     */
    @SuppressWarnings( "RedundantNoArgConstructor" )
    public UnderlineTaglet() { /* Just exists */ }

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
    public final void init( final DocletEnvironment docletEnvironment, final Doclet doclet )
    {
        Taglet.super.init( docletEnvironment, doclet );
        m_Doclet = doclet;
        m_DocletEnvironment = docletEnvironment;
        initHelperTaglets( docletEnvironment, doclet );
    }   //  init()

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
        final var docTreeFactory = m_DocletEnvironment.getDocTrees().getDocTreeFactory();
        final Function<CharSequence,Name> nameGenerator = m_DocletEnvironment.getElementUtils()::getName;

        final var tag = tags.get( 0 ).toString();
        final var matcher = PATTERN.matcher( tag );
        final var contents = matcher.matches() ? matcher.group( "contents" ) : EMPTY_STRING;
        final var retValue = isNotEmptyOrBlank( contents ) ? format( "<span style=\"text-decoration: underline;\">%1$s</span>", processInlineTags( contents, docTreeFactory, element, nameGenerator ) ) : EMPTY_STRING;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class UnderlineTaglet

/*
 *  End of File
 */