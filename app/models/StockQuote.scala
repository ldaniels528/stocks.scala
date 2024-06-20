package models

import play.api.libs.json.{Json, OFormat}

import java.util.Date

case class StockQuote(symbol: String,
                      exchange: String,
                      lastSale: Double,
                      transactionTime: Date)

object StockQuote {
  implicit val stockQuoteFormat: OFormat[StockQuote] = Json.format[StockQuote]
}
