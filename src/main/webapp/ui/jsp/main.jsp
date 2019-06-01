<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
    <spring:url value="/ui/static/css/main.css" var="mainCss" />
</head>
<body>
<link href="${mainCss}" rel="stylesheet" />
<div>
    <div class="container">
        <div class="things">
            <tiles:insertAttribute name="things" />
        </div>
        <div class="slot1">
            <tiles:insertAttribute name="slot1" />
        </div>
        <div class="slot2">
            <tiles:insertAttribute name="slot2" />
        </div>
        <div class="slot3">
            <tiles:insertAttribute name="slot3" />
        </div>
        <div class="slot4">
            <tiles:insertAttribute name="slot4" />
        </div>
    </div>
</div>
</body>
</html>