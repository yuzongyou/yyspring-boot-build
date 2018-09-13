(function ($, Vue) {
    'use strict';

    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    if (window == top) {
        gutils.mix(gwebapp, {
            start: start,
            buildSidebarMenus: buildSidebarMenus,
            hasPrivilege: hasPrivilege
        });
    }

    function start(opt) {
        var deferred = $.Deferred();
        var o = $.extend({requiresAuth: true, indexForced: true}, opt);

        if(o.indexForced){
            if (top.location.pathname != "/" && top.location.pathname != "/index.html" && top.location.pathname != "/index.html".baseUrl()) {
                // 修正浏览器地址中，当前请求页面不是 /index.html;
                top.location.href = "/index.html".baseUrl() + location.hash;
            }
        }

        if (o.requiresAuth) {
            getPrivileges(deferred);
            deferred.done(function () {
                var passport = gutils.Attr(gwebapp, "session.user.passport") || "";
                var username = $.cookie("username");
                if (!passport) {
                    gutils.$rootModel.redirectHash("/views/login.html");
                    deferred.reject();
                } else if (username && username != passport) {
                    deferred.reject();
                    gutils.$rootModel.reload();
                }
            });
        } else {
            deferred.resolve();
        }
        return deferred.promise();

    }

    function hasPrivilege(privilegeId) {
        var privilegeKeyMap = gutils.Attr(gwebapp, "privilegeKeyMap") || {};
        return !!privilegeKeyMap[privilegeId]
    }

    function buildSidebarMenus() {
        var privileges = gutils.Attr(gwebapp, "session.privileges") || [];

        var privilegeMenus = [];
        var privilegeMenuMap = {};
        $.each(privileges, function (index, item) {
            if (!item.showMenu) {
                return;
            }
            let copyItem = $.extend({}, item);
            privilegeMenus.push(copyItem);
            privilegeMenuMap[copyItem.privilegeId] = copyItem;
        });

        var rootPrivileges = [];
        $.each(privilegeMenus, function (index, item) {
            let parentPrivilege = privilegeMenuMap[item.parentPrivilegeId];
            if (!parentPrivilege) {
                rootPrivileges.push(item);
                return;
            }

            if (!parentPrivilege.children) parentPrivilege.children = [];

            parentPrivilege.children.push(item);
        });

        gutils.Attr(gwebapp, "privilegeMenuTree", rootPrivileges);
        return rootPrivileges;
    }

    function buildPrivilegeKeyMap() {
        var privileges = gutils.Attr(gwebapp, "session.privileges") || [];
        var privilegeKeyMap = {};
        $.each(privileges, function (index, item) {
            privilegeKeyMap[item.privilegeId] = item;
        });

        gutils.Attr(gwebapp, "privilegeKeyMap", privilegeKeyMap);

    }

    function getPrivileges(deferred) {
        var privileges = gutils.Attr(gwebapp, "session.privileges") || [];
        var sessionUrl = gutils.Attr(gconfig, "sessionUrl") || "";
        if (privileges.length) {
            deferred.resolve();
            return;
        }


        // =============== 本地测试使用, 需删除 ==========//
        if(!sessionUrl){
            gutils.Ajax({
                type: "get",
                url: gutils.baseUrl("/scripts/gviews/gview.privilege-test.json")
            }).done(function (jsonData) {
                var status = gutils.Attr(jsonData, "status") || -599;
                var privileges = gutils.Attr(jsonData, "data.privileges") || [];
                var user = gutils.Attr(jsonData, "data.user") || {};
                var product = gutils.Attr(jsonData, "data.product") || {};

                user.passport = user.passport || $.cookie("username");

                if (!user.passport) {
                    gutils.$rootModel.redirectHash("/views/login.html");
                    deferred.reject();
                    return;
                }

                gutils.Attr(gwebapp, "session.user", user);
                gutils.Attr(gwebapp, "session.privileges", privileges);
                buildPrivilegeKeyMap();
                gutils.Attr(gwebapp, "session.product", product);
                deferred.resolve();
            });
            return;
        }
        // =============== 本地测试使用, 需删除 ==========//


        gutils.Ajax({
            type: "get",
            url: sessionUrl,
            dataType: "jsonp"
        }).done(function (jsonData) {
            var status = gutils.Attr(jsonData, "status") || -599;
            var privileges = gutils.Attr(jsonData, "data.privileges") || [];
            var user = gutils.Attr(jsonData, "data.user") || {};
            var product = gutils.Attr(jsonData, "data.product") || {};
            if (status == 400404 || $.isEmptyObject(user) || !user.passport) {
                gutils.$rootModel.redirectHash("/views/login.html");
                deferred.reject();
                return;
            }
            gutils.Attr(gwebapp, "session.user", user);
            if (!privileges.length) {
                gutils.$rootModel.redirectHash("/views/403.html");
                deferred.reject();
                return;
            }
            gutils.Attr(gwebapp, "session.privileges", privileges);
            buildPrivilegeKeyMap();

            gutils.Attr(gwebapp, "session.product", product);
            deferred.resolve();
        });
    }


}(jQuery, Vue));