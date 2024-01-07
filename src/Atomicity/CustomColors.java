package Atomicity;

import java.awt.Color;

public enum CustomColors {
    COLOR_0(12),
    COLOR_1(34),
    COLOR_2(56),
    COLOR_3(78),
    COLOR_4(100),
    COLOR_5(122),
    COLOR_6(144),
    COLOR_7(166),
    COLOR_8(188),
    COLOR_9(210),
    COLOR_10(232),
    COLOR_11(254),
    COLOR_12(276),
    COLOR_13(298),
    COLOR_14(320),
    COLOR_15(342);

    private final int hue;
    private final double saturation = 1;
    private final double value = 1;

    CustomColors(int hue) {
        this.hue = hue;
    }


    public static CustomColors findClosestColor(Color inputColor) {
        int hue = (int) (inputColor.getRed() * 0.299 + inputColor.getGreen() * 0.587 + inputColor.getBlue() * 0.114);

        CustomColors closestColor = null;
        int minDiff = Integer.MAX_VALUE;

        for (CustomColors color : CustomColors.values()) {
            int diff = Math.abs(hue - color.hue);
            if (diff < minDiff) {
                minDiff = diff;
                closestColor = color;
            }
        }

        return closestColor;
    }


    public Color getColor() {
        double c = value * saturation;
        double x = c * (1 - Math.abs((hue / 60.0) % 2 - 1));
        double m = value - c;

        double r, g, b;
        if (hue >= 0 && hue < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (hue >= 60 && hue < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (hue >= 120 && hue < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (hue >= 180 && hue < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (hue >= 240 && hue < 300) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        int red = (int) ((r + m) * 255);
        int green = (int) ((g + m) * 255);
        int blue = (int) ((b + m) * 255);

        return new Color(red, green, blue);
    }
}
