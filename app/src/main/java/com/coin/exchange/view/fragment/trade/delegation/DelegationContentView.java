package com.coin.exchange.view.fragment.trade.delegation;

import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.libbase.view.IView;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public interface DelegationContentView extends IView {

    void onGetDelegationListSuc(List<DelegationItemVO> list, boolean isFirst);

    void onGetDelegationListError();

    void onCancelError();

    void onCancelSuc(String orderId, int position);

}
