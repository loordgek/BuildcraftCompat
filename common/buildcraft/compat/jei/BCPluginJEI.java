package buildcraft.compat.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.Loader;

import buildcraft.api.core.BCLog;
import buildcraft.energy.fuels.FuelManager;
import buildcraft.energy.gui.GuiCombustionEngine;
import buildcraft.energy.gui.GuiStoneEngine;
import buildcraft.silicon.gui.GuiAdvancedCraftingTable;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@JEIPlugin
public class BCPluginJEI implements IModPlugin {
    public static boolean disableFacadeJEI;
    public static IModRegistry registry;
    public static IJeiRuntime runtime;

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {}

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {}

    @Override
    public void register(IModRegistry jeiRegistry) {
        registry = jeiRegistry;
        boolean transport = Loader.isModLoaded("BuildCraft|Transport");
        boolean factory = Loader.isModLoaded("BuildCraft|Factory");
        boolean energy = Loader.isModLoaded("BuildCraft|Energy");
        boolean silicon = Loader.isModLoaded("BuildCraft|Silicon");
        boolean robotics = Loader.isModLoaded("BuildCraft|Robotics");
        List<String> lst = new ArrayList<>();

        jeiRegistry.addAdvancedGuiHandlers(new LedgerGuiHandler());
        if (transport) {
            lst.add("transport");
            loadTransport(jeiRegistry);
        }
        if (factory) {
            lst.add("factory");
            // if (energy) {

            // }
        }
        if (energy) {
            lst.add("energy");
            loadEnergy(jeiRegistry);
        }
        if (silicon) {
            lst.add("silicon");
            loadSilicon(jeiRegistry);
        }
        BCLog.logger.info("Loaded JEI mods: " + Arrays.toString(lst.toArray()));
    }

    private static void loadTransport(IModRegistry jeiRegistry) {
        jeiRegistry.addAdvancedGuiHandlers(new GateGuiHandler());
    }

    private static void loadEnergy(IModRegistry jeiRegistry) {
        jeiRegistry.addRecipeCategories(new CategoryCombustionEngine(jeiRegistry.getJeiHelpers().getGuiHelper()));
        jeiRegistry.addRecipeHandlers(new HandlerCombusionEngine());
        jeiRegistry.addRecipes(ImmutableList.copyOf(FuelManager.INSTANCE.getFuels()));

        jeiRegistry.addRecipeClickArea(GuiCombustionEngine.class, 76, 41, 22, 15, CategoryCombustionEngine.UID);
        jeiRegistry.addRecipeClickArea(GuiStoneEngine.class, 80, 24, 16, 16, VanillaRecipeCategoryUid.FUEL);
    }

    private static void loadSilicon(IModRegistry jeiRegistry) {
        jeiRegistry.addRecipeClickArea(GuiAdvancedCraftingTable.class, 93, 34, 22, 15, VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {}

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }
}
