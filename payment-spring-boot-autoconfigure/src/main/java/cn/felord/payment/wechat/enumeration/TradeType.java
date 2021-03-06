package cn.felord.payment.wechat.enumeration;

/**
 * 微信侧返回交易类型
 *
 * @author felord.cn
 * @since 1.0.0.RELEASE
 */
public enum TradeType {
    /**
     * 公众号支付
     */
    JSAPI,
    /**
     * 扫码支付
     */
    NATIVE,
    /**
     * APP支付
     */
    APP,
    /**
     * 付款码支付
     */
    MICROPAY,
    /**
     * H5支付
     */
    MWEB,
    /**
     * 刷脸支付
     */
    FACEPAY,
}
