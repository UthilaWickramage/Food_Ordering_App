package lk.software.app.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.animate().translationX(0).setDuration(5000);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);


        TextView textView = findViewById(R.id.textView);


        Animation animation2;

        animation2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.intro_anim2);

        animation2.setDuration(3500);
        animation2.setFillAfter(true);
        textView.startAnimation(animation2);


        findViewById(R.id.getStartedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}