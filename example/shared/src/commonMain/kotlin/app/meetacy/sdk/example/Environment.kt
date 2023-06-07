package app.meetacy.sdk.example

import app.meetacy.sdk.types.auth.Token

interface Environment {
    var currentToken: Token?
}