package Level.GameRule;

import org.cloudburstmc.protocol.bedrock.data.GameRuleData;

import java.util.ArrayList;
import java.util.List;

public class GameRuleIndex
{
    public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = new GameRule<>("commandblockoutput", true);
    public static final GameRule<Boolean> COMMAND_BLOCKS_ENABLED = new GameRule<>("commandblocksenabled", true);
    public static final GameRule<Boolean> DO_DAYLIGHT_CYCLE = new GameRule<>("dodaylightcycle", true);
    public static final GameRule<Boolean> DO_ENTITY_DROPS = new GameRule<>("doentitydrops", true);
    public static final GameRule<Boolean> DO_FIRE_TICK = new GameRule<>("dofiretick", true);
    public static final GameRule<Boolean> DO_IMMEDIATE_RESPAWN = new GameRule<>("doimmediaterespawn", false);
    public static final GameRule<Boolean> DO_INSOMNIA = new GameRule<>("doinsomnia", true);
    public static final GameRule<Boolean> DO_MOB_LOOT = new GameRule<>("domobloot", true);
    public static final GameRule<Boolean> DO_MOB_SPAWNING = new GameRule<>("domobspawning", true);
    public static final GameRule<Boolean> DO_TILE_DROPS = new GameRule<>("dotiledrops", true);
    public static final GameRule<Boolean> DO_WEATHER_CYCLE = new GameRule<>("doweathercycle", true);
    public static final GameRule<Boolean> DROWNING_DAMAGE = new GameRule<>("drowningdamage", true);
    public static final GameRule<Boolean> EXPERIMENTAL_GAME_PLAY = new GameRule<>("experimentalgameplay", false);
    public static final GameRule<Boolean> FALL_DAMAGE = new GameRule<>("falldamage", true);
    public static final GameRule<Boolean> FIRE_DAMAGE = new GameRule<>("firedamage", true);
    public static final GameRule<Boolean> FREEZE_DAMAGE = new GameRule<>("freezedamage", true);
    public static final GameRule<Integer> FUNCTION_COMMAND_LIMIT = new GameRule<>("functioncommandlimit", 10000);
    public static final GameRule<Boolean> KEEP_INVENTORY = new GameRule<>("keepinventory", false);
    public static final GameRule<Integer> MAX_COMMAND_CHAIN_LENGTH = new GameRule<>("maxcommandchainlength", 65535);
    public static final GameRule<Boolean> MOB_GRIEFING = new GameRule<>("mobgriefing", true);
    public static final GameRule<Boolean> NATURAL_REGENERATION = new GameRule<>("naturalregeneration", true);
    public static final GameRule<Boolean> PVP = new GameRule<>("pvp", true);
    public static final GameRule<Integer> RANDOM_TICK_SPEED = new GameRule<>("randomtickspeed", 1);
    public static final GameRule<Boolean> RESPAWN_BLOCKS_EXPLODE = new GameRule<>("respawnblocksexplode", true);
    public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = new GameRule<>("sendcommandfeedback", true);
    public static final GameRule<Boolean> SHOW_BORDER_EFFECT = new GameRule<>("showbordereffect", true);
    public static final GameRule<Boolean> SHOW_COORDINATES = new GameRule<>("showcoordinates", false);
    public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = new GameRule<>("showdeathmessages", true);
    public static final GameRule<Boolean> SHOW_TAGS = new GameRule<>("showtags", true);
    public static final GameRule<Integer> SPAWN_RADIUS = new GameRule<>("spawnradius", 5);
    public static final GameRule<Boolean> TNT_EXPLODES = new GameRule<>("tntexplodes", true);

    public static List<GameRule<?>> getGameRulesList()
    {
        List<GameRule<?>> gameRules = new ArrayList<>();

        // Add all GameRule constants to the list
        gameRules.add(COMMAND_BLOCK_OUTPUT);
        gameRules.add(COMMAND_BLOCKS_ENABLED);
        gameRules.add(DO_DAYLIGHT_CYCLE);
        gameRules.add(DO_ENTITY_DROPS);
        gameRules.add(DO_FIRE_TICK);
        gameRules.add(DO_IMMEDIATE_RESPAWN);
        gameRules.add(DO_INSOMNIA);
        gameRules.add(DO_MOB_LOOT);
        gameRules.add(DO_MOB_SPAWNING);
        gameRules.add(DO_TILE_DROPS);
        gameRules.add(DO_WEATHER_CYCLE);
        gameRules.add(DROWNING_DAMAGE);
        gameRules.add(EXPERIMENTAL_GAME_PLAY);
        gameRules.add(FALL_DAMAGE);
        gameRules.add(FIRE_DAMAGE);
        gameRules.add(FREEZE_DAMAGE);
        gameRules.add(FUNCTION_COMMAND_LIMIT);
        gameRules.add(KEEP_INVENTORY);
        gameRules.add(MAX_COMMAND_CHAIN_LENGTH);
        gameRules.add(MOB_GRIEFING);
        gameRules.add(NATURAL_REGENERATION);
        gameRules.add(PVP);
        gameRules.add(RANDOM_TICK_SPEED);
        gameRules.add(RESPAWN_BLOCKS_EXPLODE);
        gameRules.add(SEND_COMMAND_FEEDBACK);
        gameRules.add(SHOW_BORDER_EFFECT);
        gameRules.add(SHOW_COORDINATES);
        gameRules.add(SHOW_DEATH_MESSAGES);
        gameRules.add(SHOW_TAGS);
        gameRules.add(SPAWN_RADIUS);
        gameRules.add(TNT_EXPLODES);

        return gameRules;
    }

    public static void gameRulesToNetwork(List<GameRule<?>> gameRules, List<GameRuleData<?>> networkRules)
    {
        gameRules.forEach((rule) ->
        {
            networkRules.add(new GameRuleData<>(rule.getLabel(), rule.getValue()));
        });
    }

}
