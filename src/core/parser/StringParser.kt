package core.parser

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.tryParseToEnd

val escapeChars = mapOf('t' to '\t', 'n' to '\n', '\"' to '\"', '\\' to '\\')
private val escapeSet = escapeChars.keys.joinToString("") { if(it == '\\') "\\\\" else "$it" }

//Extends Grammar<String> to create the language's grammar
internal class StringParser : Grammar<String>() {
    private val ESCAPESEQUENCE by token("\\\\[$escapeSet]")
    private val QUOTE by token("\"")
    private val CHAR by token("[^\\\\]")

    //Converts the input to a list of the successful matches (ie: turns it into tokens)
    private val escapeParser = ESCAPESEQUENCE.map { match ->
        escapeChars[match.text[1]]!! //Safe because match.text passes the ESCAPESEQUENCE pattern
    }

    //Actually parses the tokens into the language
    override val rootParser =
            -QUOTE and zeroOrMore(escapeParser or CHAR.use { text.first() }) and -QUOTE map {
                chars -> String(chars.toCharArray())
            }
}

fun main(args: Array<String>) {
    println(StringParser().tryParseToEnd("\"\\t\""))
}