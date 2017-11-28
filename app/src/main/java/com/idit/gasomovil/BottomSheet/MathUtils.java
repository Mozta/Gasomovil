package com.idit.gasomovil.BottomSheet;

/**
 * Created by viper on 28/11/2017.
 */

class MathUtils {

    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}
