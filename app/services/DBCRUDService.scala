package services

import dbio.KeyedEntityAsyncDbio
import models.Tables.TypedKeyedTable
import models.TypedKeyedEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait DBCRUDService[E <: TypedKeyedEntity[K], K] extends HasDatabaseConfigProvider[JdbcProfile] {
  def dbio: KeyedEntityAsyncDbio[E, _ <: TypedKeyedTable[E, K], K]

  def lookup(id: K): Future[Option[E]] = db.run(dbio.lookup(id))

  def insert(entity: E): Future[E] = db.run(dbio.insert(entity))

  def update(entity: E): Future[Int] = db.run(dbio.update(entity))

  def delete(id: K): Future[Int] = db.run(dbio.delete(id))

  def insertAll(entities: Seq[E]): Future[Option[Int]] = db.run(dbio.insertAll(entities))

  def listAll: Future[Seq[E]] = db.run(dbio.listAll)
}
