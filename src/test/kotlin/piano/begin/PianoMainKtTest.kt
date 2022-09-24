package piano.begin

import javafx.scene.shape.Rectangle
import junit.framework.TestCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import piano.controller.*
import piano.model.loadInstrument

class PianoTest: TestCase(){
    init {
        keySetup(0)
        for ((i,e) in pianoKeys.withIndex()){
            notePair += Pair(100 + i*4L , e)
        }
        libraryList += listOf(notePair)
        loadInstrument(60,60)
//        libraryPlay(libraryList.size - 1)
    }
    // control many sounds, check keys if it is the right press
    fun test(){
        assertEquals(true, libraryList[0].size == notePair.size)
        var x = 0
        while (x != libraryList[0].size - 1){
            assertEquals(true, longCheck[x] - timeCheck[x] in 0..3 )
            x+=1
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