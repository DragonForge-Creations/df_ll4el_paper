package me.quickscythe.paper.ll4el.utils.managers.party;

import json2.JSONObject;

import java.awt.*;

public class Party {
    private final JSONObject data;

    public Party(JSONObject data) {
        this.data = data;
    }

    public void setColor(Color yellow) {
        data.put("color", yellow.getRGB());
    }
}
