package gg.inventories.adapters.player;

import com.google.gson.JsonObject;
import gg.inventories.adapters.items.ItemAdapter;

public abstract class PlayerAdapter <P,I extends ItemAdapter> {

    public abstract JsonObject toJson(P player);

    public abstract I getItemAdapter();
}