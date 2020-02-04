package br.com.aguido.financas.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import br.com.aguido.financas.R;

/**
 * Created by vilmar.filho on 1/5/16.
 */
public class FinancasLoading extends ImageView{

    public FinancasLoading(Context context) {
        super(context);
    }

    public FinancasLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupLoading();
    }

    public FinancasLoading(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupLoading();
    }

    private void setupLoading(){
        setBackgroundResource(R.drawable.loading_animated);

        post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
                frameAnimation.start();
            }
        });
    }
}
