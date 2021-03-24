package io.rml.framework.core.model

import io.rml.framework.core.model.std.StdLogicalTarget

import java.util.Objects

/**
  * MIT License
  *
  * Copyright (C) 2017 - 2021 RDF Mapping Language (RML)
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
  * */
trait LogicalTarget extends Node {

  def target: DataSink

  def serialization: Option[Uri]

  def compression: Option[Uri]

  override def identifier: String = {
    val serHash = if (serialization.isDefined) serialization.get.identifier else ""
    val compHash = if (compression.isDefined) compression.get.identifier else ""
    Objects.hash(target.identifier, serHash, compHash).toHexString
  }

}

object LogicalTarget {
  def apply(
           target: DataSink,
           serialization: Option[Uri],
           compression: Option[Uri]
           ): LogicalTarget = {
    StdLogicalTarget(target, serialization, compression)
  }
}
