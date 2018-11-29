package application
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import javax.inject._
import play.api.Configuration

case class Location(lat: Double, long: Double)

case class Place(name: String, location: Location)

object Place {
  var list: List[Place] = {
    List(
      Place(
        "Sandleford",
        Location(51.377797, -1.318965)
      ),
      Place(
        "Watership Down",
        Location(51.235685, -1.309197)
      )
    )
  }
  def save(place: Place) = {
    list = list ::: List(place)
  }
}
class ApiApplication @Inject() (cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {
  implicit val locationWrites: Writes[Location] = (
    (__ \ "lat").write[Double] and
    (__ \ "long").write[Double]
  )(unlift(Location.unapply))

  implicit val placeWrites: Writes[Place] = (
    (__ \ "name").write[String] and
    (__ \ "location").write[Location]
  )(unlift(Place.unapply))

  implicit val locationReads: Reads[Location] = (
    (__ \ "lat").read[Double] and
    (__ \ "long").read[Double]
  )(Location.apply _)

  implicit val placeReads: Reads[Place] = (
    (__ \ "name").read[String] and
    (__ \ "location").read[Location]
  )(Place.apply _)

  // implicit val placeWrites = Json.writes[Place]
  // implicit val placeReads = Json.reads[Place]

  def listPlaces = Action {
    val json = Json.toJson(Place.list)
    Ok(json)
  }

  def savePlace = Action(parse.json) { request =>
    val placeResult = request.body.validate[Place]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      place => {
        Place.save(place)
        Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + place.name + "' saved.")))
      }
    )
  }

  def playTransform = Action { request =>
    val json = Json.obj("key1" -> "value1", "key2" -> Json.obj("key21" -> "value21", "key22" -> "value22"))
    val jsonTransformer = (__ \ "key2").json.pick
    Ok(json.transform(jsonTransformer).toString)
  }

  def getConfiguration = Action { request =>
    Ok(config.get[String]("db.default.username"))
  }
}
