package app.meetacy.sdk

import app.meetacy.api.MeetacyApi
import app.meetacy.sdk.auth.Token

public class MeetacySdk(public val executor: MeetacyApi) {



    public class Authorized(
        public val token: Token,
        public val base: MeetacySdk
    ) {

    }
}
