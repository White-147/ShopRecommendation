function burypint(obj, event) {
    var data = {};
    if (document) {
        //域名获取
        data.domain = document.domain || '';
        //url
        data.url = document.URL || '';
        //入链
        data.referrer = document.referrer || '';
        //页面标题
        data.title = document.title || '';
    }
    if (window && window.screen) {
        // 屏幕高
        data.sh = window.screen.height || 0;
        //屏幕宽
        data.sw = window.screen.width || 0;
        //颜色深度
        data.cd = window.screen.colorDepth || 0;
    }
    if (navigator) {
        //语言
        data.lang = navigator.language || '';
    }
    if (event) {
        //距离浏览器上边界
        data.clientX = event.clientX || 0;
        //距离浏览器左边界
        data.clientY = event.clientY || 0;
    }
    //事件源标签内容
    data.avalue = $(obj).text().trim().replace("\r", "") || '';
    //事件
    data.type0 = event.type;

    //下一个页面
    data.type2 = $(obj).attr("href") || '';
    var args = "";
    $.each(data, function (i, e) {
        args += i + "=" + encodeURIComponent(e) + "&";
    });
    args = args.substr(0, args.length - 1);
    var img = new Image(1, 1);
    img.src = "/log?" + args;
}

$(function () {
    $('a').click(function (event) {
        burypint($(this), event)
    })


})




