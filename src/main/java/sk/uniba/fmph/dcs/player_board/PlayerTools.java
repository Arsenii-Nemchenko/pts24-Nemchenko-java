package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.*;

public class PlayerTools implements InterfaceGetState {


    private final int[] tools = new int[3];
    private final boolean[] usedTools = new boolean[3];
    private int totalToolsCount;
    private int roundToolsCount;
    private final List<Integer> additionalTools = new ArrayList<>();

    public void newTurn() {
        Arrays.fill(usedTools, false);
        roundToolsCount = totalToolsCount;
        totalToolsCount = 0;
    }

    public void addTool() {
        if (totalToolsCount < 12) {
            int position = totalToolsCount % 3;
            int value = 1 + totalToolsCount / 3;
            tools[position] = value;
            totalToolsCount++;
            roundToolsCount++;
        }
    }

    public void addSingleUseTool(int strength) {
        additionalTools.add(strength);
        totalToolsCount += strength;
        roundToolsCount += strength;
    }

    public Optional<Integer> useTool(int index) {
        if (index > 2) {
            int additionalIndex = index % 3;
            int additionalToolValue = additionalTools.get(additionalIndex);
            totalToolsCount -= additionalToolValue;
            roundToolsCount -= additionalToolValue;
            additionalTools.remove(additionalIndex);
            return Optional.of(additionalToolValue);
        } else {
            roundToolsCount = roundToolsCount - tools[index];
            usedTools[index] = true;
            return Optional.of(tools[index]);
        }
    }

    public boolean hasSufficientTools(int goal) {
        return goal <= roundToolsCount;
    }


    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "tools", tools,
                "usedTools", usedTools,
                "totalToolsCount", totalToolsCount,
                "roundToolsCount", roundToolsCount,
                "additionalTools", additionalTools
        );

        return new JSONObject(state).toString();
    }

    public int getTotalToolsCount() {
        return totalToolsCount;
    }

    public boolean[] getUsedTools() {
        return usedTools;
    }

    public int[] getTools() {
        return tools;
    }

    public List<Integer> getAdditionalTools() {
        return additionalTools;
    }

    public int getRoundToolsCount() {
        return roundToolsCount;
    }
}
