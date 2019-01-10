package com.coin.exchange.view;

import com.coin.libbase.view.IView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/29
 * @description
 */
public interface BindView extends IView {

    void onBindSuc(String apiKey, String secretKey, String passphraseKey);

    void onBindError(String msg);

    void onBitMEXBindError(String msg);

    void onBitMEXBindSuc(String apiKey, String secretKey);

}
