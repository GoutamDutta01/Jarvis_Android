import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

ALLOWED = {
    "open_app", "google_search", "youtube_search", "maps_search", "navigate",
    "call", "take_photo", "record_video", "play_pause", "next_track",
    "previous_track", "set_alarm", "open_settings", "home", "back"
}

class Handler(BaseHTTPRequestHandler):
    def _json(self, status, payload):
        body = json.dumps(payload).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def do_POST(self):
        if self.path != "/v1/plan":
            return self._json(404, {"error": "not_found"})
        try:
            length = int(self.headers.get("Content-Length", "0"))
            data = json.loads(self.rfile.read(length))
            command = str(data.get("command", "")).strip()
            if not command or len(command) > 1000:
                return self._json(400, {"error": "invalid_command"})

            # Provider integration belongs here. Keep credentials in server
            # environment variables, never in the Android APK or repository.
            # Until a provider adapter is configured, fail closed instead of
            # pretending to execute an LLM plan.
            if not os.getenv("LLM_PROVIDER_API_KEY"):
                return self._json(503, {"error": "llm_provider_not_configured"})

            return self._json(501, {"error": "provider_adapter_not_implemented"})
        except (ValueError, json.JSONDecodeError):
            return self._json(400, {"error": "invalid_json"})

    def log_message(self, *_):
        pass

if __name__ == "__main__":
    port = int(os.getenv("PORT", "8080"))
    ThreadingHTTPServer(("0.0.0.0", port), Handler).serve_forever()
