<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="accountbook.SpendingItem"%>
<%@page import="accountbook.SpendingBlock"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<jsp:useBean id="sb" class="accountbook.SpendingBlock" scope="request" />
<jsp:useBean id="kind" type="Map<Integer, String>" scope="request" />
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>支出額の更新</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>       
        <%@include file="Header.jsp"%>
        <div class="container theme-showcase" role="main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class="panel-title">収入額の更新</p>
                </div>
                <div class="panel-body">
                    <form class="form-vertical" action="?action=update_spe&block_id=<%=request.getParameter("block_id")%>" method="post">
                        <div class="form-group">
                            <div class="row">
                                <label class="col-md-3 control-label">日付</label>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <input class="form-control" type="date" name="date" value="<%=new SimpleDateFormat("yyyy-MM-dd").format(sb.getDate())%>" pattern="\d{4}-(0?[1-9]|1[0-2])-\d{1,2}" title="yyyy-MM-ddの形式で日付を入力してください" placeholder="日付（yyyy-MM-dd）" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <label class="col-md-3 control-label">店名</label>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <input class="form-control" type="text" name="place" value="<%=sb.getPlace()%>" placeholder="店名" >
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
                            <%                                boolean addFlag = true;
                                List<SpendingItem> siList = sb.getSpendingItemList();
                                for (int i = 0; i < siList.size(); i++) {
                            %>
                            <div class="form-group form-block" <%="id=\"form_block[" + i + "]\""%>>
                                <div class="row">
                                    <div class="col-md-2 col-xs-3">
                                        <select class="form-control" <%="name=\"kind[" + i + "]\" id=\"kind[" + i + "]\""%> required>
                                            <%
                                                for (Map.Entry<Integer, String> e : kind.entrySet()) {
                                            %>
                                            <option value="<%=e.getKey()%>"
                                                    <%
                                                        if (siList.get(i).getKindId() == e.getKey()) {
                                                    %>
                                                    <%="selected"%>
                                                    <%
                                                        }
                                                    %>><%=e.getValue()%></option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                    <div class="col-md-2 col-xs-3">
                                        <input class="form-control" type="text" <%="name=\"item_name[" + i + "]\" id=\"item_name[" + i + "]\""%> value="<%=siList.get(i).getItemName()%>" placeholder="名前">
                                    </div>
                                    <div class="col-md-2 col-xs-3">
                                        <input class="form-control" type="number" <%="name=\"price[" + i + "]\" id=\"price[" + i + "]\""%> value="<%=siList.get(i).getPrice()%>" min="0" placeholder="金額" required>
                                    </div>
                                    <div class="col-md-2 col-xs-2">
                                        <input class="form-control" type="number" <%="name=\"count[" + i + "]\" id=\"count[" + i + "]\""%> value="<%=siList.get(i).getCount()%>" min="1" placeholder="数" required>
                                    </div>
                                    <div class="ac-btn col-md-1 col-xs-1">
                                        <%
                                            if (addFlag) {
                                        %>
                                        <div class="add-btn" title="Add"><button class="add btn btn-default" type="button">+</button></div>
                                        <div class="close-btn" title="Close"><button class="btn btn-default" type="button">-</button></div>
                                        <%
                                            addFlag = false;
                                        } else {
                                        %>
                                        <div class="add-btn" title="Add"><button class="add btn btn-default" type="button" style="display: none;">+</button></div>
                                        <div class="close-btn" title="Close" style="display: block;"><button class="btn btn-default" type="button">-</button></div>
                                        <%
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <button class="btn btn-primary" type="submit">送信</button>
                        <button class="btn btn-default" type="reset">リセット</button>
                    </form>
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