package mechanics;

import dangerzone.InventoryContainer;
import dangerzone.entities.Entity;

public interface ItemSupplier
{
	/**
	 * Retrieves a new InventoryContainer containing zero or one block or item
	 * from the Item Supplier.
	 * 
	 * This method should not return a null pointer. If you want to signal that
	 * there is no block or item supplied, return an empty InventoryContainer.
	 * The block or item inside the InventoryContainer can be whatever you want
	 * to implement.
	 * 
	 * @param other
	 *            The Entity calling this method
	 * @param power
	 *            The amount of carrying power left
	 * @return A new InventoryContainer holding the item
	 **/
	public InventoryContainer getItem(Entity other, int power);

	/**
	 * Tests whether a filled InventoryContainer can be retrieved from the Item
	 * Supplier Entity.
	 * 
	 * This method should always return false if calling getItem would return an
	 * empty container. This method should, but is not required to, return true
	 * if calling getItem would return a filled container.
	 * 
	 * @param other
	 *            The Entity calling this method
	 * @param power
	 *            The amount of carrying power left
	 * @return Whether the Entity will deliver a filled InventoryContainer if
	 *         get() is called right now.
	 **/
	public boolean hasItem(Entity other, int power);
}
