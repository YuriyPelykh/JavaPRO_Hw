<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Password recovery</title>
</head>
<body>
<div align="center">
    <c:url value="/password_recovery" var="recUrl" />
    <c:url value="/register" var="regUrl" />

    <form action="${recUrl}" method="POST">
        Enter your login:<br/><input type="text" name="login" value="${login}"><br/>
        Enter your e-mail:<br/><input type="text" name="email"><br/>
        <input type="submit" />

        <c:if test="${recovery_need_confirm eq true}">
            <p>Link for ${login}'s password recovery was sent to e-mail ${email}<br>
                Check your mailbox and follow link to proceed</p>
        </c:if>
        <c:if test="${recovery_need_confirm eq false}">
            <p>Such user wasn't found. You can register <a href="${regUrl}">here</a></p>
        </c:if>

    </form>



</div>
</body>
</html>
