import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    // instantiate for task 4
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testWordToDeque() {
        Deque d1 = palindrome.wordToDeque("");
        String actual1 = "";
        for (int i = 0; i < "".length(); i++) {
            actual1 += d1.removeFirst();
        }
        assertEquals("", actual1);
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        // Edge cases length 1 or 0 is a palindrome, null is not
        assertTrue(palindrome.isPalindrome(""));

        assertTrue(palindrome.isPalindrome("a"));

        assertFalse(palindrome.isPalindrome(null));

        // normal cases
        assertTrue(palindrome.isPalindrome("racecar"));

        assertFalse(palindrome.isPalindrome("horse"));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        // Edge cases length 1 or 0 is a palindrome, null is not
        assertTrue(palindrome.isPalindrome("", offByOne));

        assertTrue(palindrome.isPalindrome("a", offByOne));

        assertFalse(palindrome.isPalindrome(null, offByOne));

        // normal cases
        assertTrue(palindrome.isPalindrome("flake", offByOne));

        assertFalse(palindrome.isPalindrome("racecar", offByOne));

        //  no CharacterComparator provided, method resort to isPalindrome(String word)
        assertFalse(palindrome.isPalindrome("flake", null));

        assertTrue(palindrome.isPalindrome("racecar", null));
    }

}
