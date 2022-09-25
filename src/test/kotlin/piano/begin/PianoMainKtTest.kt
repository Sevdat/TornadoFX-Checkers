package piano.begin

import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import junit.framework.TestCase
import piano.controller.*
import piano.view.rectangleSetUp
import java.io.File

class PianoTest: TestCase(){
    val keyAmount = 3
    init {
        fileName = "src/test/kotlin/piano/begin/PianoTest.txt"
        File(fileName).createNewFile()
        File(fileName).writeText(
            "ef [(570, 0), (604, 1), (607, 2)]\n" +
                "fe [(1816, 0), (584, 4), (539, 2)]\n" +
                "asdd [(671, 0), (534, 2), (534, 1)]\n" +
                "fasdd [(670, 0), (1070, 2), (570, 4), (535, 5), (569, 3), (605, 1), (709, 5), (685, 6)]\n" +
                "asdd ddd [(887, 0), (550, 2), (542, 4), (528, 3)]\n"
        )
        keySetup(keyAmount)
        rectangleSetUp()
        getLibrary(fileName)
        libraryPlay(libraryMap.keys.first())
        notePair = listOf(
            Pair(680L, Rectangle(98.0, 0.0, 50.0, 150.0).apply {
                id = "0"
                fill = Paint.valueOf("0x0000ffff")
            })
        )
    }
    fun test(){
        assertEquals(keyAmount*12, pianoKeys.size)
        assertEquals(
            "[(570, 0), (604, 1), (607, 2)]",
            libraryMap["ef"].toString()
        )
        assertEquals(
            "[(670, 0), (1070, 2), (570, 4), (535, 5), (569, 3), (605, 1), (709, 5), (685, 6)]",
            libraryMap["fasdd"].toString()
        )
        run{
            save("kek")
            assertEquals(
                "ef [(570, 0), (604, 1), (607, 2)]\n" +
                        "fe [(1816, 0), (584, 4), (539, 2)]\n" +
                        "asdd [(671, 0), (534, 2), (534, 1)]\n" +
                        "fasdd [(670, 0), (1070, 2), (570, 4), (535, 5), (569, 3), (605, 1), (709, 5), (685, 6)]\n" +
                        "asdd ddd [(887, 0), (550, 2), (542, 4), (528, 3)]\n" +
                        "kek [(680, 0)]\n",
                File(fileName).readText()
            )
        }
    }
}

//FileChooser + сохранение в ФС
//PianoApp: App - что это такое?
//coordinate -> View
//firstNull -> first
//
//
//
//Поиск индекса кнопки в списке (вместо траверса экрана)
//    val index = values.withIndex()
//        .first { item == it.value }
//        .index
//
//open -> close()
//
//
//
//loadInstrument -> model(!)
//startRecord = boolean
//notePair -> list<Pair>
//
//
//
//Модель (midiChannel + метод проигрывания звука - класс со статическими переменными и методами)
//notePair (превращаем в мапу) -> класс MusicBuffer -> push (добавить пару) и getAll (выдать список всех пар) +
//создаете объект в контроллере и стучитесь через публичные методы
//
//
//
//do-re-mi (file vs notePair) - сравниваем реальное после вызова методов с ожидаемым