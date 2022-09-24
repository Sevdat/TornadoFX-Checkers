package piano.view

import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import piano.controller.*
import piano.model.instrumentLoader
import tornadofx.*

class PianoView : View("Piano") {
    override val root = pane()
    init {
        instrumentLoader.open()
        with(root){
            keySetup(amountOfKeySets)
            for ((i,e) in coordinateKeys.withIndex()){
                pianoKeys += Rectangle(e.x,e.y,e.keyWidth,e.keyHeight).apply {
                    if (e.keyHeight != 75.0) {
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
                            SaveSong().openWindow()
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

class SaveSong : View("Save") {

    override val root = pane()
    init {
        form {
            val save = button("Save")
            val dontSave = button("Don't Save")

            dontSave.setOnAction {
                close()
            }

            save.setOnAction {
                dontSave.isVisible = false
                save.isVisible = false
                currentWindow?.apply {
                    this.width = 300.0
                    this.height = 300.0
                }
                vbox {
                    fieldset("Song Name") {
                        val s = textfield().characters
                        button("Save and Close").setOnAction {
                            namedSong +=  "$s"
                            close()
                        }
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
