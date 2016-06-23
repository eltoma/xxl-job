package com.xxl.job.admin.core.callback;

import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.util.DynamicSchedulerUtil;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by xuxueli on 2016-5-22 11:15:42
 */
public class XxlJobLogCallbackServerHandler extends AbstractHandler {

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        // parse param
        String log_id = httpServletRequest.getParameter("log_id");
        String status = httpServletRequest.getParameter("status");
        String msg = httpServletRequest.getParameter("msg");
        String returnInfo = httpServletRequest.getParameter("returnInfo");

        // process
        CallBack callBack = CallBack.fail();
        if (StringUtils.isNumeric(log_id) && StringUtils.isNotBlank(status)) {
            XxlJobLog log = DynamicSchedulerUtil.xxlJobLogDao.load(Integer.valueOf(log_id));
            if (log != null) {
                log.setHandleTime(new Date());
                log.setHandleStatus(status);
                log.setHandleMsg(msg);
                log.setHandleReturnInfo(returnInfo);
                DynamicSchedulerUtil.xxlJobLogDao.updateHandleInfo(log);
                callBack.setSuccess();
            }
        }
        String resp = JacksonUtil.writeValueAsString(callBack);

        // response
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        httpServletResponse.getWriter().println(resp);
    }

}
