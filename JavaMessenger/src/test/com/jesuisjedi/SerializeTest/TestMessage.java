package com.jesuisjedi.SerializeTest;

import java.io.Serializable;
import java.util.ArrayList;

public class TestMessage implements Serializable {
    public EnumTest enumTest;
    public Object payload;
    public ArrayList<String> recipients;

    public TestMessage(EnumTest enumTest, Object payload, ArrayList<String> recipients) {
        this.enumTest = enumTest;
        this.payload = payload;
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        final String enumTestStr = (enumTest == null) ? "null" : enumTest.toString();
        final String payloadStr = (payload == null) ? "null" : payload.toString();
        final String recipientsStr = (recipients == null) ? "null" : recipients.toString();

        return "TestMessage: {"
                + "enumTest: " + enumTestStr + ", "
                + " payload=" + payloadStr + ", "
                + " recipients=" + recipientsStr
                + " }";
    }
}
