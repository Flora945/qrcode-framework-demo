package com.jlpay.qrcode.external.db.service;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.services.OrderSystemService;
import com.jlpay.qrcode.external.business.services.protocol.response.ChannelTranNotifyResponse;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.business.protocol.enums.PayType;
import com.jlpay.qrcode.external.business.protocol.enums.TradeType;
import com.jlpay.qrcode.external.business.protocol.request.BaseTransRequest;
import com.jlpay.qrcode.external.business.protocol.request.MicropayRequest;
import com.jlpay.qrcode.external.business.protocol.request.RefundTransRequest;
import com.jlpay.qrcode.external.config.properties.QrcodeApiProperties;
import com.jlpay.qrcode.external.db.mapper.OutQrPayOrderMapper;
import com.jlpay.qrcode.external.db.model.CheckOrderId;
import com.jlpay.qrcode.external.db.model.OrderStatusType;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.db.model.OutQrStaticQrcode;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.qrcode.external.support.QrcodeApiUtils;
import com.jlpay.utils.exception.BusiAssert;
import com.jlpay.utils.exception.BusiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutQrPayOrderServiceImpl implements IOutQrPayOrderService {

    private final OrderSystemService orderSystemService;

    private final OutQrPayOrderMapper outQrPayOrderMapper;

    private final ICheckOrderIdService checkOrderIdService;

    private final QrcodeApiProperties apiProperties;

    @Override
    public OutQrPayOrder createOrder(BaseTransRequest transCodeRequest) {
        transCodeRequest.setTransTime(new Date());
        //效验同一机构下,商户订单号是否重复
        isNotExist(transCodeRequest.getOutTradeNo(),transCodeRequest.getOrgCode());

        // 计算并保存过期时间
        String paymentValidTime = transCodeRequest.getPaymentValidTime();
        if (StringUtils.isBlank(paymentValidTime)) {
            paymentValidTime = apiProperties.getPayValidTime();
            transCodeRequest.setPaymentValidTime(paymentValidTime);
        }

        String orderId = orderSystemService.createOrderByV2(transCodeRequest);

        OutQrPayOrder outQrPayOrder = new OutQrPayOrder();
        outQrPayOrder.setOrderId(orderId);
        outQrPayOrder.setOrgCode(transCodeRequest.getOrgCode());
        outQrPayOrder.setMerchNo(transCodeRequest.getMchId());
        outQrPayOrder.setFeeCalcType(PayType.findPayType(transCodeRequest.getPayType()).getFeeCalcType());
        outQrPayOrder.setAmount(Long.valueOf(transCodeRequest.getTotalFee()));
        outQrPayOrder.setBusiType(transCodeRequest.getBusiType());
        outQrPayOrder.setBusiSubType(transCodeRequest.getBusiSubType());
        outQrPayOrder.setOutOrderId(transCodeRequest.getOutTradeNo());
        outQrPayOrder.setStatus(OrderStatusType.NEW.getOutStatus());
        //是否记账 0-未记账，1-记账成功，2-记账失败
        outQrPayOrder.setAccFlag("0");
        outQrPayOrder.setAddTime(transCodeRequest.getTransTime());
        outQrPayOrder.setTransTime(transCodeRequest.getTransTime());
        outQrPayOrder.setOrderDate(transCodeRequest.getTransTime());
        outQrPayOrder.setTradeType(transCodeRequest.getTradeType());
        //来源,0-下游,1-静态码,外部接入,默认不上送此字段,故默认为0
        outQrPayOrder.setSource(StringUtils.isNotEmpty(transCodeRequest.getSource())?transCodeRequest.getSource(): ApiConstants.OUTSIDE);
        //码付版本,区分重构的老外接码付和 重构的新外接码付
        outQrPayOrder.setQrVersion(StringUtils.isNotEmpty(transCodeRequest.getQrcodeVersion())?transCodeRequest.getQrcodeVersion():ApiConstants.VERSION);
        outQrPayOrder.setTermNo(transCodeRequest.getTermNo());
        outQrPayOrder.setOutDeviceSn(transCodeRequest.getDeviceInfo());
        //是否分账：0-否，1-是   默认不分账
        outQrPayOrder.setProfitSharing(StringUtils.isNotEmpty(transCodeRequest.getProfitSharing())?transCodeRequest.getProfitSharing():ApiConstants.NO_PROFITSHARING);
        outQrPayOrder.setRemark(transCodeRequest.getRemark());

        outQrPayOrder.setLongitude(transCodeRequest.getLongitude());
        outQrPayOrder.setLatitude(transCodeRequest.getLatitude());
        outQrPayOrder.setClientIp(transCodeRequest.getMchCreateIp());
        outQrPayOrder.setOpUserId(transCodeRequest.getOpUserId());
        outQrPayOrder.setOpShopId(transCodeRequest.getOpShopId());

        outQrPayOrder.setAppid(transCodeRequest.getSubAppid());
        outQrPayOrder.setNotifyUrl(transCodeRequest.getNotifyUrl());

        if (ApiConstants.MICROPAYASYN_SERVICE.equals(transCodeRequest.getService())){
            MicropayRequest micropayRequest=(MicropayRequest)transCodeRequest;
            outQrPayOrder.setAuthCode(micropayRequest.getAuthCode());
        }

        outQrPayOrder.setSubject(transCodeRequest.getBody());
        //保存反向交易的订单号
        outQrPayOrder.setOriOrderId(transCodeRequest.getOriTransactionId());
        outQrPayOrder.setOriOutOrderId(transCodeRequest.getOriOutTradeNo());
        outQrPayOrder.setExpireDate(calcExpireDate(outQrPayOrder.getAddTime(), paymentValidTime));
        save(outQrPayOrder);
        return outQrPayOrder;
    }

    /**
     * 计算过期时间
     *
     * @param createTime 创建时间
     * @param paymentValidTime 支付有效时间(分)
     * @return 创建时间 + 支付有效时间
     */
    private Date calcExpireDate(Date createTime, String paymentValidTime) {
        return new Date(createTime.getTime() + Long.parseLong(paymentValidTime) * 60000);
    }

    @Override
    public void save(OutQrPayOrder outQrPayOrder) {
        long startTime=System.currentTimeMillis();
        outQrPayOrderMapper.save(outQrPayOrder);
        log.info("保存订单:{},耗时:{}ms",JSON.toJSONString(outQrPayOrder),System.currentTimeMillis()-startTime);
    }

    /**
     * 创建反向交易流水
     * @param reverseTransRequest 反向交易请求类
     * @param oriOutPayOrder 原交易订单号
     * @param busiSubType 业务小类
     * @return 反向交易流水
     */
    @Override
    public OutQrPayOrder createReverseOrder(RefundTransRequest reverseTransRequest, OutQrPayOrder oriOutPayOrder, String busiSubType) {
        log.debug("创建反向交易流水");
        BusiAssert.isTrue(reverseTransRequest.getMchId().equals(oriOutPayOrder.getMerchNo()),"商户号与原交易订单商户号不一致");
        reverseTransRequest.setBusiSubType(busiSubType);
        reverseTransRequest.setTradeType(oriOutPayOrder.getTradeType());
        reverseTransRequest.setPayType(PayType.findPayTypeByFeeC(oriOutPayOrder.getFeeCalcType()).getPayType());
        reverseTransRequest.setProfitSharing(oriOutPayOrder.getProfitSharing());
        reverseTransRequest.setTermNo(oriOutPayOrder.getTermNo());
        reverseTransRequest.setDeviceInfo(oriOutPayOrder.getOutDeviceSn());
        reverseTransRequest.setOriTransactionId(oriOutPayOrder.getOrderId());
        reverseTransRequest.setOriOutTradeNo(oriOutPayOrder.getOutOrderId());
        return createOrder(reverseTransRequest);
    }

    private void isNotExist(String outTradeNo, String orgCode){
        CheckOrderId checkOrderId = new CheckOrderId();
        checkOrderId.setOrgAndOrderId(orgCode+outTradeNo);
        checkOrderId.setCreateTime(new Date());
        try{
            checkOrderIdService.save(checkOrderId);
        }catch (DuplicateKeyException e){
            log.debug("机构下商户订单号重复: {},{}",orgCode,outTradeNo);
            throw new BusiException("外部订单号已经存在[ORDER.EXIST]");
        }
    }

    @Override
    public OutQrPayOrder findById(String orderId) {
        long startTime=System.currentTimeMillis();
        OutQrPayOrder outQrPayOrder = outQrPayOrderMapper.queryByOrderId(orderId);
        log.info("根据orderId查询订单耗时:{}ms",System.currentTimeMillis()-startTime);

        return outQrPayOrder;
    }

    @Override
    public OutQrPayOrder findByOutTradeNo(String orgCode, String outTradeNo) {
        long startTime=System.currentTimeMillis();
        OutQrPayOrder outQrPayOrder = outQrPayOrderMapper.queryByOutTradeNo(orgCode,outTradeNo);
        log.info("根据outTradeNo,orgCode 查询订单耗时:{}ms",System.currentTimeMillis()-startTime);

        return outQrPayOrder;
    }

    @Override
    public List<OutQrPayOrder> findByOriOrderId(String oriOrderId, String status) {
        long startTime=System.currentTimeMillis();
        List<OutQrPayOrder> outQrPayOrder = outQrPayOrderMapper.queryByOriOrderId(oriOrderId,status);
        log.info("根据oriOrderId,status 查询订单耗时:{}ms",System.currentTimeMillis()-startTime);

        return outQrPayOrder;
    }

    @Override
    public void update(OutQrPayOrder payOrder) {
        payOrder.setUtime(new Date());
        long startTime=System.currentTimeMillis();
        outQrPayOrderMapper.update(payOrder);
        log.info("更新订单信息:{},耗时:{}ms",JSON.toJSONString(payOrder),System.currentTimeMillis()-startTime);
    }

    /**
     * 根据银联交易通知创建交易流水
     *
     * @param notifyMessage 交易通知
     * @param staticQrcode  静态码/NFC信息
     * @return 外接码付流水
     */
    @Override
    public OutQrPayOrder createUnionpayOrder(ChannelTranNotifyResponse notifyMessage, OutQrStaticQrcode staticQrcode) {
        Date date=new Date();
        OutQrPayOrder outQrPayOrder = new OutQrPayOrder();
        outQrPayOrder.setOrderId(notifyMessage.getOrderNo());
        outQrPayOrder.setAddTime(date);
        outQrPayOrder.setAmount(QrcodeApiUtils.parseStrToLong(notifyMessage.getAmount()));
        outQrPayOrder.setBusiType(StringUtils.isNotBlank(notifyMessage.getBusiType()) ? notifyMessage.getBusiType() : ApiConstants.BUSI_TYPE_1001);
        String busiSubType=ApiConstants.NFC.equals(notifyMessage.getNotifyType())? BusiSubType.NFC_PAY.getFlag():BusiSubType.C2B_NATIVEPAY.getFlag();
        outQrPayOrder.setBusiSubType(busiSubType);
        outQrPayOrder.setQrcode(staticQrcode.getQrcodeUrl());
        outQrPayOrder.setTradeType(TradeType.QRCODE.getValue());
        outQrPayOrder.setMerchNo(staticQrcode.getMerchNo());
        outQrPayOrder.setOrgCode(staticQrcode.getOrgCode());

        outQrPayOrder.setOriOrderId(staticQrcode.getOrderId());

        outQrPayOrder.setOutOrderId(notifyMessage.getChnOrderNo());
        outQrPayOrder.setOrderDate(date);
        outQrPayOrder.setFeeCalcType(PayType.unionpay.getFeeCalcType());
        outQrPayOrder.setStatus(OrderStatusType.SUCCESS.getOutStatus());
        outQrPayOrder.setRemark("银联"+notifyMessage.getNotifyType()+"支付");
        outQrPayOrder.setChnOrderNo(notifyMessage.getChnOrderNo());
        outQrPayOrder.setTransTime(date);
        outQrPayOrder.setChannelNo(notifyMessage.getChannelId());

        outQrPayOrder.setTermNo(staticQrcode.getTermNo());
        outQrPayOrder.setDeviceSn(staticQrcode.getDeviceSn());

        outQrPayOrder.setDiscountAmount(QrcodeApiUtils.parseStrToLong(notifyMessage.getDiscountAmount()));
        outQrPayOrder.setSettleAmount(QrcodeApiUtils.parseStrToLong(notifyMessage.getSettlementAmt()));
        outQrPayOrder.setExterDiscountAmount(QrcodeApiUtils.parseStrToLong(notifyMessage.getExterDiscountAmount()));
        outQrPayOrder.setFinnalAmount(QrcodeApiUtils.parseStrToLong(notifyMessage.getFinnalAmount()));
        outQrPayOrder.setSettleType(notifyMessage.getDiscountSettleType());
        outQrPayOrder.setDiscountName(notifyMessage.getDiscountName());
        outQrPayOrder.setCouponInfo(notifyMessage.getCouponInfo());

        //是否记账 0-未记账，1-记账成功，2-记账失败
        outQrPayOrder.setAccFlag("0");
        //内部接入,静态码
        outQrPayOrder.setSource(ApiConstants.INSIDE_STATIC_CODE);
        outQrPayOrder.setQrVersion(ApiConstants.VERSION);
        //不分帐
        outQrPayOrder.setProfitSharing(ApiConstants.NO_PROFITSHARING);

        save(outQrPayOrder);
        return outQrPayOrder;
    }

}
