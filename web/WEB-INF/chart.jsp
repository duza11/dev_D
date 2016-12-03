<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="utf-8">
        <title>JFreeChartによるクリッカブル・マップの作成</title>
    </head>
    <body>
        ${requestScope['map']}
        <img src="chart.jpg" usemap="#map" border="0" />
    </body>
</html>