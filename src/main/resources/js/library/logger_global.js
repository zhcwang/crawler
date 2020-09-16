// JavaScript脚本执行引擎全局对象上下文，全局共享

function info(msg) {
    com.leaning.crawler.js.JavaScriptExecutor.logger.info(msg);
}

function warn(msg) {
    com.leaning.crawler.js.JavaScriptExecutor.logger.warn(msg);
}

function error(msg) {
    com.leaning.crawler.js.JavaScriptExecutor.logger.error(msg);
}

var logger = {
    info: info,
    warn: warn,
    error: error
};