package cn.dancingsnow.dglab.util;

import java.util.ArrayList;
import java.util.List;

public class DgLabPulseUtil {

    /**
     * Convent frequency to Dg-Lab format
     *
     * @param frequency frequency
     * @return Dg-Lab format number
     */
    public static int convent(int frequency) {
        if (frequency <= 10) return 10;
        if (frequency <= 100) return frequency;
        if (frequency <= 600) return (frequency - 100) / 5 + 100;
        if (frequency <= 1000) return (frequency - 600) / 10 + 200;
        return 10;
    }

    /**
     * input frequency and strength, return the pulse string
     *
     * @param args Frequency and strength alternation
     * @return pulse strings
     */
    public static List<String> pulse(int... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("number of arguments must be even");
        }
        List<String> pulses = new ArrayList<>();
        StringBuilder frequency = new StringBuilder();
        StringBuilder strength = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                if (args[i] < 0 || args[i] > 1000) {
                    throw new IllegalArgumentException("frequency must be between 0 and 1000");
                }
                frequency.append("%02X".formatted(convent(args[i])));
            } else {
                if (args[i] < 0 || args[i] > 100) {
                    throw new IllegalArgumentException("strength must be between 0 and 100");
                }
                strength.append("%02X".formatted(args[i]));
                if ((i + 1) % 8 == 0) {
                    pulses.add("" + frequency + strength);
                    frequency = new StringBuilder();
                    strength = new StringBuilder();
                }
            }
        }
        if (!frequency.isEmpty() || !strength.isEmpty()) {
            pulses.add(frequency
                    + "0".repeat(8 - frequency.length())
                    + strength
                    + "0".repeat(8 - strength.length()));
        }
        return pulses;
    }

    public static String toStringArray(List<String> array) {
        System.out.println(array);
        if (array.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.size(); i++) {
            sb.append("\"").append(array.get(i)).append("\"");
            if (i < array.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
