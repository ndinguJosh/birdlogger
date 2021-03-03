package com.example.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * By Charles Joshua Gaunt
 * 02 March 2021
 */

/*
 * This class provides access to information about the birds being researched and their behaviours
 */

public class ResearchDetails {
    private final List<String> birdNames;

    public enum Behaviour {
        SINGING("singing"),
        FLYING("flying"),
        FORAGING("foraging/eating"),
        NEST_BUILDING("nest building"),
        ATTRACTION_DISPLAY("performing attraction display"),
        COPULATION("copulating/mating");

        public final String label;

        Behaviour(String label) {
            this.label = label;
        }

        /**
         * Get the Behaviour value for a specific string label.
         * For example, "singing" will return Behaviour.SINGING
         */
        public static Behaviour valueOfLabel(String label) {
            for (Behaviour b : values()) {
                if (b.label.equals(label)) return b;
            }
            return SINGING; // a default behaviour
        }
    }

    private final List<List<Behaviour>> invalidBehaviourCombos;

    public final LocalDate MIN_DATE;

    public final int MAX_QUANTITY;

    /**
     * Constructor. Initialises birdName list with default values. Initialises
     * invalidBehaviourCombinations list with pairs of bird behaviours that are
     * physically impossible. Initialises MIN_DATE with date that research project
     * began.
     */
    public ResearchDetails() {
        birdNames = Arrays.asList(
                "Speckled Pigeon",
                "Cape Turtle-Dove",
                "African Green-Pigeon",
                "Burchell's Coucal",
                "White-rumped Swift",
                "Southern Fiscal",
                "Lesser Striped Swallow",
                "Sombre Greenbul",
                "Dark-capped Bulbul");
        invalidBehaviourCombos = Arrays.asList(
                Arrays.asList(Behaviour.FLYING, Behaviour.COPULATION),
                Arrays.asList(Behaviour.FORAGING, Behaviour.NEST_BUILDING),
                Arrays.asList(Behaviour.FORAGING, Behaviour.ATTRACTION_DISPLAY),
                Arrays.asList(Behaviour.FORAGING, Behaviour.COPULATION),
                Arrays.asList(Behaviour.NEST_BUILDING, Behaviour.ATTRACTION_DISPLAY),
                Arrays.asList(Behaviour.NEST_BUILDING, Behaviour.COPULATION));
        MIN_DATE = LocalDate.of(2020, 1, 1);
        MAX_QUANTITY = 200;
    }

    /**
     * Retrieve list of common names of birds being researched
     */
    public List<String> getBirdNames() {
        return birdNames;
    }

    /**
     * Retrieve list of common names of birds being researched as comma-separated String
     */
    public String getBirdNamesAsString() {
        String birdsAsString = getBirdNames().get(0); // Add the first bird to the String
        for (String bird : getBirdNames())
            birdsAsString += ", " + bird; // Add each bird name to string followed by comma
        return birdsAsString;
    }

    /**
     * Retrieve list of bird behaviours as enum values
     */
    public List<Behaviour> getBehaviours() {
        return Arrays.asList(Behaviour.values());
    }

    /**
     * Convert list of string values (representing behaviours) to Behaviour values
     */
    public List<Behaviour> behaviourValuesOf(List<String> strings) {
        List<Behaviour> behaviours = new ArrayList<>();
        for (String s : strings)
            behaviours.add(Behaviour.valueOfLabel(s));
        return behaviours;
    }

    /**
     * Convert list of behaviour values to string values (representing behaviours)
     */
    public List<String> stringValuesOf(List<Behaviour> behaviours) {
        List<String> strings = new ArrayList<>();
        for (Behaviour b : behaviours)
            strings.add(stringValueOf(b));
        return strings;
    }

    /**
     * Get a human-readable String of the Behaviour enum value
     */
    public String stringValueOf(Behaviour behaviour) {
        return behaviour.label;
    }

    /**
     * Retrieve list of all invalid (physically impossible) behaviour combination pairs.
     * I.e. cannot be performed at the same time
     */
    public List<List<Behaviour>> getAllInvalidBehaviourCombos() {
        return invalidBehaviourCombos;
    }
}
