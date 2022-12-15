import org.w3c.dom.*
import kotlinx.browser.*

// External functions can be used to call JS functions defined in global scope.
// This function is defined inside a <script> tag in src/wasmMain/kotlin/index.html
external fun consoleLogExample(): Unit

// Use @JsFun to write JS code inside a Kotlin file.
// External functions can pass and receive values from JS.
@JsFun("function addDog(s) { return s + '🐶'; }")
external fun addDog(x: String): String

// From now on, we'll use the arrow syntax for brevity.
// You can pass values, like strings, to and from JS
@JsFun("s => s + '🐱'")
external fun addCat(x: String): String

// Numbers are supported too
@JsFun("(b, s, i, f) => b + s + i + f")
external fun addDifferntNumberTypes(b: Byte, s: Short, i: Int, f: Float): Double

// All number types are converted to JS Number, except Long, which doesn't fit.
// Long is converted to JS BigInt.
@JsFun("x => typeof x")
external fun jsTypeOf(x: Long): String

// Booleans are supported too.
@JsFun("(a, b) => !(a && b)")
external fun nand(a: Boolean, b: Boolean): Boolean

// Types can be nullable.
// In this function we'll use multiline """ string literal to format our JS code in multiple lines!
@JsFun("""(s) => {
     if (s == null) {
        return null;
     } else { 
        return s + '🐱';
     }
}""")
external fun addCatIfNotNull(x: String?): String?

// You can pass functions
@JsFun("(element, f) => { console.log(element); element.onclick = f; }")
external fun setOnClick(element: Element, f: () -> Unit)

// Higher order functions support the same parameter and return types
// as a regular external functions ...
@JsFun("(add, negate) => (x, y) => add(x, negate(y))")
external fun makeSubtract(
    add: (Int, Int) -> Int,
    negate: (Int) -> Int
): (Int, Int) -> Int

// In addition to functions we can have external top-level properties
external var externalInt: Int

// External objects
external object Counter {
    fun increment(): Unit
    val value: Int
    var step: Int
}

// External class
external class Rectangle(height: Double, width: Double) {
    val height: Double
    val width: Double
    val area: Double
    fun calcArea(): Double
}

fun runJsInteropTutorial() {
    val p = document.createElement("p")
        .apply { innerHTML = "<h1>JavaScript Interop logs</h1>" }
        .also { document.body!!.appendChild(it) }

    fun log(x: Any?) {
        document.createElement("p")
            .apply { textContent = x.toString() }
            .also { p.appendChild(it) }
    }

    consoleLogExample()

    log(addCat(addDog("Passing strings to and from JavaScript")))

    log("1 + 1 + 1 + 1 = " + addDifferntNumberTypes(1.toByte(), 1.toShort(), 1, 1.0f))

    log("Kotlin Long <-> to JS ${jsTypeOf(10L)}")

    for (a in listOf(false, true)) {
        for (b in listOf(false, true)) {
            log("nand($a, $b) = ${nand(a, b)}")
        }
    }

    log(addCatIfNotNull("Not null"))
    log(addCatIfNotNull(null))

    val buttonElement = document.createElement("input").apply {
        setAttribute("type", "button")
        setAttribute("value", "Click Me")
    }.also {
        p.appendChild(it)
    }

    val counterElement = document.createElement("p")
        .apply { textContent = "0" }
        .also { p.appendChild(it) }


    Counter.step = 10

    setOnClick(buttonElement) {
        Counter.increment()
        counterElement.textContent = Counter.value.toString()
    }

    val subtract = makeSubtract(add = { a, b -> a + b }, negate = { a -> -a })
    log("10 - 7 = " + subtract(10, 7))

    val rectangle = Rectangle(5.5, 10.0)
    log("Rectangle(5.6, 10).area = ${rectangle.area}")

}