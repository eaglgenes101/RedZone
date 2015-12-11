import items.RedZoneItems;
import blocks.RedZoneBlocks;
import dangerzone.BaseMod;
import dangerzone.Crafting;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entities;
import dangerzone.items.Items;
import entities.EntityCornerPipe;
import entities.EntityDispenser;
import entities.EntityDropper;
import entities.EntityFiveWayPipe;
import entities.EntityParticleStreamer;
import entities.EntityPusherCornerPipe;
import entities.EntityPusherFiveWayPipe;
import entities.EntityPusherStraightPipe;
import entities.EntityStraightPipe;

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
 * Mod starter class. 
 * 
/*/

public class RedZoneMain extends BaseMod
{
	public RedZoneMain (){
		super();
	}
	
	@Override
	public String getModName()
	{
		return "RedZone v0.2.3";
	}
	
	@Override
	public String versionBuiltWith()
	{
		return "0.8";
	}
	
	public void registerThings()
	{
		Blocks.registerBlock(RedZoneBlocks.STRAIGHT_WIRE);
		Blocks.registerBlock(RedZoneBlocks.BENT_WIRE);
		Blocks.registerBlock(RedZoneBlocks.JUNCTION_WIRE);
		Blocks.registerBlock(RedZoneBlocks.STRAIGHT_WIRE_ACTIVE);
		Blocks.registerBlock(RedZoneBlocks.BENT_WIRE_ACTIVE);
		Blocks.registerBlock(RedZoneBlocks.JUNCTION_WIRE_ACTIVE);
		
		Blocks.registerBlock(RedZoneBlocks.POWER_STICK);
		Blocks.registerBlock(RedZoneBlocks.POWER_STICK_INACTIVE);
		
		Blocks.registerBlock(RedZoneBlocks.SMOKE_TESTER);
		Blocks.registerBlock(RedZoneBlocks.DISPENSER);
		Blocks.registerBlock(RedZoneBlocks.PRESS_SWITCH);
		Blocks.registerBlock(RedZoneBlocks.PRESS_SWITCH_ACTIVE);
		Blocks.registerBlock(RedZoneBlocks.DETECTOR_SWITCH);
		Blocks.registerBlock(RedZoneBlocks.DROPPER);
		Blocks.registerBlock(RedZoneBlocks.PARTICLE_STREAMER);
		
		Blocks.registerBlock(RedZoneBlocks.STRAIGHT_PIPE);
		Blocks.registerBlock(RedZoneBlocks.PUSHER_STRAIGHT_PIPE);
		Blocks.registerBlock(RedZoneBlocks.CORNER_PIPE);
		Blocks.registerBlock(RedZoneBlocks.PUSHER_CORNER_PIPE);
		Blocks.registerBlock(RedZoneBlocks.FIVE_WAY_PIPE);
		Blocks.registerBlock(RedZoneBlocks.PUSHER_FIVE_WAY_PIPE);
		
		Items.registerItem(RedZoneItems.STRAIGHT_WIRE_ITEM);
		Items.registerItem(RedZoneItems.BENT_WIRE_ITEM);
		Items.registerItem(RedZoneItems.JUNCTION_WIRE_ITEM);
		Items.registerItem(RedZoneItems.PRESS_SWITCH_ITEM);
		
		Entities.registerEntity(EntityDispenser.class, "RedZone:EntityDispenser", null);
		Entities.registerEntity(EntityStraightPipe.class, "RedZone:EntityStraightPipe", null);
		Entities.registerEntity(EntityPusherStraightPipe.class, "RedZone:EntityPusherStraightPipe", null);
		Entities.registerEntity(EntityCornerPipe.class, "RedZone:EntityCornerPipe", null);
		Entities.registerEntity(EntityPusherCornerPipe.class, "RedZone:EntityPusherCornerPipe", null);
		Entities.registerEntity(EntityFiveWayPipe.class, "RedZone:EntityFiveWayPipe", null);
		Entities.registerEntity(EntityPusherFiveWayPipe.class, "RedZone:EntityPusherFiveWayPipe", null);
		Entities.registerEntity(EntityDropper.class, "RedZone:EntityDropper", null);
		Entities.registerEntity(EntityParticleStreamer.class, "RedZoen:EntityParticleStreamer", null);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				null, null, null, null, null, null, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, null, null, null, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, null, null, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, null, 
				Items.lumpcopper, null, null, Items.lumpcopper, null, null, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null,
				null, Items.lumpcopper, null, null, Items.lumpcopper, null, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, null, Items.lumpcopper, 
				null, null, Items.lumpcopper, null, null, Items.lumpcopper, RedZoneItems.STRAIGHT_WIRE_ITEM, 6, true);
		
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null,
				Items.lumpcopper, Items.lumpcopper, null, null, null, null, RedZoneItems.BENT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				null, Items.lumpcopper, Items.lumpcopper, null, null, null, RedZoneItems.BENT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				Items.lumpcopper, Items.lumpcopper, null, null, Items.lumpcopper, null, RedZoneItems.BENT_WIRE_ITEM, 6, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, null, RedZoneItems.BENT_WIRE_ITEM, 6, true);
		
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				null, Items.lumpcopper, null, RedZoneItems.JUNCTION_WIRE_ITEM, 10, true);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, null, 
				Items.stick, null, null, null, null, null, RedZoneBlocks.POWER_STICK, 4, true);
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				null, Items.stick, null, null, null, null, RedZoneBlocks.POWER_STICK, 4, true);
		Crafting.registerCraftingRecipe(null, null, Items.lumpcopper, 
				null, null, Items.stick, null, null, null, RedZoneBlocks.POWER_STICK, 4, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				Items.lumpcopper, null, null, Items.stick, null, null, RedZoneBlocks.POWER_STICK, 4, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, Items.lumpcopper, null, null, Items.stick, null, RedZoneBlocks.POWER_STICK, 4, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, null, Items.lumpcopper, null, null, Items.stick, RedZoneBlocks.POWER_STICK, 4, true);
		
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, 
				Blocks.stone, Items.firestick, Blocks.stone, 
				Blocks.stone, Blocks.stone, Blocks.stone, RedZoneBlocks.SMOKE_TESTER, 1, true);
		
		Crafting.registerCraftingRecipe(null, Blocks.stone, Blocks.stone, 
				null, null, null, null, null, null, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, null, 
				null, null, null, null, null, null, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				Blocks.stone, Blocks.stone, null, null, null, null, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, Blocks.stone, Blocks.stone, null, null, null, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, null, null, Blocks.stone, Blocks.stone, null, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		Crafting.registerCraftingRecipe(null, null, null, 
				null, null, null, null, Blocks.stone, Blocks.stone, RedZoneItems.PRESS_SWITCH_ITEM, 2, true);
		
		Crafting.registerCraftingRecipe(Items.lumptin, RedZoneItems.PRESS_SWITCH_ITEM, Items.lumptin, 
				RedZoneItems.PRESS_SWITCH_ITEM, null, RedZoneItems.PRESS_SWITCH_ITEM, 
				Items.lumptin, RedZoneItems.PRESS_SWITCH_ITEM, Items.lumptin, RedZoneBlocks.DETECTOR_SWITCH, 1, true);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				null, null, null, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, RedZoneBlocks.STRAIGHT_PIPE, 3, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				Items.lumpcopper, null, Items.lumpcopper, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.STRAIGHT_PIPE, 3, true);
		
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				Items.lumptin, RedZoneBlocks.STRAIGHT_PIPE, Items.lumptin, 
				null, Items.lumpcopper, null, RedZoneBlocks.PUSHER_STRAIGHT_PIPE, 1, true);
		Crafting.registerCraftingRecipe(null, Items.lumptin, null, 
				Items.lumpcopper, RedZoneBlocks.STRAIGHT_PIPE, Items.lumpcopper, 
				null, Items.lumptin, null, RedZoneBlocks.PUSHER_STRAIGHT_PIPE, 1, true);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				Items.lumpcopper, null, null, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.CORNER_PIPE, 3, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				null, null, Items.lumpcopper, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.CORNER_PIPE, 3, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				null, null, Items.lumpcopper, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, RedZoneBlocks.CORNER_PIPE, 3, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				Items.lumpcopper, null, null, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, RedZoneBlocks.CORNER_PIPE, 3, true);
		
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				Items.lumptin, RedZoneBlocks.CORNER_PIPE, Items.lumptin, 
				null, Items.lumpcopper, null, RedZoneBlocks.PUSHER_CORNER_PIPE, 1, true);
		Crafting.registerCraftingRecipe(null, Items.lumptin, null, 
				Items.lumpcopper, RedZoneBlocks.CORNER_PIPE, Items.lumpcopper, 
				null, Items.lumptin, null, RedZoneBlocks.PUSHER_CORNER_PIPE, 1, true);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, 
				null, null, null, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.FIVE_WAY_PIPE, 2, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				Items.lumpcopper, null, null, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.FIVE_WAY_PIPE, 2, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				null, null, Items.lumpcopper, 
				Items.lumpcopper, null, Items.lumpcopper, RedZoneBlocks.FIVE_WAY_PIPE, 2, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, 
				null, null, null, 
				Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, RedZoneBlocks.FIVE_WAY_PIPE, 2, true);
		
		Crafting.registerCraftingRecipe(null, Items.lumpcopper, null, 
				Items.lumptin, RedZoneBlocks.FIVE_WAY_PIPE, Items.lumptin, 
				null, Items.lumpcopper, null, RedZoneBlocks.PUSHER_FIVE_WAY_PIPE, 1, true);
		Crafting.registerCraftingRecipe(null, Items.lumptin, null, 
				Items.lumpcopper, RedZoneBlocks.FIVE_WAY_PIPE, Items.lumpcopper, 
				null, Items.lumptin, null, RedZoneBlocks.PUSHER_FIVE_WAY_PIPE, 1, true);
		
	}
	
	
}
