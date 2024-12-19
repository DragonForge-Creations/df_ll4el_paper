package me.quickscythe.paper.ll4el.utils.managers.party;

import json2.JSONObject;

import java.awt.*;

public record Party(String name, JSONObject data) {

    public Color getColor() {
        return new Color(data.has("color") ? data.getInt("color") : 16777215);
    }

    public void setColor(Color yellow) {
        data.put("color", yellow.getRGB());
    }
}
