<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<jsp:useBean id="category" type="Map<Integer, String>" scope="request" />
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
        <script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <title>棒グラフ</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <h1><%=dateArray[0]%>年<%=dateArray[1]%>月の<%=request.getParameter("action").equals("show_rev_bar") ? "収入" : "支出"%></h1>
            <p>
                <a href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=previousYear%>-<%=previousMonth%>">先月</a>
                <a href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=nextYear%>-<%=nextMonth%>">翌月</a>
            </p>
            <form id="submit_form" action="?action=<%=request.getParameter("action")%><% if (request.getParameter("date") != null) {%>&date=<%=request.getParameter("date")%><%}%>" method="post">
                <select id="submit_select" name="category" required>
                    <option value="0" <%=(request.getParameter("category").equals("0")) ? "selected" : ""%>>全て</option>
                    <%
                        for (Map.Entry<Integer, String> e : category.entrySet()) {
                    %>
                    <option value="<%=e.getKey()%>" <%=(Integer.parseInt(request.getParameter("category")) == e.getKey()) ? "selected" : ""%>><%=e.getValue()%></option>
                    <%
                        }
                    %>
                </select>
            </form>
            <h1><img src="barchart.jpg?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" /></h1>
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
