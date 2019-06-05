<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<h2>Schematy</h2>
<c:set var = "path" value = "${pageContext.request.contextPath}/template?name="/>
<c:forEach items="${elementsMap.templatesList}" var="entry">
    <c:set var = "name" value = "${entry.name}"/>
    <div>
        <form action="${path}${fn:toLowerCase(name)}" method="post">
            <label id="template_element">
                <c:if test="${entry.active eq true}"><button type="submit" class="template template-on"> </button></c:if>
                <c:if test="${entry.active eq false}"><button type="submit" class="template template-off"> </button></c:if>
            </label>
            <label for="template_element">${entry.displayName}</label>
        </form>
    </div>
</c:forEach>