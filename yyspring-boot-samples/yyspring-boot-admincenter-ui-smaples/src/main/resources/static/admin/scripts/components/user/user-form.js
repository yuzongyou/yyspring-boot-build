(function ($, Vue) {
    'use strict';

    Vue.component('entityForm', vueComponent);

    function vueComponent(resolve) {

        gutils.Load({
            url: gutils.baseUrl("/views/user/form.html")
        }).done(function (hData) {
            resolve(vueOpts(hData));
        });

        function vueOpts(htmlTpl) {
            var options = {
                props: {
                    entityForm: {
                        type: Object, required: true, twoWay: true,
                        default: function () {
                            return {
                                showModal: false,
                                entity: {
                                    email:""
                                }
                            }
                        }
                    }
                },
                template: htmlTpl,
                data: function () {
                    return {}
                },
                methods: {
                    submit: submit
                },
                watch: {
                    "entityForm.showModal": function (val, oldVal) {
                        this.$refs["entityFormValidate"].resetFields();
                        this.$emit('on-change-show-modal', val);
                    }
                }
            };
            return options;

            function submit() {
                var vModel = this;
                vModel.$refs["entityFormValidate"].validate(function (valid) {
                    if (!valid) {
                        return false;
                    }

                    vModel.$Modal.confirm({
                        title: '操作确认',
                        content: '确定提交吗？',
                        onOk: function () {

                            vModel.entityForm.showModal = false;
                            vModel.$emit('on-submit-success');

                            // doSubmit(vModel);
                        }
                    });
                });
            }

            function doSubmit(vModel) {
                var param = $.extend({}, vModel.entity);
                var url = param.id ? "/entity/update" : "/entity/create";
                return gutils.Ajax(
                    {url: url, data: param}
                ).done(function () {
                    vModel.entityForm.showModal = false;
                    vModel.$emit('on-submit-success');
                });
            }
        }
    }


}(jQuery, Vue));