package redzone.entities;

import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.entities.Entity;

/*/
 * Copyright 2015 Eugene "eaglgenes101" Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * Base class for item supplier entities. See EntityPipe for an example. 
 * 
 /*/

public abstract class EntityItemSupplier extends Entity
{

	public EntityItemSupplier(World w)
	{
		super(w);
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
	 * @param power The amount of carrying power left
	 * @return A new InventoryContainer holding the item
	 **/
	public abstract InventoryContainer get(Entity other, int power);
	
	/**
	 * Tests whether a filled InventoryContainer can be retrieved from the Item
	 * Supplier Entity. Should be consistent with get(). 
	 * 
	 * @param other The Entity calling this method
	 * @param power The amount of carrying power left
	 * @return Whether the Entity will deliver a filled InventoryContainer if
	 * get() is called right now. 
	 **/
	public abstract boolean hasItem(Entity other, int power);

}
