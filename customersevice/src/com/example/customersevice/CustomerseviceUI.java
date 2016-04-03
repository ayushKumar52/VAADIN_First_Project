package com.example.customersevice;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("customersevice")
public class CustomerseviceUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CustomerseviceUI.class)
	public static class Servlet extends VaadinServlet {
	}

	private CustomerService service = CustomerService.getInstance();
	private Grid myGrid = new Grid();
	private TextField filterBox = new TextField();
	private Button clearButton = new Button(FontAwesome.TIMES);
	private Button addNew = new Button("Add new customer");

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout cssLayout = new HorizontalLayout();

		cssLayout.addComponents(filterBox, clearButton);
		cssLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		cssLayout.setSizeFull();
		cssLayout.setExpandRatio(filterBox, 1);
		filterBox.setSizeFull();

		HorizontalLayout toolBar = new HorizontalLayout();
		toolBar.addComponents(cssLayout, addNew);
		toolBar.setSpacing(true);
		toolBar.setSizeFull();
		addNew.setSizeFull();

		layout.addComponents(toolBar, myGrid);
		myGrid.setSizeFull();

		CustomerForm form = new CustomerForm(this);
		HorizontalLayout formLayout = new HorizontalLayout(layout, form);

		formLayout.setSpacing(true);
		formLayout.setSizeFull();
		formLayout.setExpandRatio(layout, 1);

		formLayout.setMargin(true);
		setContent(formLayout);

		clearButton.setDescription("clear filter field");
		clearButton.addClickListener(e -> {
			filterBox.clear();
			updateList();
		});

		addNew.addClickListener(e -> {
			myGrid.select(null);
			form.setEnabled(true);
			form.setCustomer(new Customer());
		});

		filterBox.setInputPrompt("filter by...");
		filterBox.addTextChangeListener(e -> {
			myGrid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAll(e.getText())));
		});

		myGrid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				form.setEnabled(false);
				form.clearField();
				updateList();
			} else
				form.setCustomer((Customer) e.getSelected().iterator().next());
		});

		myGrid.setColumns("firstName", "lastName", "email", "status");
		form.setEnabled(false);
		updateList();
	}

	public void updateList() {
		List<Customer> customerList = service.findAll();
		myGrid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customerList));
	}

}