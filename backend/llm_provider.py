import json
import os
from urllib.request import Request, urlopen

SYSTEM_PROMPT = '''You are Jarvis Android's action planner.
Return JSON only in this shape: {"actions":[{"type":"...","value":"..."}]}.
Allowed action types: open_app, google_search, youtube_search, maps_search, navigate, call,
take_photo, record_video, play_pause, next_track, previous_track, set_alarm,
open_settings, home, back.
Use multiple actions only when the user explicitly asks for a sequence.
Never invent phone numbers, contacts, credentials, or destructive actions.
For set_alarm, value must be HH:MM|label. If required information is missing, return {"actions":[]}.
'''


def plan_with_openai(command: str) -> dict:
    api_key = os.environ["LLM_PROVIDER_API_KEY"]
    model = os.getenv("LLM_MODEL", "gpt-4o-mini")
    payload = {
        "model": model,
        "temperature": 0,
        "response_format": {"type": "json_object"},
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": command},
        ],
    }
    request = Request(
        "https://api.openai.com/v1/chat/completions",
        data=json.dumps(payload).encode(),
        headers={
            "Authorization": f"Bearer {api_key}",
            "Content-Type": "application/json",
        },
        method="POST",
    )
    with urlopen(request, timeout=30) as response:
        data = json.loads(response.read().decode())
    content = data["choices"][0]["message"]["content"]
    return json.loads(content)


def validate_plan(plan: dict) -> dict:
    allowed = {
        "open_app", "google_search", "youtube_search", "maps_search", "navigate",
        "call", "take_photo", "record_video", "play_pause", "next_track",
        "previous_track", "set_alarm", "open_settings", "home", "back",
    }
    actions = []
    for item in plan.get("actions", []):
        if not isinstance(item, dict):
            continue
        action_type = str(item.get("type", "")).strip()
        value = str(item.get("value", "")).strip()
        if action_type in allowed and len(value) <= 500:
            actions.append({"type": action_type, "value": value})
    return {"actions": actions}
