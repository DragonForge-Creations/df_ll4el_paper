package me.quickscythe.paper.ll4el.utils.donations;

import org.bukkit.OfflinePlayer;

public record DonationEvent(Donation donation) {

    public OfflinePlayer player() {
        return donation.player();
    }

    public long participantId() {
        return donation.participantId();
    }

    public long donationId() {
        return donation.donationId();
    }
}
