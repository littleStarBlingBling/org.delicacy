// 管理页用户的增删查改

$(function () {

    var _pageSize; // 存储用于搜索


    // 根据用户名、页面索引、页面大小获取用户列表
    function getUserByUsername(pageIndex, pageSize) {
        $.ajax({
            url: "/admin/user",
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

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getUserByUsername(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchUsernameBtn").click(function () {
        getUserByUsername(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addUser").click(function () {
        $.ajax({
            url: "/admin/user/add",
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function (data) {
                toastr.error("添加用户失败!");
            }
        });
    });

    // 获取编辑用户的界面
    $("#rightContainer").on("click", ".article-edit-user", function () {
        $.ajax({
            url: "/admin/user/edit/" + $(this).attr("userId"),
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function () {
                toastr.error("编辑用户失败!");
            }
        });
    });

    // 提交变更后，清空表单
    $("#submitEdit").click(function () {
        $.ajax({
            url: "/admin/user",
            type: 'POST',
            data: $('#userForm').serialize(),
            success: function (data) {
                $('#userForm')[0].reset();

                if (data.success) {
                    // 重刷新主界面
                    getUserByUsername(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }

            },
            error: function () {
                toastr.error("提交失败!");
            }
        });
    });

    // 删除用户
    $("#rightContainer").on("click", ".article-delete-user", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/admin/user/" + $(this).attr("userId"),
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 刷新主界面
                    getUserByUsername(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("删除失败!");
            }
        });
    });
});