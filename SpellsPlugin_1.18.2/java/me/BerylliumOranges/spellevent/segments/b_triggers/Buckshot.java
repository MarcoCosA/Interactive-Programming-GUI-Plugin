package me.BerylliumOranges.spellevent.segments.b_triggers;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.BerylliumOranges.spellevent.other.Guideline;
import me.BerylliumOranges.spellevent.processes.SegmentProcess;
import me.BerylliumOranges.spellevent.processes.SpellActions;
import me.BerylliumOranges.spellevent.projectiles.SpellProjectile;
import me.BerylliumOranges.spellevent.segments.abstracts.EventTriggerSegment;

public final class Buckshot extends EventTriggerSegment {
	public final static ItemStack ITEM = getUpdatedItem(new int[2], 0);
	public final static String LOCAL_NAME = ITEM.getItemMeta().getLocalizedName();
	public static final double MANA_COST = 10;
	public static final int RARITY = 2;
	public static final int PROJECTILE_COUNT = 6;

	public static final boolean IS_PROJECTILE = true;

	public static Guideline processSegment(SegmentProcess proc) {
		proc.addTimesCloned(PROJECTILE_COUNT);
		if (proc.subtractMana(MANA_COST) < 1)
			return new Guideline(proc, true, true);

		for (int i = 0; i < PROJECTILE_COUNT; i++) {
			try {
				SegmentProcess clone = (SegmentProcess) proc.clone();
				Vector direction = proc.getSpellLocation().getDirection().clone()
						.add(new Vector(Math.sqrt(Math.random()) * (Math.random() > 0.5 ? 1 : -1),
								Math.sqrt(Math.random()) * (Math.random() > 0.5 ? 1 : -1),
								Math.sqrt(Math.random()) * (Math.random() > 0.5 ? 1 : -1)).multiply(0.16));
				clone.getSpellLocation().setDirection(direction);
				SpellProjectile proj = new SpellProjectile(clone, clone.getSpellLocation().clone(), direction);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Guideline(proc, true, true);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getUpdatedItem(int upgrades[], int dir) {
		ItemStack item = new ItemStack(Material.IRON_NUGGET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getColorFromRarity(RARITY) + "Buckshot");
		ArrayList<String> lore = new ArrayList<String>(Arrays.asList(
				ChatColor.WHITE + "Launches " + ChatColor.YELLOW + PROJECTILE_COUNT + ChatColor.GOLD + " Projectiles ",
				"", getCostText(MANA_COST) + " " + ChatColor.YELLOW + SpellActions.getArrowModelFromInt(dir)));
		lore.addAll(getMultiplierLore(MANA_COST_MULTIPLIER));
		meta.setLore(lore);
		meta.setLocalizedName(THIS_SEGMENT_TYPE + "[" + ChatColor.stripColor(meta.getDisplayName()) + "]");
		meta.setCustomModelData(dir + CUSTOM_MODEL_TRIGGER);
		item.setItemMeta(meta);
		return item;
	}
}