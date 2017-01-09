package de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view;

import java.util.Map;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.contractapplication.ui.view.ButtonFactory;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.view.ExceptionMappingStrategy;

public abstract class ContractApplicationEditViewImplEx<O extends ContractApplicationEditView.Observer>
		extends ContractApplicationEditViewImpl<O> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplicationEditViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy,
			ButtonFactory buttonFactory) {
		super(exceptionMappingStrategy, buttonFactory);
	}

	@Override
	public void onValidationError(Map<Field<?>, InvalidValueException> validationErrors) {
		super.onValidationError(validationErrors);
		StringBuilder sb = new StringBuilder();
		for(Field<?> field: validationErrors.keySet()) {
			if(sb.length()!= 0) {
				sb.append(", ");
			}
			sb.append(field.getCaption());		
		}
		Notification.show("Fehler in folgenden Feldern:", sb.toString(), Notification.Type.ERROR_MESSAGE);
	}
}
