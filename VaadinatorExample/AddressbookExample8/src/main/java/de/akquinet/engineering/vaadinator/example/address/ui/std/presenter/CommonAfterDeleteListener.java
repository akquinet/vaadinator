package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.address.ui.presenter.listener.AfterDeleteListener;

public class CommonAfterDeleteListener implements AfterDeleteListener<Object> {

	@Override
	public void afterDelete(Object entity) {
		Notification.show("Gelöscht");
	}
}
