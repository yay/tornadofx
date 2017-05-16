package tornadofx.tests

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.junit.Test
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import tornadofx.column
import tornadofx.makeIndexColumn
import tornadofx.tableview
import tornadofx.value
import java.nio.file.Paths

class TableViewTest {
    class TestObject(i: Int) {
        val A = SimpleIntegerProperty(5 * i)
        val B = SimpleDoubleProperty(3.14159 * i)
        val C = SimpleStringProperty("Test string $i")
    }

    val TestList = FXCollections.observableArrayList(Array(5, ::TestObject).asList())

    val primaryStage: Stage = FxToolkit.registerPrimaryStage()

    @Test
    fun columnTest() {
        FxToolkit.setupFixture {
            val root = StackPane().apply {
                tableview(TestList) {
                    makeIndexColumn()
                    column("A Column", TestObject::A)
                    column("A Column", Boolean::class).x()
                    column("B Column", Double::class) {
                        x()
                        value { it.value.B }
                    }
                    column("C Column", TestObject::C)
                }
                setPrefSize(400.0, 160.0)
            }
            primaryStage.scene = Scene(root)
            primaryStage.show()
        }

        val robot = FxRobot()
        robot.robotContext().captureSupport.saveImage(robot.capture(primaryStage.scene.root), Paths.get("example-table.png"))
    }
}

inline fun <T, reified S : Any> TableColumn<T, S>.x(): TableColumn<T, S> {
    tableView?.isEditable = true
    isEditable = true
    println(S::class)
    println(S::class.javaPrimitiveType)
    println(S::class.java)
    val type = S::class.javaPrimitiveType ?: S::class
    println(type == Double::class.java)
    return this
}
