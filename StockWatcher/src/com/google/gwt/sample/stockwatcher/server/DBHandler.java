package com.google.gwt.sample.stockwatcher.server;


import java.sql.*;
import java.util.ArrayList;

import com.google.gwt.sample.stockwatcher.client.StockPrice;

/**
 * @author meckis
 *
 */
/**
 * @author meckis
 *
 */
public class DBHandler {


	/**Adds a name to the database if the name hasn't already been added
	 * else updates the name. 
	 * @param name
	 * @param no
	 * @throws SQLException
	 */
	public void add(String name, String no) throws SQLException {
		Connection conn = this.connect();
		Statement stat = conn.createStatement();
		ResultSet names = stat .executeQuery("SELECT * from nameList");
		boolean foundName = false;
		while (names.next()){
			if (names.getString("name").equals(name)){
				foundName=true;
				break;
			}
		}
		if (foundName==false){
			PreparedStatement prep = conn.prepareStatement("INSERT INTO nameList (name, no) VALUES ( ?, ?)");
			prep.setString(1, name);
			prep.setString(2, no);
			prep.execute();
			prep.close();
		}
		else update(name, no);
	}

	/**Removes the name from the database
	 * @param name
	 * @throws SQLException
	 */
	public void remove(String name) throws SQLException{
		Connection conn = this.connect();
		PreparedStatement prep = conn.prepareStatement("DELETE from nameList where name = ? ");
		prep.setString(1, name);
		prep.execute();
		prep.close();
	}

	/**Updates the name in the database
	 * @param name
	 * @param no
	 * @throws SQLException
	 */
	public void update(String name, String no) throws SQLException {
		Connection conn = this.connect();
		PreparedStatement prep = conn.prepareStatement("UPDATE nameList SET no =? where name= ?");
		prep.setString(1, no);
		prep.setString(2, name);
		prep.executeUpdate();
		prep.close();
	}
	
	/**Returns the content in the database
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<StockPrice> getNameList() throws SQLException{
		ArrayList<StockPrice> nameList = new ArrayList<StockPrice>();
		Connection conn = this.connect();
		Statement stat =conn.createStatement();
		ResultSet rs = stat.executeQuery("Select * from nameList");
		while (rs.next()){
			String name = rs.getString("name");
			String  no = rs.getString("no");
			StockPrice stockPrice = new StockPrice(name, no);
			nameList.add(stockPrice);	
		}
		return nameList;	
	}
	
	/**
	 * Creates a connection to the database
	 * @return Connection
	 */
	public Connection connect(){
		Connection dbConnection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(Exception ex){
			System.out.println( "Unable to load the driver class!" );
		}
		try{
			dbConnection = DriverManager.getConnection("jdbc:mysql://www-und.ida.liu.se/micov348",
					"micov348", "micov3481ba3");
		}
		catch( SQLException x ){
			System.out.println( "Couldn't get connection!" );
		}
		return dbConnection;
	}
}


