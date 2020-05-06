package internet.com.larkmusic.base;

import android.os.Bundle;
import android.util.Log;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import internet.com.larkmusic.BuildConfig;
import internet.com.larkmusic.network.Config;

/**
 * Created by sjning
 * created on: 2019-07-01 15:41
 * description:
 */
public class AdsBaseActivity extends EventActivity {
    String ID_RELEASE = "a039387eaf3f41f696fc10cc35b8280d";
    String ID_DEBUG = "24534e1901884e398f1253216226017e";
    private MoPubInterstitial mInterstitial;
    private final int ADS_LOAD_RETRY_MAX_COUNT = 2;
    private int mAdsLoadReyCount = 0;

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
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(getId()).build();
        MoPub.initializeSdk(getApplicationContext(), sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
            /* MoPub SDK initialized.
             Check if you should show the consent dialog here, and make your ad requests. */
                System.out.println("onInitializationFinished");
                mInterstitial.load();

            }
        });
        mInterstitial = new MoPubInterstitial(this, getId());
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                System.out.println("onInterstitialLoaded");
                mAdsLoadReyCount = 0;
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                Log.e("onInterstitialFailed", errorCode.toString());
                Log.e("mAdsLoadReyCount", mAdsLoadReyCount + "");
                if(mAdsLoadReyCount < ADS_LOAD_RETRY_MAX_COUNT){
                    mAdsLoadReyCount ++;
                    mInterstitial.load();
                }else {
                    mAdsLoadReyCount = 0;
                }
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
        mAdsLoadReyCount = 0;
    }


    protected void showInsertAd() {
        if (mInterstitial.isReady()) {
            mInterstitial.show();
            Config.PLAY_ADS_COUNT++;
        } else {
            mInterstitial.load();
            // Caching is likely already in progress if `isReady()` is false.
            // Avoid calling `load()` here and instead rely on the callbacks as suggested below.
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

    private String getId() {
        if (BuildConfig.DEBUG) {
            return ID_DEBUG;
        } else {
            return ID_RELEASE;
        }
    }

}
