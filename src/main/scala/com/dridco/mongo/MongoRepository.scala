package com.dridco.mongo

import play.api.libs.json.{ Json, JsObject, Format }
import play.api.libs.json.Json.JsValueWrapper
import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.collections.GenericCollection
import reactivemongo.core.commands.LastError
import scala.concurrent.Future
import scala.util.{ Success, Failure }

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

  def saveOrUpdate(query: Option[JsObject], modifier: Option[JsObject], o: T)(implicit format: Format[T]): Future[Any] = {
    val result = for {
      found <- findOneByQuery(query)
    } yield saveOrUpdate(found, query, modifier, o)

    result.recover {
      case e: Exception => throw new RuntimeException("Save or update with query: " + query)
    }
  }

  private def saveOrUpdate(found: Option[T], query: Option[JsObject], modifier: Option[JsObject], o: T)(implicit format: Format[T]): Future[Any] = found match {
    case Some(obj) => updateField(query.getOrElse(throw new RuntimeException("Mandatory Query")), modifier.get)
    case None => add(o)
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

  def updateField(query: JsObject, modifier: JsObject)(implicit format: Format[T]): Future[Boolean] = {
    collection.update(query, modifier).map(le => le.updated > 0)
  }

  def remove(query: JsObject)(implicit format: Format[T]): Future[Boolean] = {
    collection.remove(query).map(le => le.updated > 0)
  }

}