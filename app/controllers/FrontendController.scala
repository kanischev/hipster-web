package controllers

import javax.inject._

import com.typesafe.config.ConfigList
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._
import scala.collection.JavaConverters._

/**
  * Frontend controller managing all static resource associate routes.
  * @param assets Assets controller reference.
  * @param cc Controller components reference.
  */
@Singleton
class FrontendController @Inject()(assets: Assets,
                                   errorHandler: HttpErrorHandler,
                                   config: Configuration,
                                   cc: ControllerComponents) extends AbstractController(cc) {

  lazy val apiPrefixes: Seq[String] = config.getOptional[ConfigList]("apiPrefixes").map(_.unwrapped().asScala.map(_.toString)).getOrElse(Nil)

  def index: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (apiPrefixes.exists(resource.startsWith)) {
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else index
  }
}