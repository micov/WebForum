package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;


public class MyFlexTableDropController extends AbstractDropController {

	private StockWatcher stockwatcher;
	private StockPrice currentPerson;


	public MyFlexTableDropController(FlexTable newTable, StockWatcher stockwatcher) {
		super(newTable);
		this.stockwatcher = stockwatcher;
	}

	/* When the StockPrice has been dropped it calls for a function to add it to the new table
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	public void onDrop(DragContext context){
		super.onDrop(context);
		this.currentPerson= stockwatcher.getCurrentPerson();
		stockwatcher.preAddNameOnDrop(this.currentPerson);

	}
}
