@file:Suppress("USELESS_CAST")

//import app.meetacy.sdk.Meetacy
//import UserId

//val client = Meetacy.production(enableLogging = false)
//
//val otherUserId = UserId(1)
//val otherUserAccessHash = AccessHash("7Y2AJiQwUaxtqWOWBMRDKHwBha1y2ckC4pe37YigbRsbMrlSGuFOPV6XCf2AcQMrPUfiKRvcCTIoBLrIlWRPooyd7xFkYpRYJP2fyexZLnFYFOoGZ3l2n3FkpBvAaAapsCaXIbjgw3CdohtNQwmsMx5mfYbzXwiFBiw5HOvOthQ7JsZArbi6brv1pRgUqtrTB9qck07fgL48Y64nfIzXrCsNNfvZnepNOFIZjYl2HGiDLbL9UdtiGwZfOmfYtkQi1659696268385")
//
//suspend fun main() {
//    val token = client.generateToken("Alex Sokol")
//        .connection.successOr { showConnectionError(it) { return } }
//        .generateToken.success
//
//    val (authorizedClient, user) = client.authorize(token)
//        .connection.successOr { showConnectionError(it) { return } }
//        .authorizedClient.successOr { error("Token was generated just now, this should not happen") }
//
//    println("Created user: ")
//    println("User id: ${user.id.id}")
//    println("User access hash: ${user.accessHash.hash}")
//    println("User name: ${user.nickname}")
//
//    val otherUser = authorizedClient.getUser(otherUserId, otherUserAccessHash)
//        .connection.successOr { showConnectionError(it) { return } }
//        .authorizedResult.getUser
//        .successOr { return println("Cannot find a user. Check if the endpoint you are connecting to is valid.") }
//
//    println()
//    println("Other user: ")
//    println("User id: ${otherUser.id.id}")
//    println("User access hash: ${otherUser.accessHash.hash}")
//    println("User name: ${otherUser.nickname}")
//}
//
//private inline fun showConnectionError(failure: ConnectionEither.Failure, returns: () -> Nothing): Nothing {
//    when (failure) {
//        ConnectionEither.Failure.HostNotFound ->
//            println("Cannot connect to the server. Check if it is running or you have internet access.")
//        ConnectionEither.Failure.ServerError ->
//            println("Internal server error, see http logs for details")
//    }
//    returns()
//}
