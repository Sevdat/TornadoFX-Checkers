package com.example.view

import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.*
import kotlin.math.abs


class MainView : View("Russian Checkers") {
    // what is View
    override val root: Pane = pane {
        val fifty = 50.0
        val eightFifty = 850.0
        val rectangles = group()
        val allCircles = group()
        val location = group()
        val loc = location.children
        val all = allCircles.children
        val rec = rectangles.children
        val coordinates = listOf(Pair(100, 100), Pair(-100, 100), Pair(100, -100), Pair(-100, -100))
        var choose = 0

        fun choose(evt: MouseEvent) {
            val checker = mutableListOf(0, 0, 0, 0)
            rec.firstOrNull {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
            }.apply {

                loc.firstOrNull {
                    val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                    it.contains(mousePt)

                }.apply {
                    this as Circle?
                    if (this != null) {
                        val pick = loc[0] as Circle
                        val pickedPiece = all.find { it.contains(pick.centerX, pick.centerY) } as Circle
                        val newLocX = this.centerX
                        val newLocY = this.centerY
                        val substractX = pickedPiece.centerX - this.centerX
                        val substractY = pickedPiece.centerY - this.centerY
                        var g = 0
                        val dig = abs(substractX).toInt() / 100
                        while (g != dig) {
                            g += 1
                            val diagonalX = newLocX + ((substractX / dig) * g)
                            val diagonalY = newLocY + ((substractY / dig) * g)

                            val finder = all.find { it.contains(diagonalX, diagonalY) } as Circle?
                            if (finder != null) {
                                allCircles += finder.apply {
                                    centerX = 850.0
                                    centerY = 150.0
                                }
                            }
                        }
                        allCircles += pickedPiece.apply {
                            centerX = newLocX
                            centerY = newLocY
                            when (choose) {
                                0 -> choose = 1
                                1 -> choose = 0
                            }
                        }
                    }
                    for (i in all.filter { (it as Circle).centerY == 50.0 || it.centerY == 750.0 }) {
                        i as Circle
                        val beDama: Boolean =
                            (i.fill == Color.WHITE && i.centerY == 50.0) ||
                                    (i.fill == Color.BLACK && i.centerY == 750.0)
                        when {
                            beDama -> {
                                allCircles += i.apply {
                                    fill = if (i.centerY == 50.0) Color.AQUA else Color.DARKMAGENTA
                                    strokeWidth = 8.0
                                }
                            }
                        }
                    }
                }

                var relocate = 0
                this as Rectangle?
                while (relocate != loc.size - 1) {
                    location += loc[relocate].apply {
                        this as Circle
                        centerX = eightFifty
                        centerY = fifty
                    }
                    relocate += 1
                }

                val turn = if (choose == 0)
                    all.filter { (it as Circle).fill == Color.WHITE || it.fill == Color.AQUA }
                else
                    all.filter { (it as Circle).fill == Color.BLACK || it.fill == Color.DARKMAGENTA }

                if (this != null) {
                    val pick = loc[0]
                    val current = all.find { it.contains(this.x + fifty, this.y + fifty) } as Circle?
                    var expand = 1
                    var corCount = 0
                    if (current in turn) for (i in loc) {
                        if (current != null) {
                            val gateKeeper: Boolean =
                                (corCount == coordinates.size && current.fill != Color.BLACK &&
                                        current.fill != Color.WHITE)
                            if (gateKeeper) {
                                expand += 1
                                corCount = 0
                            }

                            if (corCount != coordinates.size) {

                                val signX = coordinates[corCount].first * expand
                                val signY = coordinates[corCount].second * expand

                                location += i.apply {
                                    this as Circle
                                    if (i != pick) {
                                        val newX = current.centerX + signX
                                        val newY = current.centerY + signY
                                        val bluePath = all.find { it.contains(newX, newY) } as Circle?
                                        val blackOrWhite: Boolean =
                                            (current.fill == Color.WHITE || current.fill == Color.BLACK)

                                        val che = checker[corCount]
                                        if (che != 2) {

                                            if (bluePath != null) {
                                                val possb = (
                                                        (current.fill == Color.BLACK && bluePath.fill != Color.DARKMAGENTA) ||
                                                                (current.fill == Color.WHITE && bluePath.fill != Color.AQUA) ||
                                                                (current.fill == Color.AQUA && bluePath.fill == Color.DARKMAGENTA) ||
                                                                (current.fill == Color.DARKMAGENTA && bluePath.fill == Color.AQUA) ||
                                                                (current.fill == Color.DARKMAGENTA && bluePath.fill == Color.WHITE) ||
                                                                (current.fill == Color.AQUA && bluePath.fill == Color.BLACK)
                                                        )
                                                if (possb)
                                                    checker[corCount] += 1 else checker[corCount] = 2
                                            } else checker[corCount] = 0

                                            if (bluePath == null) {

                                                if (blackOrWhite) {
                                                    when (current.fill) {
                                                        Color.WHITE, Color.BLACK -> {
                                                            val initial =
                                                                (current.fill == Color.WHITE && corCount in 2..3 ||
                                                                        current.fill == Color.BLACK && corCount in 0..1)
                                                            if (initial) {
                                                                centerX = newX
                                                                centerY = newY
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    centerX = newX
                                                    centerY = newY
                                                }

                                            } else {
                                                val jump: Boolean =
                                                    (blackOrWhite && current.fill != bluePath.fill)
                                                val jump1: Boolean =
                                                    (current.fill == Color.BLACK && bluePath.fill != Color.DARKMAGENTA) ||
                                                            (current.fill == Color.WHITE && bluePath.fill != Color.AQUA)
                                                if (jump) {
                                                    val bluePath2 = all.find {
                                                        it.contains(
                                                            newX + signX,
                                                            newY + signY
                                                        )
                                                    } as Circle?

                                                    if (bluePath2 == null && jump1) {
                                                        centerX = newX + signX
                                                        centerY = newY + signY
                                                    }
                                                }
                                            }
                                        }
                                        corCount += 1

                                    } else {
                                        centerX = current.centerX
                                        centerY = current.centerY
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        addEventFilter(MouseEvent.MOUSE_PRESSED, ::choose)

        fun pieces(startX: Double, startY: Double, f: Color): Circle {
            return circle(startX, startY) {
                fill = f
                when (f) {
                    Color.BLACK, Color.WHITE, Color.GREEN, Color.DARKBLUE -> {
                        run {
                            if (this.fill == Color.BLACK || this.fill == Color.WHITE) {
                                radius = 30.0
                                stroke = if (this.fill == Color.BLACK) Color.WHITE else Color.BLACK
                                strokeWidth = 3.0
                                allCircles.children.add(this)
                            } else {
                                radius = 15.0
                                location.children.add(this)
                            }
                        }
                    }
                }
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
                            rectangles.add(this)
                        }.toBack()
                        when (yCor != 300 || yCor != 200) {
                            (yCor < 300) -> pieces(50.0 + xCor, 750.0 - yCor, Color.WHITE)
                            (yCor > 400) -> pieces(50.0 + xCor, 750.0 - yCor, Color.BLACK)
                        }

                    }

                    tile += 1
                    xCor += 100
                }
                if (yCor == 0) pieces(850.0, 50.0, Color.GREEN)
                pieces(850.0, 50.0, Color.DARKBLUE)
                pieces(850.0, 50.0, Color.DARKBLUE)
                pieces(850.0, 50.0, Color.DARKBLUE)
                pieces(850.0, 50.0, Color.DARKBLUE)
                xCor = 0
                tile += 1
                yCor += 100
            }
            rectangle(0, 0, 800.0, 800.0).toBack()
        }
        topBoard.invoke()
    }

}
//                        var k = 0
//                        var finder2: Circle? = null
//                        while (k != 2) {
//                            k += 1
//                            for (i in coordinates) {
//                            val drx = pickedPiece.centerX + (i.first * k)
//                            val dry = pickedPiece.centerY + (i.second * k)
//
//                            finder2 = all.find { it.contains(drx, dry) } as Circle?
//
//
//                            when (choose) {
//                                0 -> if (g > 1) choose = 1
//                                1 -> if (g > 1) choose = 0
//                            }
//                        }
//                        }

//                                val newPiece = allCircles.children.find {
//                                    it.contains(
//                                        pickedPiece.centerX + (substractX / 2),
//                                        pickedPiece.centerY + (substractY / 2)
//                                    )
//                                } as Circle?
//                                // make numbers val and name better
//                                if (newPiece != null) {
//                                    allCircles += newPiece.apply {
//                                        centerX = 850.0
//                                        centerY = 150.0
//                                    }
//                                }

// println(allCircles.children.filter { e -> (e as Circle).fill == Color.AQUA || e.fill == Color.DARKMAGENTA }.size)

//                    while (count != 4) {
//                        var signX = list[count].first
//                        var signY = list[count].second
//                        if (count in 0..4) {
//
//                            if (current?.fill != Color.AQUA || current?.fill != Color.DARKMAGENTA) {
//                                if (current?.fill == Color.WHITE) signY = -signY
//                                val diagonal = all.find { it.contains(this.x + signX + 50, this.y + signY + 50) } as Circle?
//                                var diagonal2: Circle? = null
//                                if (diagonal != null && diagonal.fill != current?.fill) {
//                                    signX *= 2
//                                    signY *= 2
//                                    diagonal2 = all.find { it.contains(this.x + signX + 50, this.y + signY + 50) } as Circle?
//                                }
//                                if (diagonal?.fill != current?.fill && diagonal2 == null && current != null)
//                                    location += loc[count + 1].apply {
//                                        this as Circle
//                                        centerX = current.centerX + signX
//                                        centerY = current.centerY + signY
//                                    }
//                            }
//
//                            if (current != null && count != 0) {
//                                var count2 = 0
//                                if (current.fill == Color.AQUA || current.fill == Color.DARKMAGENTA)
//                                    while (count2 != 4) {
//                                        location += loc[count2 + count * 2].apply {
//                                            this as Circle
//                                            centerX = current.centerX + signX * (count2 + count * 2)
//                                            centerY = current.centerY + signY * (count2 + count * 2)
//                                        }
//                                        count2 += 1
//                                    }
//                            }
//
//                        }
//                        if (current != null && count == 0)
//                            location += loc[count].apply {
//                                this as Circle
//                                centerX = current.centerX
//                                centerY = current.centerY
//                            }
//                        count += 1
//                    }

//            pathList.firstOrNull{
//                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
//                it.contains(mousePt)
//
//            }.apply {
//
//                pathList.forEach { it.toFront() }
//                if (this != null) {
//                    val newLocX = this.centerX - pieceRadius
//                    val newLocY = this.centerY - pieceRadius
//
//                    allCircles.find { e -> e.contains(pick.last().centerX,pick.last().centerY)  }?.apply {
//                        centerX = newLocX
//                        centerY = newLocY
//                        relocate(centerX - pieceRadius,centerY - pieceRadius )
//                        centerX = newLocX + pieceRadius
//                        centerY = newLocY + pieceRadius
//                    }
//                }
//            }
//            pathList.forEach { it.relocate(935.0, 135.0) }
//            pick.forEach { it.relocate(935.0, 135.0) }
//            if (this != null) {
//                pathList.forEach { it.relocate(935.0, 135.0) }
//                pick.forEach { it.relocate(935.0, 135.0) }
//                var count = 0
//
//                while (count != 4) {
//                    val list = listOf(Pair(100,100),Pair(-100,100),Pair(100,-100),Pair(-100,-100))
//                    var signX = list[count].first
//                    var signY = list[count].second
//
//                    if (count in 0..1) {
//                        if (this.fill == Color.WHITE) signY = -signY
//                        val diagonal = allCircles
//                            .find { e -> e.contains(this.centerX + signX, this.centerY + signY) }
//                        var diagonal2 : Circle? = null
//                            if (diagonal != null && diagonal.fill != this.fill) {
//                                signX *= 2
//                                signY *= 2
//                                diagonal2 = allCircles
//                                    .find { e -> e.contains(this.centerX + signX, this.centerY + signY) }
//                            }
//                        if (diagonal?.fill != this.fill && diagonal2 == null) {
//                            pathList += pieces(this.centerX + signX, this.centerY + signY, Color.DARKBLUE)
//                        }
//                        pick += pieces(this.centerX, this.centerY, Color.GREEN)
//                    }
//                    count +=1
//                }
//            }
//            pick.forEach { it.toFront() }
//        }
//        allCircles.filter { e -> e.fill == Color.WHITE }.find { k -> k.centerY == 50.0 }?.apply {
//            this.stroke = Color.DARKBLUE
//            this.strokeWidth = 15.0
//        }
//        allCircles.filter { e -> e.fill == Color.BLACK }.find { k -> k.centerY == 750.0 }?.apply {
//            this.stroke = Color.DARKGREEN
//            this.strokeWidth = 15.0
//            dama.add(this)
//        }
//    }

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