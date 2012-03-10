package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.DelistedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * @author meckis
 *
 */
public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

	private DBHandler dB = new DBHandler();
	private File file = new File("/Users/meckis/Documents/flicknamn2011.csv");
	private Scanner scanner; 


	public StockPrice getPrices(String name) throws DelistedException {
		StockPrice price = null;
		String no = "no";
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			if ((line.split(" "))[0].equalsIgnoreCase(name)){
				no = line.split(" ")[1];
				System.out.println(no);
				price = new StockPrice(name,no);
			}
		}
		return price;
	}
	public ArrayList<StockPrice> addStockPrice(String nameLine){
		ArrayList<StockPrice> price = new ArrayList<StockPrice>();
		final String name;
		final String no;	
		name=nameLine.split(" ")[0];
		no=nameLine.split(" ")[1];
		try {
			dB.add(name, no);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			price =dB.getNameList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
	}

	public ArrayList<StockPrice> getNameList(){
		ArrayList<StockPrice> price = new ArrayList<StockPrice>();
		try {
			price =dB.getNameList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
	}	

   public ArrayList<StockPrice> remove(String name){
	   ArrayList<StockPrice> price = new ArrayList<StockPrice>();
		try {
			dB.remove(name);
		} catch (SQLException e) {
			
		}
		try {
			price =dB.getNameList();
		} catch (SQLException e) {
			
		}

		return price;
   }
}






