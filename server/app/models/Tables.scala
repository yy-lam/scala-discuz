package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Posts.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Posts
   *  @param postId Database column post_id SqlType(serial), AutoInc, PrimaryKey
   *  @param postUser Database column post_user SqlType(int4), Default(None)
   *  @param text Database column text SqlType(varchar), Length(2000,true) */
  case class PostsRow(postId: Int, postUser: Option[Int] = None, text: String)
  /** GetResult implicit for fetching PostsRow objects using plain SQL queries */
  implicit def GetResultPostsRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String]): GR[PostsRow] = GR{
    prs => import prs._
    PostsRow.tupled((<<[Int], <<?[Int], <<[String]))
  }
  /** Table description of table posts. Objects of this class serve as prototypes for rows in queries. */
  class Posts(_tableTag: Tag) extends profile.api.Table[PostsRow](_tableTag, "posts") {
    def * = (postId, postUser, text) <> (PostsRow.tupled, PostsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(postId), postUser, Rep.Some(text))).shaped.<>({r=>import r._; _1.map(_=> PostsRow.tupled((_1.get, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column post_id SqlType(serial), AutoInc, PrimaryKey */
    val postId: Rep[Int] = column[Int]("post_id", O.AutoInc, O.PrimaryKey)
    /** Database column post_user SqlType(int4), Default(None) */
    val postUser: Rep[Option[Int]] = column[Option[Int]]("post_user", O.Default(None))
    /** Database column text SqlType(varchar), Length(2000,true) */
    val text: Rep[String] = column[String]("text", O.Length(2000,varying=true))

    /** Foreign key referencing Users (database name posts_post_user_fkey) */
    lazy val usersFk = foreignKey("posts_post_user_fkey", postUser, Users)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Posts */
  lazy val Posts = new TableQuery(tag => new Posts(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(20,true)
   *  @param password Database column password SqlType(varchar), Length(20,true) */
  case class UsersRow(id: Int, username: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (id, username, password) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column password SqlType(varchar), Length(20,true) */
    val password: Rep[String] = column[String]("password", O.Length(20,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
