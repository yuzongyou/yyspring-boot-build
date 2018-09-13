(function ($, Vue) {
    'use strict';

    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    Vue.component('gviewIframe', vueComponent()); //主页

    function vueComponent() {
        return vueOpts();

        function vueOpts() {
            var options = {
                props: {
                    url: {type: String, required: false, default: ""}
                },
                data: function () {
                    return {
                        iframeUrl: validUrl(this.url) ? getIframeUrl(this.url) : ""
                    }
                },
                template: "<div class='gview-iframe-wrapper'><slot><iframe class='gview-iframe' :src='iframeUrl' @load='loadIframe'></iframe></slot></div>",
                methods: {
                    loadIframe: loadIframe,
                    reload: reload
                },
                watch: {
                    url: function (value, oldValue) {
                        if (value != oldValue) {
                            top.layer.load(2)
                        }
                        if (value != oldValue && validUrl(value)) {
                            this.iframeUrl = getIframeUrl(value);

                        } else {
                            top.layer.closeAll("loading");
                        }
                    }
                }
            };
            return options;

            function loadIframe() {

                top.layer.closeAll("loading");
            }

            function reload() {
                let $iframe = $(this.$el).children("iframe:first");
                $iframe.attr("src", $iframe.attr("src"));

            }

            function getIframeUrl(url) {
                return gutils.baseUrl(url);
            }

            function validUrl(url) {
                if (url == "/" || url == "/index.html" || url == "/index.html".baseUrl()) {
                    return false;
                }
                var parentWin = window;
                do {
                    if (parentWin.location.pathname == url) return false;
                    parentWin = parentWin.parent;
                } while (parentWin != window.top) ;
                return true;
            }
        }
    }


}(jQuery, Vue));