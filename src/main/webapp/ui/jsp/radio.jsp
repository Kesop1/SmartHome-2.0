<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/amplituner?cmd="/>
<h2>Radio</h2>
<table class="radio-table">
    <tr>
        <td>
            <form action="${path}1_volup" method="post">
                <button type="submit" class="radio-buttn">+1</button>
            </form>
        </td>
        <td>
            <form action="${path}5_volup" method="post">
                <button type="submit" class="radio-buttn">+5</button>
            </form>
        </td>
        <td>
            <form action="${path}10_volup" method="post">
                <button type="submit" class="radio-buttn">+10</button>
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <form action="${path}1_voldn" method="post">
                <button type="submit" class="radio-buttn">-1</button>
            </form>
        </td>
        <td>
            <form action="${path}5_voldn" method="post">
                <button type="submit" class="radio-buttn">-5</button>
            </form>
        </td>
        <td>
            <form action="${path}10_voldn" method="post">
                <button type="submit" class="radio-buttn">-10</button>
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <form action="${path}1" method="post">
                <button type="submit" class="radio-buttn">1</button>
            </form>
        </td>
        <td>
            <form action="${path}2" method="post">
                <button type="submit" class="radio-buttn">2</button>
            </form>
        </td>
        <td>
            <form action="${path}3" method="post">
                <button type="submit" class="radio-buttn">3</button>
            </form>
        </td>
        <td>
            <form action="${path}4" method="post">
                <button type="submit" class="radio-buttn">4</button>
            </form>
        </td>
    </tr>
</table>