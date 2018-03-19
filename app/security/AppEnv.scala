package security

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator

trait AppEnv extends Env {
  type I = AppUser
  type A = CookieAuthenticator
}