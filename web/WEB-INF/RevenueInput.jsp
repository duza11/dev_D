<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>収入額の入力画面</title>
    </head>
    <body>
        <form action="?action=register_rev" method="post">
            <h1>収入額の入力画面</h1>
            <p>日付：<input type="date" name="date" required></p>
            <p>場所：<input type="text" name="place"></p>
            <p>ジャンル：<select name="spending_genre" required>
                    <option value="1">給料</option>
                    <option value="2">懸賞金</option>
                    <option value="3">奨学金</option>
                    <option value="4">ボーナス</option>
                    <option value="5">その他</option>
                </select>
            <p>名前：<input type="text" name="item_name" ></p>
            <p>金額：<input type="number" name="price" min="0" required>円</p>
            <p>数；<input type="number" name="count" min="1" required></p>
            <p><input type="submit" value="送信">
                <input type="reset" value="リセット"></p>
        </form>
    </body>
</html>