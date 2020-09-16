package com.leaning.crawler.js;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Global;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * JavaScript脚本执行工具类
 */
public class JavaScriptExecutor<ocr> {

    /**
     * JavaScript引擎全局对象上下文（包含预定义的变量与函数）
     */
    private static Global sharedScope;

    /**
     * 第三方JavaScript脚本文件夹路径
     */
    private static final String VENDOR_JS_FOLDER_PATH = "src/main/resources/js/vender";

    /**
     * 第三方JavaScript脚本文件加载顺序列表
     */
    private static final List<String> VENDOR_JS_FILE_LOAD_LIST = Arrays.asList("env.rhino.1.2.js", "jquery.min.js");

    /**
     * 自定义全局JavaScript脚本文件夹路径
     */
    private static final String CUSTOM_GLOBAL_JS_FOLDER_PATH = "src/main/resources/js/library";

    /**
     * 第三方JavaScript脚本文件加载顺序列表
     */
    private static final List<String> CUSTOM_GLOBAL_JS_FILE_LOAD_LIST = Arrays.asList("logger_global.js","encode_decode_global.js");

    /**
     * 自定义实例JavaScript脚本文件夹路径
     */
    private static final String CUSTOM_INSTANCE_JS_FOLDER_PATH = "src/main/resources/js/instance";

    /**
     * 第三方JavaScript脚本文件加载顺序列表
     */
    private static final List<String> CUSTOM_INSTANCE_JS_FILE_LOAD_LIST = Arrays.asList("logger_instance.js");


    static {
        try {
            // 初始化JavaScript引擎全局对象上下文
            Context ctx = Context.enter();
            sharedScope = new Global(ctx);
            ctx.setOptimizationLevel(-1);
            loadJsScriptFile(ctx, sharedScope, VENDOR_JS_FOLDER_PATH, VENDOR_JS_FILE_LOAD_LIST);
            loadJsScriptFile(ctx, sharedScope, CUSTOM_GLOBAL_JS_FOLDER_PATH, CUSTOM_GLOBAL_JS_FILE_LOAD_LIST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Context.exit();
        }
    }

    /**
     * 在JavaScript引擎中加载预设的JavaScript脚本
     * @param ctx JavaScript引擎执行上下文
     * @param scope JavaScript引擎对象上下文
     * @param folderPath JavaScript脚本文件夹路径
     * @param fileNames 指定加载顺序的文件名列表
     */
    private static void loadJsScriptFile(Context ctx, Scriptable scope, String folderPath, List<String> fileNames) throws IOException {
        if (fileNames != null && fileNames.size() > 0) {
            for (String fileName : fileNames) {
                String filePath = folderPath + "/" + fileName;
                String fileContent = getJSString(filePath);
                String sourceName = fileName.replace(".js", "");
                ctx.evaluateString(scope, fileContent, sourceName, 1, null);
            }
        }
    }

    /**
     * 执行JavaScript脚本，获取执行结果
     * @param jsScript JavaScript脚本语句
     * @param inputParams 输入参数（作为脚本中的全局变量）
     * @param outputParamNames 输出参数名称列表（从脚本全局变量中提取）
     * @return 脚本执行结果
     */
    public static Map<String, Object> executeScript(String jsScript, Map<String, Object> inputParams, List<String> outputParamNames) {
        try {
            Context ctx = Context.enter();
            Scriptable instanceScope = ctx.initStandardObjects();
            return execute(ctx, instanceScope, jsScript, inputParams, outputParamNames);
        } finally {
            Context.exit();
        }
    }

    /**
     * 执行JavaScript转换脚本，获取执行结果
     * @param jsScript JavaScript脚本语句
     * @param inputParams 输入参数（作为脚本中的全局变量）
     * @param outputParamNames 输出参数名称列表（从脚本全局变量中提取）
     * @return 脚本执行结果
     */
    public static Map<String, Object> executeConvertScript(String jsScript, Map<String, Object> inputParams, List<String> outputParamNames) throws IOException {
        try {
            // 创建线程本地JavaScript执行上下文
            Context ctx = Context.enter();
            Scriptable instanceScope = ctx.initStandardObjects();
            instanceScope.setPrototype(sharedScope);
            loadJsScriptFile(ctx, instanceScope, CUSTOM_INSTANCE_JS_FOLDER_PATH, CUSTOM_INSTANCE_JS_FILE_LOAD_LIST);
            return execute(ctx, instanceScope, jsScript, inputParams, outputParamNames);
        } finally {
            Context.exit();
        }
    }

    /**
     * 在JavaScript引擎中执行脚本
     * @param ctx JavaScript引擎执行上下文
     * @param scope JavaScript引擎对象上下文
     * @param jsScript JavaScript脚本语句
     * @param inputParams 输入参数（作为脚本中的全局变量）
     * @param outputParamNames 输出参数名称列表（从脚本全局变量中提取）
     * @return 脚本执行结果
     */
    private static Map<String, Object> execute(Context ctx, Scriptable scope, String jsScript,
                                               Map<String, Object> inputParams, List<String> outputParamNames) {
        Map<String, Object> result = new HashMap<String, Object>();
        Script script = ctx.compileString(jsScript, "jsScript", 1, null);
        if (inputParams != null) {
            Set<Map.Entry<String, Object>> entries = inputParams.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                ScriptableObject.putProperty(scope, entry.getKey(), entry.getValue());
            }
        }
        script.exec(ctx, scope);
        if (outputParamNames != null) {
            for (String paramName : outputParamNames) {
                result.put(paramName, ScriptableObject.getProperty(scope, paramName));
            }
        }
        return result;
    }

    /**
     * 获取classpath下的JS脚本字符串
     * @param jsFilePath classpath下的JS脚本文件相对路径
     * @return JS脚本字符串
     * @throws IOException
     */
    public static String getJSString(String jsFilePath) throws IOException {
        return FileUtils.readFileToString(new File(jsFilePath), "UTF-8");
    }

    public static String encodeByCharset(String str, String charset) {
		String result = null;
		try {
			result = URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String decodeByCharset(String str, String charset) {
		String result = null;
		try {
			result = URLDecoder.decode(str, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static Object nativeJavaObjectToObject(Object reqUrl) {
		reqUrl = (reqUrl instanceof NativeJavaObject) ? ((NativeJavaObject) reqUrl).getDefaultValue(String.class)
				: reqUrl;
		return reqUrl;
	}

    public static boolean nativeJavaObjectToBoolean(Object obj) {
        obj = (obj instanceof NativeJavaObject) ? ((NativeJavaObject) obj).getDefaultValue(boolean.class)
                : obj;
        return (boolean)obj;
    }
}
