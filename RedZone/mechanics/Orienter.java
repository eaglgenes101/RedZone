package mechanics;

import dangerzone.blocks.BlockRotation;

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
 * RedZone uses a quaternion rotation system to efficiently support
 * orientation-dependent behavior.
 * <p>
 * To the uninitiated, Euler angles are easy enough to grasp, and seem like a
 * no-brainer in rotation systems. However, this apparent conceptual simplicity
 * belies the considerable computational complexity of Euler angles. Other
 * rotation systems are less easy to describe, but are much more computationally
 * simple. My choice of rotation system for RedZone is based on quaternions.
 * <p>
 * Internally, DangerZone makes use of a Euler angle system. RedZone was
 * designed to be very orientation-dependent, and trying to sort through the
 * Euler angles would be time-consuming for me, the developer, so I wrote this
 * collection of helper methods to do this hard work for me. Go ahead and use it
 * yourself.
 * 
 * @author eaglgenes101
 */

public class Orienter
{
	public static final double[] X_QUARTER_TURN = {Math.sqrt(2) / 2.0, Math.sqrt(2) / 2.0, 0, 0};
	public static final double[] Y_QUARTER_TURN = {Math.sqrt(2) / 2.0, 0, Math.sqrt(2) / 2.0, 0};
	public static final double[] Z_QUARTER_TURN = {Math.sqrt(2) / 2.0, 0, 0, Math.sqrt(2) / 2.0};

	public static final double[] X_REVERSE_QUARTER_TURN = {Math.sqrt(2) / 2.0, -Math.sqrt(2) / 2.0, 0, 0};
	public static final double[] Y_REVERSE_QUARTER_TURN = {Math.sqrt(2) / 2.0, 0, -Math.sqrt(2) / 2.0, 0};
	public static final double[] Z_REVERSE_QUARTER_TURN = {Math.sqrt(2) / 2.0, 0, 0, -Math.sqrt(2) / 2.0};

	public static final double[] WEST_VECTOR = {1.0, 0.0, 0.0};
	public static final double[] UP_VECTOR = {0.0, 1.0, 0.0};
	public static final double[] NORTH_VECTOR = {0.0, 0.0, 1.0};
	public static final double[] EAST_VECTOR = {-1.0, 0.0, 0.0};
	public static final double[] DOWN_VECTOR = {0.0, -1.0, 0.0};
	public static final double[] SOUTH_VECTOR = {0.0, 0.0, -1.0};

	public static final int[] WEST_COMPARE = {1, 0, 0};
	public static final int[] UP_COMPARE = {0, 1, 0};
	public static final int[] NORTH_COMPARE = {0, 0, 1};
	public static final int[] EAST_COMPARE = {-1, 0, 0};
	public static final int[] DOWN_COMPARE = {0, -1, 0};
	public static final int[] SOUTH_COMPARE = {0, 0, -1};

	public static final int[][] metadataTable =
	//[topdirection][frontdirection]
	//side 0 = up
	//side 1 = north
	//side 2 = south
	//side 3 = west
	//side 4 = east
	//side 5 = down
	{{0, 0xa800, 0x8800, 0x9800, 0xb800, 0}, {0x6000, 0, 0, 0x5000, 0x7000, 0x4000},
			{0x6800, 0, 0, 0x5800, 0x7800, 0x4800}, {0x6c00, 0xac00, 0x2c00, 0, 0, 0xec00},
			{0x6400, 0xa400, 0x2400, 0, 0, 0xe400}, {0, 0x0800, 0x2800, 0x1800, 0x3800, 0}};

	// Because I still have to deal with hardcoded constants...
	public static final int UP = 0;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public static final int EAST = 4;
	public static final int DOWN = 5;

	/**
	 * Returns the product of two quarternions.
	 * 
	 * @param q1
	 *            Quarternion 1
	 * @param q2
	 *            Quarternion 2
	 * @return The quarternion product of the two quarternions
	 */
	public static double[] quartproduct(double[] q1, double[] q2)
	{
		double[] returnQuart = {q1[0] * q2[0] - q1[1] * q2[1] - q1[2] * q2[2] - q1[3] * q2[3], // Real part
				q1[0] * q2[1] + q1[1] * q2[0] + q1[2] * q2[3] - q1[3] * q2[2], // i part
				q1[0] * q2[2] + q1[2] * q2[0] + q1[3] * q2[1] - q1[1] * q2[3], // j part
				q1[0] * q2[3] + q1[3] * q2[0] + q1[1] * q2[2] - q1[2] * q2[1] // k part
		};
		return returnQuart;
	}

	/**
	 * Rotates a vector through a quarternion rotation.
	 * 
	 * @param start
	 *            The vector to be rotated
	 * @param rotate
	 *            The quarternion representation of the rotation
	 * @return The vector result of the rotation
	 */
	public static double[] rotate(double[] start, double[] rotate)
	{
		double[] startQ = {0.0, start[0], start[1], start[2]};
		double[] intermediate = quartproduct(startQ, rotate);
		double[] rotateprime = {rotate[0], -rotate[1], -rotate[2], -rotate[3]};
		double[] quartResult = quartproduct(rotateprime, intermediate);
		double[] returnVector = {quartResult[1], quartResult[2], quartResult[3]};
		return returnVector;
	}

	/**
	 * Returns a block's orientation based on its rotation metadata.
	 * 
	 * @param start
	 *            The block's orientation vector to check
	 * @param meta
	 *            The metadata of the block
	 * @return The vector's actual facing
	 */
	public static double[] getDirection(double[] start, int meta)
	{
		while ((meta & BlockRotation.Z_MASK) > 0)
		{
			start = rotate(start, Z_REVERSE_QUARTER_TURN);
			meta -= BlockRotation.Z_ROT_90;
		}

		while ((meta & BlockRotation.Y_MASK) > 0)
		{
			start = rotate(start, Y_REVERSE_QUARTER_TURN);
			meta -= BlockRotation.Y_ROT_90;
		}

		while ((meta & BlockRotation.X_MASK) > 0)
		{
			start = rotate(start, X_REVERSE_QUARTER_TURN);
			meta -= BlockRotation.X_ROT_90;
		}
		return start;
	}
	
	public static double[] bodyxyzToQuart(double x, double y, double z)
	{
		double[] quartx = {Math.cos(x/2.0), Math.sin(x/2.0), 0.0, 0.0};
		double[] quarty = {Math.cos(y/2.0), 0.0, Math.sin(y/2.0), 0.0};
		double[] quartz = {Math.cos(z/2.0), 0.0, 0.0, Math.sin(z/2.0)};
		return quartproduct(quartx, quartproduct(quarty, quartz));
	}
	
	public static double[] quartToBodyZYX(double[] rot)
	{
		double[] returnVal = new double[3];
		double check = rot[0]*rot[2] - rot[3]*rot[1];
		if (check > 0.499)
		{
			returnVal[0] = 0;
			returnVal[1] = Math.PI/2.0;
			returnVal[2] = 2*Math.atan2(rot[1], rot[0]);
		}
		else if (check < -.499)
		{
			returnVal[0] = 0;
			returnVal[1] = Math.PI/2.0;
			returnVal[2] = -2*Math.atan2(rot[1], rot[0]);
		}
		else
		{
			returnVal[0] = Math.atan2(2*(rot[0]*rot[1] + rot[2]*rot[3]), 1 - 2*(rot[1]*rot[1] + rot[2]*rot[2]));
			returnVal[1] = Math.asin(2*(rot[0]*rot[2] - rot[3]*rot[1]));
			returnVal[2] = Math.atan2(2*(rot[0]*rot[3] + rot[1]*rot[2]), 1 - 2*(rot[2]*rot[2] + rot[3]*rot[3]));
		}
		return returnVal;
	}

	//side 0 = top (0, 1, 0)
	//side 1 = front (0, 0, 1)
	//side 2 = back (0, 0, -1)
	//side 3 = left (-1, 0, 0)
	//side 4 = right (1, 0, 0)
	//side 5 = bottom (0, -1, 0)
	/**
	 * Returns the enumerated side form of a position vector.
	 * 
	 * @param vec
	 *            The position vector
	 * @return The side which the position vector corresponds to
	 */
	public static int getSideForm(int[] vec)
	{
		// -1 means default case
		// -2 means non-unit vector
		if (vec[0] == -1)
		{
			if (vec[1] == -1)
				return -2;
			else if (vec[1] == 0)
			{
				if (vec[2] == -1)
					return -2;
				else if (vec[2] == 0)
					return 3; //(-1, 0, 0)
				else if (vec[2] == 1)
					return -2;
				else
					return -1;
			}
			else if (vec[1] == 1)
				return -2;
			else
				return -1;
		}
		else if (vec[0] == 0)
		{
			if (vec[1] == -1)
			{
				if (vec[2] == -1)
					return -2;
				else if (vec[2] == 0)
					return 5; //(0, -1, 0)
				else if (vec[2] == 1)
					return -2;
				else
					return -1;
			}
			else if (vec[1] == 0)
			{
				if (vec[2] == -1)
					return 2; //(0, 0, -1)
				else if (vec[2] == 0)
					return -2;
				else if (vec[2] == 1)
					return 1; //(0, 0, 1)
				else
					return -1;
			}
			else if (vec[1] == 1)
			{
				if (vec[2] == -1)
					return -2;
				else if (vec[2] == 0)
					return 0; //(0, 1, 0)
				else if (vec[2] == 1)
					return -2;
				else
					return -1;
			}
			else
				return -1;
		}
		else if (vec[0] == 1)
		{
			if (vec[1] == -1)
				return -2;
			else if (vec[1] == 0)
			{
				if (vec[2] == -1)
					return -2;
				else if (vec[2] == 0)
					return 4; //(1, 0, 0)
				else if (vec[2] == 1)
					return -2;
				else
					return -1;
			}
			else if (vec[1] == 1)
				return -2;
			else
				return -1;
		}
		else
			return -1;
	}

}
