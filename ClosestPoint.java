import java.io.*;
import java.util.*;

import java.awt.Point;

public class ClosestPoint {
	// The minimum distance squared
	static long dminsq = Long.MAX_VALUE;
	
	static ArrayList<Pair> parr = new ArrayList<Pair>();


	public static void main(String[] args) {
		// The list of points that will be sorted by X values
		List<Point> points = new ArrayList<>();

		// The list of points that will be sorted by Y values
		List<Point> qpoints = new ArrayList<>();

		Scanner scan = new Scanner(System.in);

		long n = scan.nextLong();

		// Adding the input points to Points and QPoints
		for (long i = 0; i < n; i++) {
			Point p = new Point();
			p.setLocation(scan.nextLong(), scan.nextLong());
			points.add(p);
			qpoints.add(p);

		}
		scan.close();
		// Sort points by X Coordinate
		sortByX(points);
		// Sort the QPoints by Y Coordinate
		sortByY(qpoints);

		closestPair(points, qpoints);
		sortX(parr);
		sortDist(parr);

		System.out.println(parr.get(0).x1 + " " + parr.get(0).y1 + " " + parr.get(0).x2 + " " + parr.get(0).y2);

	}

	// Sorts the list of points by their X-Value
	private static void sortByX(List<Point> points) {
		Collections.sort(points, new Comparator<Point>() {
			public int compare(Point x1, Point x2) {
				if (x1.getX() < x2.getX())
					return -1;
				if (x1.getX() == x2.getX() && (x1.getY() < x2.getY()))
					return -1;
				if (x1.getX() == x2.getX() && (x1.getY() > x2.getY()))
					return 1;

				if (x1.getX() > x2.getX())
					return 1;
				return 0;

			}
		});
	}

	// Sorts the list of points by their Y-Value
	private static void sortByY(List<Point> points) {
		Collections.sort(points, new Comparator<Point>() {
			public int compare(Point y1, Point y2) {
				if (y1.getY() < y2.getY())
					return -1;
				if (y1.getY() == y2.getY() && (y1.getX() < y2.getX()))
					return -1;
				if (y1.getY() == y2.getY() && (y1.getX() > y2.getX()))
					return 1;
				if (y1.getY() > y2.getY())
					return 1;
				return 0;
			}
		});
	}

	// The Efficient Closest Pair Algorithm
	public static long closestPair(List<Point> p, List<Point> q) {

		// Base Case
		// if p.size() is less than or equal to 3, call the brute force
		// algorithm
		if (p.size() <= 3) {
			return bruteForce(p);
		} else {
			long w = 0;
			long n = p.size();
			long mid = (n / 2);
			List<Point> pl = new ArrayList<>();
			List<Point> ql = new ArrayList<>();
			List<Point> pr = new ArrayList<>();
			List<Point> qr = new ArrayList<>();

			// Created a hashset to hold the points of pl, so that the .contains
			// function below
			// to add the points to ql and qr will execute in linear time
			Set<Point> hpl = new HashSet<Point>();

			// Add the left half of the points of P to pl
			for (long i = 0; i < mid; i++) {

				pl.add((int) i, p.get((int) i));
				hpl.add(p.get((int) i));

			}
			// Add the right half of the points of P to pr
			for (long j = mid; j < p.size(); j++) {

				pr.add((int) w, p.get((int) j));

				w++;
			}

			// Adding the points to ql and qr through hpl's .contains method
			for (Point pt : q) {
				if (hpl.contains(pt)) {
					ql.add(pt);
				} else {
					qr.add(pt);
				}
			}

			List<Point> s = new ArrayList<>();
			long dl;
			long dr;
			long d;
			long m;

			dl = closestPair(pl, ql);

			dr = closestPair(pr, qr);

			d = Math.min(dl, dr);

			// m is the median between pl and pr
			m = p.get((int) ((n / 2) - 1)).x;

			// copy all of the points of q for which |x-m| < d into arrayList S
			long g = 0;
			for (long a = 0; a < q.size(); a++) {
				if ((Math.abs(q.get((int) a).x - m)) < (double) Math.sqrt(d)) {

					s.add((int) g, q.get((int) a));
					g = g + 1;

				}
			}

			dminsq = d;
			long be = 0;

			for (long u = 0; u < s.size(); u++) {
				for (long k = u + 1; k < s.size(); k++) {
					be = (long) (Math.pow((s.get((int) k).x - s.get((int) u).x), 2)
							+ Math.pow((s.get((int) k).y - s.get((int) u).y), 2));
					if (dminsq >= be) {

						dminsq = be;

						ClosestPoint pol = new ClosestPoint();
						long dist = dminsq;
						// Creating a custom pair object to hold all of the
						// pairs and their distances
						Pair goodPair = pol.new Pair(dist, s.get((int) u).x, (s.get((int) u).y), s.get((int) k).x,
								(s.get((int) k).y));
						parr.add(goodPair);

					}

					if ((Math.abs(s.get((int) k).y - Math.sqrt(d))) >= s.get((int) u).y) {

						break;
					}
				}

			}
			return dminsq;

		}

	}

	// This sorts a list of Pairs by their x1 coordinate
	public static void sortX(List<Pair> pairs) {
		Collections.sort(pairs, new Comparator<Pair>() {
			@Override
			public int compare(Pair x1, Pair x2) {
				if (x1.getX1() < x2.getX1())
					return -1;
				if (x1.getX1() > x2.getX1())
					return 1;
				return 0;
			}
		});
	}

	// This sorts a list of Pairs by their closest distance
	public static void sortDist(List<Pair> pairs) {
		Collections.sort(pairs, new Comparator<Pair>() {
			@Override
			public int compare(Pair x1, Pair x2) {
				if (x1.getDist() < x2.getDist())
					return -1;
				if (x1.getDist() > x2.getDist())
					return 1;
				return 0;
			}
		});
	}

	// The closest pair brute-force algorithm
	public static long bruteForce(List<Point> p) {

		long d = Long.MAX_VALUE;
		long te = 0;
		for (long i = 0; i < p.size(); i++) {
			for (long j = i + 1; j < p.size(); j++) {
				te = (long) (Math.pow((p.get((int) i).x - p.get((int) j).x), 2)
						+ Math.pow((p.get((int) i).y - p.get((int) j).y), 2));
				if (d >= te) {

					d = te;

					ClosestPoint pol = new ClosestPoint();
					long dist = d;
					Pair goodPair = pol.new Pair(dist, p.get((int) i).x, (p.get((int) i).y), p.get((int) j).x,
							(p.get((int) j).y));
					parr.add(goodPair);

				}

			}
		}

		return d;

	}

	// Custom Pair class to hold a pair of x and y coordinates and the distance
	// between them.
	public class Pair {
		long x1;
		long y1;
		long x2;
		long y2;
		long dist;

		public Pair(long dist, long x1, long y1, long x2, long y2) {
			this.dist = dist;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public Pair() {
			// TODO Auto-generated constructor stub
		}

		public long getDist() {
			return dist;
		}

		public long getX1() {
			return x1;
		}

		public long getY1() {
			return y1;
		}

		public long getX2() {
			return x2;
		}

		public long getY2() {
			return y2;
		}

	}
}
