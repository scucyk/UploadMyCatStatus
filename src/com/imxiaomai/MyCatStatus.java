package com.imxiaomai;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by apple on 18/1/16.
 * 获取每个mycat的datahost的状态
 */
public class MyCatStatus {
    private static Logger logger = Logger.getLogger(com.imxiaomai.MyCatStatus.class);
    private ConfigHelper ch;

    public MyCatStatus()
    {
        ch = new ConfigHelper();
    }

    public Map<String,Integer> getDataHostStatus()
    {
        String dnindexPath = ch.getDnindexPath();
        String dataHosts = ch.getDataHosts();
        String[] dataHostArray = dataHosts.split(",");
        Properties prop = new Properties();
        Map<String, Integer> dataHostMap = new HashMap<>();

        try {
            prop.load(new FileInputStream(dnindexPath));
        }catch (IOException ex)
        {
            logger.error(ex);
        }

        for(String dataHost: dataHostArray)
        {
            Integer index = Integer.valueOf(prop.getProperty(dataHost));
            dataHostMap.put(dataHost, index);
        }

        return dataHostMap;
    }
}
