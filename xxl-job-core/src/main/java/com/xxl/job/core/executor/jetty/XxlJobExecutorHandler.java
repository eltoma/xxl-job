package com.xxl.job.core.executor.jetty;

import com.xxl.job.core.handler.ActionHandlerRepository;
import com.xxl.job.core.util.JacksonUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuxueli on 2016/3/2 21:23.
 */
public class XxlJobExecutorHandler extends AbstractHandler {

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        Map<String, String> _param = new HashMap<String, String>();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] value = entry.getValue();
                _param.put(entry.getKey(), value.length > 0 ? value[0] : null);
            }
        }

        String resp = JacksonUtil.writeValueAsString(ActionHandlerRepository.service(_param));

        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        httpServletResponse.getWriter().println(resp);
    }

}
