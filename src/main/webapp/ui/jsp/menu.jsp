<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<html>
<body>
<h2>Menu</h2>
<div>
    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Wyloguj</button>
    </form>
    <br/>
    <form action="${pageContext.request.contextPath}/weblogger" method="get">
        <button type="submit">Logger</button>
    </form>
    <br/>
    <form action="${pageContext.request.contextPath}/main" method="get">
        <button type="submit">Ekran główny</button>
    </form>
    <br/>
    <form action="${pageContext.request.contextPath}/scheduler" method="get">
        <button type="submit">Planowanie</button>
    </form>
    <br/>
    <form action="${pageContext.request.contextPath}/help" method="get">
        <button type="submit">Pomoc</button>
    </form>
    <br/>
    <form action="${pageContext.request.contextPath}/calendar" method="get">
        <button type="submit">Kalendarz</button>
    </form>
</div>
</body>
</html>