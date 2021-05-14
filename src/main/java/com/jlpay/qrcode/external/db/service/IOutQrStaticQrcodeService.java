package com.jlpay.qrcode.external.db.service;

import com.jlpay.qrcode.external.db.model.OutQrStaticQrcode;

public interface IOutQrStaticQrcodeService {

    public int insert(OutQrStaticQrcode record);

    public int saveNotNull(OutQrStaticQrcode record);

    public OutQrStaticQrcode getByOrderId(String orderId);

    public int updateNotNull(OutQrStaticQrcode record);

    public int updateByPrimaryKey(OutQrStaticQrcode record);

    public OutQrStaticQrcode getValidNfcTag(String merchNo, String termNo);

}
