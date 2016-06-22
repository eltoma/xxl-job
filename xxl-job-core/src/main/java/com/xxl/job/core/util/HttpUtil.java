package com.xxl.job.core.util;

import org.apache.commons.codec.Charsets;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * http util to send data
 *
 * @author xuxueli
 * @version 2015-11-28 15:30:59
 */
public class HttpUtil {

    /**
     * http remote callback
     */
    public static CallBack post(String reqURL, Map<String, String> params) {
        CallBack callback = CallBack.fail();
        try {
            String response = Request.Post(reqURL).socketTimeout(5000).connectTimeout(5000)
                    .bodyForm(toNameValuePair(params), Consts.UTF_8).execute().returnContent().asString(Charsets.UTF_8);
            callback = JacksonUtil.readValue(response, CallBack.class);
            if (callback == null) {
                callback = CallBack.fail("responseMsg parse json fail, responseMsg:" + response);
            }
        } catch (Exception e) {
            callback.setMsg(e.getMessage());
        }

        return callback;
    }

    public static List<NameValuePair> toNameValuePair(Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            return Collections.EMPTY_LIST;
        }
        Form form = Form.form();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            form.add(entry.getKey(), entry.getValue());
        }
        return form.build();
    }


    /**
     * parse address ip:port to url http://.../
     *
     * @param address
     * @return
     */
    public static String addressToUrl(String address) {
        return "http://" + address + "/";
    }

}
