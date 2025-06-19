package dev.aidanarredondo.emergency_systems.util;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class SoundPatterns {

    public enum AlarmPattern {
        TEMPORAL_3("temporal_3", new int[]{500, 500, 500, 1500}), // 3 beeps, pause
        TEMPORAL_4("temporal_4", new int[]{250, 250, 250, 250, 250, 250, 250, 1750}), // 4 beeps, pause
        CONTINUOUS("continuous", new int[]{2000}), // Continuous sound
        MARCH_TIME("march_time", new int[]{150, 150, 150, 150, 150, 1250}), // Fast beeping
        SLOW_WHOOP("slow_whoop", new int[]{3000}), // Slow rising tone
        FAST_WHOOP("fast_whoop", new int[]{1000}); // Fast rising tone

        private final String name;
        private final int[] pattern; // milliseconds for on/off cycles

        AlarmPattern(String name, int[] pattern) {
            this.name = name;
            this.pattern = pattern;
        }

        public String getName() { return name; }
        public int[] getPattern() { return pattern; }
    }

    public enum SirenPattern {
        ALERT("alert", new int[]{1000, 1000}), // On/off pattern
        ATTACK("attack", new int[]{200, 200}), // Fast on/off
        WAIL("wail", new int[]{3000}), // Long continuous
        YELP("yelp", new int[]{300, 700}), // Short on, longer off
        RAID("raid", new int[]{6000}); // Very long continuous

        private final String name;
        private final int[] pattern;

        SirenPattern(String name, int[] pattern) {
            this.name = name;
            this.pattern = pattern;
        }

        public String getName() { return name; }
        public int[] getPattern() { return pattern; }
    }

    public static AlarmPattern getAlarmPatternFromString(String patternName) {
        try {
            return AlarmPattern.valueOf(patternName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AlarmPattern.TEMPORAL_3; // Default fallback
        }
    }

    public static SirenPattern getSirenPatternFromString(String patternName) {
        try {
            return SirenPattern.valueOf(patternName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SirenPattern.ALERT; // Default fallback
        }
    }

    public static void playAlarmWithPattern(Level level, BlockPos pos, SoundEvent sound, AlarmPattern pattern, float volume) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, 1.0F);
    }

    public static void playSirenWithPattern(Level level, BlockPos pos, SoundEvent sound, SirenPattern pattern, float volume) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, 1.0F);
    }
}
