package Utils;

import org.andengine.util.math.MathUtils;

import GameLevels.Elements.Camp;

public class Math {

	public static float calEuclidDistance(Camp v, Camp w) {
		return MathUtils.distance(v.getmX(), v.getmY(), w.getmX(), w.getmY());
	}
	
}
