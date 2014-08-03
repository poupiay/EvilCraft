package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link DarkGemCrushed}.
 * @author rubensworks
 *
 */
public class DarkGemCrushedConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkGemCrushedConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkGemCrushedConfig() {
        super(
        	true,
            "darkGemCrushed",
            null,
            DarkGemCrushed.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARKCRUSHED;
    }
    
}