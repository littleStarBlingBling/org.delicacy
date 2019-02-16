//分页处理
(function ($) {
    $.tbpage = function (selector, handler) {
        $(selector).off("click", ".tbpage-item").on("click", ".tbpage-item", function () {
            var pageIndex = $(this).attr("pageIndex");
            var pageSize = $('.tbpage-size option:selected').val();

            //判断所选元素是否为当前页面，若不是就需要处理
            if ($(this).parent().attr("class").indexOf("active") > 0) {
                console.log("当前页面");
            } else {
                handler(pageIndex, pageSize);
            }
        });

        $(selector).off("change", ".tbpage-size").on("change", ".tbpage-size", function () {
            var pageIndex = $(this).attr("pageIndex");
            var pageSize = $('.tbpage-size option:selected').val();
            handler(pageIndex, pageSize);
        });
    };
})(jQuery);
