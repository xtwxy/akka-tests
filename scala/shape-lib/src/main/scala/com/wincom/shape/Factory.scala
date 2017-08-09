package com.wincom.shape

import java.util.Map

import com.wincom.dcim.driver._

class TestDriver extends Driver {
  override def datalink(s: Sender): Unit = {

  }

  override def received(s: Sender, m: scala.Any): Unit = {

  }

  override def initialized(): Unit = {

  }
}

class TestFactory extends DriverFactory {
  override def name = "Test"

  override def create(params: Map[String, String]): Option[Driver] = {
    if (!params.isEmpty) Some(new TestDriver) else None
  }
}