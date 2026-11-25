package smartplacement;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.ContentLoader;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.distribution.Sorter;
import mindustry.world.blocks.distribution.StackConveyor;

public final class SmartPlacementRulesTest{
    private static final SmartPlacementMod.CrossingOptions allEnabled =
        new SmartPlacementMod.CrossingOptions(true, true, true, true);

    public static void main(String[] args){
        Vars.content = new ContentLoader();

        Blocks.invertedSorter = new Sorter("test-inverted-sorter");
        Blocks.invertedSorter.alwaysUnlocked = true;

        Block conveyor = new Conveyor("test-conveyor");
        Block armoredConveyor = new ArmoredConveyor("test-armored-conveyor");
        Block plastanium = new StackConveyor("test-plastanium-conveyor");
        Block duct = new Duct("test-duct");
        Duct armoredDuct = new Duct("test-armored-duct");
        armoredDuct.armored = true;
        Block other = new Block("test-other");
        Block vanillaFallback = new Block("test-vanilla-fallback");

        Block[] lineBlocks = {conveyor, armoredConveyor, plastanium, duct, armoredDuct};

        // Every item-line pair, both families included, resolves to the empty inverted sorter.
        for(Block selected : lineBlocks){
            for(Block existing : lineBlocks){
                requireSame(Blocks.invertedSorter,
                    crossing(selected, existing, allEnabled, vanillaFallback),
                    selected.name + " over " + existing.name);
            }
        }

        // The 2x2 matrix is switchable per direction and does not leak into other cells.
        requireSame(vanillaFallback, crossing(conveyor, conveyor, new SmartPlacementMod.CrossingOptions(false, true, true, true), vanillaFallback),
            "conveyor x conveyor disabled");
        requireSame(vanillaFallback, crossing(conveyor, duct, new SmartPlacementMod.CrossingOptions(true, false, true, true), vanillaFallback),
            "conveyor x duct disabled");
        requireSame(vanillaFallback, crossing(duct, conveyor, new SmartPlacementMod.CrossingOptions(true, true, false, true), vanillaFallback),
            "duct x conveyor disabled");
        requireSame(vanillaFallback, crossing(duct, duct, new SmartPlacementMod.CrossingOptions(true, true, true, false), vanillaFallback),
            "duct x duct disabled");

        requireSame(Blocks.invertedSorter,
            crossing(duct, conveyor, new SmartPlacementMod.CrossingOptions(false, false, true, false), vanillaFallback),
            "disabling other cells keeps duct over conveyor on");
        requireSame(Blocks.invertedSorter,
            crossing(conveyor, duct, new SmartPlacementMod.CrossingOptions(false, true, false, false), vanillaFallback),
            "disabling other cells keeps conveyor over duct on");

        // Ambiguous or intentionally untouched scenes keep vanilla behavior.
        requireSame(vanillaFallback, crossing(conveyor, plastanium, allEnabled, vanillaFallback, 0, 0, true, true, true),
            "parallel lines");
        requireSame(vanillaFallback, crossing(conveyor, plastanium, allEnabled, vanillaFallback, 0, 1, false, true, true),
            "line endpoint");
        requireSame(vanillaFallback, crossing(conveyor, other, allEnabled, vanillaFallback),
            "non-line block");
        requireSame(vanillaFallback, crossing(duct, other, allEnabled, vanillaFallback),
            "duct over non-line block");
        requireSame(vanillaFallback, crossing(conveyor, duct, allEnabled, vanillaFallback, 0, 1, true, true, false),
            "single-tech crossing delegates to vanilla");

        // Planet gating stays adjustable.
        requireTrue(SmartPlacementMod.replacementEnabled(true, true), "rules planet set to sun/any");
        requireFalse(SmartPlacementMod.replacementEnabled(false, true), "rules planet set to a single-tech planet");
        requireTrue(SmartPlacementMod.replacementEnabled(false, false), "mixed-planet-only disabled");
        requireTrue(SmartPlacementMod.replacementEnabled(true, false), "mixed-planet-only disabled on any");

        // Line classification.
        requireKind(SmartPlacementMod.LineKind.conveyor, conveyor, "conveyor");
        requireKind(SmartPlacementMod.LineKind.conveyor, armoredConveyor, "armored conveyor");
        requireKind(SmartPlacementMod.LineKind.conveyor, plastanium, "plastanium conveyor");
        requireKind(SmartPlacementMod.LineKind.duct, duct, "duct");
        requireKind(SmartPlacementMod.LineKind.duct, armoredDuct, "armored duct");
        requireKind(null, other, "plain block");

        // Wide blocked bands: only runs longer than the threshold are bridged.
        requireRuns(SmartPlacementMod.wideBlockedRuns(new boolean[]{false, true, true, true, false}, 2), "3-wide band", 1, 3);
        requireRuns(SmartPlacementMod.wideBlockedRuns(new boolean[]{true, true, false, true, true, true, true}, 2), "two runs", 3, 6);
        requireRuns(SmartPlacementMod.wideBlockedRuns(new boolean[]{true, true, true}, 2), "whole band", 0, 2);
        requireTrue(SmartPlacementMod.wideBlockedRuns(new boolean[]{false, true, true, false}, 2).isEmpty(),
            "2-wide band stays on sorters");
        requireTrue(SmartPlacementMod.wideBlockedRuns(new boolean[0], 2).isEmpty(), "empty line");

        System.out.println("Smart Placement crossing, settings and wide-band rules passed.");
    }

    private static Block crossing(Block selected, Block existing, SmartPlacementMod.CrossingOptions options, Block fallback){
        return crossing(selected, existing, options, fallback, 0, 1, true, true, true);
    }

    private static Block crossing(Block selected, Block existing, SmartPlacementMod.CrossingOptions options, Block fallback,
                                  int existingRotation, int planRotation, boolean hasFront, boolean hasBack, boolean dualTech){
        return SmartPlacementMod.crossingReplacement(selected, existing, existingRotation, planRotation, hasFront, hasBack, dualTech, options, fallback);
    }

    private static void requireSame(Block expected, Block actual, String caseName){
        if(expected != actual){
            throw new AssertionError("Failed rule: " + caseName + "; expected " + expected + ", got " + actual);
        }
    }

    private static void requireKind(SmartPlacementMod.LineKind expected, Block block, String caseName){
        SmartPlacementMod.LineKind actual = SmartPlacementMod.kindOf(block);
        if(expected != actual){
            throw new AssertionError("Failed rule: " + caseName + "; expected " + expected + ", got " + actual);
        }
    }

    private static void requireRuns(Seq<int[]> runs, String caseName, int... expected){
        int count = expected.length / 2;
        if(runs.size != count){
            throw new AssertionError("Failed rule: " + caseName + "; expected " + count + " run(s), got " + runs.size);
        }
        for(int i = 0; i < count; i++){
            int[] run = runs.get(i);
            int wantStart = expected[i * 2], wantEnd = expected[i * 2 + 1];
            if(run.length != 2 || run[0] != wantStart || run[1] != wantEnd){
                throw new AssertionError("Failed rule: " + caseName + "; expected run " + wantStart + ".." + wantEnd
                    + ", got " + run[0] + ".." + run[1]);
            }
        }
    }

    private static void requireTrue(boolean actual, String caseName){
        if(!actual) throw new AssertionError("Failed rule: " + caseName + "; expected true");
    }

    private static void requireFalse(boolean actual, String caseName){
        if(actual) throw new AssertionError("Failed rule: " + caseName + "; expected false");
    }
}
