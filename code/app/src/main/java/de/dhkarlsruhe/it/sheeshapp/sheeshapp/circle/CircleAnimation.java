package de.dhkarlsruhe.it.sheeshapp.sheeshapp.circle;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by d0272129 on 16.07.18.
 */

public class CircleAnimation extends Animation{

    private MyCircle circle;

    private float oldAngle;
    private float newAngle;

    public CircleAnimation(MyCircle circle, int newAngle) {
        this.oldAngle = circle.getAngle();
        this.newAngle = newAngle;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
        circle.setAngle(angle);
        circle.requestLayout();
    }

    public float getOldAngle() {
        return oldAngle;
    }

    public void setOldAngle(float oldAngle) {
        this.oldAngle = oldAngle;
    }

    public float getNewAngle() {
        return newAngle;
    }

    public void setNewAngle(float newAngle) {
        this.newAngle = newAngle;
    }
}
