$(function () {

    //删除文章事件
    $(".article-content-container").on("click", ".article-delete", function () {
        //获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: articleUrl,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    window.location = data.body;
                } else {
                    toastr.error(data.message)
                }
            },
            error: function () {
                toastr.error("删除文章失败!");
            }
        });
    });

    //获取评论列表
    function getComment(articleId) {
        //获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'GET',
            data: {"articleId": articleId},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("获取评论列表失败!");
            }
        });
    }

    //提交评论
    $(".article-content-container").on("click", "#submitComment", function () {
        //获取CSRf Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'POST',
            data: {"articleId": articleId, "commentContent": $('#commentContent').val()},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    getComment(articleId);
                    $("#commentContent").val('');
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("提交评论失败!");
            }
        });
    });

    //删除评论
    $(".article-content-container").on("click", ".article-delete-comment", function () {
        //获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/comments/' + $(this).attr("commentId") + '?articleId=' + articleId,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    getComment(articleId);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("删除评论失败!")
            }
        });
    });

    //提交点赞
    $(".article-content-container").on("click", "#submitPraise", function () {
        //获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/praises',
            type: 'POST',
            data: {"articleId": articleId},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    window.location = articleUrl;
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("提交点赞失败！")
            }
        });
    });

    //删除点赞
    $(".article-content-container").on("click", "#cancelPraise", function () {
        //获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/praises/' + $(this).attr('praiseId') + '?articleId=' + articleId,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    window.location = articleUrl;
                } else {
                    toastr.error(data.message);
                }
            }, error: function () {
                toastr.error("删除点赞失败!");
            }
        });
    });

    //初始化 文章评论
    getComment(articleId);

});