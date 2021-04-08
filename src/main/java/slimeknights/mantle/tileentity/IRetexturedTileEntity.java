package slimeknights.mantle.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import slimeknights.mantle.util.RetexturedHelper;

/**
 * Standard interface that should be used by retexturable tile entities, allows control over where the texture is saved.
 *
 * Use alongside {@link slimeknights.mantle.block.RetexturedBlock} and {@link slimeknights.mantle.item.RetexturedBlockItem}. See {@link RetexturedTileEntity} for implementation.
 */
public interface IRetexturedTileEntity {
  /* Gets the Forge tile data for the tile entity */
  CompoundTag getTileData();

  /**
   * Gets the current texture block name
   * @return Texture block name
   */
  default String getTextureName() {
    return RetexturedHelper.getTextureName(getTileData());
  }
  /**
   * Gets the current texture block
   * @return Texture block
   */
  default Block getTexture() {
    return RetexturedHelper.getBlock(getTextureName());
  }

  /**
   * Updates the texture to the given name
   * @param name  Texture name
   */
  default void updateTexture(String name) {
    RetexturedHelper.setTexture(getTileData(), name);
  }

  /**
   * Gets the model data instance with the relevant texture block
   * @return  Model data for the TE
   */
  default CompoundTag getRetexturedModelData() {
    // texture not loaded
    Block block = getTexture();
    // cannot support air, saves a conditional on usage
    if (block == Blocks.AIR) {
      block = null;
    }
    return new CompoundTag();
//    TODO: e
//    return new SinglePropertyData<>(RetexturedHelper.BLOCK_PROPERTY, block);
  }
}
