<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
        <head>
                <spring:url value="/ui/static/css/template.css" var="templateCss" />
                <title>SmartHome by Krys</title>
        </head>
<body>
        <link href="${templateCss}" rel="stylesheet" />
        <div>
                <div class="container" style="margin:50px">
                <div class="header">
                        <tiles:insertAttribute name="header" />
                </div>
                <div class="menu">
                        <tiles:insertAttribute name="menu" />
                </div>
                <div class="content">
                        <tiles:insertAttribute name="content" />
                </div>
                <div class="footer">
                        <tiles:insertAttribute name="footer" />
                </div>
        </div>
</body>
</html>