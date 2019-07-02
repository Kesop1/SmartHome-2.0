<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<h2>Pogoda</h2>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<c:forEach items="${elementsMap.sensorsList}" var="entry">
    <p>${entry.displayName}</p>
    <c:forEach var="read" items="${entry.values}">
        ${read.key} = ${read.value}
        <br>
    </c:forEach>
</c:forEach>