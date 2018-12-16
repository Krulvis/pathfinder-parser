import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection


fun main(args: Array<String>) {
    Parser().parse()
}

class Parser {

    val baseFile = File("lib/item")

    val USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0"
    fun parse() {
        val files = getFiles()
        files.filter { it.isFile }.take(5).forEach {
            val data = InputStreamReader(it.inputStream()).readText()
            println(data)

            val url = "http://localhost/api/parse"
            val obj = URL(url)
            val con = obj.openConnection() as HttpURLConnection

            //add reuqest header
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type" , "application/json")
            con.setRequestProperty("User-Agent", USER_AGENT)
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5")

            // Send post request
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(data)
            wr.flush()
            wr.close()

            val responseCode = con.responseCode

            println("\nSending 'POST' request to URL : $url")
            println("Response Code : $responseCode")

            handleResponse(con)
        }
    }


    fun getFiles(): FileTreeWalk {
        return baseFile.walkTopDown()
    }

    fun handleResponse(con: HttpURLConnection) {

        val incoming = BufferedReader(
            InputStreamReader(con.inputStream)
        )
        val response = incoming.readLines().joinToString("\n")
        incoming.close()

        //print result
        println(response)
    }
}