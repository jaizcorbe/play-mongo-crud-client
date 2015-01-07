package com.dridco.mongo

import com.dridco.mongo.SampleModel.UserDto
import com.dridco.mongo.SampleRepositoryMock.{InvalidUserMock, ValidUserMock}
import org.junit.runner._
import org.scalatestplus.play._
import org.specs2.runner._
import play.api.libs.json.{Json, JsObject}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * This spec must be executed in a Play! Application context
 * */
@RunWith(classOf[JUnitRunner])
object SampleRepositorySpec extends PlaySpec {

  import com.dridco.mongo.SampleModel.Implicits._

  "find users" should {
    "return user list" in {
      val user = new ValidUserMock
      val response = Await.result(user.db.find(), Duration.Inf)
      response must not be empty
      response must have size 2
      response must contain (UserDto(1, "foo"))
      response must contain (UserDto(2, "bar"))
    }
  }

  "find users" should {
    "return empty users list" in {
      val user = new InvalidUserMock
      val response: List[UserDto] = Await.result(user.db.find(), Duration.Inf)
      response mustBe empty
    }
  }

  "save a user" should {
    "okey" in {
      val user = new ValidUserMock
      val obj = UserDto(3, "new-user")
      val emptyJsModifier: JsObject = Json.obj("" -> "")
      val response = user.db.saveOrUpdate(None, emptyJsModifier, obj)
      response mustBe "ok"
    }
  }

  "update user" should {
    "define..." in {
      pending
    }
  }

}
