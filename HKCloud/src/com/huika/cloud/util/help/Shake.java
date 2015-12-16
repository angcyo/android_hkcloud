package com.huika.cloud.util.help;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by lee on 2014/7/31.
 */
public class Shake extends BaseEffects {

  @Override protected void setupAnimation(View view) {
    getAnimatorSet().playTogether(
        ObjectAnimator.ofFloat(view, "translationX", 0f, .10f, -25f, .26f, 25f, .42f, -25f, .58f,
            25f, .74f, -25f, .90f, 1f, 0f).setDuration(mDuration)

    );
  }
}
