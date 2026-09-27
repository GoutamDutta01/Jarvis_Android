import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

from llm_provider import plan_with_openai, validate_plan

MAX_BODY_BYTES = 8_192

class Handler(BaseHTTPRequestHandler):
    def _json(self, status, payload):
        body = json.dumps(payload).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(body)

    def do_GET(self):
        if self.path == "/health":
            return self._json(200, {"status": "ok", "service": "jarvis-backend"})
        return self._json(404, {"error": "not_found"})

    def do_POST(self):
        if self.path != "/v1/plan":
            return self._json(404, {"error": "not_found"})
        try:
            raw_length = self.headers.get("Content-Length")
            length = int(raw_length or "0")
            if length <= 0 or length > MAX_BODY_BYTES:
                return self._json(413, {"error": "request_too_large"})
            data = json.loads(self.rfile.read(length))
            command = str(data.get("command", "")).strip()
            if not command or len(command) > 1000:
                return self._json(400, {"error": "invalid_command"})
            if not os.getenv("LLM_PROVIDER_API_KEY"):
                return self._json(503, {"error": "llm_provider_not_configured"})
            return self._json(200, validate_plan(plan_with_openai(command)))
        except (ValueError, json.JSONDecodeError):
            return self._json(400, {"error": "invalid_json"})
        except Exception:
            # Do not expose provider errors or credentials to the client.
            return self._json(502, {"error": "planning_failed"})

    def log_message(self, *_):
        pass

if __name__ == "__main__":
    port = int(os.getenv("PORT", "8080"))
    ThreadingHTTPServer(("0.0.0.0", port), Handler).serve_forever()
