package Try2

import javafx.stage.Stage
import tornadofx.App


class MyApp2: App(MVCView::class, Styles2::class){
    override fun start(stage: Stage) {
        with(stage){
            width = 1200.0
            isResizable = false
        }
        super.start(stage)
    }



}