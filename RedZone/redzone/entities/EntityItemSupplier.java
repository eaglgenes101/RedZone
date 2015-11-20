package redzone.entities;

import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.entities.Entity;

public abstract class EntityItemSupplier extends Entity
{

	public EntityItemSupplier(World w)
	{
		super(w);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieves a new InventoryContainer from the Item Supplier Entity. Should
	 * not return null. If you want to signal that there is no block or item
	 * supplied, return an empty InventoryContainer.
	 * 
	 * The object inside the InventoryContainer can be whatever you want to
	 * implement.
	 * 
	 * @param other The Entity calling this method
	 * @return A new InventoryContainer holding the item
	 **/
	public abstract InventoryContainer get(Entity other);
	
	/**
	 * Tests whether a filled InventoryContainer can be retrieved from the Item
	 * Supplier Entity. Should be consistent with get(). 
	 * 
	 * @param other The Entity calling this method
	 * @return Whether the Entity will deliver a filled InventoryContainer if
	 * get() is called right now. 
	 **/
	public abstract boolean hasItem(Entity other);

}
