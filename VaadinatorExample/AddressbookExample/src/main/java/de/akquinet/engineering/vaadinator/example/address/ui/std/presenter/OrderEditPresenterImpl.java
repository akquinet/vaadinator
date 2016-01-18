package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import java.util.Arrays;
import java.util.Map;

import de.akquinet.engineering.vaadinator.example.address.model.Order;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.OrderEditView;

public class OrderEditPresenterImpl implements OrderEditPresenter, OrderEditView.Observer {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> context;
	private OrderEditView view;
	private Order myOrder = new Order();

	public OrderEditPresenterImpl(Map<String, Object> context, OrderEditView view) {
		super();
		this.context = context;
		this.view = view;
	}

	@Override
	public Order getOrder() {
		return myOrder;
	}

	@Override
	public void setOrder(Order newOrder) {
		this.myOrder = newOrder;
	}

	@Override
	public OrderEditView getView() {
		return view;
	}

	@Override
	public void startPresenting() {
		view.setObserver(this);
		view.initializeUi();
		view.setChoicesForWhat(Arrays.asList("Mockingjay I", "Mockingjay II"));
		loadFromModel();
	}

	protected void loadFromModel() {
		view.setWhat(getOrder().getWhat());
		view.setForWhom(getOrder().getForWhom());
	}

	protected void saveToModel() {
		getOrder().setWhat(view.getWhat());
		getOrder().setForWhom(view.getForWhom());
	}

	@Override
	public void onSave() {
		if (!view.checkAllFieldsValid()) {
			return;
		}
		saveToModel();
	}

	@Override
	public void onCancel() {
	}

}