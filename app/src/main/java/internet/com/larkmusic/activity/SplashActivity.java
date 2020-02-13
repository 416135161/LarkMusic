package internet.com.larkmusic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import internet.com.larkmusic.R;
import internet.com.larkmusic.animations.PulseAnimation;
import internet.com.larkmusic.network.Config;
import internet.com.larkmusic.player.MusicPlayer;

/**
 * Created by sjning
 * created on: 2019/5/26 下午2:26
 * description:
 */
public class SplashActivity extends AppCompatActivity {


    int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Config.tf4 = Typeface.createFromAsset(getAssets(), "fonts/Intro_Cond_Light.otf");
            Config.tf3 = Typeface.createFromAsset(getAssets(), "fonts/Gidole-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {
            startHomeActivity();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGranted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
        }
        if (allGranted) {
            startHomeActivity();
        } else {
            Toast.makeText(this, "Please grant the requested permissions.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void startHomeActivity() {
        int delayTime = 1500;
        if (MusicPlayer.getPlayer().isPlaying()) {
            delayTime = 600;
        }
        PulseAnimation.create().with(findViewById(R.id.splash_img))
                .setDuration(300)
                .setRepeatCount(4)
                .setRepeatMode(PulseAnimation.REVERSE)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, delayTime);
    }

    public void requestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
    }

}