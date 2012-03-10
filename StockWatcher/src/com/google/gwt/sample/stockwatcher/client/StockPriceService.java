package com.google.gwt.sample.stockwatcher.client;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("stockPrices")

public interface StockPriceService extends RemoteService {

	  StockPrice getPrices(String name) throws DelistedException;
	  ArrayList<StockPrice> addStockPrice(String text);
	  ArrayList<StockPrice> getNameList();
	  ArrayList<StockPrice> remove(String text);

	}

