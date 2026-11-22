package flynn.HA5.code;

// baut die nachrichten für json-rpc
public final class JsonRpc {

    // anfrage
    public static String request(String method, String params, int id) {
        return "{\"jsonrpc\":\"2.0\",\"method\":\"" + method + "\",\"params\":[" + params + "],\"id\":" + id + "}";
    }

    // antwort
    public static String result(String result, String id) {
        return "{\"jsonrpc\":\"2.0\",\"result\":" + result + ",\"id\":" + id + "}";
    }

    // antwort
    public static String error(int code, String message, String id) {
        return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":" + code + ",\"message\":" + Json.quote(message) + "},\"id\":" + id + "}";
    }
}
