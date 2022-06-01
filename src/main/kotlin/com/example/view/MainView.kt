package com.example.view

import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import tornadofx.*


class MainView : View("Russian Checkers") {

override val root = pane {
    val allCircles = mutableListOf<Circle>()
    val blackCircles = mutableListOf<Circle>()
    val whiteCircles = mutableListOf<Circle>()
    val pathList = mutableListOf<Circle>()
    val pick = mutableListOf<Circle>()

    fun blackPiece(startX: Double,startY: Double): Circle {
        return circle(startX, startY ,30){
            stroke = Color.WHITE
            strokeWidth = 3.0
            blackCircles.add(this)
            allCircles.add(this)
        }
    }
    fun whitePiece(startX: Double, startY: Double, f: Color): Circle {
        return circle(startX, startY ,30){
            fill = f
            stroke = Color.BLACK
            strokeWidth = 3.0
            whiteCircles.add(this)
            allCircles.add(this)
        }
    }
    fun path(startX: Double, startY: Double, f: Color): Circle {
        return circle(startX, startY ,15){
            fill = f
        }
    }
    fun choose(evt: MouseEvent) {

        allCircles.firstOrNull{
            val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
            it.contains(mousePt)
        }.apply {

            pathList.firstOrNull{
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
            }.apply {
                pathList.forEach { it.toFront() }
                if (this != null) {
                    val yo = this.centerX - 30
                    val oy = this.centerY - 30

                    allCircles.find { e -> e.contains(pick[0].centerX,pick[0].centerY)  }?.apply {
                        centerX = yo
                        centerY = oy
                        relocate(centerX - 30,centerY - 30 )
                        centerX = yo + 30
                        centerY = oy + 30
                    }
                }
            }

            pick.forEach { it.removeFromParent() }
            pathList.forEach { it.removeFromParent() }
            pathList.clear()
            pick.clear()
            if (this != null) {

                var count = 0
                var signX = 0
                var signY = 0

                while (count != 4) {
                    when (count){
                        0 -> {
                            signX = 100
                            signY = 100
                        }
                        1 -> {
                            signX = -100
                            signY = 100
                        }
                        2 -> {
                            signX = 100
                            signY = -100
                        }
                        3 -> {
                            signX = -100
                            signY = -100
                        }
                    }
                    val wLocation = whiteCircles
                        .find { e -> e.contains(this.centerX, this.centerY)  }
                    val bLocation = blackCircles
                        .find { k -> k.contains(this.centerX, this.centerY)  }

                    val bDiagonal = blackCircles
                        .find { k -> k.contains(this.centerX + signX, this.centerY - signY)  }
                    val wDiagonal = whiteCircles
                        .find { e -> e.contains(this.centerX + signX, this.centerY + signY)  }

                    val wDiagonal2 = whiteCircles
                        .find { e -> e.contains(this.centerX + signX*2, this.centerY + signY*2)  }
                    val bDiagonal2 = blackCircles
                        .find { k -> k.contains(this.centerX + signX*2, this.centerY - signY*2)  }
                    when (this) {
                        in blackCircles -> {
                            if (count in 0..1) {
                                if (bLocation != null && wDiagonal != null) {
                                    if ((bLocation.centerX - wDiagonal.centerX == 100.0 && wDiagonal.centerY - bLocation.centerY == 100.0) ||
                                        (wDiagonal.centerY - bLocation.centerY == 100.0 && wDiagonal.centerY - bLocation.centerY == 100.0) &&
                                        wDiagonal2 == null) {
                                        signX *= 2
                                        signY *= 2
                                    }

                                }
                                pathList += path(this.centerX + signX, this.centerY + signY, Color.DARKBLUE)
                                pick += path(this.centerX, this.centerY, Color.GREEN)
                            }
                        }
                        in whiteCircles -> {
                            if (count in 0..1) {
                                if (wLocation != null && bDiagonal != null) {
                                    if ((bDiagonal.centerX- wLocation.centerX  == 100.0 &&
                                        wLocation.centerY - bDiagonal.centerY == 100.0) ||
                                        (wLocation.centerX - bDiagonal.centerX == 100.0 &&
                                                wLocation.centerY - bDiagonal.centerY == 100.0 )) {
                                        if (bDiagonal2 == null) {
                                            signX *= 2
                                            signY *= 2
                                        } else {
                                            signX *= 0
                                            signY *= 0
                                        }
                                    }
                                }
                                pathList += path(this.centerX + signX, this.centerY - signY, Color.DARKBLUE)
                                pick += path(this.centerX, this.centerY, Color.GREEN)
                            }
                        }
                    }
                    count +=1
                }
            }
            whiteCircles.forEach { it.toFront() }
            blackCircles.forEach { it.toFront() }
            pick.forEach { it.toFront() }
        }
    }

        val topBoard = {
            var tile = 0
            var xCor = 0
            var yCor = 0
            while (yCor != 800) {

                while (xCor != 800) {

                    if (tile % 2 == 0) {
                        rectangle(xCor, 700 - yCor, 100.0, 100.0) {
                            fill = Paint.valueOf("#a94442")
                        }.toBack()
                        when (yCor != 300 || yCor != 200){
                            (yCor < 300)-> whitePiece(50.0 + xCor, 750.0 - yCor, Color.WHITE)
                            (yCor > 400)-> blackPiece(50.0 + xCor, 750.0 - yCor)
                        }

                    }
                    tile += 1
                    xCor += 100
                }
                xCor = 0
                tile += 1
                yCor += 100
            }
            rectangle(0,0,800.0,800.0).toBack()
        }

    topBoard.invoke()

    addEventFilter(MouseEvent.MOUSE_PRESSED, ::choose)
    }

}

//class MainView : View("Moving App") {
//
//    val rectangles = mutableListOf<Rectangle>()
//    var selectedRectangle: Rectangle? = null
//    var selectedOffset: Point2D? = null
//    val positionMessage = SimpleStringProperty("")
//
//    enum class XFormType { NONE, SCALE, ROTATE }
//
//    private fun startDrag(evt : MouseEvent) {
//
//        rectangles.firstOrNull {
//            val mousePt = it.sceneToLocal(evt.x, evt.y)
//            it.contains(mousePt)
//        }.apply {
//            if( this != null ) {
//
//                selectedRectangle = this
//
//                val mousInRectsPos = this.parent.sceneToLocal( evt.sceneX, evt.sceneY )
//                val vizBounds = this.boundsInParent
//
//                selectedOffset = Point2D(
//                    mousInRectsPos.x - vizBounds.minX - (vizBounds.width - this.boundsInLocal.width)/2,
//                    mousInRectsPos.y - vizBounds.minY - (vizBounds.height - this.boundsInLocal.height)/2
//                )
//
//            }
//        }
//    }
//    private fun drag(evt : MouseEvent) {
//
//        val mousePt : Point2D = (evt.source as Pane).sceneToLocal( evt.sceneX, evt.sceneY )
//        if( selectedRectangle != null && selectedOffset!= null  ) {
//
//            selectedRectangle!!.relocate(
//                mousePt.x - selectedOffset!!.x,
//                mousePt.y - selectedOffset!!.y)
//
//            positionMessage.value =
//                "Last Selection: Mouse (${mousePt.x}, ${mousePt.y}) " +
//                        "Moving To (${mousePt.x - selectedOffset!!.x}, ${mousePt.y - selectedOffset!!.y})"
//
//        }
//    }
//
//    private fun endDrag(evt : MouseEvent) {
//        selectedRectangle = null
//        selectedOffset = null
//    }
//
//    override val root = vbox {
//
//        anchorpane {
//            pane {
//
//                fun createRectangle(startX: Double, f: Color, xform: XFormType = XFormType.NONE): Rectangle {
//                    return rectangle(startX, 100.0, 50.0, 50.0) {
//                        fill = f
//                        stroke = Color.BLACK
//                        rectangles.add(this)  // for convenience
//                        layoutX = 25.0
//                        layoutY = 25.0
//                        when (xform) {
//                            XFormType.SCALE -> {
//                                scaleX = 2.0
//                                scaleY = 2.0
//                            }
//                            XFormType.ROTATE -> {
//                                rotate = 45.0
//                            }
//                        }
//                    }
//                }
//
//                createRectangle(100.0, Color.BLUE)
//                createRectangle(300.0, Color.YELLOW, XFormType.SCALE)
//                createRectangle(500.0, Color.GREEN, XFormType.ROTATE)
//
//                anchorpaneConstraints {
//                    topAnchor = 0.0
//                    bottomAnchor = 0.0
//                    rightAnchor = 0.0
//                    leftAnchor = 0.0
//                }
//
//                addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
//                addEventFilter(MouseEvent.MOUSE_DRAGGED, ::drag)
//                addEventFilter(MouseEvent.MOUSE_RELEASED, ::endDrag)
//            }
//
//            vboxConstraints {
//                vgrow = Priority.ALWAYS
//            }
//        }
//
//        label(positionMessage) {
//            padding = Insets(2.0)
//        }
//
//        padding = Insets(2.0)
//    }
//}


//override val root = pane {
//        var xCor = 0
//        var yCor = 0
//        var boardColor = 0
//        var circles = mutableListOf<Circle>()
//
//        while (yCor != 800) {
//
//            while (xCor != 800) {
//                if (boardColor % 2 == 0){
//                    rectangle(xCor, yCor, 100, 100){
//                        fill = Paint.valueOf("#a94442")
//                    }
//                    if(yCor != 300 && yCor != 400) {
//                       var piece = circle(xCor + 50, yCor + 50, 30) {
//                            if (yCor >= 300 ) fill = Paint.valueOf("#d49942")
//                        }
//
//                    }
//                    boardColor += 2
//                } else rectangle(xCor, yCor, 100, 100)
//                boardColor += 1
//                xCor += 100
//            }
//            boardColor += 1
//        xCor = 0
//        yCor +=100
//        }
//
//    }

//override val root = pane {
//        var xCor = 0
//        var yCor = 0
//        var boardColor = 0
//
//       val bottomBoard = rectangle(0,0,800.0,800.0)
//
//        val topBoard = {
//            while (yCor != 800){
//                while (xCor !=800)
//            rectangle(xCor, yCor, 100.0, 100.0) {
//                fill = Paint.valueOf("#a94442")
//            }
//        }
//        }
//
//
//    }

//        var labelText = SimpleStringProperty()
//        label(labelText) {
//            bind(labelText)
//            addClass(Styles.heading)
//        }
//        button {
//            this.text = "click"
//            action {
//                labelText.set("yo")
//
//            }
//        }

//[19:34] Егорова Инга Сергеевна
//Добавить main, kotlinx Parser для обработки аргументов пользователяСобрать jar-архив средствами Идеи (и уметь его запускать)Нужно ли преобразование к Int при записи в файлДобавить тест на последовательную шифрацию 2 раза подряд, убедиться, что конечный файл равен исходномуДобавить тесты на некорректный ключ (assertThrows)
//
//[19:40] Егорова Инга Сергеевна
//https://www.youtube.com/watch?v=QsQQ5D4TARw
//
//[19:40] Егорова Инга Сергеевна
//onClickListener
//
//[19:42] Егорова Инга Сергеевна
//https://stackoverflow.com/questions/40757911/javafx-adding-actionlistener-to-button
//Javafx adding ActionListener to button
//button.setOnAction(new EventHandler() {
//    @Override public void handle(ActionEvent e) {
//        label.setText("Accepted");
//    }
//});
//In the code above we are defining what will

// chat
//Message List
//Meeting started
//18:49 Meeting started
//Last read
//Recording has started
//18:49 Recording has started
//Profile picture of Егорова Инга Сергеевна.Добавить main, kotlinx Parser для обработки... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:34
//Добавить main, kotlinx Parser для обработки аргументов пользователя
//
//Собрать jar-архив средствами Идеи (и уметь его запускать)
//
//Нужно ли преобразование к Int при записи в файл
//
//Добавить тест на последовательную шифрацию 2 раза подряд, убедиться, что конечный файл равен исходному
//
//Добавить тесты на некорректный ключ (assertThrows)
//
//Profile picture of Егорова Инга Сергеевна.Link https://www.youtube.com/watch?v=QsQQ5D... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:40
//https://www.youtube.com/watch?v=QsQQ5D4TARw
//
//onClickListener by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:40
//onClickListener
//
//Link https://stackoverflow.com/questions/40... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:42
//https://stackoverflow.com/questions/40757911/javafx-adding-actionlistener-to-button
//
//Url Preview for Javafx adding ActionListener to button
//Javafx adding ActionListener to button
//button.setOnAction(new EventHandler() { @Override public void handle(ActionEvent e) { label.setText("Accepted"); } }); In the code above we are defining what will
//
//stackoverflow.com
//Profile picture of Егорова Инга Сергеевна.Model: Board, Cell, Checker Map-> Button, C... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:49
//Model: Board, Cell, Checker
//
//Map-> Button, Checker -> щелкнули по Button, достали checker, можем выполнять операцию
//
//resizable = false by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:50
//resizable = false
//
//Model View Controller by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:52
//Model View Controller
//
//Отрисованы клавиши, по щелчку звук, кнопка ... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:55
//Отрисованы клавиши, по щелчку звук, кнопка начать запись - сохраняем в список - остановить - FileChooser - сохранить, файл сохраняется, нет - не сохраняется