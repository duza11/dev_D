<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="kind" type="Map<Integer, String>" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>支出額の入力</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <div class="well">
                <h1>支出額の入力画面</h1>
                <form action="?action=register_spe" method="post">
                    <div class="form-group">
                        <div class="row">
                            <label class="col-md-3 control-label">日付</label>
                        </div>
                        <div class="row">
                            <div class="col-md-3">
                                <input class="form-control" type="date" name="date" pattern="\d{4}-(0?[1-9]|1[0-2])-\d{1,2}" title="日付を入力してください" required>
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
                            <label class="col-md-2 col-xs-3 control-label">種類</label>
                            <label class="col-md-2 col-xs-3 control-label">名前</label>
                            <label class="col-md-2 col-xs-3 control-label">金額</label>
                            <label class="col-md-2 col-xs-2 control-label">数</label>
                        </div>
                        <div class="form-group form-block" id="form_block[0]">
                            <div class="row">
                                <div class="col-md-2 col-xs-3">
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
                                <div class="col-md-2 col-xs-3">
                                    <input class="form-control" type="text" name="item_name[0]" id="item_name[0]" required>
                                </div>
                                <div class="col-md-2 col-xs-3">
                                    <input class="form-control" type="number" name="price[0]" id="price[0]" min="0" required>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input class="form-control" type="number" name="count[0]" id="count[0]" min="1" required>
                                </div>
                                <div class="clone-close col-md-1 col-xs-1">
                                    <div class="close-btn" title="Close"><button type="button" class="btn btn-primary">-</button></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button class="btn btn-primary" type="submit">送信</button>
                    <button class="btn btn-primary" type="reset">リセット</button>
                    <button class="add btn btn-primary" type="button">+</button>
                </form>
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