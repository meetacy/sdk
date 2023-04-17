
Meetacy SDK
===========

[![GitHub license](https://img.shields.io/github/license/meetacy/sdk?style=flat-square)](https://github.com/meetacy/sdk/blob/main/LICENSE) [![GitHub issues](https://img.shields.io/github/issues/meetacy/sdk?style=flat-square)](https://github.com/meetacy/sdk/issues) [![GitHub stars](https://img.shields.io/github/stars/meetacy/sdk?style=flat-square)](https://github.com/meetacy/sdk/stargazers) [![API documentation](https://img.shields.io/badge/API%20documentation-swagger-brightgreen?style=flat-square)](https://api.meetacy.app/swagger)

Meetacy SDK is a Kotlin library that provides a convenient way to access the Meetacy HTTP API. The API documentation is available at [api.meetacy.app/swagger](https://api.meetacy.app/swagger).

Getting started
---------------

To use the Meetacy SDK, you need to create an API client instance. You can do this using the `MeetacyApi.production()` method:

```kotlin
val client = MeetacyApi.production()
```

Once you have a client instance, you can call any API method by looking at its URL in the API documentation. For example, to call the `/auth/generate` method, you can use the `client.auth.generate(...)` method:

```kotlin
val response = client.auth.generate(nickname = "Alex Sokol")
```

Some API methods require an authentication token to work. If you don't want to manually pass the token every time, you can use the `AuthorizedMeetacyApi` class. This class allows you to provide a token once and then use it with all requests. To create an authorized client, use the `client.authorized(token: Token)` method:

```kotlin
val authorizedClient = client.authorized(token)
```

Alternatively, you can use the `client.auth.generateAuthorizedApi(...)` method to generate an authorized client instance directly:

```kotlin
val authorizedClient = client.auth.generateAuthorizedApi(token)
```

License
-------

This library is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

Issues and feedback
-------------------

If you encounter any issues with the Meetacy SDK, please report them in the [issues section](https://github.com/meetacy/sdk/issues).

If you have any feedback or suggestions for improvement, feel free to open an issue or contact the developer directly.
