public class Palindrome {

    /**  given a String, wordToDeque should return a Deque
     * where the characters appear in the same order as in the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    private boolean isPalindromeHelper(Deque<Character> wordDeque) {
        if (wordDeque.size() == 0 || wordDeque.size() == 1) {
            return true;
        }
        Character dFirst = wordDeque.removeFirst();
        Character dLast = wordDeque.removeLast();
        // After run removeFirst() and removeLast(), wordDeque also is modified, first and last
        // nodes are removed
        // for primitive char, we can use == or equals, both are fine
        // https://stackoverflow.com/questions/45893095/can-you-compare-chars-with
        if (dFirst.equals(dLast)) {
            return isPalindromeHelper(wordDeque);
        } else {
            return false;
        }
    }

    private boolean isPalindromeHelper(Deque<Character> wordDeque, CharacterComparator cc) {
        // this nicely handles even and odd length
        if (wordDeque.size() == 0 || wordDeque.size() == 1) {
            return true;
        }
        Character dFirst = wordDeque.removeFirst();
        Character dLast = wordDeque.removeLast();
        // After run removeFirst() and removeLast(), wordDeque also is modified,
        // first and last nodes are removed
        if (cc.equalChars(dFirst, dLast)) {
            return isPalindromeHelper(wordDeque, cc);
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word) {
        // corner case that input is null
        if (word == null) {
            return false;
        }
        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindromeHelper(wordDeque);
    }

    // Overloaded methods
    public boolean isPalindrome(String word, CharacterComparator cc) {
        // corner case that input word is null
        if (word == null) {
            return false;
        } else if (cc == null) {
            //  no CharacterComparator provided,
            //  method resort to isPalindrome(String word)
            return isPalindrome(word);
        }
        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindromeHelper(wordDeque, cc);
    }
}
