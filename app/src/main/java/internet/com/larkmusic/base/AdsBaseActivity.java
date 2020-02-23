package internet.com.larkmusic.base;

import android.os.Bundle;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019-07-01 15:41
 * description:
 */
public class AdsBaseActivity extends EventActivity {
    //    private InterstitialAd mInterstitialAd;
    private MoPubInterstitial mInterstitial;
    protected boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.PLAY_ADS_COUNT = 0;
        initAds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoPub.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MoPub.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MoPub.onResume(this);
    }

    private void initAds() {
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("24534e1901884e398f1253216226017e").build();
        MoPub.initializeSdk(getApplicationContext(), sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
            /* MoPub SDK initialized.
             Check if you should show the consent dialog here, and make your ad requests. */
                System.out.println("onInitializationFinished");
                mInterstitial.load();
            }
        });
        mInterstitial = new MoPubInterstitial(this, "24534e1901884e398f1253216226017e");
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                System.out.println("onInterstitialLoaded");
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                System.out.println("onInterstitialFailed");
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
                System.out.println("onInterstitialShown");
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                System.out.println("onInterstitialClicked");
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {

                mInterstitial.load();
            }
        });

    }


    protected void showInsertAd() {
        if (mInterstitial.isReady()) {
            mInterstitial.show();
            Config.PLAY_ADS_COUNT++;
        } else {
            // Caching is likely already in progress if `isReady()` is false.
            // Avoid calling `load()` here and instead rely on the callbacks as suggested below.
        }
    }

    public void showAd(int adsType) {
        //广告初始化完成弹出一次
        if (isFirstLoad) {
            showInsertAd();
            isFirstLoad = false;
            return;
        }
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
