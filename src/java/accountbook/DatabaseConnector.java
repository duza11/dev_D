package accountbook;

import java.util.*;
import java.sql.*;
import java.security.*;

/**
 * データベースと連携し，ユーザ認証や新規登録を担当するクラス
 */
public class DatabaseConnector {

    private static String dbName = ""; // DB名
    private static String dbUserName = ""; // DBのユーザ名
    private static String dbPassword = ""; // DBのパスワード

    private Connection con = null;
    private Statement st = null;
    private PreparedStatement ps = null;

    /**
     * コンストラクタ データベース名，パスワードテーブル名，DBのログイン名， DBログインパスワード名を引数にとる
     */
    public DatabaseConnector(String dbName, String dbUserName, String dbPassword) {
        this.dbName = dbName;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
    }

    /**
     * データベースへ接続する
     */
    public void openConnection() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/" + dbName
                + "?useUnicode=true&characterEncoding=UTF-8" + "&autoReconnect=true";

        con = DriverManager.getConnection(url, dbUserName, dbPassword);
        st = con.createStatement();
    }

    public void openConnection(String sql) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/" + dbName
                + "?useUnicode=true&characterEncoding=UTF-8" + "&autoReconnect=true";

        con = DriverManager.getConnection(url, dbUserName, dbPassword);
        ps = con.prepareStatement(sql);
    }

    /**
     * データベースとの接続を切断し，オブジェクトを破棄する
     */
    public void closeConnection() throws Exception {

        if (ps != null) {
            ps.close();
        }
        if (st != null) {
            st.close();
        }
        if (con != null) {
            con.close();
        }
    }

    /**
     * 接続状態を管理するオブジェクトを返す
     */
    public Statement getStatement() {
        return st;
    }
    
    public PreparedStatement getPreparedStatement() {
        return ps;
    }
}
