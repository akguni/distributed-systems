package flynn.HA5.code;

// allgemeine hilfsklasse für json-text
public final class Json {

    // gibt den value zu einem key in einem json zurück
    // beispiel: json = {"jsonrpc":"2.0","method":"getRecord","id":7}
    // aufruff beispiel: getValue(json, "method") -> "getRecord"
    public static String getValue(String json, String key) {
        int i = json.indexOf("\"" + key + "\""); // sucht "method", i zeigt auf das erste "
        if (i < 0) {
            return null; // key kommt nicht vor
        }
        i = json.indexOf(':', i) + 1; // hinter den doppelpunkt, übrig: "getRecord","id":7}
        while (json.charAt(i) == ' ') {
            i++; // leerzeichen nach dem doppelpunkt überspringen
        }
        int end = i;
        if (json.charAt(i) == '"') { // text: bis zum schließenden anführungszeichen
            end = i + 1; // hinter das erste ", übrig: getRecord","id":7}
            while (json.charAt(end) != '"') { // läuft bis zum nächsten "
                if (json.charAt(end) == '\\') { // z.b. \" im text ist noch nicht das ende
                    end++; // zeichen nach dem backslash überspringen
                }
                end++;
            }
            end++; // das schließende " gehört noch dazu, ausgeschnitten: "getRecord"
        } else { // zahl oder null: bis komma oder klammer, bei "id":7} ausgeschnitten: 7
            while (json.charAt(end) != ',' && json.charAt(end) != '}') {
                end++;
            }
        }
        return json.substring(i, end).trim(); // schneidet von i bis end aus dem json aus
    }

    // setzt anführungszeichen um text, z.b.
    // Wedel -> "Wedel"
    // Er sagte "hi" -> "Er sagte "hi""
    // C:\temp ->	"C:\temp"
    public static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    // nimmt die anführungszeichen wieder weg, z.b. "Wedel" -> Wedel
    public static String unquote(String text) {
        String inner = text.substring(1, text.length() - 1);
        return inner.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
