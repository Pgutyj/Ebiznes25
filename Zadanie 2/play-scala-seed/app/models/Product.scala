package Products
import play.api.libs.json._
import scala.io.Source
import play.api.Environment
import java.nio.file.{Files, Paths, StandardOpenOption}

case class Product(id: Int, name: String, cena: Double, kraj_pochodzenia: String, waga: Double, jednostka: String, kategoria: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}

object ProductService {
  def loadProducts(): List[Product] = {
    val jsonStr = Source.fromFile("app/models/Products.json").mkString
    val json = Json.parse(jsonStr)
    

    (json \ "products").as[List[Product]]
  }
  def getById(id: Int): Option[Product] = {
    loadProducts().find(_.id == id)
  }

  def deleteById(id: Int): Option[Product] = {
    val products = loadProducts()
    val toDelete =  products.find(_.id == id)

    val updatedProducts = products.filterNot(_.id == id)
        
    val updatedJson = Json.obj("products" -> Json.toJson(updatedProducts))

    Files.write(
          Paths.get("app/models/Products.json"), 
          Json.prettyPrint(updatedJson).getBytes, 
          StandardOpenOption.TRUNCATE_EXISTING
    )

    toDelete
  }

  def AddRecord(newProduct: Product): List[Product] = {
    val products = loadProducts()
    val updatedProducts = products :+ newProduct
    val updatedJson = Json.obj("products" -> Json.toJson(updatedProducts))

    Files.write(
    Paths.get("app/models/Products.json"), 
    Json.prettyPrint(updatedJson).getBytes, 
    StandardOpenOption.TRUNCATE_EXISTING
    )

    updatedProducts

  }
  def updateRecord(id: Int, updatedProduct: Product): List[Product] = {
    val products = loadProducts()

    val updatedProducts = products.map { product =>
        if (product.id == id) updatedProduct else product
    }

    val updatedJson = Json.obj("products" -> Json.toJson(updatedProducts))

    Files.write(
      Paths.get("app/models/Products.json"), 
      Json.prettyPrint(updatedJson).getBytes, 
      StandardOpenOption.TRUNCATE_EXISTING
    )

    products
  }

}