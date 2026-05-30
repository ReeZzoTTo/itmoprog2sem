package com.andreysankov.itmoprog2sem.client.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalizationManager {
    private Locale locale;
    private ResourceBundle bundle;

    public LocalizationManager(Locale locale) {
        setLocale(locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
