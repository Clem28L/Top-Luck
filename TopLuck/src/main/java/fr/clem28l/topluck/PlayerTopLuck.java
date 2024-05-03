package fr.clem28l.topluck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerTopLuck {

    private UUID uuid;
    private String name;
    private int totalBlocksBroken;
    private Map<String, Integer> oreBlocksBroken = new HashMap<>();

    public PlayerTopLuck(UUID uuid, String name, List<String> oreList) {
        this.uuid = uuid;
        this.name = name;
        this.totalBlocksBroken = 0;
        for (String oreName : oreList) {
            oreBlocksBroken.put(oreName, 0);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getTotalBlocksBroken() {
        return totalBlocksBroken;
    }

    public Map<String, Integer> getOreBlocksBroken() {
        return oreBlocksBroken;
    }

    public void incrementTotalBlocksBroken() {
        totalBlocksBroken++;
        calculateOreRatios();   }

    public void incrementOreBlocksBroken(String oreName) {
        int count = oreBlocksBroken.getOrDefault(oreName, 0);
        oreBlocksBroken.put(oreName, count + 1);
        calculateOreRatios();
    }

    public Map<String, Double> calculateOreRatios() {
        Map<String, Double> oreRatios = new HashMap<>();
        int totalOreBlocks = oreBlocksBroken.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : oreBlocksBroken.entrySet()) {
            String oreName = entry.getKey();
            int oreCount = entry.getValue();
            double ratio = (double) oreCount / totalBlocksBroken;
            ratio = Math.round(ratio * 100.0) / 100.0;
            oreRatios.put(oreName, ratio);
        }
        double globalOreRatio = (double) totalOreBlocks / totalBlocksBroken;
        globalOreRatio = Math.round(globalOreRatio * 100.0) / 100.0;
        oreRatios.put("Global_Ore_Ratio", globalOreRatio);
        return oreRatios;
    }





}