<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.Map>"/>
<c:forEach items="${elementsMap.thingsMap}" var="entry">
    <c:set var = "name" value = "${entry.key}"/>
    <div>
        <c:if test="${entry.value['class'].name eq 'com.piotrak.elementservice.element.SwitchElement'}">
            <form action="${pageContext.request.contextPath}/${fn:toLowerCase(name)}?cmd=${entry.value.on eq true ? "OFF" : "ON"}" method="post">
                <label id="switch_element">
                    <c:if test="${entry.value.on eq true}"><button type="submit" class="switch on"> </button></c:if>
                    <c:if test="${entry.value.on eq false}"><button type="submit" class="switch off"> </button></c:if>
                </label>
                <label for="switch_element">${name}</label>
            </form>
        </c:if>
    </div>
</c:forEach>