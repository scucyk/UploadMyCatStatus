package com.imxiaomai;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.*;

/**
 * Created by apple on 18/1/16.
 * 上传dnindex.properties状态信息至数据库
 */
public class UploadMyCatStatus {
    private static Logger logger = Logger.getLogger(com.imxiaomai.UploadMyCatStatus.class);
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static void main(String[] args){
        try (
            Connection conn = ds.getConnection();
            PreparedStatement queryDataHost = conn.prepareStatement("select dnindex from mycat where mycat_host = ? and data_host = ?");
            PreparedStatement updateDataHost = conn.prepareStatement("update mycat set dnindex = ?, flag = 1 where mycat_host = ? and data_host=?");
        ){
            ConfigHelper ch = new ConfigHelper();
            String myCatHost = ch.getMyCatHost();

            //循环检查dnindex.properties文件
            while(true) {
                logger.info("检查MyCat dnindex.properties文件");
                MyCatStatus mcs = new MyCatStatus();
                Map<String, Integer> dataHostMap = mcs.getDataHostStatus();

                dbOperator(conn, myCatHost, queryDataHost, updateDataHost, dataHostMap);
                try {
                    Thread.sleep(60000);
                }
                catch (InterruptedException ex)
                {
                    logger.error(ex);
                }
            }
        }catch (SQLException ex)
        {
            logger.error(ex);
        }
    }

    //将mycat的dnindex.properties信息写入数据库
    public static void dbOperator(Connection conn, String myCatHost, PreparedStatement queryDataHost,
                                  PreparedStatement updateDataHost, Map<String, Integer> dataHostMap)
    {
            for(String dataHost: dataHostMap.keySet())
            {
                int index = dataHostMap.get(dataHost);
                try {
                    queryDataHost.setString(1, myCatHost);
                    queryDataHost.setString(2, dataHost);
                    ResultSet rs = queryDataHost.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt(1) != index) {
                            logger.warn(myCatHost + "后端数据库发生主从切换，dataHost: " + dataHost + ", index切换前的值是: "
                                    + rs.getInt(1) + ", 切换后的值是: " + index);
                            updateDataHost.setInt(1, index);
                            updateDataHost.setString(2, myCatHost);
                            updateDataHost.setString(3, dataHost);
                            updateDataHost.executeUpdate();
                        }
                    }
                }catch (SQLException ex)
                {
                    logger.equals(ex);
                }
            }
    }
}
