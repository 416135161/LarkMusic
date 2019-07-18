package internet.com.larkmusic.base;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import internet.com.larkmusic.R;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.player.PlayerService;
import internet.com.larkmusic.util.SpHelper;

/**
 * Created by sjning
 * created on: 2019-07-18 16:58
 * description:
 */
public class MainBaseActivity extends AdsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showStarDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.text_please_rate))
                .setView(getLayoutInflater().inflate(R.layout.dlg_star, null))
                .setPositiveButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpHelper.getDefault().putBoolean(SpHelper.KEY_STAR, true);
                        jumpToPlay();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doFinish();
                    }
                })
                .show();
    }

    protected void doFinish() {
        if (MusicPlayer.getPlayer() != null && MusicPlayer.getPlayer().isPlaying()) {
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
        } else {
            finish();
            Intent intent = new Intent();
            intent.setClass(this, PlayerService.class);
            stopService(intent);
        }
    }

    private void jumpToPlay() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Log.e("to play star: " + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage());
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }
}

