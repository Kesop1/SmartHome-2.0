<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="calendarsMap" scope="request" type="java.util.Map<java.lang.String, java.lang.String>"/>
<html>
<head>
    <spring:url value="/ui/static/css/calendar.css" var="calendarCss" />
</head>
<body>
    <link href="${calendarCss}" rel="stylesheet" />
    <table class="calendarTable">
        <th>Roboczy</th>
        <th>Prywatny</th>

        <tr>
            <td height="90%">
                ${calendarsMap["smarthome"]}
            </td>
            <td height="90%">
                ${calendarsMap["private"]}
            </td>
        </tr>
    </table>
</body>