<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<h2>Pogoda</h2>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<table style="width:100%">
    <c:forEach items="${elementsMap.sensorsList}" var="entry">
        <tr>
            <td>${entry.displayName}</td>
            <td>${entry.value}&nbsp;${entry.unit}</td>
        </tr>
    </c:forEach>
</table>