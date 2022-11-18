package util;

import java.sql.Connection;
import java.sql.SQLException;
import util.ConnUtil;
/**
 * @author panhai
 * @create 2022-11-18 11:15
 */
public class TransactionManage {
    public static  void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);


    }
    public static void commit() throws SQLException {
        Connection connection=ConnUtil.getConn();
        connection.commit();
        ConnUtil.closeConn();
    }

    public static void rollback() throws SQLException {
        Connection connection=ConnUtil.getConn();
        connection.rollback();
        ConnUtil.closeConn();
    }

}
