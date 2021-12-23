package gg.inventories.adapters.items;

import com.google.gson.JsonObject;

public abstract class ItemAdapter<I> {
    public abstract JsonObject toJson(I item);
}
