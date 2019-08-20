<%@page pageEncoding="UTF-8"%>
<c:set var = "path" value = "${pageContext.request.contextPath}/"/>
<h2>Przyciski</h2>
<p>Odkurzacz</p>
<div>
    <form action="${path}vacuum/ir?cmd=clean" method="post">
        <button type="submit" class="button">Do roboty</button>
    </form>
    <form action="${path}vacuum/ir?cmd=home" method="post">
        <button type="submit" class="button">Do domu</button>
    </form>
</div>
<br><br>
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
<br>