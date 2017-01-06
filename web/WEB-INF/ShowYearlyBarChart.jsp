<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<jsp:useBean id="category" type="Map<Integer, String>" scope="request" />
<html>
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
            <h1><%=date%>年の<%=request.getParameter("action").equals("show_rev_bar") ? "収入" : "支出"%></h1>
            <div class="row">
                <div class="col-md-2">
                    <form id="submit_form" action="?action=<%=request.getParameter("action")%><% if (request.getParameter("date") != null) {%>&date=<%=request.getParameter("date")%><%}%>" method="post">
                        <div class="form-group">
                            <select class="form-control" id="submit_select" name="category" required>
                                <option value="0" <%=(request.getParameter("category").equals("0")) ? "selected" : ""%>>全て</option>
                                <%
                                    for (Map.Entry<Integer, String> e : category.entrySet()) {
                                %>
                                <option value="<%=e.getKey()%>" <%=(Integer.parseInt(request.getParameter("category")) == e.getKey()) ? "selected" : ""%>><%=e.getValue()%></option>
                                <%
                                    }
                                %>
                            </select>

                        </div>
                    </form>
                </div>
                <div class="col-md-2">
                    <a class="btn btn-primary" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=Integer.parseInt(date) - 1%>">前年</a>
                    <a class="btn btn-primary" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=Integer.parseInt(date) + 1%>">翌年</a>
                </div>
            </div>
            <h1><img src="barchart.jpg?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" /></h1>
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
