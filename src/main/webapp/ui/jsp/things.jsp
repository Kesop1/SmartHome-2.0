<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<c:forEach items="${elementsMap.thingsList}" var="entry">
    <c:set var = "name" value = "${entry.name}"/>
    <div>
        <c:if test="${entry['class'].name eq 'com.piotrak.service.element.SwitchElement'}">
            <form action="${pageContext.request.contextPath}/${fn:toLowerCase(name)}?cmd=${entry.on eq true ? "OFF" : "ON"}" method="post">
                <label id="switch_element">
                    <c:if test="${entry.on eq true}"><button type="submit" class="switch on"> </button></c:if>
                    <c:if test="${entry.on eq false}"><button type="submit" class="switch off"> </button></c:if>
                </label>
                <label for="switch_element">${entry.displayName}</label>
            </form>
        </c:if>
        <c:if test="${entry['class'].name eq 'com.piotrak.service.element.HorizontalSeparatorElement'}">
            <hr>
        </c:if>
<%--        TODO: SchemaElement--%>
    </div>
</c:forEach>