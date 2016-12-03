<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="//code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="js/script.js"></script>
        <title>支出額の入力画面</title>
    </head>
    <body>
        <h1>支出額の入力画面</h1>
        <form action="?action=register_spe" method="post">
            <p>日付：<input type="date" name="date" required></p>
            <p>場所：<input type="text" name="place"></p>
            <div class="form-block" id="form_block[0]">
                <div class="close" title="Close" style="display: none;"><button type="button">-</button></div>
                <p>ジャンル：<select name="kind[0]" id="kind[0]" required>
                        <option value="1">医療費</option>
                        <option value="2">教育費</option>
                        <option value="3">交際費</option>
                        <option value="4">交通費</option>
                        <option value="5">在宅費</option>
                        <option value="6">食費</option>
                        <option value="7">水道光熱費</option>
                        <option value="8">生活雑貨・日用品</option>
                        <option value="9">税金</option>
                        <option value="10">通信費</option>
                        <option value="11">被服費</option>
                        <option value="12">美容費</option>
                        <option value="13">ペット</option>
                        <option value="14">保険</option>
                        <option value="15">遊興費</option>
                        <option value="16">その他</option>
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