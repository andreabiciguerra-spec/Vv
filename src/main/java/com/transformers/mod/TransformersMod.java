package com.transformers.mod;

import com.transformers.mod.capability.PlayerTransformationProvider;
import com.transformers.mod.client.ClientSetup;
import com.transformers.mod.config.ModConfig;
import com.transformers.mod.entity.ModEntityTypes;
import com.transformers.mod.event.ModEvents;
import com.transformers.mod.init.ModItems;
import com.transformers.mod.network.ModMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TransformersMod.MOD_ID)
public class TransformersMod {
    public static final String MOD_ID = "transformersmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public TransformersMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Config
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC, "transformersmod-common.toml");

        // Registries
        ModItems.ITEMS.register(modBus);
        ModEntityTypes.ENTITIES.register(modBus);

        // Lifecycle
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerCapabilities);

        // Forge events
        forgeBus.register(new ModEvents());
        forgeBus.register(new PlayerTransformationProvider());

        LOGGER.info("Transformers Mod initializing... Autobots, roll out!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            LOGGER.info("Transformers network and common setup complete.");
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.init();
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(com.transformers.mod.capability.PlayerTransformation.class);
    }
  }
