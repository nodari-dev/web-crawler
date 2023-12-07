package application.crawler

class Mocks {
    val html = """
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Basic HTML with Links</title>
        </head>

        <body>

            <h1>Welcome to My Website!</h1>

            <p>This is a basic HTML template with some links:</p>

            <ul>
                <li><a href="https://www.example.com" target="_blank">Example Website</a></li>
                <li><a href="https://www.google.com" target="_blank">Google</a></li>
                <li><a href="https://www.github.com" target="_blank">GitHub</a></li>
            </ul>

        </body>

        </html>

    """
}