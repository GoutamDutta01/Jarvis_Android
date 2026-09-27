import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

from llm_provider import plan_with_openai, validate_plan

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
            if not os.getenv("LLM_PROVIDER_API_KEY"):
                return self._json(503, {"error": "llm_provider_not_configured"})
            return self._json(200, validate_plan(plan_with_openai(command)))
        except Exception:
            # Do not expose provider errors or credentials to the client.
            return self._json(502, {"error": "planning_failed"})

    def log_message(self, *_):
        pass

if __name__ == "__main__":
    port = int(os.getenv("PORT", "8080"))
    ThreadingHTTPServer(("0.0.0.0", port), Handler).serve_forever()
