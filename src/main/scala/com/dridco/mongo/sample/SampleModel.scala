package com.dridco.mongo

import play.api.libs.json.Json

object SampleModel {

  case class UserDto(userId: Double, name: String)

  object Implicits {

    implicit val userFormatJson = Json.format[UserDto]

  }

}