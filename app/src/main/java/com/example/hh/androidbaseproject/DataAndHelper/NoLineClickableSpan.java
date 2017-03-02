package com.example.hh.androidbaseproject.DataAndHelper;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by hh on 16/8/5.
 */
public class NoLineClickableSpan extends ClickableSpan {

        public NoLineClickableSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            /**set textColor**/
            ds.setColor(ds.linkColor);
            /**Remove the underline**/
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
        }
}
