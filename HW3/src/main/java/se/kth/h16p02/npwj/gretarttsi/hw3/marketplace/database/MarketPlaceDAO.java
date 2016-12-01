package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MarketPlaceDAO
{
    private static final String TRADER_TABLE_NAME = "TRADER";
    private static final String USERNAME_COLUMN_NAME = "USERNAME";
    private static final String PASSWORD_COLUMN_NAME = "PASSWORD";

    private static final String SALEITEM_TABLE_NAME = "SALEITEM";
    private static final String WISHLISTITEM_TABLE_NAME = "WISHLISTITEM";
    private static final String ITEMNAME_COLUMN_NAME = "ITEMNAME";
    private static final String PRICE_COLUMN_NAME = "PRICE";
    private static final String SOLD_COLUMN_NAME = "SOLD";
    private static final String BOUGHT_COLUMN_NAME = "BOUGHT";

    private PreparedStatement createTraderStmt;
    private PreparedStatement findTraderStmt;
    private PreparedStatement getAllTradersStmt;
    private PreparedStatement insertSaleItem;
    private PreparedStatement getSaleItemsForTrader;
    private PreparedStatement insertWishListItem;
    private PreparedStatement getWishListItemsForTrader;
    private PreparedStatement setSaleItemSold;
    private PreparedStatement setWishListItemBought;

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

    private Connection createDataSource () throws ClassNotFoundException, SQLException, MarketplaceDBException
    {
        Connection connection = getConnection();
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet rs = dbm.getTables(null, null, null, null);

        // Check what tables exist

        boolean traderTableExists = false;
        boolean saleItemTableExists = false;
        boolean wishlistItemTableExists = false;

        while (rs.next())
        {
            if (rs.getString(tableNameColumn).equals(TRADER_TABLE_NAME))
                traderTableExists = true;
            else if (rs.getString(tableNameColumn).equals(SALEITEM_TABLE_NAME))
                saleItemTableExists = true;
            else if (rs.getString(tableNameColumn).equals(WISHLISTITEM_TABLE_NAME))
                wishlistItemTableExists = true;
        }

        rs.close();

        // Create missing tables

        if (!traderTableExists)
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + TRADER_TABLE_NAME
                    + " (" + USERNAME_COLUMN_NAME + " VARCHAR(32) PRIMARY KEY, "
                    + PASSWORD_COLUMN_NAME + " VARCHAR(32))");
        }

        if (!saleItemTableExists)
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + SALEITEM_TABLE_NAME
                    + " ("
                    + ITEMNAME_COLUMN_NAME + " VARCHAR(32), "
                    + PRICE_COLUMN_NAME + " INT, "
                    + USERNAME_COLUMN_NAME + " VARCHAR(32), "
                    + SOLD_COLUMN_NAME + " BOOLEAN, "
                    + "PRIMARY KEY (" + ITEMNAME_COLUMN_NAME + "," + PRICE_COLUMN_NAME + "), "
                    + "FOREIGN KEY(" + USERNAME_COLUMN_NAME + ") REFERENCES " + TRADER_TABLE_NAME + "(" + USERNAME_COLUMN_NAME + ")"
                    + ")"
            );
        }

        if (!wishlistItemTableExists)
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + WISHLISTITEM_TABLE_NAME
                    + " ("
                    + ITEMNAME_COLUMN_NAME + " VARCHAR(32), "
                    + PRICE_COLUMN_NAME + " INT, "
                    + USERNAME_COLUMN_NAME + " VARCHAR(32), "
                    + BOUGHT_COLUMN_NAME + " BOOLEAN, "
                    + "PRIMARY KEY (" + ITEMNAME_COLUMN_NAME + "," + PRICE_COLUMN_NAME + "," + USERNAME_COLUMN_NAME + "), "
                    + "FOREIGN KEY(" + USERNAME_COLUMN_NAME + ") REFERENCES " + TRADER_TABLE_NAME + "(" + USERNAME_COLUMN_NAME + ")"
                    + ")"
            );
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

        insertSaleItem = connection.prepareStatement("INSERT INTO " + SALEITEM_TABLE_NAME + " VALUES (?, ?, ?, ?)");
        insertWishListItem = connection.prepareStatement("INSERT INTO " + WISHLISTITEM_TABLE_NAME + " VALUES (?, ?, ?, ?)");

        getSaleItemsForTrader = connection.prepareStatement("SELECT * from " + SALEITEM_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = ?");
        getWishListItemsForTrader = connection.prepareStatement("SELECT * from " + WISHLISTITEM_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = ?");
        
        setSaleItemSold = connection.prepareStatement(
                "UPDATE " + SALEITEM_TABLE_NAME
                + " SET " + SOLD_COLUMN_NAME + "=?"
                + " WHERE " + ITEMNAME_COLUMN_NAME + "=? AND " + PRICE_COLUMN_NAME + "=?"
        );
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
            if (result.next())
            {
                return result.getString(PASSWORD_COLUMN_NAME);
            } else
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

    public void createSaleItem (String itemname, int price, String username, boolean sold) throws MarketplaceDBException
    {
        System.out.println("Adding sale item [" + itemname + "," + price + "," + username + "] to database");

        String failureMsg = "DatabaseError: Could not add sale item";
        try
        {
            insertSaleItem.setString(1, itemname);
            insertSaleItem.setInt(2, price);
            insertSaleItem.setString(3, username);
            insertSaleItem.setBoolean(4, sold);
            int rowsCreated = insertSaleItem.executeUpdate();
            if (rowsCreated != 1)
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

    public void createWishlistItem (String itemname, int price, String username, boolean bought) throws MarketplaceDBException
    {
        System.out.println("Adding wish list item [" + itemname + "," + price + "," + username + "] to database");

        String failureMsg = "DatabaseError: Could not add wish list item";
        try
        {
            insertWishListItem.setString(1, itemname);
            insertWishListItem.setInt(2, price);
            insertWishListItem.setString(3, username);
            insertWishListItem.setBoolean(4, bought);
            int rowsCreated = insertWishListItem.executeUpdate();
            if (rowsCreated != 1)
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

    public ArrayList<SaleItem> getSaleItemsByUsername(String username) throws MarketplaceDBException
    {
        String failureMsg = "Database Error: Could not get sale items for user: " + username;
        ResultSet result = null;

        ArrayList<SaleItem> saleItems = new ArrayList<>();

        try
        {
            getSaleItemsForTrader.setString(1, username);
            result = getSaleItemsForTrader.executeQuery();
            while (result.next())
            {
                SaleItem foundSaleItem = new SaleItem(
                        new Item(result.getString(ITEMNAME_COLUMN_NAME), new BigDecimal(result.getInt(PRICE_COLUMN_NAME))),
                        result.getString(USERNAME_COLUMN_NAME)
                );

                saleItems.add(foundSaleItem);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
            throw new MarketplaceDBException(failureMsg);
        }
        finally
        {
            try
            {
                result.close();
            }
            catch (Exception e)
            {
                System.err.println(e);
                throw new MarketplaceDBException(failureMsg);
            }
        }

        return saleItems;
    }

    public ArrayList<WishListItem> getWishlistItemsByUsername(String username) throws MarketplaceDBException
    {
        String failureMsg = "Database Error: Could not get wish list items for user: " + username;
        ResultSet result = null;

        ArrayList<WishListItem> wishListItems = new ArrayList<>();

        try
        {
            getWishListItemsForTrader.setString(1, username);
            result = getWishListItemsForTrader.executeQuery();
            while (result.next())
            {
                WishListItem foundWishlistItem = new WishListItem(
                        result.getString(USERNAME_COLUMN_NAME),
                        new Item(result.getString(ITEMNAME_COLUMN_NAME), new BigDecimal(result.getInt(PRICE_COLUMN_NAME)))
                );

                wishListItems.add(foundWishlistItem);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
            throw new MarketplaceDBException(failureMsg);
        }
        finally
        {
            try
            {
                result.close();
            }
            catch (Exception e)
            {
                System.err.println(e);
                throw new MarketplaceDBException(failureMsg);
            }
        }

        return wishListItems;
    }

    public void setSaleItemSold(String itemname, int price, boolean isSold) throws MarketplaceDBException
    {
        System.out.println(
                "Setting sale item [" + itemname + "," + price + "] to sold: " + isSold + " in database");

        String failureMsg = "DatabaseError: Could not set item sold";
        try
        {
            setSaleItemSold.setBoolean(1, isSold);
            setSaleItemSold.setString(2, itemname);
            setSaleItemSold.setInt(3, price);
            int rowsAffected = setSaleItemSold.executeUpdate();
            if (rowsAffected == 0)
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
}
