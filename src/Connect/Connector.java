package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bundit_put
 */
public class Connector {

    private Connection conn;
    private PreparedStatement pstmt;
    private Statement stmt;
    private ResultSet rs;
    private String host;
    private String username;
    private String password;
    private String dbName;
    private String port = "1521";

    public Connector() {
        this.host = ConfigGateway.getHost();
        this.username = ConfigGateway.getUser();
        this.password = ConfigGateway.getPass();
        this.dbName = ConfigGateway.getDbname();
    }

    public Connector(String host, String username, String password, String dbName, String port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
    }

    public void connect() throws Exception {
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        conn = (Connection) DriverManager.getConnection("jdbc:sqlserver://" + host + ";databaseName=" + dbName, username, password);

        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName, username, password);

        stmt = conn.createStatement();
    }

    public ResultSet executeQuery(String sql) throws Exception {
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        return rs;
    }

    public ResultSet executeQuery() throws Exception {
        rs = pstmt.executeQuery();
        return rs;
    }

    public boolean execute(String sql) throws Exception {
        pstmt = conn.prepareStatement(sql);
        return pstmt.execute(sql);
    }

    public boolean execute() throws Exception {
        return pstmt.execute();
    }

    public int executeUpdate(String sql) throws Exception {
        pstmt = conn.prepareStatement(sql);
        return pstmt.executeUpdate();
    }

    public int executeUpdate() throws Exception {
        return pstmt.executeUpdate();
    }

    public void addBatch(String sqlCmd) throws Exception {
        stmt.addBatch(sqlCmd);
    }

    public void addBatch() throws Exception {
        pstmt.addBatch();
    }

    public int[] executeBatch() throws Exception {
        return stmt.executeBatch();
    }

    public void clearBatch() throws Exception {
        stmt.clearBatch();
    }

    public void setStatement(String sql) throws Exception {
        pstmt = conn.prepareStatement(sql);
    }

    public void setString(int parameterIndex, String value) throws Exception {
        pstmt.setString(parameterIndex, value);
    }

    public void setInt(int parameterIndex, int value) throws Exception {
        pstmt.setInt(parameterIndex, value);
    }

    public void setDouble(int parameterIndex, Double value) throws Exception {
        pstmt.setDouble(parameterIndex, value);
    }

    public ResultSet getResult() throws Exception {
        return pstmt.getResultSet();
    }

    public int getGeneratedKeys() throws Exception {
        int key = -1;
        rs = pstmt.getGeneratedKeys();

        if (rs.next()) {
            key = (int) rs.getLong(1);
        }

        return key;
    }

    public void setAutoCommit(boolean b) throws Exception {
        conn.setAutoCommit(b);
    }

    public void commit() throws Exception {
        conn.commit();
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
