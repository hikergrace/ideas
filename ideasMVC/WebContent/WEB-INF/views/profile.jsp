<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Profile</title>
</head>
<body>

	<!-- Leading navigation bar to return home -->
	<nav class="navbar navbar-expand-lg navbar-light bg-light"> <a class="navbar-brand" href="">Home</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	</nav>
	
	<!-- Big headline telling you whose profile you're looking at -->
	<h1>${profile.username }</h1><br>
	
	<img alt="Profile picture of ${profile.username }" src="${profile.profilePic }"><br>

	<h5>Member since: ${profile.createdDate }</h5><br>
	<p>${profile.bio }</p>
	
	<hr>
	
	<!-- Reputation probably special object in Controller -->
	<h5>Reputation: ${reputation }</h5><br>
	
	<!-- ideaCount probably special object in Controller -->
	<h5>Total Ideas: ${ideaCount }</h5><br>
	
	
	<!-- Listing for user's ideas -->
	<c:forEach var="i" items="${profile.ideaList}">
		<h3>
			<a href="" id="ideaLink">${i.name}</a>
		</h3>
		<br>
	</c:forEach>
	
</body>
</html>