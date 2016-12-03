<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="message" class="java.lang.String" scope="request" />

<html>
    <head>
        <title>ログイン/新規登録</title>
    </head>

    <body>
        <div style="width:500; margin:00 auto;">
            <!-- 表示すべきメッセージがある場合は赤字で出力 -->
            <% if (message != null) {%>
            <font color="red"><%= message%></font>
            <br>
            <% }%>
            <h1>ログイン</h1>
            <form method=post action="?action=login">
                <table>
                    <tr>
                        <td>ユーザ名</td>
                        <td><input type=text name="uname" size="16"></td>
                    </tr>
                    <tr>
                        <td>パスワード</td>
                        <td><input type=password name="pass" size="16"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align=right><input type="submit" value="ログイン">
                        </td>
                    </tr>
                </table>
            </form>
            <br>

            <h1>新規登録</h1>
            <form method=post action="?action=registration">
                <table>
                    <tr>
                        <td>ユーザ名</td>
                        <td><input type=text name="uname" size="16"></td>
                    </tr>
                    <tr>
                        <td>パスワード</td>
                        <td><input type=password name="pass" size="16"></td>
                    </tr>
                    <tr>
                        <td>パスワード(確認用)</td>
                        <td><input type=password name="pass2" size="16"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align=right><input type="submit" value="新規登録"></td>
                    </tr>
                </table>   
            </form>
        </div>            
    </body>
</html>