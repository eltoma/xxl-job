package com.xxl.job.core.log;

import com.xxl.job.core.handler.WorkerCallable;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.*;
import java.util.Date;

/**
 * store trigger log in each log-file
 *
 * @author xuxueli 2016-3-12 19:25:12
 */
public class XxlJobFileAppender extends AppenderSkeleton {


    // trogger log file path
    public static String filePath;

    public void setFilePath(String filePath) {
        XxlJobFileAppender.filePath = filePath;
    }

    @Override
    protected void append(LoggingEvent event) {
        String trigger_log_id = WorkerCallable.getLogId();
        if (trigger_log_id == null || trigger_log_id.trim().length() == 0) {
            return;
        }

        // filePath/
        File filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdirs();
        }

        // filePath/yyyy-MM-dd/
        String nowFormat = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
        File filePathDateDir = new File(filePathDir, nowFormat);
        if (!filePathDateDir.exists()) {
            filePathDateDir.mkdirs();
        }

        // filePath/yyyy-MM-dd/9999.log
        String logFileName = trigger_log_id.concat(".log");
        File logFile = new File(filePathDateDir, logFileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // append file content
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(logFile, true);
                fos.write(layout.format(event).getBytes("utf-8"));
                if (layout.ignoresThrowable()) {
                    String[] throwableInfo = event.getThrowableStrRep();
                    if (throwableInfo != null) {
                        for (int i = 0; i < throwableInfo.length; i++) {
                            fos.write(throwableInfo[i].getBytes("utf-8"));
                            fos.write(Layout.LINE_SEP.getBytes("utf-8"));
                        }
                    }
                }
                fos.flush();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean requiresLayout() {
        // TODO Auto-generated method stub
        return false;
    }


    /**
     * read data from line num
     *
     * @param logFile
     * @param fromLineNum
     * @return
     */
    public static String readLinesFrom(File logFile, int fromLineNum) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(logFile));

            // sBuffer
            StringBuffer sBuffer = new StringBuffer();
            String line = null;
            int maxLineNum = 0;
            while ((line = reader.readLine()) != null) {
                maxLineNum++;
                if (reader.getLineNumber() >= fromLineNum) {
                    sBuffer.append(line).append("\n");
                }
            }

            System.out.println("maxLineNum : " + maxLineNum);
            return sBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

        /*
        // it will return the number of characters actually skipped
        reader.skip(Long.MAX_VALUE);	
        int maxLineNum = reader.getLineNumber();  
        maxLineNum++;	// 最大行号
        */
    }

}
