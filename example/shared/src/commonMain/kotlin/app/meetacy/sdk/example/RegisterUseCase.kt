package app.meetacy.sdk.example

import app.meetacy.sdk.MeetacyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUseCase internal constructor(
    private val environment: Environment,
    private val meetacyApi: MeetacyApi
) {

    suspend fun register(nickName: String) {
        withContext(Dispatchers.Main) {
            environment.currentToken = meetacyApi.auth.generate(nickName)
        }
    }
}
