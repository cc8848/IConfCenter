package com.cc.service;

import apimodel.*;
import com.cc.component.ConfCenterConf;
import com.ct.tconf.LoadConf;
import com.ct.tconf.PropertiesConf;
import com.ct.tjedis.JedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/31.
 */
@Service
public class ConfCenterService {

    @Autowired
    private JedisTool jedisTool;

    @Autowired
    private ConfCenterConf confCenterConf;

    /**
     * 获取配置信息
     *
     * @param rq
     * @return
     */
    public MoGetConfRp getconf(MoGetConfRq rq) {

        MoGetConfRp rp = new MoGetConfRp();
        try {
            //未指定配置版本，采用默认配置版本
            if (rq.getConfVersion().isEmpty()) {
                rq.setConfVersion(confCenterConf.confserver_confs_currentConfVersion);
            }
            if (rq.getConfVersion().isEmpty()) {
                rp.setMessage("未找到配置版本");
                return rp;
            }

            //缓存key
            String cacheKey = String.format("confs_%s", rq.getConfVersion());

            //获取缓存中是否存在
            rp = jedisTool.get(cacheKey, MoGetConfRp.class);
            if (rp.getStatus() == EnumHelper.EmRpStatus.成功.getVal() &&
                    rp.getConfs().size() >= 1) {
                rp.setStatus(EnumHelper.EmRpStatus.成功.getVal());
                rp.setMessage(EnumHelper.EmRpStatus.成功.toString());
                return rp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rp;
    }
}
