package GameLevels;

import java.util.ArrayList;
import java.util.LinkedList;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

import GameLevels.Levels.CampDef;
import GameLevels.Levels.EdgeDef;
import GameLevels.Levels.LevelDef;
import GameLevels.Elements.Camp;
import GameLevels.Elements.Edge;
import GameLevels.Elements.Player;
import Managers.GameManager;

public class GameLevel extends ManagedGameScene implements IOnSceneTouchListener {

	// ==========================================
	// CONSTANTS
	// ==========================================
 
	// ==========================================
	// VARIABLES
	// ==========================================
	public final LevelDef mLevelDef;

	public ArrayList<Camp> mCamps;
	public ArrayList<Edge> mEdges;

	public LinkedList<Player> mPlayers;

	// ==========================================
	// UPDATE HANDLERS
	// ==========================================

	// ==========================================
	// OBJECT POOLS
	// ==========================================

	// ==========================================
	// CONSTRUCTOR 
	// ==========================================
	public GameLevel(final LevelDef pLevelDef) {
		this.mLevelDef = pLevelDef;
		
		mCamps = new ArrayList<Camp>();
		mEdges = new ArrayList<Edge>();
	}

	protected void addEdge(EdgeDef curEdge) {
		// helper method needed as we need to reference the two camps at the two ends
		// of edge
		int v = curEdge.v;
		int w = curEdge.w;
		
		assert v < mCamps.size() && w < mCamps.size(): "v:" + v + " w:" + w + " nCamps: " + mCamps.size();
		assert v != w : "Edge must point to two distinct camps!";
		
		Edge edge = new Edge(mCamps.get(v), mCamps.get(w));
		mCamps.get(v).addEdge(edge);
		mCamps.get(w).addEdge(edge);
		
		this.mEdges.add(edge);
	}
	
	@Override
	public void onLoadLevel() {
		GameManager.setGameLevel(this);
		
		this.addLoadingStep(new LoadingRunnable("Loading game textures", this) {
			
			@Override
			public void onLoad() {
				// load camps
				for(final CampDef curCamp : GameLevel.this.mLevelDef.mCamps) {
					Camp camp = new Camp(curCamp.mX, curCamp.mY, curCamp.mSize, curCamp.color, GameLevel.this);
					GameLevel.this.mCamps.add(camp);
				}
					
				// add edges
				for(final EdgeDef curEdge : GameLevel.this.mLevelDef.mEdges)
					GameLevel.this.addEdge(curEdge);
			}
		});
		
		this.setBackgroundEnabled(true);
		this.setBackground(new Background(0.1f, 0.1f, 0.1f));
		this.setOnSceneTouchListener(this);
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
}
