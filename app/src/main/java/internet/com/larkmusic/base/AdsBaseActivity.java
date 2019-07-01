package internet.com.larkmusic.base;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import internet.com.larkmusic.BuildConfig;
import internet.com.larkmusic.R;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.player.MusicPlayer;
import internet.com.larkmusic.util.CommonUtil;
import internet.com.larkmusic.util.SpHelper;

/**
 * Created by sjning
 * created on: 2019-07-01 15:41
 * description:
 */
public class AdsBaseActivity extends EventActivity {
    private InterstitialAd mInterstitialAd;
    protected int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAds();
    }

    private void initAds() {
        MobileAds.initialize(this, CommonUtil.getMetaData(this, "ads_id"));
        Config.PLAY_ADS_COUNT = 0;
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(BuildConfig.DEBUG ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-7203590267874431/7784247488");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }


    protected void showInsertAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Config.PLAY_ADS_COUNT++;
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void showAd() {
        count++;
        if (count % Config.COUNT != 0) {
            return;
        }
        if (Config.PLAY_ADS_COUNT == Config.MAX_PLAY_COUNT) {
            return;
        }
        showInsertAd();

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
