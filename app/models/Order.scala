package models

import play.api.libs.json.{Json, OFormat}

/**
 * Represents an order
 * @param orderType the order type (BUY, SELL)
 * @param symbol    the symbol of the security to buy or sell
 * @param exchange  the exchange of the security to buy or sell
 * @param quantity  the quantity to buy or sell
 * @example {{{
 *  { "orderType": "BUY", "symbol": "ABC", "exchange": "NYSE", "quantity": 100 }
 * }}}
 */
case class Order(orderType: String,
                 symbol: String,
                 exchange: String,
                 quantity: Double)

object Order {

  implicit val orderFormat: OFormat[Order] = Json.format[Order]
}