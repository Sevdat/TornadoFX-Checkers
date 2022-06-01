package com.example

import com.example.view.MainView
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import tornadofx.App
import tornadofx.GridPaneConstraint
import tornadofx.tilepane
import java.awt.Rectangle

class MyApp: App(MainView::class, Styles::class){
    override fun start(stage: Stage) {
        var pane = GridPane()
        var count = 0


        with(stage){
            isResizable = false

        }


        super.start(stage)
    }


}