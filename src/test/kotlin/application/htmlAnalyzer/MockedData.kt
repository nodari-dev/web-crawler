package application.htmlAnalyzer

import core.dto.URLInfo

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

    val url = URLInfo("https://example.com")
}