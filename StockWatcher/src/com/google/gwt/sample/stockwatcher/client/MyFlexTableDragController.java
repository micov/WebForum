package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class MyFlexTableDragController extends PickupDragController {

	private StockWatcher stockwatcher;

	public MyFlexTableDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel, StockWatcher stockWatcher) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		this.stockwatcher = stockWatcher;
		boundaryPanel.getElement().getStyle().setProperty("position", "relative");
	}

	public void dragStart(){
		super.dragStart();
	}
	@Override
	protected Widget newDragProxy(DragContext context){
		HTML proxy = new HTML();
		proxy.addStyleName("watchList");
		if (stockwatcher != null && stockwatcher.getCurrentPerson() != null)
			proxy.setHTML("<table border = '1'><tr>" + "<td>" + stockwatcher.getCurrentPerson().getSymbol() + "</td> <td>"
					+ stockwatcher.getCurrentPerson().getPrice() + "</td>" + "</tr></table>");
		return proxy;
	}


}
