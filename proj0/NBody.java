public class NBody {

    /* Define the two static string background image and sound */
    public static String backgroundImage = "images/starfield.jpg";
    public static String backgroundSound = "audio/2001.mid";

    /* Static methods to read radius and bodies from file */
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int planetNum = in.readInt();
        double uniRadius = in.readDouble();
        return uniRadius;
    }

    public static Body[] readBodies(String fileName) {
        In in = new In(fileName);
        int planetNum = in.readInt();
        double uniRadius = in.readDouble();
        Body [] bodies = new Body[planetNum];
        for (int i = 0; i < planetNum; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            bodies[i] = new Body(xP, yP, xV, yV, m, img);
            // bodies[i] = new Body(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return bodies;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
			System.out.println("Please pass three command line arguments with space in order T dt filename.");
            return;
        }

        /* Initialize arguments from the inputs */
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double uniRadius = NBody.readRadius(filename);
        Body [] bodies = NBody.readBodies(filename);
        int planetNum = bodies.length;

        StdDraw.enableDoubleBuffering();
        /** Sets up the universe so it goes from
		  * -uniRadius, -uniRadius up to uniRadius, uniRadius */
		StdDraw.setScale(-uniRadius, uniRadius);

        // create the time variable
        double t = 0;

        // Play the sound track
        StdAudio.play(backgroundSound);

        while (t <= T) {
            double[] xForces = new double[planetNum];
            double[] yForces = new double[planetNum];

            /** Calculate net forces for each body */
            for (int i = 0; i < planetNum; i++) {
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
            }

            /* Update status on each body, velocity, position*/
            for (int i = 0; i < planetNum; i++) {
                bodies[i].update(dt, xForces[i], yForces[i]);
            }

            /* Clears the drawing window. */
            StdDraw.clear();

            /* Draw the starfield*/
            StdDraw.picture(0, 0, backgroundImage);

            /* Draw each body */
            for (Body b : bodies) {
                b.draw();
            }

            /* Shows the drawing to the screen, and waits 2000 milliseconds. */
		    StdDraw.show();
		    StdDraw.pause(1);

            /* increment t by dt */
            t += dt;
        }

        /* Print out simulation results */
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", uniRadius);
        for (int i = 0; i < planetNum; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                          bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
        }
    }
}