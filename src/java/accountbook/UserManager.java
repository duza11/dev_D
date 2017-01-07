package accountbook;

import java.sql.*;
import java.util.*;
import java.security.*;
import java.math.BigInteger;

/**
 * データベースと連携し，ユーザ認証や新規登録を担当するクラス
 */
public class UserManager {

    private Statement st = null;
    private ResultSet rs = null;

    /**
     * コンストラクタ データベース名，パスワードテーブル名，DBのログイン名 DBログインパスワード名を引数にとる
     */
    public UserManager(DatabaseConnector dc) {
        st = dc.getStatement();
    }

    /**
     * ユーザを認証する．認証された場合はtrueを，パスワードが異なっている， ユーザが存在しない，3回以上連続で認証に失敗したことでアカウントが
     * 無効の場合はfalseを返す
     */
    public boolean authenticate(String username, String password) throws Exception {
        boolean ret = false;
        String sql = "select * from users where username='" + username + "'";
        rs = st.executeQuery(sql);

        if (!rs.next()) {
            return false; // 指定されたユーザ名が存在しない
        }

        String dbpass = rs.getString("password");
        String md5pass = md5(password);

        // 入力されたパスワードとDBに記録されたものを比較
        if (dbpass.equals(md5pass)) {
            ret = true; // 認証成功
        } else {
            ret = false; // 認証失敗 (パスワードが異なる or アカウントが無効)
        }
        return ret;
    }

    /**
     * 新規ユーザの登録を行う． 既に同名のユーザが登録されているときはfalseを ，登録が成功した場合はtrueを返す
     */
    public boolean registration(String username, String password) throws Exception {
        String sql = "select password from users where username='" + username + "'";
        rs = st.executeQuery(sql);

        if (rs.next()) {
            return false;
        }
        rs.close();

        String md5pass = md5(password);
        sql = "insert into users (username, password) values ('" + username + "', '" + md5pass + "')";

        st.executeUpdate(sql);
        return true;
    }

    public void withdraw(int userId) throws Exception {
        String sql = "delete from users where user_id='" + userId + "'";
        st.executeUpdate(sql);
    }
    
    public User getUser(String userName) throws Exception {
        String sql = "select user_id, username from users where username='" + userName + "'";
        rs = st.executeQuery(sql);
        
        if(rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            return user;
        }
        return null;
    }

    /**
     * 引数で指定された文字列に対して， MD5でハッシュ化されたハッシュ値を返す
     */
    private static String md5(String password) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        String digest = new BigInteger(1, md5.digest()).toString(16);
        char[] buf = new char[32];
        Arrays.fill(buf, '0');
        System.arraycopy(digest.toCharArray(), 0, buf, buf.length - digest.length(), digest.length());
        return new String(buf);
    }
}