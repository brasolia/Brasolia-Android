package br.com.brasolia.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;

import br.com.brasolia.Activities.BSLoginActivity;
import br.com.brasolia.R;

/**
 * Created by cayke on 26/10/17.
 */

public class BSAlertUtil {
    public static void toLikeUserShouldLogin() {
        final Activity activity = ActivityUtils.getTopActivity();

        AlertUtil.confirm(activity, "Entrar", activity.getString(R.string.liked_logout), "Cancelar", "Conectar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = new Intent(activity, BSLoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("cameFromApp", true);
                                activity.startActivity(i);
                                break;
                        }
                    }
                });
    }
}
