
import accountbook.*;
import java.awt.Color;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private static String DB_PASS = "root";                  // DBのパスワード

    // ログインのビューを担当
    private static String LOGIN_JSP = "/WEB-INF/Login.jsp";
    // トップページのビューを担当
    private static String SHOW_CALENDAR_JSP = "/WEB-INF/ShowCalendar.jsp";
    // 収入の入力のビューを担当
    private static String INPUT_REVENUE_JSP = "/WEB-INF/InputRevenue.jsp";
    // 支出の入力のビューを担当
    private static String INPUT_SPENDING_JSP = "/WEB-INF/InputSpending.jsp";

    private static String INPUT_REVENUE_UPDATE_JSP = "WEB-INF/InputRevenueUpdate.jsp";

    private static String INPUT_SPENDING_UPDATE_JSP = "WEB-INF/InputSpendingUpdate.jsp";

    // 収入の円グラフのビューを担当
    private static String SHOW_MONTHLY_PIE_CHART_JSP = "/WEB-INF/ShowMonthlyPieChart.jsp";
    // 棒グラフのビューを担当
    private static String SHOW_MONTHLY_BAR_CHART_JSP = "/WEB-INF/ShowMonthlyBarChart.jsp";

    private static String SHOW_YEARLY_BAR_CHART_JSP = "/WEB-INF/ShowYearlyBarChart.jsp";

    private static String SHOW_YEARLY_PIE_CHART_JSP = "/WEB-INF/ShowYearlyPieChart.jsp";

    private static String SHOW_DAILY_DATA_JSP = "/WEB-INF/ShowDailyData.jsp";

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
            if (user == null) {
                if (action.equals("") || action.equals("loginPage")) {
                    nextView = LOGIN_JSP; // ログイン用のページを表示
                } else if (action.equals("login")) {
                    // 認証の処理を実行
                    if (login(um, req)) {
                        user = (User) session.getAttribute("user");
                        setDateArray(rm, sm, user, req);
                        nextView = SHOW_CALENDAR_JSP;
//                    nextView = showItems(im, req); // ログイン成功
                    } else {
                        nextView = LOGIN_JSP; // ログイン失敗
                    }

                } else if (action.equals("registration")) {
                    // 新規ユーザの登録処理を実行
                    nextView = registration(um, req);

                } else {
                    /*
		     * ユーザオブジェクトが無い場合はログインしていないと判断し，
		     * 強制的にログインページへ遷移
                     */
                    req.setAttribute("error", "先にログインしてください");
                    nextView = LOGIN_JSP;
                }
            } else if (action.equals("logout")) {
                // ログアウトの処理を実行
                nextView = logout(req);

            } else if (action.equals("show_calendar") || action.equals("")) {
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
                // 商品一覧を表示
//                    nextView = showItems(im, req);
            } else if (action.equals("show_monthly_rev_pie")) {
                nextView = createMonthlyPieChart(user, rm, sm, req);
            } else if (action.equals("show_monthly_spe_pie")) {
                nextView = createMonthlyPieChart(user, rm, sm, req);
            } else if (action.equals("show_yearly_rev_pie")) {
                nextView = createYearlyPieChart(user, rm, sm, req);
            } else if (action.equals("show_yearly_spe_pie")) {
                nextView = createYearlyPieChart(user, rm, sm, req);
            } else if (action.equals("show_monthly_rev_bar")) {
                nextView = createMonthlyBarChart(user, rm, sm, req);
            } else if (action.equals("show_monthly_spe_bar")) {
                nextView = createMonthlyBarChart(user, rm, sm, req);
            } else if (action.equals("show_yearly_rev_bar")) {
                nextView = createYearlyBarChart(user, rm, sm, req);
            } else if (action.equals("show_yearly_spe_bar")) {
                nextView = createYearlyBarChart(user, rm, sm, req);
            } else if (action.equals("input_rev")) {
                setRevenueItemKindMap(rm, req);
                nextView = INPUT_REVENUE_JSP;
            } else if (action.equals("input_spe")) {
                setSpendingItemKindMap(sm, req);
                nextView = INPUT_SPENDING_JSP;
            } else if (action.equals("input_rev_u")) {
                setRevenueItemKindMap(rm, req);
                nextView = getRevenueBlock(user, rm, sm, req);
            } else if (action.equals("input_spe_u")) {
                setSpendingItemKindMap(sm, req);
                nextView = getSpendingBlock(user, rm, sm, req);
            } else if (action.equals("register_rev")) {
                registerRevenue(user, rm, req, 0);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("register_spe")) {
                registerSpending(user, sm, req, 0);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("update_rev")) {
                updateRevenue(user, rm, req);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("update_spe")) {
                updateSpending(user, sm, req);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("delete_rev")) {
                deleteRevenue(user, rm, req);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("delete_spe")) {
                deleteSpending(user, sm, req);
                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            } else if (action.equals("withdraw")) {
                nextView = withdraw(user, um, req);
            } else if (action.equals("show_daily")) {
                nextView = setDailyData(rm, sm, user, req);
            } else {

                // 要求に該当する処理が無い場合
                nextView = "";
            }

            if (nextView.equals("")) {
                // actionパラメータの指定が無い，または不明な処理が要求された場合
                req.setAttribute("error", "不正なアクションが要求されました("
                        + req.getParameter("action") + ")");

                setDateArray(rm, sm, user, req);
                nextView = SHOW_CALENDAR_JSP;
            }

            dc.closeConnection(); // データベースへの接続を切断

        } catch (Exception e) {
            // 例外の詳細を/usr/tomcat/logs/catalina.outへ出力
            // 問題が発生した際に参考にすると良い
            e.printStackTrace();
            req.setAttribute("error", "例外が発生しました:" + e.toString());
            User user = (User) req.getSession(true).getAttribute("user");
            if (user == null) {
                nextView = LOGIN_JSP;
            } else {
                try {
                    setDateArray(rm, sm, user, req);
                    nextView = SHOW_CALENDAR_JSP;
                } catch(Exception ex) {
                    req.getSession(true).invalidate();
                    e.printStackTrace();
                    nextView = LOGIN_JSP;
                }
            }
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
            req.setAttribute("error", "記入漏れがあります");
            return false;
        } else if (um.authenticate(userName, password) == false) {
            req.setAttribute("error", "ユーザ名またはパスワードが違います");
            return false;
        } else {
            // ログインに成功した場合
            HttpSession session = req.getSession(true);

            // セッションにユーザ名と新しいユーザオブジェクトをセットする
            User user = um.getUser(userName);
            session.setAttribute("user", user);

            req.setAttribute("success", "認証に成功しました");
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
            req.setAttribute("error", "記入漏れがあります");
        } else if (!password.equals(password2)) {
            req.setAttribute("error", "パスワードが確認用と一致しません");
        } else if (!userName.matches("^([a-zA-Z0-9]{6,})$") || !password.matches("^([a-zA-Z0-9]{6,})$") || !password2.matches("^([a-zA-Z0-9]{6,})$")) {
            req.setAttribute("error", "半角英数字で6文字以上入力してください");
        } else if (um.registration(userName, password)) {
            req.setAttribute("success", "登録に成功しました");
        } else {
            req.setAttribute("error", "すでに登録されています");
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
            req.setAttribute("success", "ログアウトしました");
        }

        // ログイン画面へ移動させる
        return LOGIN_JSP;
    }

    private String withdraw(User user, UserManager um, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession(true);
        int userId = user.getUserId();
        um.withdraw(userId);
        session.invalidate();
        req.setAttribute("success", "退会しました");
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
        } else if (!isDate(param, "yyyy")) {
            year = -999;
            req.setAttribute("error", "不正なパラメータです");
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
        } else if (!isDate(param, "MM", "M")) {
            month = -999;
            req.setAttribute("error", "不正なパラメータです");
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
            month = (month + 11) % 12;
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

    private void registerRevenue(User user, RevenueManager rm, HttpServletRequest req, int blockId) throws Exception {
        RevenueBlock rb = new RevenueBlock();
        if (!isDate(req.getParameter("date"), "yyyy-MM-dd", "yyyy-M-dd", "yyyy-MM-d", "yyyy-M-d")) {
            req.setAttribute("error", "不正なパラメータです");
            return;
        }
        rb.setDate(req.getParameter("date"));
        rb.setPlace(req.getParameter("place"));

        List<RevenueItem> revenueItemList = new ArrayList<RevenueItem>();

        for (int i = 0; req.getParameter("kind[" + i + "]") != null; i++) {
            String name = req.getParameter("item_name[" + i + "]");
            int kind = Integer.parseInt(req.getParameter("kind[" + i + "]"));
            int price = Integer.parseInt(req.getParameter("price[" + i + "]"));
            int count = Integer.parseInt(req.getParameter("count[" + i + "]"));
            if (kind < 1 || kind > rm.getRevenueKindMap().size() || price < 0 || count < 1) {
                req.setAttribute("error", "不正なパラメータです");
                return;
            }
            RevenueItem ri = new RevenueItem();
            ri.setItemName(name);
            ri.setKindId(kind);
            ri.setPrice(price);
            ri.setCount(count);
            revenueItemList.add(ri);
        }

        rb.setRevenueItemList(revenueItemList);

        rm.registerRevenueBlock(user.getUserId(), blockId, rb);
        req.setAttribute("success", "登録が完了しました");
    }

    private void registerSpending(User user, SpendingManager sm, HttpServletRequest req, int blockId) throws Exception {
        SpendingBlock sb = new SpendingBlock();
        if (!isDate(req.getParameter("date"), "yyyy-MM-dd", "yyyy-M-dd", "yyyy-MM-d", "yyyy-M-d")) {
            req.setAttribute("error", "不正なパラメータです");
            return;
        }
        sb.setDate(req.getParameter("date"));
        sb.setPlace(req.getParameter("place"));

        List<SpendingItem> spendingItemList = new ArrayList<SpendingItem>();

        for (int i = 0; req.getParameter("kind[" + i + "]") != null; i++) {
            String name = req.getParameter("item_name[" + i + "]");
            int kind = Integer.parseInt(req.getParameter("kind[" + i + "]"));
            int price = Integer.parseInt(req.getParameter("price[" + i + "]"));
            int count = Integer.parseInt(req.getParameter("count[" + i + "]"));
            if (kind < 1 || kind > sm.getSpendingKindMap().size() || price < 0 || count < 1) {
                req.setAttribute("error", "不正なパラメータです");
                return;
            }
            SpendingItem si = new SpendingItem();
            si.setItemName(name);
            si.setKindId(kind);
            si.setPrice(price);
            si.setCount(count);
            spendingItemList.add(si);
        }

        sb.setSpendingItemList(spendingItemList);

        sm.registerSpendingBlock(user.getUserId(), blockId, sb);
        req.setAttribute("success", "登録が完了しました");
    }

    private String getRevenueBlock(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        int blockId = Integer.parseInt(req.getParameter("block_id"));
        RevenueBlock rb = rm.getRevenueBlock(user.getUserId(), blockId);

        if (rb == null) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        req.setAttribute("rb", rb);
        return INPUT_REVENUE_UPDATE_JSP;
    }

    private String getSpendingBlock(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        int blockId = Integer.parseInt(req.getParameter("block_id"));
        SpendingBlock sb = sm.getSpendingBlock(user.getUserId(), blockId);

        if (sb == null) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        req.setAttribute("sb", sb);
        return INPUT_SPENDING_UPDATE_JSP;
    }

    private boolean deleteRevenue(User user, RevenueManager rm, HttpServletRequest req) throws Exception {
        int blockId = Integer.parseInt(req.getParameter("block_id"));
        if (rm.deleteRevenue(user.getUserId(), blockId)) {
            req.setAttribute("success", "削除が完了しました");
            return true;
        }
        req.setAttribute("error", "不正な操作です");
        return false;
    }

    private boolean deleteSpending(User user, SpendingManager sm, HttpServletRequest req) throws Exception {
        int blockId = Integer.parseInt(req.getParameter("block_id"));
        if (sm.deleteSpending(user.getUserId(), blockId)) {
            req.setAttribute("success", "削除が完了しました");
            return true;
        }
        req.setAttribute("error", "不正な操作です");
        return false;
    }

    private void updateRevenue(User user, RevenueManager rm, HttpServletRequest req) throws Exception {
        if (deleteRevenue(user, rm, req)) {
            registerRevenue(user, rm, req, Integer.parseInt(req.getParameter("block_id")));
            req.setAttribute("success", "更新が完了しました");
        }
    }

    private void updateSpending(User user, SpendingManager sm, HttpServletRequest req) throws Exception {
        if (deleteSpending(user, sm, req)) {
            registerSpending(user, sm, req, Integer.parseInt(req.getParameter("block_id")));
            req.setAttribute("success", "更新が完了しました");
        }
    }

    private String createMonthlyPieChart(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        List<PieChartItem> pieChartItemList = null;

        String date = req.getParameter("date");

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            date = sdf.format(d);
        }

        if (!isDate(date, "yyyy-MM", "yyyy-M")) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        if (req.getParameter("action").equals("show_monthly_rev_pie")) {
            pieChartItemList = rm.getPieChartItemList(user, date);
        } else if (req.getParameter("action").equals("show_monthly_spe_pie")) {
            pieChartItemList = sm.getPieChartItemList(user, date);
        }

        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        DefaultPieDataset objDpd = new DefaultPieDataset();
        for (PieChartItem pci : pieChartItemList) {
            objDpd.setValue(Integer.toString(pci.getKindId()), pci.getPrice());
        }
        JFreeChart objCht = ChartFactory.createPieChart3D("", objDpd, true, true, true);
        objCht.setBackgroundPaint(Color.WHITE);
        // クリッカブル・マップ用のリンクを生成
        PiePlot objPp = (PiePlot) objCht.getPlot();
        objPp.setURLGenerator(new StandardPieURLGenerator("?action=" + (req.getParameter("action").equals("show_monthly_rev_pie") ? "show_monthly_rev_bar" : "show_monthly_spe_bar") + "&date=" + date));
        // マップ用に生成された画像を保存するためにダミー・ファイルを生成
        File objFl = File.createTempFile("tips", ".jpg");

        objFl.deleteOnExit();
        // イメージを生成
        ChartRenderingInfo objCri = new ChartRenderingInfo(new StandardEntityCollection());
        ChartUtilities.saveChartAsJPEG(objFl, objCht, 600, 400, objCri);
        // リクエスト属性"map"に<map>タグを含む文字列データをセット
        req.setAttribute("map", ChartUtilities.getImageMap("map", objCri));
//            this.getServletContext().getRequestDispatcher("/chart.jsp").forward(req, response);
        req.setAttribute("date", date);
        return SHOW_MONTHLY_PIE_CHART_JSP;
    }

    private String createYearlyPieChart(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        List<PieChartItem> pieChartItemList = null;

        String date = req.getParameter("date");

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.format(d);
        }

        if (!isDate(date, "yyyy")) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        if (req.getParameter("action").equals("show_yearly_rev_pie")) {
            pieChartItemList = rm.getYearlyPieChartItemList(user, date);
        } else if (req.getParameter("action").equals("show_yearly_spe_pie")) {
            pieChartItemList = sm.getYearlyPieChartItemList(user, date);
        }

        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        DefaultPieDataset objDpd = new DefaultPieDataset();
        for (PieChartItem pci : pieChartItemList) {
            objDpd.setValue(Integer.toString(pci.getKindId()), pci.getPrice());
        }
        JFreeChart objCht = ChartFactory.createPieChart3D("", objDpd, true, true, true);
        objCht.setBackgroundPaint(Color.WHITE);
        // クリッカブル・マップ用のリンクを生成
        PiePlot objPp = (PiePlot) objCht.getPlot();
        objPp.setURLGenerator(new StandardPieURLGenerator("?action=" + (req.getParameter("action").equals("show_yearly_rev_pie") ? "show_yearly_rev_bar" : "show_yearly_spe_bar") + "&date=" + date));
        // マップ用に生成された画像を保存するためにダミー・ファイルを生成
        File objFl = File.createTempFile("tips", ".jpg");

        objFl.deleteOnExit();
        // イメージを生成
        ChartRenderingInfo objCri = new ChartRenderingInfo(new StandardEntityCollection());
        ChartUtilities.saveChartAsJPEG(objFl, objCht, 600, 400, objCri);
        // リクエスト属性"map"に<map>タグを含む文字列データをセット
        req.setAttribute("map", ChartUtilities.getImageMap("map", objCri));
//            this.getServletContext().getRequestDispatcher("/chart.jsp").forward(req, response);
        req.setAttribute("date", date);
        return SHOW_YEARLY_PIE_CHART_JSP;
    }

    private String createMonthlyBarChart(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        String date = req.getParameter("date");Map<Integer, String> itemKindMap = null;

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            date = sdf.format(d);
        }
        
        if (req.getParameter("action").equals("show_monthly_rev_bar")) {
            itemKindMap = rm.getRevenueKindMap();
        } else {
            itemKindMap = sm.getSpendingKindMap();
        }
        
        int category = Integer.parseInt(req.getParameter("category"));

        if (!isDate(date, "yyyy-MM", "yyyy-M") || category < 0 || category > itemKindMap.size()) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        req.setAttribute("date", date);
        req.setAttribute("category", itemKindMap);
        return SHOW_MONTHLY_BAR_CHART_JSP;
    }

    private String createYearlyBarChart(User user, RevenueManager rm, SpendingManager sm, HttpServletRequest req) throws Exception {
        String date = req.getParameter("date");

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.format(d);
        }
        
        Map<Integer, String> itemKindMap = null;

        if (req.getParameter("action").equals("show_yearly_rev_bar")) {
            itemKindMap = rm.getRevenueKindMap();
        } else {
            itemKindMap = sm.getSpendingKindMap();
        }
        
        int category = Integer.parseInt(req.getParameter("category"));

        if (!isDate(date, "yyyy") || category < 0 || category > itemKindMap.size()) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }

        req.setAttribute("date", date);
        req.setAttribute("category", itemKindMap);
        return SHOW_YEARLY_BAR_CHART_JSP;
    }

    private String setDailyData(RevenueManager rm, SpendingManager sm, User user, HttpServletRequest req) throws Exception {
        String date = req.getParameter("date");
        if (!isDate(date, "yyyy-MM-dd", "yyyy-M-dd", "yyyy-MM-d", "yyyy-M-d")) {
            req.setAttribute("error", "不正なパラメータです");
            setDateArray(rm, sm, user, req);
            return SHOW_CALENDAR_JSP;
        }
        List<RevenueBlock> rList = rm.setDailyDataSet(user, date);
        List<SpendingBlock> sList = sm.setDailyDataSet(user, date);
        req.setAttribute("rList", rList);
        req.setAttribute("sList", sList);
        return SHOW_DAILY_DATA_JSP;
    }

    private boolean isDate(String date, String... formats) {
        for (String f : formats) {
            try {
                SimpleDateFormat d = new SimpleDateFormat(f);
                d.setLenient(false);
                Date result = d.parse(date);
                if (date.equals(d.format(result))) {
                    return true;
                }
            } catch (ParseException e) {
                continue;
            }
        }
        return false;
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
