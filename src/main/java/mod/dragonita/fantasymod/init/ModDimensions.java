package mod.dragonita.fantasymod.init;

import mod.dragonita.fantasymod.Main;
import mod.dragonita.fantasymod.world.dimensions.FantasyModDimension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDimensions {
	public static final DeferredRegister<ModDimension> DIMENSION = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Main.MODID);
	public static final ResourceLocation GEF = new ResourceLocation(Main.MODID, "fantasydimension");
	
	public static final RegistryObject<ModDimension> RAINBOW_MOD_DIMENSION = DIMENSION.register("fantasymoddimension", () -> new FantasyModDimension());
	
	public static void registerDimensions() {
		if (DimensionType.byName(GEF) == null) {
			DimensionManager.registerDimension(GEF, RAINBOW_MOD_DIMENSION.get(), null, true);
		}
	}
}