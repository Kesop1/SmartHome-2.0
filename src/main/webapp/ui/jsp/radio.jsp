<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/amplituner?cmd="/>
<h2>Radio</h2>
<div>
<%--    TODO: tutaj dodać kody--%>
    <form action="${path}1" method="post">
        <label id="template_element">
            <c:if test="${entry.active eq true}"><button type="submit" class="template template-on"> </button></c:if>
            <c:if test="${entry.active eq false}"><button type="submit" class="template template-off"> </button></c:if>
        </label>
        <label for="template_element">${entry.displayName}</label>
    </form>
</div>