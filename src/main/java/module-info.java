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

/**
 *  <p>{@summary The Foundation Javadoc Extensions.}</p>
 */

module org.tquadrat.foundation.javadoc
{
    requires java.base;
    requires java.compiler;
    requires org.apiguardian.api;
    requires jdk.compiler;
    requires jdk.javadoc;
    requires jakarta.activation;
    requires org.commonmark;
    requires org.commonmark.ext.gfm.tables;

    exports org.tquadrat.foundation.javadoc;
}

/*
 *  End of File
 */