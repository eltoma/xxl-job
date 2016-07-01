package com.xxl.job.core.log.reader;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.xxl.job.core.log.ILogTemplate;
import com.xxl.job.core.log.annotation.LogView;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * Created by feiluo on 6/29/2016.
 */
@Component
public class FreeMarkerLogTemplate implements ILogTemplate {


    public static final String FREE_MARKER_REPO = CLASSPATH_URL_PREFIX + "freemarker/logView";

    @Override
    public String getView(LogView logView, Object returnData) {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile(FREE_MARKER_REPO));
            configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
            configuration.setEncoding(Locale.getDefault(), "utf-8");

            Map<String, Object> viewData = new HashMap<>();
            viewData.put("logView", logView);
            viewData.put("callBack", returnData);

            String viewName = getViewName(logView, returnData, viewData);
            Template template = configuration.getTemplate(viewName, "utf-8");
            StringWriter stringWriter = new StringWriter();
            template.process(viewData, stringWriter, ObjectWrapper.BEANS_WRAPPER);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 获取模板以及map
     *
     * @param logView
     * @param returnData
     * @param viewData
     * @return
     */
    public String getViewName(LogView logView, Object returnData, Map<String, Object> viewData) {
        String[] views = logView.value();
        String view;
        if (ArrayUtils.isEmpty(views)) {
            view = getViewByType(logView, returnData, viewData);
        } else {
            view = views[0];
        }
        return view;
    }

    /**
     * 通过类型解析对应的模板
     *
     * @param logView
     * @param returnData
     * @param viewData
     * @return
     */
    public String getViewByType(LogView logView, Object returnData, Map<String, Object> viewData) {
        if (returnData == null || returnData.getClass().isPrimitive()) {
            return "primitiveView.ftl";
        }
        if (returnData instanceof List) {
            List<?> list = (List<?>) returnData;
            if (CollectionUtils.isEmpty(list)) {
                return "listPrimitiveView.ftl";
            }
            Object obj = list.get(0);
            if (obj == null || obj.getClass().isPrimitive()) {
                return "listPrimitiveView.ftl";
            } else if (obj instanceof Map) {
                return "listMapView.ftl";
            } else {
                return "listObjectView.ftl";
            }
        }
        if (returnData instanceof Map) {
            return "mapView.ftl";
        }
        return "ObjectView.ftl";
    }

}
