package com.imxiaomai;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by apple on 18/1/16.
 * 读取config.properties配置文件
 */
public class ConfigHelper
{
    private static Logger logger = Logger.getLogger(com.imxiaomai.ConfigHelper.class);
    private Properties prop;

    public ConfigHelper()
    {
        prop = new Properties();
        try
        {
            prop.load(new FileInputStream("config.properties"));
        }
        catch (IOException ex)
        {
            logger.error(ex);
        }
    }

    public String getDataHosts()
    {
        return prop.getProperty("dataHost");
    }

    public String getDnindexPath()
    {
        return prop.getProperty("dnindexPath");
    }

    public String getMyCatHost()
    {
        return prop.getProperty("myCatHost");
    }
}
