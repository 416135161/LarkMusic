package internet.com.larkmusic.base;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import internet.com.larkmusic.BuildConfig;
import internet.com.larkmusic.network.Config;

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
        MobileAds.initialize(this, BuildConfig.DEBUG ? "ca-app-pub-3940256099942544~3347511713"
                : "ca-app-pub-7203590267874431~2284508794");
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



}
