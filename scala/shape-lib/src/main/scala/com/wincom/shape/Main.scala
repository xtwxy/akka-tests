package com.wincom.shape

import com.wincom.dcim.driver._

/**
  * Created by wangxy on 17-8-2.
  */
object Main extends App {
  val s = Shape.apply(1.0)
  println(s.area)
  DriverRegistry.main(Array[String]())
}
