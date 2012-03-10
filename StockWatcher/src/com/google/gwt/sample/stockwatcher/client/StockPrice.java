package com.google.gwt.sample.stockwatcher.client;
import java.io.Serializable;

public class StockPrice implements Serializable {

	 private String name;
	  private String no;

	  public StockPrice() {
	  }
	  public StockPrice(String name, String no) {
	    this.name = name;
	    this.no = no;
	    
	  }

	  public String getSymbol() {
	    return this.name;
	  }

	  public String getPrice() {
	    return this.no;
	  }

	  
	  public void setSymbol(String symbol) {
	    this.name = symbol;
	  }

	  public void setPrice(String price) {
	    this.no = price;
	  }
	  public String toString(){
		String name = this.name;
		String no = this.no;
		String str = name + " " + no;
		  return str;
		  
	  }
	}

