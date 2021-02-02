package cn.ltt.projectcollection.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import cn.ltt.projectcollection.R;
import cn.ltt.projectcollection.base.BaseActivity;

public class DeepLinkActivity extends BaseActivity {

    @Override
    protected void initPage() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deeplink;
    }

    public void searchApp(View view) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source", getPackageName() + "#场景3");
            jsonObject.put("keyword", "热门应用");
            String params = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.URL_SAFE);
            Uri uri = Uri.parse("os-cf://cloudfolder/search/?params=" + params);
            Log.d(TAG, "searchApp: " + uri);
            startDeepLink(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAppDetails(View view) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source", getPackageName() + "#场景4");
            jsonObject.put("id_mark", "pkg");
            jsonObject.put("app_id", "com.hunantv.imgo.activity");
            jsonObject.put("data_source", "360os");
            jsonObject.put("category", "分类名称");
            jsonObject.put("is_ad", "0");
            jsonObject.put("app_name", "芒果TV");
            jsonObject.put("download_url", "http://d.comp.360os.com/cpeproviderfile/T1NTMy5BOS5CTEFOSy4xNTMzNTI2MzAwLm1ndHZfNi4wLjBfMTgwODA2XzExMTdfeWp4ZDAxLmFwaw==");
            jsonObject.put("behavior", "打点相关josn字符串");
            String params = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.URL_SAFE);
            Uri uri = Uri.parse("os-cf://cloudfolder/app_details/display?params=" + params);
            Log.d(TAG, "showAppDetails: " + uri);
            startDeepLink(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void installAppDetails(View view) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source", getPackageName() + "#场景5");
            jsonObject.put("id_mark", "pkg");
            jsonObject.put("app_id", "com.hunantv.imgo.activity");
            jsonObject.put("data_source", "360os");
            jsonObject.put("category", "分类名称");
            jsonObject.put("is_ad", "0");
            jsonObject.put("app_name", "芒果TV");
            jsonObject.put("download_url", "http://d.comp.360os.com/cpeproviderfile/T1NTMy5BOS5CTEFOSy4xNTMzNTI2MzAwLm1ndHZfNi4wLjBfMTgwODA2XzExMTdfeWp4ZDAxLmFwaw==");
            jsonObject.put("behavior", "打点相关josn字符串");
            String params = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.URL_SAFE);
            Uri uri = Uri.parse("os-cf://cloudfolder/app_details/display_install?params=" + params);
            Log.d(TAG, "installAppDetails: " + uri);
            startDeepLink(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void marketSearch(View view) {
        startDeepLink(Uri.parse("market://search?q=" + "芒果TV"));
    }

    public void marketDetails(View view) {
        startDeepLink(Uri.parse("market://details?id=" + "com.hunantv.imgo.activity"));
    }

    private void startDeepLink(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void courseDetails(View v) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("courseId", "640992219678830592");
            long time = System.currentTimeMillis();
            Log.d(TAG, "time:"+time + "  /1000:" +time / 1000);
            jsonObject.put("seconds", String.valueOf(time));
            String params = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.URL_SAFE);
            Uri uri = Uri.parse("xdfdeeplink://startpage/courseDetail/display?params=" + params);
            Log.d(TAG, "showAppDetails: " + uri);
            startDeepLink(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void webViewClick(View v) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "https://m.souke.xdf.cn/h5-pages/pages/search/index.html?cid=1&gradeCode=XX0103&externalCategoryCode=1");
            jsonObject.put("fullscreen", "1");
            jsonObject.put("isImmersive", "1");
            jsonObject.put("seconds", String.valueOf(System.currentTimeMillis() / 1000));
            String params = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.URL_SAFE);
            Uri uri = Uri.parse("xdfdeeplink://startpage/web/btnClick?params=" + params);
            Log.d(TAG, "webView: " + uri);
            startDeepLink(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}