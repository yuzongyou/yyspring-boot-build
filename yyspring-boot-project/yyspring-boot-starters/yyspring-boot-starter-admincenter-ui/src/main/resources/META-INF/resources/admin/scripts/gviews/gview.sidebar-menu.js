(function ($, Vue) {
    'use strict';
    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    Vue.component('gviewSidebarMenu', vueComponent);

    function vueComponent(resolve) {

        gutils.Load({
            url: gutils.baseUrl("/tpls/gviews/gview.sidebar-menu.tpl.html")
        }).done(function (hData) {
            resolve(vueOpts(hData));
        });

        function vueOpts(htmlTpl) {
            var options = {
                props: {
                    entitys: {type: Array, required: true, default: []},
                    isFirst: {type: Boolean, required: false, default: true}
                },
                template: htmlTpl,
                data: function () {
                    return {
                        currentUrl: ""
                    }
                },
                methods: {
                    clickMenu: clickMenu,
                    clickMenuCycle: clickMenuCycle
                },
                created: function () {
                    var vModel = this;
                    this.currentUrl = vModel.$gutils.router.currentHash;
                    $.each(this.entitys, function (index, item) {
                        activeTreeViewClass(vModel, item, null);
                    });
                }
            };
            return options;

            /**
             * 激活当前模块Item样式；
             * @param vModel
             * @param item
             */
            function activeTreeViewClass(vModel, item, parent) {
                gutils.VueSet(item, "treeViewClass", {
                    'active': vModel.currentUrl == item.privilegeUrl,
                    'treeview': item.children && item.children.length
                });

                if (item.children && item.children.length) {
                    $.each(item.children, function (index, sItem) {
                        activeTreeViewClass(vModel, sItem, item);
                    });
                }

                if(parent && item.treeViewClass.active){
                    gutils.VueSet(parent, "treeViewClass.active", true);
                    gutils.VueSet(parent, "treeViewClass.menu-open", true);
                }
            }

            function clickMenu(item) {
                var vModel = this;

                if(item.privilegeUrl){
                    vModel.currentUrl = item.privilegeUrl;
                    $.each(this.entitys, function (index, aItem) {
                        activeTreeViewClass(vModel, aItem, null);
                    });


                    vModel.$nextTick(function () {
                        $(vModel.$refs["sidebar-menu"]).find("ul.treeview-menu").each(function (index, aItem) {
                            if($(aItem).parent().hasClass("active")) return ;
                            $(aItem).hide();
                        });
                    });

                }

                this.$emit('on-click-menu', item);

            }

            function clickMenuCycle(item) {
                this.clickMenu(item);
            }
        }
    }


}(jQuery, Vue));