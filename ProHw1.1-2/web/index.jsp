<%--
  Created by IntelliJ IDEA.
  User: Rocca
  Date: 06.04.2020
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$ Questionnaire</title>
  </head>
  <body>
    <form action="/questionnaire" method="POST">
        First name: <input type="text" name="name"><br>
        Last name: <input type="text" surname="surname"><br>
        Your age: <input type="text" age="age"><br>
        <br/>Do you like Java?
        <br/><input type = "radio" name = "question1" value = "yes" /> Yes
        <br/><input type = "radio" name = "question1" value = "no" /> No
        <br/>Do you like .NET?
        <br/><input type = "radio" name = "question2" value = "yes" /> Yes
        <br/><input type = "radio" name = "question2" value = "no" /> No
        <br/><input type="submit"> </input>
    </form>
  </body>
</html>
