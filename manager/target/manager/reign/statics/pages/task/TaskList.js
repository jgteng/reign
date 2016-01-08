/**
 * Created by ji on 15-10-29.
 */
taskListApp.controller('taskListCtrl', function ($scope) {

    $scope.queryTaskList = function () {
        $scope.grid_selector = $('#grid-table');
        $scope.pager_selector = $('#grid-pager');
        $scope.grid_selector.jqGrid({
            url: '/task/listTask',
            datatype: 'json',
            height: 'auto',
            colNames: ['ID', '任务名称', '执行脚本', '所属节点', '节点类型', '创建时间', '状态', '上次运行时间', '下次运行时间', '运行节点', '任务描述', '节点ID', '运行节点ID'],
            colModel: [
                {name: 'id', index: 'id', width: 50, fixed: false},
                {
                    name: 'taskName',
                    index: 'taskName',
                    width: 200,
                    fixed: false,
                    formatter: function (p1, p2, record) {
                        return '<a href="/task/info?id=' + record.id + '" target="_blank">' + p1 + '</a>'
                    }
                },
                {name: 'mainScript', index: 'main_script', width: 150, fixed: false},
                {name: 'nodeName', index: 'node_id', width: 250, fixed: false},
                {
                    name: 'nodeType', index: 'node_type', width: '100', fixed: false,
                    formatter: function (param) {
                        if (!param)
                            return ''
                        else if (param == 'physical')
                            return "物理节点"
                        else if (param == 'virtual')
                            return "节点组"
                    }
                },
                {name: 'created', index: 'created', width: 150, fixed: false},
                {
                    name: 'status', index: 'status', width: 80, fixed: false,
                    formatter: function (param1) {
                        if (!param1)
                            return ''
                        else if (param1 == 0)
                            return '正常'
                        else if (param1 == 1)
                            return '停用'
                        else
                            return param1
                    }
                },
                {name: 'lastRunTime', index: 'last_run_time', width: 150, fixed: false},
                {name: 'nextTime', index: 'next_time', width: 150, fixed: false},
                {name: 'runNodeName', index: 'run_node_id', width: 250, fixed: false},
                {name: 'description', index: 'description'},
                {name: 'nodeId', index: 'node_id', width: 50, hidden: true, fixed: false},
                {name: 'runNodeId', index: 'run_node_id', width: 50, hidden: true, fixed: false}
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
        window.open("/task/to_add")
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
                                url: "/task/del",
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

})