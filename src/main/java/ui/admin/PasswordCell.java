package ui.admin;

import entities.Account;
import javafx.scene.control.*;

/**
 * A {@code TableCell} containing a {@link PasswordField} that outputs
 */
class PasswordCell extends TableCell<Account, char[]> {
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
		commitEdit(getPasswordAsArray());
	}

	private void concealField() {
		setText("*****");
		setContentDisplay(ContentDisplay.TEXT_ONLY);
		setGraphic(null);
	}

	@Override
	protected void updateItem(char[] value, boolean empty) {
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

	/**
	 * Get the password from the PasswordField as a char[], trying not to make
	 * it into a string.
	 */
	private char[] getPasswordAsArray() {
		CharSequence content = passwordField.getCharacters();
		char[] password = new char[content.length()];

		// OpenJFX stores TextField/PasswordField data in a StringBuilder, and OpenJDK's
		// StringBuilder stores its contents in a char[]. So we can use that to access the
		// char array almost directly.
		if (content instanceof StringBuilder) {
			StringBuilder buffer = (StringBuilder) content;

			// Copy the StringBuilder's internal data into our own array.
			buffer.getChars(0, buffer.length(), password, 0);

			// Blank the StringBuilder's internal buffer by overwriting it with .replace
			// and a string of null bytes.
			buffer.replace(0, buffer.length(), new String(new char[buffer.length()]));
			// If we use buffer.delete here, the TextField will get confused and crash
			// when we clear it below.
		} else {
			// If we didn't get a StringBuilder, we're not using the OpenJFX PasswordField,
			// and we don't know what's inside 'content', but we can copy the data byte by
			// byte from it and hope that it's using an array internally and that it's
			// cleared along with the password field.
			//
			// To do better than this, we'd probably need to reimplement TextField from
			// scratch ourselves.
			for (int i = 0; i < password.length; ++i) {
				password[i] = content.charAt(i);
			}
		}
		passwordField.clear();
		return password;
	}
}
