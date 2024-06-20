package controllers

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import models.Order
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderController @Inject()(val controllerComponents: ControllerComponents)
                               (implicit ec: ExecutionContext)
  extends BaseController {

  /**
   * Cancels an existing Order
   * @param symbol   the symbol of the stock for which to search.
   * @param exchange the exchange of the stock for which to search.
   * @example {{{
   *  curl -X DELETE http://localhost:9000/orders/ABC/NYSE
   * }}}
   */
  def cancelOrder(symbol: String, exchange: String): Action[AnyContent] = Action.async {
    val response = for {
      _ <- IO.println(s"cancelling order $symbol/$exchange...")
      _ <- deleteOrder(symbol, exchange)
    } yield ()

    response.unsafeToFuture().map { _ =>
      Ok(Json.parse("{}"))
    }
  }

  /**
   * Creates a new Order
   * @example {{{
   *  curl -X POST http://localhost:9000/orders \
   *    -H "Content-Type: application/json" \
   *    -d '{ "orderType": "BUY", "symbol": "ABC", "exchange": "NYSE", "quantity": 100 }'
   * }}}
   */
  def createOrder(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Order].fold(
      errors =>
        Future.successful(BadRequest(Json.obj(
          "message" -> "Invalid JSON",
          "details" -> JsError.toJson(errors)
        ))),
      order => {
        val result: IO[Result] = for {
          _ <- insertOrder(order)
        } yield Created(Json.obj(
          "message" -> "Order created successfully",
          "order" -> Json.toJson(order)
        ))

        // convert IO[Result] to Future[Result]
        result.unsafeToFuture()
      }
    )
  }

  /**
   * Retrieves an existing Order
   * @param symbol   the symbol of the stock for which to search.
   * @param exchange the exchange of the stock for which to search.
   * @example {{{
   *  curl -X GET http://localhost:9000/orders/ABC/NYSE
   * }}}
   */
  def getOrder(symbol: String, exchange: String): Action[AnyContent] = Action.async {
    val response = for {
      _ <- IO.println(s"searching symbol '$symbol'...")
      order <- findOrderBySymbol(symbol, exchange)
    } yield order

    response.unsafeToFuture().map { order =>
      Ok(Json.toJson(order))
    }
  }

  /**
   * Modifies an existing Order
   * @example {{{
   *  curl -X PUT http://localhost:9000/orders \
   *    -H "Content-Type: application/json" \
   *    -d '{ "orderType": "BUY", "symbol": "ABC", "exchange": "NYSE", "quantity": 100 }'
   * }}}
   */
  def modifyOrder(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Order].fold(
      errors =>
        Future.successful(BadRequest(Json.obj(
          "message" -> "Invalid JSON",
          "details" -> JsError.toJson(errors)
        ))),
      order => {
        val result: IO[Result] = for {
          _ <- updateOrder(order)
        } yield Created(Json.obj(
          "message" -> "Order updated successfully",
          "order" -> Json.toJson(order)
        ))

        // convert IO[Result] to Future[Result]
        result.unsafeToFuture()
      }
    )
  }

  private def insertOrder(order: Order): IO[Unit] = IO {
    // TODO create order
    println(s"Order created: $order")
  }

  private def deleteOrder(symbol: String, exchange: String): IO[Unit] = IO {
    // TODO delete order
    println(s"Order deleted: $symbol/$exchange")
  }

  private def findOrderBySymbol(symbol: String, exchange: String): IO[Order] = IO {
    // TODO find order
    val order = Order("BUY", symbol, exchange, 100.0)
    println(s"Order found: $order")
    order
  }

  private def updateOrder(order: Order): IO[Unit] = IO {
    // TODO update order
    println(s"Order updated: $order")
  }

}
