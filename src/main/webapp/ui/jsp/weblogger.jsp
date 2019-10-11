<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var = "path" value = "${pageContext.request.contextPath}/"/>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
    <spring:url value="/ui/static/css/logger.css" var="loggerCss" />
    WEBLOGGER
</head>
<body>
<link href="${loggerCss}" rel="stylesheet" />
    <form action="${path}weblogger?level=${level}&size=${size}" method="post">
        Poziom:
        <select name="level">
            <option selected>${loglevel}</option>
            <option>OFF</option>
            <option>SEVERE</option>
            <option>WARNING</option>
            <option>INFO</option>
            <option>CONFIG</option>
            <option>FINE</option>
            <option>FINER</option>
            <option>FINEST</option>
            <option>ALL</option>
        </select>
        Rozmiar:
        <select name="size">
            <option selected>${logsize}</option>
            <option>0</option>
            <option>10</option>
            <option>50</option>
            <option>100</option>
            <option>200</option>
            <option>500</option>
            <option>1000</option>
        </select>
        <input type="submit" class="button"></input>
    </form>
    <table class="loggerTable">
        <th>Data</th>
        <th>Klasa</th>
        <th>Poziom</th>
        <th>Wiadomość</th>
        <c:forEach items="${logs}" var="entry">
            <tr>
                <td>${entry.dateTime}</td>
                <td>${entry.className}</td>
                <td>${entry.level}</td>
                <td>${entry.message}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>