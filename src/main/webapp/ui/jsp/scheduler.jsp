<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var = "path" value = "${pageContext.request.contextPath}/scheduler/"/>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
    <spring:url value="/ui/static/css/scheduler.css" var="schedulerCss" />
</head>
<body>
    <link href="${schedulerCss}" rel="stylesheet" />
    <form method="post">
        Element:
        <select name="element">
            <c:forEach items="${elementsMap.thingsList}" var="entry">
                <option value="${entry.name}">${entry.displayName}</option>
            </c:forEach>
        </select>
         Command:
        <select name="command">
            <option>ON</option>
            <option>OFF</option>
        </select>
         Delay:
        <select name="delay">
            <option></option>
            <option>10</option>
            <option>20</option>
            <option>30</option>
            <option>45</option>
            <option>60</option>
            <option>90</option>
            <option>120</option>
        </select>
         or
         Time:
        <input type="time" name="time"/>
        <input type="submit" class="button"/>
    </form>
    <br/>
    <br/>
    <br/>
    <h1>Scheduled jobs:</h1>
    <form method="post">
        <table class="schedulerTable">
            <th>Date</th>
            <th>Element</th>
            <th>Command</th>
            <th>Remove</th>
            <c:forEach items="${scheduledCommands}" var="entry">
                <tr>
                    <td>${entry.date}</td>
                    <td>${entry.element}</td>
                    <td>${entry.value}</td>
                    <td><input type="submit" value="Remove" formaction="${path}${entry.id}"/></td>
                </tr>
            </c:forEach>
        </table>
    </form>
</body>
</html>