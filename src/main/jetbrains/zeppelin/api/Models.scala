package jetbrains.zeppelin.api

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Config(enabled: Option[Boolean] = Some(true))

case class Notebook(id: String, name: String = "", paragraphs: List[Paragraph] = List())

case class Paragraph(id: String, jobName: String = "",
                     status: String = "",
                     user: String = "",
                     config: Config = Config(),
                     title: Option[String] = None,
                     text: Option[String] = None)


case class Credentials(principal: String, ticket: String, roles: String)


/**
  * Model for new notebook request by REST API
  */
case class NewNotebook(name: String)


object ZeppelinAPIProtocol extends DefaultJsonProtocol {
  implicit val configFormat: RootJsonFormat[Config] = jsonFormat(Config, "enabled")

  implicit val paragraphFormat: RootJsonFormat[Paragraph] = jsonFormat7(Paragraph)
  implicit val noteFormat: RootJsonFormat[Notebook] = jsonFormat3(Notebook)
  implicit val CredentialsFormat: RootJsonFormat[Credentials] = jsonFormat3(Credentials)

  implicit val NewNotebookFormat: RootJsonFormat[NewNotebook] = jsonFormat1(NewNotebook)
}
