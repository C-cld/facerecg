<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>人脸识别</title>
<script type="text/javascript" src="../js/tracking-min.js"></script>
<script type="text/javascript" src="../js/face-min.js"></script>
<script src="http://code.jquery.com/jquery-1.4.1.min.js"></script>
<style>
.main {
	width: 700px;
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
	width: 200px;
}

#title {
	font-size: 20px;
	width: 60px;
	height: 50px;
	padding-left: 20px;
}
</style>
</head>
<body>
	<form action="/facerecg/detect" id="detect">
		<div class="main">
			<video id="video" width="500" height="375" autoplay></video>
			<table>
				<tr>
					<td id="title">姓名：</td>
					<td><a id="username"></a></td>
				</tr>
				<tr>
					<td id="title">性别：</td>
					<td><a id="sex"></a></td>
				</tr>
				<tr>
					<td id="title">年龄：</td>
					<td><a id="age"></a></td>
				</tr>
				<tr>
					<td id="title">颜值：</td>
					<td><a id="beauty"></a></td>
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
					<td colspan="2"><font color="red" style="margin-left: 20px;"><a id="errorMsg"></a></font></td>
				</tr>
			</table>
			<!-- 用于显示跟踪框 -->
			<canvas id="canvas" width="500" height="375"></canvas>
			<!-- 用于上传，不能用同一个是因为 drawImage会覆盖视频-->
			<canvas id="canvas2" width="500" height="375"></canvas>
			<input type="hidden" id="imgValue" name="imgValue" />
		</div>
	</form>
	<script>
			window.onload = function() {
				var video = document.getElementById('video');
				var canvas = document.getElementById('canvas');
				var context = canvas.getContext('2d');
				var canvas2 = document.getElementById('canvas2');
				var context2 = canvas2.getContext('2d');
				var temp = true;
				
				var tracker = new tracking.ObjectTracker('face');
				tracker.setInitialScale(7);
				tracker.setStepSize(2);
				tracker.setEdgesDensity(0.1);
				
				tracking.track('#video', tracker, { camera: true });
				
				tracker.on('track', function(event) {
					context.clearRect(0, 0, canvas.width, canvas.height);
					event.data.forEach(function(rect) {
						context.strokeStyle = '#3b85f5';
						context.strokeRect(rect.x, rect.y, rect.width, rect.height);
						context.font = '11px Helvetica';
						context.fillStyle = "#fff";
						context.fillText('x: ' + rect.x + 'px', rect.x + rect.width + 5, rect.y + 11);
						context.fillText('y: ' + rect.y + 'px', rect.x + rect.width + 5, rect.y + 22);
						context2.drawImage(video, 0, 0, 500, 375);
						var snapData = canvas2.toDataURL('image/png');
						var imgData = snapData.substr(22);
						document.getElementById("imgValue").value = imgData;
						//实时检测人脸，但是三秒调一次api
						if (temp) {
							uploadImg(imgData);	
							temp = false;
							setTimeout(function () {temp = true;},3000);
						}
						
						
					});
				});
			}
			
			function uploadImg(imgData) {
				$.ajax({
					url : "/facerecg/detect",
					type : "post",
					dataType : "json",
					data : $("#detect").serialize(),
					success : function(data) {
						if (data) {
							if (data.user.username) {
								document.getElementById("errorMsg").text = "";
								document.getElementById("username").text = data.user.username;
								if (data.user.sex == 1) {
									document.getElementById("sex").text = "男";
								} else {
									document.getElementById("sex").text = "女";
								}
									document.getElementById("age").text = data.user.age;
									document.getElementById("beauty").text = data.user.beauty;
								} else {//检测到人脸但是不在人脸库中
									document.getElementById("username").text = "";
									document.getElementById("sex").text = "";
									document.getElementById("age").text = "";
									document.getElementById("beauty").text = data.user.beauty;
									document.getElementById("errorMsg").text = "人脸不在人脸库中。";
								}
							} else {
								document.getElementById("username").text = "";
								document.getElementById("sex").text = "";
								document.getElementById("age").text = "";
								document.getElementById("beauty").text = "";
								document.getElementById("errorMsg").text = "检测不到人脸。";
							}
						},
					error : function(response) {
					}
				});
				console.log("===");
			}
			
			function sleep(numberMillis) {
				var now = new Date();
				var exitTime = now.getTime() + numberMillis;
				while (true) {
					now = new Date();
					if (now.getTime() > exitTime)
						return;
					}
				}

		</script>
	</body>
</html>