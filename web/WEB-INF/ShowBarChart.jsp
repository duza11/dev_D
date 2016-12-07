<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="accountbook.User" scope="session" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div align=right>
            ようこそ、<%=user.getUsername()%>さん
            <a href="?action=logout">(ログアウト)</a>
            <a href="?action=withdraw">(退会)</a>
        </div>
        <h1><img src="barchart.jpg?action=<%=request.getParameter("action")%>&category=<%=request.getParameter("category")%>&date=<%=request.getParameter("date")%>" /></h1>
    </body>
</html>
