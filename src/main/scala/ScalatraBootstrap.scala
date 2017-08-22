import javax.servlet.ServletContext

import com.egen.app.controller.EntityController
import org.scalatra._

import scala.language.postfixOps

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new EntityController, "/*")
  }
}
