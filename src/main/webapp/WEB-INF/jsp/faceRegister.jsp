<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>人脸注册</title>
<style>
.main {
	width: 800px;
	height: 375px;
	margin-left: auto;
	margin-right: auto;
	margin-top: 50px;
	font-size: 15px;
}

#video, #canvas {
	position: absolute;
}

table {
	float: right;
	width: 300px;
}

#title {
	font-size: 20px;
	width: 60px;
	height: 45px;
	padding-left: 20px;
}

#registerButton {
	font-size: 15px;
	background: #3b85f5;
	border-style: none;
	color: white;
	width: 100px;
	height: 40px;
	margin-top: 0px;
	margin-left: 20px;
}

#username, #age {
	border: 1px solid #ccc;
	padding: 5px 0px;
	border-radius: 3px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
}
/* 美化输入框 */
#username:focus{
	border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6)
}
#age:focus{
	border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6)
}
/* 美化单选框 */
#sex {
    width: 0px;
    margin-right: 25px;/*选项之间的距离*/
    position: relative;
}
    #sex:before,#sex:after {
    content: '';
    position: absolute;
    border-radius: 50%; /*圆角*/
    transition: .1s ease; /*中点跳转的速度*/
}
    #sex:before {/*所有选项*/
    width: 18px;
    height: 18px; /*外圆的大小*/
    background-color: #fff; /*圆环的颜色*/
    border: 1px solid #66afe9;/*外圆的边框*/
}
    #sex:after {/*未选中的中点*/
    top: 6px;
    left: 6px;
    width: 8px;
    height: 8px;
    background-color: #fff; /*未选的中点的颜色*/
}
    #sex:checked:after {/*选中的中点*/
    top: 4px;
    left: 4px;
    width: 12px;
    height: 12px;
    background-color:#66afe9; /*选中的中点*/
}
    #sex:checked:before {
     border-color:#66afe9; /*选中的外圆边框*/
}
</style>
<script>
	navigator.getUserMedia = navigator.getUserMedia
			|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
	if (navigator.getUserMedia) {
		navigator.getUserMedia({
			audio : false,
			video : {
				width : 500,
				height : 375
			}
		}, function(stream) {
			var video = document.getElementById("video");
			video.src = window.URL.createObjectURL(stream);
			video.onloadedmetadata = function(e) {
				video.play();
			};
		}, function(err) {
			console.log("The following error occurred: " + err.name);
		});
	}
	
	function mycheck() {
		var username = document.getElementById("username").value;
		var age = document.getElementById("age").value
		if(username.length == 0 || username.trim().length == 0) {
			alert("姓名不能为空。");
			return false;
		}
		if(age.lenght == 0 || age.trim().lenght == 0) {
			alert("年龄不能为空。");
			return false;
		}
		
		var context = document.getElementById("canvas").getContext("2d");
	    context.drawImage(video, 0, 0, 500, 375);
	    var snapData = document.getElementById("canvas").toDataURL("image/png");
	    var imgData=snapData.substr(22);
	    document.getElementById("imgValue").value = imgData;
	    
	    return true;
	}
	
	setTimeout(function () {
		document.getElementById("errorMsg").text = "";
	},5000);
</script>
</head>
<body>
	<form action="faceRegister/register" id="register" method="post" onsubmit="return mycheck()">
		<div class="main">
			<video id="video" width="500" height="375" autoplay></video>
			<table>
				<tr>
					<td id="title">姓名：</td>
					<td><input type="text" id="username" name="username" /></td>
				</tr>
				<tr>
					<td id="title">性别：</td>
					<td><input type="radio" name="sex" id="sex" value="1" checked />男<input type="radio" name="sex" id="sex" value="0"/>女</td>
				</tr>
				<tr>
					<td id="title">年龄：</td>
					<td><input type="text" id="age" name="age"/></td>
				</tr>
				<tr>
					<td id="title">占坑：</td>
					<td></td>
				</tr>
				<tr>
					<td id="title">占坑：</td>
					<td></td>
				</tr>
				<tr>
					<td id="title">占坑：</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" id="registerButton" value="注册" onclick="" />
					<input type="button" id="registerButton" value="返回" onclick="" /></td>
					
				</tr>
				<tr>
					<td colspan="2">
						<font color="red" style="margin-left: 20px;">
							<a id="errorMsg">
								<c:if test="${!empty errorMsg }">
									<c:out value="${errorMsg }" />
								</c:if>
							</a>
						</font>
					</td>
				</tr>
			</table>
			<canvas id="canvas" width="500" height="375"></canvas>
			<input type="hidden" id="imgValue" name="imgValue" />
		</div>
	</form>
</body>
</html>