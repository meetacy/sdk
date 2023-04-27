import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.upload
import app.meetacy.sdk.io.asMeetacyInputSource
import app.meetacy.sdk.io.readIterative
import app.meetacy.sdk.io.use
import app.meetacy.sdk.production
import app.meetacy.sdk.types.auth.Token
import kotlinx.browser.document
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.files.get

@OptIn(DelicateCoroutinesApi::class)
public fun main() {
    val sdk = MeetacyApi.production()

    val node = document.getElementById("input") as HTMLInputElement
    val button = document.getElementById("button") as HTMLElement

    button.onclick = {
        GlobalScope.launch {
            val fileId = sdk.files.upload(
                token = Token("66:CttTaeSxGW7V2eW0UNHh1RBGU74xjznSy2kji5KcBFXNpilDCa8DFojTy228mi5Pc3o4xPYWUj0Jt8HbDotx0acmH8eaAmu0dnGp7Kg69Is49p3jogWu5NOdfUm6JVJ63jSUiswN4sXNrP35FghTqDscyFsdYLGOVVIzKBiJmIfS15ZgMymb4VZVGuNeMD0Va53C6Y1CZk8bUDUoCTTQgqYkGCBIDgcyc8ZT1dcPw2xuFl9vNqKnPbPxULW0slTG"),
                source = node.files!![0]!!
            ) { uploaded, totalBytes ->
                println("Uploaded ${uploaded}b / ${totalBytes}b (${uploaded.toDouble() / totalBytes * 100}%)...")
            }

            println(fileId)
        }
    }
}
