package io.thimblebird.struckobsidian.config;

import io.wispforest.owo.config.annotation.*;

@SuppressWarnings("unused")
@Modmenu(modId = "struckobsidian")
@Config(name = "struckobsidian", wrapperName = "StruckObsidianConfig")
public class StruckObsidianConfigModel {
    private static final int rangeMin = 1;
    private static final int rangeMax = 64;

    @SectionHeader("sectionConversionConfiguration")
        public ChoiceSecondStrikeSourceBlockBehavior secondStrikeSourceBlockBehavior = ChoiceSecondStrikeSourceBlockBehavior.NONE;
        public enum ChoiceSecondStrikeSourceBlockBehavior {
            NONE, DESTROY //, DROP (not working)
        }

        @Nest
        public SpreadBranches spreadBranches = new SpreadBranches();
        public static class SpreadBranches {
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int minSpreadBranches = 1;
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int maxSpreadBranches = 3;
        }

        @Nest
        public SpreadAttempts spreadAttempts = new SpreadAttempts();
        public static class SpreadAttempts {
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int minSpreadAttempts = 1;
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int maxSpreadAttempts = 8;
        }

        @Nest
        public SpreadAfterAttempts spreadAfterAttempts = new SpreadAfterAttempts();
        public static class SpreadAfterAttempts {
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int minSpreadAfterAttempts = 1;
            @RangeConstraint(min = rangeMin, max = rangeMax)
            public int maxSpreadAfterAttempts = 4;
        }

        @Nest
        public SpreadDirectionsAllowlist spreadDirectionsAllowlist = new SpreadDirectionsAllowlist();
        public static class SpreadDirectionsAllowlist {
            public boolean spreadDirectionDown = true;
            public boolean spreadDirectionUp = true;
            public boolean spreadDirectionNorth = true;
            public boolean spreadDirectionSouth = true;
            public boolean spreadDirectionWest = true;
            public boolean spreadDirectionEast = true;
        }

    @SectionHeader("sectionDebugConfiguration")
        public boolean debugSourceGlassBlock = false;
        public boolean debugLogAttempts = false;
}
