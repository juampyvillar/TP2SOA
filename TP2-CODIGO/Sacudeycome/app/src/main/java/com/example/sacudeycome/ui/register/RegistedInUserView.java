package com.example.sacudeycome.ui.register;

/**
 * Class exposing authenticated user details to the UI.
 */
class RegistedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    RegistedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}