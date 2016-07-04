package com.xxl.job.core.log.reader;

import com.google.common.base.Throwables;
import com.xxl.job.core.log.ILogTemplate;
import com.xxl.job.core.log.annotation.LogView;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
            configuration.setEncoding(Locale.getDefault(), "utf-8");

            Map<String, Object> viewData = new HashMap<>();
            viewData.put("logView", logView);
            viewData.put("callBack", returnData);

            String viewName = getViewName(logView, returnData, viewData);
            Template template = configuration.getTemplate(viewName, "utf-8");
            StringWriter stringWriter = new StringWriter();
            template.process(viewData, stringWriter, BeansWrapper.getDefaultInstance());
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
            List<?> returnDatalist = (List<?>) returnData;
            if (CollectionUtils.isEmpty(returnDatalist)) {
                return "listPrimitiveView.ftl";
            }
            Object returnDataEm = returnDatalist.get(0);
            if (returnDataEm == null || returnDataEm.getClass().isPrimitive()) {
                return "listPrimitiveView.ftl";
            } else if (returnDataEm instanceof Map) {
                return "listMapView.ftl";
            } else {
                viewData.put("head", getHead(returnDatalist));
                viewData.put("callBack", getBeanMap(returnDatalist));
                return "listObjectView.ftl";
            }
        }
        if (returnData instanceof Map) {
            return "mapView.ftl";
        }
        viewData.put("head", getHead(returnData));
        viewData.put("callBack", getBeanMap(returnData));
        return "ObjectView.ftl";
    }

    /**
     * 获得head
     *
     * @param returnDatalist
     * @return
     */
    public List<String> getHead(List<?> returnDatalist) {
        if (CollectionUtils.isEmpty(returnDatalist)) {
            return Collections.EMPTY_LIST;
        }
        Object returnDataEm = returnDatalist.get(0);
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(returnDataEm.getClass());
        List<String> list = new ArrayList<>(propertyDescriptors.length);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            list.add(propertyDescriptor.getName());
        }
        return list;
    }

    /**
     * 获得head
     *
     * @param obj
     * @return
     */
    public List<String> getHead(Object obj) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj.getClass());
        List<String> list = new ArrayList<>(propertyDescriptors.length);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            list.add(propertyDescriptor.getName());
        }
        return list;
    }

    /**
     * bean 转化为Map
     *
     * @param objectList
     * @return
     */
    public List<Map<String, String>> getBeanMap(List<?> objectList) {
        List<Map<String, String>> list = new ArrayList<>(objectList.size());
        try {
            for (Object obj : objectList) {
                list.add(BeanUtils.describe(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * bean转化为map
     *
     * @param obj
     * @return
     */
    public Map<String, String> getBeanMap(Object obj) {
        try {
//        return BeanMap.create(obj);
            return BeanUtils.describe(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_MAP;
    }

}
