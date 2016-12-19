<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head>
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css" type="text/css" media="all">
<link rel="stylesheet" type="text/css" href="<%=basePath%>js/flavr/flavr/css/flavr.css" />
<link rel="stylesheet" id="main-css" href="<%=basePath%>xiu/style.css" type="text/css" media="all">
<link href="<%=basePath%>js/ueditor1_4_3/third-party/SyntaxHighlighter/shCoreDefault.min.css" rel="stylesheet" type="text/css" />
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=11,IE=10,IE=9,IE=8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="baidu-site-verification" content="emEdenaBVA">
<meta http-equiv="Cache-Control" content="no-siteapp">
<title>${article.title} - 乱七八糟</title>
<meta name="description" content="coriger 夏末冬初  ${fn:substring(article.description, 0,60)}" />
<meta name="keywords" content="
<c:forEach items="${article.tagList}" var="tag">${tag.tagName} </c:forEach>${article.title} coriger 夏末冬初  "/>
<style type="text/css">
	img.wp-smiley,
	img.emoji {
		display: inline !important;
		border: none !important;
		box-shadow: none !important;
		height: 1em !important;
		width: 1em !important;
		margin: 0 .07em !important;
		vertical-align: -0.1em !important;
		background: none !important;
		padding: 0 !important;
	}

    .ab-box{ padding: 10px ; color: #333; position: absolute; margin-left:40px; width: 350px;display: none; border-radius: 5px;}
    .ab-box .ab-box-ico{position: absolute; left: -25px;}
    .ab-box textarea{     width: 330px;
        height: 300px;
        overflow: auto;
        border: 2px solid rgb(49, 112, 143);
        margin-bottom: 10px;
        border-radius: 15px;
        color: #31708f;}
    .btn-cancel,.btn-ok{ display: inline-block; width: 48%; text-align: center;text-decoration: none; color: #ff5e52; float: left;line-height: 30px;}
    .btn-cancel{background-color: #e3e3e3;}
    .btn-ok{background-color: #ff5e52; color: #FFF; margin-left: 100px;}

</style>


</head>

<body id="contain" class="home blog ui-c3">
<section class="container">
<form method="get" class="degfy_search_box">
        <input class="degfy_search_input" name="s" type="text" placeholder="输入关键字" value="">
</form>
<header class="header">
    <div class="logo_right"><span class="glyphicon glyphicon-search degfy_search"></span></div>
    <div class="logo_left"></div>
	<h1 class="logo"><a href="<%=basePath%>">乱七八糟</a></h1>
	<ul class="nav">

<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-home menu-item-61">
	<a href="<%=basePath%>">
		<span class="glyphicon glyphicon-home"></span>首页
	</a>
</li>
<c:forEach items="${categoryList}" var="category">
	<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-61  
		<c:if test="${article.categoryId == category.id}">
			current_page_item 
		</c:if>
	">
	<a href="<%=basePath%>category/${category.aliasName}">
		<span class="glyphicon glyphicon-${category.iconClass}"></span>${category.categoryName}
	</a></li>
</c:forEach>

</ul>		<div class="widget_head">
			</div>
</header>
<div class="content-wrap">
    <input type="hidden" id="articleId" value="${article.id}"/>
	<div class="content" style="margin-right: 0px;">
				<header class="article-header" style="text-align: center;">
			<h1 class="article-title" style="margin-top: -10px;"><a href="<%=basePath%>article/html/${article.id}">${article.title}</a></h1>
			<ul class="article-meta" >
                    <li>作者：${article.author}&nbsp;&nbsp;&nbsp;&nbsp分类：<a href="<%=basePath%>category/${article.aliasName}" rel="category tag" data-original-title="" title="">${article.categoryName}</a>
						&nbsp;&nbsp;&nbsp;&nbsp;时间：${article.createTime}</li>
			</ul>
		</header>
				<article class="article-content">
					${article.content}
					<p class="post-copyright">未经允许不得转载：<a href="<%=basePath%>">乱七八糟</a> » <a href="<%=basePath%>article/html/${article.id}">${article.title}</a></p>
				</article>

						<div class="article-tags">
						标签：
						<c:forEach items="${article.tagList}" var="tag">
							<a href="javascript:void(0)" onclick="goTag('${tag.tagName}')" rel="tag" data-original-title="" title="">${tag.tagName}</a>
						</c:forEach>
			</div>
		
		<nav class="article-nav">
			<c:if test="${beforeArticle != null}">
				<span class="article-nav-prev">上一篇<br><a href="<%=basePath%>article/html/${beforeArticle.id}" rel="prev">${beforeArticle.title}</a></span>
			</c:if>
			
			<c:if test="${nextArticle != null}">
				<span class="article-nav-next">下一篇<br><a href="<%=basePath%>article/html/${nextArticle.id}" rel="next">${nextArticle.title}</a></span>
			</c:if>
		</nav>

			
			<c:if test="${relationList != null && fn:length(relationList) > 0}">
				<div class="relates"><h3 class="title"><strong>相关推荐</strong></h3>
				<ul>
					<c:forEach items="${relationList}" var="relation">
						<li>
							<a href="<%=basePath%>article/html/${relation.id}">
                                <img data-original="<%=basePath%>uploads/${relation.thumbUrl}" class="thumb" src="<%=basePath%>uploads/${relation.thumbUrl}" style="display: inline;">
								${relation.title}
							</a>
						</li>
					</c:forEach>
				</ul>
				</div>	
			</c:if>

        <!-- UY BEGIN -->
        <div id="uyan_frame"></div>
        <script type="text/javascript" src="http://v2.uyan.cc/code/uyan.js?uid=2057443"></script>
        <!-- UY END -->

	</div>

</div>

	<jsp:include page="../footer.jsp" flush="true"></jsp:include>

</section>

<div class="ab-box">
    <div class="pr">
        <img src="<%=basePath%>img/tips_ico_04.png" alt="" class="ab-box-ico" />
        <textarea id="tip"></textarea>
        <a href="javasciript:;" onclick="addTip()" class="btn-ok">确定</a>
    </div>
</div>

<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery.lazyload/1.9.1/jquery.lazyload.js"></script>
<script type="text/javascript" src="<%=basePath%>js/ueditor1_4_3/third-party/SyntaxHighlighter/shCore.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/validation.js"></script>
<script type="text/javascript" src="<%=basePath%>js/flavr/flavr/js/flavr.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/flavr/base.js"></script>
</body>

<script>

$(function(){
        SyntaxHighlighter.highlight();

//        var _hmt = _hmt || [];
//        var hm = document.createElement("script");
//  		hm.src = "//hm.baidu.com/hm.js?790c0d88ebfcc979bcb6d95993bffb1d";
//  		var s = document.getElementsByTagName("script")[0];
//  		s.parentNode.insertBefore(hm, s);

        $("p").each(function(){
            var index= $("p").index(this);
            var obj = $(this);
            // 获取tip
            $.ajax({
                type : "POST",
                url : getRootPath()+'article/gettip',
                data : '&line='+index+'&articleId='+$("#articleId").val(),
                success  : function(data) {
                    $("#tip").val("");
                    $("#tip").val(data);
                    if(!isEmpty(data)){
                        obj.css("color","#ff5e52");
                    }
                }
            });
        })

        $("p").hover(function(){
            var index= $("p").index(this);
            var left = $(this).offset().left;
            var top = $(this).offset().top;
            var wid = $(this).width();
            // 获取tip
            $.ajax({
                type : "POST",
                url : getRootPath()+'article/gettip',
                data : '&line='+index+'&articleId='+$("#articleId").val(),
                success  : function(data) {
                    $("#tip").val("");
                    $("#tip").val(data);
                    if(!isEmpty(data)){
                        $(this).css("color","#ff5e52");
                    }
                    $(".ab-box").css({"left":(left+wid)+"px","top":top+"px"}).show();
                    // 设置ab-box对应的p标签行数
                    $(".ab-box").attr("line",index);
                    //console.log($(this).offset().left+"==="+$(this).offset().top+"===="+$(this).width());
                }
            });
        })

});

function addTip(){
    var tip = $("#tip").val();
    if(isEmpty(tip)){
        return;
    }
    if(tip.length > 200){
        return;
    }
    $.ajax({
        type : "POST",
        url : getRootPath()+'article/tip',
        data : '&content='+encodeURI(encodeURI(tip))+'&line='+$(".ab-box").attr("line")+'&articleId='+$("#articleId").val(),
        success  : function(data) {
            autoCloseAlert(data.errorInfo,1000);
        }
    });
}

function clearTip(){
    $("#tip").val("");
}

function goTag(tagName){
	window.location.href = getRootPath()+"tag/"+encodeURI(encodeURI(tagName))
}

function getRootPath() {
	//获取当前网址，如： http://localhost:8080/GameFngine/share/meun.jsp
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录，如： GameFngine/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8080
	var localhostPaht = curWwwPath.substring(0, pos);
	//获取带"/"的项目名，如：/GameFngine
	return (localhostPaht + "/");
}

</script>

</html>