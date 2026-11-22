package flynn.HA6.code.common;

import java.io.Serializable;

public class DBResult implements Serializable { // Serializable: objekt kann über das netzwerk geschickt werden

    private int key;
    private String value;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
