<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="message" class="java.lang.String" scope="request" />

<html>
    <head>
        <title>ログイン/新規登録</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/top.css">
        <link rel="stylesheet" href="css/theme.css">
    </head>

    <body>
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <!-- 表示すべきメッセージがある場合は赤字で出力 -->
            <% if (message != null) {%>
            <p><font color="red"><%= message%></font></p>
                <% }%>


            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <h1>ログイン</h1>
                    <form class="form-horizontal" method="post" action="?action=login">
                        <div class="form-group">
                            <label class="col-xs-4 control-label">
                                ユーザ名
                            </label>
                            <div class="col-xs-8">
                                <input class="form-control" type="text" name="uname" size="16" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 control-label">
                                パスワード
                            </label>
                            <div class="col-xs-8">
                                <input class="form-control" type="password" name="pass" size="16" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-offset-4 col-xs-8">
                                <button type="submit" class="btn btn-primary">ログイン</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-xs-3"></div>
            </div>

            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <h1>新規登録</h1>
                    <form class="form-horizontal" method="post" action="?action=registration">
                        <div class="form-group">
                            <label class="col-xs-4 control-label">
                                ユーザ名
                            </label>
                            <div class="col-xs-8">
                                <input class="form-control" type="text" name="uname" size="16" required pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 control-label">
                                パスワード
                            </label>
                            <div class="col-xs-8">
                                <input class="form-control" type="password" name="pass" size="16" required  pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 control-label">
                                パスワード（確認用）
                            </label>
                            <div class="col-xs-8">
                                <input class="form-control" type="password" name="pass2" size="16" required  pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-offset-4 col-xs-8">
                                <button type="submit" class="btn btn-primary">新規登録</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>