import es.datastructur.synthesizer.GuitarString;

// Follow GuitarHeroLite but extend to 37 notes
public class GuitarHero {
    private static final double CONCERT_A = 440.0;
//    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] arr = new GuitarString[37];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new GuitarString(CONCERT_A * Math.pow(2, i / 12.0));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int arrIndex = keyboard.indexOf(key);
                if (arrIndex != -1) {
                    arr[arrIndex].pluck();
                }
            }


            /* compute the superposition of samples */
            double sample = 0.0;
            for (GuitarString guitarString : arr) {
                sample += guitarString.sample();
            }


            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString guitarString : arr) {
                guitarString.tic();
            }

        }
    }
}
