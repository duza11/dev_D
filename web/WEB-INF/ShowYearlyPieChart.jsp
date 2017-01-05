<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <h1><%=date%>年の<%=request.getParameter("action").equals("show_rev_pie") ? "収入" : "支出"%></h1>
            <p>
                <a href="?action=<%=request.getParameter("action")%>&date=<%=Integer.parseInt(date) - 1%>">前年</a>
                <a href="?action=<%=request.getParameter("action")%>&date=<%=Integer.parseInt(date) + 1%>">翌年</a>
            </p>
            ${requestScope['map']}
            <img src="piechart.jpg?action=<%=request.getParameter("action")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" usemap="#map" border="0" />
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </body>
</html>
