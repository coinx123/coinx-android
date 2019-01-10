package com.coin.exchange.utils;

import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/28
 * @description 币种信息 的 工具类： 假设 instrumentId:BTC-USD-180213
 * 1、获取 rootName : BTC {@link #getRootName(String)}
 * 2、获取 desName : 0213 {@link #getDesName(String)}}
 * 3、获取 dayTime : 180213 (int类型) {@link #getDayTime(String)}
 * 4、获取 币种单位 : USD {@link #getUnit(String)}
 * 5、对 {@link FuturesInstrumentsTickerList} 的列表进行赋值 "当周、当季、次周"
 */
public class IconInfoUtils {

    private final static String SPLIT_LINE = "-";

    private static final String[] ONE = {"当周"};
    private static final String[] TWO = {"当周", "当季"};
    private static final String[] THREE = {"当周", "次周", "次季"};

    public static String getRootName(String insId) {
        String[] idArray = insId.split(SPLIT_LINE);
        if (idArray.length > 0) {
            return idArray[0];
        }
        return insId;
    }

    public static String getDesName(String insId) {
        String[] idArray = insId.split(SPLIT_LINE);
        // 裁出 1228 日期
        if (idArray.length >= 3) {
            String result = idArray[2];
            if (result.length() <= 4) {
                return result;
            }
            return result.substring(result.length() - 4);
        } else {
            return "";
        }
    }

    public static int getDayTime(String insId) {
        String[] idArray = insId.split(SPLIT_LINE);
        // 裁出 1228 日期
        if (idArray.length >= 3) {
            try {
                return Integer.parseInt(idArray[2]);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static String getUnit(String insId) {
        String[] idArray = insId.split(SPLIT_LINE);
        // 裁出 1228 日期
        if (idArray.length >= 2) {
            return idArray[1];
        } else {
            return "";
        }
    }

    public static Map<String, List<FuturesInstrumentsTickerList>> sortTheTicker(
            List<FuturesInstrumentsTickerList> tickerLists) {

        Map<String, List<FuturesInstrumentsTickerList>> result = new HashMap<>();

        // 将相同 rootName 的币种 归类
        for (FuturesInstrumentsTickerList item : tickerLists) {
            String rootName = getRootName(item.getInstrument_id());
            String nameDes = getDesName(item.getInstrument_id());
            int dayTime = getDayTime(item.getInstrument_id());

            List<FuturesInstrumentsTickerList> itemList;
            if (result.containsKey(rootName)) {
                itemList = result.get(rootName);
            } else {
                itemList = new ArrayList<>();
                result.put(rootName, itemList);
            }

            item.setRootName(rootName);
            item.setDesName(nameDes);
            item.setDayTime(dayTime);

            itemList.add(item);
        }

        // 进行填充 "当周、当季、次周"
        for (Map.Entry<String, List<FuturesInstrumentsTickerList>> entry : result.entrySet()) {

            List<FuturesInstrumentsTickerList> value = entry.getValue();

            // 进行按时间排序
            Collections.sort(value, new OkExTickerDayTimeComparator());

            int size = value.size();
            String typeArray[];

            if (size == 1) {
                typeArray = ONE;
            } else if (size == 2) {
                typeArray = TWO;
            } else if (size == 3) {
                typeArray = THREE;
            } else if (size > 3) {
                typeArray = null;
            } else {
                typeArray = null;
            }

            if (typeArray == null) {
                continue;
            }

            for (int i = 0; i < size; ++i) {
                value.get(i).setType(typeArray[i]);
            }

        }

        return result;
    }

}
