/**
 * Created by ji on 15-10-19.
 */
taskAddApp.controller("addTaskCtrl", function ($scope, $http) {
    $scope.taskName = "abc";
    $scope.save = function () {
        //ajaxSubmit 会将taskForm中的数据提交到后台(整个form提交,如果不是整个form提交,可以用$.ajax提交)
        $('#taskForm').ajaxSubmit({
            url: '/reign/task/add',
            type: 'post',
            dataType: 'json',
            success: function (json, statusText, xhr, $form) {
                if (json.code == 0) {
                    var href = "/reign/task/info?id="+json.obj.id
                    window.open(href,'_self')
                }
            }
        });

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
});
