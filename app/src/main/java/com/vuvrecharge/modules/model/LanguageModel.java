package com.vuvrecharge.modules.model;

public class LanguageModel {
    String languageName;
    String languageCode;
    boolean isSelected;

    public LanguageModel(String languageName, String languageCode, boolean isSelected) {
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.isSelected = isSelected;
    }

    public String getLanguageName() { return languageName; }
    public String getLanguageCode() { return languageCode; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
