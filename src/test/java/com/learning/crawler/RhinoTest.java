package com.learning.crawler;

import com.leaning.crawler.js.JavaScriptExecutor;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RhinoTest {
    @Test
    public void executeScriptSimple() throws Exception {
        Map<String, Object> inputParams = new LinkedHashMap<>();
        inputParams.put("a", 1);
        inputParams.put("b", 2);
        List<String> outputParamNames = Arrays.asList("c");
        String jsScript =  FileUtils.readFileToString(new File("src/test/resources/js/test.js"), "UTF-8");
        Map<String, Object> result = JavaScriptExecutor.executeScript(jsScript, inputParams, outputParamNames);
        Object c = result.get("c");
        System.out.println(c.toString());
    }

    @Test
    public void encryptPassword() throws Exception {
        Map<String, Object> inputParams = new LinkedHashMap<String, Object>();
        inputParams.put("password", "aaa123");
        List<String> outputParamNames = Arrays.asList("result");
        String jsScript =  FileUtils.readFileToString(new File("src/test/resources/js/jsencrypt.js"), "UTF-8");
        Map<String, Object> result = JavaScriptExecutor.executeScript(jsScript, inputParams, outputParamNames);
        Object encryptedPassword = result.get("result");
        System.out.println(encryptedPassword.toString());
    }
}
