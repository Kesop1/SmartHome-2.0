<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/"/>
<h2>Przyciski</h2>
<p>Odkurzacz</p>
<div>
    <form action="${path}vacuum?cmd=clean" method="post">
        <button type="submit" class="button">Do roboty</button>
    </form>
    <form action="${path}vacuum?cmd=home" method="post">
        <button type="submit" class="button">Do domu</button>
    </form>
</div>
<br>