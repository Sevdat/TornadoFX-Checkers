package piano.controller

import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import piano.model.Keys
import piano.model.loadInstrument
import java.io.File
import java.io.FileWriter

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
var notePair = listOf<Pair<Long,String>>()
var pianoKeys = listOf<Rectangle>()
var coordinateKeys = listOf<Keys>()

data class timeAndID(
        var time:Long,
        var ID: String
)

data class nameAndList(
        var Name:String,
        var Record: List<timeAndID>

)
var newLibList = listOf<nameAndList>()

var amountOfKeySets = 3
const val intensity = 60
const val volume = 60
var startRecord = false
var fileName = "PianoSongLibrary.txt"
var now:Long = 0
// music buffer notepair. two public method push and getall

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
        var newLong: Long
        var savingToData = listOf<timeAndID>()
        for ((i,e) in notePair.withIndex()){
                newLong = if (startRecord == true){
                        if (i == 0) e.first else e.first - notePair[i -1].first
                } else e.first
                savingToData += timeAndID(newLong, e.second)
        }
        newLibList += nameAndList(name, savingToData)
        notePair = listOf()
        if (startRecord == true){
                saveToFile(name)
        }
}

 fun saveToFile(name:String){
         val timeIDData = newLibList.find { (i,e) -> i == name }!!.Record
         var organizeData = ""
         for (i in timeIDData){
                 organizeData += "${i.time},${i.ID},"
         }
         val string = "$name:${organizeData.dropLast(1)}\n"
         FileWriter(fileName,true).use { out -> out.write(string) }
 }
fun getLibrary(fileName:String){
        if (File(fileName).exists() && File(fileName).length() != 0L){
                val fil = File(fileName).readLines()
                for (i in fil) {
                        val k = i.split(":")
                        val name = k[0]
                        val pair = k[1].split(",")
                        var pairCount = 0
                        while (pairCount < pair.size - 1){
                                notePair += Pair(pair[pairCount].toLong(), pianoKeys[pair[pairCount + 1].toInt()].id)
                                pairCount += 2
                        }
                        save(name)
                }
        }
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
                                notePair += Pair(nowHappening, this.id)
                        }
                }
        }
}

fun libraryPlay(name:String){
        for ((i,e) in newLibList.find { i -> i.Name == name }!!.Record){
                var nowNow: Long = 0
                now = System.currentTimeMillis()
                while (nowNow <= i) nowNow = System.currentTimeMillis() - now
                chooseKey(pianoKeys[e.toInt()])
        }
}

//use codes that got sent to you for indexInParent
//val indexOfPianoKey = pianoKeys.find { it == rec }?.indexInParent