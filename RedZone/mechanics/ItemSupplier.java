package mechanics;

import dangerzone.InventoryContainer;
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
/*/

/**
 * Certain components supply items and blocks to other components.
 * 
 * Internally, these components must draw off surrounding ItemSupplier entities.
 * Item suppliers may release, generate, or transfer items, depending on what
 * type of item supplier they are.
 * <p>
 * If you wish to implement this interface, be aware of these details:
 * <ul>
 * <li>Please, please follow the invariants of Inventory container.
 * <li>In particular, please don't return null pointers.
 * <li>Also, please return zero or one item or block per container. Components
 * that draw from ItemSuppliers will expect this to be true.
 * <li>Double check to make sure that hasItem is consistent with getItem.
 * <li>When you recursively call either method, please remember to deduct power
 * by 1 or more. You might cause infinite recursion otherwise.
 * </ul>
 * 
 * @author eaglgenes101
 * @see Pipe
 * @see Dispenser
 * @see Dropper
 */

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
	 */
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
	 */
	public boolean hasItem(Entity other, int power);
}
