package models

import scala.collection.mutable

object InMemoryModel {
  private val users = mutable.Map[String, String]("admin" -> "password")
  private val classes = mutable.Map[String, Seq[(String, String)]](
    "admin" -> Seq[(String, String)](
      "15213" -> "Introduction to Computer Systems",
      "105" -> "Programming Languages",
      "40" -> "Machine Structures"
    )
  )
  private val posts = mutable.Map[String, List[String]]("15213" -> List("post 1", "post 2", "post 3"))

  def validateUser(username: String, password: String): Boolean =
    (users.get(username) map (_ == password)).getOrElse(false)

  def createUser(username: String, password: String): Boolean =
    if (users.contains(username)) false
    else {
      users(username) = password
      true
    }

  def getClasses(username: String): Seq[(String, String)] =
    classes.get(username).getOrElse(Seq())

  def getPost(classId: String): Seq[String] =
    posts.get(classId).getOrElse(Nil)

  def addPost(classId: String, post: String): Unit =
    posts(classId) = post :: posts.get(classId).getOrElse(Nil)

  def removePost(classId: String, index: Int): Boolean = {
    if (posts.get(classId).isEmpty || index < 0 || index > posts(classId).length) false
    else {
      posts(classId) = posts(classId).patch(index, Nil, 1)
      true
    }
  }
}
