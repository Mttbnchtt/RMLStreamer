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

package io.rml.framework.engine.statement

import io.rml.framework.core.internal.Logging
import io.rml.framework.core.model.{Entity, PredicateObjectMap, Uri}
import io.rml.framework.flink.item.Item
class PredicateObjectGeneratorAssembler(predicateGeneratorAssembler: PredicateGeneratorAssembler,
                                        objectGeneratorAssembler: ObjectGeneratorAssembler,
                                        graphGeneratorAssembler: GraphGeneratorAssembler) extends Logging{

  def assemble(predicateObjectMap: PredicateObjectMap)
  : List[(Item => Option[Iterable[Uri]], Item => Option[Iterable[Entity]], Item => Option[Iterable[Uri]])] = {

    this.logDebug("assemble (predicateObjectMap)")

    val graphStatement = graphGeneratorAssembler.assemble(predicateObjectMap.graphMap)
    predicateObjectMap.predicateMaps.flatMap(predicateMap => {
      predicateObjectMap.objectMaps.map(objectMap => {
        (predicateGeneratorAssembler.assemble(predicateMap),
          objectGeneratorAssembler.assemble(objectMap),
          graphStatement)
      })

    })

  }
}

object PredicateObjectGeneratorAssembler {

  def apply(
             predicateGeneratorAssembler: PredicateGeneratorAssembler = PredicateGeneratorAssembler(),
             objectGeneratorAssembler: ObjectGeneratorAssembler = ObjectGeneratorAssembler(),
             graphGeneratorAssembler: GraphGeneratorAssembler = GraphGeneratorAssembler())

  : PredicateObjectGeneratorAssembler = new PredicateObjectGeneratorAssembler(predicateGeneratorAssembler,
    objectGeneratorAssembler, graphGeneratorAssembler)
}
