<%@page import="accountbook.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
        <!-- Fixed navbar -->
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="?">家計簿</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <%
                        if (session.getAttribute("user") == null) {
                    %>
                    <ul class="nav navbar-nav">
                        <li class="nav-item">
                            <a href="#loginModal" data-toggle="modal" data-target="#loginModal">ログイン</a>
                        </li>
                        <li class="nav-item">
                            <a href="#registrationModal" data-toggle="modal" data-target="#registrationModal">新規登録</a>
                        </li>
                    </ul>
                    <%
                    } else {
                    %>
                    <ul class="nav navbar-nav">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">入力<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="?action=input_rev">収入</a></li>
                                <li><a href="?action=input_spe">支出</a></li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">分析<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li class="dropdown-header">円グラフ</li>
                                <li><a href="?action=show_monthly_rev_pie">今月の収入</a></li>
                                <li><a href="?action=show_monthly_spe_pie">今月の支出</a></li>
                                <li><a href="?action=show_yearly_rev_pie">今年の収入</a></li>
                                <li><a href="?action=show_yearly_spe_pie">今年の支出</a></li>
                                <li role="separator" class="divider"></li>
                                <li class="dropdown-header">棒グラフ</li>
                                <li><a href="?action=show_monthly_rev_bar&category=0">今月の収入</a></li>
                                <li><a href="?action=show_monthly_spe_bar&category=0">今月の支出</a></li>
                                <li><a href="?action=show_yearly_rev_bar&category=0">今年の収入</a></li>
                                <li><a href="?action=show_yearly_spe_bar&category=0">今年の支出</a></li>
                            </ul>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li></li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">設定<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="?action=logout">ログアウト</a></li>
                                <li><a href="#withdrawModal" data-toggle="modal" data-target="#withdrawModal">退会</a></li>
                            </ul>
                        </li>
                    </ul>
                    <%
                        }
                    %>
                </div>
                <!--/.nav-collapse -->
            </div>
        </nav>
        <%
            if (session.getAttribute("user") == null) {
        %>
        <div id="loginModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4>ログイン</h4>
                    </div>                   
                    <form method="post" action="?action=login">
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="control-label">ユーザ名</label>
                                <input type="text" name="uname" class="form-control" placeholder="ユーザ名" required>
                            </div>
                            <div class="form-group">
                                <label class="control-label">パスワード</label>
                                <input type="password" name="pass" class="form-control" placeholder="パスワード" required>
                            </div>
                            <div class="form-group">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">キャンセル</button>
                            <button id="loginBtn" type="submit" class="btn btn-primary">ログイン</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="registrationModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4>新規登録</h4>
                    </div>                   
                    <form method="post" action="?action=registration">
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="control-label">ユーザ名</label>
                                <input type="text" name="uname" class="form-control" placeholder="ユーザ名" pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。" required>
                            </div>
                            <div class="form-group">
                                <label class="control-label">パスワード</label>
                                <input type="password" name="pass" class="form-control" placeholder="パスワード" pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。" required>
                            </div>
                            <div class="form-group">
                                <label class="control-label">パスワード（確認）</label>
                                <input type="password" name="pass2" class="form-control" placeholder="パスワード（確認）" pattern="^([a-zA-Z0-9]{6,})$" title="英数字6文字以上で入力してください。" required>
                            </div>
                            <div class="form-group">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">キャンセル</button>
                            <button id="registrationBtn" type="submit" class="btn btn-primary">新規登録</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%
        } else {
        %>

        <div id="withdrawModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4>退会</h4>
                    </div>
                    <div class="modal-body">
                        <p class="text-center">すべてのデータが失われますがよろしいですか？</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">キャンセル</button>
                        <a class="btn btn-primary" href="?action=withdraw">退会</a>
                    </div>
                </div>
            </div>
        </div>
        <%
            }
        %>