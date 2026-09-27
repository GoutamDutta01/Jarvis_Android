# Jarvis Backend

The backend is the server-side boundary for the Android app's LLM integration. Keep provider credentials on the server; never ship them inside the APK.

## Local run

```bash
export LLM_PROVIDER_API_KEY="your-server-side-key"
export LLM_MODEL="gpt-4o-mini"
python app.py
```

The API listens on `0.0.0.0:8080` by default.

## API

`POST /v1/plan`

Request:
```json
{"command":"Open YouTube and search for Python tutorials"}
```

Response:
```json
{"actions":[{"type":"open_app","value":"YouTube"},{"type":"youtube_search","value":"Python tutorials"}]}
```

The server validates the action allowlist before returning a plan. It never returns arbitrary executable code.

## Docker

```bash
docker build -t jarvis-backend ./backend
docker run --rm -p 8080:8080 -e LLM_PROVIDER_API_KEY="$LLM_PROVIDER_API_KEY" jarvis-backend
```

For production, deploy the container behind HTTPS on a managed service or reverse proxy. Set `LLM_PROVIDER_API_KEY` and `LLM_MODEL` as server environment variables. Do not commit `.env` or provider keys.
