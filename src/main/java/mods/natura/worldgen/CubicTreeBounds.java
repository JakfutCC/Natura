package mods.natura.worldgen;

import net.minecraft.world.World;

import cpw.mods.fml.common.Loader;

public final class CubicTreeBounds {

    private CubicTreeBounds() {}

    public static boolean isCubicWorld(World world) {
        return Loader.isModLoaded("cubicchunks") && CubicBounds.isCubicWorld(world);
    }

    public static boolean isPlantedFootprintAvailable(World world, int x, int y, int z, int radius, int minYOffset,
            int maxYOffset, boolean vanillaResult) {
        return isPlantedBoundsAvailable(
                world,
                x,
                y,
                z,
                -(long) radius,
                (long) radius,
                -(long) radius,
                (long) radius,
                minYOffset,
                maxYOffset,
                vanillaResult);
    }

    public static boolean isPlantedBoundsAvailable(World world, int x, int y, int z, long minXOffset, long maxXOffset,
            long minZOffset, long maxZOffset, int minYOffset, int maxYOffset, boolean vanillaResult) {
        if (!Loader.isModLoaded("cubicchunks")) return vanillaResult;
        return CubicBounds.isAvailable(
                world,
                x,
                y,
                z,
                minXOffset,
                maxXOffset,
                minZOffset,
                maxZOffset,
                minYOffset,
                maxYOffset,
                vanillaResult);
    }

    private static final class CubicBounds {

        private static boolean isCubicWorld(World world) {
            return world instanceof com.cardinalstar.cubicchunks.world.ICubicWorld
                    && world.getChunkProvider() instanceof com.cardinalstar.cubicchunks.world.cube.ICubeProvider;
        }

        private static boolean isAvailable(World world, int x, int y, int z, long minXOffset, long maxXOffset,
                long minZOffset, long maxZOffset, int minYOffset, int maxYOffset, boolean vanillaResult) {
            if (!isCubicWorld(world)) return vanillaResult;

            com.cardinalstar.cubicchunks.world.ICubicWorld cubicWorld = (com.cardinalstar.cubicchunks.world.ICubicWorld) world;
            long minY = (long) y + minYOffset;
            long maxY = (long) y + maxYOffset;
            long minX = (long) x + minXOffset;
            long maxX = (long) x + maxXOffset;
            long minZ = (long) z + minZOffset;
            long maxZ = (long) z + maxZOffset;
            if (minX > maxX || minY > maxY
                    || minZ > maxZ
                    || minY < cubicWorld.getMinHeight()
                    || maxY >= cubicWorld.getMaxHeight()
                    || minX < Integer.MIN_VALUE
                    || maxX > Integer.MAX_VALUE
                    || minZ < Integer.MIN_VALUE
                    || maxZ > Integer.MAX_VALUE) {
                return false;
            }

            return world.checkChunksExist((int) minX, (int) minY, (int) minZ, (int) maxX, (int) maxY, (int) maxZ);
        }
    }
}
