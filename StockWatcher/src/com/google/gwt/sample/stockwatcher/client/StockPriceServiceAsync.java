package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {

  void getPrices(String name, AsyncCallback<StockPrice> callback);

  void addStockPrice(String text, AsyncCallback<ArrayList<StockPrice>> callback);
  
  void getNameList(AsyncCallback<ArrayList<StockPrice>> callback);
  
  void remove(String text, AsyncCallback<ArrayList<StockPrice>> callback);

}