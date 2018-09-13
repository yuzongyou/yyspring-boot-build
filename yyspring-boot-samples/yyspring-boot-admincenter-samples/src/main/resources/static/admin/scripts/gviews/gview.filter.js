(function ($, Vue) {
    'use strict';

    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    // 切取定长字符串,超出隐藏
    Vue.filter('cutText', function (text, length) {
        var displayLength = length || text.length;
        if (!text) return "";

        var result = "";
        var count = 0;
        for (var i = 0; i < displayLength; i++) {
            var _char = text.charAt(i);
            if (count >= displayLength) break;
            if (/[^x00-xff]/.test(_char)) count++;  //双字节字符，//[u4e00-u9fa5]中文

            result += _char;
            count++;
        }
        if (result.length < text.length) {
            result += "...";
        }
        return result;
    });

    // 时间缀转时间
    Vue.filter('timePretty', function (text, type) {
        var display = type ? type : "yyyy-MM-dd HH:mm:ss";
        if (!text) return "";

        var dDate = typeof text === "date" ? text : new Date(text);

        return dDate.format(display);
    });

    Vue.filter('textareaPretty', function (text) {
        if (!text) return "";

        text = text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#x27;").replaceAll("&", "&amp;").replaceAll("\\\\", "&#92;");
        text = text.replaceAll("\\r\\n", "<br/>").replaceAll("\\n", "<br/>").replaceAll("\\r", "<br/>");
        return text.trim();
    });

    Vue.filter('trimZero', function (text) {
        if (!text) return "";
        var zero = 0;
        try {
            zero = parseFloat(text);
        } catch (e) {
            zero = 0;
        }
        if (!zero) return "";
        return text;
    });

}(jQuery, Vue));