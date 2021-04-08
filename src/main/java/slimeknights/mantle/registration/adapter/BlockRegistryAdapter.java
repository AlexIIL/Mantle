package slimeknights.mantle.registration.adapter;

import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.registration.object.BuildingBlockObject;
import slimeknights.mantle.registration.object.FenceBuildingBlockObject;
import slimeknights.mantle.registration.object.WallBuildingBlockObject;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides utility registration methods when registering blocks.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BlockRegistryAdapter extends EnumRegistryAdapter<Block> {

  /** @inheritDoc */
  public BlockRegistryAdapter(IForgeRegistry<Block> registry) {
    super(registry);
  }

  /** @inheritDoc */
  public BlockRegistryAdapter(IForgeRegistry<Block> registry, String modid) {
    super(registry, modid);
  }

  /**
   * Registers a block override based on the given block
   * @param constructor  Override constructor
   * @param base         Base block
   * @param <T>          Block type
   * @return  Registered block
   */
  public <T extends Block> T registerOverride(Function<Settings, T> constructor, Block base) {
    return register(constructor.apply(Block.Properties.copy(base)), base);
  }

  /* Building */

  /**
   * Registers the given block as well as a slab and a stair variant for it.
   * Uses the vanilla slab and stair blocks. Uses the passed blocks properties for both.
   * Slabs and stairs are registered with a "_slab" and "_stairs" prefix
   *
   * @param block  The main block to register and whose properties to use
   * @param name   The registry name to use for the block and as base for the slab and stairs
   * @return  BuildingBlockObject for the given block
   */
  public BuildingBlockObject registerBuilding(Block block, String name) {
    return new BuildingBlockObject(
      this.register(block, name),
      this.register(new SlabBlock(Block.Properties.copy(block)), name + "_slab"),
      this.register(new StairsBlock(block::getDefaultState, Block.Properties.copy(block)), name + "_stairs")
    );
  }

  /**
   * Same as {@link #registerBuilding(Block, String)}, but also includes a wall variant
   *
   * @param block  The main block to register and whose properties to use
   * @param name   The registry name to use for the block and as base for the slab and stairs
   * @return  BuildingBlockObject for the given block
   */
  public WallBuildingBlockObject registerWallBuilding(Block block, String name) {
    return new WallBuildingBlockObject(
      registerBuilding(block, name),
      this.register(new WallBlock(Block.Properties.copy(block)), name + "_wall")
    );
  }

  /**
   * Same as {@link #registerBuilding(Block, String)}, but also includes a fence variant
   *
   * @param block  The main block to register and whose properties to use
   * @param name   The registry name to use for the block and as base for the slab and stairs
   * @return  BuildingBlockObject for the given block
   */
  public FenceBuildingBlockObject registerFenceBuilding(Block block, String name) {
    return new FenceBuildingBlockObject(
      registerBuilding(block, name),
      this.register(new FenceBlock(Block.Properties.copy(block)), name + "_fence")
    );
  }

  /* Fluid */

  /**
   * Registers a fluid block from a fluid
   * @param fluid       Fluid supplier
   * @param material    Fluid material
   * @param lightLevel  Fluid light level
   * @param name        Fluid name, unfortunately no way to fetch from the fluid as it does not exist yet
   * @return  Fluid block instance
   */
  public FluidBlock registerFluidBlock(Supplier<? extends ForgeFlowingFluid> fluid, Material material, int lightLevel, String name) {
    return register(
        new FluidBlock(fluid, Block.Properties.of(material)
                                                     .noCollision()
                                                     .strength(100.0F)
                                                     .dropsNothing()
                                                     .luminance((state) -> lightLevel)),
        name + "_fluid");
  }
}
