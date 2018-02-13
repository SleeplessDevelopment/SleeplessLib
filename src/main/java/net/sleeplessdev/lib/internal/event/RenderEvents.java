package net.sleeplessdev.lib.internal.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.sleeplessdev.lib.SleeplessLib;
import net.sleeplessdev.lib.client.render.ExtendedSelectionBox;
import net.sleeplessdev.lib.client.render.OrientableSelectionBox;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = SleeplessLib.ID, value = Side.CLIENT)
final class RenderEvents {

    private RenderEvents() {}

    @SubscribeEvent
    protected static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.getTarget() == null) return;
        if (event.getTarget().typeOfHit != RayTraceResult.Type.BLOCK) return;

        EntityPlayer player = event.getPlayer();
        World world = player.world;
        BlockPos pos = event.getTarget().getBlockPos();
        IBlockState state = world.getBlockState(pos);

        if (!(state.getBlock() instanceof ExtendedSelectionBox)) return;

        state = state.getActualState(world, pos);

        ExtendedSelectionBox iface = (ExtendedSelectionBox) state.getBlock();
        List<AxisAlignedBB> boxes = new ArrayList<>();

        iface.getSelectionBoundingBoxes(state, world, pos, boxes);

        double offsetX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double offsetY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double offsetZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
                SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        double x = pos.getX() - offsetX;
        double y = pos.getY() - offsetY;
        double z = pos.getZ() - offsetZ;

        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

        if (iface instanceof OrientableSelectionBox) {
            OrientableSelectionBox orientable = (OrientableSelectionBox) iface;
            Vector3f angle = orientable.getRotationAngles(state, world, pos);
            GlStateManager.rotate(angle.x, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(angle.y, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(angle.z, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.translate(-0.5D, -0.5D, -0.5D);

        for (AxisAlignedBB box : boxes) {
            RenderGlobal.drawSelectionBoundingBox(box.grow(0.002D), 0.0F, 0.0F, 0.0F, 0.4F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.glLineWidth(0.2F);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        event.setCanceled(true);
    }

}
