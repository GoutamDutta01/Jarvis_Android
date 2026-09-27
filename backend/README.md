# Jarvis AI Gateway

This folder is the server-side boundary for the Android app's LLM integration.

## Why a gateway

Do not ship an OpenAI/Groq/etc. API key inside the APK. The Android client should call this gateway over HTTPS, and the gateway keeps provider credentials server-side.

## Contract

`POST /v1/plan`

Request:
```json
{"command":"Open YouTube and search for Python tutorials"}
```

Response:
```json
{"actions":[{"type":"open_app","value":"YouTube"},{"type":"youtube_search","value":"Python tutorials"}]}
```

The server must validate the action schema before returning a plan. It should never return arbitrary executable code.

A provider implementation is intentionally left configurable through environment variables.
