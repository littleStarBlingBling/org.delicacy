$(function () {
    var _pageSize; // 存储用于搜索


    // 根据用户名、页面索引、页面大小获取文章列表
    function listAllArticle(pageIndex, pageSize) {
        $.ajax({
            url: "/manager",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("获取文章列表失败!");
            }
        });
    }
    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        listAllArticle(pageIndex, pageSize);
        _pageSize = pageSize;
    });



});