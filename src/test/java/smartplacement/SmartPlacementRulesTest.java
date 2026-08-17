package smartplacement;

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
    public static void main(String[] args){
        Vars.content = new ContentLoader();

        Blocks.invertedSorter = new Sorter("test-inverted-sorter");
        Blocks.invertedSorter.alwaysUnlocked = true;

        Block normal = new Conveyor("test-conveyor");
        Block armoredConveyor = new ArmoredConveyor("test-armored-conveyor");
        Block plastanium = new StackConveyor("test-plastanium-conveyor");
        Block duct = new Duct("test-duct");
        Duct armoredDuct = new Duct("test-armored-duct");
        armoredDuct.armored = true;
        Block other = new Block("test-other");
        Block vanillaFallback = new Block("test-vanilla-fallback");
        Block[] itemLines = {normal, armoredConveyor, plastanium, duct, armoredDuct};

        for(Block selected : itemLines){
            for(Block existing : itemLines){
                requireSame(Blocks.invertedSorter,
                    SmartPlacementMod.crossingReplacement(selected, existing, 0, 1, true, true, true, vanillaFallback),
                    selected.name + " over " + existing.name);
            }
        }

        requireSame(vanillaFallback,
            SmartPlacementMod.crossingReplacement(normal, plastanium, 0, 0, true, true, true, vanillaFallback),
            "parallel lines");
        requireSame(vanillaFallback,
            SmartPlacementMod.crossingReplacement(normal, plastanium, 0, 1, false, true, true, vanillaFallback),
            "line endpoint");
        requireSame(vanillaFallback,
            SmartPlacementMod.crossingReplacement(normal, other, 0, 1, true, true, true, vanillaFallback),
            "non-line block");
        requireSame(vanillaFallback,
            SmartPlacementMod.crossingReplacement(normal, duct, 0, 1, true, true, false, vanillaFallback),
            "single-tech crossing delegates to vanilla");

        requireTrue(SmartPlacementMod.replacementEnabled(true), "rules planet set to sun/any");
        requireFalse(SmartPlacementMod.replacementEnabled(false), "rules planet set to a single-tech planet");

        System.out.println("Smart Placement crossing and vanilla fallback rules passed.");
    }

    private static void requireSame(Block expected, Block actual, String caseName){
        if(expected != actual){
            throw new AssertionError("Failed rule: " + caseName + "; expected " + expected + ", got " + actual);
        }
    }

    private static void requireTrue(boolean actual, String caseName){
        if(!actual) throw new AssertionError("Failed rule: " + caseName + "; expected true");
    }

    private static void requireFalse(boolean actual, String caseName){
        if(actual) throw new AssertionError("Failed rule: " + caseName + "; expected false");
    }
}
