<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="success" class="java.lang.String" scope="request" />
<jsp:useBean id="error" class="java.lang.String" scope="request" />
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="abc" class="accountbook.AccountBookCalendar" scope="request" />

<%
    int previousMonth = (abc.getMonth() + 1 - 2 + 12) % 12 + 1;
    int previousYear = abc.getYear() - previousMonth / 12;
    int nextMonth = (abc.getMonth() + 1) % 12 + 1;
    int nextYear = abc.getYear() + (abc.getMonth() + 1) / 12;
%>

<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>カレンダー</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <div class="container alert-container">
                <!-- 表示すべきメッセージがある場合は出力 -->
                <% if (success != null && success.length() > 0) {%>
                <div class="alert alert-success" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="閉じる"><span aria-hidden="true">×</span></button>
                    <%=success%>
                </div>
                <% }%>
                <% if (error != null && error.length() > 0) {%>
                <div class="alert alert-danger" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="閉じる"><span aria-hidden="true">×</span></button>
                    <%=error%>
                </div>
                <% }%>
            </div>
            <table class="calendar">
                <tr>
                    <td class="text-center"><a href="?action=show_calendar&year=<%=previousYear%>&month=<%=previousMonth%>">前月</a></td>
                    <td colspan="5" class="text-center"><%=abc.getYear()%>年<%=abc.getMonth() + 1%>月</td>
                    <td class="text-center"><a href="?action=show_calendar&year=<%=nextYear%>&month=<%=nextMonth%>">翌月</a></td>
                </tr>
                <tr><td class="week">日</td><td class="week">月</td><td class="week">火</td><td class="week">水</td><td class="week">木</td><td class="week">金</td><td class="week">土</td></tr>
                <%
                    for (int i = 0; i < abc.getDayList().size() / 7; i++) {
                %>
                <tr>
                    <%
                        for (int j = i * 7; j < i * 7 + 7; j++) {
                            if (abc.getDayList().get(j).getDay() > 35) {
                    %>
                    <td class="otherday"><%=abc.getDayList().get(j).getDay() - 35%>
                        <%
                        } else {
                        %>
                    <td class="day">
                        <a href="?action=show_daily&date=<%=abc.getYear()%>-<%=abc.getMonth() + 1%>-<%=abc.getDayList().get(j).getDay()%>">
                            <%=abc.getDayList().get(j).getDay()%>
                        </a>
                        <%
                            }
                        %>
                    </td>
                    <%
                        }
                    %>
                </tr>
                <tr>
                    <%
                        for (int j = i * 7; j < i * 7 + 7; j++) {
                            if (abc.getDayList().get(j).getDay() > 35) {
                    %>
                    <td class="sche"></td>
                    <%
                    } else {
                    %>
                    <td class="sche">
                        <%
                            if (abc.getDayList().get(j).getRevenue() != -1) {
                        %>
                        <div class="ft-red"><%=abc.getDayList().get(j).getRevenue()%></div>
                        <%
                            }
                            if (abc.getDayList().get(j).getSpending() != -1) {
                        %>
                        <div class="ft-green"><%=abc.getDayList().get(j).getSpending()%></div>
                        <%
                            }
                        %>
                    </td>
                    <%                        }
                        }
                    %>
                </tr>
                <%
                    }
                %>
            </table>
        </div>
        <script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
    </body>
</html>