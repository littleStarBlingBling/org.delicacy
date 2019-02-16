
$(function() {

    var _pageSize;

    // 根据用户名、页面索引、页面大小获取文章列表
    function getArticlesByName(pageIndex, pageSize) {
        $.ajax({
            url: "/search",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#indexkeyword").val()
            },
            success: function(data){
                $("#mainContainer").html(data);

                var keyword = $("#indexkeyword").val();

            },
            error : function() {
                toastr.error("获取文章列表失败!");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getArticlesByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 关键字搜索
    $("#indexsearch").click(function() {
        getArticlesByName(0, _pageSize);
    });

        // 清空搜索框内容
        $("#indexkeyword").val('');


});