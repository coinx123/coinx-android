package com.coin.exchange.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.cache.BitMexUserCache;
import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.di.component.DaggerBindApiComponent;
import com.coin.exchange.di.module.BindApiModule;
import com.coin.exchange.model.bitMex.BitMEXUserModel;
import com.coin.exchange.model.okex.OkExUserModel;
import com.coin.exchange.presenter.BindApiPresenter;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.GsonUtils;
import com.google.zxing.Constant;
import com.google.zxing.activity.CaptureActivity;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.activity.JToolbarActivity;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/29
 * @description 绑定账号页面
 */

public class BindActivity extends JToolbarActivity<BindApiPresenter>
        implements View.OnClickListener, com.coin.exchange.view.BindView {

    @BindView(R.id.et_api_key)
    EditText etApiKey;

    @BindView(R.id.iv_clear_api)
    ImageView ivClearApi;

    @BindView(R.id.et_secret_key)
    EditText etSecretKey;

    @BindView(R.id.iv_secret_api)
    ImageView ivSecretApi;

    @BindView(R.id.et_passphrase_key)
    EditText etPassphraseKey;

    @BindView(R.id.iv_passphrase_api)
    ImageView ivPassphraseApi;

    @BindView(R.id.ll_scan)
    LinearLayout llScan;

    @BindView(R.id.tv_bind)
    TextView tvBind;

    @BindView(R.id.ll_passphrase_key)
    LinearLayout llPassphraseKey;

    @BindView(R.id.v_passphrase_key_divider)
    View vPassphraseKeyDivider;

    public static final String TYPE = "type";
    private String type;

    @Override
    protected int getLayout() {
        return R.layout.activity_bind;
    }

    @Override
    protected void initIntent(Intent intent) {
        type = intent.getStringExtra(TYPE);
    }

    @Override
    protected void initView() {
        ivClearApi.setOnClickListener(this);
        ivSecretApi.setOnClickListener(this);
        ivPassphraseApi.setOnClickListener(this);

        llScan.setOnClickListener(this);

        tvBind.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        switch (type) {
            case AppUtils.BITMEX:
                setTitle("BitMEX 绑定");
                llScan.setVisibility(View.GONE);
                llPassphraseKey.setVisibility(View.GONE);
                vPassphraseKeyDivider.setVisibility(View.GONE);
                break;
            case AppUtils.OKEX:
                setTitle("OKEx 绑定");
                llScan.setVisibility(View.VISIBLE);
                llPassphraseKey.setVisibility(View.VISIBLE);
                vPassphraseKeyDivider.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void registerDagger() {
        DaggerBindApiComponent
                .builder()
                .appComponent(AppApplication.getAppComponent())
                .bindApiModule(new BindApiModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_api:
                etApiKey.setText("");
                break;
            case R.id.iv_secret_api:
                etSecretKey.setText("");
                break;
            case R.id.iv_passphrase_api:
                etPassphraseKey.setText("");
                break;
            case R.id.ll_scan:
                startScanQRCode();
                break;
            case R.id.tv_bind:
                String apiKey = etApiKey.getText().toString().trim();
                String secretKey = etSecretKey.getText().toString().trim();
                String passphraseKey = etPassphraseKey.getText().toString().trim();

                if (type.equals(AppUtils.OKEX)) {
                    checkOKExAccount(apiKey, secretKey, passphraseKey);
                } else if (type.equals(AppUtils.BITMEX)) {
                    checkBitMEXAccount(apiKey, secretKey);
                }

                break;
        }
    }

    private void checkOKExAccount(String apiKey, String secretKey, String passphraseKey) {
        if (TextUtils.isEmpty(apiKey) ||
                TextUtils.isEmpty(secretKey) ||
                TextUtils.isEmpty(passphraseKey)) {
            ToastUtil.show("请填写完整信息");
            return;
        }

        showDialog();

        mPresenter.checkUserInfo(apiKey, secretKey, passphraseKey);
    }

    private void checkBitMEXAccount(String apiKey, String secretKey) {
        if (TextUtils.isEmpty(apiKey) ||
                TextUtils.isEmpty(secretKey)) {
            ToastUtil.show("请填写完整信息");
            return;
        }

        showDialog();

        mPresenter.checkBitMEXInfo(apiKey, secretKey);
    }

    @Permission(value = {Manifest.permission.CAMERA}, requestCode = 100)
    private void startScanQRCode() {
        // 二维码扫码
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    //只需加上注解 @PermissionCanceled 和 参数类型为 CancelInfo 的一个参数即可
    @PermissionCanceled(requestCode = 100)
    private void cancel(CancelInfo cancelInfo) {
        Toast.makeText(this, "请授权相机权限，以便正常登录使用", Toast.LENGTH_SHORT).show();
    }

    //只需加上注解 @PermissionDenied 和 参数类型为 DenyInfo 的一个参数即可
    @PermissionDenied(requestCode = 100)
    private void deny(DenyInfo denyInfo) {
        new MaterialDialog.Builder(this)
                .title("请求授予相机权限")
                .content("使用扫描二维码，需要相机权限，请前往设置页面授予相机权限")
                .positiveColor(ContextCompat.getColor(this, R.color.blue))
                .positiveText("前往设置")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        JPermissionUtil.goToMenu(BindActivity.this);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                Toast.makeText(this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
                return;
            }
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            Log.i(TAG, "onActivityResult: " + scanResult);

            if (TextUtils.isEmpty(scanResult)) {
                Toast.makeText(this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                OkExUserModel userModel = (OkExUserModel) GsonUtils.getInstance()
                        .fromJson(scanResult, OkExUserModel.class);
                if (TextUtils.isEmpty(userModel.getApiKey())) {
                    Toast.makeText(this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userModel.getSecretKey())) {
                    Toast.makeText(this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
                    return;
                }

                etApiKey.setText(userModel.getApiKey());
                etSecretKey.setText(userModel.getSecretKey());

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBindSuc(String apiKey, String secretKey, String passphraseKey) {
        hideDialog();
        OkExUserModel user = new OkExUserModel();
        user.setApiKey(apiKey);
        user.setSecretKey(secretKey);
        user.setPassphrase(passphraseKey);
        OkExUserCache.save(user);
        ToastUtil.show("绑定成功");
        finish();
    }

    @Override
    public void onBindError(String msg) {
        hideDialog();
        ToastUtil.show(msg);
    }

    @Override
    public void onBitMEXBindError(String msg) {
        hideDialog();
        ToastUtil.show(msg);
    }

    @Override
    public void onBitMEXBindSuc(String apiKey, String secretKey) {
        hideDialog();
        BitMEXUserModel user = new BitMEXUserModel();
        user.setApiKey(apiKey);
        user.setSecretKey(secretKey);
        BitMexUserCache.save(user);
        ToastUtil.show("绑定成功");
        finish();
    }
}
