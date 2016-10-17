package com.kevin.lottery.draws;

/**
 * Created by kevin on 2016/10/16.
 */
public interface OnDrawListener {

    void onDraw(int index, String str);

    void alertDialog(String str);
}
