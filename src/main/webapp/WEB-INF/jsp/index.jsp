<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<style type="text/css">
			table {
				width: 150px;
			    height: 100px;
			    margin: auto;
			    position: absolute;
			    top: 0;
			    left: 0;
			    right: 0;
			    bottom: 150;
			    font-size:25px;
			    text-align:center;
			}
			a {
				color:black;
				text-decoration:none;
			}
		</style>
	</head>
	<body>
		<table>
			<tr>
				<td><a href="faceRegister">人脸注册</a></td>
			</tr>
			<tr>
				<td><a href="faceDetect">人脸识别</a></td>
			</tr>
		</table>
	</body>
</html>
