package Try2

import javafx.stage.Stage
import tornadofx.App


class `2-MyApp`: App(MVCView::class, Styles2::class){
    override fun start(stage: Stage) {
        with(stage){
            isResizable = true
        }
        super.start(stage)
    }


}

