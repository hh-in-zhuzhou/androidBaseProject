package com.example.hh.androidbaseproject.DataAndHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hh.androidbaseproject.App;
import com.example.hh.androidbaseproject.Beans.Beans;
import com.example.hh.androidbaseproject.Beans.LoginData;
import com.example.hh.androidbaseproject.MainActivity;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by hh on 16/7/20.
 */
public class DataCache {
    private static DataCache data = null;
    public Gson gson = new Gson();
    public MainActivity mainActivity;

    final private String base = "http://a.xl18z.cn";
    final public String baseUrl = base;
    public final int PAGENUM = 15;
    public boolean hasLogin = false;
    public LoginData.LoginItemData loginInfo;

    public static SharedPreferences pref = null;

    private boolean debug = false;

    public void init() {
        pref = App.getAppContext()
                .getSharedPreferences("rryin", Context.MODE_PRIVATE);

        hasLogin = pref.getBoolean("hasLogin", false);
        loginInfo = gson.fromJson(pref.getString("loginInfo", ""), LoginData.LoginItemData.class);
        if (debug && loginInfo == null) {
            // debug用
            loginInfo = new LoginData.LoginItemData();
            hasLogin = true;
            loginInfo.客户名称 = "软件开发测试";
            loginInfo.客户代码 = loginInfo.客户名称;
            loginInfo.权限 = "客户";
        }
    }

    public static DataCache getInstance() {
        if (data == null) {
            data = new DataCache();
        }
        return data;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void savePreference() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("hasLogin", hasLogin);
        editor.putString("loginInfo", gson.toJson(loginInfo));
        editor.commit();
    }

    /**
     * 登录方法
     *
     * @param name
     * @param pass
     * @param callback
     */
    public static void login(String name, final String pass, final DataCallBack<LoginData> callback) {
        RequestParams params = new RequestParams(data.baseUrl + "/api/HNHBAN_User");
        params.addQueryStringParameter("客户名称", name);
        params.addQueryStringParameter("登陆密码", Helper.def(pass, "123456"));
        requestData(HttpMethod.GET, params, LoginData.class, new DataCallBack<LoginData>() {
            @Override
            public void onEnd(LoginData beans) {
                LoginData d = beans;
                Log.e("-----pre-Login---->",beans.errcode + "," + beans.errmsg);
                if (d.errcode == 0 && d.record.size() > 0) {
                    data.hasLogin = true;
                    data.loginInfo = ((LoginData) beans).record.get(0);
                    data.loginInfo.密码 = pass;
                    data.savePreference();
                }
                callback.onEnd(beans);
            }
        });
    }



    ////////////////////////////

    /**
     * 底层访问网络方法
     *
     * @param method
     * @param params
     * @param cls
     * @param callback
     */
    private static <T extends Beans> void requestData(HttpMethod method, RequestParams params, final Class<T> cls, final DataCallBack<T> callback) {
        Log.e("->", params.toString());
        x.http().request(method, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("-res->", result);
                T d = data.gson.fromJson(result, cls);
                callback.onEnd(d);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                try {
                    T d = (T) Class.forName(cls.getName()).newInstance();
                    d.errcode = 1;
                    d.errmsg = "onError:"+ex.toString();
                    Log.e("---error!--->",d.errmsg);
                    callback.onEnd(d);
                } catch (ClassNotFoundException ex2) {

                } catch (IllegalAccessException ex3) {

                } catch (InstantiationException ex4) {

                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //
                try {
                    T d = (T) Class.forName(cls.getName()).newInstance();
                    d.errcode = 1;
                    d.errmsg = "onCancelled:" + cex.toString();
                    Log.e("---canceled!--->",d.errmsg);
                    callback.onEnd(d);
                } catch (ClassNotFoundException ex2) {

                } catch (IllegalAccessException ex3) {

                } catch (InstantiationException ex4) {

                }
            }

            @Override
            public void onFinished() {
                //
            }
        });
    }
}
