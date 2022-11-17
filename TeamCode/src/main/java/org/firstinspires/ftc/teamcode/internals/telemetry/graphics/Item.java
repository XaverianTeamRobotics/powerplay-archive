package org.firstinspires.ftc.teamcode.internals.telemetry.graphics;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * An Item is a choice in a {@link Menu}.
 */
public class Item {

    private final String NAME;

    /**
     * Creates an item.
     * @param name The name of the item, or what will be displayed on the menu for this item.
     */
    public Item(String name) {
        NAME = name;
    }

    public String getName() {
        return NAME;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return getName() != null ? getName() : "NULL";
    }
}
