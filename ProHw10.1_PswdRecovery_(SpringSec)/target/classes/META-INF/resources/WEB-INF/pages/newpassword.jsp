<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Password recovery</title>
</head>
<body>
<div align="center">
    <c:url value="/password_recovery_complete" var="recUrl" />
    <c:url value="/register" var="regUrl" />

    <form action="${recUrl}" method="POST">
        <input type="hidden" name="login" value="${login}">
        Enter new password for ${login}:<br/><input type="password" name="password"><br/>
        Confirm password:<br/><input type="password" name="passwordConfirmation"><br/>
        <input type="submit" />

        <c:if test="${err_passwords_mismatch eq true}">
            <p>ERR: Password and confirmation mismatch!</p>
        </c:if>

    </form>



</div>
</body>
</html>
