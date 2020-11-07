package com.wct.base;

/**
 * @author wangchenteng
 * @date 2020/8/31
 * @desc
 */
public class AppConfig {
    private static final String LoginApp = "com.wct.login_impl.LoginApp";
//    public static final String ShareApp = "com.bytedance.share_impl.ShareApp";

    public static String[] moduleApps = {
            LoginApp,
//            ShareApp
    };
}
