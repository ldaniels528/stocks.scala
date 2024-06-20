package controllers

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class CatsEffectController @Inject()(val controllerComponents: ControllerComponents)
                                    (implicit ec: ExecutionContext)
  extends BaseController {

  def index(): Action[AnyContent] = Action.async {
    val ops: IO[String] = for {
      _ <- IO(println("Handling request..."))
      response <- IO.pure("Hello from Cats Effect!")
    } yield response

    ops.unsafeToFuture().map { result =>
      Ok(result)
    }
  }
}
