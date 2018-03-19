package models

import java.time._
// AUTO-GENERATED Slick data model

trait TypedKeyedEntity[K] {
  def id: K
}

/** Stand-alone Slick data model for immediate use */
object Tables {
  val profile = models.HipsterPostgresProfile
  import profile.api._

  abstract class TypedKeyedTable[T <: TypedKeyedEntity[K], K](_tableTag: Tag, schema: Option[String], _tableName: String) extends profile.api.Table[T](_tableTag, schema, _tableName) {
    def this(_tableTag: Tag, _tableName: String) = this(_tableTag, None, _tableName)
    def id: Rep[K]
  }

  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Role.schema ++ UserAccount.schema ++ UserAuthToken.schema ++ UserRole.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Role
   *  @param id Database column id SqlType(varchar), PrimaryKey, Length(255,true)
   *  @param description Database column description SqlType(text), Default(None) */
  case class RoleRow(id: String, description: Option[String] = None) extends TypedKeyedEntity[String]
  /** GetResult implicit for fetching RoleRow objects using plain SQL queries */
  implicit def GetResultRoleRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[RoleRow] = GR{
    prs => import prs._
    RoleRow.tupled((<<[String], <<?[String]))
  }
  /** Table description of table role. Objects of this class serve as prototypes for rows in queries. */
  class Role(_tableTag: Tag) extends TypedKeyedTable[RoleRow, String](_tableTag, "role") {
    def * = (id, description) <> (RoleRow.tupled, RoleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description).shaped.<>({r=>import r._; _1.map(_=> RoleRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column description SqlType(text), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
  }
  /** Collection-like TableQuery object for table Role */
  lazy val Role = new TableQuery(tag => new Role(tag))

  /** Entity class storing rows of table UserAccount
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(varchar), Length(1000,true), Default(None)
   *  @param passwordHash Database column password_hash SqlType(varchar), Length(1000,true), Default(None)
   *  @param passwordSalt Database column password_salt SqlType(varchar), Length(1000,true), Default(None)
   *  @param email Database column email SqlType(varchar), Length(1000,true), Default(None)
   *  @param firstName Database column first_name SqlType(varchar), Length(1000,true), Default(None)
   *  @param middleName Database column middle_name SqlType(varchar), Length(1000,true), Default(None)
   *  @param lastName Database column last_name SqlType(varchar), Length(1000,true), Default(None)
   *  @param registerDate Database column register_date SqlType(timestamp), Default(None)
   *  @param temporalPassword Database column temporal_password SqlType(bool), Default(None)
   *  @param disabled Database column disabled SqlType(bool), Default(None)
   *  @param mailVerified Database column mail_verified SqlType(bool), Default(None)
   *  @param verificationCode Database column verification_code SqlType(varchar), Length(1000,true), Default(None)
   *  @param lastLogin Database column last_login SqlType(timestamp), Default(None) */
  case class UserAccountRow(id: Long, login: Option[String] = None, passwordHash: Option[String] = None, passwordSalt: Option[String] = None, email: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, lastName: Option[String] = None, registerDate: Option[java.time.LocalDateTime] = None, temporalPassword: Option[Boolean] = None, disabled: Option[Boolean] = None, mailVerified: Option[Boolean] = None, verificationCode: Option[String] = None, lastLogin: Option[java.time.LocalDateTime] = None) extends TypedKeyedEntity[Long]
  /** GetResult implicit for fetching UserAccountRow objects using plain SQL queries */
  implicit def GetResultUserAccountRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[java.time.LocalDateTime]], e3: GR[Option[Boolean]]): GR[UserAccountRow] = GR{
    prs => import prs._
    UserAccountRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[java.time.LocalDateTime], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[String], <<?[java.time.LocalDateTime]))
  }
  /** Table description of table user_account. Objects of this class serve as prototypes for rows in queries. */
  class UserAccount(_tableTag: Tag) extends TypedKeyedTable[UserAccountRow, Long](_tableTag, "user_account") {
    def * = (id, login, passwordHash, passwordSalt, email, firstName, middleName, lastName, registerDate, temporalPassword, disabled, mailVerified, verificationCode, lastLogin) <> (UserAccountRow.tupled, UserAccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), login, passwordHash, passwordSalt, email, firstName, middleName, lastName, registerDate, temporalPassword, disabled, mailVerified, verificationCode, lastLogin).shaped.<>({r=>import r._; _1.map(_=> UserAccountRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(varchar), Length(1000,true), Default(None) */
    val login: Rep[Option[String]] = column[Option[String]]("login", O.Length(1000,varying=true), O.Default(None))
    /** Database column password_hash SqlType(varchar), Length(1000,true), Default(None) */
    val passwordHash: Rep[Option[String]] = column[Option[String]]("password_hash", O.Length(1000,varying=true), O.Default(None))
    /** Database column password_salt SqlType(varchar), Length(1000,true), Default(None) */
    val passwordSalt: Rep[Option[String]] = column[Option[String]]("password_salt", O.Length(1000,varying=true), O.Default(None))
    /** Database column email SqlType(varchar), Length(1000,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(1000,varying=true), O.Default(None))
    /** Database column first_name SqlType(varchar), Length(1000,true), Default(None) */
    val firstName: Rep[Option[String]] = column[Option[String]]("first_name", O.Length(1000,varying=true), O.Default(None))
    /** Database column middle_name SqlType(varchar), Length(1000,true), Default(None) */
    val middleName: Rep[Option[String]] = column[Option[String]]("middle_name", O.Length(1000,varying=true), O.Default(None))
    /** Database column last_name SqlType(varchar), Length(1000,true), Default(None) */
    val lastName: Rep[Option[String]] = column[Option[String]]("last_name", O.Length(1000,varying=true), O.Default(None))
    /** Database column register_date SqlType(timestamp), Default(None) */
    val registerDate: Rep[Option[java.time.LocalDateTime]] = column[Option[java.time.LocalDateTime]]("register_date", O.Default(None))
    /** Database column temporal_password SqlType(bool), Default(None) */
    val temporalPassword: Rep[Option[Boolean]] = column[Option[Boolean]]("temporal_password", O.Default(None))
    /** Database column disabled SqlType(bool), Default(None) */
    val disabled: Rep[Option[Boolean]] = column[Option[Boolean]]("disabled", O.Default(None))
    /** Database column mail_verified SqlType(bool), Default(None) */
    val mailVerified: Rep[Option[Boolean]] = column[Option[Boolean]]("mail_verified", O.Default(None))
    /** Database column verification_code SqlType(varchar), Length(1000,true), Default(None) */
    val verificationCode: Rep[Option[String]] = column[Option[String]]("verification_code", O.Length(1000,varying=true), O.Default(None))
    /** Database column last_login SqlType(timestamp), Default(None) */
    val lastLogin: Rep[Option[java.time.LocalDateTime]] = column[Option[java.time.LocalDateTime]]("last_login", O.Default(None))

    /** Uniqueness Index over (email) (database name user_account_email_key) */
    val index1 = index("user_account_email_key", email, unique=true)
  }
  /** Collection-like TableQuery object for table UserAccount */
  lazy val UserAccount = new TableQuery(tag => new UserAccount(tag))

  /** Entity class storing rows of table UserAuthToken
   *  @param id Database column id SqlType(varchar), PrimaryKey, Length(255,true)
   *  @param userId Database column user_id SqlType(int8), Default(None)
   *  @param host Database column host SqlType(varchar), Length(255,true), Default(None)
   *  @param lastRequestPath Database column last_request_path SqlType(text), Default(None)
   *  @param creationTime Database column creation_time SqlType(timestamp)
   *  @param lastRequestTime Database column last_request_time SqlType(timestamp), Default(None)
   *  @param expiryTime Database column expiry_time SqlType(timestamp) */
  case class UserAuthTokenRow(id: String, userId: Option[Long] = None, host: Option[String] = None, lastRequestPath: Option[String] = None, creationTime: java.time.LocalDateTime, lastRequestTime: Option[java.time.LocalDateTime] = None, expiryTime: java.time.LocalDateTime) extends TypedKeyedEntity[String]
  /** GetResult implicit for fetching UserAuthTokenRow objects using plain SQL queries */
  implicit def GetResultUserAuthTokenRow(implicit e0: GR[String], e1: GR[Option[Long]], e2: GR[Option[String]], e3: GR[java.time.LocalDateTime], e4: GR[Option[java.time.LocalDateTime]]): GR[UserAuthTokenRow] = GR{
    prs => import prs._
    UserAuthTokenRow.tupled((<<[String], <<?[Long], <<?[String], <<?[String], <<[java.time.LocalDateTime], <<?[java.time.LocalDateTime], <<[java.time.LocalDateTime]))
  }
  /** Table description of table user_auth_token. Objects of this class serve as prototypes for rows in queries. */
  class UserAuthToken(_tableTag: Tag) extends TypedKeyedTable[UserAuthTokenRow, String](_tableTag, "user_auth_token") {
    def * = (id, userId, host, lastRequestPath, creationTime, lastRequestTime, expiryTime) <> (UserAuthTokenRow.tupled, UserAuthTokenRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), userId, host, lastRequestPath, Rep.Some(creationTime), lastRequestTime, Rep.Some(expiryTime)).shaped.<>({r=>import r._; _1.map(_=> UserAuthTokenRow.tupled((_1.get, _2, _3, _4, _5.get, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column user_id SqlType(int8), Default(None) */
    val userId: Rep[Option[Long]] = column[Option[Long]]("user_id", O.Default(None))
    /** Database column host SqlType(varchar), Length(255,true), Default(None) */
    val host: Rep[Option[String]] = column[Option[String]]("host", O.Length(255,varying=true), O.Default(None))
    /** Database column last_request_path SqlType(text), Default(None) */
    val lastRequestPath: Rep[Option[String]] = column[Option[String]]("last_request_path", O.Default(None))
    /** Database column creation_time SqlType(timestamp) */
    val creationTime: Rep[java.time.LocalDateTime] = column[java.time.LocalDateTime]("creation_time")
    /** Database column last_request_time SqlType(timestamp), Default(None) */
    val lastRequestTime: Rep[Option[java.time.LocalDateTime]] = column[Option[java.time.LocalDateTime]]("last_request_time", O.Default(None))
    /** Database column expiry_time SqlType(timestamp) */
    val expiryTime: Rep[java.time.LocalDateTime] = column[java.time.LocalDateTime]("expiry_time")

    /** Foreign key referencing UserAccount (database name user_auth_token_user_id_fkey) */
    lazy val userAccountFk = foreignKey("user_auth_token_user_id_fkey", userId, UserAccount)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserAuthToken */
  lazy val UserAuthToken = new TableQuery(tag => new UserAuthToken(tag))

  /** Entity class storing rows of table UserRole
   *  @param userAccountId Database column user_account_id SqlType(int8)
   *  @param roleId Database column role_id SqlType(varchar), Length(255,true) */
  case class UserRoleRow(userAccountId: Long, roleId: String)
  /** GetResult implicit for fetching UserRoleRow objects using plain SQL queries */
  implicit def GetResultUserRoleRow(implicit e0: GR[Long], e1: GR[String]): GR[UserRoleRow] = GR{
    prs => import prs._
    UserRoleRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table user_role. Objects of this class serve as prototypes for rows in queries. */
  class UserRole(_tableTag: Tag) extends profile.api.Table[UserRoleRow](_tableTag, "user_role") {
    def * = (userAccountId, roleId) <> (UserRoleRow.tupled, UserRoleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userAccountId), Rep.Some(roleId)).shaped.<>({r=>import r._; _1.map(_=> UserRoleRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_account_id SqlType(int8) */
    val userAccountId: Rep[Long] = column[Long]("user_account_id")
    /** Database column role_id SqlType(varchar), Length(255,true) */
    val roleId: Rep[String] = column[String]("role_id", O.Length(255,varying=true))

    /** Primary key of UserRole (database name user_role_pkey) */
    val pk = primaryKey("user_role_pkey", (userAccountId, roleId))

    /** Foreign key referencing Role (database name user_role_role_id_fkey) */
    lazy val roleFk = foreignKey("user_role_role_id_fkey", roleId, Role)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing UserAccount (database name user_role_user_account_id_fkey) */
    lazy val userAccountFk = foreignKey("user_role_user_account_id_fkey", userAccountId, UserAccount)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserRole */
  lazy val UserRole = new TableQuery(tag => new UserRole(tag))
}
