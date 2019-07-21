package internet.com.larkmusic.player;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.action.ActionPlayEvent;

/**
 * Created by sjning
 * created on: 2019-07-21 12:35
 * description:
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    private static String TAG = "CustomPhoneStateListener";
    private Context mContext;

    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
        Log.d(CustomPhoneStateListener.TAG, "CustomPhoneStateListener onServiceStateChanged: " + serviceState);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Log.d(CustomPhoneStateListener.TAG, "CustomPhoneStateListener state: "
                + state + " incomingNumber: " + incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:      // 电话挂断
                Log.d(CustomPhoneStateListener.TAG, "CustomPhoneStateListener onCallStateChanged CALL_STATE_IDLE");

                break;
            case TelephonyManager.CALL_STATE_RINGING:   // 电话响铃
                Log.d(CustomPhoneStateListener.TAG, "CustomPhoneStateListener onCallStateChanged CALL_STATE_RINGING");
                pauseMusic();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:   // 来电接通 或者 去电，去电接通  但是没法区分
                Log.d(CustomPhoneStateListener.TAG, "CustomPhoneStateListener onCallStateChanged CALL_STATE_OFFHOOK");
                pauseMusic();
                break;
        }
    }

    private void pauseMusic() {
        if (MusicPlayer.getPlayer().isPlaying()) {
            ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
            actionPlayEvent.setAction(ActionPlayEvent.Action.STOP);
            EventBus.getDefault().post(actionPlayEvent);
        }
    }
}