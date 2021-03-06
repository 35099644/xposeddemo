package com.example.administrator.xposeddemo.hook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "MAINHOOK";
    private boolean ALIPAY_PACKAGE_ISHOOK = false;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        try {
            if (loadPackageParam.packageName.equals("com.eg.android.AlipayGphone")) {

                XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook(){
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        Context context = (Context) param.args[0];
                        ClassLoader classLoader = context.getClassLoader();

                        String thisObject = param.thisObject.toString();
                        XposedBridge.log("activity=========" + thisObject);
                        Class<?> MspContainerActivity = XposedHelpers.findClass("com.alipay.mobile.quinox.activity.QuinoxContext", classLoader);

                        for(Method method :  MspContainerActivity.getMethods()){
                            XposedBridge.log("method=========" + method);
                        }
//
//                        if(clazz.getName().equals("com.alipay.android.msp.ui.views.MspContainerActivity")){
//                            for(Method method : clazz.getMethods()){
//                                XposedBridge.log("method============" + method);
//                            }
//                        }

//                        XposedBridge.hookAllMethods(MspContainerActivity, "setTitle", new XC_MethodHook(){
//
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                                param.setResult("111");
//                            }
//
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                            }
//                        });


                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });








//                Class<?> clazz = classLoader.loadClass("com.alipay.mobile.nebulabiz.rpc.H5RpcUtil");
//                Class<?> h5PageClazz = classLoader.loadClass("com.alipay.mobile.h5container.api.H5Page");
//                Class<?> jSONObjectClazz = classLoader.loadClass("com.alibaba.fastjson.JSONObject");

//                XposedHelpers.findAndHookMethod(clazz, "rpcCall", String.class, String.class, String.class, boolean.class,
//                        jSONObjectClazz, String.class, boolean.class,
//                        h5PageClazz, int.class, String.class, boolean.class, int.class, new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                            }
//
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//
//                                Object resp = param.getResult();
//                                if (resp != null) {
//                                    Method method = resp.getClass().getMethod("getResponse", new Class<?>[]{});
//                                    String response = (String) method.invoke(resp, new Object[]{});
//
//                                    XposedBridge.log(response);
//                                }
//                            }
//                        });
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        if (loadPackageParam.packageName.equals("com.eg.android.AlipayGphone")) {

            XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {

                    securityCheckHook(loadPackageParam.classLoader);

//                    XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//
//                            XposedBridge.log(param.thisObject.toString());
//
//                            super.beforeHookedMethod(param);
//                        }
//                    });


//                    Class<?> insertTradeMessageInfo = XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", loadPackageParam.classLoader);

//                    XposedBridge.hookAllMethods(insertTradeMessageInfo, "insertMessageInfo", new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            try {
//                                //获取全部字段
//
//                                Object object = param.args[0];
//                                String messageInfo = (String) XposedHelpers.callMethod(object, "toString");
//                                String content = getTextCenter(messageInfo, "content='", "'");
//                                XposedBridge.log("MessageInfo:" + messageInfo);
//                                XposedBridge.log("content:" + content);
//                            } catch (Exception e) {
//                                XposedBridge.log(e.getMessage());
//                            }
//                            super.beforeHookedMethod(param);
//                        }
//                    });

                    if ("com.eg.android.AlipayGphone".equals(loadPackageParam.processName) && !ALIPAY_PACKAGE_ISHOOK) {

                        ALIPAY_PACKAGE_ISHOOK = true;

                        Class<?> insertTradeMessage = XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", loadPackageParam.classLoader);
                        XposedBridge.hookAllMethods(insertTradeMessage, "insertMessageInfo", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                try {
                                    XposedBridge.log("======支付宝支付助手start=========");
                                    //获取全部字段
                                    Object object = param.args[0];
                                    String messageInfo = (String) XposedHelpers.callMethod(object, "toString");

                                    if (messageInfo.contains("支付助手")) {
                                        String content = getTextCenter(messageInfo, "content='", "'");
                                        JSONObject jsonObject = new JSONObject(content);
                                        String money = jsonObject.getString("content").replace("￥", "");

                                        String price = String.valueOf(ArithmeticUtil.mul(String.valueOf(money), "100", 0)).replace(".0", "");

                                        TransmitRequest transmitRequest = new TransmitRequest();
                                        transmitRequest.setUserName("bt");
                                        transmitRequest.setPassword("123456");
                                        transmitRequest.setPrice(price);
                                        transmitRequest.setType("autoTransferAliPay");

                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(transmitRequest));

                                        Request request = new Request.Builder()
                                                .url("http://biu.haohan168.cn/order/client/transmit")
                                                .post(requestBody)
                                                .build();
                                        OkHttpClient okHttpClient = new OkHttpClient();
                                        okHttpClient.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Log.d("dfdf", "onFailure: " + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                Log.d("dfdf", response.protocol() + " " + response.code() + " " + response.message());

                                            }
                                        });
                                        XposedBridge.log("MessageInfo:" + messageInfo);
                                        XposedBridge.log("content:" + content);
                                        XposedBridge.log("======支付宝支付助手end=========");
                                    }
                                } catch (Exception e) {
                                    XposedBridge.log(e.getMessage());
                                }
                                super.beforeHookedMethod(param);
                            }
                        });

                    }
                }
            });
        }
    }


    public String getTextCenter(String text, String begin, String end) {
        try {
            int b = text.indexOf(begin) + begin.length();
            int e = text.indexOf(end, b);
            return text.substring(b, e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    //破解支付宝安全解密
    private void securityCheckHook(ClassLoader classLoader) {
        try {
            Class<?> securityCheckClazz = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", String.class, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object object = param.getResult();
                    XposedHelpers.setBooleanField(object, "a", false);
                    param.setResult(object);
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", Class.class, String.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) {
                    return false;
                }
            });

        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }
}
