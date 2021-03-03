package com.example.application;

import java.time.LocalDate;
import java.util.List;

/*
 * This class performs field-specific validation based on details in ResearchDetails class
 */
public class Validator {
    private final ResearchDetails researchDetails = new ResearchDetails();

    public Validator() {
    }

    /**
     * Checks that a field has not been left blank.
     *
     * @return true if string is not blank (null, "", or whitespace)
     */
    public boolean checkPresence(String string) {
        return !string.isBlank();
    }

    /**
     * Checks that a field does not contain any digits.
     *
     * @return true if there are no digits in string, special characters are ignored
     */
    public boolean checkNoNumbers(String string) {
        // Split string into individual characters, loop through them, check for digits, return false if one is found
        for (char character : string.toCharArray()) {
            if (Character.isDigit(character)) return false;
        }
        return true; // If this statement is reached, it means no digits were found in loop, so return true
    }

    /**
     * Checks whether the bird name is in the list of birds being researched
     *
     * @param birdName the name of the bird to be checked (case sensitive)
     * @return true if birdName is a valid name
     */
    public boolean checkIsBirdBeingResearched(String birdName) {
        return researchDetails.getBirdNames().contains(birdName);
    }

    /**
     * Check that quantity is greater than 0 and less than the max bird quantity expected
     */
    public boolean checkValidQuantity(int quantity) {
        return quantity > 0 && quantity <= researchDetails.MAX_QUANTITY;
    }

    /**
     * Checks that a date entered is not before research period began.
     *
     * @return true if date is after research period began (ResearchDetails.MIN_DATE)
     */
    public boolean checkDateAfterMin(LocalDate date) {
        return date.isAfter(researchDetails.MIN_DATE) || date.equals(researchDetails.MIN_DATE);
    }

    /**
     * Checks that a date entered is not in the future as of day value was entered
     *
     * @return true if date is equal to or before current date
     */
    public boolean checkDateNotInFuture(LocalDate date) {
        return date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    /**
     * Takes a list of bird behaviours and checks that there are no combinations that are physically impossible,
     * i.e. that cannot be performed at the same time.
     *
     * @param behaviours a list containing all Behaviours the user selected (not yet verified)
     * @return true if there ARE any invalid behaviour combinations
     */
    public boolean checkForInvalidBehaviourCombos(List<String> behaviours) {
        // First convert List<String> to List<Behaviours> by calling ResearchDetails' method
        List<ResearchDetails.Behaviour> unverifiedBehaviours
                = researchDetails.behaviourValuesOf(behaviours);

        // Then go through known invalid behaviour combinations and check if the argument contains any
        for (List<ResearchDetails.Behaviour> invalidCombo : researchDetails.getAllInvalidBehaviourCombos()) {
            if (unverifiedBehaviours.containsAll(invalidCombo))
                return true; // check if the unverified set of behaviours contains BOTH items in the invalid combo
        }
        return false;
    }

    /**
     * Takes a list of bird behaviours and compiles a list of combinations that are physically impossible,
     * i.e. that cannot be performed at the same time.
     *
     * @param behaviours a list containing all Behaviours the user selected (not yet verified)
     * @return a list of pairs of invalid behaviours (there may be multiple similar combos)
     */
    public String getInvalidBehaviourCombos(List<String> behaviours) {
        // First convert List<String> to List<Behaviours> by calling ResearchDetails' method
        List<ResearchDetails.Behaviour> unverifiedBehaviours
                = researchDetails.behaviourValuesOf(behaviours);

        String s = "";

        // Then go through known invalid behaviour combinations and check if the argument contains any. Add them to the output list.
        for (List<ResearchDetails.Behaviour> invalidCombo : researchDetails.getAllInvalidBehaviourCombos()) {
            if (unverifiedBehaviours.containsAll(invalidCombo)) // check if the unverified set of behaviours contains BOTH items in the invalid combo
                s += researchDetails.stringValueOf(invalidCombo.get(0)) + " and " + researchDetails.stringValueOf(invalidCombo.get(1)) + ", ";
        }
        return s;
    }
}
