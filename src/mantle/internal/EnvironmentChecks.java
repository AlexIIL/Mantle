package mantle.internal;

import java.util.ArrayList;
import java.util.List;

import static mantle.lib.CoreRepo.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import mantle.internal.crash.CallableUnsuppConfig;
import mantle.internal.crash.CallableSuppConfig;

/**
 * Environment Checks
 *
 * Checks the runtime environment is safe for use. If not, registers warnings and adds a suitable crash callable.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class EnvironmentChecks
{

    public static boolean hasOptifine = false;

    private EnvironmentChecks()
    {
    } // Singleton

    /**
     * Checks for conflicting stuff in environment; adds callable to any crash logs if so.
     * Note: This code adds additional data to crashlogs. It does not trigger any crashes.
     */
    public static void verifyEnvironmentSanity ()
    {
        List<String> modIds = new ArrayList<String>();

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLClientHandler.instance().hasOptifine() || Loader.isModLoaded("optifine"))
        {
            logger.severe("[Environment Checks] Optifine detected. This may cause issues due to base edits or ASM usage.");
            modIds.add("optifine");
            hasOptifine = true;
        }

        try
        {
            Class cl = Class.forName("org.bukkit.Bukkit");
            if (cl != null)
            {
                logger.severe("[Environment Checks] Bukkit implementation detected. This may cause issues. Bukkit implementations include Craftbukkit and MCPC+.");
                modIds.add("bukkit");
            }
        }
        catch (Exception ex)
        {
            // No Bukkit in environment.
        }

        try
        {
            Class cl = Class.forName("magic.launcher.Launcher");
            if (cl != null)
            {
                logger.severe("[Environment Checks] Magic Launcher detected. This launches the game in strange ways and as such is not recommended.");
                modIds.add("magic_launcher");
            }
        }
        catch (Exception ex)
        {
            // No Magic Launcher in environment.
        }

        if (modIds.size() == 0)
        {
            ICrashCallable callable = new CallableSuppConfig(modId);
            FMLCommonHandler.instance().registerCrashCallable(callable);
        }
        else
        {
            ICrashCallable callable = new CallableUnsuppConfig(modId, modIds);
            FMLCommonHandler.instance().registerCrashCallable(callable);
        }
    }

}
