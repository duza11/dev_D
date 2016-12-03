<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="//code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <title>収入額の入力画面</title>
    </head>
    <body>
        <h1>収入額の入力画面</h1>
        <form action="?action=register_rev" method="post">
            <p>日付：<input type="date" name="date" required></p>
            <p>場所：<input type="text" name="place"></p>
            <div class="form-block" id="form_block[0]">
                <div class="close" title="Close" style="display: none;"><button type="button">-</button></div>
                <p>ジャンル：<select name="kind[0]" id="kind[0]" required>
                        <option value="1">給料</option>
                        <option value="2">懸賞金</option>
                        <option value="3">奨学金</option>
                        <option value="4">ボーナス</option>
                        <option value="5">その他</option>
                    </select>
                <p>名前：<input type="text" name="item_name[0]" id="item_name[0]"></p>
                <p>金額：<input type="number" name="price[0]" id="price[0]" min="0" required>円</p>
                <p>数：<input type="number" name="count[0]" id="count[0]" min="1" required></p>
            </div>
            <div class="form-block" id="form_add">
                <button class="add" type="button">+</button>
            </div>
            <p><input type="submit" value="送信">
                <input type="reset" value="リセット"></p>
        </form>
    </body>
</html>