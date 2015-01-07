package com.dridco.mongo

import play.api.Play.current
import play.api.libs.json.{Format, JsObject}
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.commands.LastError

import scala.concurrent.Future

trait MongoRepository {

  def collectionName: String
  def collection = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)

}

trait ObjectRepository[T] {

  self: MongoRepository =>

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def add(o: T)(implicit format: Format[T]): Future[LastError] = {
    collection.insert[T](o)
  }

  def saveOrUpdate(query: Option[JsObject], modifier: JsObject, o: T)(implicit format: Format[T]): Future[Any] = {
    findOneByQuery(query).map {
      case Some(found) => updateField(query, modifier)
      case None => add(o)
    }
  }

  def find()(implicit format: Format[T]): Future[List[T]] = {
    find(None)
  }

  def find(query: JsObject)(implicit format: Format[T]): Future[List[T]] = {
    find(Some(query))
  }

  private def find(query: Option[JsObject])(implicit format: Format[T]): Future[List[T]] = {
    collection.find(query.getOrElse(emptyQuery)).
      cursor[T].
      collect[List]()
  }

  def findOneByQuery(query: Option[JsObject])(implicit format: Format[T]): Future[Option[T]] = {
    collection.find(query.getOrElse(emptyQuery)).one[T]
  }

  private def emptyQuery = JsObject(List())

  def updateField(filter: Option[JsObject], modifier: JsObject)(implicit format: Format[T]): Future[Boolean] = filter match {
    case Some(query) => collection.update(query, modifier).map(le => le.updated > 0)
    case None => Future(false)
  }

  def remove(filter: Option[JsObject])(implicit format: Format[T]): Future[Boolean] = filter match {
    case Some(query) => collection.remove(query).map(le => le.updated > 0)
    case None => Future(false)
  }

}