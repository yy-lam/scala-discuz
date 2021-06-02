package model

import scala.collection.mutable

object InMemoryModel {
  private val users = mutable.Map[String, String]("admin" -> "password")
  private val classes = mutable.Map[String, mutable.Map[Int, String]](
    "admin" -> mutable.Map[Int, String](
      15213 -> "Introduction to Computer Systems",
      105 -> "Programming Languages",
      40 -> "Machine Structures"
    )
  )
  private val posts = mutable.Map[Int, List[String]](15213 -> List("post 1", "post 2", "post 3"))

  def validateUser(username: String, password: String): Boolean =
    (users.get(username) map (_ == password)).getOrElse(false)

  def createUser(username: String, password: String): Boolean =
    if (users.contains(username)) false
    else {
      users(username) = password
      true
    }

  def getClasses(username: String): mutable.Map[Int, String] =
    classes.get(username).getOrElse(mutable.Map[Int,String]())

  def getPost(classId: Int): Seq[String] =
    posts.get(classId).getOrElse(Nil)

  def addPost(classId: Int, post: String): Unit =
    posts(classId) = post :: posts.get(classId).getOrElse(Nil)

  def removePost(classId: Int, index: Int): Boolean = {
    if (posts.get(classId).isEmpty || index < 0 || index > posts(classId).length) false
    else {
      posts(classId) = posts(classId).patch(index, Nil, 1)
      true
    }
  }
}
