package controllers

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class QuotesController @Inject()(val controllerComponents: ControllerComponents)
                                (implicit ec: ExecutionContext)
  extends BaseController {

  def lookupSymbol(symbol: String): Action[AnyContent] = Action.async {
    val ops: IO[String] = for {
      _ <- IO(println("Handling request..."))
      response <- IO.pure("Hello from Cats Effect!")
    } yield response

    ops.unsafeToFuture().map { result =>
      Ok(result)
    }
  }
}