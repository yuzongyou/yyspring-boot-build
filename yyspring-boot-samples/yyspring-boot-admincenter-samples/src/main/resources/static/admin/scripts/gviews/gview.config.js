(function ($, Vue) {
    'use strict';

    var gview = top.gview || getGview();

    function getGview() {
        var gview = {
            config: {
                staticRoot: "",  //静态页面根路径；
                mainUrlMap: {
                    "/views/login.html": "/views/login.html",
                    "/views/403.html": "/views/403.html"
                },
                sessionUrl: "" //拉取用户sessionUrl；
            },
            utils: {
                $rootModel: {},   //全局Vue实例对象，放在index.html；
                router: {
                    currentHash: "", //全局当前页面url;
                    previousHash: ""
                }
            },
            webapp: {
                session: {
                    user: {},
                    privileges: [],
                    product: {
                        logoutUrl: "",
                        productName: "管理系统"
                    }
                },
                privilegeKeyMap: {},
                privilegeMenuTree: []
            },
        };
        //自动计算静态页面根路径；
        var staticRoot = (function () {
            var path = "";
            var scripts = document.getElementsByTagName("script");
            for (var n = 0; n < scripts.length; n++) {
                var script = scripts[n];
                if (script.src.match(/\/gviews\//)) {
                    path = dirname(script.src);
                    break;
                }
            }

            path = path.replace(window.location.protocol + "//" + window.location.host, "");
            if (path == "/") return "";
            return path;
        }());
        gview.config.staticRoot = staticRoot;

        return gview;
    }

    // ========= 挂载全局对象 ========= //
    window.gview = gview;
    $.each(gview, function (index, item) {
        window["g" + index] = item;
    });

    Vue.use(function (Vue, options) {
        Vue.gview = gview;
        Vue.prototype.$gview = gview;

        $.each(gview, function (index, item) {
            let key = "g" + index;
            Vue[key] = item;
            Vue.prototype["$" + key] = item
        });
    });

    // ========= 挂载全局对象 ========= //


    function dirname(path) {
        // http://localhost.yy.com:8666/statics/scripts/gviews/gview.webapp.js
        // http://localhost.yy.com:8666/statics/
        return path.substring(0, path.indexOf("/gviews/")).split("/").slice(0, -1).join("/") + "/";
    }

}(jQuery, Vue));