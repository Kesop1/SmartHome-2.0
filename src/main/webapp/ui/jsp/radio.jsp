<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/amplituner?cmd="/>
<h2>Radio</h2>
<p>Głośność</p>
<div>
    <form action="${path}1_volup" method="post">
        <button type="submit" class="radio-button">+1</button>
    </form>
    <form action="${path}5_volup" method="post">
        <button type="submit" class="radio-button">+5</button>
    </form>
    <form action="${path}10_volup" method="post">
        <button type="submit" class="radio-button">+10</button>
    </form>
    <form action="${path}1_voldn" method="post">
        <button type="submit" class="radio-button">-1</button>
    </form>
    <form action="${path}5_voldn" method="post">
        <button type="submit" class="radio-button">-5</button>
    </form>
    <form action="${path}10_voldn" method="post">
        <button type="submit" class="radio-button">-10</button>
    </form>
</div>
<br>
<br>
<p>Kanał</p>
<div>
    <form action="${path}1" method="post">
        <button type="submit" class="radio-button">1</button>
    </form>
    <form action="${path}2" method="post">
        <button type="submit" class="radio-button">2</button>
    </form>
    <form action="${path}3" method="post">
        <button type="submit" class="radio-button">3</button>
    </form>
    <form action="${path}4" method="post">
        <button type="submit" class="radio-button">4</button>
    </form>
</div>
<br>
<br>
<p>Radiostacja</p>
<div>
    <form action="${path}radio1" method="post">
        <button type="submit" class="radio-button">Trójka</button>
    </form>
    <form action="${path}radio2" method="post">
        <button type="submit" class="radio-button">Jedynka</button>
    </form>
    <form action="${path}radio3" method="post">
        <button type="submit" class="radio-button">ESKA Rock</button>
    </form>
    <form action="${path}radio4" method="post">
        <button type="submit" class="radio-button">Meloradio</button>
    </form>
    <form action="${path}radio5" method="post">
        <button type="submit" class="radio-button">RMF Classic</button>
    </form>
    <form action="${path}radio6" method="post">
        <button type="submit" class="radio-button">Antyradio</button>
    </form>
    <form action="${path}radio7" method="post">
        <button type="submit" class="radio-button">Rock Radio</button>
    </form>
    <form action="${path}radio8" method="post">
        <button type="submit" class="radio-button">Kampus</button>
    </form>
</div>


