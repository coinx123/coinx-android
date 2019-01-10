package com.coin.exchange.view.fragment.trade.position;

import com.coin.exchange.model.okex.vo.PositionItemVO;
import com.coin.libbase.view.IView;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
public interface PositionContentView extends IView {

    void onGetType(List<String> typeList);

    // 获取 合约持仓信息 成功回调
    void onGetFuturesPosition(List<PositionItemVO> positionItemVOList);

    // 获取 合约持仓信息 失败回调
    void onGetFuturesPositionError();

    // 提交成功
    void onPostSellSuc(int position);

    // 提交失败
    void onPostSellError(String msg);

    // 调节保证金失败
    void onAdjustSecError(String msg);

    // 调节保证金成功
    void onAdjustSec();


}
