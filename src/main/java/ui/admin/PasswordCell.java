package ui.admin;

import entities.Account;
import javafx.scene.control.*;

/**
 * A {@code TableCell} containing a {@link PasswordField} that outputs
 */
class PasswordCell extends TableCell<Account, String> {
	private final PasswordField passwordField = new PasswordField();

	public PasswordCell() {
		// Commit edit upon losing focus
		passwordField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
			if (wasFocused && !isFocused) {
				// This works even when the user presses escape because we clear the
				// password field there.
				commitEdit();
			}
		});

		passwordField.setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case ENTER:
				case TAB:
					commitEdit();
					getTableRow().requestFocus();
					break;
				case ESCAPE:
					cancelEdit();
					getTableRow().requestFocus();
					break;
			}
		});
	}

	public void commitEdit() {
		commitEdit(passwordField.getText());
	}

	private void concealField() {
		setText("*****");
		setContentDisplay(ContentDisplay.TEXT_ONLY);
		setGraphic(null);
	}

	@Override
	protected void updateItem(String value, boolean empty) {
		super.updateItem(value, empty);

		if (empty || value == null) {
			setText(null);
			setGraphic(null);
		} else if (!isEditing()) {
			concealField();
		}
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (!isEditing()) {
			// Only clear if not already editing
			passwordField.clear();
		}

		setGraphic(passwordField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setText(null);
		passwordField.requestFocus();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		passwordField.clear();
		concealField();
	}
}
