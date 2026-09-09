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

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.EMPTY_STRING;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.NOT_FOUND;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.escapeHTML;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.isNotEmpty;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.mapFromNull;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNotEmptyArgument;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.splitString;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.AnchorTaglet;
import org.tquadrat.foundation.javadoc.AuthorTaglet;
import org.tquadrat.foundation.javadoc.HRefTaglet;
import org.tquadrat.foundation.javadoc.ImageTaglet;
import org.tquadrat.foundation.javadoc.InspiredTaglet;
import org.tquadrat.foundation.javadoc.ModifiedTaglet;
import org.tquadrat.foundation.javadoc.NoteTaglet;
import org.tquadrat.foundation.javadoc.ThanksTaglet;
import org.tquadrat.foundation.javadoc.ToDoTaglet;
import org.tquadrat.foundation.javadoc.UnderlineTaglet;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import com.sun.source.doctree.DocRootTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.IndexTree;
import com.sun.source.doctree.LinkTree;
import com.sun.source.doctree.LiteralTree;
import com.sun.source.doctree.ReferenceTree;
import com.sun.source.doctree.SystemPropertyTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.util.DocTreeFactory;
import com.sun.source.util.DocTreeScanner;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.StandardDoclet;
import jdk.javadoc.doclet.Taglet;

/**
 *  <p>{@summary The common base class for all custom taglet in this
 *  library.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CustomTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $
 *  @since 0.25.2
 */
@ClassVersion( sourceVersion = "$Id: CustomTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $" )
@API( status = STABLE, since = "0.25.2" )
public sealed abstract class CustomTagletBase implements Taglet
    permits OtherFileTagletBase, SimpleTagletBase, AnchorTaglet, AuthorTaglet,
        HRefTaglet, ImageTaglet, InspiredTaglet, ModifiedTaglet, NoteTaglet,
        ThanksTaglet, ToDoTaglet, UnderlineTaglet
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary The common base class for all custom taglet in this
     *  library.}</p>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: CustomTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $
     *  @since 0.25.2
     */
    @ClassVersion( sourceVersion = "$Id: CustomTagletBase.java 1282 2026-09-08 23:52:53Z tquadrat $" )
    @API( status = INTERNAL, since = "0.25.2" )
    protected final class CustomTagScanner extends DocTreeScanner<String,Element>
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  {@summary Creates a new instance of {@code CustomTagScanner}.}
         */
        public CustomTagScanner() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final String reduce( final String r1, final String r2 )
        {
            final var retValue = mapFromNull( r2, EMPTY_STRING ).concat( mapFromNull( r1, EMPTY_STRING ) );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  reduce()

        /**
         *  {@inheritDoc}
         */
        @Override
        public String visitDocRoot( final DocRootTree node, final Element element )
        {
            final var elementUtils = getDocletEnvironment().getElementUtils();
            final var pkg = elementUtils.getPackageOf( element );
            final var pkgName = pkg.getQualifiedName().toString();

            var depth = pkgName.isEmpty() ? 0 : splitString( pkgName, '.' ).length;

            //---* Module counts as an additional folder level *---------------
            final var module = elementUtils.getModuleOf( element );
            if( nonNull( module ) && !module.isUnnamed() ) ++depth;

            final var retValue = depth == 0
                ? "."
                : join( "/", nCopies( depth, ".." ) );

            //---* Done *----------------------------------------------------------
            return retValue;
        }   //  visitDocRoot()

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String visitIndex( final IndexTree node, final Element element )
        {
            final var retValue = super.visitIndex( node, element );

            //---* Done *----------------------------------------------------------
            return retValue;
        }   //  visitIndex()

        /**
         *  {@inheritDoc}
         */
        @Override
        public String visitLink( final LinkTree node, final Element element )
        {
            final var tag = node.getKind() == DocTree.Kind.LINK ? "code" : "span";
            final var reference = scan( node.getReference(), element );
            final var label = scan( node.getLabel(), element );
            final String retValue;
            if( isNotEmpty( label ) )
            {
                retValue = reduce( "<%1$s>%2$s</%1$s>".formatted( tag, label ), "<%1$s>%2$s</%1$s>".formatted( "code", reference ) );
            }
            else
            {
                retValue = "<%1$s>%2$s</%1$s>".formatted( tag, reference );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  visitLink()

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String visitLiteral( final LiteralTree node, final Element element )
        {
            final var body = escapeHTML( scan( node.getBody(), element ) );
            final var tag = node.getKind() == DocTree.Kind.CODE ? "code" : "span";
            final var retValue = "<%1$s>%2$s</%1$s>".formatted( tag, body );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  visitLiteral()

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String visitReference( final ReferenceTree node, final Element element )
        {
            /*
             *  We do not support {@link} and {@linkplain} inside our custom
             *  tags.
             */
            final var signature = node.getSignature();
            final var retValue = signature.startsWith( "#" )
                ? signature.substring( 1 )
                : signature.replace( '#', '.' );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  visitReference()

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String visitSystemProperty( final SystemPropertyTree node, final Element element )
        {
            final var retValue = "<code>%s</code>".formatted( node.getPropertyName() );

            //---* Done *----------------------------------------------------------
            return retValue;
        }   //  visitSystemProperty()

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String visitText( final TextTree node, final Element unused )
        {
            final var retValue = node.getBody();

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  visitText()

        /**
         *  {@inheritDoc}
         */
        @Override
        public String visitUnknownInlineTag( final UnknownInlineTagTree node, final Element element )
        {
            final var tagName = node.getTagName();
            BiFunction<List<? extends DocTree>,Element,String> function = m_TagFunctions.get( tagName );
            final String retValue;
            if( isNull( function ) )
            {
                printf( ERROR, "Unknown Tag: %s", tagName );
                retValue = "[{@%s} unknown]".formatted( tagName );
            }
            else
            {
                retValue = function.apply( List.of( node ), element );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  visitUnknownInlineTag()
    }
    //  class CustomTagScanner

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The lead for an inline tag: {@value}.}</p>
     */
    public static final String INLINE_TAG_LeadIn = "{@";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The locations that are allowed for the tag that is
     *  implemented by this taglet.}</p>
     */
    private final Set<Location> m_AllowedLocations = EnumSet.noneOf( Location.class );

    /**
     *  <p>{@summary The reference for the Doclet.} Will be set when
     *  {@link #init(DocletEnvironment,Doclet)}
     *  is called.</p>
     */
    private StandardDoclet m_Doclet;

    /**
     *  <p>{@summary The reference for the Doclet environment.} Will be set
     *  when
     *  {@link #init(DocletEnvironment,Doclet)}
     *  is called.</p>
     */
    private DocletEnvironment m_DocletEnvironment;

    /**
     *  <p>{@summary Flag that indicates whether the tag is an inline tag.}</p>
     */
    private final boolean m_IsInlineTag;

    /**
     *  <p>{@summary The name of the tag.}</p>
     */
    private final String m_Name;

    /**
     *  <p>{@summary The implementation of
     *  {@link Taglet#toString(List,Element)}
     *  for the inline custom tags.}</p>
     */
    private static final Map<String,BiFunction<List<? extends DocTree>,Element,String>> m_TagFunctions = new ConcurrentHashMap<>();

    /**
     *  <p>{@summary The tag scanner.}</p>
     */
    private final CustomTagletBase.CustomTagScanner m_TagScanner;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new instance of {@code CustomTagletBase}.}</p>
     *
     *  @param  name    The name of the tag.
     *  @param  isInlineTag {@code true} if the tag is an inline tag,
     *      {code false} for a block tag.
     *  @param  allowedLocations    The locations that are allowed for the tag
     *      that is implemented by this taglet.
     */
    protected CustomTagletBase( final String name, final boolean isInlineTag, final Location... allowedLocations )
    {
        m_Name = requireNotEmptyArgument( name, "name" );
        m_IsInlineTag = isInlineTag;

        m_AllowedLocations.addAll( asList( requireNonNullArgument( allowedLocations, "allowedLocations" ) ) );

        m_TagScanner = new CustomTagletBase.CustomTagScanner();
    }   //  CustomTagletBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Composes an HTML {@code <href>} link from the given
     *  {@code url} string and contents.}</p>
     *
     *  @param  url The link target.
     *  @param  contents    The link text.
     *  @return The {@code <href>}.
     */
    protected final String composeLink( final String url, final String contents )
    {
        final var retValue = format( "<a href=\"%1$s\">%2$s</a>", url, contents );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeLink()

    /**
     *  <p>{@summary Composes a
     *  {@link Name}
     *  instance from the given String.}</p>
     *
     *  @param  name    The name value.
     *  @return The {@code Name} instance.
     */
    private final Name composeName( final CharSequence name )
    {
        final var retValue = getDocletEnvironment().getElementUtils().getName( name );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeName()

    /**
     *  Finds the end of a tag in the given text.
     *
     *  @param  text    The text.
     *  @return The position for closing bracket for the tag, or -1 if there is
     *      no closing bracket, or the text is not a tag.
     */
    private final int findEnd( final String text )
    {
        var retValue = -1;
        if( text.startsWith( INLINE_TAG_LeadIn) )
        {
            var level = 1;
            retValue = 2;
            while( (level > 0) && (retValue < text.length()) )
            {
                switch( text.charAt( retValue ) )
                {
                    case '}' -> { if( --level > 0 ) ++retValue; }
                    case '{' -> { if( (retValue++ < text.length()) && (text.charAt( retValue ) == '@') ) ++level; }
                    default -> ++retValue;
                }
            }
            if( level > 0 ) retValue = -1;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findEnd()

    /**
     *  <p>{@summary Taglets place additional initialisations here.}</p>
     *  <p>This implementation does nothing.</p>
     */
    protected void customInit()
    {
        /* Does nothing */
    }   //  customInit()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Location> getAllowedLocations() { return m_AllowedLocations; }

    /**
     *  Returns a reference to the
     *  {@link Doclet}
     *  instance that uses this taglet.
     *
     *  @return The reference to the Doclet.
     */
    @SuppressWarnings( "unused" )
    protected final Doclet getDoclet() { return m_Doclet; }

    /**
     *  <p>{@summary Returns a reference to the
     *  {@link DocletEnvironment}
     *  instance that is used by the
     *  {@link Doclet}
     *  running this taglet.}</p>
     *
     *  @return The reference to the Doclet processing environment.
     */
    protected final DocletEnvironment getDocletEnvironment() { return m_DocletEnvironment; }

    /**
     *  <p>{@summary Returns a reference to a
     *  {@link DocTreeFactory}
     *  instance that can be used to create
     *  {@link DocTree}
     *  instances.}</p>
     *
     *  @return The reference to the {@code DocTreeFactory}.
     */
    protected final DocTreeFactory getDocTreeFactory() { return m_DocletEnvironment.getDocTrees().getDocTreeFactory(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getName() { return m_Name; }

    /**
     *  <p>{@summary Returns the type utils.}</p>
     *
     *  @return The type utils.
     */
    protected final Types getTypeUtils() { return m_DocletEnvironment.getTypeUtils(); }

    /**
     *  <p>{@summary Parses the given text for a known inline tag and returns
     *  the relevant
     *  {@link DocTree}
     *  instance, or an
     *  {@link UnknownInlineTagTree}
     *  instance if the tag is not known (or it is a custom tag).}</p>
     *
     *  @param  text    The text that should be a tag.
     *  @return The matching {@code DocTree} instance.
     */
    private DocTree identifyTag( final String text )
    {
        final var factory = getDocTreeFactory();

        //---* Get the tag *---------------------------------------------------
        final var blankPos = text.indexOf( ' ' );
        final var endPos = findEnd( text );
        final var tag = text.substring( 2, blankPos > NOT_FOUND ? blankPos : endPos );
        final var body = blankPos == NOT_FOUND ? EMPTY_STRING : text.substring( blankPos + 1, endPos );
        final var contents = factory.newTextTree( body );

        //---* Create the DocTree *--------------------------------------------
        final DocTree retValue = switch( tag )
        {
            //noinspection DataFlowIssue
            case null -> factory.newTextTree( EMPTY_STRING );
            case "code" -> factory.newCodeTree( contents );
            case "docRoot" -> factory.newDocRootTree();
            case "index" -> factory.newIndexTree( contents, List.of() );
            case "inheritDoc" -> factory.newTextTree( EMPTY_STRING );
            case "literal" -> factory.newLiteralTree( contents );
            case "snippet" -> factory.newSnippetTree( List.of(), contents );
            case "summary" -> contents;
            case "systemProperty" -> factory.newSystemPropertyTree( composeName( body ) );
            default -> factory.newUnknownInlineTagTree( composeName( tag ), List.of( contents ) );
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  identifyTag()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void init( final DocletEnvironment env, final Doclet doclet )
    {
        Taglet.super.init( env, doclet );
        if( doclet instanceof StandardDoclet standardDoclet )
        {
            m_Doclet = standardDoclet;
        }
        else
        {
            printf( ERROR, "Wrong Doclet implementation: %s%nThis custom taglet work only with '%s'", doclet.getClass().getName(), StandardDoclet.class.getName() );
            throw new Error();
        }
        m_DocletEnvironment = env;
        if( m_IsInlineTag ) m_TagFunctions.computeIfAbsent( getName(), _ -> this::toString );
        customInit();
    }   //  init()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isInlineTag() { return m_IsInlineTag; }

    /**
     *  <p>{@summary Parses the given tags for a name and an email
     *  address.}</p>
     *  <p>The tag is prefixed with the tag text, and name and email address
     *  are separated by a hyphen (&quot;&#x2d;&quot; or &amp;#x2d;),
     *  surrounded by blanks.</p>
     *
     *  @param  tags    The tags.
     *  @return The formatted output.
     */
    protected final String parseNameAndEmail( final Collection<? extends DocTree> tags )
    {
        final var prefix = format( "@%s", getName() );
        final var prefixLen = prefix.length();
        final var retValue = tags.stream()
            .map( Object::toString )
            .map( s -> s.substring( prefixLen ).trim() )
            .map( s ->
            {
                final var pos = s.indexOf( " - " );
                if( pos < 0 )
                {
                    if( s.endsWith( " -" ) ) return s.substring( 0, s.length() - 1 ).trim();
                    return s.trim();
                }

                final var emailAddress = s.substring( pos + 3 ).trim();
                if( emailAddress.isEmpty() ) return s.substring( 0, pos ).trim();

                final var buffer = s.substring( 0, pos ).trim() +
                    " (<a href=\"mailto:" +
                    emailAddress +
                    "\">" +
                    emailAddress +
                    "</a>)";
                return buffer;
            } )
            .collect( joining( ", ") );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseNameAndEmail()

    /**
     *  Parses the given text for some inline tags and returns the respective
     *  {@link DocTree}
     *  instances.
     *
     *  @param  text    The text to parse.
     *  @return The {@code DocTree} instances.
     */
    protected final List<? extends DocTree> parseText( final String text )
    {
        final var factory = getDocTreeFactory();
        final List<DocTree> retValue = new ArrayList<>();
        var pos = text.indexOf( INLINE_TAG_LeadIn );
        if( pos == NOT_FOUND )
        {
            retValue.add( factory.newTextTree( text ) );
        }
        else
        {
            var remainder = text;
            ScanLoop: while( pos > NOT_FOUND )
            {
                if( pos > 0 )
                {
                    retValue.add( factory.newTextTree( remainder.substring( 0, pos ) ) );
                    remainder = remainder.substring( pos );
                }
                var end = findEnd( remainder );
                if( end == NOT_FOUND )
                {
                    retValue.add( factory.newTextTree( remainder ) );
                    //noinspection UnusedAssignment
                    pos = NOT_FOUND;
                    break ScanLoop;
                }
                retValue.add( identifyTag( remainder.substring( 0, ++end ) ) );
                remainder = remainder.substring( end );
                pos = remainder.indexOf( INLINE_TAG_LeadIn );
            }   //  ScanLoop:
            if( !remainder.isEmpty() ) retValue.add( factory.newTextTree( remainder ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseText()

    /**
     *  <p>{@summary Prints a diagnostic message.}</p>
     *
     *  @param  kind    The kind of diagnostic.
     *  @param  message The message to be printed.
     */
    protected final void print( final Kind kind, final String message )
    {
        m_Doclet.getReporter().print( kind, message );
    }   //  print()

    /**
     *  <p>{@summary Prints a diagnostic message related to the given
     *  element.}</p>
     *
     *  @param  kind    The kind of diagnostic.
     *  @param  element The element the message is related to.
     *  @param  message The message to be printed.
     */
    protected final void print( final Kind kind, final Element element, final String message )
    {
        m_Doclet.getReporter().print( kind, element, message );
    }   //  print()

    /**
     *  <p>{@summary Prints a diagnostic message.} The message will be composed
     *  of the format and the arguments.</p>
     *
     *  @param  kind    The kind of diagnostic.
     *  @param  format  The format string for the message.
     *  @param  args    The arguments.
     *
     *  @see String#format(String,Object...)
     */
    protected final void printf( final Kind kind, final String format, final Object... args )
    {
        print( kind, format( format, args ) );
    }   //  printf()

    /**
     *  <p>{@summary Prints a diagnostic message.} The message will be composed
     *  of the format and the arguments.</p>
     *
     *  @param  kind    The kind of diagnostic.
     *  @param  element The element the message is related to.
     *  @param  format  The format string for the message.
     *  @param  args    The arguments.
     *
     *  @see String#format(String,Object...)
     */
    protected final void printf( final Kind kind, final Element element, final String format, final Object... args )
    {
        print( kind, element, format( format, args ) );
    }   //  printf()

    /**
     *  <p>{@summary Processes the content from a custom tag that may contain
     *  other tags.}</p>
     *
     *  @param tags The content.
     *  @param element  The owning element.
     *  @return The output.
     */
    protected final String processTagContent( final List<? extends DocTree> tags, final Element element )
    {
        final var retValue = m_TagScanner.scan( tags, element );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processTagContent()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract String toString( final List<? extends DocTree> tags, final Element element );
}
//  class CustomTagletBase

/*
 *  End of File
 */