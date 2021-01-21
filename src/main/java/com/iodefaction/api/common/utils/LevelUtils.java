package com.iodefaction.api.common.utils;

import org.apache.commons.math3.util.FastMath;

public class LevelUtils {
    public static double getMaxPoints(int level) {
        return FastMath.pow(FastMath.log(level * 4),  5) * 10;
    }
}
