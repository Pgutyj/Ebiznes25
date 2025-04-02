package controllers
import javax.inject._
import play.api.mvc._
import Products._
import play.api.libs.json._
import scala.collection.mutable.ArrayBuffer
import play.api.Logger
import play.filters.csrf._

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def ShowAll() = Action {
     val products = ProductService.loadProducts()
        Ok(views.html.product(products))

  }

   def showProduct(id: Int) = Action { implicit request =>
     val product = ProductService.getById(id)
          Ok(views.html.show_product(product))
   }

    def addProduct() = Action { implicit request =>
          
      request.method match {
         case "POST" =>
               val formData = request.body.asFormUrlEncoded

               val name = formData.flatMap(_.get("name").flatMap(_.headOption)).getOrElse("")
               val price = formData.flatMap(_.get("price").flatMap(_.headOption)).flatMap(s => scala.util.Try(s.toDouble).toOption).getOrElse(0.0)
               val country = formData.flatMap(_.get("country").flatMap(_.headOption)).getOrElse("")
               val weight = formData.flatMap(_.get("weight").flatMap(_.headOption)).flatMap(s => scala.util.Try(s.toDouble).toOption).getOrElse(0.0)
               val unit = formData.flatMap(_.get("unit").flatMap(_.headOption)).getOrElse("")
               val category = formData.flatMap(_.get("category").flatMap(_.headOption)).getOrElse("")
               val products = ProductService.loadProducts()
               val newId = if (products.isEmpty) 1 else products.map(_.id).max + 1

               val newProduct = Product(newId, name, price, country, weight, unit, category)

               ProductService.AddRecord(newProduct)

               Redirect(routes.ProductController.ShowAll()).flashing("success" -> "Produkt został dodany.")
    

         case "GET" =>
            Ok(views.html.add_product())
      
      }
   }

   def deleteProduct(id: Int) = Action { implicit request =>
     val product = ProductService.getById(id)


      request.method match {
         case "POST" =>
            val deleted = ProductService.deleteById(id)
            Redirect(routes.ProductController.ShowAll()).flashing("success" -> "Produkt został usunięty.")

         case "GET" =>
            Ok(views.html.delete_product(product))
      
      }
   }

   def updateProduct(id: Int) = Action { implicit request =>
     val product = ProductService.getById(id)


      request.method match {
         case "POST" =>
               val formData = request.body.asFormUrlEncoded

               val name = formData.flatMap(_.get("name").flatMap(_.headOption)).getOrElse("")
               val price = formData.flatMap(_.get("price").flatMap(_.headOption)).flatMap(s => scala.util.Try(s.toDouble).toOption).getOrElse(0.0)
               val country = formData.flatMap(_.get("country").flatMap(_.headOption)).getOrElse("")
               val weight = formData.flatMap(_.get("weight").flatMap(_.headOption)).flatMap(s => scala.util.Try(s.toDouble).toOption).getOrElse(0.0)
               val unit = formData.flatMap(_.get("unit").flatMap(_.headOption)).getOrElse("")
               val category = formData.flatMap(_.get("category").flatMap(_.headOption)).getOrElse("")
               val products = ProductService.loadProducts()

               val newProduct = Product(id, name, price, country, weight, unit, category)
               ProductService.updateRecord(id, newProduct)

               Redirect(routes.ProductController.ShowAll()).flashing("success" -> "Produkt został zaktualizowany.")

         case "GET" =>
            Ok(views.html.update_product(product))
      
      }
   }
}
