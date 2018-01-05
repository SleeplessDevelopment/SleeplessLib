package net.sleeplessdev.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.sleeplessdev.lib.util.BlockMaterial;
import net.sleeplessdev.lib.util.ColorVariant;

public class SimpleBlock extends Block {

    public SimpleBlock(BlockMaterial material, ColorVariant color) {
        super(material.getMaterial(), color.getMapColor());
        setHardness(material.getHardness());
        setResistance(material.getResistance());
        setSoundType(material.getSound());
    }

    public SimpleBlock(BlockMaterial material) {
        super(material.getMaterial(), material.getMaterial().getMaterialMapColor());
        setHardness(material.getHardness());
        setResistance(material.getResistance());
        setSoundType(material.getSound());

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override // TODO Remove in 1.13
    public String getUnlocalizedName() {
        return super.getUnlocalizedName().replace("tile.", "block.");
    }

    @Override
    public BlockStateContainer getBlockState() {
        return super.getBlockState();
    }
}