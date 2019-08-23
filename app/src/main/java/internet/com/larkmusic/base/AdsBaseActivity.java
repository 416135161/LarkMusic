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
    protected boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAds();
    }

    private void initAds() {
        MobileAds.initialize(this, BuildConfig.DEBUG ? "ca-app-pub-3940256099942544~3347511713"
                : "ca-app-pub-2768667239475946~9267110652");
        Config.PLAY_ADS_COUNT = 0;
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(BuildConfig.DEBUG ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2768667239475946/5327865642");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //广告初始化完成弹出一次
                if (isFirstLoad) {
                    mInterstitialAd.show();
                    isFirstLoad = false;
                }
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

    public void showAd(int adsType) {
        int count = Config.getAdsArray().get(adsType);
        Config.getAdsArray().put(adsType, count + 1);
        if (count % Config.COUNT != 0) {
            return;
        }
        if (Config.PLAY_ADS_COUNT == Config.MAX_PLAY_COUNT) {
            return;
        }
        showInsertAd();

    }

}
