package piano.controller

import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import piano.model.Keys
import piano.model.loadInstrument
import java.io.File
import javax.sound.midi.MidiChannel
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer

// map instead of pair
// music veriables models
// models static
// more sounds
// class music buffer
//getall
// val index = values.withIndex()
//
//        .first { item == it.value }
//
//        .index
var pianoKeys = listOf<Rectangle>()
var coordinateKeys = listOf<Keys>()
var namedSong = listOf<String>()
var libraryList = listOf<List<Pair<Long,Rectangle>>>()
var libraryMap = mapOf<String,List<Pair<Long,String>>>()

var amountOfKeySets = 3
const val intensity = 60
const val volume = 60
var startRecord = false
var notePair = listOf<Pair<Long,Rectangle>>()
// music buffer notepair. two public method push and getall
var now:Long = 0

fun keySetup(amount: Int){
        if (amount != 0) {
                var x = 98.0
                val y = 0.0
                val keyWidth = 50.0
                for (i in 0..12) {
                        var keyHeight = 150.0
                        var paint: Color = Color.BLUE
                        if (i % 2 != 0) {
                                keyHeight = 75.0
                                paint = Color.BLACK
                        }
                        val keyNumber = if (i in 5..12) i - 1 else i
                        if (i != 5) coordinateKeys +=
                                Keys("$keyNumber", x + i * (keyWidth / 2), y, keyWidth, keyHeight, paint)
                        x += 2.0
                }
                var cycle = 1
                var empty = listOf<Keys>()
                while (cycle != amount) {
                        for ((i,e) in coordinateKeys.withIndex()) {
                               empty += e.copy().apply {
                                       this.x += 378*cycle
                                       this.keyName = "${i + 12*cycle}"
                               }
                        }
                        cycle += 1
                }
                coordinateKeys += empty
        }
}

fun chooseKey(rec: Rectangle?){
        val indexOfPianoKey = pianoKeys.withIndex().first { (_,e) -> e == rec }.index
        loadInstrument(indexOfPianoKey + intensity, volume)
}
fun save(name:String) {
        namedSong +=  name
        libraryList += listOf(notePair)

        var num = listOf<Pair<Long,String>>()
        var newLong: Long = 0
        val last = libraryList.last()
        for ((i,e) in last.withIndex()){
                newLong = if (i == 0) e.first else e.first - last[i -1].first

                num += Pair(newLong, e.second.id.toString())
        }

        libraryMap += mapOf(namedSong.last() to num)
        notePair = listOf()
        val fileName = "src/main/kotlin/piano/file/${name}.txt"
        File(fileName).createNewFile()
        File(fileName).writeText(libraryMap.toString())

}

fun playKey(evt: MouseEvent) {
        val containList = pianoKeys.filter {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
        }
        if (containList.isNotEmpty()) {
                val option = containList.find { it.fill == Color.BLACK } ?: containList.first()
                option.apply {
                        chooseKey(this)
                        if (startRecord == true) {
                                val nowHappening = System.currentTimeMillis() - now
                                notePair += Pair(nowHappening, this)
                        }
                }
        }
}

var longCheck = listOf<Long>()
var timeCheck = listOf<Long>()
fun libraryPlay(f:String){
        for ((i,e) in libraryMap[f]!!){
                var nowNow: Long = 0
                now = System.currentTimeMillis()
                while (nowNow <= i) nowNow = System.currentTimeMillis() - now
                val rectt = pianoKeys[e.toInt()]
                chooseKey(rectt)
                longCheck += nowNow
        }
}

//use codes that got sent to you for indexInParent
//val indexOfPianoKey = pianoKeys.find { it == rec }?.indexInParent