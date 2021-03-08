package com.example.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CssImport("./views/new-entry-view.css")
@Route("")
@PageTitle("New Bird Entry")
public class MainView extends Div {

    /*
        NOTE TO MARKER:
            The following is a lot of GUI-creation code. Don't worry much about it. Scroll down to the validateSomething methods
            which use the Validator class to check data and print an error messages.
     */

    /**
     *
     */
    private static final long serialVersionUID = 8681922828539246285L;
    // Initialise GUI components
    private TextField txtBirdName = new TextField("Name of bird");
    private Span spnBirdNames = new Span();
    private NumberField numQuantity = new NumberField("Quantity");
    private Button btnIncrement = new Button("+");
    private DatePicker dtpSightingDate = new DatePicker("Date observed");
    private Button btnToday = new Button("Seen today");
    private CheckboxGroup<String> cbgBehaviours = new CheckboxGroup<>();
    private Span spnValid = new Span("Awaiting validation");
    private Button btnClear = new Button("Clear");
    private Button btnValidate = new Button("Validate");

    // Create instances of Validator and ResearchDetails classes for data validation
    private Validator validator = new Validator();
    private ResearchDetails researchDetails = new ResearchDetails();

    // Initialise a boolean variable that will be updated if any fields are invalid.
    private boolean allValid;

    public MainView() {
        add(createFormLayout());
    }

    private Component createFormLayout() {
        addClassName("new-entry-view");

        // Display the birds being researched for user's reference
        spnBirdNames.setText("These are the birds being researched: " + researchDetails.getBirdNamesAsString());
        spnBirdNames.setWidthFull();

        // Add all the form items to the layout
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        formLayout.setAlignSelf(FlexComponent.Alignment.CENTER);
        formLayout.add(
                createHeader(),
                spnBirdNames,
                createNameAndQuantityLayout(),
                createDateLayout(),
                createBehaviourLayout(),
                createButtonLayout());

        return formLayout;
    }

    private Component createHeader() {
        H3 title = new H3("New Bird Entry");
        Span credits = new Span("By Joshua Gaunt");
        HorizontalLayout header = new HorizontalLayout(title, credits);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.expand(title);
        return header;
    }

    private Component createNameAndQuantityLayout() {
        btnIncrement.addClickListener(event -> {
            if (numQuantity.isEmpty()) numQuantity.setValue(1.0);
            else numQuantity.setValue(numQuantity.getValue() + 1);
        });

        HorizontalLayout nameAndQuantityLayout = new HorizontalLayout(txtBirdName, numQuantity, btnIncrement);
        nameAndQuantityLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        nameAndQuantityLayout.expand(txtBirdName);

        return nameAndQuantityLayout;
    }

    private Component createDateLayout() {
        // If "Seen today" button is clicked, the date picker is set to today's date. This eliminates user errors.
        btnToday.addClickListener(e -> {
            dtpSightingDate.setValue(LocalDate.now());
        });

        HorizontalLayout dateLayout = new HorizontalLayout(dtpSightingDate, btnToday);
        dateLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        dateLayout.expand(dtpSightingDate);

        return dateLayout;
    }

    private Component createBehaviourLayout() {
        cbgBehaviours.setLabel("Bird behaviours");
        cbgBehaviours.setItems(researchDetails.stringValuesOf(researchDetails.getBehaviours()));
        cbgBehaviours.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        return cbgBehaviours;
    }

    private Component createButtonLayout() {
        btnValidate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // When validate button clicked, run validation checks on all components
        btnValidate.addClickListener(buttonClickEvent -> {
            allValid = true;
            validateBirdName();
            validateQuantity();
            validateDate();
            validateBehaviour();
            displayValidity();
        });

        // When clear button clicked, clear all components and remove errors
        btnClear.addClickListener(e -> {
                    txtBirdName.setInvalid(false);
                    txtBirdName.clear();
                    numQuantity.setInvalid(false);
                    numQuantity.clear();
                    dtpSightingDate.setInvalid(false);
                    dtpSightingDate.clear();
                    cbgBehaviours.setInvalid(false);
                    cbgBehaviours.clear();
                    spnValid.setText("Awaiting validation.");
                }
        );

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        buttonLayout.add(spnValid, btnClear, btnValidate);
        buttonLayout.expand(spnValid);
        return buttonLayout;
    }

    private void validateBirdName() {
        // Check that field is not blank (presence check)
        if (!validator.checkPresence(txtBirdName.getValue())) {
            txtBirdName.setErrorMessage("This field cannot be left blank.");
            txtBirdName.setInvalid(true);
            removeValidity();
        }
        // Check that field does not contain numbers (type check)
        else if (!validator.checkNoNumbers(txtBirdName.getValue())) {
            txtBirdName.setErrorMessage("The bird mustn't contain numbers. Enter quantity in the next field.");
            txtBirdName.setInvalid(true);
            removeValidity();
        }
        // Check that bird is among those being researched (look-up table check)
        else if (!validator.checkIsBirdBeingResearched(txtBirdName.getValue())) {
            txtBirdName.setErrorMessage("Please enter a bird name that is among those being researched. This field is case sensitive.");
            txtBirdName.setInvalid(true);
            removeValidity();
        }
    }

    private void validateQuantity() {
        // Check that field is not blank (presence check)
        if (numQuantity.isEmpty()) {
            numQuantity.setErrorMessage("This field cannot be left blank.");
            numQuantity.setInvalid(true);
            removeValidity();
        }
        // Check that quantity is in acceptable range (range check)
        else if (!validator.checkValidQuantity(numQuantity.getValue().intValue())) {
            numQuantity.setErrorMessage("Please enter a number between 1 and " + researchDetails.MAX_QUANTITY + " (inclusively)");
            numQuantity.setInvalid(true);
            removeValidity();
        }

    }

    private void validateDate() {
        // Check that field is not blank (presence check)
        if (dtpSightingDate.isEmpty()) {
            dtpSightingDate.setErrorMessage("This field cannot be left empty.");
            dtpSightingDate.setInvalid(true);
            removeValidity();
        }
        // Check that the selected date is after research began (range check)
        else if (!validator.checkDateAfterMin(dtpSightingDate.getValue())) {
            dtpSightingDate.setErrorMessage("Please enter from " + researchDetails.MIN_DATE + " and afterwards. This is when the research period began.");
            dtpSightingDate.setInvalid(true);
            removeValidity();
        }
        // Check that the selected date is not in the future (range check / logic check)
        else if (!validator.checkDateNotInFuture(dtpSightingDate.getValue())) {
            dtpSightingDate.setErrorMessage("Please enter a date from today or before. Sighting date cannot be in the future.");
            dtpSightingDate.setInvalid(true);
            removeValidity();
        }
    }

    private void validateBehaviour() {
        List<String> selectedBehaviours = new ArrayList<>(cbgBehaviours.getSelectedItems());

        // Check for invalid (physically impossible) simultaneous behaviour combinations. (logic check)
        if (validator.checkForInvalidBehaviourCombos(selectedBehaviours)) {
            cbgBehaviours.setErrorMessage("Birds cannot be both " + validator.getInvalidBehaviourCombos(selectedBehaviours) + "at the same time. Please remove invalid combinations or log a separate sighting if you saw both behaviours occur separately.");
            cbgBehaviours.setInvalid(true);
            removeValidity();
        }
    }

    private void displayValidity() {
        // Display a message if all fields are valid.
        if (allValid) spnValid.setText("All fields are valid!");
        else spnValid.setText("There are invalid fields. Please go back and check these.");
    }

    private void removeValidity() {
        if (allValid) allValid = false;
    }
}

