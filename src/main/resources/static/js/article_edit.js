
$(function() {

    // 初始化 md 编辑器
    $("#md").markdown({
        language: 'zh',
        fullscreen: {
            enable: true
        },
        resize:'vertical',
        localStorage:'md'
    });

    $('.form-control-chosen').chosen();

    $("#uploadImage").click(function() {
        $.ajax({
            url: fileServerUrl,
            type: 'POST',
            cache: false,
            data: new FormData($('#uploadformid')[0]),
            processData: false,
            contentType: false,
            success: function(data){
                var mdcontent=$("#md").val();
                $("#md").val(mdcontent + "\n![]("+data +") \n");

            }
        }).done(function(res) {
            $('#file').val('');
        }).fail(function(res) {});
    })


    //发布文章
    $("#submitArticle").click(function () {
        // 获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/u/' + $(this).attr("username") + '/articles/edit',
            type: 'POST',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                "id": $('#articleId').val(),
                "title": $("#title").val(),
                "canteen": $('input:radio:checked').val(),
                "address": $('#address').val(),
                "price": $('#price').val(),
                "food": $('#food').val(),
                "summary": $('#summary').val(),
                "content": $('#md').val(),
                "catalog": {'id': $('#catalogSelect').val()},
                "tags": $('.form-control-tag').val()
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    window.location = data.body;
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("发布文章失败！");
            }
        })
    });

    // 初始化标签
    $('#tags').tagsInput({
        'defaultText':'输入标签'
    });
});