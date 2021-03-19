public class Fib {
    /** Implement fib which takes in an integer n and returns the nth Fibonacci number */
    public static int fib(int n) {
        int res = 0;
        if (n == 0) {
            res = 0;
        } else if ( n == 1 ) {
            res = 1;
        } else {
            res = fib(n-1) + fib(n-2);
        }
        return res;
    }
    /**
    solution: 
    public static int fib(int n) {
        if (n <= 1) {
        return n;
        } else {
        return fib(n - 1) + fib(n - 2);
        }
    } */

    /**
    More efficient one, which we store every var we calculated
    and use recursive definition
        @param k is where we start the Fib, should be 0 
    */
    public static int fib2(int n, int k, int f0, int f1) {
        if (n == k) {
            return f0;
        } else {
            return fib2(n, k + 1, f1, f0 + f1);
        }
    }
    public static void main(String[] args) {
       System.out.printf("call fib(), result is %d\n", fib(5));
       System.out.printf("call fib2(), result is %d\n", fib2(5, 0, 0, 1));
    }
}