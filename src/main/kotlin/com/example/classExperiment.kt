package com.example

import tornadofx.cssclass

//inheretence
//


abstract class MusicalInstrument(val Owner: String){
    abstract fun getName()
    abstract fun getInstrument()
    fun print(){println("hello")}
}
class Guitar(Owner: String): MusicalInstrument(Owner){
    override fun getName() { run { println(Owner) } }
    override fun getInstrument() { run { println(this::class.simpleName) } }
}
class Piano(Owner:String): MusicalInstrument(Owner){
    override fun getName() { run { println(Owner) } }
    override fun getInstrument() { run { println(this::class.simpleName) } }
}
fun main(){

    Guitar("try").getName()
    Guitar("try").getInstrument()
    Piano("try2").getName()
    Piano("try2").getInstrument()
    Piano("try2").print()
}

//open class MusicalInstrument(val Owner: String){
//    fun getName(){ println(Owner) }
//    fun getInstrument(){ println(this::class.simpleName)}
//}
//class Guitar(Owner: String): MusicalInstrument(Owner)
//class Piano(Owner:String): MusicalInstrument(Owner)
//fun main(){
//    Guitar("try").getName()
//    Guitar("try").getInstrument()
//    Piano("try2").getName()
//    Piano("try2").getInstrument()
//}