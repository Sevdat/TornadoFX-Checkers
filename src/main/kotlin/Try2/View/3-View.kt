package Try2

import Try2.model.rectangleLocation
import Try2.model.rectanglePaint
import Try2.model.white
import javafx.scene.input.MouseEvent
import tornadofx.*

class MVCView : View("MVC App") {
    override val root = pane()

    init {
        with(root){
            for ((i,e) in MotionControl().boardSetUp.withIndex()){
                var sizeUp = 1
                if (i == zero) sizeUp = 8
                recList += rectangle(e.second,e.first, ratio*sizeUp,ratio*sizeUp).apply {
                    if (sizeUp != 8) fill = rectanglePaint
                }
                if (i != zero && i !in emptyBoard)
                    picList += circle(e.second + 50,e.first + 50, ratio/3,).apply {
                    if (i in whiteSide) fill = white
                }
            }
            rectangle(0,800,800,100)

            fun choose(evt: MouseEvent) {
                picList.firstOrNull {
                    val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                    it.contains(mousePt)
                }.apply {
                    if (this != null) {
                        MotionControl().delete(this)
                    }
                    println(picList.size)
                }
            }

            addEventFilter(MouseEvent.MOUSE_PRESSED, ::choose)

        }
    }

}



//class MVCView : View("MVC App") {
//
//    val form : MVCFormView by inject()
//    val status : MVCStatusView by inject()
//
//    override val root = vbox {
//        add(form)
//        separator()
//        add(status)
//
//        prefWidth = 480.0
//        prefHeight = 320.0
//
//    }
//}
//
//class MVCFormView : View() {
//
//    val data = SimpleStringProperty()
//
//    override val root = form {
//        fieldset {
//            field("Data") {
//                textfield(data)
//            }
//        }
//        button("Save") {
//            action {
//                find<MVCController>().save()  // breaks by inject cycle
//            }
//        }
//
//        padding = Insets(10.0)
//        vgrow = Priority.ALWAYS
//        alignment = Pos.CENTER_LEFT
//    }
//}
//
//class MVCStatusView : View() {
//
//    val lastMessage = SimpleStringProperty()
//
//    override val root = hbox {
//        label(lastMessage)
//        padding = Insets(4.0)
//        vgrow = Priority.NEVER
//    }
//}
//
//class MVCController : Controller() {
//
//    val form : MVCFormView by inject()
//    val status : MVCStatusView by inject()
//
//    fun save() {
//
//        val dataToSave = form.data.value
//
//        // execute save here
//        println("Saving $dataToSave")
//
////        status.lastMessage.value = "Data saved"
//    }
//}