/**
  * MIT License
  *
  * Copyright (C) 2017 - 2020 RDF Mapping Language (RML)
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in
  * all copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  * THE SOFTWARE.
  *
  **/

package io.rml.framework.core.model

import io.rml.framework.core.model.std.StdSubjectMap

/**
  * This trait defines a subject map. A subject map is a term map.
  * It specifies a rule for generating the subjects of the RDF triples generated by a triples map.
  *
  * Spec: http://rml.io/spec.html#subject-map
  *
  */
trait SubjectMap extends TermMap {

  /**
    *
    * @return
    */
  def `class`: List[Uri]

  def graphMap: Option[GraphMap]
}

object SubjectMap {

  /**
    *
    * @param identifier
    * @param `class`
    * @param constant
    * @param reference
    * @param template
    * @param termType
    * @return
    */
  def apply(identifier: String,
            `class`: List[Uri],
            functionMap: List[FunctionMap],
            constant: Option[Entity],
            reference: Option[Literal],
            template: Option[Literal],
            termType: Option[Uri],
            graphMap: Option[GraphMap]): SubjectMap = {

    StdSubjectMap(identifier, `class`, functionMap, constant, reference, template, termType, graphMap)
  }

}
