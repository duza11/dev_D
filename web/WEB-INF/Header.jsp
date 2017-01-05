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
                                <li><a href="?action=show_rev_pie">今月の収入</a></li>
                                <li><a href="?action=show_spe_pie">今月の支出</a></li>
                                <li><a href="?action=show_yearly_rev_pie">今年の収入</a></li>
                                <li><a href="?action=show_yearly_spe_pie">今年の支出</a></li>
                                <li role="separator" class="divider"></li>
                                <li class="dropdown-header">棒グラフ</li>
                                <li><a href="?action=show_rev_bar&category=0">今月の収入</a></li>
                                <li><a href="?action=show_spe_bar&category=0">今月の支出</a></li>
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
                                <li><a href="?action=withdraw">退会</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!--/.nav-collapse -->
            </div>
        </nav>