<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<jsp:useBean id="category" type="Map<Integer, String>" scope="request" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>棒グラフ</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class="panel-title pull-left"><%=date%>年の<%=request.getParameter("action").equals("show_yearly_rev_bar") ? "収入" : "支出"%></p>
                    <div class="pull-right">
                        <a class="btn btn-default" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=Integer.parseInt(date) - 1%>">前年</a>
                        <a class="btn btn-default" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=Integer.parseInt(date) + 1%>">翌年</a>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="panel-body">
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
                    </div>
                    <p>
                        <img class="img-responsive" src="barchart.jpg?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" />
                    </p>
                </div>
            </div>
        </div>
        <script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
    </body>
</html>
