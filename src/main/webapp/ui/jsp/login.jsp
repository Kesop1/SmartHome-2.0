<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head></head>
<body>
<h1>Logowanie</h1>
<form name='f' action="perform_login" method='POST'>
    <table>
        <tr>
            <td>Login:</td>
            <td><input type='text' name='username' value=''></td>
        </tr>
        <tr>
            <td>Hasło:</td>
            <td><input type='password' name='password' /></td>
        </tr>
        <tr>
            <td>Zapamiętaj mnie:</td>
            <td><input type="checkbox" name="remember-me" /></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
        </tr>
    </table>
</form>
</body>
</html>