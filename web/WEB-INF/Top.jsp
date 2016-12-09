<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<jsp:useBean id="message" class="java.lang.String" scope="request" />
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="abc" class="accountbook.AccountBookCalendar" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>カレンダー</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
        <link href="css/calendar.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <table>
                <tr>
                    <td class="text-center"><a href="?action=show_calendar&year=<%=abc.getYear()%>&month=<%=abc.getMonth() - 1%>">前月</a></td>
                    <td colspan="5" class="text-center"><%=abc.getYear()%>年<%=abc.getMonth() + 1%>月</td>
                    <td class="text-center"><a href="?action=show_calendar&year=<%=abc.getYear()%>&month=<%=abc.getMonth() + 1%>">翌月</a></td>
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
                        <a href="#<%=abc.getYear()%>-<%=abc.getMonth() + 1%>-<%=abc.getDayList().get(j).getDay()%>">
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
                        <div class="revenue"><%=abc.getDayList().get(j).getRevenue()%></div>
                        <%
                            }
                            if (abc.getDayList().get(j).getSpending() != -1) {
                        %>
                        <div class="spending"><%=abc.getDayList().get(j).getSpending()%></div>
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
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </body>
</html>