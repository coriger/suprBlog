// 保存文章
function saveArticle(){
    var param = {};
    
    // 收集参数 校验
    var categoryId = $("#categoryId").val();
    if(categoryId == '-1'){
    	autoCloseAlert("请选择栏目",500);
    	return false;
    }
    param["categoryId"] = categoryId;
    
    var title = $("#title").val();
    if(isEmpty(title)){
    	autoCloseAlert("请输入标题",500);
    	return false;
    }
    param["title"] = title;
    
    var arr = [];
    arr.push(UE.getEditor('editor').getContent());
    var content = arr.join("\n");

    var thumbUrl = $("#thumbPicUrl").val();
    if(isEmpty(thumbUrl)){
        autoCloseAlert("请上传文章缩略图",500);
        return false;
    }
    param["thumbUrl"] = thumbUrl;
    
    // 简介
    var description = UE.getEditor('editor').getContentTxt().substring(0,500);
    
    // 标签
    var tagId = $(".chosen-select").val();
    if(!isEmpty(tagId)){
    	var ids = (tagId+"").split("\,");
    	var tagArray = [];
    	for(var i=0;i<ids.length;i++){
    		tagObj = {id:ids[i]};
    		tagArray.push(tagObj);
    	}
    	param["tagList"] = tagArray;
    	console.info(tagArray);
    }else{
    	autoCloseAlert("请输入标签",500);
    	return false;
    }

    // 保存文章
    $.ajax({
        type : "POST",
        url : '../article/add',
        data : 'param='+encodeURI(encodeURI(JSON.stringify(param)))+"&content="+encodeURI(encodeURI(content)).replace(/\&/g, "%26").replace(/\+/g, "%2B")+"&description="+encodeURI(encodeURI(description)),
        success  : function(data) {
        	if(data.resultCode != 'success'){
        		autoCloseAlert(data.errorInfo,1000);
				return false;
			}else{
				// 调到列表页
				window.location.href = "../article/list";
			}
		}
    });
}

function cancelSaveArticle(){
	window.location.href = "../article/list";
}

function getRootPath() {
    //获取当前网址，如： http://localhost:8080/GameFngine/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： GameFngine/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPaht = curWwwPath.substring(0, pos);
    return (localhostPaht + "/");
}

// 上传文章缩略图
function uploadThumbPic(obj){
    var file = document.getElementById("thumbPic").value;
    if (file == undefined || file == '') {
        autoCloseAlert("请选择需要上传的文件",1000);
        return;
    } else if (!/.(png)$/.test(file) && !/.(jpg)$/.test(file)) {
        autoCloseAlert("文件类型必须是.png或者.jpg",1000);
        return;
    } else {
        $.ajaxFileUpload({
            url : getRootPath()+'upload',
            secureuri : false,
            method : 'post',
            fileElementId : 'thumbPic',// 上传控件的id
            dataType : 'json',
            data : {}, // 其它请求参数
            success : function(data, status) {
                // 更新缩略图
                $("#thumbPicUrl").val(data.msg);
                $("#thumbShowPic").attr("src", getRootPath() +"uploads/"+ data.msg);
                $("#thumbShowPic").parent().show();
            }
        })
    }
}