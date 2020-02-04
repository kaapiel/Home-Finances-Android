package br.com.aguido.financas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeFinancesActivity extends AppCompatActivity {

    private String TAG = "ACTIVITY_CONTROL";

    protected Typeface tf;
    protected ActionBar mActionBar;

    private static int mForegroundActivities = 0;
    private boolean inBackground = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void configureActionBar() {
        mActionBar = getSupportActionBar();

        if (mActionBar == null) {
            return;
        }

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("");
        mActionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_voltar));
    }


    public void customizeToolbarWithButtonBack() {
        if (getSupportActionBar() == null) return;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mForegroundActivities++;

        if (mForegroundActivities == 1) {
            Log.i(TAG, "in foreground");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        inBackground = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        inBackground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        inBackground = true;
        mForegroundActivities--;

        if (mForegroundActivities == 0) {
            Log.i(TAG, "in background");
        }
    }

    public static boolean isAppInForeground() {
        if (mForegroundActivities == 1) {
            return true;
        }

        return false;
    }

    public boolean isAlive() {
        return !inBackground && !isFinishing();
    }

    public int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
