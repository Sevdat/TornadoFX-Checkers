package piano.model

import javafx.scene.paint.Paint
import javax.sound.midi.MidiChannel
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer


data class Keys(
    var keyName: String,
    var x: Double,
    var y: Double,
    var keyWidth: Double,
    var keyHeight: Double,
    var color: Paint
    )

lateinit var instrumentPlayer: MidiChannel
val instrumentLoader: Synthesizer = MidiSystem.getSynthesizer()
fun loadInstrument(soundIntensity: Int, velocity: Int){
    instrumentLoader.loadInstrument(instrumentLoader.defaultSoundbank.instruments[0])
    instrumentPlayer = instrumentLoader.channels[0]
    instrumentPlayer.noteOn(soundIntensity,velocity)
}



//    var count = 0
//    val y = 0.0
//    var x = 100.0
//    val keyWidth = 50.0
//    val keyHeight = 150.0
//    val spaceInBetween = 3
//    while (count != 3) {
//        if (count != 0) x += keyWidth + spaceInBetween
//        pianoKeys += Rectangle(x, y, keyWidth, keyHeight)
//        count += 1
//    }