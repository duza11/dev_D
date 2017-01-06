<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="kind" type="Map<Integer, String>" scope="request" />
<html lang="ja">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <title>支出額の入力画面</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link href="css/theme.css" rel="stylesheet">
    </head>
    <body>

        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <h1>支出額の入力画面</h1>
            
            <form action="?action=register_spe" method="post">
                <div class="form-group">
                    <div class="row">
                        <label class="col-md-3 control-label">日付</label>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <input class="form-control" type="date" name="date" required>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label class="col-md-3 control-label">店名</label>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <input class="form-control" type="text" name="place" required>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label class="col-md-2 control-label">種類</label>
                        <label class="col-md-2 control-label">名前</label>
                        <label class="col-md-2 control-label">金額</label>
                        <label class="col-md-2 control-label">数</label>
                    </div>
                    <div class="form-block" id="form_block[0]">
                        <div class="row">
                            <div class="col-md-2">
                                <select class="form-control" name="kind[0]" id="kind[0]" required>
                                    <%
                                        for (Map.Entry<Integer, String> e : kind.entrySet()) {
                                    %>
                                    <option value="<%=e.getKey()%>"><%=e.getValue()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <input class="form-control" type="text" name="item_name[0]" id="item_name[0]" required>
                            </div>
                            <div class="col-md-2">
                                <input class="form-control" type="number" name="price[0]" id="price[0]" required>
                            </div>
                            <div class="col-md-2">
                                <input class="form-control" type="number" name="count[0]" id="count[0]" required>
                            </div>
                            <div class="clone-close col-md-1">
                                <div class="close" title="Close" style="display: none;"><button type="button" class="btn btn-secondary">-</button></div>
                            </div>
                        </div>
                    </div>
                </div>
                <button class="btn btn-secondary" type="submit">送信</button>
                <button class="btn btn-secondary" type="reset">リセット</button>
                <button class="add btn btn-secondary" type="button">+</button>
            </form>
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>