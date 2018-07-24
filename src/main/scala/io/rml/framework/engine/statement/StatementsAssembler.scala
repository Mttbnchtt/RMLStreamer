/*
 * Copyright (c) 2017 Ghent University - imec
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.rml.framework.engine.statement

import io.rml.framework.core.model._
import io.rml.framework.core.vocabulary.RDFVoc
import io.rml.framework.flink.item.{Item, JoinedItem}

/**
  * Creates statements from triple maps.
  */
class StatementsAssembler(subjectAssembler: SubjectGeneratorAssembler = SubjectGeneratorAssembler(),
                          predicateObjectAssembler: PredicateObjectGeneratorAssembler = PredicateObjectGeneratorAssembler()) {

  /**
    * Creates statements from a triple map.
    *
    * @param tripleMap
    * @return
    */
  def assembleStatements(tripleMap: TripleMap): List[((Item) => Option[Iterable[TermNode]], (Item) => Option[Iterable[Uri]], (Item) => Option[Iterable[Entity]])] = {
    // assemble subject
    val subjectGenerator = subjectAssembler.assemble(tripleMap.subjectMap)
    // check for class mappings (rr:class)
    val classMappings = getClassMappingStatements(subjectGenerator, tripleMap.subjectMap.`class`)
    // assemble predicate and object
    val predicateObjects = tripleMap.predicateObjectMaps.flatMap(predicateObjectMap => {
      predicateObjectAssembler.assemble(predicateObjectMap)
    })
    // create the statements
    predicateObjects.map(predicateObject => {
      (subjectGenerator, predicateObject._1, predicateObject._2)
    }) ++ classMappings // add class mappings

  }


  private def getClassMappingStatements(subjectGenerator: (Item) => Option[Iterable[TermNode]],
                                        classes: List[Uri]): Seq[((Item) => Option[Iterable[TermNode]], (Item) => Some[Iterable[Uri]], (Item) => Some[Iterable[Uri]])] = {
    classes.map(_class => {
      val predicateGenerator = (item: Item) => Some(List(Uri(RDFVoc.Property.TYPE)))
      val objectGenerator = (item: Item) => Some(List(_class))
      (subjectGenerator, predicateGenerator, objectGenerator)
    })

  }

}

object StatementsAssembler {

  def assembleStatements(tripleMap: TripleMap): List[Statement[Item]] = {
    val triples = new StatementsAssembler()
      .assembleStatements(tripleMap)
    triples.map(triple => Statement.createStandardStatement(triple._1, triple._2, triple._3))
  }

  def assembleChildStatements(joinedTripleMap: JoinedTripleMap): List[Statement[JoinedItem]] = {
    val triples = new StatementsAssembler()
      .assembleStatements(joinedTripleMap)
    triples.map(triple => Statement.createChildStatement(triple._1, triple._2, triple._3))
  }

  def assembleParentStatements(joinedTripleMap: JoinedTripleMap): List[Statement[JoinedItem]] = {
    val triples = new StatementsAssembler()
      .assembleStatements(joinedTripleMap.parentTriplesMap)
    triples.map(triple => Statement.createParentStatement(triple._1, triple._2, triple._3))
  }


}
