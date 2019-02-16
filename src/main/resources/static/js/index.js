

// DOM 加载完再执行
$(function() {
    var _pageSize; // 存储用于搜索
    // 菜单事件
    $(".article-menu .list-group-item").click(function() {

        var url = $(this).attr("url");

        // 先移除其他的点击样式，再添加当前的点击样式
        $(".article-menu .list-group-item").removeClass("active");
        $(this).addClass("active");

        // 加载其他模块的页面到右侧工作区
        $.ajax({
            url: url,
            success: function(data){
                $("#rightContainer").html(data);
            },
            error : function() {
                alert("error");
            }
        });
    });


    // 选中菜单第一项
    $(".article-menu .list-group-item:first").trigger("click");


    // 根据用户名、页面索引、页面大小获取用户列表
    function getArticleBytitle(pageIndex, pageSize) {
        $.ajax({
            url: "/admin/manager",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "username": $("#searchUsername").val()
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("获取用户列表失败!");
            }
        });
    }
});