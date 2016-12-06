<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="kind" type="Map<Integer, String>" scope="request" />
<html lang="ja">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="//code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <title>収入額の入力画面</title>
    </head>
    <body>
        <div align=right>
            ようこそ、<%=user.getUsername()%>さん
            <a href="?action=logout">(ログアウト)</a>
            <a href="?action=withdraw">(退会)</a>
        </div>
        <h1>収入額の入力画面</h1>
        <form action="?action=register_rev" method="post">

            <table>
                <tr>
                    <td colspan="5">日付</td>
                </tr>
                <tr>
                    <td colspan="5"><input type="date" name="date" required></td>
                </tr>
                <tr>
                    <td colspan="5">店名</td>
                </tr>
                <tr>
                    <td colspan="5"><input type="text" name="place"></td>
                </tr>
                <tr>
                    <td>ジャンル</td>
                    <td>名前</td>
                    <td>金額</td>
                    <td>数</td>
                </tr>
                <tr class="form-block" id="form_block[0]">
                    <td>
                        <select name="kind[0]" id="kind[0]" required>
                            <%
                                for (Map.Entry<Integer, String> e : kind.entrySet()) {
                            %>
                            <option value="<%=e.getKey()%>"><%=e.getValue()%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                    <td><input type="text" name="item_name[0]" id="item_name[0]"></td>
                    <td><input type="number" name="price[0]" id="price[0]" min="0" required></td>
                    <td><input type="number" name="count[0]" id="count[0]" min="1" required></td>
                    <td><div class="close" title="Close" style="display: none;"><button type="button">-</button></div></td>
                </tr>
            </table>
            <div class="form-block" id="form_add">
                <p>
                    <button class="add" type="button">+</button>
                </p>
            </div>
            <p>
                <input type="submit" value="送信">
                <input type="reset" value="リセット">
            </p>
        </form>
    </body>
</html>