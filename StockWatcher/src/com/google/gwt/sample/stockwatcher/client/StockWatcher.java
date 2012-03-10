package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

public class StockWatcher implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("LŠgg till");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();
	private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
	private Label errorMsgLabel = new Label();
	private TextArea newNameArea = new TextArea();
	private FlexTable newTable = new FlexTable();
	private HorizontalPanel newPanel = new HorizontalPanel();
	private Button addNameButton = new Button("LŠgg till");
	private ArrayList<String> names = new ArrayList<String>();
	private MyFlexTableDragController dragController = new MyFlexTableDragController(RootPanel.get("stockList"), false, this); 
	private MyFlexTableDropController dropController = new MyFlexTableDropController(newTable, this);
	private StockPrice currentPerson = new StockPrice();
	private StockPrice droppedPerson = new StockPrice(); 

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		// Create table for stock data.
		stocksFlexTable.setText(0, 0, "Namn");
		stocksFlexTable.setText(0, 1, "Antal");
		stocksFlexTable.setText(0, 2, "Ta bort");
		// Add styles to elements in the stock list table.
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		//stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListRemoveColumn");
		stocksFlexTable.setCellPadding(6);

		//Create new table
		newTable.setText(0, 0, "Namn");
		newTable.setText(0, 1, "Antal");
		newTable.setText(0, 2, "Ta bort");
		// Add styles to elements in the stock list table.
		newTable.getRowFormatter().addStyleName(0, "watchListHeader");
		newTable.addStyleName("watchList");
		newTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		newTable.getCellFormatter().addStyleName(0, 2, "watchListRemoveColumn");
		newTable.setCellPadding(6);

		// Assemble Add Stock panel.
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		addPanel.addStyleName("addPanel");

		//Add new stuff to new panel
		newPanel.add(newNameArea);
		newPanel.add(addNameButton);
		newPanel.addStyleName("addPanel");

		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);
		mainPanel.add(errorMsgLabel);
		mainPanel.add(newTable);
		mainPanel.add(newPanel);


		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);

		// Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);

		//Associate DropController to DragController
		dragController.setBehaviorDragProxy(true);
		dragController.registerDropController(dropController);

		new Timer() {
			@Override
			public void run() {
			}
		};

		// Listen for mouse events on the Add button.
		addStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addStock();
			}
		});
		// Listen for mouse events on the AddName button.
		addNameButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				preAddName();
			}
		});
		//Listen for keyboard events in the input box.
		newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addStock();
				}
			}
		});
		//Listen for keyboard events in the input box.
		newNameArea.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					preAddName();
				}
			}
		});
		loadTable();
	}
	//Load the newTable with data from the database
	private void loadTable(){
		if (stockPriceSvc == null) {
			stockPriceSvc = GWT.create(StockPriceService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<StockPrice>> callback = new AsyncCallback<ArrayList<StockPrice>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ArrayList<StockPrice> result) {
				updateTable2(result);
				System.out.println("tji");
			}
		};
		//make the call
		stockPriceSvc.getNameList(callback);

	}

	/**Prepares a StockPrice that has been dropped to the table and
	 * calls for the add function
	 * @param stockPrice
	 */
	public void preAddNameOnDrop(StockPrice stockPrice){
		this.setDroppedPerson(stockPrice);
		String name =stockPrice.getSymbol();
		String command = "dropped";
		addName(name, command);	
	}

	/**
	 * Prepares a String that has been added to the table by the textArea and
	 * calls for the add function
	 */
	private void preAddName(){
		final String newLine = newNameArea.getText().toUpperCase().trim();
		newNameArea.setFocus(true); 
		final String newName = newLine.split(" ")[0];
		if (!newLine.matches("^[A-Z0-9' '\\.]{1,20}$")) {
			Window.alert("'" + newLine + "' is not a valid argument.");
			newNameArea.selectAll();
			return;
		}
		String command = "added";
		addName(newName, command);

	}

	/**Makes a call to the server with a string and calls for the updates table function with
	 * an arraylist of all the names in the database 
	 * @param name
	 * @param command
	 */
	private void addName(final String name, String command ) {
		if (stockPriceSvc == null) {
			stockPriceSvc = GWT.create(StockPriceService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<StockPrice>> callback = new AsyncCallback<ArrayList<StockPrice>>() {
			public void onFailure(Throwable caught) {
			}
			public void onSuccess(ArrayList<StockPrice> result) {
				updateTable2(result);
			}
		};
		if (command.equals("added")){
			// Make the call to the stock price service.
			stockPriceSvc.addStockPrice(newNameArea.getText(), callback);
			newNameArea.setText("");
		}
		if(command.equals("dropped")){
			stockPriceSvc.addStockPrice(this.droppedPerson.toString(), callback);		
		}
	}

	/**Populates the table with all the names fro the database and adds a remove button to 
	 * every one of them.
	 * @param result
	 */
	public void updateTable2(ArrayList<StockPrice> result) {
		names.clear();
		for(int i =0;i< result.size(); i++){
			final String name = result.get(i).getSymbol();
			String no = result.get(i).getPrice();
			names.add(name);
			int row= i+1;
			newTable.setWidget(row, 2, new Label());
			newTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
			newTable.getCellFormatter().addStyleName(row, 2, "watchListRemoveColumn");


			// Add a button to remove this name from the table.
			Button removeNameButton = new Button("x");
			removeNameButton.addStyleDependentName("remove");
			removeNameButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					//Ta bort frŒn databasen
					AsyncCallback<ArrayList<StockPrice>> callback = new AsyncCallback<ArrayList<StockPrice>>() {
						public void onFailure(Throwable caught) {
							// If the stock code is in the list of delisted codes, display an error message.
						}
						public void onSuccess(ArrayList<StockPrice> result) {
							int removedIndex = names.indexOf(name);
							names.remove(removedIndex);
							System.out.println(names);
							newTable.removeRow(removedIndex + 1);
							loadTable();
						}
					};
					// Make the call to the stock price service.
					stockPriceSvc.remove(name, callback);
				}
			});
			newTable.setWidget(row, 2, removeNameButton);
			// Populate the Table
			newTable.setText(i+1, 0, name);
			newTable.setText(i+1, 1, no);

		}

	}


	/**Updates the first table with the latest added name and number
	 * @param result
	 */
	private void updateTable(final StockPrice result) {
		int row = stocksFlexTable.getRowCount();
		stocks.add(result.getSymbol());
		stocksFlexTable.setWidget(row, 2, new Label());
		stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListRemoveColumn");


		// Add a button to remove this stock from the table.
		Button removeStockButton = new Button("x");
		removeStockButton.addStyleDependentName("remove");
		removeStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = stocks.indexOf(result.getSymbol());
				stocks.remove(removedIndex);        
				stocksFlexTable.removeRow(removedIndex+1);
			}
		});
		stocksFlexTable.setWidget(row, 2, removeStockButton);

		// Make Name a widget.
		HorizontalPanel namePanel = new HorizontalPanel();
		Label name = new Label(result.getSymbol());
		namePanel.add(name);

		//Make no a widget
		HorizontalPanel noPanel = new HorizontalPanel();
		Label no = new Label(result.getPrice());
		noPanel.add(no);

		// Populate the Table with new data.
		stocksFlexTable.setWidget(row, 0, namePanel);
		stocksFlexTable.setWidget(row, 1, noPanel);		

		name.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				currentPerson = result;
			}
		});
		no.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				currentPerson = result;

			}
		});
		dragController.makeDraggable(name);
		dragController.makeDraggable(no);
	}

	/**
	 * Add name to FlexTable. Executed when the user clicks the addStockButton or
	 * presses enter in the newSymbolTextBox.
	 */
	private void addStock() {
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		newSymbolTextBox.setFocus(true);

		// Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
		if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
			Window.alert("'" + symbol + "' is not a valid symbol.");
			newSymbolTextBox.selectAll();
			return;
		}

		// Don't add the name if it's already in the table.
		if (stocks.contains(symbol))
			return;


		// Initialize the service proxy.
		if (stockPriceSvc == null) {
			stockPriceSvc = GWT.create(StockPriceService.class);
		}
		// Set up the callback object.
		AsyncCallback<StockPrice> callback = new AsyncCallback<StockPrice>() {
			public void onFailure(Throwable caught) {
				// If the stock code is in the list of delisted codes, display an error message.
				String details = caught.getMessage();
				if (caught instanceof DelistedException) {
					details = "Company '" + ((DelistedException)caught).getSymbol() + "' was delisted";
				}

				errorMsgLabel.setText("Error: " + details);
				errorMsgLabel.setVisible(true);
			}
			//If the name is in the file of girlnames the name and number is updated to the table
			//else an popup window is displayed
			public void onSuccess(StockPrice result) {
				if (result!=null)				
					updateTable(result);
				if (result==null){
					Window.alert("'" + symbol + "' finns inte i listan.");
				}
			}
		};

		// Make the call to the stock price service.
		stockPriceSvc.getPrices(newSymbolTextBox.getText(), callback);
		newSymbolTextBox.setText("");


	}
	//The stockPrice beeing dragged 
	public StockPrice getCurrentPerson(){
		return this.currentPerson;
	}
	//The last dropped person
	public void setDroppedPerson(StockPrice dropped){
		droppedPerson.setSymbol(dropped.getSymbol());
		droppedPerson.setPrice(dropped.getPrice());
	}

}