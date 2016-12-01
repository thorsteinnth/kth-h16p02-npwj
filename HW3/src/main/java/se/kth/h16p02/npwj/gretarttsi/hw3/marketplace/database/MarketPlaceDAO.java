package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.sql.*;

/**
 * Created by GretarAtli on 30/11/2016.
 */
public class MarketPlaceDAO {

    private static final String TRADER_TABLE_NAME = "TRADER";
    private static final String USERNAME_COLUMN_NAME = "USERNAME";
    private static final String PASSWORD_COLUMN_NAME = "PASSWORD";
    private static final String ITEM_TABLE_NAME = "ITEM";
    private static final String SALEITEM_TABLE_NAME = "SALEITEM";
    private static final String WISHLISTITEM_TABLE_NAME = "WISHLISTITEM";
    private PreparedStatement createTraderStmt;
    private PreparedStatement findTraderStmt;
    private PreparedStatement getAllTradersStmt;

    public MarketPlaceDAO() throws MarketplaceDBException
    {
        try
        {
            Connection connection = createDataSource();
            prepareStatements(connection);
            System.out.println("marketplaceDAO - connection ready");
        }
        catch (ClassNotFoundException | SQLException exception)
        {
            System.err.println(exception.getMessage());
            throw new MarketplaceDBException("Could not connect to datasource.");
        }
    }

    private Connection createDataSource () throws
            ClassNotFoundException, SQLException, MarketplaceDBException
    {
        Connection connection = getConnection();
        boolean exist = false;
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();) {
            if (rs.getString(tableNameColumn).equals(TRADER_TABLE_NAME)) {
                exist = true;
                rs.close();
                break;
            }
        }

        if (!exist) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + TRADER_TABLE_NAME
                    + " (" + USERNAME_COLUMN_NAME + " VARCHAR(32) PRIMARY KEY, "
                    + PASSWORD_COLUMN_NAME + " VARCHAR(32))");
        }
        return connection;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException, MarketplaceDBException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:marketPlacejdbc.sqlite");
    }

    private void prepareStatements(Connection connection) throws SQLException
    {
        createTraderStmt = connection.prepareStatement("INSERT INTO " + TRADER_TABLE_NAME + " VALUES (?, ?)");
        findTraderStmt = connection.prepareStatement("SELECT * from " + TRADER_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = ?");
    }

    public void createTrader (String username, String password) throws MarketplaceDBException
    {
        System.out.println("Trying to create trader in database");

        String failureMsg = "DatabaseError: Could not create the trader: " + username;
        try
        {
            createTraderStmt.setString(1,username);
            createTraderStmt.setString(2,password);
            int rowsCreated = createTraderStmt.executeUpdate();
            if(rowsCreated != 1)
            {
                throw new MarketplaceDBException(failureMsg);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
            throw new MarketplaceDBException(failureMsg);
        }
    }

    public boolean traderExists(String username) throws MarketplaceDBException
    {
        String failureMsg = "Database Error: something went wrong, could not search for specified trader: " + username;
        ResultSet result = null;

        try
        {
            findTraderStmt.setString(1, username);
            result = findTraderStmt.executeQuery();
            if(result.next())
            {
                //return result.getString(USERNAME_COLUMN_NAME);
                return true;
            }
            else
            {
                //throw new TraderNotFoundException("No trader with the username: " + username);
                return false;
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
            throw new MarketplaceDBException(failureMsg);
        }
        finally
        {
            try {
                result.close();
            } catch (Exception e) {
                System.err.println(e);
                throw new MarketplaceDBException(failureMsg);
            }
        }
    }


    public String GetTradersPassword(String username) throws MarketplaceDBException, TraderNotFoundException
    {
        String failureMsg = "Database Error: something went wrong, could not search for specified trader: " + username;
        ResultSet result = null;

        try
        {
            findTraderStmt.setString(1, username);
            result = findTraderStmt.executeQuery();
            if(result.next())
            {
                return result.getString(PASSWORD_COLUMN_NAME);
            }
            else
            {
                throw new TraderNotFoundException("No trader with the username: " + username);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
            throw new MarketplaceDBException(failureMsg);
        }
        finally
        {
            try {
                result.close();
            } catch (Exception e) {
                System.err.println(e);
                throw new MarketplaceDBException(failureMsg);
            }
        }

    }

}
