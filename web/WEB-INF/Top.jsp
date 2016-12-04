<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<jsp:useBean id="message" class="java.lang.String" scope="request" />
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="abc" class="accountbook.AccountBookCalendar" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>カレンダー</title>
        <style>
            table{border:1px solid #a9a9a9;width:90%;padding:0px;margin:0px;border-collapse:collapse;}
            td{width:12%;border-top:1px solid #a9a9a9;border-left:1px solid #a9a9a9;vertical-align:top;margin:0px;padding:2px;}
            td.week{background-color:#f0f8ff;text-align:center;}
            td.day{background-color:#f5f5f5;text-align:right;font-size:0.75em;}
            td.otherday{background-color:#f5f5f5;color:#d3d3d3;text-align:right;font-size:0.75em;}
            td.sche{background-color:#fffffff;text-align:left;height:80px;}
            img{border:0px;}
            p{font-size:0.75em;}
        </style>
    </head>
    <body>
        <div align=right>
            ようこそ、<%=user.getUsername()%>さん
            <a href="?action=logout">(ログアウト)</a>
            <a href="?action=withdraw">(退会)</a>
        </div>
        <p><a href="?action=input_rev">収入を入力</a> <a href="?action=input_spe">支出を入力</p>
        <p>
            <a href="?action=show_calendar&year=<%=abc.getYear()%>&month=<%=abc.getMonth() - 1%>">前月</a>
            <%=abc.getYear()%>年
            <%=abc.getMonth() + 1%>月
            <a href="?action=show_calendar&year=<%=abc.getYear()%>&month=<%=abc.getMonth() + 1%>">翌月</a>
        </p>
        <table>
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
                <td class="day"><%=abc.getDayList().get(j).getDay()%>
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
                    <a href="#<%=abc.getYear()%>-<%=abc.getMonth()%>-<%=abc.getDay()%>">
                        <img src="./img/memo.png" width="14" height="16">
                    </a><br>
                    <%
                        if (abc.getDayList().get(j).getRevenue() != -1) {
                    %>
                    <div style="color: green;"><%=abc.getDayList().get(j).getRevenue()%></div>
                    <%
                        }
                        if (abc.getDayList().get(j).getSpending() != -1) {
                    %>
                    <div style="color: red;"><%=abc.getDayList().get(j).getSpending()%></div>
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
    </body>
</html>