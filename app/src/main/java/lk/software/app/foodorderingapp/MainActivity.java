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

////        ImageView imageView = findViewById(R.id.imageView);
//        CardView cardView = findViewById(R.id.cardView);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.intro_anim);
 Animation animation2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.intro_anim2);
//        animation1.setRepeatCount(Animation.INFINITE);
//        animation1.setInterpolator(new LinearInterpolator());
//        animation1.setDuration(30000);
 animation2.setDuration(3500);
 animation2.setFillAfter(true);
//
//        imageView.startAnimation(animation1);
 textView.startAnimation(animation2);

        findViewById(R.id.getStartedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}