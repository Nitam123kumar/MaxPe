package com.vuvrecharge.customKeyBoard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.vuvrecharge.R;

public class CustomKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{


    private KeyboardView keyboardView;
    private Keyboard keyboard;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        keyboard = new Keyboard(this, R.xml.custom_keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        EditorInfo editorInfo = getCurrentInputEditorInfo();
        if (editorInfo != null) {
            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                getCurrentInputConnection().deleteSurroundingText(1, 0);
            } else if (primaryCode == Keyboard.KEYCODE_DONE) {
                getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            } else {
                char code = (char) primaryCode;
                if (editorInfo.inputType == InputType.TYPE_CLASS_NUMBER) {
                    getCurrentInputConnection().commitText(String.valueOf(code), 1);
                }
            }
        }
    }
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }
    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
