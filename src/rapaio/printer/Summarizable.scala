/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 Aurelian Tutuianu
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package rapaio.printer

import rapaio.workspace.Workspace

/**
 * Trait which is used to uniformly add auto-describing features to various
 * objects.
 *
 * Offers methods which works directly with printer
 * selected in [[rapaio.workspace.Workspace]].
 *
 * Various options for formatting the output are defined and used
 * from [[rapaio.]]
 * @author <a href="mailto:padreati@yahoo.com>Aurelian Tutuianu</a>
 */
trait Summarizable {

  // default implementation uses toString
  def summary(): Unit = {
    Workspace.printer().p(this.toString)
  }
}