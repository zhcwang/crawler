package com.leaning.crawler.ocr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CJYConstants {
    public static final String DOCAPTCHA_URL = "http://upload.chaojiying.net/Upload/Processing.php";
    public static final String REPORTERROR_URL = "http://upload.chaojiying.net/Upload/ReportError.php";
    public static final Map<String, String> CJYTYPE;

    static {
        Map<String, String> temType = new HashMap();
        temType.put("NUM_4", "4004");
        temType.put("NUM_5", "4005");
        temType.put("NUM_6", "4006");
        temType.put("NUM_7", "4007");
        temType.put("NUM_8", "4008");
        temType.put("LETTER_4", "3004");
        temType.put("LETTER_5", "3005");
        temType.put("LETTER_6", "3006");
        temType.put("LETTER_7", "3007");
        temType.put("LETTER_8", "3008");
        temType.put("NUMANDLETTER_4", "1004");
        temType.put("NUMANDLETTER_5", "1005");
        temType.put("NUMANDLETTER_6", "1006");
        temType.put("NUMANDLETTER_7", "1007");
        temType.put("NUMANDLETTER_8", "1008");
        temType.put("NUMANDLETTER_9", "1009");
        temType.put("NUMANDLETTER_10", "1010");
        temType.put("CALCULATION", "6001");
        temType.put("SLIDE", "9101");
        temType.put("QUESTION", "9004");
        temType.put("CHARACTER_1", "2001");
        temType.put("CHARACTER_2", "2002");
        temType.put("CHARACTER_3", "2003");
        temType.put("CHARACTER_4", "2004");
        temType.put("CHARACTER_5", "2005");
        temType.put("CHARACTER_6", "2006");
        temType.put("CHARACTER_7", "2007");
        CJYTYPE = Collections.unmodifiableMap(temType);
    }

    public CJYConstants() {
    }
}
