package com.dridco.mongo

import SampleModel._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.indexes.{ Index, IndexType }
import reactivemongo.bson._

object SampleRepository {

  def apply() = new SampleRepository()

}

class SampleRepository extends BaseSampleRepository {

  collection.indexesManager.ensure(Index(key = Seq(("userId", IndexType(BSONInteger(1)))), unique = true));

}

trait BaseSampleRepository extends MongoRepository with ObjectRepository[UserDto] {

  def collectionName: String = "users"

}