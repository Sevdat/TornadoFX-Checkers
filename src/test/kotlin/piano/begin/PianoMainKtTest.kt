package piano.begin

import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import junit.framework.TestCase
import piano.controller.*
import piano.model.instrumentLoader
import piano.view.rectangleSetUp
import java.io.File

class PianoTest: TestCase(){
    val keyAmount = 3
    init {
        instrumentLoader.open()
        fileName = "src/test/kotlin/piano/begin/PianoTest.txt"
        File(fileName).createNewFile()
        File(fileName).writeText(
            "huu:626,0,596,2,557,1\n" +
                    "ytrr:590,0,593,1,557,2,481,4\n" +
                    "huuu:609,0,713,2,927,1\n"
        )
        keySetup(keyAmount)
        rectangleSetUp()

        getLibrary(fileName)
        libraryPlay(newLibList.first().Name)
        notePair = listOf(
            Pair(680L, "0")
        )
    }
    fun test(){
        assertEquals(keyAmount*12, pianoKeys.size)
        assertEquals(
            "nameAndList(" +
                    "Name=huu, " +
                    "Record=[" +
                    "timeAndID(time=626, ID=0), " +
                    "timeAndID(time=596, ID=2), " +
                    "timeAndID(time=557, ID=1)" +
                    "])",
            newLibList.find { i -> i.Name == "huu" }.toString()
        )
        assertEquals(
            "nameAndList(" +
                    "Name=huu, " +
                    "Record=[" +
                    "timeAndID(time=626, ID=0), " +
                    "timeAndID(time=596, ID=2), " +
                    "timeAndID(time=557, ID=1)" +
                    "])",
            newLibList.find { i -> i.Name == "huu" }.toString()
        )
        run{
            startRecord = true
            save("kek")
            assertEquals(
                "huu:626,0,596,2,557,1\n" +
                        "ytrr:590,0,593,1,557,2,481,4\n" +
                        "huuu:609,0,713,2,927,1\n" +
                        "kek:680,0\n",
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