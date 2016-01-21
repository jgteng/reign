/**
 * Created by ji on 15-10-29.
 */
scriptListApp.controller('scriptListCtrl', function ($scope) {
    $scope.queryTaskList = function () {
        $scope.grid_selector = $('#grid-table');
        $scope.pager_selector = $('#grid-pager');
        $scope.grid_selector.jqGrid({
            url: '/reign/script/listScript',
            datatype: 'json',
            height: 'auto',
            colNames: ['ID', '脚本名称', '脚本类型', '脚本路径', '脚本版本', '脚本描述', '创建时间', '修改时间'],
            colModel: [
                {name: 'id', index: 'id', width: 50, fixed: false},
                {name: 'scptName', index: 'scpt_name', width: 200, fixed: false},
                {name: 'scptType', index: 'scpt_type', width: 150, fixed: false},
                {name: 'scptPath', index: 'scpt_path', width: 200, fixed: false},
                {name: 'scptVersion', index: 'scpt_version', width: '100', fixed: false},
                {name: 'description', index: 'description', width: '200', fixed: false},
                {name: 'created', index: 'created', width: 150, fixed: false},
                {name: 'modified', index: 'modified', width: 150, fixed: false},

            ],
            viewrecords: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: $scope.pager_selector,
            altRows: false,
            autowidth: true,
            loadComplete: function () {
                var table = this;
                updatePagerIcons(table);
            },
            onSelectRow: function (id) {
                var rowData = $scope.grid_selector.jqGrid("getRowData", id);//根据上面的id获得本行的所有数据
                var status = rowData.status;
                var disabled = rowData.disabled;

                if (disabled == 1) {
                    $('btn_eidt').addClass("disabled");
                }

            }
        }).jqGrid('navGrid', '#grid-pager', {
            refresh: true,
            edit: false,
            del: false,
            add: false,
            search: false,
            refreshicon: 'icon-refresh green',
        });
    }
    $scope.queryTaskList();


    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
            'ui-icon-seek-prev': 'icon-angle-left bigger-140',
            'ui-icon-seek-next': 'icon-angle-right bigger-140',
            'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    $scope.add = function () {
        window.open("/reign/task/to_add")
    }

    $scope.edit = function () {
        var id = $scope.grid_selector.jqGrid('getGridParam', 'selrow');//根据点击行获得点击行的id（id为jsonReader: {id: "id" },）
        var rowData = $scope.grid_selector.jqGrid("getRowData", id);//根据上面的id获得本行的所有数据
        var val = rowData.id; //获得制定列的值 （auditStatus 为colModel的name）
        window.open("/reign/task/to_edit?id=" + val);
    };

    $scope.disable = function () {
        var id = $scope.grid_selector.jqGrid('getGridParam', 'selrow');//根据点击行获得点击行的id（id为jsonReader: {id: "id" },）
        var rowData = $scope.grid_selector.jqGrid("getRowData", id);//根据上面的id获得本行的所有数据
        var val = rowData.id; //获得制定列的值 （auditStatus 为colModel的name）
        alert(rowData.taskName);

        //$.ajax({
        //    url: "/task/add",
        //    type: "POST",
        //    dataType: "JSON",
        //    data: {"taskName":$scope.taskName},
        //    success:function(data){
        //        alert("success")
        //    }
        //});
    }

    $scope.del = function () {
        var id = $scope.grid_selector.jqGrid('getGridParam', 'selrow');//根据点击行获得点击行的id（id为jsonReader: {id: "id" },）
        var rowData = $scope.grid_selector.jqGrid("getRowData", id);//根据上面的id获得本行的所有数据
        var val = rowData.id; //获得制定列的值 （auditStatus 为colModel的name）
        if (val) {
            $("#dialog-confirm").removeClass('hide').dialog({
                resizable: false,
                modal: true,
                buttons: [
                    {
                        html: "<i class='icon-trash bigger-110'></i>&nbsp; 删除",
                        "class": "btn btn-danger btn-xs",
                        click: function () {
                            $(this).dialog("close");
                            $.ajax({
                                url: "/reign/task/del",
                                type: "POST",
                                dataType: "JSON",
                                data: {"id": val},
                                success: function (data) {
                                    alert("success")
                                    $scope.grid_selector.trigger("reloadGrid");
                                }
                            });
                        }
                    }
                    ,
                    {
                        html: "<i class='icon-remove bigger-110'></i>&nbsp; 取消",
                        "class": "btn btn-xs",
                        click: function () {
                            $(this).dialog("close");
                        }
                    }
                ]
            });

        }
        else
            alert("请先选中要删除的任务")
    }

});
