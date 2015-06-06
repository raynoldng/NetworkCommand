package GameLevels.Elements;

import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;

import GameLevels.GameLevel;
import Managers.ResourceManager;

public class Edge {

	// ====================================================
	// VARIABLES
	// ====================================================
	private final GameLevel mGameLevel;
	private final Camp v, w;
	private final float mWeight;
	private final float mGradient;

	// =====================================================
	// CONSTUCTOR
	// =====================================================

	public Edge(Camp pEither, Camp pOr, GameLevel pGameLevel) {
		this.v = pEither;
		this.w = pOr;
		this.mWeight = Utils.Math.calEuclidDistance(v, w);
		this.mGameLevel = pGameLevel;
		
		float dY = w.getmY() - v.getmY();
		float dX = w.getmX() - v.getmX();
		
		mGradient = dY / dX;
		
		//draw line
		Line line = new Line(w.getmX(), w.getmY(), v.getmX(), v.getmY(), ResourceManager.getActivity().getVertexBufferObjectManager());
		line.setColor(Color.BLACK);
		mGameLevel.attachChild(line);
		line.setZIndex(0);
	}

	public double getWeight() {
		return this.mWeight;
	}
	
	public double getGradient() {
		return mGradient;
	}

	public Camp other(Camp vertex) {
		if (vertex == v)
			return w;
		else if (vertex == w)
			return v;
		else
			throw new IllegalArgumentException("Illegal Endpoint");
	}

	public int compareTo(Edge that) {
		if (this.getWeight() < that.getWeight())
			return -1;
		else if (this.getWeight() > that.getWeight())
			return +1;
		else
			return 0;
	}
}
