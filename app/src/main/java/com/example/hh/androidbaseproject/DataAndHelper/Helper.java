package com.example.hh.androidbaseproject.DataAndHelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.hh.androidbaseproject.App;
import com.example.hh.androidbaseproject.R;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by hh on 16/7/20.
 */
public class Helper {
    public interface IFilter<T> {
        boolean filter(T o);
    }

    public interface IMap<T, K> {
        K map(T o);
    }

    public interface IReduce<P,T> {
        P reduce(P o, T next);
    }

    public interface IForEach<T> {
        void forEachDo(T o);
    }

    public interface IForEachIdx<T> {
        void forEachDo(T o, int idx);
    }

    public interface IBool<T> {
        boolean isOK(T o);
    }

    public interface IFilterThenMap<T, K> {
        boolean filter(T o);

        K map(T o);
    }

    public interface IFilterThenForEach<T> {
        boolean filter(T o);

        void forEachDo(T o);
    }

    public interface IURLExist {
        void isExist(boolean exist);
    }

    public static <T> List<T> filter(List<T> array, IFilter<T> fun) {
        List<T> res = new ArrayList();
        for (T obj :
                array) {
            if (fun.filter(obj)) {
                res.add(obj);
            }
        }
        return res;
    }

    public static <T, K> List<K> map(List<T> array, IMap<T, K> fun) {
        List<K> res = new ArrayList<>();
        for (T obj :
                array) {
            res.add(fun.map(obj));
        }
        return res;
    }

    public static <T, K> List<K> filterThenMap(List<T> array, IFilterThenMap<T, K> fun) {
        List<K> res = new ArrayList<>();
        for (T obj :
                array) {
            if (fun.filter(obj)) {
                res.add(fun.map(obj));
            }
        }
        return res;
    }

    public static <T> void filterThenForEachDo(List<T> array, IFilterThenForEach<T> fun) {
        for (T obj :
                array) {
            if (fun.filter(obj)) {
                fun.forEachDo(obj);
            }
        }
    }

    public static <P,T> P reduce(List<T> array, P initValue, IReduce<P,T> fun) {
        P res = initValue;
        for (T obj :
                array) {
            res = fun.reduce(res, obj);
        }
        return res;
    }

    public static <T> List<T> uniq(List<T> array) {
        List<T> res = new ArrayList<>();
        for (T obj :
                array) {
            if (!res.contains(obj)) {
                res.add(obj);
            }
        }
        return res;
    }

    public static <T> void forEachDo(List<T> array, IForEach<T> fun) {
        for (T obj :
                array) {
            fun.forEachDo(obj);
        }
    }

    public static <T> void forEachDo(List<T> array, IForEachIdx<T> fun) {
        int i = 0;
        for (T obj :
                array) {
            fun.forEachDo(obj, i++);
        }
    }

    public static <T> boolean some(List<T> array, IBool<T> fun) {
        if (array == null){
            return false;
        }
        for (T obj :
                array) {
            if (fun.isOK(obj)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean every(List<T> array, IBool<T> fun) {
        if (array == null){
            return false;
        }
        for (T obj :
                array) {
            if (!fun.isOK(obj)) {
                return false;
            }
        }
        return true;
    }

    public static <T> int index(List<T> array, IBool<T> fun) {
        for (int i = 0; i < array.size(); i++) {
            if (fun.isOK(array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////----以下是功能方法

    public static List<String> split(String string, String sepa) {
        String[] list = string.split(sepa);
        return Arrays.asList(list);
    }

    public static String join(String[] texts, String sepa) {
        return join(Arrays.asList(texts), sepa);
    }

    public static String join(List<String> list, String sepa) {
        String res = "";
        for (String str :
                list) {
            res += str + sepa;
        }
        if (res.length() < sepa.length()) {
            return "";
        }
        return res.substring(0, res.length() - sepa.length());
    }

    public static <T> T def(T obj, T obj2) {
        if (obj == null) {
            return obj2;
        }
        return obj;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取主色
     *
     * @return
     */
    public static int getMainColor() {
        return ContextCompat.getColor(App.getAppContext(), R.color.colorPrimary);
    }

    /**
     * 判断wifi
     *
     * @return
     */
    public static boolean isWifiConnected() {
        return isConnected(App.getAppContext(), ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileConnected() {
        return isConnected(App.getAppContext(), ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnected(@NonNull Context context, int type) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(type);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return isConnected(connMgr, type);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isConnected(@NonNull ConnectivityManager connMgr, int type) {
        Network[] networks = connMgr.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connMgr.getNetworkInfo(mNetwork);
            if (networkInfo != null && networkInfo.getType() == type && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 判断URL是否存在
     *
     * @param URLName
     * @return
     */
    public static void urlExists(final Activity activity, final String URLName, final IURLExist callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
                    HttpURLConnection.setFollowRedirects(false);
                    //到 URL 所引用的远程对象的连接
                    final HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                            .openConnection();
           /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
                    con.setRequestMethod("HEAD");
                    //从 HTTP 响应消息获取状态码
                    final boolean res = con.getResponseCode() == HttpURLConnection.HTTP_OK;
                    Log.e("---urlExists--->", res+":"+URLName);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.isExist(res);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Activity activity) {
        View v = activity.getWindow().getDecorView();
        return v.getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        View v = activity.getWindow().getDecorView();
        return v.getHeight();
    }

    public static boolean isVersionOld(String ver) {
        String curVer = getAppVersionName();
        int curV = Integer.parseInt(curVer.replace(".", ""));
        int v = Integer.parseInt(ver.replace(".", ""));
        Log.e("----ver---->", curV + " <? " + v);
        return curV < v;
    }

    public static String getAppVersionName() {
        String versionName = "";
        Context context = App.getAppContext();
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取获取几天的日期
     *
     * @param days 如果为3，意思是3天前的日期
     * @return
     */
    public static String getDateString(int days) {
        long time = System.currentTimeMillis() - days * 24 * 3600 * 1000L;//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String getDateTime(){
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String getDateTime(String format_){
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(format_);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static boolean isPostProcess(String process) {
        String[] list = {"专色", "其它", "压型", "压折", "压纹", "折页", "烫金", "粘合", "装订", "覆膜", "过UV"};
        for (String str :
                list) {
            if (str.equals(process)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Activity> ProgressBar showProgressBar(T context) {
        ProgressBar bar = new ProgressBar(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        bar.setLayoutParams(params);

        ViewGroup vg = (ViewGroup) context.getWindow().getDecorView();
        vg.addView(bar);
        return bar;
    }

    public static ProgressBar showProgress(Context context, FrameLayout layout) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        ProgressBar bar = new ProgressBar(context);
        bar.setLayoutParams(params);
        layout.addView(bar);
        return bar;
    }

    public static void addToWindow(Activity context, View view) {
        getWindow(context).addView(view);
    }

    public static void removeFormWindow(Activity context, View view) {
        getWindow(context).removeView(view);
    }

    public static <T extends Activity> ViewGroup getWindow(T context) {
        return (ViewGroup) context.getWindow().getDecorView();
    }

    public static <T extends Activity> void showPrice(T context, String price) {
        int index = price.indexOf("计算结果");
        index += 5;
        SpannableStringBuilder builder = new SpannableStringBuilder(price);
        if (price.contains("计算结果")) {
            builder.setSpan(new ForegroundColorSpan(Color.RED), index, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {

        }
        final Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle("报价结果").
                setMessage(builder).
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    public static <T extends Activity> void alert(T context, String title, String content) {
        final Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(content).
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    public static String getPriceInputTitle(String title) {
        title = title.replace(" ", "");
        title = title.replace("：", "");
        return title;
    }

    public static <T extends Activity> void closeKeyboard(T context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 分享功能
     *
     * @param context       上下文
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public static void shareMsg(Context context, String activityTitle, String msgTitle, String msgText,
                                String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

}
