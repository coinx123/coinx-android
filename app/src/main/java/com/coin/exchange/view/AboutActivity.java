package com.coin.exchange.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.libbase.view.activity.JToolbarActivity;

import butterknife.BindView;

/**
 * @author dean
 * @date 创建时间：2019/1/9
 * @description 关于我们
 */
public class AboutActivity extends JToolbarActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        setToolbarBackground(R.color.white);
        setTitle("关于我们");
    }

    @Override
    protected void initData() {
        try {
            tvVersion.setText(getVersionName(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

}