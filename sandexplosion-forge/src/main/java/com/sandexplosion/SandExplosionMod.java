package com.sandexplosion;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.FloatArgumentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("sandexplosion")
public class SandExplosionMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean explosionsEnabled = true;
    public static float explosionPower = 3.0f;

    public SandExplosionMod() {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("SandExplosion mod loaded!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("sandexplosion")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("on")
                                .executes(ctx -> {
                                    explosionsEnabled = true;
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§aSand explosions ENABLED"), true
                                    );
                                    return 1;
                                })
                        )

                        .then(Commands.literal("off")
                                .executes(ctx -> {
                                    explosionsEnabled = false;
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§cSand explosions DISABLED"), true
                                    );
                                    return 1;
                                })
                        )

                        .then(Commands.literal("status")
                                .executes(ctx -> {
                                    String state = explosionsEnabled ? "§aENABLED" : "§cDISABLED";
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Sand explosions: " + state + " §7| Power: §e" + explosionPower), false
                                    );
                                    return 1;
                                })
                        )

                        .then(Commands.literal("set")
                                .then(Commands.argument("power", FloatArgumentType.floatArg(0.1f, 20.0f))
                                        .executes(ctx -> {
                                            explosionPower = FloatArgumentType.getFloat(ctx, "power");
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("§eExplosion power set to: §f" + explosionPower), true
                                            );
                                            return 1;
                                        })
                                )
                        )

                        .then(Commands.literal("reset")
                                .executes(ctx -> {
                                    explosionPower = 3.0f;
                                    explosionsEnabled = true;
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§aSand explosions reset to defaults (power: 3.0, enabled)"), true
                                    );
                                    return 1;
                                })
                        )
        );
    }
}