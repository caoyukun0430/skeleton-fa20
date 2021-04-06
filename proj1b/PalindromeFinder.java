
/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-fa20/data/words.txt");
        Palindrome palindrome = new Palindrome();
        // instantiate for task 4
//        CharacterComparator offByOne = new OffByOne();
        // instantiate for task 5
//        CharacterComparator offByN = new OffByN(5);

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word)) {
//          if (word.length() >= minLength && palindrome.isPalindrome(word, offByN)) {
                System.out.println(word);
            }
        }
    }
}
