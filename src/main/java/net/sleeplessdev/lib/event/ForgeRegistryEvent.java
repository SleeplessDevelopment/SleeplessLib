package net.sleeplessdev.lib.event;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.sleeplessdev.lib.base.ModContainers;

import java.util.Collection;
import java.util.Objects;

class ForgeRegistryEvent<V extends IForgeRegistryEntry<V>> extends Event implements IContextSetter {

    private final IForgeRegistry<V> registry;

    protected ForgeRegistryEvent(IForgeRegistry<V> registry) {
        this.registry = registry;
    }

    public void register(V entry) {
        Objects.requireNonNull(entry.getRegistryName());
        registry.register(entry);
    }

    public void register(V entry, ResourceLocation name) {
        if (entry.getRegistryName() == null)
            entry.setRegistryName(name);
        register(entry);
    }

    public void register(V entry, String name) {
        register(entry, ModContainers.applyActiveDomain(name));
    }

    public void registerAll(V... entries) {
        for (V entry : entries) {
            register(entry);
        }
    }

    public void registerAll(Collection<V> entries) {
        for (V entry : entries) {
            register(entry);
        }
    }

    public V retrieve(ResourceLocation name) {
        return registry.getValue(name);
    }

    public ResourceLocation retrieve(V entry) {
        return registry.getKey(entry);
    }

    public ImmutableList<V> retrieveAll() {
        return ImmutableList.copyOf(registry.getValues());
    }

    static class Post<V extends IForgeRegistryEntry<V>>  extends Event {
        private final IForgeRegistry<V> registry;

        protected Post(IForgeRegistry<V> registry) {
            this.registry = registry;
        }

        public V retrieve(ResourceLocation name) {
            return registry.getValue(name);
        }

        public ResourceLocation retrieve(V entry) {
            return registry.getKey(entry);
        }

        public ImmutableList<V> retrieveAll() {
            return ImmutableList.copyOf(registry.getValues());
        }
    }

}
