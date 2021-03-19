public class Body {
    public static double G = 6.67e-11;

    /** instance variables for Body */
    public double xxPos;
    public double yyPos;

    public double xxVel;
    public double yyVel;

    public double mass;

    public String imgFileName;

    /**
        Constructor to initialize an instance of the Planet class with given parameters
        @param xP: current x position
        @param yP: current y position
        @param xV: current velocity in the x direction
        @param yV: current velocity in the y direction
        @param m: mass
        @param img: path to an image file that depicts the planet
     */
    public Body(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    };

    /**
       Constructor to copy given Planet object
    */
    public Body(Body b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    };

    /**
        Method to calculate the distance between defined Body and input Body
     */
    public double calcDistance(Body b) {
        double distX2 = Math.pow((xxPos - b.xxPos), 2);
        double distY2 = Math.pow((yyPos - b.yyPos), 2);
        double dist = Math.sqrt(distX2 + distY2);
        return dist;
    }

    /**
        calcForceExertedBy total force between two bodys
     */
    public double calcForceExertedBy(Body b) {
        if (this.equals(b)) {
            return 0;
        } else {
            double force = (G * mass * b.mass) / Math.pow(calcDistance(b), 2);
            return force;
        }
    }

    /**
        Body force in the x direction
     */
    public double calcForceExertedByX(Body b) {
        double distX = b.xxPos - xxPos;
        double forceX = calcForceExertedBy(b) * distX / calcDistance(b);
        return forceX;
    }

    /**
        Body force in the Y direction
     */
    public double calcForceExertedByY(Body b) {
        double distY = b.yyPos - yyPos;
        double forceY = calcForceExertedBy(b) * distY / calcDistance(b);
        return forceY;
    }

    /**
        Net force from all bodys in x direction
     */
    public double calcNetForceExertedByX(Body[] allBodys) {
        double netforceX = 0;
        for (Body b : allBodys) {
            if (this.equals(b)) {
                continue;
            }
            netforceX += calcForceExertedByX(b);
        }
        return netforceX;
    }

    /**
        Net force from all bodys in y direction
     */
    public double calcNetForceExertedByY(Body[] allBodys) {
        double netforceY = 0;
        for (Body b : allBodys) {
            if (this.equals(b)) {
                continue;
            }
            netforceY += calcForceExertedByY(b);
        }
        return netforceY;
    }

    /**
        Update the velocity and postion of body based on forces
        in x and y direction
     */
    public void update(double dt, double fX, double fY) {
        xxVel += dt * fX / mass;
        yyVel += dt * fY / mass;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    /**
        Draw the planet at its position
     */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
    }
}