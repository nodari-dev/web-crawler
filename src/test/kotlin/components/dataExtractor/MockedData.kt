package components.dataExtractor

object MockedData {
    const val title = "Simple HTML Example"
    const val OGtitle = "OGSimple HTML Example"
    const val heading = "Hello World!"
    const val paragraph = "This is a simple HTML example."
    const val metaDescription = "Meta description"
    const val keywords = "HTML, example, simple, introduction"
    const val imageAlt = "imageAlt"

    val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>$title</title>
            <meta name="description" content="$metaDescription">
            <meta name="keywords" content="$keywords">
        </head>
        <body>
            <h1>$heading</h1>
            <p>$paragraph</p>
            <img src="https://example.com" alt="$imageAlt">
        </body>
        </html>
    """

    const val htmlOG = """
        <!DOCTYPE html>
        <html>
        <head>
            <title></title>
            <meta property="og:title" content="$OGtitle">
            <meta name="description" content="">
            <meta name="keywords" content="">
        </head>
        <body>
            <h1></h1>
            <p></p>
            <img src="https://example.com" alt="">
        </body>
        </html>
    """

    const val emptyHTML = """
        <!DOCTYPE html>
        <html>
        <head>
            <title></title>
        </head>
        <body>
            <h1></h1>
            <p></p>
        </body>
        </html>
    """

    const val url = "https://example.com"

    fun getKeyWords(): Map<String, Int> {
        val keywords = HashMap<String, Int>()
        keywords["simple"] = 3
        keywords["html"] = 3
        keywords["example"] = 2
        keywords["hello"] = 1
        keywords["description"] = 1
        keywords["world!"] = 1
        keywords["imagealt"] = 1
        keywords["meta"] = 1
        keywords["example."] = 1
        keywords["introduction"] = 1
        keywords["a"] = 0
        keywords["this"] = 0
        keywords["is"] = 0

        return keywords.entries.sortedByDescending { keyword -> keyword.value }.associate { it.toPair() }
    }

    fun getOgKeyWords(): HashMap<String, Int> {
        val keywords = HashMap<String, Int>()
        keywords["ogsimple"] = 1
        keywords["html"] = 1
        keywords["example"] = 1

        return keywords
    }
}