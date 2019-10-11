<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="elementsMap" scope="request" type="java.util.Map<java.lang.String, java.util.List>"/>
<html>
<head>
    <spring:url value="/ui/static/css/help.css" var="helpCss" />
</head>
<body>
    <link href="${helpCss}" rel="stylesheet" />
    <h1>Pomoc</h1>

    <h2>Dostępne template'y</h2>
    <c:forEach items="${elementsMap.templatesList}" var="entry">
        ${entry.name},
    </c:forEach>

    <h2>Dostępne elementy</h2>
    <c:forEach items="${elementsMap.thingsList}" var="entry">
        <c:if test="${entry['class'].name ne 'com.piotrak.service.element.HorizontalSeparatorElement'}">
            ${entry.name},
        </c:if>
    </c:forEach>

    <h2>Wydarzenia kalendarza</h2>
       W celu ustawienia zdarzenia kalendarza należy wejść w odpowiedni Kalendarz Google oraz ustawić wydarzenie w odpowiednim formacie:<br>
       1. Element, który ma zadziałać (może to być "Template" lub właściwy element)<br>
       2. Spacja<br>
       3. Komenda do uruchomienia (Właściwa komenda, komenda z przedrostkiem "IR_" utworzy komendę IR, lub nazwa template)<br><br>

       Na przykład, żeby ustawić włączenie template radio: "Template radio"<br>
       &emsp;&emsp;&emsp;&emsp;&emsp;żeby włączyć Amplituner: "Amplituner on"<br>
       &emsp;&emsp;&emsp;&emsp;&emsp;żeby zgłośnić Amplituner przy pomocy komendy IR: "Amplituner IR_6_volup"<br><br>

       Można także ustawić kilka komend w jednym wydarzeniu, trzeba je tylko oddzielić średnikiem, na przykład: "Template radio;Amplituner off"<br><br>

       Dostępne komendy: on, off, IR_(1_..n_)volup, IR_(1_..n_)voldn, IR_radio(1..n), IR_clean, IR_home
</body>
</html>