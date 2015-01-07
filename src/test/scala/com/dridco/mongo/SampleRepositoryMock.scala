package com.dridco.mongo

import com.dridco.mongo.SampleModel.UserDto
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{Json, JsObject}

import scala.concurrent.Future

object SampleRepositoryMock {

  import com.dridco.mongo.SampleModel.Implicits._

  trait SampleMock extends MockitoSugar {

    lazy val db: BaseSampleRepository = mock[BaseSampleRepository]

  }

  def emptyJsModifier: JsObject = Json.obj("" -> "")

  class ValidUserMock extends SampleMock {

    val users = List(UserDto(1, "foo"), UserDto(2, "bar"))
    when(db.find()).thenReturn(Future(users))

    val obj = UserDto(3, "new-user")
    when(db.saveOrUpdate(None, emptyJsModifier, obj))

  }

  class InvalidUserMock extends SampleMock {

    when(db.find()).thenReturn(Future(List()))

    val obj = UserDto(3, "new-user")
    when(db.saveOrUpdate(Option(emptyJsModifier), emptyJsModifier, obj)).thenReturn(Future(false))

  }

}
