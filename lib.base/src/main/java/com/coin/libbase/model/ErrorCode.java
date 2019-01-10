package com.coin.libbase.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dean
 * @date 创建时间：2018/11/24
 * @description
 */
public class ErrorCode {
    public static final Map<String, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put("30001", "请求头\"OK_ACCESS_KEY\"不能为空");
        errorCodeMap.put("30002", "请求头\"OK_ACCESS_SIGN\"不能为空");
        errorCodeMap.put("30003", "请求头\"OK_ACCESS_TIMESTAMP\"不能为空");
        errorCodeMap.put("30004", "请求头\"OK_ACCESS_PASSPHRASE\"不能为空");
        errorCodeMap.put("30005", "无效的OK_ACCESS_TIMESTAMP");
        errorCodeMap.put("30006", "无效的OK_ACCESS_KEY");
        errorCodeMap.put("30007", "无效的Content_Type，请使用“application/json”格式");
        errorCodeMap.put("30008", "请求时间戳过期");
        errorCodeMap.put("30009", "系统错误");
        errorCodeMap.put("30010", "api 校验失败");
        errorCodeMap.put("30011", "无效的ip");
        errorCodeMap.put("30012", "无效的授权");
        errorCodeMap.put("30013", "无效的sign");
        errorCodeMap.put("30014", "请求太频繁");
        errorCodeMap.put("30015", "请求头\"OK_ACCESS_PASSPHRASE\"错误");
        errorCodeMap.put("30016", "您使用的是v1的apiKey，请调用v1接口。若您希望调用v3接口，请注册v3的apiKey。");
        errorCodeMap.put("30017", "apikey所属broker ID不匹配");
        errorCodeMap.put("30018", "apikey所属域名不匹配");

        errorCodeMap.put("30020", "body 不能为空");
        errorCodeMap.put("30021", "json数据格式错误");
        errorCodeMap.put("30023", "{0}参数不能为空 必填参数不能为空");
        errorCodeMap.put("30024", "{0}参数不能为空 参数值填写错误");
        errorCodeMap.put("30025", "{0}参数类型错误");
        errorCodeMap.put("30026", "用户请求频率过快，超过该接口允许的限额");
        errorCodeMap.put("30027", "登录失败");
        errorCodeMap.put("30028", "非本人操作");
        errorCodeMap.put("30029", "用户被冻结");
        errorCodeMap.put("30030", "请求接口失败，请您重试");
        errorCodeMap.put("30031", "币种不存在");// TODo 要继续写

        errorCodeMap.put("32001", "合约账户被冻结");
        errorCodeMap.put("32002", "用户合约账户不存在");
        errorCodeMap.put("32003", "撤单中，请耐心等待");
        errorCodeMap.put("32004", "您当前没有未成交的订单");
        errorCodeMap.put("32005", "超过最大下单量");
        errorCodeMap.put("32006", "委托价格或触发价格超过100万美元");
        errorCodeMap.put("32007", "合约相同方向只支持一个杠杆");
        errorCodeMap.put("32008", "当前最多可开仓位（全仓）");
        errorCodeMap.put("32009", "当前最多可开仓位（逐仓）");
        errorCodeMap.put("32010", "当前有持仓，无法设置杠杆");
        errorCodeMap.put("32011", "虚拟合约状态错误");
        errorCodeMap.put("32012", "合约订单更新错误");
        errorCodeMap.put("32013", "币种类型为空");
        errorCodeMap.put("32014", "您的平仓张数大于该仓位的可平张数");
        errorCodeMap.put("32015", "开仓前保证金率低于100%");
        errorCodeMap.put("32016", "开仓后保证金率低于100%");
        errorCodeMap.put("32017", "暂无对手价");
        errorCodeMap.put("32018", "下单数量不足1张，请重新选择");
        errorCodeMap.put("32019", "下单价格高于前一分钟的103%或低于97%");
        errorCodeMap.put("32020", "价格不在限价范围内");
        errorCodeMap.put("32021", "杠杆比率错误");
        errorCodeMap.put("32022", "根据相关法律，您所在的国家或地区不能使用该功能");
        errorCodeMap.put("32023", "账户存在借款");
        errorCodeMap.put("32024", "合约交割中，无法下单");
        errorCodeMap.put("32025", "合约清算中，无法下单");
        errorCodeMap.put("32026", "您的账户已被限制开仓操作");
        errorCodeMap.put("32027", "撤单超过20");
        errorCodeMap.put("32028", "用户被爆仓冻结");
        errorCodeMap.put("32029", "订单信息不存在");

    }

}
