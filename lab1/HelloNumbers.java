public class HelloNumbers {
	public static void main(String[] args) {
		int x = 0;
		int sum = 0;
		while (x < 10) {
			sum += x;
			System.out.print(sum + " ");
			x = x + 1;
		}
	}
}

/*
1. Before Java variables can be used, they must be declared.
2. Java variables must have a specific type.
3. Java variable types can never change.
4. Types are verified before the code even runs!!!
*/