package com.xxl.job.executor.service.parser;

import org.springframework.util.Assert;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feiluo on 6/16/2016.
 */
public class KettleJobParamParser implements IJobParamParser {

    private String[] params;
    private String filePath;
    private String fileSuffix;
    private Map<String, String> variableMap;


    public KettleJobParamParser(String... params) {
        this.params = params;
        parse(params);
    }

    @Override
    // params[0] 为fileName,传递的参数为：params[1]:params[2],params[3]:params[4],……
    public void parse(String... params) {
        Assert.noNullElements(params, "kettle file name is required.");
        filePath = params[0];
        Assert.hasText(filePath, "kettle file name is required.");
        Assert.isTrue(new File(filePath).exists(), String.format("%s is not found.Please check", filePath));
        fileSuffix = filePath.lastIndexOf('.') < 0 ? null : filePath.substring(filePath.lastIndexOf('.') + 1);
        Assert.hasText(fileSuffix, "unkown file suffix.");
        variableMap = parse(params, 1);
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public String getFilePath() {
        return filePath;
    }

    public Map<String, String> getVariableMap() {
        return variableMap;
    }

    public Map<String, String> parse(String[] array, int offset) {
        if (array == null || array.length - offset < 1) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new HashMap<String, String>();
        String key = null;
        String value;
        for (int i = offset; i <= array.length; i++) {
            if (key == null) {
                key = array[i];
                continue;
            }
            value = i == array.length ? null : array[i];
            map.put(key, value);
            key = null;
        }
        return map;
    }
}
