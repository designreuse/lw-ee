<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>${param.title}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
    href="<c:url value="/css/bootstrap.min.css"/>">
<!-- Custom styles for this template -->
<link href="<c:url value="/css/sticky-footer.css"/>" rel="stylesheet">
<script
    src="<c:url value="/js/jquery.min.js"/>"></script>
<script
    src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script
    src="<c:url value="/js/bootbox.min.js"/>"></script>

    
 <c:forEach items="${param.css}" var="cssFile">
    <link href="<c:url value="/css/${cssFile}"/>" rel="stylesheet">
 </c:forEach>
 
 <c:forEach items="${param.js}" var="jsFile">
    <script src="<c:url value="/js/${jsFile}"/>"></script>
 </c:forEach>
</head>
<body>