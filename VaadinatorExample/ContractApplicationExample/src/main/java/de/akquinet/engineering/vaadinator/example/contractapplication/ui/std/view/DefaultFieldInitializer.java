package de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view;

import org.vaadin.viritin.fields.EagerValidateable;

import com.vaadin.event.FieldEvents.TextChangeNotifier;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import de.akquinet.engineering.vaadinator.example.contractapplication.ui.view.FieldInitializer;

public class DefaultFieldInitializer implements FieldInitializer {

	@Override
	public void initializeField(Component component, Component view) {
		if (view instanceof EagerValidatableView) {
			EagerValidatableView eagerValidatableView = (EagerValidatableView) view;
			if (component instanceof Field<?>) {
				((AbstractComponent) component).setImmediate(true);
				((Field<?>) component).addValueChangeListener(eagerValidatableView);
				if (component instanceof EagerValidateable) {
					((EagerValidateable) component).setEagerValidation(true);
				}
				if (component instanceof TextChangeNotifier) {
					final TextChangeNotifier abstractTextField = (TextChangeNotifier) component;
					abstractTextField.addTextChangeListener(eagerValidatableView);
				}
			}
		}
	}

}
