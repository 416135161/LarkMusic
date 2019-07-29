/*
 * Copyright (C) 2007 The Android Open Source Project Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package internet.com.larkmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import org.greenrobot.eventbus.EventBus;

import internet.com.larkmusic.action.ActionPlayEvent;
import internet.com.larkmusic.player.MusicPlayer;


/**
 * Used to control headset playback.
 * Single press: pause/resume
 * Double press: next track
 * Triple press: previous track
 * Long press: voice search
 */
public class MediaButtonIntentReceiver extends BroadcastReceiver {

    private static MyAction lastAction;

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
            dealNoisyAction();
        } else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            dealMediaButtonAction(intent);
        }
    }

    private void dealNoisyAction() {
        if (MusicPlayer.getPlayer().isPlaying()) {
            ActionPlayEvent actionPlayEvent = new ActionPlayEvent();
            actionPlayEvent.setAction(ActionPlayEvent.Action.STOP);
            EventBus.getDefault().post(actionPlayEvent);
        }
    }

    private void dealMediaButtonAction(Intent intent) {
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int keycode = event.getKeyCode();
        // 防止广播事件连续收到
        if (lastAction != null && lastAction.code == keycode && System.currentTimeMillis() - lastAction.time < 500) {
            lastAction.code = keycode;
            lastAction.time = System.currentTimeMillis();

            return;
        } else {
            lastAction = new MyAction();
            lastAction.code = keycode;
            lastAction.time = System.currentTimeMillis();
        }
        switch (keycode) {
            case KeyEvent.KEYCODE_MEDIA_STOP:
                MusicPlayer.getPlayer().pause();
                break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                if (MusicPlayer.getPlayer().isPlaying()) {
                    MusicPlayer.getPlayer().pause();
                } else {
                    if (MusicPlayer.getPlayer().isPause()) {
                        MusicPlayer.getPlayer().resume();
                    } else {
                        MusicPlayer.getPlayer().play();
                    }
                }

                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                MusicPlayer.getPlayer().next();
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                MusicPlayer.getPlayer().previous();
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                MusicPlayer.getPlayer().pause();
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                MusicPlayer.getPlayer().play();
                break;
        }
    }

    public class MyAction {
        public long time;
        public int code;
    }
}
