package me.alpha432.oyvey.features.gui.futureutils;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilder.BuiltBuffer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

@SuppressWarnings("redundant")
public final class RenderUtilic {

    public static void drawModalRect(int var0, int var1, float var2, float var3, int var4, int var5, int var6, int var7, float var8, float var9) {
        float var10 = (var6 - var4) / var8;
        float var11 = (var7 - var5) / var9;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        //bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(var0, var1 + var7, 0.0D).texture(var2, var3 + var9).color(var4, var5, var6, var7).next();
        bufferBuilder.vertex(var0 + var8, var1 + var7, 0.0D).texture(var2 + var10, var3 + var9).color(var4, var5, var6, var7).next();
        bufferBuilder.vertex(var0 + var8, var1, 0.0D).texture(var2 + var10, var3).color(var4, var5, var6, var7).next();
        bufferBuilder.vertex(var0, var1, 0.0D).texture(var2, var3).color(var4, var5, var6, var7).next();
        bufferBuilder.end();
    }
}
