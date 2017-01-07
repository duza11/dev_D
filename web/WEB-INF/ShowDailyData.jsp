<%@page import="accountbook.RevenueItem"%>
<%@page import="accountbook.RevenueBlock"%>
<%@page import="accountbook.SpendingItem"%>
<%@page import="accountbook.SpendingBlock"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<jsp:useBean id="rList" type="List<RevenueBlock>" scope="request" />
<jsp:useBean id="sList" type="List<SpendingBlock>" scope="request" />
<!DOCTYPE html>
<%
    int dateArray[] = new int[3];
    for (int i = 0; i < dateArray.length; i++) {
        dateArray[i] = Integer.parseInt(request.getParameter("date").split("-")[i]);
    }
    int rSum = 0;
    int sSum = 0;
%>
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>詳細データ</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <h1>収支の詳細（<%=dateArray[0]%>年<%=dateArray[1]%>月<%=dateArray[2]%>日）</h1>

            <div class="panel panel-default">
                <div class="panel-heading">
                    収入
                </div>
                <%
                    for (RevenueBlock rb : rList) {
                %>
                <div class="panel-body">
                    <h3><%=rb.getPlace()%></h3>
                </div>
                <table class="table table-bordered table-striped"">
                    <thead>
                        <tr>
                            <th>ジャンル</th>
                            <th>名前</th>
                            <th>金額</th>
                            <th>数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (RevenueItem ri : rb.getRevenueItemList()) {
                                rSum += ri.getPrice() * ri.getCount();
                        %>
                        <tr>
                            <td><%=ri.getKindName()%></td>
                            <td><%=ri.getItemName()%></td>
                            <td><%=ri.getPrice()%></td>
                            <td><%=ri.getCount()%></td>
                            <%
                                }
                            %>
                        </tr>
                    </tbody>
                </table>
                <%
                    }
                %>
                <div class="panel-footer">
                    合計金額 ： <%=rSum%>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading">
                    支出
                </div>
                <%
                    for (SpendingBlock sb : sList) {
                %>
                <div class="panel-body">
                    <h3><%=sb.getPlace()%></h3>
                </div>
                <table class="table table-bordered table-striped" style=" padding-bottom: 20px;">
                    <thead>
                        <tr>
                            <th>ジャンル</th>
                            <th>名前</th>
                            <th>金額</th>
                            <th>数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (SpendingItem si : sb.getSpendingItemList()) {
                                sSum += si.getPrice() * si.getCount();
                        %>
                        <tr>
                            <td><%=si.getKindName()%></td>
                            <td><%=si.getItemName()%></td>
                            <td><%=si.getPrice()%></td>
                            <td><%=si.getCount()%></td>
                            <%
                                }
                            %>
                        </tr>
                    </tbody>
                </table>
                <%
                    }
                %>
                <div class="panel-footer">
                    合計金額 ： <%=sSum%>
                </div>
            </div>
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
    </body>
</html>