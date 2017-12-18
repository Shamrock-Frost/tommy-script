package interpreter

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import core.parser.TommyParser
import standard_library.stdLib

const val prelude: String = "ts> "
const val quit: String = "--quit"

fun main(args : Array<String>) {
    val builtins = mutableMapOf<String, Value>("print" to VFunction(Print),
            "len" to VFunction(Len),
            "str" to VFunction(Str),
            "push" to VFunction(Push))
    val environment = Scope(builtins)
    stdLib.forEach { exec(it, environment) }
    while(true) {
        print(prelude)
        val line = readLine()
        if(line == null || line == quit) break
        val prog = TommyParser().parseToEnd(line)[0]
        interp(prog, environment)
        println()
    }
}
