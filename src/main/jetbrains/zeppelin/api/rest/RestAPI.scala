package jetbrains.zeppelin.api.rest

import java.net.HttpCookie

import scalaj.http.{Http, HttpOptions, HttpResponse}
import spray.json._


final case class RestApiException(private val message: String = "",
                                  private val cause: Throwable = None.orNull) extends Exception(message, cause)

class RestAPI(host: String, port: Int, https: Boolean = false) {
  private val protocol = if (https) "https" else "http"
  private val apiUrl = s"$protocol://$host:$port/api"


  def performGetRequest(uri: String, cookie: Option[HttpCookie]): HttpResponse[String] = {
    var request = Http(apiUrl + uri)
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000))

    if (cookie.isDefined) {
      request = request.cookie(cookie.get)
    }
    request.asString
  }

  def performPostData(uri: String, data: JsValue = JsObject(), cookie: Option[HttpCookie]): HttpResponse[String] = {
    var request = Http(apiUrl + uri).postData(data.compactPrint)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000))

    if (cookie.isDefined) {
      request = request.cookie(cookie.get)
    }
    val result = request.asString
    result
  }

  def performPostForm(uri: String, params: Map[String, String]): HttpResponse[String] = {
    val paramString = if (params.nonEmpty) "?" + params.map(_.productIterator.mkString("=")).mkString("&")
    val result = Http("http://localhost:8080/api/login" + paramString).postForm
      .option(HttpOptions.readTimeout(10000)).asString
    result
  }

  def performPutData(uri: String, data: JsValue = JsObject(), cookie: Option[HttpCookie]): HttpResponse[String] = {
    var request = Http(apiUrl + uri).put(data.compactPrint)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000))

    if (cookie.isDefined) {
      request = request.cookie(cookie.get)
    }
    val result = request.asString
    result
  }
}
