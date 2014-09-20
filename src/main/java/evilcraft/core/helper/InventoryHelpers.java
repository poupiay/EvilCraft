package evilcraft.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Contains helper methods involving {@link IInventory}S.
 * @author immortaleeb
 *
 */
public class InventoryHelpers {

	/**
	 * Erase a complete inventory
	 * @param inventory inventory to clear
	 */
	public static void clearInventory(IInventory inventory) {
	    for (int i = 0; i < inventory.getSizeInventory(); i++) {
	        inventory.setInventorySlotContents(i, null);
	    }
	}
	
	/**
	 * Try adding a new item stack originating from the given original stack to the same original stack.
	 * The original item stack should not have it's stack-size decreased yet, this method does this.
	 * Otherwise it will add the new stack to another inventory slot and in the worst case drop it on the floor.
	 * @param player The player.
	 * @param originalStack The original item stack from which the new item stack originated.
	 * @param newStackPart The new item stack.
	 */
	public static void tryReAddToStack(EntityPlayer player, ItemStack originalStack, ItemStack newStackPart) {
		if (!player.capabilities.isCreativeMode) {
        	if(--originalStack.stackSize == 0) {
        		player.inventory.setInventorySlotContents(player.inventory.currentItem, newStackPart);
        	} else {
        		if(!player.inventory.addItemStackToInventory(newStackPart)) {
        			player.dropPlayerItemWithRandomChoice(newStackPart, false);
        		}
        	}
        }
	}
	
	/**
	 * Validate the NBT storage of the given inventory in the given item.
	 * Should be called in constructors of inventories.
	 * @param inventory The inventory.
	 * @param itemStack The item stack to read/write.
	 * @param tagName The tag name to read from.
	 */
	public static void validateNBTStorage(IInventory inventory, ItemStack itemStack, String tagName) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			itemStack.setTagCompound(tag);
		}
		if(!tag.hasKey(tagName)) {
			tag.setTag(tagName, new NBTTagList());
		}
		readFromNBT(inventory, tag, tagName);
	}
	
	/**
	 * Read an inventory from NBT.
	 * @param inventory The inventory.
	 * @param data The tag to read from.
	 * @param tagName The tag name to read from.
	 */
	public static void readFromNBT(IInventory inventory, NBTTagCompound data, String tagName) {
        NBTTagList nbttaglist = data.getTagList(tagName, MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
        
        for(int j = 0; j < inventory.getSizeInventory(); j++) {
        	inventory.setInventorySlotContents(j, null);
        }

        for(int j = 0; j < nbttaglist.tagCount(); j++) {
            NBTTagCompound slot = (NBTTagCompound) nbttaglist.getCompoundTagAt(j);
            int index;
            if(slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getByte("Slot");
            }
            if(index >= 0 && index < inventory.getSizeInventory()) {
            	inventory.setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slot));
            }
        }
    }
	
	/**
	 * Write the given inventory to NBT.
	 * @param inventory The inventory.
	 * @param data The tag to write to.
	 * @param tagName The tag name to write into.
	 */
	public static void writeToNBT(IInventory inventory, NBTTagCompound data, String tagName) {
        NBTTagList slots = new NBTTagList();
        for(byte index = 0; index < inventory.getSizeInventory(); ++index) {
        	ItemStack itemStack = inventory.getStackInSlot(index);
            if(itemStack != null && itemStack.stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slot.setInteger("index", index);
                slots.appendTag(slot);
                itemStack.writeToNBT(slot);
            }
        }
        data.setTag(tagName, slots);
    }

}
