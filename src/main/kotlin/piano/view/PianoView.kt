package piano.view

import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.w3c.dom.css.Rect
import piano.controller.*
import tornadofx.*

class PianoView : View("Piano") {
    override val root = pane()
    init {
        instrumentLoader.open()
        with(root){
            keySetup(1)
            for ((i,e) in coordinateKeys.withIndex()){
                pianoKeys += Rectangle(e.x,e.y,e.keyWidth,e.keyHeight).apply {
                    if (i % 2 == 0) {
                        id = e.keyName
                        fill = e.color
                    } else {
                        id = e.keyName
                    }
                }
                println(pianoKeys[i].id)
            }
            root.getChildList()?.addAll(pianoKeys)
            for (i in root.getChildList()!!.filter { i -> (i as Rectangle).fill == Color.BLACK }) {
                i.toFront()
            }
            addEventFilter(MouseEvent.MOUSE_PRESSED, ::playKey)
        }
        // data class
        vbox {
            for (i in 0..2){
                var name = ""
                val record = "Record  "
                val stop = "Stop    "
                val library = "Library"

                when (i){
                    0 -> name = record
                    1 -> name = stop
                    2 -> name = library
                }
                button(name) {
                    when (name){
                        record -> setOnAction {
                            startRecord = true
                            now = System.currentTimeMillis()
                        }
                        stop -> setOnAction {
                            startRecord = false
                            if (notePair.isNotEmpty()){
                                libraryList += listOf(notePair)
                                notePair = listOf()
                            }
                        }
                        library -> setOnAction { SongLibrary().openWindow() }
                    }
                }
            }
        }
    }
}


class SongLibrary : View("Library") {

    override val root = pane()
    init {
        flowpane {
            for ((i,e) in libraryList.withIndex()){

                button("$i") {
                    setOnAction { libraryPlay(i) }
                }

            }

        }

    }
}
