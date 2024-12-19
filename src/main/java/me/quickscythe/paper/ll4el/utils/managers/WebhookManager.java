package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WebhookManager extends ConfigManager {
    public WebhookManager(JavaPlugin plugin) {
        super(plugin, "webhooks", "webhooks.json");
        for(String key : config().getData().keySet()){
            JSONObject webhook = config().getData().getJSONObject(key);
            WebhookUtils.add(key, webhook.getString("id"), webhook.getString("token"));
            CoreUtils.logger().log("WebhookManager", "Added webhook " + key);
        }
    }
}
