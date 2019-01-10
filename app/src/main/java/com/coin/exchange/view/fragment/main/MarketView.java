package com.coin.exchange.view.fragment.main;

import com.coin.exchange.model.okex.vo.HotCoinItemVO;
import com.coin.exchange.model.okex.vo.RankItemVO;
import com.coin.libbase.view.IView;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public interface MarketView extends IView {

    void onGetHotCoinInfoList(List<HotCoinItemVO> hotCoinItemVOList);
    void onGetHotCoinInfoListError();

    /**
     * OkEx 的 排行榜回调
     */
    void onGetOkExRankListInfo(List<RankItemVO> rankItemVOList);
    void onGetOkExError();

    /**
     * BitMex 的 排行榜回调
     */
    void onGetBitMexRankListInfo(List<RankItemVO> rankItemVOList);
    void onGetBitMexError();

}
