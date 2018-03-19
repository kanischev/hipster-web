package codegen

import java.time.{LocalDate, LocalDateTime, LocalTime}

import slick.codegen.SourceCodeGenerator
import slick.model.Model
import slick.sql.SqlProfile.ColumnOption

class TablesCodeGen(m: Model)
  extends SourceCodeGenerator(m.copy(tables = m.tables.filter(_.name.table != "play_evolutions"))) {
  override def Table = new Table(_) { table =>
    private def pkColumnOpt = table.primaryKey
      .filter(t => t.columns.size == 1 && t.columns.forall(_.model.name == "id"))
      .flatMap(_.columns.headOption)
      .orElse(table.columns.find(_.model.name == "id"))

    override def EntityType = new EntityType{
      override def parents: Seq[String] = super.parents ++ pkColumnOpt
        .map(c => s"TypedKeyedEntity[${c.model.tpe}]")
    }

    override def TableClass = new TableClass {
      override def code: String = pkColumnOpt.foldLeft(super.code){case (code, col) => code.replaceAllLiterally(s"profile.api.Table[$elementType]", s"TypedKeyedTable[$elementType, ${col.model.tpe}]")}
    }

    override def Column = new Column(_) { column =>
      // customize db type -> scala type mapping, pls adjust it according to your environment
      override def rawType: String = model.tpe match {
        case "java.sql.Date" => classOf[LocalDate].getName
        case "java.sql.Time" => classOf[LocalTime].getName
        case "java.sql.Timestamp" => classOf[LocalDateTime].getName
        // currently, all types that's not built-in support were mapped to `String`
        case "String" => model.options.find(_.isInstanceOf[ColumnOption.SqlType]).map(_.asInstanceOf[ColumnOption.SqlType].typeName).map({
          case "hstore" => "Map[String, String]"
          case "geometry" => "com.vividsolutions.jts.geom.Geometry"
          case "int8[]" => "List[Long]"
          case _ =>  "String"
        }).getOrElse("String")
        case _ => super.rawType
      }
    }
  }

  // ensure to use our customized postgres driver at `import profile.simple._`
  override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]) : String = {
    s"""
package $pkg

import java.time._
// AUTO-GENERATED Slick data model

trait TypedKeyedEntity[K] {
  def id: K
}

/** Stand-alone Slick data model for immediate use */
object $container${parentType.map(t => s" extends $t").getOrElse("")} {
  val profile = $profile
  import profile.api._

  abstract class TypedKeyedTable[T <: TypedKeyedEntity[K], K](_tableTag: Tag, schema: Option[String], _tableName: String) extends profile.api.Table[T](_tableTag, schema, _tableName) {
    def this(_tableTag: Tag, _tableName: String) = this(_tableTag, None, _tableName)
    def id: Rep[K]
  }

  ${indent(code)}
}
      """.trim()
  }
}