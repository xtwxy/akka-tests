package com.wincom.shape

/**
  * Created by wangxy on 17-8-2.
  */
trait Shape {
  def area: Double
}

object Shape {

  private class Circle(radius: Double) extends Shape {
    override val area = 3.14 * radius * radius
  }

  private class Rectangle(height: Double, length: Double) extends Shape {
    override val area = height * length
  }

  def apply(radius: Double): Shape = new Circle(radius)

  def apply(height: Double, length: Double): Shape = new Rectangle(height, length)
}

class Vehicle(speed: Int) {
  val mph: Int = speed

  def race() = println("Racing")
}

class Car(speed: Int) extends Vehicle(speed) {
  override val mph: Int = speed

  override def race() = println("Racing Car")

}

class Bike(speed: Int) extends Vehicle(speed) {
  override val mph: Int = speed

  override def race() = println("Racing Bike")

}

trait flying {
  def flies() = println("flying")
}

trait floating {
  def floats() = println("floating")
}

trait gliding {
  def glides() = println("gliding")
}

class Batmobile(speed: Int) extends Vehicle(speed) with flying with gliding with floating {
  override val mph: Int = speed

  override def race() = println("Racing Batmobile")

  override def flies() = println("Flying Batmobile")

  override def floats() = println("Gliding Batmobile")
}

class Stuff(val name: String, val age: Int) {
  override def toString = "Stuff(" + name + ", " + age + ")"

  override def hashCode = name.hashCode + age

  override def equals(other: scala.Any): Boolean = other match {
    case s: Stuff => this.name == s.name && this.age == s.age
    case _ => false
  }
}

object Stuff {
  def apply(name: String, age: Int) = new Stuff(name, age)

  def unapply(s: Stuff) = Some((s.name, s.age))
}