package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}

class OrderControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "OrderController GET" should {

    "create a new order with valid JSON" in {
      val controller = inject[OrderController]
      val request = FakeRequest(POST, "/orders")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(Json.parse(
          """{ "orderType": "BUY", "symbol": "ABC", "exchange": "NYSE", "quantity": 100 }"""
        ))
      val result = controller.createOrder().apply(request).run()
      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Order created successfully")
    }

    "retrieve an order with valid symbol and exchange" in {
      val controller = inject[OrderController]
      val request = FakeRequest(POST, "/orders")
      val result = controller.getOrder("ABC", "NYSE").apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsJson(result) mustBe Json.parse(
        """{"orderType":"BUY","symbol":"ABC","exchange":"NYSE","quantity":100}"""
      )
    }

    "modify an existing order with valid JSON" in {
      val controller = inject[OrderController]
      val request = FakeRequest(PUT, "/orders")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(Json.parse(
          """{ "orderType": "BUY", "symbol": "ABC", "exchange": "NYSE", "quantity": 100 }"""
        ))
      val result = controller.modifyOrder().apply(request).run()
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Order updated successfully")
    }

    "cancel an order via a valid symbol and exchange" in {
      val controller = inject[OrderController]
      val request = FakeRequest(POST, "/orders")
      val result = controller.cancelOrder("ABC", "NYSE").apply(request)
      status(result) mustBe OK
    }

  }

}
