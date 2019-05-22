<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.Map>"/>
<c:forEach items="${elementsMap.thingsMap}" var="entry">
    <c:set var = "name" value = "${entry.key}"/>
    <div>
        <c:if test="${entry.value['class'].name eq 'com.piotrak.service.element.SwitchElement'}">
            <label class="switch" id="switch_element">
                <input type="checkbox" <c:if test="${entry.value.on eq true}">checked=checked</c:if>
                       onclick="location.href='${pageContext.request.contextPath}/${fn:toLowerCase(name)}?cmd=${entry.value.on eq true ? "OFF" : "ON"}'"/>
                <span class="slider round"></span>
            </label>
            <label for="switch_element">${name}</label>
        </c:if>
    </div>
</c:forEach>