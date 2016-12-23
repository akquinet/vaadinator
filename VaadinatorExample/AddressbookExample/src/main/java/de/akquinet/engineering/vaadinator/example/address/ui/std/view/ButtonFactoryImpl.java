package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import de.akquinet.engineering.vaadinator.example.address.ui.view.DefaultButtonFactory;

public class ButtonFactoryImpl extends DefaultButtonFactory {

	@Override
	public void initNewButton(Button newButton, String caption, String style, ClickListener clickListener) {
		super.initNewButton(newButton, caption, style, clickListener);
		newButton.setIcon(FontAwesome.PLUS);
	}

	@Override
	public void initRemoveButton(Button removeButton, String caption, String style, ClickListener clickListener) {
		super.initRemoveButton(removeButton, caption, style, clickListener);
		removeButton.setIcon(FontAwesome.TRASH_O);
	}

	@Override
	public void initSaveButton(Button saveButton, String caption, String style, ClickListener clickListener) {
		super.initSaveButton(saveButton, caption, style, clickListener);
		saveButton.setIcon(FontAwesome.SAVE);
	}
}
