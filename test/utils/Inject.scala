package utils

import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder

import scala.reflect.ClassTag

trait Inject {
  lazy val injector: Injector = (new GuiceApplicationBuilder).injector()

  def inject[T: ClassTag]: T = injector.instanceOf[T]
}

