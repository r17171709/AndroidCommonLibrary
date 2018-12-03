package com.renyu.commonlibrary.dialog.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.renyu.commonlibrary.dialog.R;


public class VerificationCodeInput extends ViewGroup {

    public enum VerificationCodeInputType {
        TYPE_NUMBER,
        TYPE_TEXT,
        TYPE_PASSWORD,
        TYPE_PHONE
    }

    private int box;
    private int boxWidth;
    private int boxHeight;
    private int childHPadding;
    private int childVPadding;
    private VerificationCodeInputType inputType = null;
    private Drawable boxBgFocus;
    private Drawable boxBgNormal;
    private Listener listener;

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_box, 4);
        childHPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.vericationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.vericationCodeInput_box_bg_normal);
        switch (a.getString(R.styleable.vericationCodeInput_inputType)) {
            case "number":
                inputType = VerificationCodeInputType.TYPE_NUMBER;
                break;
            case "text":
                inputType = VerificationCodeInputType.TYPE_TEXT;
                break;
            case "password":
                inputType = VerificationCodeInputType.TYPE_PASSWORD;
                break;
            case "phone":
                inputType = VerificationCodeInputType.TYPE_PHONE;
                break;
        }

        boxWidth = (int) a.getDimension(R.styleable.vericationCodeInput_child_width, 120);
        boxHeight = (int) a.getDimension(R.styleable.vericationCodeInput_child_height, 120);
        a.recycle();
        initViews();
    }

    private void initViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focus();
                    checkAndCommit();
                }
            }
        };

        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        };

        removeAllViews();

        for (int i = 0; i < box; i++) {
            EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            if (i != box - 1) {
                layoutParams.rightMargin = childHPadding;
            }
            layoutParams.gravity = Gravity.CENTER;

            editText.setCursorVisible(false);
            editText.setOnKeyListener(onKeyListener);
            setBg(editText, false);
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            if (VerificationCodeInputType.TYPE_NUMBER == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (VerificationCodeInputType.TYPE_PASSWORD == inputType) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (VerificationCodeInputType.TYPE_TEXT == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (VerificationCodeInputType.TYPE_PHONE == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(textWatcher);
            addView(editText, i);
        }
    }

    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }
        }
        if (full) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
                setEnabled(false);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(getClass().getName(), "onMeasure");
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth + childHPadding) * box - childHPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(getClass().getName(), "onLayout");
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    public void setVarificationInputType(VerificationCodeInputType inputType) {
        for (int i = 0 ; i < getChildCount() ; i++) {
            if (getChildAt(i) instanceof EditText) {
                if (VerificationCodeInputType.TYPE_NUMBER == inputType) {
                    ((EditText) getChildAt(i)).setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (VerificationCodeInputType.TYPE_PASSWORD == inputType) {
                    ((EditText) getChildAt(i)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (VerificationCodeInputType.TYPE_TEXT == inputType) {
                    ((EditText) getChildAt(i)).setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (VerificationCodeInputType.TYPE_PHONE == inputType) {
                    ((EditText) getChildAt(i)).setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
        }
    }

    public interface Listener {
        void onComplete(String content);
    }

    public void setBox(int box) {
        this.box = box;
        initViews();
        requestLayout();
    }
}

