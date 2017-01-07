<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="success" class="java.lang.String" scope="request" />
<jsp:useBean id="error" class="java.lang.String" scope="request" />

<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>ログイン/新規登録</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>

    <body id="bg-ab">
        <%@include file="Header.jsp"%>

        <div class="container theme-showcase" role="main">
            <!-- 表示すべきメッセージがある場合は出力 -->
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

            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <div class="panel bg-black bg-semitrans ft-white">
                        <div class="panel-body">
                            <h1 class="text-center">ログイン</h1>
                            <form class="form-horizontal" method="post" action="?action=login">
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">
                                        ユーザ名
                                    </label>
                                    <div class="col-xs-8">
                                        <input class="form-control" type="text" name="uname" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">
                                        パスワード
                                    </label>
                                    <div class="col-xs-8">
                                        <input class="form-control" type="password" name="pass" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-offset-4 col-xs-8">
                                        <button type="submit" class="btn btn-primary">ログイン</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3"></div>
            </div>

            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <div class="panel bg-black bg-semitrans ft-white">
                        <div class="panel-body">
                            <h1 class="text-center">新規登録</h1>
                            <form class="form-horizontal" method="post" action="?action=registration">
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">
                                        ユーザ名
                                    </label>
                                    <div class="col-xs-8">
                                        <input class="form-control" type="text" name="uname" required pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。" placeholder="ユーザ名">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">
                                        パスワード
                                    </label>
                                    <div class="col-xs-8">
                                        <input class="form-control" type="password" name="pass" required  pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。"placeholder="パスワード">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">
                                        パスワード（確認用）
                                    </label>
                                    <div class="col-xs-8">
                                        <input class="form-control" type="password" name="pass2" required  pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。"placeholder="パスワード（確認）">
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