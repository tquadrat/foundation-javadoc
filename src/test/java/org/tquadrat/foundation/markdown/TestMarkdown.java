/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.markdown;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.testutil.TestBaseClass;

import static org.apiguardian.api.API.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *  Playground for Markdown
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@DisplayName( "org.tquadrat.foundation.markdown.TestMarkdown" )
public class TestMarkdown extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Create an HTML fragment from an input String.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testCreateFragment() throws Exception
    {
        skipThreadTest();

        var input =
            """
            This is *Markdown*""";

        var expected =
            """
            <p>This is <em>Markdown</em></p>
            """;

        final var parser = Parser.builder()
            .build();
        var document = parser.parse( input );
        final var renderer = HtmlRenderer.builder()
            .build();
        var current = renderer.render( document );
        assertEquals( expected, current );

        input =
            """
            Problem Number 0815 consists of these points:
              - Number 1
              - Number 2
              - Some more complex things
                - Detail 1
                - Detail 2""";

        expected =
            """
            <p>Problem Number 0815 consists of these points:</p>
            <ul>
            <li>Number 1</li>
            <li>Number 2</li>
            <li>Some more complex things
            <ul>
            <li>Detail 1</li>
            <li>Detail 2</li>
            </ul>
            </li>
            </ul>
            """;

        document = parser.parse( input );
        current = renderer.render( document );
        assertEquals( expected, current );

        input =
            """
            - Problem Number 4711 consists of these points:
              - Number 1
              - Number 2
              - Some more complex things
                - Detail 1
                - Detail 2""";

        expected =
            """
            <ul>
            <li>Problem Number 4711 consists of these points:
            <ul>
            <li>Number 1</li>
            <li>Number 2</li>
            <li>Some more complex things
            <ul>
            <li>Detail 1</li>
            <li>Detail 2</li>
            </ul>
            </li>
            </ul>
            </li>
            </ul>
            """;

        document = parser.parse( input );
        current = renderer.render( document );
        assertEquals( expected, current );

        input =
            """
            Try this `code` fragment:
            ```
            <p>Code Fragment</p>
            ```""";

        expected =
            """
            <p>Try this <code>code</code> fragment:</p>
            <pre><code>&lt;p&gt;Code Fragment&lt;/p&gt;
            </code></pre>
            """;

        document = parser.parse( input );
        current = renderer.render( document );
        assertEquals( expected, current );

        input =
            """
             # Headline
            Try this `code` fragment:
            ```
            <p>Code Fragment</p>
            ```""";

        expected =
            """
            <h1>Headline</h1>
            <p>Try this <code>code</code> fragment:</p>
            <pre><code>&lt;p&gt;Code Fragment&lt;/p&gt;
            </code></pre>
            """;

        document = parser.parse( input );
        current = renderer.render( document );
        assertEquals( expected, current );
    }   //  testCreateFragment()
}
//  class TestMarkdown

/*
 *  End of File
 */