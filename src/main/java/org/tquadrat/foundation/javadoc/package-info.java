/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 *  <p>{@summary The classes in the package
 *  {@code org.tquadrat.foundation.javadoc} provide some extensions to the
 *  standard Javadoc extensions.}</p>
 *  <p>An older version provided a custom
 *  {@linkplain jdk.javadoc.doclet.Doclet Doclet}
 *  that extended
 *  {@link jdk.javadoc.doclet.StandardDoclet};
 *  it did some pre-processing and then delegated the work to the parent.</p>
 *  <p>The new implementation here is completely based on the new Java&nbsp;9
 *  APIs, and will implement all functions only as instances of
 *  {@link jdk.javadoc.doclet.Taglet Taglet}.</p>
 *  <p>Basically, the extension are used by the Foundation Library itself, but
 *  the are available for any other project,too. To activate the extensions,
 *  add the following parameters to your Javadoc call:</p>
 *  <pre><code>
 *  -tagletpath &lt;<i>path/to/</i>&gt;org.tquadrat.foundation.javadoc-0.1.0.jar:&lt;<i>path/to/</i>&gt;apiguardian-api-1.1.0.jar:&lt;<i>path/to/</i>&gt;jakarta.activation-2.0.0.jar
 *
 *  -taglet org.tquadrat.foundation.javadoc.AuthorTaglet
 *  -taglet org.tquadrat.foundation.javadoc.AnchorTaglet
 *  -taglet org.tquadrat.foundation.javadoc.HRefTaglet
 *  -taglet org.tquadrat.foundation.javadoc.IgnoreTaglet
 *  -taglet org.tquadrat.foundation.javadoc.IncludeTaglet
 *  -taglet org.tquadrat.foundation.javadoc.InspiredTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ModifiedTaglet
 *  -taglet org.tquadrat.foundation.javadoc.NoteTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ThanksTaglet
 *  -taglet org.tquadrat.foundation.javadoc.ToDoTaglet
 *  -taglet org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet
 *  -taglet org.tquadrat.foundation.javadoc.UnderlineTaglet
 *
 *  -tag note
 *  -tag param
 *  -tag return
 *  -tag throws
 *  -tag author
 *  -tag extauthor
 *  -tag thanks
 *  -tag modified
 *  -tag version
 *  -tag since
 *  -tag see
 *  -tag inspired
 *  -tag UMLGraph.link
 *  -tag todo
 *  </code></pre><br>
 *  <p>In detail, the following new documentation tags can be used:</p>
 *  <dl>
 *      <dt>{@code @extauthor}</dt>
 *      <dd><p>A replacement for the default {@code @author} taglet that
 *      provides the author's email as a hyper link.</p>
 *      <p>This means that the tag requires the reference to the author in the
 *      format</p>
 *      <pre><code>  &#x40;extauthor &lt;<i>name</i>&gt; <b>-</b> &lt;<i>email address</i>&gt;</code></pre><br>
 *      <p>Basically, this is the name of the author, followed by their email
 *      address, separated by a hyphen (&quot;&#x2d;&quot; or &amp;#x2d;),
 *      surrounded by blanks.</p>
 *      <p>If there is no email address, the output is the same as for the
 *      standard {@code @author} taglet.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.AuthorTaglet}.</p></dd>
 *      <dt>{@code @thanks}</dt>
 *      <dd><p>Use this tag to add a reference to the author of the model for
 *      the current piece of code. It uses the same format as the
 *      {@code @extauthor} tag described above.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.ThanksTaglet}.</p></dd>
 *      <dt>{@code @modified}</dt>
 *      <dd><p>When code written by somebody else was modified, this tag can be
 *      used to refer to the editor. It makes also use of the format as
 *      described for the {@code @extauthor} above.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.ModifiedTaglet}.</p></dd>
 *      <dt>{@code @inspired}</dt>
 *      <dd><p>Sometimes a piece of code was inspired by a document of some
 *      kind, a description of an algorithm, a product white paper, or
 *      whatever. This tag allows to add a reference to that source of
 *      inspiration. The text for this taglet has some limitations as described
 *      in the documentation for the implementing class
 *      {@link org.tquadrat.foundation.javadoc.InspiredTaglet}.</p></dd>
 *      <dt>{@code @note}</dt>
 *      <dd><p>With this tag, it is easy to add important notes to the
 *      documentation for an element. All notes will be added to a bullet list
 *      placed immediately beneath the documentation text (given that the
 *      {@code tag} sequence shown above is used). The text for the
 *      {@code @note} is somehow limited, refer to the documentation for the
 *      implementing class
 *      {@link org.tquadrat.foundation.javadoc.NoteTaglet}.</p></dd>
 *      <dt>{@code @todo <task.list>}</dt>
 *      <dd><p>This tag can be used to add a list of open issues to the
 *      documentation for a module or a package. {@code <task.list>} is the
 *      name of a file with the open issue; the documentation for the
 *      implementing class
 *      {@link org.tquadrat.foundation.javadoc.ToDoTaglet}
 *      provides information about the format of the file and how it will be
 *      retrieved during the Javadoc generation.</p></dd>
 *      <dt>{@code @UMLGraph.link}</dt>
 *      <dd><p>With this tag a UML graph can be added to the documentation for a
 *      class.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet}.</p></dd>
 *      <dt>{@code {@anchor #<name> <text>}}</dt>
 *      <dd><p>This tag allows to add an HTML anchor to the documentation,
 *      where {@code <name>} is the name of that anchor, and {@code <text>} the
 *      anchor text. The hash symbol (&quot;#&quot;) before the anchor name is
 *      mandatory!</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.AnchorTaglet}.</p></dd>
 *      <dt>{@code {@href <url> <text>}} or {@code {@href <url>}}</dt>
 *      <dd><p>With this tag a hyper link can be added to the documentation;
 *      different from the {@code {@link}} and {@code {@linkplain}} tags, this
 *      is used to refer to external resources. Obviously, {@code <url>} is the
 *      target URL, while {@code <text>}} is the clickable text. If the latter
 *      is omitted, the URL itself will be used instead.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.HRefTaglet}.</p></dd>
 *      <dt>{@code {@underline <text>}}</dt>
 *      <dd><p>If text needs to be underlined, this is the tag.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.UnderlineTaglet}.</p></dd>
 *      <dt><code>{&#64;include &lt;file&gt;:[&lt;processMode&gt;]}}</code></dt>
 *      <dd><p>This tag allows to include other files from the source path into
 *      the JavaDoc documentation. For the details, refer to the documentation
 *      for the implementing class
 *      {@link org.tquadrat.foundation.javadoc.IncludeTaglet}.</p></dd>
 *      <dt>{@code {@ignore <text>}}</dt>
 *      <dd><p>The standard tag {@code @hidden} allows to exclude the whole
 *      documentation for an element (a type, method or field) from the
 *      generated Javadoc documentation. With the tag {@code @ignore} it is
 *      possible to exclude just the text inside. This can be useful when the
 *      comments for the generated documentation are difficult to read.</p>
 *      <p>Implemented by the class
 *      {@link org.tquadrat.foundation.javadoc.UmlGraphLinkTaglet}.</p></dd>
 *  </dl>
 *
 *  @todo task.list
 */

package org.tquadrat.foundation.javadoc;

/*
 *  End of File
 */