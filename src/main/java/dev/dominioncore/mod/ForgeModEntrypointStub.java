package dev.dominioncore.mod;

import dev.dominioncore.runtime.DominionRuntime;

/**
 * Placeholder Forge entrypoint adapter for the prototype phase.
 *
 * This class intentionally avoids direct Forge imports so the plain-Java
 * prototype can compile in isolation while documenting the target wiring.
 */
public final class ForgeModEntrypointStub {
    private final DominionRuntime runtime = new DominionRuntime();

    public String modId() {
        return "dominioncore";
    }

    public DominionRuntime runtime() {
        return runtime;
    }

    public String lifecycleStatus() {
        return "ForgeEntrypointStub{modId='" + modId() + "', loader='" + runtime.preferredLoader() + "'}";
    }
}
