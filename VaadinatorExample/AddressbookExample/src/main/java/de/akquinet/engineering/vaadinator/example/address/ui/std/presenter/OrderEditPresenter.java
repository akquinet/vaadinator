package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import de.akquinet.engineering.vaadinator.example.address.model.Order;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.OrderEditView;

public interface OrderEditPresenter extends java.io.Serializable {

	public void startPresenting();

	public OrderEditView getView();

	public Order getOrder();

	public void setOrder(Order newOrder);

}