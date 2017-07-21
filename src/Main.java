import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Ball {

	int value;

	long color;

	public Ball(int value, long color) {
		super();
		this.value = value;
		this.color = color;
	}

	@Override
	public String toString() {
		return "[" + value + "," + color + "]";
	}

}

class Main {

	private static final Ball BALLS[] = { new Ball(1, 0x0000FF), new Ball(2, 0x000000), new Ball(3, 0xFF0000),
			new Ball(4, 0x00FFFF), new Ball(5, 0xFFFF00), new Ball(6, 0xFF00FF), new Ball(7, 0x800000),
			new Ball(8, 0xC0C0C0), new Ball(9, 0x0000FF), new Ball(10, 0x000000), new Ball(11, 0xFF0000),
			new Ball(12, 0x00FFFF), new Ball(13, 0xFFFF00), new Ball(14, 0xFF00FF), new Ball(15, 0x800000),
			new Ball(16, 0xC0C0C0), new Ball(17, 0x0000FF), new Ball(18, 0x000000), new Ball(19, 0xFF0000),
			new Ball(20, 0x00FFFF), new Ball(21, 0xFFFF00), new Ball(22, 0xFF00FF), new Ball(23, 0x800000),
			new Ball(24, 0xC0C0C0), new Ball(25, 0x0000FF), new Ball(26, 0x000000), new Ball(27, 0xFF0000),
			new Ball(28, 0x00FFFF), new Ball(29, 0xFFFF00), new Ball(30, 0xFF00FF), new Ball(31, 0x800000),
			new Ball(32, 0xC0C0C0), new Ball(33, 0x0000FF), new Ball(34, 0x000000), new Ball(35, 0xFF0000),
			new Ball(36, 0x00FFFF), new Ball(37, 0xFFFF00), new Ball(38, 0xFF00FF), new Ball(39, 0x800000),
			new Ball(40, 0xC0C0C0), new Ball(41, 0x0000FF), new Ball(42, 0x000000), new Ball(43, 0xFF0000),
			new Ball(44, 0x00FFFF), new Ball(45, 0xFFFF00), new Ball(46, 0xFF00FF), new Ball(47, 0x800000),
			new Ball(48, 0xC0C0C0), };

	private static void draw(Ball[] drawn, long combination) {
		for (int i = 0, j = 0; i < drawn.length; i++) {
			while ((combination & 0x1L) == 0) {
				combination >>= 1;
				j++;
			}

			drawn[i] = BALLS[j];
			combination >>= 1;
			j++;
		}
	}

	private static int sum(Ball[] drawn) {
		int result = 0;

		for (Ball ball : drawn) {
			result += ball.value;
		}

		return result;
	}

	private static void sumsStatistics() {
		long current = 0x1FL;
		long last = 0xF80000000000L;
		long buffer = 0;

		long found[] = { 0L, 0L, 0L, 0L, 0L, 0L };
		long total = 0L;
		int sum = 0;

		Ball drawn[] = { null, null, null, null, null };

		while (current <= last) {
			draw(drawn, current);
			sum = sum(drawn);

			if (sum < 102.5) {
				found[0]++;
			}
			if (sum > 102.5) {
				found[1]++;
			}
			if (sum < 122.5) {
				found[2]++;
			}
			if (sum > 122.5) {
				found[3]++;
			}
			if (sum < 142.5) {
				found[4]++;
			}
			if (sum > 142.5) {
				found[5]++;
			}

			total++;

			buffer = (current | (current - 1)) + 1;
			current = buffer | ((((buffer & -buffer) / (current & -current)) >> 1) - 1);
		}

		System.out.println("=" + found[0] + "/" + total);
		System.out.println("=" + found[1] + "/" + total);
		System.out.println("=" + found[2] + "/" + total);
		System.out.println("=" + found[3] + "/" + total);
		System.out.println("=" + found[4] + "/" + total);
		System.out.println("=" + found[5] + "/" + total);
	}

	private static int count(Ball[] drawn) {
		Map<Long, Integer> found = new HashMap<Long, Integer>();

		/*
		 * Initialize counters.
		 */
		found.put(0x0000FFL, 0);
		found.put(0x000000L, 0);
		found.put(0xFF0000L, 0);
		found.put(0x00FFFFL, 0);
		found.put(0xFFFF00L, 0);
		found.put(0xFF00FFL, 0);
		found.put(0x800000L, 0);
		found.put(0xC0C0C0L, 0);

		/*
		 * Count found balls for each color.
		 */
		for (Ball ball : drawn) {
			found.put(ball.color, found.get(ball.color) + 1);
		}

		/*
		 * Count how many colors are complete.
		 */
		int result = 0;
		for (Map.Entry<Long, Integer> entry : found.entrySet()) {
			if (entry.getValue() == 6) {
				result++;
			}
		}
		return result;
	}

	private static void colorsStatistics() {
		long current = 0x7FFFFFFFFL;
		long last = 0xFFFFFFFFE000L;
		long buffer = 0;

		long counters[] = { 0L, 0L, 0L, 0L, 0L, 0L };
		long total = 0L;

		Ball drawn[] = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, };

		while (current <= last) {
			draw(drawn, current);
			counters[count(drawn)]++;

			/*
			 * Report progress.
			 */
			if (total % 10000000 == 0) {
				System.err.println(100 * total / 192928249296D + "%");
			}

			total++;

			buffer = (current | (current - 1)) + 1;
			current = buffer | ((((buffer & -buffer) / (current & -current)) >> 1) - 1);
		}

		System.out.println("=" + counters[0] + "/" + total);
		System.out.println("=" + counters[1] + "/" + total);
		System.out.println("=" + counters[2] + "/" + total);
		System.out.println("=" + counters[3] + "/" + total);
		System.out.println("=" + counters[4] + "/" + total);
		System.out.println("=" + counters[5] + "/" + total);
	}

	public static void main(String[] args) throws java.lang.Exception {
		// sumsStatistics();
		
		colorsStatistics();
	}

}