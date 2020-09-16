// JavaScript脚本执行引擎实例对象上下文，实例私有

var debugOutput = [];

function debug(debugInfo, data) {
    debugOutput.push({
        debugInfo: debugInfo,
        data: data
    });
}

logger.debug = debug;