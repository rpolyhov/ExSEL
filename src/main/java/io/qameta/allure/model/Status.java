package io.qameta.allure.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(
        name = "Status"
)
@XmlEnum
public enum Status {
    @XmlEnumValue("failed")
    FAILED("failed"),
    @XmlEnumValue("broken")
    BROKEN("broken"),
    @XmlEnumValue("passed")
    PASSED("passed"),
    @XmlEnumValue("skipped")
    SKIPPED("skipped"),
    @XmlEnumValue("blocked")
    BLOCKED("blocked");

    private final String value;

    private Status(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static Status fromValue(String v) {
        Status[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Status c = var1[var3];
            if (c.value.equals(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }
}
