package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.address.ui.presenter.listener.AfterCancelListener;

public class CommonAfterCancelListener implements AfterCancelListener<Object> {

	@Override
	public void afterCancel(Object entity) {
		Notification.show("Abgebrochen");
	}
}
