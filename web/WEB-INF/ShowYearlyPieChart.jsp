<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="date" class="java.lang.String" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>円グラフ</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class="panel-title"><%=date%>年の<%=request.getParameter("action").equals("show_monthly_rev_pie") ? "収入" : "支出"%></p>
                </div>
                <div class="panel-body">
                    <p>
                        <a class="btn btn-primary" href="?action=<%=request.getParameter("action")%>&date=<%=Integer.parseInt(date) - 1%>">前年</a>
                        <a class="btn btn-primary" href="?action=<%=request.getParameter("action")%>&date=<%=Integer.parseInt(date) + 1%>">翌年</a>
                    </p>
                    <p>
                        ${requestScope['map']}
                        <img class="img-responsive" src="piechart.jpg?action=<%=request.getParameter("action")%><%=(request.getParameter("date") != null) ? "&date=" + request.getParameter("date") : ""%>" usemap="#map" border="0" />
                    </p>
                </div>
            </div>
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
    </body>
</html>
