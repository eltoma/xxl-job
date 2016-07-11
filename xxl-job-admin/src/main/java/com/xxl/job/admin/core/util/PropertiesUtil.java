package com.xxl.job.admin.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * properties util
 *
 * @author xuxueli 2015-8-28 10:35:53
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final String file_name = "classpath:config.properties";

    /**
     * load properties
     *
     * @param propertyFileName
     * @return
     */
    public static Properties loadProperties(String propertyFileName) {
        Properties prop = new Properties();
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(new FileInputStream(ResourceUtils.getFile(file_name)), "UTF-8");
            prop.load(in);
        } catch (IOException e) {
            logger.error("load {} error!", propertyFileName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close {} error!", propertyFileName);
                }
            }
        }
        return prop;
    }

    public static String getString(String key) {
        Properties prop = loadProperties(file_name);
        if (prop != null) {
            return prop.getProperty(key);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getString("login.username"));
    }

}
