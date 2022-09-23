package piano.controller

import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import piano.model.Keys
import piano.model.loadInstrument
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
lateinit var instrumentPlayer: MidiChannel
val instrumentLoader: Synthesizer = MidiSystem.getSynthesizer()
const val intensity = 60
const val volume = 60
var startRecord = false
var notePair = listOf<Pair<Long,Rectangle>>()
// music buffer notepair. two public method push and getall
var now:Long = 0
var libraryList = listOf<List<Pair<Long,Rectangle>>>()

fun keySetup(amount: Int){
        var x = 98.0
        val y = 0.0
        val keyWidth = 50.0
        for (i in 0..12){
                var keyHeight = 150.0
                var paint: Color = Color.BLUE
                if (i % 2 != 0){
                        keyHeight = 75.0
                        paint = Color.BLACK
                }
                coordinateKeys +=
                        Keys("${amount * i}",x + i*(keyWidth/2),y,keyWidth,keyHeight, paint)
                x += 2.0
        }

}

fun chooseKey(rec: Rectangle?){
        val indexOfPianoKey = pianoKeys.withIndex().first { (_,e) -> e == rec }.index
        loadInstrument(indexOfPianoKey + intensity, volume)
}

fun playKey(evt: MouseEvent) {
        println(pianoKeys.filter {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
        }
        )
        pianoKeys.firstOrNull {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
        }.apply {
                if (this != null) {
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
fun libraryPlay(f:Int){
        for ((i,e) in libraryList[f].withIndex()){
                var nowNow: Long = 0
                now = System.currentTimeMillis()
                val timeGap: Long = if (i == 0) e.first else e.first - libraryList[f][i-1].first
                while (nowNow <= timeGap) {
                        nowNow = System.currentTimeMillis() - now
                }
                chooseKey(e.second)
                longCheck += nowNow
                timeCheck += timeGap
        }
}

//use codes that got sent to you for indexInParent
//val indexOfPianoKey = pianoKeys.find { it == rec }?.indexInParent