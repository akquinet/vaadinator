package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class OrderEditViewImpl extends com.vaadin.ui.CustomComponent implements OrderEditView {

	private static final long serialVersionUID = 1L;

	private OrderEditView.Observer observer;

	// (all UI elements should be protected)
	protected com.vaadin.ui.VerticalLayout mainLayout = new com.vaadin.ui.VerticalLayout();
	protected com.vaadin.ui.HorizontalLayout buttonLayout = new com.vaadin.ui.HorizontalLayout();
	protected com.vaadin.ui.FormLayout editLayout = new com.vaadin.ui.FormLayout();
	protected com.vaadin.ui.Button cancel = new com.vaadin.ui.Button();
	protected com.vaadin.ui.Button save = new com.vaadin.ui.Button();

	protected ComboBox fieldWhat = new ComboBox();

	public String getWhat() {
		return (String) fieldWhat.getValue();
	}

	public void setWhat(String newWhat) {
		this.fieldWhat.setValue(newWhat);
	}

	public void setChoicesForWhat(java.util.List<String> choicesForWhat) {
		for (String item : choicesForWhat) {
			fieldWhat.addItem(item);
			fieldWhat.setItemCaption(item, item);
		}
	}
	
	protected TextField fieldForWhom = new TextField();

	public String getForWhom() {
		return (String) fieldForWhom.getValue();
	}

	public void setForWhom(String newForWhom) {
		this.fieldForWhom.setValue(newForWhom);
	}

	@Override
	public void initializeUi() {
		fieldWhat.setCaption("What");
		editLayout.addComponent(fieldWhat);
		fieldForWhom.setCaption("For whom");
		editLayout.addComponent(fieldForWhom);
		cancel.addClickListener(new com.vaadin.ui.Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				OrderEditViewImpl.this.observer.onCancel();
			}

		});
		buttonLayout.addComponent(cancel);
		save.addClickListener(new com.vaadin.ui.Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				OrderEditViewImpl.this.observer.onSave();
			}

		});
		buttonLayout.addComponent(save);
		mainLayout.addComponent(editLayout);
		mainLayout.addComponent(buttonLayout);
	}

	@Override
	public Object getComponent() {
		return this;
	}

	@Override
	public boolean checkAllFieldsValid() {
		try {
			fieldWhat.validate();
			fieldForWhom.validate();
		} catch (com.vaadin.data.Validator.InvalidValueException e) {
			return false;
		}
		return true;
	}

	@Override
	public void showErrorMessage(String message) {
		com.vaadin.ui.Notification.show(message, com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void setObserver(OrderEditView.Observer observer) {
		this.observer = observer;
	}

}