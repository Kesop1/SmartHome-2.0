<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page pageEncoding="UTF-8"%>
<h2>Schematy</h2>
<c:set var = "path" value = "${pageContext.request.contextPath}/schema?name="/>
<div>
    <form  action="${path}off" method="post">
        <label id="switch_element">
            <button type="submit" class="switch off"> </button>
        </label>
        <label for="switch_element">Wyłącz wszystko</label>
    </form>
</div>