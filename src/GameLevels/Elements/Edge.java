package GameLevels.Elements;

public class Edge {

	// ====================================================
	// VARIABLES
	// ====================================================
	private final Camp v, w;
	private final float mWeight;

	// =====================================================
	// CONSTUCTOR
	// =====================================================

	public Edge(Camp pEither, Camp pOr) {
		this.v = pEither;
		this.w = pOr;
		this.mWeight = Utils.Math.calEuclidDistance(v, w);
	}

	public double getWeight() {
		return this.mWeight;
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
