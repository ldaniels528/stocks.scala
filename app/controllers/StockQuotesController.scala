package controllers

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import models.StockQuote
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import java.util.Date
import javax.inject._
import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext

@Singleton
class StockQuotesController @Inject()(val controllerComponents: ControllerComponents)
                                     (implicit ec: ExecutionContext)
  extends BaseController {
  private val quotes = TrieMap[String, StockQuote]()

  /**
   * Retrieves a quote for a security by symbol
   * @param symbol   the symbol of the stock for which to search.
   * @param exchange the exchange of the stock for which to search.
   * @example {{{
   *  curl -X GET http://localhost:9000/quotes/ABC/NYSE
   * }}}
   */
  def getStockQuote(symbol: String, exchange: String): Action[AnyContent] = Action.async {
    val response = for {
      _ <- IO.println(s"searching for quote $symbol/$exchange...")
      quote <- findStockQuote(symbol, exchange)
    } yield quote

    response.unsafeToFuture().map { quote =>
      Ok(Json.toJson(quote))
    }
  }

  private def findStockQuote(symbol: String, exchange: String): IO[StockQuote] = IO {
    quotes.getOrElseUpdate(symbol, {
      val random = scala.util.Random
      StockQuote(
        symbol.toUpperCase,
        exchange = exchange.toUpperCase,
        lastSale = random.nextDouble() * 200.0,
        transactionTime = new Date())
    })
  }

}