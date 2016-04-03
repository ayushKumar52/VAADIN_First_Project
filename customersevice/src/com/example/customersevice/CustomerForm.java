package com.example.customersevice;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {
	private TextField firstName = new TextField();
	private TextField lastName = new TextField();
	private TextField email = new TextField("Email");
	private NativeSelect status = new NativeSelect("Status");
	private PopupDateField birthDate = new PopupDateField("DOB");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");

	private CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private CustomerseviceUI myui;

	public CustomerForm(CustomerseviceUI ui) {
		this.myui = ui;
		status.addItems(CustomerStatus.values());
		firstName.setInputPrompt("First Name");
		lastName.setInputPrompt("Last Name");
		setSizeUndefined();

		HorizontalLayout names = new HorizontalLayout();
		HorizontalLayout buttons = new HorizontalLayout();

		names.addComponents(firstName, lastName);
		names.setCaption("Name");
		buttons.addComponents(save, delete);

		names.setSpacing(true);
		buttons.setSpacing(true);
		
		save.setClickShortcut(KeyCode.ENTER);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		delete.setStyleName(ValoTheme.BUTTON_DANGER);

		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());

		addComponents(names, email, status, birthDate, buttons);
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);

		// enable delete button for only customers already in the database
		if (customer.isPersisted()) {
			delete.setEnabled(true);
			save.setEnabled(false);
		} else {
			delete.setEnabled(false);
			save.setEnabled(true);
		}

		setEnabled(true);
		firstName.selectAll();
	}

	private void delete() {
		service.delete(customer);
		myui.updateList();
		clearField();
		setEnabled(false);
	}

	private void save() {
		service.save(customer);
		myui.updateList();
		clearField();
		setEnabled(false);
	}

	public void clearField() {
		firstName.clear();
		lastName.clear();
		email.clear();
		status.clear();
		birthDate.clear();
	}
}