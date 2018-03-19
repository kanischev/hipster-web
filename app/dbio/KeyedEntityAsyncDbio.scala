package dbio

import models.Tables.{TypedKeyedTable, _}
import models.Tables.profile.api._
import models.TypedKeyedEntity
import slick.lifted.TableQuery

trait KeyedEntityAsyncDbio[E <: TypedKeyedEntity[K], T <: TypedKeyedTable[E, K], K] {
  def table: TableQuery[T]

  def insert(entity: E): DBIO[E] = {
    (table returning table) += entity
  }

  def update(item: E): DBIO[Int]

  def delete(id: K): DBIO[Int]

  def lookup(id: K): DBIO[Option[E]]

  def insertAll(entities: Seq[E]): DBIO[Option[Int]] = {
    table ++= entities
  }

  def listAll: DBIO[Seq[E]] = {
    table.result
  }
}

trait LongKeyedEntityAsyncDbio[E <: TypedKeyedEntity[Long], T <: TypedKeyedTable[E, Long]] extends KeyedEntityAsyncDbio[E, T, Long] {
  def lookup(id: Long): DBIO[Option[E]] = {
    (for (e <- table.filter(_.id === id)) yield e).result.headOption
  }

  def update(entity: E): DBIO[Int] = {
    table.filter(_.id === entity.id).update(entity)
  }

  override def delete(id: Long): DBIO[Int] = {
    table.filter(_.id === id).delete
  }
}

trait StringKeyedEntityAsyncDbio[E <: TypedKeyedEntity[String], T <: TypedKeyedTable[E, String]] extends KeyedEntityAsyncDbio[E, T, String] {
  def lookup(id: String): DBIO[Option[E]] = {
    (for (e <- table.filter(_.id === id)) yield e).result.headOption
  }

  def update(entity: E): DBIO[Int] = {
    table.filter(_.id === entity.id).update(entity)
  }

  override def delete(id: String): DBIO[Int] = {
    table.filter(_.id === id).delete
  }
}
