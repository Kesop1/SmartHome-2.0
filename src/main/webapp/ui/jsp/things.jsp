<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.Map>"/>
<c:forEach items="${elementsMap.thingsMap}" var="entry">
    <div>
        <c:if test="${entry.value['class'].name eq 'com.piotrak.service.element.SwitchElement'}">
            <label class="switch" id="switch_element">
                <input type="checkbox" >
                <span class="slider round"></span>
            </label>
            <label for="switch_element">${entry.key}</label>
        </c:if>
    </div>
</c:forEach>