package controllers

import com.example.playscalajs.shared.SharedMessages
import models.InMemoryModel
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import javax.inject._
import play.api.db.slick._
import scala.concurrent.ExecutionContext
import slick.driver.JdbcProfile
import slick.jdbc.PostgresProfile.api._

// handling submission: https://www.playframework.com/documentation/2.8.x/ScalaForms
case class UserData(username: String, password: String)
case class ClassData(classId: String)

@Singleton
class Application @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val controllerComponents: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesBaseController with HasDatabaseConfigProvider[JdbcProfile]{

  val classSelectForm = Form(mapping(
    "Class" -> text
  )(ClassData.apply)(ClassData.unapply))

  val loginForm = Form(mapping(
    "Username" -> text,
    "Password" -> text
  )(UserData.apply)(UserData.unapply))

  val signupForm = Form(mapping(
    "Username" -> text(3, 12),
    "Password" -> text(6)
  )(UserData.apply)(UserData.unapply))

  def index = Action { implicit request =>
    request.session.get("username") match {
      case Some(username: String) => Ok(views.html.index(SharedMessages.itWorks, classSelectForm, InMemoryModel.getClasses(username)))
      case None => Ok(views.html.index(SharedMessages.itWorks, classSelectForm, Seq[(String, String)]()))
    }
    
  }

  def login() = Action { implicit request =>
    Ok(views.html.login(loginForm, signupForm))
  }

  def validateLoginForm = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors, formWithErrors)), // f := formsWithErrors
      loginData =>
        if (InMemoryModel.validateUser(loginData.username, loginData.password)) {
          val username = loginData.username
          Redirect(routes.Application.load)
            .withSession("username" -> username)
            .flashing("success" -> s"Hello $username!")
        } else Redirect(routes.Application.login()).flashing("error" -> "Invalid username/password")
    )
  }

  def logout() = Action {
    Redirect(routes.Application.login())
      .withNewSession
      .flashing("success" -> "user logged out")
  }

  def createUser = Action{ request =>
    val postVal = request.body.asFormUrlEncoded
    postVal.map { args =>
      val username = args("Username").head
      val password = args("Password").head
      if (InMemoryModel.createUser(username, password)) {
        Redirect(routes.Application.load).withSession("username" -> username)
      } else Redirect(routes.Application.login()).flashing("error" -> "user existed")
    }.getOrElse(Redirect("login"))
  }

  def createUserForm = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors, formWithErrors)),
      registerData =>
        if (InMemoryModel.validateUser(registerData.username, registerData.password)) {
          val username = registerData.username
          val password = registerData.password
          if (InMemoryModel.createUser(username, password)) {
            Redirect(routes.Application.load).withSession("username" -> username)
          } else Redirect(routes.Application.login()).flashing("error" -> "user existed")
        } else Redirect(routes.Application.login()).flashing("error" -> "Invalid username/password")
    )
  }

  def load = Action { implicit request =>
    request.session.get("username") match {
      case Some(username: String) => Ok("Loading classes...")// Ok(InMemoryModel.getClasses(username))
      case None => Ok("Please login first")
    }
  }

  def goToClass(classId: Int) = Action { implicit request =>
    request.session.get("username") match {
      case Some(username: String) => Ok(views.html.classPage(classId))
      case None => Redirect("login")
    }
  }

  def emptyCall = Action { Ok("It works") }

  def validateClassSelectForm = Action { implicit request =>
    classSelectForm.bindFromRequest.fold(
      formWithErrors => BadRequest, // f := formsWithErrors
      classData => Redirect(routes.Application.goToClass(classData.classId.toInt))
    )
  }
}
