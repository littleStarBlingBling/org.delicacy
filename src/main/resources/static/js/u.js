"use strict";

$(function () {
    var _pageSize;
    var catalogId;

    //根据用户名、页面索引、页面大小获取文章列表
    function getArticlesByUsername(pageIndex, pageSize) {
        $.ajax({
            url: "/u/" + username + "/articles",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "keyword": $("#keyword").val()
            },
            success: function (data) {
                $("#mainContainer").html(data);

                // 如果是分类查询，则取消最新、最热选中样式
                if (catalogId) {
                    $(".nav-item .nav-link").removeClass("active");
                }
            },
            error: function () {
                toastr.error("获取文章列表失败!");
            }
        });
    }

    //分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getArticlesByUsername(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    //关键词搜索
    $("#searchArticles").click(function () {
        getArticlesByUsername(0, _pageSize);
    });

    //最热与最新的切换
    $(".nav-item .nav-link").click(function () {

        var url = $(this).attr("url");

        //先移除其他的点击样式，再添加当前的点击样式
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");

        //加载其他模块的页面到右侧区域
        $.ajax({
            url: url + '&async=true',
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("加载其他模块失败!")
            }
        });

        //清空搜索框内容
        $("#keyword").val('');
    });

    // 获取分类列表
    function getCatalogs(username) {

        $.ajax({
            url: '/catalogs',
            type: 'GET',
            data: {"username":username},
            success: function (data) {
                $("#catalogMain").html(data);
            },
            error: function () {
                toastr.error("获取分类列表失败!");
            }
        });
    }


    //获取新增分类的页面
    $(".article-content-container").on("click",".article-add-catalog", function () {
        console.log("获取分类列表");
        $.ajax({
            url: '/catalogs/edit',
            type: 'GET',
            success: function(data){
                $("#catalogFormContainer").html(data);
            },
            error : function() {
                toastr.error("获取新增分类页面失败!");
            }
        });
    });

    //获取编辑单个分类的页面
    $(".article-content-container").on("click", ".article-edit-catalog", function () {
        $.ajax({
            url: '/catalogs/edit/' + $(this).attr('catalogId'),
            type: 'GET',
            success: function (data) {
                $("#catalogFormContainer").html(data);
            },
            error: function () {
                toastr.error("获取编辑分类的页面失败!");
            }
        });
    });

    //提交分类
    $("#submitEditCatalog").click(function () {
        //获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/catalogs',
            type: 'POST',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                "username": username,
                "catalog": {"id": $('#catalogId').val(), "name": $('#catalogName').val()}
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("提交分类失败！");
            }
        });
    });

    //删除分类
    $(".article-content-container").on("click", ".article-delete-catalog", function () {
        //获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/catalogs/' + $(this).attr('catalogId') + '?username=' + username,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("删除分类失败！");
            }
        });
    });

    //根据分类查询
    $(".article-content-container").on("click", ".article-query-by-catalog", function () {
        catalogId = $(this).attr('catalogId');
        getArticlesByUsername(0, _pageSize)
    });

    getCatalogs(username);

});