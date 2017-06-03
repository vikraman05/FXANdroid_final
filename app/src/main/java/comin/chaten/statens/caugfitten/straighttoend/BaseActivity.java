package comin.chaten.statens.caugfitten.straighttoend;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public abstract class BaseActivity extends AppCompatActivity {

    public void showSnackbar(final String message, final int duration) {
        if (duration != Snackbar.LENGTH_LONG && duration != Snackbar.LENGTH_INDEFINITE &&
                duration != Snackbar.LENGTH_SHORT) return;

        if (message == null || message.trim().isEmpty()) return;

        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rootView, message, duration)
                        .show();
            }
        });
    }

}
