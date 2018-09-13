(function ($, Vue) {
    'use strict';

    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    extendString();
    extendDate();
    extendJquery();
    extendGutils();

    extendGutilsBs();

    function extendString() {
        mix(String.prototype, {
            baseUrl: function () {
                var staticRoot = gconfig.staticRoot || "";

                var protocols = ["https://", "http://", "ftp://", "//"];
                for (var i in protocols) {
                    if (this.startsWith(protocols[i])) return this;
                }

                if (staticRoot && this.startsWith(gconfig.staticRoot)) {
                    return this;
                }

                return (staticRoot + this.trim()).replaceAll("//", "/");

            },
            trim: function () {
                return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            replaceAll: function (os, ns) {
                return this.replace(new RegExp(os, "gm"), ns);
            },
            startsWith: function (pattern) {
                return this.indexOf(pattern) === 0;
            },
            endsWith: function (pattern) {
                var d = this.length - pattern.length;
                return d >= 0 && this.lastIndexOf(pattern) === d;
            }
        });
    }

    function extendDate() {
        mix(Date.prototype, {
            format: function (fmt) {
                // (new Date()).Format("yyyy-MM-dd HH:mm:ss.S")
                var o = {
                    "M+": this.getMonth() + 1, //月份
                    "d+": this.getDate(), //日
                    "H+": this.getHours(), //小时
                    "m+": this.getMinutes(), //分
                    "s+": this.getSeconds(), //秒
                    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                    "S": this.getMilliseconds() //毫秒
                };
                if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            }
        });
    }

    function extendJquery() {
        extendCookie();
        extendUrl();

        function extendCookie() {
            var pluses = /\+/g;

            var config = $.cookie = function (key, value, options) {
                // 写
                if (value !== undefined && !$.isFunction(value)) {
                    options = $.extend({}, options);

                    if (typeof options.expires === 'number') {
                        var days = options.expires, t = options.expires = new Date();
                        t.setTime(+t + days * 864e+5);
                    }
                    return (document.cookie = [
                        encode(key), '=', stringifyCookieValue(value),
                        options.expires ? '; expires=' + options.expires.toUTCString() : '',
                        options.path ? '; path=' + options.path : '',
                        options.domain ? '; domain=' + options.domain : '',
                        options.secure ? '; secure' : ''
                    ].join(''));
                }

                // 读
                var result = key ? undefined : {};
                var cookies = document.cookie ? document.cookie.split('; ') : [];
                for (var i = 0, l = cookies.length; i < l; i++) {
                    var parts = cookies[i].split('=');
                    var name = decode(parts.shift());
                    var cookie = parts.join('=');

                    if (key && key === name) {
                        result = read(cookie, value);
                        break;
                    }
                    if (!key && (cookie = read(cookie)) !== undefined) {
                        result[name] = cookie;
                    }
                }

                return result;
            };

            config.defaults = {};

            $.removeCookie = function (key, options) {
                if ($.cookie(key) === undefined) {
                    return false;
                }
                $.cookie(key, '', $.extend({}, options, {expires: -1}));
                return !$.cookie(key);
            };

            function encode(s) {
                return config.raw ? s : encodeURIComponent(s);
            }

            function decode(s) {
                return config.raw ? s : decodeURIComponent(s);
            }

            function stringifyCookieValue(value) {
                return encode(config.json ? JSON.stringify(value) : String(value));
            }

            function parseCookieValue(s) {
                if (s.indexOf('"') === 0) {
                    s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
                }
                try {
                    s = decodeURIComponent(s.replace(pluses, ' '));
                    return config.json ? JSON.parse(s) : s;
                } catch (e) {
                }
            }

            function read(s, converter) {
                var value = config.raw ? s : parseCookieValue(s);
                return $.isFunction(converter) ? converter(value) : value;
            }
        }

        function extendUrl() {
            var urlFn = (function () {

                function _t() {
                    return new RegExp(/(.*?)\.?([^\.]*?)\.(gl|com|net|org|biz|ws|in|me|co\.uk|co|org\.uk|ltd\.uk|plc\.uk|me\.uk|edu|mil|br\.com|cn\.com|eu\.com|hu\.com|no\.com|qc\.com|sa\.com|se\.com|se\.net|us\.com|uy\.com|ac|co\.ac|gv\.ac|or\.ac|ac\.ac|af|am|as|at|ac\.at|co\.at|gv\.at|or\.at|asn\.au|com\.au|edu\.au|org\.au|net\.au|id\.au|be|ac\.be|adm\.br|adv\.br|am\.br|arq\.br|art\.br|bio\.br|cng\.br|cnt\.br|com\.br|ecn\.br|eng\.br|esp\.br|etc\.br|eti\.br|fm\.br|fot\.br|fst\.br|g12\.br|gov\.br|ind\.br|inf\.br|jor\.br|lel\.br|med\.br|mil\.br|net\.br|nom\.br|ntr\.br|odo\.br|org\.br|ppg\.br|pro\.br|psc\.br|psi\.br|rec\.br|slg\.br|tmp\.br|tur\.br|tv\.br|vet\.br|zlg\.br|br|ab\.ca|bc\.ca|mb\.ca|nb\.ca|nf\.ca|ns\.ca|nt\.ca|on\.ca|pe\.ca|qc\.ca|sk\.ca|yk\.ca|ca|cc|ac\.cn|com\.cn|edu\.cn|gov\.cn|org\.cn|bj\.cn|sh\.cn|tj\.cn|cq\.cn|he\.cn|nm\.cn|ln\.cn|jl\.cn|hl\.cn|js\.cn|zj\.cn|ah\.cn|gd\.cn|gx\.cn|hi\.cn|sc\.cn|gz\.cn|yn\.cn|xz\.cn|sn\.cn|gs\.cn|qh\.cn|nx\.cn|xj\.cn|tw\.cn|hk\.cn|mo\.cn|cn|cx|cz|de|dk|fo|com\.ec|tm\.fr|com\.fr|asso\.fr|presse\.fr|fr|gf|gs|co\.il|net\.il|ac\.il|k12\.il|gov\.il|muni\.il|ac\.in|co\.in|org\.in|ernet\.in|gov\.in|net\.in|res\.in|is|it|ac\.jp|co\.jp|go\.jp|or\.jp|ne\.jp|ac\.kr|co\.kr|go\.kr|ne\.kr|nm\.kr|or\.kr|li|lt|lu|asso\.mc|tm\.mc|com\.mm|org\.mm|net\.mm|edu\.mm|gov\.mm|ms|nl|no|nu|pl|ro|org\.ro|store\.ro|tm\.ro|firm\.ro|www\.ro|arts\.ro|rec\.ro|info\.ro|nom\.ro|nt\.ro|se|si|com\.sg|org\.sg|net\.sg|gov\.sg|sk|st|tf|ac\.th|co\.th|go\.th|mi\.th|net\.th|or\.th|tm|to|com\.tr|edu\.tr|gov\.tr|k12\.tr|net\.tr|org\.tr|com\.tw|org\.tw|net\.tw|ac\.uk|uk\.com|uk\.net|gb\.com|gb\.net|vg|sh|kz|ch|info|ua|gov|name|pro|ie|hk|com\.hk|org\.hk|net\.hk|edu\.hk|us|tk|cd|by|ad|lv|eu\.lv|bz|es|jp|cl|ag|mobi|eu|co\.nz|org\.nz|net\.nz|maori\.nz|iwi\.nz|io|la|md|sc|sg|vc|tw|travel|my|se|tv|pt|com\.pt|edu\.pt|asia|fi|com\.ve|net\.ve|fi|org\.ve|web\.ve|info\.ve|co\.ve|tel|im|gr|ru|net\.ru|org\.ru|hr|com\.hr|ly|xyz)$/);
                }

                function _d(s) {
                    return decodeURIComponent(s.replace(/\+/g, ' '));
                }

                function _i(arg, str) {
                    var sptr = arg.charAt(0),
                        split = str.split(sptr);

                    if (sptr === arg) {
                        return split;
                    }

                    arg = parseInt(arg.substring(1), 10);

                    return split[arg < 0 ? split.length + arg : arg - 1];
                }

                function _f(arg, str) {
                    var sptr = arg.charAt(0),
                        split = str.split('&'),
                        field = [],
                        params = {},
                        tmp = [],
                        arg2 = arg.substring(1);

                    for (var i = 0, ii = split.length; i < ii; i++) {
                        field = split[i].match(/(.*?)=(.*)/);

                        // TODO: regex should be able to handle this.
                        if (!field) {
                            field = [split[i], split[i], ''];
                        }

                        if (field[1].replace(/\s/g, '') !== '') {
                            field[2] = _d(field[2] || '');

                            // If we have a match just return it right away.
                            if (arg2 === field[1]) {
                                return field[2];
                            }

                            // Check for array pattern.
                            tmp = field[1].match(/(.*)\[([0-9]+)\]/);

                            if (tmp) {
                                params[tmp[1]] = params[tmp[1]] || [];

                                params[tmp[1]][tmp[2]] = field[2];
                            }
                            else {
                                params[field[1]] = field[2];
                            }
                        }
                    }

                    if (sptr === arg) {
                        return params;
                    }

                    return params[arg2];
                }

                return function (arg, url) {
                    var _l = {}, tmp, tmp2;

                    if (arg === 'tld?') {
                        return _t();
                    }

                    url = url || window.location.toString();

                    if (!arg) {
                        return url;
                    }

                    arg = arg.toString();

                    if (tmp = url.match(/^mailto:([^\/].+)/)) {
                        _l.protocol = 'mailto';
                        _l.email = tmp[1];
                    }
                    else {

                        // Ignore Hashbangs.
                        if (tmp = url.match(/(.*?)\/#\!(.*)/)) {
                            url = tmp[1] + tmp[2];
                        }

                        // Hash.
                        if (tmp = url.match(/(.*?)#(.*)/)) {
                            _l.hash = tmp[2];
                            url = tmp[1];
                        }

                        // Return hash parts.
                        if (_l.hash && arg.match(/^#/)) {
                            return _f(arg, _l.hash);
                        }

                        // Query
                        if (tmp = url.match(/(.*?)\?(.*)/)) {
                            _l.query = tmp[2];
                            url = tmp[1];
                        }

                        // Return query parts.
                        if (_l.query && arg.match(/^\?/)) {
                            return _f(arg, _l.query);
                        }

                        // Protocol.
                        if (tmp = url.match(/(.*?)\:?\/\/(.*)/)) {
                            _l.protocol = tmp[1].toLowerCase();
                            url = tmp[2];
                        }

                        // Path.
                        if (tmp = url.match(/(.*?)(\/.*)/)) {
                            _l.path = tmp[2];
                            url = tmp[1];
                        }

                        // Clean up path.
                        _l.path = (_l.path || '').replace(/^([^\/])/, '/$1');

                        // Return path parts.
                        if (arg.match(/^[\-0-9]+$/)) {
                            arg = arg.replace(/^([^\/])/, '/$1');
                        }
                        if (arg.match(/^\//)) {
                            return _i(arg, _l.path.substring(1));
                        }

                        // File.
                        tmp = _i('/-1', _l.path.substring(1));

                        if (tmp && (tmp = tmp.match(/(.*?)\.([^.]+)$/))) {
                            _l.file = tmp[0];
                            _l.filename = tmp[1];
                            _l.fileext = tmp[2];
                        }

                        // Port.
                        if (tmp = url.match(/(.*)\:([0-9]+)$/)) {
                            _l.port = tmp[2];
                            url = tmp[1];
                        }

                        // Auth.
                        if (tmp = url.match(/(.*?)@(.*)/)) {
                            _l.auth = tmp[1];
                            url = tmp[2];
                        }

                        // User and pass.
                        if (_l.auth) {
                            tmp = _l.auth.match(/(.*)\:(.*)/);

                            _l.user = tmp ? tmp[1] : _l.auth;
                            _l.pass = tmp ? tmp[2] : undefined;
                        }

                        // Hostname.
                        _l.hostname = url.toLowerCase();

                        // Return hostname parts.
                        if (arg.charAt(0) === '.') {
                            return _i(arg, _l.hostname);
                        }

                        // Domain, tld and sub domain.
                        if (_t()) {
                            tmp = _l.hostname.match(_t());

                            if (tmp) {
                                _l.tld = tmp[3];
                                _l.domain = tmp[2] ? tmp[2] + '.' + tmp[3] : undefined;
                                _l.sub = tmp[1] || undefined;
                            }
                        }

                        // Set port and protocol defaults if not set.
                        _l.port = _l.port || (_l.protocol === 'https' ? '443' : '80');
                        _l.protocol = _l.protocol || (_l.port === '443' ? 'https' : 'http');
                    }

                    // Return arg.
                    if (arg in _l) {
                        return _l[arg];
                    }

                    // Return everything.
                    if (arg === '{}') {
                        return _l;
                    }

                    // Default to undefined for no match.
                    return undefined;
                };
            })();
            $.url = function (arg, url) {
                return urlFn(arg, url);
            }

        }
    }

    function extendGutils() {
        $.extend(gutils, {
            Attr: function (target, attrChain, value) {
                if (!target) return null;

                var isAttr = typeof attrChain !== 'undefined' && attrChain !== null;
                var isValue = typeof value !== 'undefined';
                if (isValue && !isAttr) {
                    target = value;
                    return;
                }

                if (!isValue && !attrChain) return;


                var attrChains = attrChain.split('.');
                var length = attrChains.length;
                var idx = 0;
                var o = target;
                if (isValue) {
                    while (idx < length - 1) {
                        if (o === null || typeof o[attrChains[idx]] === 'undefined') {
                            o[attrChains[idx]] = {};
                        }
                        o = o[attrChains[idx]];
                        idx++;
                    }
                    o[attrChains[idx]] = value;
                    return;
                } else {
                    while (idx < length) {
                        o = o[attrChains[idx]];
                        if (typeof o === 'undefined' || o === null) {
                            return;
                        }
                        idx++;
                    }
                    return o;
                }
            },
            Ajax: function (opts, customOpts) {
                var o = $.extend({type: "POST", dataType: "json", cache: false}, opts);
                var custom = $.extend({requestBody: false}, customOpts);
                var requestBody = gutils.Attr(custom, "requestBody") || false;
                if (requestBody) {
                    o.contentType = 'application/json;charset=UTF-8';
                    o.traditional = true;
                    if (typeof o.data !== 'string' && typeof JSON !== 'undefined') {
                        o.data = JSON.stringify(o.data);
                    }
                }

                if (typeof top.layer == "undefined") {
                    return $.ajax(o);
                }

                var layerIndex = top.layer.load(2);
                try {
                    return $.ajax(o).always(function () {
                        top.layer.close(layerIndex);
                    });
                } catch (e) {
                    top.layer.close(layerIndex);
                }
            },
            AjaxDone: function (jsonData, options) {
                var o = $.extend({status: 200, message: "", data: {}}, gutils.Json(jsonData));
                var opts = $.extend({error: null, success: null}, options);

                var status = gutils.Attr(o, "status");
                var message = gutils.Attr(o, "message");
                var data = gutils.Attr(o, "data");

                if (status != 200) {
                    if ($.isFunction(opts.error) && opts.error(jsonData) === false) {
                        return false;
                    }

                    gutils.$rootModel.$Message.error(message);

                } else {
                    if ($.isFunction(opts.success) && opts.success(data, jsonData)) {
                        return false;
                    }

                    gutils.$rootModel.$Message.success(message);
                }
            },
            VueSet: function (target, attrChain, value) {
                if (!target) return;
                if (!attrChain) return;
                if (typeof value === 'undefined') return;

                var attrChains = attrChain.split('.');
                var length = attrChains.length;
                var idx = 0;
                var o = target;

                while (idx < length - 1) {
                    if (o === null || typeof o[attrChains[idx]] === 'undefined') {
                        Vue.set(o, attrChains[idx], {});
                    }
                    o = o[attrChains[idx]];
                    idx++;
                }
                Vue.set(o, attrChains[idx], value);
            },
            Json: function (data) {
                try {
                    if ($.type(data) == 'string')
                        return eval('(' + data + ')');
                    else return data;
                } catch (e) {
                    return {};
                }
            },
            Load: function (opts) {
                var self = this;
                var o = $.extend({type: "GET", dataType: "html", cache: true}, opts);
                return self.Ajax(o);
            },
            baseUrl: function (path) {
                return (path || "").baseUrl();
            },
            mix: function (target, ex, jqExtend) {
                if (jqExtend) {
                    return $.extend(target, ex);
                } else {
                    if (!target) target = {};
                    $.each(ex, function (index, item) {
                        if (!target[index]) {
                            target[index] = item;
                        }
                    });
                    return target;
                }
            },
            String: {
                format: function () {
                    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
                        args[_key] = arguments[_key];
                    }

                    var i = 1;
                    var f = args[0];
                    var len = args.length;
                    if (typeof f === 'function') {
                        return f.apply(null, args.slice(1));
                    }
                    if (typeof f === 'string') {
                        var str = String(f).replace(formatRegExp, function (x) {
                            if (x === '%%') {
                                return '%';
                            }
                            if (i >= len) {
                                return x;
                            }
                            switch (x) {
                                case '%s':
                                    return String(args[i++]);
                                case '%d':
                                    return Number(args[i++]);
                                case '%j':
                                    try {
                                        return JSON.stringify(args[i++]);
                                    } catch (_) {
                                        return '[Circular]';
                                    }
                                    break;
                                default:
                                    return x;
                            }
                        });
                        for (var arg = args[i]; i < len; arg = args[++i]) {
                            str += ' ' + arg;
                        }
                        return str;
                    }
                    return f;
                }
            }
        });


    }

    function extendGutilsBs() {
        $.extend(gutils, {
            setHash: function (hashUrl) {
                gutils.router.previousHash = gutils.router.currentHash || "";
                gutils.router.currentHash = hashUrl;
                top.location.hash = "#" + hashUrl;
            },
            validator: getValidator(),
        });

        function getValidator() {
            var messages = {
                string: {
                    len: '必须为%s个字符',
                    min: '最少为%s个字符',
                    max: '最多为%s个字符',
                    range: '必须为%s到%s个字符',
                },
                number: {
                    len: '必须等于%s',
                    min: '最小值为%s',
                    max: '最大值为%s',
                    range: '最小值为%s，最大值为%s',
                },
                array: {
                    len: '%s must be exactly %s in length',
                    min: '%s cannot be less than %s in length',
                    max: '%s cannot be greater than %s in length',
                    range: '%s must be between %s and %s in length',
                }
            };
            var obj = {};
            obj.validFn = {
                email: function (value) {
                    if (isNoneValue(value)) return false;

                    if (/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)) {
                        return true;
                    }
                    return false;
                },
                mobile: function (value) {
                    if (isNoneValue(value)) return false;

                    if (/^1[3|4|5|8|7][0-9]\d{8}$/.test(value)) {
                        return true;
                    }
                    return false;
                },
                alphanumeric: function (value) {
                    if (isNoneValue(value)) return false;
                    if (/^\w+$/i.test(value)) {
                        return true;
                    }
                    return false;
                },
                alpha: function (value) {
                    if (isNoneValue(value)) return false;
                    if (/^[a-zA-Z_]+$/i.test(value)) {
                        return true;
                    }
                    return false;
                },
                alphanumeric_cn: function (value) {
                    if (isNoneValue(value)) return false;
                    if (!(/[^\u4e00-\u9fa5\w]/.test(value))) {
                        return true;
                    }
                    return false;
                },
                number: function (value) {
                    if (isNoneValue(value)) return false;
                    if (/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value)) {
                        return true;
                    }
                    return false;
                },
                integer: function (value) {
                    if (isNoneValue(value)) return false;
                    if (/^\d+$/.test(value)) {
                        return true;
                    }
                    return false;
                },
                double2: function (value) {
                    if (isNoneValue(value)) return false;
                    if (/^\d{1,}(\.\d{1,2})?$/.test(value)) {
                        return true;
                    }
                    return false;
                },
                link: function (value) {
                    if (isNoneValue(value)) return false;
                    if (value.indexOf("http://") === 0 ||
                        value.indexOf("https://") === 0 ||
                        value.indexOf("ftp://") === 0) {
                        return true;
                    }
                    return false;
                },
                fileLink: function (value) {
                    if (isNoneValue(value)) return false;
                    if (value.indexOf("http://") === 0 ||
                        value.indexOf("https://") === 0 ||
                        value.indexOf("ftp://") === 0 ||
                        value.indexOf("\\\\") === 0) {
                        return true;
                    }
                    return false;
                },
                temp: function (value) {
                    if (isNoneValue(value)) return false;
                    if (false) {
                        return true;
                    }
                    return false;
                }
            };

            obj.valid = {
                email: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.email(value)) {
                        callback();
                    }
                    callback(new Error('请输入正确的邮箱'));
                },
                mobile: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.mobile(value)) {
                        callback();
                    }
                    callback(new Error('请输入正确的手机号码'));
                },
                alphanumeric: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.alphanumeric(value)) {
                        callback();
                    }
                    callback(new Error('字母、数字、下划线'));
                },
                alpha: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.alpha(value)) {
                        callback();
                    }
                    callback(new Error('只能输入字母、下划线'));
                },
                alphanumeric_cn: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.alphanumeric_cn(value)) {
                        callback();
                    }
                    callback(new Error('中文、数字、字母、下划线'));
                },
                number: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.alphanumeric_cn(value)) {
                        callback();
                    }
                    callback(new Error('请输入合法的数字'));
                },
                integer: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.integer(value)) {
                        callback();
                    }
                    callback(new Error('只能输入整数'));
                },
                double2: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.double2(value)) {
                        callback();
                    }
                    callback(new Error('小数请保留两位'));
                },
                link: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.link(value)) {
                        callback();
                    }
                    callback(new Error('必须http://, https://, ftp://开头'));
                },
                fileLink: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        callback();
                    } else if (obj.validFn.fileLink(value)) {
                        callback();
                    }
                    callback(new Error('必须http://, https://, ftp://开头, \\\\开头'));
                },
                range: function (rule, value, callback, source, options) {
                    if (validOption(value)) {
                        return callback();
                    }

                    var errors = [];
                    var len = typeof rule.len === 'number';
                    var min = typeof rule.min === 'number';
                    var max = typeof rule.max === 'number';
                    var val = value;
                    var key = null;
                    var num = typeof value === 'number';
                    var str = typeof value === 'string';
                    var arr = Array.isArray(value);
                    if (num) {
                        key = 'number';
                    } else if (str) {
                        key = 'string';
                    } else if (arr) {
                        key = 'array';
                    }
                    // if the value is not of a supported type for range validation
                    // the validation rule rule should use the
                    // type property to also test for a particular type
                    if (!key) {
                        return false;
                    }
                    if (str || arr) {
                        val = value.length;
                    }
                    if (len) {
                        if (val !== rule.len) {
                            errors.push(gutils.String.format(messages[key].len, rule.len));
                        }
                    } else if (min && !max && val < rule.min) {
                        errors.push(gutils.String.format(messages[key].min, rule.min));
                    } else if (max && !min && val > rule.max) {
                        errors.push(gutils.String.format(messages[key].max, rule.max));
                    } else if (min && max && (val < rule.min || val > rule.max)) {
                        errors.push(gutils.String.format(messages[key].range, rule.min, rule.max));
                    }

                    callback(errors);
                }
            };

            function validOption(value) {
                return $.trim(value).length <= 0;
            }

            function isNoneValue(value) {
                if (typeof value === "undefined") return true;
                if (value === null) return true;

                return false;
            }

            return obj;
        }
    }


    function mix(target, ex, jqExtend) {
        if (jqExtend) {
            return $.extend(target, ex);
        } else {
            if (!target) target = {};
            $.each(ex, function (index, item) {
                if (!target[index]) {
                    target[index] = item;
                }
            });
            return target;
        }
    }


}(jQuery, Vue));