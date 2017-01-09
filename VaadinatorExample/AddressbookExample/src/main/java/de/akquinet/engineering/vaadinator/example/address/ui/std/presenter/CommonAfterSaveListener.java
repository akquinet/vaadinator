package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.address.ui.presenter.listener.AfterSaveListener;

public class CommonAfterSaveListener implements AfterSaveListener<Object> {

	@Override
	public void afterSave(Object entity) {
		Notification.show("Gespeichert");
	}
}
