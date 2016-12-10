<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<%
    int[] dateArray = new int[2];
    for (int i = 0; i < dateArray.length; i++) {
        dateArray[i] = Integer.parseInt(date.split("-")[i]);
    }
    int previousMonth = (dateArray[1] - 2 + 12) % 12 + 1;
    int previousYear = dateArray[0] - previousMonth / 12;
    int nextMonth = dateArray[1] % 12 + 1;
    int nextYear = dateArray[0] + dateArray[1] / 12;
%>
<html lang="ja">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>棒グラフ</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <p>
                <a href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=previousYear%>-<%=String.format("%02d", previousMonth)%>">先月</a>
                <a href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=nextYear%>-<%=String.format("%02d", nextMonth)%>">翌月</a>
            </p>
            <h1><img src="barchart.jpg?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%><% if (request.getParameter("date") != null) {%>&date=<%=request.getParameter("date")%><%}%>" /></h1>
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
