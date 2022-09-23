package piano.begin

import javafx.stage.Stage
import piano.view.PianoView
import tornadofx.App

// what is two dots ::
// file chooser
// file saver like txt or binary
class PianoApp: App(PianoView::class, Styles3::class){
    override fun start(stage: Stage) {
        with(stage){
            isResizable = true
        }
        super.start(stage)
    }
}