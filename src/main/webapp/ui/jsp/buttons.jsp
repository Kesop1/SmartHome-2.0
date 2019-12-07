<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/"/>
<html>
<head>
    <spring:url value="/ui/static/css/calendar.css" var="calendarCss" />
</head>
<body>
    <link href="${calendarCss}" rel="stylesheet" />

    <table class="buttonsTable" >
        <tr>
            <td>
                <p>Odkurzacz</p>
                <div>
                    <form action="${path}vacuum/ir?cmd=clean" method="post">
                        <button type="submit" class="button">Do roboty</button>
                    </form>
                    <form action="${path}vacuum/ir?cmd=home" method="post">
                        <button type="submit" class="button">Do domu</button>
                    </form>
                </div>
            </td>
            <td>
                <p>Dźwięk</p>
                <div>
                    <form action="${path}pc?cmd=audio-pc" method="post">
                        <button type="submit" class="button">PC</button>
                    </form>
                    <form action="${path}pc?cmd=audio-tv" method="post">
                        <button type="submit" class="button">TV</button>
                     </form>
                     <form action="${path}pc?cmd=audio-amp" method="post">
                        <button type="submit" class="button">AMP</button>
                     </form>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <p>Biurko</p>
                <div>
                    <form action="${path}desk?cmd=REST" method="post">
                        <button type="submit" class="button">Wolne</button>
                    </form>
                    <form action="${path}desk?cmd=SIT" method="post">
                        <button type="submit" class="button">Siedź</button>
                    </form>
                    <form action="${path}desk?cmd=STAND" method="post">
                        <button type="submit" class="button">Stać</button>
                    </form>
                </div>
            </td>
            <td>
            <br>
                <form action="${path}desk" method="post">
                    <input type="text" name="cmd" size="3">
                    <input type="submit" class="button"/>
                </form>
            </td>
        </tr>
    </table>
</body>