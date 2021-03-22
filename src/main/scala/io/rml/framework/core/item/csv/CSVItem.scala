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

package io.rml.framework.core.item.csv

import io.rml.framework.core.internal.Logging
import io.rml.framework.core.item.{EmptyItem, Item}
import org.apache.commons.csv.{CSVFormat, CSVRecord}

import java.io.{IOException, StringReader}
import scala.collection.JavaConverters._

/**
  *
  * @param record
  */
class CSVItem(record: CSVRecord, val tag: String) extends Item {

  /**
    *
    * @param reference
    * @return
    */
  override def refer(reference: String): Option[List[String]] = {
    try {
      Some(List(record.get(reference)))
    } catch {
      case ex: IllegalArgumentException => {
        CSVItem.logDebug(s"Cannot refer reference: \n $ex")
        None
      }
    }
  }

}


object CSVItem  extends  Logging{


  def apply(record: CSVRecord): CSVItem = new CSVItem(record, "")


  //TODO remove this method and use the one with CSVFormat
  def apply(csvLine: String, delimiter: Char, quoteCharacter: Char, headers: Array[String]): Item = {
    CSVItem(csvLine, CSVFormat.newFormat(delimiter)
      .withQuote(quoteCharacter)
      .withHeader(headers: _*) // convert to Java var args
      .withTrim())
  }

  def fromDataBatch(dataBatch: String, csvFormat: CSVFormat): List[Item] = {

    //jdata batch string must not contain any leading whitespaces
    val sanitizedData = dataBatch.replaceAll("^\\s+", "").replace("\n\n", "")
    val parser = csvFormat.parse(new StringReader(sanitizedData))
    val result:List[Item] = parser.getRecords.asScala.toList.map(new CSVItem(_, ""))
    result
  }

  def apply(csvLine: String, cSVFormat: CSVFormat): Item = {
    try {
      val reader = new StringReader(csvLine)
      val record = cSVFormat
        .parse(reader)
        .getRecords.get(0)

      CSVItem(record)
    } catch {
      case e: IOException => logError(s"Error while parsing CSV:\n${csvLine}", e); new EmptyItem
      case e: IndexOutOfBoundsException => new EmptyItem
    }


  }

}
