package com.dridco.mongo

import play.api.libs.json.Json

object SampleDto {

  case class UserDto(userId: Double, name: String)

  object Implicits {

    implicit val userFormatJson = Json.format[UserDto]

  }

}