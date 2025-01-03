package me.quickscythe.paper.ll4el.utils;

import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.entity.Cat.Type.*;

public enum EntitySkullData {
    ALLAY("Allay", "Y2MwMzg5MTc3ZGJhYTkyZjBkNWZmZGY4NDg4NjJjN2Y5YjM2ZGYyMjJmYmZkNzM3ZTI2MzlkYzMwNTllMGNmMy"),
    ARMADILLO("Armadillo", "NjYwN2FlN2E3MDE3NjczMzZjMDZiMGM0ZmZhODFkZmYyY2ZkOGJjMDcwZDk1NzE0YTZiYWRmMGVmYjcyNjNlMS"),
    BAT("Bat", "M2ViNTg4ZGNkMGJiNTdjZTZkZGFiOGUzYWZiNmZkNDMzMDA2NGVhYWMwMWI2MWE4ZTk3NjlmMDQ3NmY1MmY1MC"),
    BLAZE("Blaze", "ZGVlMjNkYzdhMTBjNmE4N2VmOTM3NDU0YzBlOTRlZDQyYzIzYWE2NDFhOTFlZDg0NzBhMzA0MmQwNWM1MmM1Mi"),
    BOGGED("Bogged", "NGY4MDdhMTg3MDc3ZjgzNmI5MzgyMGIzMmQ4ZDgzNDFkNGQzMmNkNGM4YzExMTVjZjFkYTYzNzRlMGZiZDNmZi"),
    BREEZE("Breeze", "YTI3NTcyOGFmN2U2YTI5Yzg4MTI1YjY3NWEzOWQ4OGFlOTkxOWJiNjFmZGMyMDAzMzdmZWQ2YWIwYzQ5ZDY1Yy"),
    CAMEL("Camel", "ZTY3ZDQ1OTczNDAxNjZlMTk3OGE2NjhhMDZiZjU3NTZjMTdiNGNiNWI0MGFiOGZmMjQ0MDkzYjZiOGJjNzVkMy"),
    CAVE_SPIDER("Cave Spider", "YTZhMWMyNTk5ZmM5MTIwM2E2NWEwM2Q0NzljOGRjODdmNjYyZGVhYzM2NjNjMTZjNWUwNGQ2MjViMzk3OGEyNS"),
    CHICKEN("Chicken", "NDJhZjZlNTg0N2VlYTA5OWUxYjBhYjhjMjBhOWU1ZjNjNzE5MDE1OGJkYTU0ZTI4MTMzZDliMjcxZWMwY2I0Yi"),
    COD("Cod", "ZjI0NmUxOWIzMmNmNzg0NTQ5NDQ3ZTA3Yjk2MDcyZTFmNjU2ZDc4ZTkzY2NjYTU2Mzc0ODVlNjc0OTczNDY1Mi"),
    COW("Cow", "NjNkNjIxMTAwZmVhNTg4MzkyMmU3OGJiNDQ4MDU2NDQ4Yzk4M2UzZjk3ODQxOTQ4YTJkYTc0N2Q2YjA4YjhhYi"),
    CREEPER_CHARGED("Charged Creeper", "MzUxMWU0YTNkNWFkZDZhNTQ0OTlhYmFkMTBkNzk5ZDA2Y2U0NWNiYTllNTIwYWZkMjAwODYwOGE2Mjg4YjdlNy"),
    DOLPHIN("Dolphin", "OGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ"),
    DONKEY("Donkey", "NGUyNWVlOTI3M2FkNTc5ZDQ0YmY0MDZmNmY2Mjk1NTg2NDgxZWExOThmZDU3MjA3NmNkMGM1ODgyZGE3ZTZjYy"),
    DROWNED("Drowned", "YzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MS"),
    ELDER_GUARDIAN("Elder Guardian", "NGEyZDY0ZjRhMDBlOWM4NWY2NzI2MmVkY2FjYjg0NTIzNTgxYWUwZjM3YmRhYjIyZGQ3MDQ1MjRmNjJlMTY5Zi"),
    ENDERMAN("Enderman", "ODk3N2E5NGYwMjQ5OGNhZDBjZmRiNjVjYTdjYjcyZTIzMTExYTkxNGQ4YzY3MGFjY2NjN2E2NWIzNDdkNzc3Ni"),
    ENDERMITE("Endermite", "OGM2YjY1YzIyYjQ0NjViYTY3OTNiMjE5NWNkNTA4NGNlODNiODhkY2E2ZTU1ZWI5NDg0NTQwYWNkNzM1MmE1MC"),
    EVOKER("Evoker", "MzkwZmJkODhmNjU5ZDM5NjNjNjhjYmJjYjdjNzEyMWQ4MTk1YThiZTY1YmJkMmJmMTI1N2QxZjY5YmNjYzBjNy"),
    FOX("Fox", "ZDdlMDA0MzExMWJjNTcwOTA4NTYyNTkxNTU1NzFjNzkwNmU3MDcwNDZkZjA0MWI4YjU3MjcwNGM0NTFmY2Q4Mi"),
    GHAST("Ghast", "YzUzZGUzMWEyZDAwNDFhNmVmNzViZjdhNmM4NDY4NDY0ZGIxYWFhNjIwMWViYjFhNjAxM2VkYjIyNDVjNzYwNy"),
    GLOW_SQUID("Glow Squid", "NGIyZTliNjU4MWZlZDQ4YTk5ZTAzMjMwOTFhZDVjM2MzMjZjZGEyMDA3M2UyOGE5MDJhMDM3M2Y3MzgyYjU5Zi"),
    GOAT("Goat", "ODc0NzNlMDU1ZGY2ZTdmZDk4NjY0ZTlmZGI2MzY3NWYwODgxMDYzMDVkNzQ0MDI0YTQxYmIzNTg5MThhMTQyYi"),
    GOAT_SCREAMING("Screaming Goat", "YmRhNDg1YWMyMzUxMjQyMDg5MWE1YWUxZThkZTk4OWYwOTFkODQ4ZDE1YTkwNjhkYTQ3MjBkMzE2ZmM0MzMwZi"),
    GUARDIAN("Guardian", "ZTJiYTM0NDE2NjcwNDU0YjFhMjA0OTZmODBiOTM5ODUyOWY0OTAwM2ZjNjEzZWI5MzAyNDhlYTliNWQxYTM5MS"),
    HOGLIN("Hoglin", "YmM0YTdmNTdmYzAzYjEzYWEyZjlkODNjZGQ0ODIyYjkzNjc5MzA5NmRhZjUxZTc4MDI1YmJkMjQxZWQ2ZjY4ZC"),
    HUSK("Husk", "NzMzODMxOGJjOTFhMzZjZDVhYjZhYTg4NWM5YTRlZTJiZGFjZGFhNWM2NmIyYTk5ZGZiMGE1NjA5ODNmMjQ4MC"),
    ILLUSIONER("Illusioner", "ZDM4MjcwMWM2N2Q2YzU0YzkwNzU1ODg5MWRjMTc2MjI1MTEyNTE4NzcxZTA2MWM1ZDhiZDkxODQ3OWU2YmRkOC"),
    IRON_GOLEM("Iron Golem", "ZmU3YzA3MTlmYWJlMTE2ZGNlNjA1MTk5YmNhZGM2OWE1Mzg4NjA4NjRlZjE1NzA2OTgzZmY2NjI4MjJkOWZlMy"),
    MAGMA_CUBE("Magma Cube", "YjgxNzE4ZDQ5ODQ4NDdhNGFkM2VjMDgxYTRlYmZmZDE4Mzc0MzIzOWFlY2FiNjAzMjIxMzhhNzI2MDk4MTJjMy"),
    MOOSHROOM("Mooshroom", "ZGE4MDYwNmU4MmM2NDJmMTQxNTg3NzMzZTMxODBhZTU3ZjY0NjQ0MmM5ZmZmZDRlNTk5NzQ1N2UzNDMxMWEyOS"),
    MOOSHROOM_BROWN("Brown Mooshroom Cow", "N2U2NDY2MzAyYTVhYjQzOThiNGU0NzczNDk4MDhlNWQ5NDAyZWEzYWQ4ZmM0MmUyNDQ2ZTRiZWQwYTVlZDVlIn19fQ"),
    MULE("Mule", "NDFjMjI0YTEwMzFiZTQzNGQyNWFlMTg4NWJmNGZmNDAwYzk4OTRjNjliZmVmNTZhNDkzNTRjNTYyNWMwYzA5Yy"),
    OCELOT("Ocelot", "OTE3NWNjNDNlYThhZTIwMTY4YTFmMTcwODEwYjRkYTRkOWI0ZWJkM2M5OTc2ZTlmYzIyZTlmOTk1YzNjYmMzYy"),
    PHANTOM("Phantom", "N2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4Mi"),
    PIG("Pig", "NDFlZTc2ODFhZGYwMDA2N2YwNGJmNDI2MTFjOTc2NDEwNzVhNDRhZTJiMWMwMzgxZDVhYzZiMzI0NjIxMWJmZS"),
    PIGLIN_BRUTE("Piglin Brute", "NjQ4ODc5OWM4M2VjYjI5NDUyY2ViYTg5YzNjMDA5OTIxOTI3NGNlNWIyYmZiOGFkMGIzZWE0YzY1ZmFjNDYzMC"),
    PILLAGER("Pillager", "YzIyNWYwYjQ5YzUyOTUwNDhhNDA5YzljNjAxY2NhNzlhYThlYjUyYWZmNWUyMDMzZWJiODY1ZjQzNjdlZjQzZS"),
    POLAR_BEAR("Polar Bear", "Y2Q4NzAyOTExZTYxNmMwZDMyZmJlNzc4ZDE5NWYyMWVjY2U5MDI1YmNiZDA5MTUxZTNkOTdhZjMxOTJhYTdlYy"),
    PUFFERFISH("Pufferfish", "ZTI3MzNkNWRhNTljODJlYWYzMTBiMzgyYWZmNDBiZDUxM2M0NDM1NGRiYmFiZmUxNGIwNjZhNTU2ODEwYTdmOS"),
    RAVAGER("Ravager", "ZWI0ZGIyOTg2MTQwZTI1MWUzMmU3MGVkMDhjOGEwODE3MjAzMTNjZTI1NzYzMmJlMWVmOTRhMDczNzM5NGRiIn19fQ"),
    SALMON("Salmon", "NzkxZDllNjliNzk1ZGE0ZWFhY2ZjZjczNTBkZmU4YWUzNjdmZWQ4MzM1NTY3MDZlMDQwMzM5ZGQ3ZmUwMjQwYS"),
    SHULKER("Shulker", "ZmI5ZTZhZjZiODE5ZjNkOTBlNjdjZTJlNzA1OWZiZWYzMWRhMmFhOTUzZDM1ZTM0NTRmMTAyMWZhOTEyZWZkZS"),
    SILVERFISH("Silverfish", "ZjI1ZTlmYWUzNzE2NjRkZTFhODAwYzg0ZDAyNTEyNGFiYjhmMTUxMTE4MDdjOGJjMWFiOTEyNmFhY2JkNGY5NS"),
    SKELETON_HORSE("Skeleton Horse", "NmUyMjY3MDViZDJhOWU3YmI4ZDZiMGY0ZGFhOTY5YjllMTJkNGFlNWM2NmRhNjkzYmI1ZjRhNGExZTZhYTI5Ni"),
    SLIME("Slime", "YzA2NDI0ZWM3YTE5NmIxNWY5YWQ1NzMzYTM2YTZkMWYyZTZhMGQ0MmZmY2UxZTE1MDhmOTBmMzEyYWM0Y2FlZC"),
    SNIFFER("Sniffer", "Yzg0YTdlN2ZlMTk3YjdlNzQxOWI1MWQ0NmNjMjMzNTUxYjllYzg5OWRlMWFmZTdmNjUzZTRmOGZiMjZhNjg2ZS"),
    SNOW_FOX("Snow Fox", "NDE0MzYzNzdlYjRjNGI0ZTM5ZmIwZTFlZDg4OTlmYjYxZWUxODE0YTkxNjliOGQwODcyOWVmMDFkYzg1ZDFiYS"),
    SNOW_GOLEM("Snow Golem", "Y2FhM2UxN2VmMWIyOWE0Yjg3ZmE0M2RlZTFkYjEyYzQxZmQzOWFhMzg3ZmExM2FmMmEwNzliNWIzNzhmZGU4Yi"),
    SPIDER("Spider", "ZGUyOGU2NjI5YjZlZDFkYTk0ZDRhODE4NzYxNjEyYzM2ZmIzYTY4MTNjNGI2M2ZiOWZlYTUwNzY0MTVmM2YwYy"),
    SQUID("Squid", "ODM1MWI3ZDlhNGYzNmNmZTMxZmQ1OWQ4YzkwMGU0MTlhMTM1MTQ0MTA1ZTdhOTgxY2FhNWExNjhkY2ZmMzI1Yi"),
    STRAY("Stray", "NTkyYjU1OTcwODVlMzVkYjUzZDliZGEwMDhjYWU3MmIyZjAwY2Q3ZDRjZDhkYzY5ZmYxNzRhNTViNjg5ZTZlIn19fQ"),
    STRIDER("Strider", "OWM0MGZhZDFjMTFkZTllNjQyMmI0MDU0MjZlOWI5NzkwN2YzNWJjZTM0NWUzNzU4NjA0ZDNlN2JlN2RmODg0In19fQ"),
    STRIDER_COLD("Cold Strider", "MjcxMzA4NWE1NzUyN2U0NTQ1OWMzOGZhYTdiYjkxY2FiYjM4MWRmMzFjZjJiZjc5ZDY3YTA3MTU2YjZjMjMwOS"),
    TADPOLE("Tadpole", "M2RhZjE2NTNiNWY1OWI1ZWM1YTNmNzk2MDljYjQyMzM1NzlmZWYwN2U2OTNiNjE3NDllMDkwMDE0OWVkZjU2My"),
    TROPICAL_FISH("Tropical Fish", "MzRhMGM4NGRjM2MwOTBkZjdiYWZjNDM2N2E5ZmM2Yzg1MjBkYTJmNzNlZmZmYjgwZTkzNGQxMTg5ZWFkYWM0MS"),
    TURTLE("Turtle", "MzA0OTMxMjAwYWQ0NjBiNjUwYTE5MGU4ZDQxMjI3YzM5OTlmYmViOTMzYjUxY2E0OWZkOWU1OTIwZDFmOGU3ZC"),
    VEX("Vex", "Yjk1MzhmMjgzMGM0ZGVhNjk5NmVkNzQ0Nzg1NTA0ZTMyZTBlMjBkODY2M2VkYWI2YjAyMjJmMmMwMjIwNzdiZC"),
    VINDICATOR("Vindicator", "MmRhYmFmZGUyN2VlMTJiMDk4NjUwNDdhZmY2ZjE4M2ZkYjY0ZTA0ZGFlMWMwMGNjYmRlMDRhZDkzZGNjNmM5NS"),
    WANDERING_TRADER("Wandering Trader", "NWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMi"),
    WARDEN("Warden", "YjJmMzg3OWI3MzcxMjc0ODVlYjM1ZGRlZTc0OGQwNmNmOTE0YjE5M2Q5Nzc1M2FlMzRlOTIyMzA4NDI4MzFmYi"),
    WITCH("Witch", "YTUyMGYxMmM2M2M3OTEyMTg2YzRiZTRlMzBjMzNjNWFjYWVjMGRiMGI2YWJkODM2ZDUxN2Q3NGE2MjI3NWQ0Yi"),
    WITHER("Wither", "ZWRhMTA4MjhmNjNiN2VjZGVmZDc2N2IzMjQ1ZmJkYWExM2MzZWMwYzZiMTM3NzRmMWVlOGQzMDdjMDM0YzM4My"),
    WITHER_PROJECTILE("Wither Projectile", "YjM3YzU4MTRhOTJmOGVjMGY2YWU5OTMzYWJlOTU0MmUxNjUxOTA3NjhlNzYwNDc4NTQzYWViZWVkNDAyN2MyNy"),
    WITHER_BLUE_PROJECTILE("Blue Wither Projectile", "ZDM2ODJiMDYyMDNiOWRlNGMyODU0MTA3MWEyNmNkYzM0MGRkMjVkNGMzNzJiNzAyM2VjMmY0MTIwMjFkNjJmNy"),
    ZOGLIN("Zoglin", "MmUzNDkzYTk1NmJmZDc1ODhlZDFhOGVhODU4NzU5NjY3NjU5ZDU4MTAwY2JlY2Q2ZDk2Y2NjMGNhOWIzNjkyMy"),
    ZOMBIE_HORSE("Zombie Horse", "NjYxOGZmYmUxY2ZhMjA1OGZlODBhMDY1ZjcwYzEyOGMyMjVhMWUwYmM5ZGVhZjhiMzhiMDM5NTQ0M2Y0MDkwOS"),
    ZOMBIFIED_PIGLIN("Zombified Piglin", "MmRmMDMxMjhiMDAyYTcwNzA4ZDY4MjVlZDZjZjU0ZGRmNjk0YjM3NjZkNzhkNTY0OTAzMGIxY2I4YjM0YzZmYS"),

    // Sheep
    SHEEP_RED("Red Sheep", "NTQ3OGUwNTcxNThkZTZmNDVlMjU0MWNkMTc3ODhlNjQwY2NiNTk3MjNkZTU5YzI1NGU4MmFiNTcxMWYzZmMyNy"),
    SHEEP_ORANGE("Orange Sheep", "ZjY4NGQwNGZhODBhYTU5ZGExNDUzNWRlYWQzODgzZDA5N2ZiYmE0MDA2MjU2NTlmNTI1OTk2NDgwNmJhNjZmMC"),
    SHEEP_YELLOW("Yellow Sheep", "OTRiMjhmMDM1NzM1OTA2ZjgyZmZjNGRiYTk5YzlmMGI1NTI0MGU0MjZjZDFjNTI1YTlhYTc3MTgwZWVjNDkzNC"),
    SHEEP_LIME("Lime Sheep", "NmJlYWQwMzQyYWU4OWI4ZGZkM2Q3MTFhNjBhZGQ2NWUyYzJiZmVhOGQwYmQyNzRhNzU4N2RlZWQ3YTMxODkyZS"),
    SHEEP_GREEN("Green Sheep", "OWVhODg3ZWFlNGIwNzYzNmU5ZTJmOTA2NjA5YjAwYWI4ZDliODZiNzQ3MjhiODE5ZmY2ZjM3NjU4M2VhMTM5In19fQ"),
    SHEEP_LIGHT_BLUE("Light Blue Sheep", "ZWJmMjNhZjg3MTljNDM3YjNlZTg0MDE5YmEzYzllNjljYTg1NGQzYThhZmQ1Y2JhNmQ5Njk2YzA1M2I0ODYxNC"),
    SHEEP_CYAN("Cyan Sheep", "NWQ0MmZjYmNhZjlkNDhmNzNmZmIwYzNjMzZmMzRiNDY0MzI5NWY2ZGFhNmNjNzRhYjlkMjQyZWQ1YWE1NjM2In19fQ"),
    SHEEP_BLUE("Blue Sheep", "NzQwZTI3N2RhNmMzOThiNzQ5YTMyZjlkMDgwZjFjZjRjNGVmM2YxZjIwZGQ5ZTVmNDIyNTA5ZTdmZjU5M2MwIn19fQ"),
    SHEEP_PURPLE("Purple Sheep", "MzQ0OWQwODI5MWRhZTQ1YTI0NjczNjE5NjAyZjQzNWI1N2Y0Y2Q0ZTllOThkMmUwZmJlYzRmMTgxNDQ3ODFkMy"),
    SHEEP_MAGENTA("Magenta Sheep", "YThlMWYwNWYwZGFjY2E2M2E3MzE4NzRmOTBhNjkzZmZlMjFmZjgzMmUyYjFlMWQwN2I2NWM4NzY0NTI2ZjA4OS"),
    SHEEP_PINK("Pink Sheep", "NjM2M2U4YTkzZDI4N2E4NGU2NDAzMDlhZTgzY2ExZGUwYTBiMjU3NTA1YTIwZWM1NWIzMzQ5ZDQwYTQ0ODU0In19fQ"),
    SHEEP_WHITE("White Sheep", "NmRmZTdjYzQ2ZDc0OWIxNTMyNjFjMWRjMTFhYmJmMmEzMTA4ZWExYmEwYjI2NTAyODBlZWQxNTkyZGNmYzc1Yi"),
    SHEEP_GRAY("Gray Sheep", "M2ZhZmVjZjA2MDNiMmRjZDc5ODRkMjUyNTg2MDY5ODk1ZGI5YWE3OGUxODQxYmQ1NTRiMTk1MDhkY2Y5NjdhMS"),
    SHEEP_LIGHT_GRAY("Light Gray Sheep", "MWQyZTJlOTNhMTQyYmZkNDNmMjQwZDM3ZGU4ZjliMDk3NmU3NmU2NWIyMjY1MTkwODI1OWU0NmRiNzcwZS"),
    SHEEP_BLACK("Black Sheep", "MTMzMzVlODA2NWM3YjVkZmVhNThkM2RmNzQ3NGYzOTZhZjRmYTBhMmJhNTJhM2M5YjdmYmE2ODMxOTI3MWM5MS"),
    SHEEP_BROWN("Brown Sheep", "MzEyOGQwODZiYzgxNjY5ZmMyMjU1YmIyMmNhZGM2NmEwZjVlZDcwODg1ZTg0YzMyZDM3YzFiNDg0ZGIzNTkwMS"),
    SHEEP_RAINBOW("Rainbow Sheep", "MjMzMzI2NzY1YTE5MGViZjkwZDU0ODZkNzFmMjBlMjU5N2U0YmVlMmEzOTFmZWNiYmQ4MGRlYmZlMWY4MmQ3OCJ9fX0"),

    //Axolotl

    AXOLOTL_LUCY("Lucy Axolotl", "NjY3ZTE1ZWFiNzMwNjRiNjY4MGQxZGI5OGJhNDQ1ZWQwOTE0YmEzNWE3OTk5OTdjMGRhMmIwM2ZmYzNhODgyNi"),
    AXOLOTL_WILD("Wild Axolotl", "NDdjZjAyNzQ5OThiZjVhN2YzOGIzNzAzNmUxNTRmMTEyZmEyZTI4YmFkNDBkNWE3Yzk0NzY1ZmU0ZjUyMjExZS"),
    AXOLOTL_GOLD("Gold Axolotl", "ZTU4NTYwMTE1ZmFhZDExNjE5YjNkNTVkZTc5ZWYyYTA1M2Y0NzhhNjcxOTRiYmU5MjQ3ZWRlYTBiYzk4ZTgzNC"),
    AXOLOTL_CYAN("Cyan Axolotl", "ODUxMTk2ZDQzOTMwNjU5ZDcxN2UxYjZhMDQ2YTA4ZDEyMjBmY2I0ZTMxYzQ4NTZiYzMzZTc1NTE5ODZlZjFkIn19fQ"),
    AXOLOTL_BLUE("Blue Axolotl", "NjhmZDEwYjBmZWY0NTk1OTYwYjFmNjQxOTNiYzhhMTg2NWEyZDJlZDQ4YjJlMmNlMDNkOTk0NTYzMDI3ZGY5NS"),

    //Bees

    BEE_NORMAL("Bee", "NTlhYzE2ZjI5NmI0NjFkMDVlYTA3ODVkNDc3MDMzZTUyNzM1OGI0ZjMwYzI2NmFhMDJmMDIwMTU3ZmZjYTczNi"),
    BEE_POLLINATED("Pollenated Bee", "YjcyN2QwYWIwM2Y1Y2QwMjJmODcwNWQzZjdmMTMzY2E0OTIwZWFlOGUxZTQ3YjUwNzQ0MzNhMTM3ZTY5MWU0ZS"),
    BEE_ANGRY("Angry Bee", "ZTQwMDIyM2YxZmE1NDc0MWQ0MjFkN2U4MDQ2NDA5ZDVmM2UxNWM3ZjQzNjRiMWI3Mzk5NDAyMDhmM2I2ODZkNC"),
    BEE_ANGRY_POLLINATED("Angry Pollinated Bee", "ZTZiNzRlMDUyYjc0Mjg4Nzk5YmE2ZDlmMzVjNWQwMjIxY2Y4YjA0MzMxNTQ3ZWMyZjY4ZDczNTk3YWUyYzliIn19fQ"),

    //Cats

    CAT_TABBY("Tabby Cat", "ZGUyOGQzMGRiM2Y4YzNmZTUwY2E0ZjI2ZjMwNzVlMzZmMDAzYWU4MDI4MTM1YThjZDY5MmYyNGM5YTk4YWUxYi"),
    CAT_TUXEDO("Tuxedo Cat", "NGZkMTBjOGU3NWY2NzM5OGM0NzU4N2QyNWZjMTQ2ZjMxMWMwNTNjYzVkMGFlYWI4NzkwYmNlMzZlZTg4ZjVmOC"),
    CAT_GINGER("Ginger Cat", "MjExM2RiZDNjNmEwNzhhMTdiNGVkYjc4Y2UwN2Q4MzZjMzhkYWNlNTAyN2Q0YjBhODNmZDYwZTdjYTdhMGZjYi"),
    CAT_SIAMESE("Siamese Cat", "ZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYS"),
    CAT_BRITISH("British Shorthair Cat", "NTM4OWUwZDVkM2U4MWY4NGI1NzBlMjk3ODI0NGIzYTczZTVhMjJiY2RiNjg3NGI0NGVmNWQwZjY2Y2EyNGVlYy"),
    CAT_CALICO("Calico Cat", "MzQwMDk3MjcxYmI2ODBmZTk4MWU4NTllOGJhOTNmZWEyOGI4MTNiMTA0MmJkMjc3ZWEzMzI5YmVjNDkzZWVmMy"),
    CAT_PERSIAN("Persian Cat", "ZmY0MGM3NDYyNjBlZjkxYzk2YjI3MTU5Nzk1ZTg3MTkxYWU3Y2UzZDVmNzY3YmY4Yzc0ZmFhZDk2ODlhZjI1ZC"),
    CAT_RAGDOLL("Ragdoll Cat", "ZGM3YTQ1ZDI1ODg5ZTNmZGY3Nzk3Y2IyNThlMjZkNGU5NGY1YmMxM2VlZjAwNzk1ZGFmZWYyZTgzZTBhYjUxMS"),
    CAT_WHITE("White Cat", "MjFkMTVhYzk1NThlOThiODlhY2E4OWQzODE5NTAzZjFjNTI1NmMyMTk3ZGQzYzM0ZGY1YWFjNGQ3MmU3ZmJlZC"),
    CAT_JELLIE("Jellie Cat", "YTBkYjQxMzc2Y2E1N2RmMTBmY2IxNTM5ZTg2NjU0ZWVjZmQzNmQzZmU3NWU4MTc2ODg1ZTkzMTg1ZGYyODBhNS"),
    CAT_BLACK("Black Cat", "MjJjMWU4MWZmMDNlODJhM2U3MWUwY2Q1ZmJlYzYwN2UxMTM2MTA4OWFhNDdmMjkwZDQ2YzhhMmMwNzQ2MGQ5Mi"),

    //Dogs
    WOLF_ASHEN("Ashen Wolf", "NzIzODRjNWNmMTg5NDhiODNhODk1NDhkYmE1YTk5NDVlZGVlZmM1ZTk2NTRjNWQ2ZDM4YWUxMGE1ZDUwMmU3NS"),
    WOLF_ASHEN_ANGRY("Angry Ashen Wolf", "N2NiYzMwNjZkMzFjNDM5MDM1MDM4ZmQ2ODc1ZDVkYmVlYzM5NjhjMWI4MDA2ZmZiZmI1ZjY3NGQ3NmM4OWNkZS"),
    WOLF_BLACK("Black Wolf", "YzVhNjZhNDJiMjVmODIyYTdlMTZhMjE4NzUyOGQxYTJlMjk0YTAxZDlmODUwNjcxYjk0Yzk1NzQyYmI0OTE2ZS"),
    WOLF_BLACK_ANGRY("Angry Black Wolf", "YTVlZDQ3ZGVkMjcwOGIxM2Q5MmViNTBmYjY4ZThjMWUxMWIzOWEwY2Q0NWIzOTM3MmVlYWQ4NzJjNDllZWFlYi"),
    WOLF_CHESTNUT("Chestnut Wolf", "Y2I5YjBkNDg0NDIyMDRmZjZmZDM5ZmEwNzQxNjcxMThlOWMwNjZjZGUzODg4OTc3ZDBmNjAzNmUxZDhhNjllZS"),
    WOLF_CHESTNUT_ANGRY("Angry Chestnut Wolf", "Mjg0YjI4ZjIzMmIxNGE1OWI2Y2I3NzU3MzIzOTc0ZWE1MDJiMWJjYjk4NGRlYTMwMDkzZWMyMWVkMmFkZTMxMi"),
    WOLF_PALE("Pale Wolf", "MWVlMzNjMmRjMDdkNzZiNGYwM2U2NjQyN2EwOGNiYTJlODE3OWQwNzVhZTY0YjljZTE1MGFhNDIwOWM1YWYzOS"),
    WOLF_PALE_ANGRY("Angry Pale Wolf", "Njg4N2E0Mjc4NzkwMGU2NzE2ZmE0NjJmYmFkOGRlYjU1MjZiOTQzOTg3OTc0MTRmMDNmNjAxM2VmODg1YTFkYi"),
    WOLF_RUSTY("Rusty Wolf", "MjM0NTVmNjA0OGE2ODA5OGNkMjc2MzRlMzE0NmM4MWM4MjY5YWVlZmNmMGFmZjkxY2M5NzZlZmEwYmFhMTE0Ni"),
    WOLF_RUSTY_ANGRY("Angry Rusty Wolf", "ZDFmMWMzMmU1MjU4ZjNkOGY4ZDE4MWZiMzBkZjYxZTA2OTNlNTVkNTM4YTEzZWVhYmRmNjMwMGYzODA4M2FkYy"),
    WOLF_SNOWY("Snowy Wolf", "NGVjYWRhYjUwYWE4ZDQxZmE5YjM2OWEyZjg0Zjk3NDU2YmU3OTAwYjIyMGVjZTNiOTVlOGEwMDk2ODY2MGQ1In19fQ"),
    WOLF_SNOWY_ANGRY("Angry Snowy Wolf", "NTJiN2MyODZjMjMwODI2YjI5ZTdmZDM3ZjI1NzNiOTAxNWM0MjJiYzM4ZmViMTRkOWEzMTdjNjg1NWFkYTNmNi"),
    WOLF_SPOTTY("Spotty Wolf", "NTczYjlhNjQzMWFmMjZiY2IzMTgyNmViNmZkOWY0YjM1Yjk0N2JhNTg4MmM2ZTRhYTkzNTg4NjMzZjdiOGQ5ZC"),
    WOLF_SPOTTY_ANGRY("Angry Spotty Wolf", "MTc4NmI3MzkzNDhhYTg1MDJlYTE4NWRmYjE0YmY1YWIwMWUyOWUwODJkMWZlYjg2MTNiM2ZlOTNlMGRlYmQ4ZS"),
    WOLF_STRIPED("Striped Wolf", "N2ZjNTJmYjNjZGZjNmFlYjAwZTY3YzFiN2E1OWQ4ZDMyMGRmNDQ2NTZjN2FmNjgyNGIxM2NhNjA3OTJhYTdkNy"),
    WOLF_STRIPED_ANGRY("Angry Striped Wolf", "OGQ2MGMyNTQ4OGIwNjcyNzY2OWE2OTE1ZDFkYWRhYTlhN2QyODMxYjQ2MGJlZTMwZTVkYTQwNzg3NDcwNTAwMS"),
    WOLF_WOODS("Woods Wolf", "OGQxMmFiMTc2NDdiNjljOTQyMTc2OTU3MmFjNjc0ZGUxOTkxMjRjMjg0YjllZDFmNjVhMjg1YzM4Y2QyYTUwNC"),
    WOLF_WOODS_ANGRY("Angry Woods Wolf", "NTMwNTgzZGJhOGVhNjE0MzA1ZGIwMTBiYWJkYzViYjQ0ZTlhMjAwMzMxMWIzOTlkODk2NWU3NzJkZDAxOTFmYi"),

    //Frogs

    FROG_COLD("Cold Frog", "NzY4Nzc4OTNlOTIwZmY1ZGZhNGI1ZmJkMTRkYWJlZTJlNjMwOGE2Zjk3YzNhMTliMDhlMjQxYTI5ZWI5YTVjMy"),
    FROG_TEMPERATE("Temperate Frog", "YTUwZDEwNzNkNDFmMTkzNDA1ZDk1YjFkOTQxZjlmZTFhN2ZmMDgwZTM4MTU1ZDdiYjc4MGJiYmQ4ZTg2ZjcwZC"),
    FROG_WARM("Warm Frog", "ZDViMGRhNDM5NzViODNjMzMyMjc4OGRkYTMxNzUwNjMzMzg0M2FlYmU1NTEyNzg3Y2IyZTNkNzY5ZWQyYjM4Mi"),

    //Horses

    HORSE_WHITE("White Horse", "YzdiYzYxNjA5NzMwZjJjYjAxMDI2OGZhYjA4MjFiZDQ3MzUyNjk5NzUwYTE1MDU5OWYyMWMzZmM0ZTkyNTkxYS"),
    HORSE_CREAMY("Creamy Horse", "NDJhMGQ1NGNjMDcxMjY3ZDZiZmQ1ZjUyM2Y4Yzg5ZGNmZGM1ZTgwNWZhYmJiNzYwMTBjYjNiZWZhNDY1YWE5NC"),
    HORSE_CHESTNUT("Chestnut Horse", "NmM4NzIwZDFmNTUyNjkzYjQwYTlhMzNhZmE0MWNlZjA2YWZkMTQyODMzYmVkOWZhNWI4ODdlODhmMDVmNDlmYS"),
    HORSE_BROWN("Brown Horse", "Njc3MTgwMDc3MGNiNGU4MTRhM2Q5MTE4NmZjZDc5NWVjODJlMDYxMDJmZjdjMWVlNGU1YzM4MDEwMmEwYzcwZi"),
    HORSE_BLACK("Black Horse", "NjcyM2ZhNWJlNmFjMjI5MmE3MjIzMGY1ZmQ3YWI2NjM0OTNiZDhmN2U2NDgxNjQyNGRjNWJmMjRmMTMzODkwYy"),
    HORSE_GRAY("Gray Horse", "YzI1OTg2MTAyMTgxMDgzZmIzMTdiYzU3MTJmNzEwNGRhYTVhM2U4ODkyNjRkZmViYjkxNTlmNmUwOGJhYzkwYy"),
    HORSE_DARK_BROWN("Dark Brown Horse", "N2YyMzQxYWFhMGM4MmMyMmJiYzIwNzA2M2UzMTkyOTEwOTdjNTM5YWRhZDlhYTkxM2ViODAwMWIxMWFhNTlkYS"),

    //Llamas

    LLAMA_CREAMY(   "Creamy Llama", "NGQ2N2ZkNGJmZjI5MzI2OWNiOTA4OTc0ZGNhODNjMzM0ODVlNDM1ZWQ1YThlMWRiZDY1MjFjNjE2ODcxNDAifX19"),
    LLAMA_WHITE(    "White Llama", "ODAyNzdlNmIzZDlmNzgxOWVmYzdkYTRiNDI3NDVmN2FiOWE2M2JhOGYzNmQ2Yjg0YTdhMjUwYzZkMWEzNThlYi"),
    LLAMA_BROWN(    "Brown Llama", "YzJiMWVjZmY3N2ZmZTNiNTAzYzMwYTU0OGViMjNhMWEwOGZhMjZmZDY3Y2RmZjM4OTg1NWQ3NDkyMTM2OC"),
    LLAMA_GRAY(     "Gray Llama", "Y2YyNGU1NmZkOWZmZDcxMzNkYTZkMWYzZTJmNDU1OTUyYjFkYTQ2MjY4NmY3NTNjNTk3ZWU4MjI5OWEifX19"),

    //Trader Llamas

    TRADER_LLAMA_CREAMY("Creamy Trader Llama", "ZTg5YTJlYjE3NzA1ZmU3MTU0YWIwNDFlNWM3NmEwOGQ0MTU0NmEzMWJhMjBlYTMwNjBlM2VjOGVkYzEwNDEyYy"),
    TRADER_LLAMA_WHITE( "White Trader Llama", "NzA4N2E1NTZkNGZmYTk1ZWNkMjg0NGYzNTBkYzQzZTI1NGU1ZDUzNWZhNTk2ZjU0MGQ3ZTc3ZmE2N2RmNDY5Ni"),
    TRADER_LLAMA_BROWN( "Brown Trader Llama", "ODQyNDc4MGIzYzVjNTM1MWNmNDlmYjViZjQxZmNiMjg5NDkxZGY2YzQzMDY4M2M4NGQ3ODQ2MTg4ZGI0Zjg0ZC"),
    TRADER_LLAMA_GRAY(  "Gray Trader Llama", "YmU0ZDhhMGJjMTVmMjM5OTIxZWZkOGJlMzQ4MGJhNzdhOThlZTdkOWNlMDA3MjhjMGQ3MzNmMGEyZDYxNGQxNi"),

    //Pandas

    PANDA_AGGRESSIVE("Aggressive Panda", "ZTU0NmU0MzZkMTY2YjE3ZjA1MjFiZDg1MzhlYTEzY2Q2ZWUzYjVkZjEwMmViMzJlM2U0MjVjYjI4NWQ0NDA2My"),
    PANDA_LAZY("Lazy Panda", "NTg3ZjFmNWRiMmUyNGRmNGRhYWVkNDY4NWQ2YWVlNWRlYjdjZGQwMjk2MzBmMDA3OWMxZjhlMWY5NzQxYWNmZC"),
    PANDA_PLAYFUL("Playful Panda", "OGNhZGQ0YmYzYzRjYWNlOTE2NjgwZTFmZWY5MGI1ZDE2YWQ2NjQzOTUxNzI1NjY4YmE2YjQ5OTZiNjljYTE0MC"),
    PANDA_WORRIED("Worried Panda", "ZmI4NmZkMWJmOGNiY2UyM2JjMDhmYjkwNjkxNzE3NjExYWRkYzg1YWI4MjNiNzcxNGFlYzk4YTU2NjBlZmYxNS"),
    PANDA_BROWN("Brown Panda", "MWQ1ZjZkNjEyNjcyODY3MWI0NGMxYzc3NWY5OTYxNzQyNGUzMzYxMWI1ZDMxYWQyYWNmZjI4MDRlYjk2ZWIwNi"),
    PANDA_WEAK("Weak Panda", "Y2M1NmEzNTVmYmUwZTJmYmQyOGU4NWM0ZDgxNWZmYTVkMWY5ZDVmODc5OGRiYzI1OWZmODhjNGFkZGIyMDJhZS"),
    PANDA_NORMAL("Panda", "NTlkZjQ3ZTAxNWQ1YzFjNjhkNzJiZTExYmI2NTYzODBmYzZkYjUzM2FhYjM4OTQxYTkxYjFkM2Q1ZTM5NjQ5Ny"),

    //Parrots

    PARROT_RED("Red Parrot", "NDBhM2Q0N2Y1NGU3MWE1OGJmOGY1N2M1MjUzZmIyZDIxM2Y0ZjU1YmI3OTM0YTE5MTA0YmZiOTRlZGM3NmVhYS"),
    PARROT_BLUE("Blue Parrot", "Yjk0YmQzZmNmNGQ0NjM1NGVkZThmZWY3MzEyNmRiY2FiNTJiMzAxYTFjOGMyM2I2Y2RmYzEyZDYxMmI2MWJlYS"),
    PARROT_GREEN("Green Parrot", "NmExZGMzMzExNTIzMmY4MDA4MjVjYWM5ZTNkOWVkMDNmYzE4YWU1NTNjMjViODA1OTUxMzAwMGM1OWUzNTRmZS"),
    PARROT_LIGHT_BLUE("Light Blue Parrot", "NzI2OGNlMzdiZTg1MDdlZDY3ZTNkNDBiNjE3ZTJkNzJmNjZmOWQyMGIxMDZlZmIwOGU2YmEwNDFmOWI5ZWYxMC"),
    PARROT_GRAY("Gray Parrot", "NzFiZTcyM2FhMTczOTNkOTlkYWRkYzExOWM5OGIyYzc5YzU0YjM1ZGViZTA1YzcxMzhlZGViOGQwMjU2ZGM0Ni"),

    //Rabbits

    RABBIT_TOAST("Toast", "NTFhNTdjM2QwYTliMTBlMTNmNjZkZjc0MjAwY2I4YTZkNDg0YzY3MjIyNjgxMmQ3NGUyNWY2YzAyNzQxMDYxNi"),
    RABBIT_BROWN("Brown Rabbit", "Y2ZkNGY4NmNmNzQ3M2ZiYWU5M2IxZTA5MDQ4OWI2NGMwYmUxMjZjN2JiMTZmZmM4OGMwMDI0NDdkNWM3Mjc5NS"),
    RABBIT_WHITE("White Rabbit", "OTU0MmQ3MTYwOTg3MTQ4YTVkOGUyMGU0NjliZDliM2MyYTM5NDZjN2ZiNTkyM2Y1NWI5YmVhZTk5MTg1Zi"),
    RABBIT_BLACK("Black Rabbit", "YjJiNDI1ZmYyYTIzNmFiMTljYzkzOTcxOTVkYjQwZjhmMTg1YjE5MWM0MGJmNDRiMjZlOTVlYWM5ZmI1ZWZhMy"),
    RABBIT_BLACK_WHITE("Black and White Rabbit", "MzVmNzJhMjE5NWViZjQxMTdjNTA1NmNmZTJiNzM1N2VjNWJmODMyZWRlMTg1NmE3NzczZWU0MmEwZDBmYjNmMC"),
    RABBIT_GOLD("Gold Rabbit", "NzY3YjcyMjY1NmZkZWVjMzk5NzRkMzM5NWM1ZTE4YjQ3YzVlMjM3YmNlNWJiY2VkOWI3NTUzYWExNGI1NDU4Ny"),
    RABBIT_SALT_PEPPER("Salt and Pepper Rabbit", "OTIzODUxOWZmMzk4MTViMTZjNDA2MjgyM2U0MzE2MWZmYWFjOTY4OTRmZTA4OGIwMThlNmEyNGMyNmUxODFlYy"),
    RABBIT_KILLER("The Killer Rabbit", "NzFkZDc2NzkyOWVmMmZkMmQ0M2U4NmU4NzQ0YzRiMGQ4MTA4NTM0NzEyMDFmMmRmYTE4Zjk2YTY3ZGU1NmUyZi"),

    //Villagers
    VILLAGER_ARMORER("Armorer Villager", "MWVmNjI3ZjU2NmFjMGE3ODI4YmFkOTNlOWU0Yjk2NDNkOTlhOTI4YTEzZDVmOTc3YmY0NDFlNDBkYjEzMzZiZi"),
    VILLAGER_BUTCHER("Butcher Villager", "YTFiYWQ2NDE4NWUwNGJmMWRhZmUzZGE4NDkzM2QwMjU0NWVhNGE2MzIyMWExMGQwZjA3NzU5MTc5MTEyYmRjMi"),
    VILLAGER_CARTOGRAPHER("Cartographer Villager", "ZTNhZWNmYmU4MDFjZjMyYjVkMWIwYjFmNjY4MDA0OTY2NjE1ODY3OGM1M2Y0YTY1MWZjODNlMGRmOWQzNzM4Yi"),
    VILLAGER_CLERIC("Cleric Villager", "NWI5ZTU4MmUyZjliODlkNTU2ZTc5YzQ2OTdmNzA2YjFkZDQ5MjllY2FlM2MwN2VlOTBiZjFkNWJlMzE5YmY2Zi"),
    VILLAGER_FARMER("Farmer Villager", "ZDkyNzJkMDNjZGE2MjkwZTRkOTI1YTdlODUwYTc0NWU3MTFmZTU3NjBmNmYwNmY5M2Q5MmI4ZjhjNzM5ZGIwNy"),
    VILLAGER_FISHERMAN("Fisherman Villager", "ZDE4OWZiNGFjZDE1ZDczZmYyYTU4YTg4ZGYwNDY2YWQ5ZjRjMTU0YTIwMDhlNWM2MjY1ZDVjMmYwN2QzOTM3Ni"),
    VILLAGER_FLETCHER("Fletcher Villager", "YmY2MTFmMTJlMThjZTQ0YTU3MjM4ZWVmMWNhZTAzY2Q5ZjczMGE3YTQ1ZTBlYzI0OGYxNGNlODRlOWM0ODA1Ni"),
    VILLAGER_LEATHERWORKER("Leatherworker Villager", "YWUwZTk1OTFlMTFhYWVmNGMyYzUxZDlhYzY5NTE0ZTM0MDQ4NWRlZmNjMmMxMmMzOGNkMTIzODZjMmVjNmI3OC"),
    VILLAGER_LIBRARIAN("Librarian Villager", "Y2RjYWE1NzRiYWJiNDBlZTBmYTgzZjJmZDVlYTIwY2ZmMzFmZmEyNzJmZTExMzU4OGNlZWU0Njk2ODIxMjhlNy"),
    VILLAGER_MASON("Mason Villager", "YWUwZTk1OTFlMTFhYWVmNGMyYzUxZDlhYzY5NTE0ZTM0MDQ4NWRlZmNjMmMxMmMzOGNkMTIzODZjMmVjNmI3OC"),
    VILLAGER_NITWIT("Nitwit Villager", "YWUwZTk1OTFlMTFhYWVmNGMyYzUxZDlhYzY5NTE0ZTM0MDQ4NWRlZmNjMmMxMmMzOGNkMTIzODZjMmVjNmI3OC"),
    VILLAGER("Villager", "YWUwZTk1OTFlMTFhYWVmNGMyYzUxZDlhYzY5NTE0ZTM0MDQ4NWRlZmNjMmMxMmMzOGNkMTIzODZjMmVjNmI3OC"),
    VILLAGER_SHEPHERD("Shepherd Villager", "MmFiZjRlOTE1NGFjOTI3MTk0MWM3MzNlYWNjNjJkYzlmYzBhNmRjMWI1ZDY3Yzc4Y2E5OGFmYjVjYjFiZTliMi"),
    VILLAGER_TOOLSMITH("Toolsmith Villager", "YWUwZTk1OTFlMTFhYWVmNGMyYzUxZDlhYzY5NTE0ZTM0MDQ4NWRlZmNjMmMxMmMzOGNkMTIzODZjMmVjNmI3OC"),
    VILLAGER_WEAPONSMITH("Weaponsmith Villager", "ODQ3NmZmYTQxMGJiZTdmYTcwOTA5OTY1YTEyNWY0YTRlOWE0ZmIxY2UxYjhiM2MzNGJmYjczYWFmZmQ0Y2U0My"),

    //Zombie Villagers

    ZOMBIE_VILLAGER_ARMORER("Zombie Armorer", "Yzg2NzllMDM0NzY3ZDUxODY2MGQ5NDE2ZGM1ZWFmMzE5ZDY5NzY4MmFjNDBjODg2ZTNjMmJjOGRmYTFkZTFkIn19fQ"),
    ZOMBIE_VILLAGER_BUTCHER("Zombie Butcher", "OWNjZThkNmNlNDEyNGNlYzNlODRhODUyZTcwZjUwMjkzZjI0NGRkYzllZTg1NzhmN2Q2ZDg5MjllMTZiYWQ2OS"),
    ZOMBIE_VILLAGER_CARTOGRAPHER("Zombie Cartographer", "ZTYwODAwYjAxMDEyZTk2M2U3YzIwYzhiYTE0YjcwYTAyNjRkMTQ2YTg1MGRlZmZiY2E3YmZlNTEyZjRjYjIzZC"),
    ZOMBIE_VILLAGER_CLERIC("Zombie Cleric", "Mjk1ODU3OGJlMGUxMjE3MjczNGE3ODI0MmRhYjE0OTY0YWJjODVhYjliNTk2MzYxZjdjNWRhZjhmMTRhMGZlYi"),
    ZOMBIE_VILLAGER_FARMER("Zombie Farmer", "Zjc3ZDQxNWY5YmFhNGZhNGI1ZTA1OGY1YjgxYmY3ZjAwM2IwYTJjOTBhNDgzMWU1M2E3ZGJjMDk4NDFjNTUxMS"),
    ZOMBIE_VILLAGER_FISHERMAN("Zombie Fisherman", "NjkwNWQ1M2ZlNGZhZWIwYjMxNWE2ODc4YzlhYjgxYjRiZTUyYzMxY2Q0NzhjMDI3ZjBkN2VjZTlmNmRhODkxNC"),
    ZOMBIE_VILLAGER_FLETCHER("Zombie Fletcher", "MmVhMjZhYzBlMjU0OThhZGFkYTRlY2VhNThiYjRlNzZkYTMyZDVjYTJkZTMwN2VmZTVlNDIxOGZiN2M1ZWY4OS"),
    ZOMBIE_VILLAGER_LEATHERWORKER("Zombie Leatherworker", "ZmI1NTJjOTBmMjEyZTg1NWQxMjI1NWQ1Y2Q2MmVkMzhiOWNkN2UzMGU3M2YwZWE3NzlkMTc2NDMzMGU2OTI2NC"),
    ZOMBIE_VILLAGER_LIBRARIAN("Zombie Librarian", "NjIyMTFhMWY0MDljY2E0MjQ5YzcwZDIwY2E4MDM5OWZhNDg0NGVhNDE3NDU4YmU5ODhjYzIxZWI0Nzk3Mzc1ZS"),
    ZOMBIE_VILLAGER_MASON("Zombie Mason", "ZmI1NTJjOTBmMjEyZTg1NWQxMjI1NWQ1Y2Q2MmVkMzhiOWNkN2UzMGU3M2YwZWE3NzlkMTc2NDMzMGU2OTI2NC"),
    ZOMBIE_VILLAGER_NITWIT("Zombie Nitwit", "ZmI1NTJjOTBmMjEyZTg1NWQxMjI1NWQ1Y2Q2MmVkMzhiOWNkN2UzMGU3M2YwZWE3NzlkMTc2NDMzMGU2OTI2NC"),
    ZOMBIE_VILLAGER("Zombie Villager", "ZmI1NTJjOTBmMjEyZTg1NWQxMjI1NWQ1Y2Q2MmVkMzhiOWNkN2UzMGU3M2YwZWE3NzlkMTc2NDMzMGU2OTI2NC"),
    ZOMBIE_VILLAGER_SHEPHERD("Zombie Shepherd", "NjkxMzkxYmVmM2E0NmVmMjY3ZDNiNzE3MTA4NmJhNGM4ZDE3ZjJhNmIwZjgzZmEyYWMzMGVmZTkxNGI3YzI0OS"),
    ZOMBIE_VILLAGER_TOOLSMITH("Zombie Toolsmith", "ZmI1NTJjOTBmMjEyZTg1NWQxMjI1NWQ1Y2Q2MmVkMzhiOWNkN2UzMGU3M2YwZWE3NzlkMTc2NDMzMGU2OTI2NC"),
    ZOMBIE_VILLAGER_WEAPONSMITH("Zombie Weaponsmith", "NDM3MDg5NGI1Y2MzMDVkODdhYTA4YzNiNGIwODU4N2RiNjhmZjI5ZTdhM2VmMzU0Y2FkNmFiY2E1MGU1NTI4Yi"),

    //Defaults
    ZOMBIE("Zombie", ""),
    SKELETON("Skeleton", ""),
    CREEPER("Creeper", ""),
    WITHER_SKELETON("Wither Skeleton", ""),
    ENDER_DRAGON("Ender Dragon", ""),
    PIGLIN("Piglin", ""),
    PLAYER("Player", "");


    private final String name;
    private final String encodedTexture;

    EntitySkullData(String name, String encodedTexture) {
        this.name = name;
        this.encodedTexture = encodedTexture;
    }

    public static EntitySkullData fromEntity(@NotNull Entity entity) {
        switch (entity.getType()) {
            case SKELETON, WITHER_SKELETON, ZOMBIE, ENDER_DRAGON, PIGLIN, PIG, COW, CHICKEN, OCELOT, DONKEY, MULE,
                 POLAR_BEAR, VILLAGER, WANDERING_TRADER, DOLPHIN, PUFFERFISH, TROPICAL_FISH, COD, SALMON, SQUID, BAT,
                 BLAZE, CAVE_SPIDER, DROWNED, ELDER_GUARDIAN, ENDERMITE, EVOKER, GHAST, GUARDIAN, HUSK, ILLUSIONER,
                 MAGMA_CUBE, PHANTOM, PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON_HORSE, SLIME, SPIDER, STRAY, VEX,
                 VINDICATOR, WITCH, WITHER, ZOMBIE_HORSE, ZOMBIFIED_PIGLIN, ZOGLIN, ALLAY, HOGLIN, PIGLIN_BRUTE, BOGGED,
                 BREEZE, CAMEL, ARMADILLO, ENDERMAN, GLOW_SQUID, IRON_GOLEM, SNIFFER, SNOW_GOLEM, TADPOLE, TURTLE,
                 WARDEN -> {
                return valueOf(entity.getType().name());
            }
            case EntityType.STRIDER -> {
                return ((Strider) entity).isShivering() ? STRIDER_COLD : STRIDER;
            }

            case FOX -> {
                return ((Fox) entity).getFoxType() == Fox.Type.SNOW ? SNOW_FOX : FOX;
            }
            case MOOSHROOM -> {
                return ((MushroomCow) entity).getVariant() == MushroomCow.Variant.BROWN ? MOOSHROOM_BROWN : MOOSHROOM;
            }
            case GOAT -> {
                return ((Goat) entity).isScreaming() ? GOAT_SCREAMING : GOAT;
            }
            case CREEPER -> {
                return ((Creeper) entity).isPowered() ? CREEPER_CHARGED : CREEPER;
            }
            case AXOLOTL -> {
                switch (((Axolotl) entity).getVariant()) {
                    case LUCY -> {
                        return AXOLOTL_LUCY;
                    }
                    case WILD -> {
                        return AXOLOTL_WILD;
                    }
                    case GOLD -> {
                        return AXOLOTL_GOLD;
                    }
                    case CYAN -> {
                        return AXOLOTL_CYAN;
                    }
                    case BLUE -> {
                        return AXOLOTL_BLUE;
                    }
                }
            }
            case BEE -> {
                Bee bee = (Bee) entity;
                if(bee.hasNectar() && bee.isAggressive()) return BEE_ANGRY_POLLINATED;
                if(bee.hasNectar()) return BEE_POLLINATED;
                if(bee.isAggressive()) return BEE_ANGRY;
                return BEE_NORMAL;

            }
            case PANDA -> {

            }
            case PARROT -> {
                switch (((Parrot) entity).getVariant()) {
                    case RED -> {
                        return PARROT_RED;
                    }
                    case BLUE -> {
                        return PARROT_BLUE;
                    }
                    case GREEN -> {
                        return PARROT_GREEN;
                    }
                    case CYAN -> {
                        return PARROT_LIGHT_BLUE;
                    }
                    case GRAY -> {
                        return PARROT_GRAY;
                    }
                }

            }
            case TRADER_LLAMA -> {
                switch (((TraderLlama) entity).getColor()) {
                    case CREAMY -> {
                        return TRADER_LLAMA_CREAMY;
                    }
                    case WHITE -> {
                        return TRADER_LLAMA_WHITE;
                    }
                    case BROWN -> {
                        return TRADER_LLAMA_BROWN;
                    }
                    case GRAY -> {
                        return TRADER_LLAMA_GRAY;
                    }
                }
            }
            case LLAMA -> {
                switch (((Llama) entity).getColor()) {
                    case CREAMY -> {
                        return LLAMA_CREAMY;
                    }
                    case WHITE -> {
                        return LLAMA_WHITE;
                    }
                    case BROWN -> {
                        return LLAMA_BROWN;
                    }
                    case GRAY -> {
                        return LLAMA_GRAY;
                    }
                }
            }
            case HORSE -> {
                switch (((Horse) entity).getColor()) {
                    case WHITE -> {
                        return HORSE_WHITE;
                    }
                    case CREAMY -> {
                        return HORSE_CREAMY;
                    }
                    case CHESTNUT -> {
                        return HORSE_CHESTNUT;
                    }
                    case BROWN -> {
                        return HORSE_BROWN;
                    }
                    case BLACK -> {
                        return HORSE_BLACK;
                    }
                    case GRAY -> {
                        return HORSE_GRAY;
                    }
                    case DARK_BROWN -> {
                        return HORSE_DARK_BROWN;
                    }
                }
            }
            case CAT -> {
                Cat cat = (Cat) entity;
                Cat.Type type = cat.getCatType();
                if(type.equals(TABBY)) return CAT_TABBY;
                if(type.equals(BLACK)) return CAT_TUXEDO;
                if(type.equals(RED)) return CAT_GINGER;
                if(type.equals(SIAMESE)) return CAT_SIAMESE;
                if(type.equals(BRITISH_SHORTHAIR)) return CAT_BRITISH;
                if(type.equals(CALICO)) return CAT_CALICO;
                if(type.equals(PERSIAN)) return CAT_PERSIAN;
                if(type.equals(RAGDOLL)) return CAT_RAGDOLL;
                if(type.equals(WHITE)) return CAT_WHITE;
                if(type.equals(JELLIE)) return CAT_JELLIE;
                if(type.equals(ALL_BLACK)) return CAT_BLACK;
                return null;

            }
            case WOLF -> {
                Wolf wolf = (Wolf) entity;
                Wolf.Variant variant = wolf.getVariant();
                boolean angry = wolf.isAngry();
                if(variant.equals(Wolf.Variant.BLACK)) return angry ? WOLF_BLACK_ANGRY : WOLF_BLACK;
                if(variant.equals(Wolf.Variant.CHESTNUT)) return angry ? WOLF_CHESTNUT_ANGRY : WOLF_CHESTNUT;
                if(variant.equals(Wolf.Variant.ASHEN)) return angry ? WOLF_ASHEN_ANGRY : WOLF_ASHEN;
                if(variant.equals(Wolf.Variant.PALE)) return angry ? WOLF_PALE_ANGRY : WOLF_PALE;
                if(variant.equals(Wolf.Variant.RUSTY)) return angry ? WOLF_RUSTY_ANGRY : WOLF_RUSTY;
                if(variant.equals(Wolf.Variant.SPOTTED)) return angry ? WOLF_SPOTTY_ANGRY : WOLF_SPOTTY;
                if(variant.equals(Wolf.Variant.SNOWY)) return angry ? WOLF_SNOWY_ANGRY : WOLF_SNOWY;
                if(variant.equals(Wolf.Variant.STRIPED)) return angry ? WOLF_STRIPED_ANGRY : WOLF_STRIPED;
                if(variant.equals(Wolf.Variant.WOODS)) return angry ? WOLF_WOODS_ANGRY : WOLF_WOODS;
                return null;
            }
            case RABBIT -> {
                Rabbit rabbit = (Rabbit) entity;
                if(MessageUtils.plainText(rabbit.name()).equals("toast")) return RABBIT_TOAST;
                Rabbit.Type type = rabbit.getRabbitType();
                if(type.equals(Rabbit.Type.THE_KILLER_BUNNY)) return RABBIT_KILLER;
                if(type.equals(Rabbit.Type.BLACK)) return RABBIT_BLACK;
                if(type.equals(Rabbit.Type.BLACK_AND_WHITE)) return RABBIT_BLACK_WHITE;
                if(type.equals(Rabbit.Type.BROWN)) return RABBIT_BROWN;
                if(type.equals(Rabbit.Type.GOLD)) return RABBIT_GOLD;
                if(type.equals(Rabbit.Type.SALT_AND_PEPPER)) return RABBIT_SALT_PEPPER;
                if(type.equals(Rabbit.Type.WHITE)) return RABBIT_WHITE;
                return null;

            }
            case SHEEP -> {
                Sheep sheep = (Sheep) entity;
                if (MessageUtils.plainText(sheep.name()).equals("jeb_")) return SHEEP_RAINBOW;
                DyeColor color = sheep.getColor();
                switch(color){
                    case WHITE: return SHEEP_WHITE;
                    case ORANGE: return SHEEP_ORANGE;
                    case MAGENTA: return SHEEP_MAGENTA;
                    case LIGHT_BLUE: return SHEEP_LIGHT_BLUE;
                    case YELLOW: return SHEEP_YELLOW;
                    case LIME: return SHEEP_LIME;
                    case PINK: return SHEEP_PINK;
                    case GRAY: return SHEEP_GRAY;
                    case LIGHT_GRAY: return SHEEP_LIGHT_GRAY;
                    case CYAN: return SHEEP_CYAN;
                    case PURPLE: return SHEEP_PURPLE;
                    case BLUE: return SHEEP_BLUE;
                    case BROWN: return SHEEP_BROWN;
                    case GREEN: return SHEEP_GREEN;
                    case RED: return SHEEP_RED;
                    case BLACK: return SHEEP_BLACK;
                    default: return SHEEP_WHITE;
                }
            }
            case ZOMBIE_VILLAGER -> {
                ZombieVillager zombieVillager = (ZombieVillager) entity;
                Villager.Profession profession = zombieVillager.getVillagerProfession();
                if(profession == null) return ZOMBIE_VILLAGER;
                if(profession.equals(Villager.Profession.ARMORER)) return ZOMBIE_VILLAGER_ARMORER;
                if(profession.equals(Villager.Profession.BUTCHER)) return ZOMBIE_VILLAGER_BUTCHER;
                if(profession.equals(Villager.Profession.CARTOGRAPHER)) return ZOMBIE_VILLAGER_CARTOGRAPHER;
                if(profession.equals(Villager.Profession.CLERIC)) return ZOMBIE_VILLAGER_CLERIC;
                if(profession.equals(Villager.Profession.FARMER)) return ZOMBIE_VILLAGER_FARMER;
                if(profession.equals(Villager.Profession.FISHERMAN)) return ZOMBIE_VILLAGER_FISHERMAN;
                if(profession.equals(Villager.Profession.FLETCHER)) return ZOMBIE_VILLAGER_FLETCHER;
                if(profession.equals(Villager.Profession.LEATHERWORKER)) return ZOMBIE_VILLAGER_LEATHERWORKER;
                if(profession.equals(Villager.Profession.LIBRARIAN)) return ZOMBIE_VILLAGER_LIBRARIAN;
                if(profession.equals(Villager.Profession.MASON)) return ZOMBIE_VILLAGER_MASON;
                if(profession.equals(Villager.Profession.NITWIT)) return ZOMBIE_VILLAGER_NITWIT;
                if(profession.equals(Villager.Profession.SHEPHERD)) return ZOMBIE_VILLAGER_SHEPHERD;
                if(profession.equals(Villager.Profession.TOOLSMITH)) return ZOMBIE_VILLAGER_TOOLSMITH;
                if(profession.equals(Villager.Profession.WEAPONSMITH)) return ZOMBIE_VILLAGER_WEAPONSMITH;
                return ZOMBIE_VILLAGER;

            }
            default -> {
                return null;
            }

        }
        return null;
    }

    public String itemName() {
        return name;
    }

    public String texture() {
        String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
        String suffix = "J9fX0=";
        return prefix + encodedTexture + suffix;
    }
}
