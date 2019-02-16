//获得输入字符长度
function getLength(str) {
    return str.replace(/[^\x00-\xff]/g, "xx").length;
}

//判断是否相同字符
function findStr(str, n) {
    var tmp = 0;
    for (var i = 0; i < str.length; i++) {
        if (str.charAt(i) === n)
            tmp++;
    }
    return tmp;
}


window.onload = function () {
    var aInput = document.getElementsByTagName('input');
    var oName = aInput[1];
    var pwd = aInput[2];
    var pwd2 = aInput[3];
    var email=aInput[4];
    var aP = document.getElementsByTagName('p');
    var name_msg = aP[0];
    var pwd_msg = aP[1];
    var pwd2_msg = aP[2];
    var email_msg = aP[3];
    var count = document.getElementById('count');


    var pwd_0 = document.getElementById('pwd_state');
    var pwd_1 = document.getElementById('state_1');
    var pwd_2 = document.getElementById('state_2');
    var pwd_3 = document.getElementById('state_3');

    var name_length = 0;


    //用户名输入验证
    //验证条件1：.数字、字母（不分大小写）、汉字、下划线
    //验证条件2： 4-28字符，推荐使用中文会员名

    oName.onfocus = function () {
        name_msg.style.display = "block";
        name_msg.innerHTML = '<span class="ati">限制2-28个字符</span>'
    };

    oName.onkeyup = function () {
        count.className = '';
        count.style.visibility = 'visible';
        name_length = getLength(this.value);
        count.innerHTML = "<span class='ati'>您当前输入了" + name_length + "个字符</span>";
        if (name_length === 0) {
            count.style.visibility = 'hidden';
            count.className = 'hidden';
        }
    };

    oName.onblur = function () {
        //含有非法字符
        var re = /[^\w\u4e00-\u9fa5]/g;
        if (re.test(this.value)) {
            name_msg.innerHTML = '<span class="err">您好，您的输入含有非法字符！</span>'
        }

        //不能为空
        else if (this.value === "") {
            name_msg.innerHTML = '<span class="err"> 您好，输入不能为空！</span>'
        }

        //长度超过25个字符
        else if (name_length > 28) {
            name_msg.innerHTML = '<span class="err"> 您好，您的输入长度超过了28个字符！</span>'
        }

        //长度少于5个字符
        else if (name_length < 2) {
            name_msg.innerHTML = '<span class="err"> 您好，您的输入长度少于了2个字符！</span>'
        }
        //验证通过
        else {
            name_msg.innerHTML = '<span class="ok">OK</span>'
        }
    };

    //密码输入验证
    pwd.onfocus = function () {
        pwd_msg.style.display = "block";
        pwd_msg.innerHTML = '<span class="ati" ">您需要输入6-16个字符! &nbsp;请使用字母与数字的组合密码形式</span>';
    };

    pwd.onkeyup = function () {
        //显示密码强度
        pwd_0.className = 'pwd_state';
        pwd_1.className = "state_light";
        pwd_1.style.display = "inline-block";


        //密码长度大于5字符为"中"
        if (this.value.length > 5) {
            pwd_2.className = "state_middle";
            pwd_2.style.display = "inline-block";
            pwd2.removeAttribute("disabled");
            pwd2_msg.style.display = "inline-block";
        }

        //密码长度大于10字符为"强"
        if (this.value.length > 10) {
            pwd_3.className = "state_strong";
            pwd_3.style.display = "inline-block";
        }


        //清空后原样
        if (this.value.length === 0) {
            pwd_0.className = 'hidden';
            pwd_1.className = "hidden";
            pwd_1.style.display = "none";

            pwd_2.className = "hidden";
            pwd_2.style.display = "none";
            pwd2.setAttribute("disabled", "disabled");
            pwd2_msg.style.display = "none";

            pwd_3.className = "hidden";
            pwd_3.style.display = "none";
        }
    };


    pwd.onblur = function () {
        var m = findStr(pwd.value, pwd.value[0]);
        var re_n = /[^\d]/g;
        var re_t = /[^a-zA-Z]/g;

        //不能为空
        if (this.value === "") {
            pwd_msg.innerHTML = '<span class="err">您好，输入不能为空!</span>';
        }

        //不能用相同字符
        else if (m === this.value.length) {
            pwd_msg.innerHTML = '<span class="err">您好，密码不能使用相同字符!</span> ';
        }

        //长度为6-16个字符
        else if (this.value.length < 6 || this.value.length > 16) {
            pwd_msg.innerHTML = '<span class="err">您好，密码长度应为6-16个字符!</span> ';
        }

        //不能全为数字
        else if (!re_n.test(this.value)) {
            pwd_msg.innerHTML = '<span class="err">您好，密码不能全为数字!</span> ';
        }

        //不能全为字母
        else if (!re_t.test(this.value)) {
            pwd_msg.innerHTML = '<span class="err">您好，密码不能全为字母!</span> ';
        }

        //验证通过
        else {
            pwd_msg.innerHTML = '<span class="err" style="display: block"> OK</span>';

            pwd_0.className = 'hidden';
            pwd_0.className = 'hidden';
            pwd_1.className = "hidden";
            pwd_1.style.display = "none";

            pwd_2.className = "hidden";
            pwd_2.style.display = "none";

            pwd_3.className = "hidden";
            pwd_3.style.display = "none";
        }
    };

    //确认密码输入验证
    pwd2.onblur = function () {
        if (this.value !== pwd.value) {
            pwd2_msg.innerHTML = '<span class="err">您好，两次输入的密码不一致!</span> ';
        } else {
            pwd2_msg.innerHTML = '<span class="err" style="display: block">OK</span> ';
        }
    };

    //邮箱验证
    email.onblur = function () {
        var re_e =/^[1-9a-zA-Z_]\w*@[a-zA-Z0-9]+(\.[a-zA-Z]{2,})+$/;
        if (this.value === "") {
            email_msg.innerHTML = '<span class="err">您好，输入不能为空!</span>';
        }
        else if(!re_e.test(this.value)) {
            email_msg.innerHTML = '<span class="err">您好，请输入正确的邮箱地址！</span>'
        }else {
            email_msg.innerHTML = '<span class="err" style="display: block"> OK</span>';
        }
    };
};