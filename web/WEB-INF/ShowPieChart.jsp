<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>円グラフ</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            ${requestScope['map']}
            <img src="chart.jpg?action=<%=request.getParameter("action")%><% if (request.getParameter("date") != null) {%>&date=<%=request.getParameter("date")%><%}%>" usemap="#map" border="0" />
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>