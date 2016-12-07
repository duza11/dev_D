
import accountbook.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.jfree.chart.*;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;

/**
 * ショッピングサイトへのユーザ認証や商品選択に関する処理を担当するサーブレット
 */
public class AccountBookServlet extends HttpServlet {

    private static String DB_NAME = "account_book";               // DB名
    private static String DB_USER = "root";                  // DBのユーザ名
    private static String DB_PASS = "duza11";                  // DBのパスワード

    // ログインのビューを担当
    private static String LOGIN_JSP = "/WEB-INF/Login.jsp";
    // トップページのビューを担当
    private static String TOP_JSP = "/WEB-INF/Top.jsp";
    // 収入の入力のビューを担当
    private static String INPUT_REVENUE = "/WEB-INF/RevenueInput.jsp";
    // 支出の入力のビューを担当
    private static String INPUT_SPENDING = "/WEB-INF/SpendingInput.jsp";
    // 収入の円グラフのビューを担当
    private static String SHOW_PIE_CHART = "/WEB-INF/ShowPieChart.jsp";
    // 棒グラフのビューを担当
    private static String SHOW_BAR_CHART = "/WEB-INF/ShowBarChart.jsp";

    /**
     * サーブレットがPOSTメソッドでアクセスされた際に呼ばれる．
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // doProcessを呼ぶことでGET/POSTのどちらでアクセスされても同じ処理を実行
        doProcess(req, res);
    }

    /**
     * サーブレットがGETメソッドでアクセスされた際に呼ばれる．
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // doProcessを呼ぶことでGET/POSTのどちらでアクセスされても同じ処理を実行
        doProcess(req, res);
    }

    /**
     * クライアントから要求された処理をactionから判別し実行する
     */
    protected void doProcess(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String nextView = ""; // 処理結果の表示を委託するJSPのパス
        DatabaseConnector dc = null; // データベースへの接続を行うオブジェクト
        UserManager um = null; // ユーザ認証や登録に関する処理を担当
        SpendingManager sm = null; // 支出に関する処理を担当
        RevenueManager rm = null; // 収入に関する処理を担当

        try {
            dc = new DatabaseConnector(DB_NAME, DB_USER, DB_PASS);
            dc.openConnection(); // DBへ接続

            um = new UserManager(dc);
            sm = new SpendingManager(dc);
            rm = new RevenueManager(dc);

            // 前のページから渡される値を「UTF-8」に設定
            req.setCharacterEncoding("UTF-8");
            // これから表示するページのMIMEを設定
            res.setContentType("text/html;charset=UTF-8");

            // アクセスしたユーザのユーザオブジェクトを取得
            HttpSession session = req.getSession(true);
            User user = (User) session.getAttribute("user");

            /*
	     * ユーザからの要求はURLの後方に付加された「？」
	     * 以降にGETメソッドのactionパラメータとして付加される
             */
            String action = req.getParameter("action");
            if (action == null) {
                action = "";
            }

            /**
             * 以下のif文でユーザからの要求を判別し，適切処理を行う
             */
            if (action.equals("") || action.equals("loginPage")) {
                nextView = LOGIN_JSP; // ログイン用のページを表示
            } else if (action.equals("login")) {
                // 認証の処理を実行
                if (login(um, req)) {
                    user = (User) session.getAttribute("user");
                    setDateArray(rm, sm, user, req);
                    nextView = TOP_JSP;
//                    nextView = showItems(im, req); // ログイン成功
                } else {
                    nextView = LOGIN_JSP; // ログイン失敗
                }

            } else if (action.equals("logout")) {
                // ログアウトの処理を実行
                nextView = logout(req);

            } else if (action.equals("registration")) {
                // 新規ユーザの登録処理を実行
                nextView = registration(um, req);

            } else // 以下は先にログインしている必要がある処理
            {
                if (user == null) {
                    /*
		     * カートオブジェクトが無い場合はログインしていないと判断し，
		     * 強制的にログインページへ遷移
                     */
                    req.setAttribute("message", "先にログインしてください");
                    nextView = LOGIN_JSP;

                } else if (action.equals("show_calendar") || action.equals("")) {
                    setDateArray(rm, sm, user, req);
                    nextView = TOP_JSP;
                    // 商品一覧を表示
//                    nextView = showItems(im, req);
                } else if (action.equals("show_rev_pie")) {
                    createPieChart(user, rm, sm, req);
                    nextView = SHOW_PIE_CHART;
                } else if (action.equals("show_spe_pie")) {
                    createPieChart(user, rm ,sm, req);
                    nextView = SHOW_PIE_CHART;
                } else if (action.equals("show_rev_bar")) {
                    nextView = SHOW_BAR_CHART;
                } else if (action.equals("show_spe_bar")) {
                    nextView = SHOW_BAR_CHART;
                } else if (action.equals("input_rev")) {
                    setRevenueItemKindMap(rm, req);
                    nextView = INPUT_REVENUE;
                } else if (action.equals("input_spe")) {
                    setSpendingItemKindMap(sm, req);
                    nextView = INPUT_SPENDING;
                } else if (action.equals("register_rev")) {
                    registerRevenue(user, rm, req);
                    setDateArray(rm, sm, user, req);
                    nextView = TOP_JSP;
                } else if (action.equals("register_spe")) {
                    registerSpending(user, sm, req);
                    setDateArray(rm, sm, user, req);
                    nextView = TOP_JSP;
                } else if (action.equals("withdraw")) {
                    nextView = withdraw(um, req);
                } else {

                    // 要求に該当する処理が無い場合
                    nextView = "";

                }
            }

            if (nextView.equals("")) {
                // actionパラメータの指定が無い，または不明な処理が要求された場合
                req.setAttribute("message", "不正なアクションが要求されました("
                        + req.getParameter("action") + ")");

                nextView = LOGIN_JSP;
            }

            dc.closeConnection(); // データベースへの接続を切断

        } catch (Exception e) {
            // 例外の詳細を/usr/tomcat/logs/catalina.outへ出力
            // 問題が発生した際に参考にすると良い
            e.printStackTrace();
            req.setAttribute("message", "例外が発生しました:" + e.toString());
            nextView = LOGIN_JSP;
        } finally {
            /*
	     * 正常に処理された場合も，エラーの場合もビューとして指定されたJSP
	     * へフォワードし，クライアントに結果を返す
             */
            req.getRequestDispatcher(nextView).forward(req, res);
        }
    }

    /**
     * ユーザが入力したユーザ名とパスワードを検証し，ログインの処理を行う
     */
    private boolean login(UserManager um, HttpServletRequest req)
            throws Exception {
        String userName = req.getParameter("uname"); // ユーザ名
        String password = req.getParameter("pass"); // パスワード

        if (!isValid(userName) || !isValid(password)) {
            req.setAttribute("message", "記入漏れがあります");
            return false;
        } else if (um.authenticate(userName, password) == false) {
            req.setAttribute("message", "ユーザ名またはパスワードが違います");
            return false;
        } else {
            // ログインに成功した場合
            HttpSession session = req.getSession(true);

            // セッションにユーザ名と新しいユーザオブジェクトをセットする
            User user = um.getUser(userName);
            session.setAttribute("user", user);

            req.setAttribute("message", "認証に成功しました");
        }

        return true;
    }

    /**
     * 新規ユーザの登録処理を行う
     */
    private String registration(UserManager um, HttpServletRequest req)
            throws Exception {
        String userName = req.getParameter("uname"); // ユーザ名
        String password = req.getParameter("pass"); // パスワード
        String password2 = req.getParameter("pass2"); // パスワード（確認）

        if (!isValid(userName) || !isValid(password) || !isValid(password2)) {
            req.setAttribute("message", "記入漏れがあります");
        } else if (!password.equals(password2)) {
            req.setAttribute("message", "パスワードが確認用と一致しません");
        } else if (um.registration(userName, password)) {
            req.setAttribute("message", "登録に成功しました");
        } else {
            req.setAttribute("message", "すでに登録されています");
        }

        return LOGIN_JSP;
    }

    /**
     * セッションを無効化し，ログアウトの処理を行う
     */
    private String logout(HttpServletRequest req) {

        HttpSession session = req.getSession(false); // セッションを取得
        if (session != null) {
            // セッションの無効化
            // (D) セッションを無効化するための記述を追加し，下記のreq.setAttributeを適切なものに置き換える
            session.invalidate();
            req.setAttribute("message", "ログアウトしました");
        }

        // ログイン画面へ移動させる
        return LOGIN_JSP;
    }

    private String withdraw(UserManager um, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession(true);
        String userName = (String) session.getAttribute("userName");
        um.withdraw(userName);
        session.invalidate();
        req.setAttribute("message", "退会しました");
        return LOGIN_JSP;
    }

    private void setDateArray(RevenueManager rm, SpendingManager sm, User user, HttpServletRequest req) throws Exception {
        Calendar calendar = Calendar.getInstance();

        AccountBookCalendar abc = new AccountBookCalendar();
        int year;
        int month;
        int day = calendar.get(Calendar.DATE);

        String param = req.getParameter("year");
        if (!isValid(param)) {
            year = -999;
        } else {
            try {
                year = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                year = -999;
            }
        }

        param = req.getParameter("month");
        if (!isValid(param)) {
            month = -999;
        } else {
            try {
                month = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                month = -999;
            }
        }

        if (year == -999 || month == -999) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
        } else {
            if (month == 12) {
                month = 0;
                year++;
            }
            if (month == -1) {
                month = 11;
                year--;
            }
        }

        calendar.set(year, month, 1);
        int startWeek = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.set(year, month, 0);
        int beforeMonthLastDay = calendar.get(Calendar.DATE);

        calendar.set(year, month + 1, 0);
        int thisMonthLastDay = calendar.get(Calendar.DATE);

        List<DailyData> dayList = new ArrayList<DailyData>();

        for (int i = startWeek - 2; i >= 0; i--) {
            dayList.add(new DailyData(beforeMonthLastDay - i + 35));
        }

        for (int i = 1; i <= thisMonthLastDay; i++) {
            dayList.add(new DailyData(i));
        }

        int nextMonthDay = 1;
        while (dayList.size() % 7 != 0) {
            dayList.add(new DailyData(35 + nextMonthDay++));
        }

        rm.setDayListRevenue(user, year, month + 1, dayList);
        sm.setDayListSpending(user, year, month + 1, dayList);

        abc.setYear(year);
        abc.setMonth(month);
        abc.setDay(day);
        abc.setDayList(dayList);
        req.setAttribute("abc", abc);
    }

    public void setRevenueItemKindMap(RevenueManager rm, HttpServletRequest req) throws Exception {
        Map<Integer, String> revenueItemKindMap = rm.getRevenueKindMap();
        req.setAttribute("kind", revenueItemKindMap);
    }

    public void setSpendingItemKindMap(SpendingManager sm, HttpServletRequest req) throws Exception {
        Map<Integer, String> spendingItemKindMap = sm.getSpendingKindMap();
        req.setAttribute("kind", spendingItemKindMap);
    }

    private void registerSpending(User user, SpendingManager sm, HttpServletRequest req) throws Exception {
        SpendingBlock sb = new SpendingBlock();
        List<SpendingItem> spendingItemList = new ArrayList<SpendingItem>();

        for (int i = 0; req.getParameter("kind[" + i + "]") != null; i++) {
            SpendingItem si = new SpendingItem();
            si.setItemName(req.getParameter("item_name[" + i + "]"));
            si.setKindId(Integer.parseInt(req.getParameter("kind[" + i + "]")));
            si.setPrice(Integer.parseInt(req.getParameter("price[" + i + "]")));
            si.setCount(Integer.parseInt(req.getParameter("count[" + i + "]")));
            spendingItemList.add(si);
        }

        sb.setDate(req.getParameter("date"));
        sb.setPlace(req.getParameter("place"));
        sb.setSpendingItemList(spendingItemList);

        sm.registerSpendingBlock(user, sb);
    }

    private void registerRevenue(User user, RevenueManager rm, HttpServletRequest req) throws Exception {
        RevenueBlock rb = new RevenueBlock();
        List<RevenueItem> revenueItemList = new ArrayList<RevenueItem>();

        for (int i = 0; req.getParameter("kind[" + i + "]") != null; i++) {
            RevenueItem ri = new RevenueItem();
            ri.setItemName(req.getParameter("item_name[" + i + "]"));
            ri.setKindId(Integer.parseInt(req.getParameter("kind[" + i + "]")));
            ri.setPrice(Integer.parseInt(req.getParameter("price[" + i + "]")));
            ri.setCount(Integer.parseInt(req.getParameter("count[" + i + "]")));
            revenueItemList.add(ri);
        }

        rb.setDate(req.getParameter("date"));
        rb.setPlace(req.getParameter("place"));
        rb.setRevenueItemList(revenueItemList);

        rm.registerRevenueBlock(user, rb);
    }

    private void createPieChart(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        try {
            List<PieChartItem> pieChartItemList = null;

            if (req.getParameter("action").equals("show_rev_pie")) {
                pieChartItemList = rm.getPieChartItemList(user, "");
            } else if (req.getParameter("action").equals("show_spe_pie")) {
                pieChartItemList = sm.getPieChartItemList(user, "");
            }
            
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            DefaultPieDataset objDpd = new DefaultPieDataset();
            for (PieChartItem pci : pieChartItemList) {
                objDpd.setValue(Integer.toString(pci.getKindId()), pci.getPrice());
            }
            JFreeChart objCht = ChartFactory.createPieChart3D("サイトアクセスログ", objDpd, true, true, true);
            // クリッカブル・マップ用のリンクを生成
            PiePlot objPp = (PiePlot) objCht.getPlot();
            objPp.setURLGenerator(new StandardPieURLGenerator("?action=" + (req.getParameter("action").equals("show_rev_pie")? "show_rev_bar" : "show_spe_bar") + "&date=2016-12-07"));
            // マップ用に生成された画像を保存するためにダミー・ファイルを生成
            File objFl = File.createTempFile("tips", ".jpg");

            objFl.deleteOnExit();
            // イメージを生成
            ChartRenderingInfo objCri = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.saveChartAsJPEG(objFl, objCht, 600, 400, objCri);
            // リクエスト属性"map"に<map>タグを含む文字列データをセット
            req.setAttribute("map", ChartUtilities.getImageMap("map", objCri));
//            this.getServletContext().getRequestDispatcher("/chart.jsp").forward(req, response);
        } catch (IOException ex) {
            Logger.getLogger(AccountBookServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 引数で与えられた文字列が空でないかを判定する．引数で与えられたstr
     * (文字列)がnullではなく，かつ空でない場合はtrueを，それ以外はfalseを返す．
     */
    protected boolean isValid(String str) {

        if (str != null && !str.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}