var layout = {}

layout.selectMenu = function () {
    var li = $('#' + selected_menu_id).addClass('active');
}

$(function () {
    layout.selectMenu();
})