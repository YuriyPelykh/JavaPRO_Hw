<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>File DB</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>
    <body>
    <div class="container">
        <h3><img height="50" width="55" src="<c:url value="/static/logo.png"/>"/><a href="/">File upload</a></h3>

        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul id="groupList" class="nav navbar-nav">
                        <li><button type="button" id="back" class="btn btn-default navbar-btn">Back</button></li>
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>

        <form method="POST" action="/upload" enctype="multipart/form-data">
            <input type="file" name="file"/><br/>
            <input type="submit" value="Submit"/>
        </form>

    </div>

    <script>
        $('.dropdown-toggle').dropdown();

        $('#back').click(function(){
            window.location.href='/';
        });

        $('#send').click(function(){
            window.location.href='/';
        });
    </script>
    </body>
</html>