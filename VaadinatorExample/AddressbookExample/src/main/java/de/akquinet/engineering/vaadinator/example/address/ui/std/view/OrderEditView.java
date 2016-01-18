package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

public interface OrderEditView extends java.io.Serializable {

	public void initializeUi();

	public Object getComponent();
	
	public String getWhat();

	public void setWhat(String newWhat);

	public void setChoicesForWhat(java.util.List<String> choicesForWhat);

	public String getForWhom();

	public void setForWhom(String newForWhom);

	public boolean checkAllFieldsValid();

	public void showErrorMessage(String message);

	public void setObserver(OrderEditView.Observer observer);

	public static interface Observer {

		public void onCancel();

		public void onSave();

	}

}