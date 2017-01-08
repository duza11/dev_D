<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
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
                    <p class="panel-title pull-left"><%=dateArray[0]%>年<%=dateArray[1]%>月の<%=request.getParameter("action").equals("show_monthly_rev_bar") ? "収入" : "支出"%></p>
                    <div class="pull-right">
                        <a class="btn btn-default" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=previousYear%>-<%=previousMonth%>">前月</a>
                        <a class="btn btn-default" href="?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=nextYear%>-<%=nextMonth%>">翌月</a>
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
