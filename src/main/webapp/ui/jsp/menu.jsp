<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Menu</h2>
<div>
    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Logout</button>
    </form>
</div>
</body>
</html>