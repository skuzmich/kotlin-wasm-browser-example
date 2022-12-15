import org.w3c.dom.*
import kotlinx.browser.*
import kotlinx.coroutines.*

suspend fun runCoroutinesExample() {
    val p = document.createElement("p")
        .apply { innerHTML = "<h1>Coroutines</h1><p>Click on coroutine to cancel</p>" }
        .also { document.body!!.appendChild(it) }

    coroutineScope {
        for (i in 1..10) {
            val el = document.createElement("div") as HTMLDivElement
            p.appendChild(el)
            val style = el.style
            style.apply {
                color = "white"
                background = "black"
                width = "0%"
            }
            el.textContent = "Coroutine#$i"
            launch {
                var cancelled = false
                el.onclick = { cancelled = true; null }
                for (j in 1 .. 500) {
                    if (cancelled)
                        break
                    delay(i * 5L)
                    style.width = "${j/5.0}%"
                }
                style.background = "grey"
                el.textContent += if (cancelled) " cancelled!" else " done!"
            }
        }
    }
    p.appendChild(document.createElement("p").apply { textContent = "All coroutines finished!"})
}