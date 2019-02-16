$(function () {
    var avatarApi;

    //获取编辑用户头像的界面
    $(".article-content-container").on("click", ".article-edit-avatar", function () {
        avatarApi = "/u/" + $(this).attr("username") + "/avatar";
        $.ajax({
            url: avatarApi,
            success: function (data) {
                $("#avatarFormContainer").html(data);
            },
            error: function () {
                toastr.error("获取编辑用户头像失败！")
            }
        });
    });

    //用URL方式表示base64图片数据, 并将URL转换为Blob
    function convertBaseUrlToBlob(urlData) {
        var bytes = window.atob(urlData.split(',')[1]); //去掉url的头，并转换为byte
        //处理异常，将ascii码小于0的转换为大于0
        var ab = new ArrayBuffer(bytes.length);
        var ia = new Uint8Array(ab);
        for (var i = 0; i < bytes.length; i++) {
            ia[i] = bytes.charCodeAt(i);
        }
        return new Blob([ab], {type: 'image/png'});
    }

    //提交用户头像的图片数据
    $("#submitEditAvatar").on("click", function () {
        var form = $('#avatarformid')[0];
        var formData = new FormData(form);//提交form中的参数
        var base64Codes = $(".cropImg > Img").attr("src");
        formData.append("file", convertBaseUrlToBlob(base64Codes));//append函数的第一个参数是后台获取数据的参数名,和html标签的input的name属性功能相同

        $.ajax({
            url: fileServerUrl,
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                var avatarUrl = data;

                //获取CSRF Token
                var csrfToken = $("meta[name='_csrf']").attr("content");
                var csrfHeader = $("meta[name='_csrf_header']").attr("content");

                //保存头像王更改到数据库
                $.ajax({
                    url:avatarApi,
                    type:'POST',
                    contentType:"application/json;charset=utf-8",
                    data:JSON.stringify({"id":Number($("#userId").val()),"avatar":avatarUrl}),
                    beforeSend:function (request) {
                        request.setRequestHeader(csrfHeader,csrfToken);
                    },
                    success:function (data) {
                        if(data.message){
                            //成功后置换头像图片
                            $(".article-avatar").attr("src",data.avatarUrl);
                        }else{
                            toastr.error("error!"+data.message);
                        }
                    },
                    error:function () {
                        toastr.error("保存头像失败！")
                    }
                });
            },
            error:function () {
                toastr.error("提交失败！")
            }
        })
    });
});