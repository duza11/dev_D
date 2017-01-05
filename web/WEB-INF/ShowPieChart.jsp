<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
        <meta charset="utf-8">
        <title>円グラフ</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <h1><%=dateArray[0]%>年<%=dateArray[1]%>月の<%=request.getParameter("action").equals("show_rev_pie") ? "収入" : "支出"%></h1>
            <p>
                <a href="?action=<%=request.getParameter("action")%>&date=<%=previousYear%>-<%=previousMonth%>">先月</a>
                <a href="?action=<%=request.getParameter("action")%>&date=<%=nextYear%>-<%=nextMonth%>">翌月</a>
            </p>
            ${requestScope['map']}
            <img src="piechart.jpg?action=<%=request.getParameter("action")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" usemap="#map" border="0" />
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>