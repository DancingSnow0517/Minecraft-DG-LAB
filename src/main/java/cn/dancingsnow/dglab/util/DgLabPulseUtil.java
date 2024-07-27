package cn.dancingsnow.dglab.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
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
        List<Integer> frequencies = new ArrayList<>();
        List<Integer> strengths = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                frequencies.add(args[i]);
            } else {
                strengths.add(args[i]);
            }
        }
        return pulse(frequencies, strengths);
    }

    /**
     * input frequency and strength list, return the pulse string
     *
     * @param frequencies frequency list
     * @param strengths   strength list
     * @return the pulse strings
     */
    public static List<String> pulse(List<Integer> frequencies, List<Integer> strengths) {
        List<String> pulses = new ArrayList<>();
        if (strengths.size() != frequencies.size()) {
            throw new IllegalArgumentException("strength and frequency count must be the same");
        }

        StringBuilder frequency = new StringBuilder();
        StringBuilder strength = new StringBuilder();
        for (int i = 0; i < strengths.size(); i++) {
            if (frequencies.get(i) < 0 || frequencies.get(i) > 1000) {
                throw new IllegalArgumentException("frequency must be between 0 and 1000");
            }
            frequency.append("%02X".formatted(convent(frequencies.get(i))));

            if (strengths.get(i) < 0 || strengths.get(i) > 100) {
                throw new IllegalArgumentException("strength must be between 0 and 100");
            }
            strength.append("%02X".formatted(strengths.get(i)));

            if ((i + 1) % 4 == 0) {
                pulses.add("" + frequency + strength);
                frequency = new StringBuilder();
                strength = new StringBuilder();
            }
        }
        if (!frequency.isEmpty() || !strength.isEmpty()) {
            pulses.add(StringUtils.rightPad(frequency.toString(), 8, '0')
                    + StringUtils.rightPad(strength.toString(), 8, '0'));
        }
        return pulses;
    }

    public static String toStringArray(List<String> array) {
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

    /**
     * generate sin pulse
     *
     * @param frequency   the pulse used frequency
     * @param minStrength the pulse min strength
     * @param maxStrength the pulse max strength
     * @param duration    the pulse duration (realtime = duration * 25ms)
     * @return the generated sin pulse
     */
    public static List<String> sinPulse(
            int frequency, int minStrength, int maxStrength, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be greater than 0");
        }
        List<Integer> strengths = new ArrayList<>();

        // 振幅
        double amplitude = maxStrength - minStrength;
        // 角度增量, 只需要 0 - π 的范围
        double angleStep = Math.PI / (duration - 1);

        for (int i = 0; i < duration; i++) {
            // 当前角度
            double angle = i * angleStep;
            // 计算正弦值，并且平移到给定的最大最小值
            double sinValue = Math.sin(angle) * amplitude + minStrength;
            strengths.add((int) Math.round(sinValue));
        }

        return pulse(IntStream.generate(() -> frequency).limit(duration).boxed().toList(), strengths);
    }

    /**
     * generate gradient pulse
     *
     * @param frequency     the pulse used frequency
     * @param startStrength the pulse start strength
     * @param endStrength   the pulse end strength
     * @param duration      the pulse duration (realtime = duration * 25ms)
     * @return the generated gradient pulse
     */
    public static List<String> gradientPulse(
            int frequency, int startStrength, int endStrength, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be greater than 0");
        }
        List<Integer> strengths = new ArrayList<>();

        double step = (endStrength - startStrength) / (duration - 1.0);
        for (int i = 0; i < duration; i++) {
            strengths.add((int) Math.round(startStrength + step * i));
        }

        return pulse(IntStream.generate(() -> frequency).limit(duration).boxed().toList(), strengths);
    }

    /**
     * generate smooth pulse
     *
     * @param frequency the pulse used frequency
     * @param strength  the pulse used strength
     * @param duration  the pulse duration (realtime = duration * 25ms)
     * @return generated smooth pulse
     */
    public static List<String> smoothPulse(int frequency, int strength, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be greater than 0");
        }

        return pulse(
                IntStream.generate(() -> frequency).limit(duration).boxed().toList(),
                IntStream.generate(() -> strength).limit(duration).boxed().toList());
    }
}
