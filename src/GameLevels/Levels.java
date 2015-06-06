package GameLevels;

import GameLevels.Elements.CampColor;

/**
 * This class holds an array of all of the levels that can be played in the 
 * game as well as helper methods to retrieve specific levels
 * 
 * @author NgFamily
 *
 */
public class Levels {
	
	//============================================
	// CONSTANTS
	//============================================	
	
	public static final LevelDef[] AvailableLevels = new LevelDef[] {
		new LevelDef(1, new CampDef[] {
				new CampDef(CampColor.BLUE, 100, 100, 50),
				new CampDef(CampColor.NEUTRAL, 100, 400, 50),
				new CampDef(CampColor.NEUTRAL, 400, 100, 50),
				new CampDef(CampColor.RED, 400, 240, 50)
		}, new EdgeDef[] {
				new EdgeDef(0, 1),
				new EdgeDef(1, 2),
				new EdgeDef(2, 1)
		})
	};
	
	//================================================
	// METHODS
	//================================================
	
	public static LevelDef getLevelDef(final int pLevelDef) {
		for(LevelDef curLevelDef : AvailableLevels) {
			if(curLevelDef.doIndicesMatch(pLevelDef))
				return curLevelDef;
		}
		return null;
	}

	
	
	//=========================
	// CLASSES
	//=========================
	
	public static class CampDef {
		
		CampColor color;
		public final int mX, mY;
		public final int mSize;
		
		public CampDef(CampColor color, int mX, int mY, int mSize) {
			this.color = color;
			this.mX = mX;
			this.mY = mY;
			this.mSize = mSize;
		}
	}
	
	public static class EdgeDef {
		public int v, w;

		public EdgeDef(int v, int w) {
			this.v = v;
			this.w = w;
		}
	}
	
	public static class LevelDef {
		public int mLevelIndex;
		public final CampDef[] mCamps;
		public final EdgeDef[] mEdges;
		
		public LevelDef(int mLevelIndex, CampDef[] mCamps, EdgeDef[] mEdges) {
			this.mLevelIndex = mLevelIndex;
			this.mCamps = mCamps;
			this.mEdges = mEdges;
		}
		
		public boolean doIndicesMatch(final int pLevelIndex) {
			return this.mLevelIndex == pLevelIndex;
		}
		
		
	}
	
}
