package com.xxl.job.admin.core.constant;

/**
 * job group
 *
 * @author xuxueli 2016-1-15 14:23:05
 */
public class Constants {

    public enum JobGroupEnum {
        DEFAULT("默认"),
        MD_LY_FTP_AUTO("MD-LY FTP 自动"),
        MD_FILE_FTP_MANUAL("MD-File FTP 手动"),
        MD_CS_EXCEL_MANUAL("MD-CS Excel 自动"),
        MD_REPORT_EXCEL_AUTO("MD-Report Excel 手动"),
        MD_TEMP_MANUAL("MD-Temp 手动"),
        KETTLE_JOB("Kettle Job"),
        KETTLE_TRAN("kettle transformation");
        private String desc;

        private JobGroupEnum(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public static JobGroupEnum match(String name) {
            if (name == null || name.trim().length() == 0) {
                return null;
            }
            for (JobGroupEnum group : JobGroupEnum.values()) {
                if (group.name().equals(name)) {
                    return group;
                }
            }
            return null;
        }
    }
}
