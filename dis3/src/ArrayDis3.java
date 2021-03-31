public class ArrayDis3 {

    /**
     * inserts an int item into an int[] arr at the given
     * position.
     *  If position is past the end of the array, insert item at the
     * end of the array.
     * Assume we will only ever pass in a non-negative position.
     * */
    public static int[] insert(int[] arr, int item, int position) {
        int[] arrNew = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            if (i < position) {
                arrNew[i] = arr[i];
            } else {
                arrNew[i+1] = arr[i];
            }
        }
        // handle the arrNew[position]
        if (position < arr.length) {
            arrNew[position] = item;
        } else {
            arrNew[arrNew.length-1] = item;
        }
        return arrNew;

        // dis3 solution:
//        int[] result = new int[arr.length + 1];
//        position = Math.min(arr.length, position);
//        for (int i = 0; i < position; i++) {
//            result[i] = arr[i];
//        }
//        result[position] = item;
//        for (int i = position; i < arr.length; i++) {
//            result[i + 1] = arr[i];
//        }
//        return result;

    }

    /**
     * non-destructive method
     * replaces the number at index i with arr[i] copies of itself
     * replicate([3, 2, 1])
     * would return [3, 3, 3, 2, 2, 1]
     * */
    public static int[] replicate(int[] arr) {
        int newSize = 0;
        for (int item : arr) {
            newSize += item;
        }
        int[] arrNew = new int[newSize];
        int i = 0;
        // Count the item times
        for (int item : arr) {
            for (int counter = 0; counter < item; counter++) {
                arrNew[i] = item;
                i++;
            }
        }
        return arrNew;
    }

        public static void main(String[] args) {
//        int[] arr = new int[] {5, 9, 14, 15};
//        int[] arrNew = insert(arr, 6, 0);

        int[] arr2 = new int[] {1, 2, 3};
        int[] arrNew2 = replicate(arr2);


    }
}
