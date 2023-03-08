/**
 * Create a Kalman Filter that can be used with NavigationSystem
 * Takes in a Localizer as its main parameter
 * Accounts for the following variables from the Localizer:
 * x, y, azimuth, vX, vY, vAzimuth, aX, aY, aAzimuth
 * <p>
 * There will be three main functions:
 *  - update(Localizer localizer, double dt): Update position data with data that is known to be good
 *  - getEstimate(): Estimate the position of the robot
 *  - correct(Localizer localizer): Correct the localizer given and return the corrected localizer
 * The class will compute all 3 positions and velocities at the same time
 * @author Michael L
 */
public class KalmanFilter {
    private Localizer currentLocalizer;
    private double dt;

    public KalmanFilter(Localizer localizer) {
        this.currentLocalizer = localizer;
        this.dt = 0;
    }

    public void update(Localizer localizer, double dt) {
        this.currentLocalizer = localizer;
        this.dt = dt;
    }

    /**
     * Use the basic kinematic equations to estimate the position of the robot
     * @return The estimated position of the robot
     */
    public Localizer getEstimate() {
        Localizer estimate = new Localizer();
        estimate.setPose(
                currentLocalizer.x + currentLocalizer.vX * dt + currentLocalizer.aX * dt * dt / 2,
                currentLocalizer.y + currentLocalizer.vY * dt + currentLocalizer.aY * dt * dt / 2,
                currentLocalizer.azimuth + currentLocalizer.vAzimuth * dt + currentLocalizer.aAzimuth * dt * dt / 2,
                currentLocalizer.vX + currentLocalizer.aX * dt,
                currentLocalizer.vY + currentLocalizer.aY * dt,
                currentLocalizer.vAzimuth + currentLocalizer.aAzimuth * dt);
        return estimate;
    }

    // Estimated error gain. 0 = any jerk is 0% error, 100 = the jerk is equal to our % confidence
    private final double errorGain = 50;

    /**
     * Compute the jerk in acceleration and use it to adjust the estimated % confidence in our predictions
     * @return The estimated confidence of the algorithm. 0 = no error in estimate, 1 = no confidence in estimate. [x, y, azimuth]
     */
    private double[] getEstimatedConfidence(Localizer localizer, double dt) {
        double jerkX = (localizer.aX - this.currentLocalizer.aX) / dt;
        double jerkY = (localizer.aY - this.currentLocalizer.aY) / dt;
        double jerkAzimuth = (localizer.aAzimuth - this.currentLocalizer.aAzimuth) / dt;
        double[] error = {0, 0, 0};
        error[0] = Math.min(Math.abs(jerkX * errorGain) / 100, 1);
        error[1] = Math.min(Math.abs(jerkY * errorGain) / 100, 1);
        error[2] = Math.min(Math.abs(jerkAzimuth * errorGain) / 100, 1);
        return error;
    }

    /**
     * Correct the localizer given and return the corrected localizer
     * Computes a waited average based on the estimated confidence
     * @param localizer The localizer to correct
     * @return The corrected localizer
     */
    public Localizer correct(Localizer localizer, double dt) {
        // If error is 0, then we have no error
        // If error is 1, then we have no confidence
        double[] error = getEstimatedConfidence(localizer, dt);
        System.out.println(error[0] + ", " + error[1] + ", " + error[2]);
        return new Localizer(
                ((localizer.x * error[0]) +(getEstimate().x * (1 - error[0]))) / (error[0] + (1 - error[0])),
                ((localizer.y * error[1]) + (getEstimate().y * (1 - error[1]))) / (error[1] + (1 - error[1])),
                ((localizer.azimuth) * error[2] + (getEstimate().azimuth * (1 - error[2]))) / (error[2] + (1 - error[2])),
                ((localizer.vX * error[0]) + (getEstimate().vX * (1 - error[0]))) / (error[0] + (1 - error[0])),
                ((localizer.vY * error[1]) + (getEstimate().vY * (1 - error[1]))) / (error[1] + (1 - error[1])),
                ((localizer.vAzimuth * error[2]) + (getEstimate().vAzimuth * (1 - error[2]))) / (error[2] + (1 - error[2])),
                localizer.aX,
                localizer.aY,
                localizer.aAzimuth);
    }
}
