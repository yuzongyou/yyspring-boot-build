(function ($, Vue) {
    'use strict';

    var gconfig = top.gconfig;
    var gutils = top.gutils;
    var gwebapp = top.gwebapp;

    // 权限
    Vue.directive('gview-privilege', gviewPrivilege());
    // 表格排序
    Vue.directive('gview-table-order', gviewTableOrder());


    function gviewPrivilege() {
        var options = {
            inserted: function (el, binding, vnode, oldVnode, isDestroy) {
                var hasPrivilege = false;
                if (typeof binding.value === 'boolean') {
                    hasPrivilege = binding.value;
                } else {
                    hasPrivilege = gwebapp.hasPrivilege(binding.value);
                }
                if (!hasPrivilege) {
                    $(el).hide();
                } else {
                    $(el).show();
                }
            }
        };
        return options;
    }

    function gviewTableOrder() {
        var options = {
            inserted: function (el, binding, vnode, oldVnode, isDestroy) {
                var $orderTh = $(el).find('th[data-order-field]');
                if (!$orderTh.length) return;
                $(el).addClass("gview-table-order");
                $orderTh.addClass("order-field");
                $orderTh.each(function (index, item) {
                    $(item).click(function () {
                        var $this = $(this);
                        var className = $this.hasClass("asc") ? "desc" : "asc";
                        $orderTh.addBack().removeClass("asc desc");
                        $this.addClass(className);
                        if ($.isFunction(binding.value.changeOrderField)) {
                            binding.value.changeOrderField($this.data('orderField'), className);
                        }
                    })
                });
            }
        };
        return options;
    }

}(jQuery, Vue));