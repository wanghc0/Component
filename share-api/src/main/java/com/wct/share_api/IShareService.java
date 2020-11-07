package com.wct.share_api;

import com.wct.componentbase.IService;

/**
 * @author wangchenteng
 * @date 2020/10/18
 * @desc
 */
public interface IShareService extends IService {
    void share(String content);
}
