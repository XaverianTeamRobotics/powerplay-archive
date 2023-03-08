import org.junit.jupiter.api.Test;

public class KalmanFilterTest {
    // Create two arbitrary Localizers to test the KalmanFilter
    private static final Localizer pose1 = new Localizer(0, 0, 0, 0, 0, 0, 0, 0, 0);
    private static final Localizer pose2 = new Localizer(1, 1, 1, 1, 1, 1, 3, 3, 3);

    // Create a test case to print the results of the KalmanFilter
    @Test
    public void testKalmanFilter() {
        // Create a KalmanFilter
        KalmanFilter kalmanFilter = new KalmanFilter(pose1);
        kalmanFilter.update(pose1, 1);
        Localizer pose2_corrected = kalmanFilter.correct(pose2, 1);

        // Print the results
        System.out.println("pose1: " + pose1);
        System.out.println("estimate:" + kalmanFilter.getEstimate());
        System.out.println("pose2: " + pose2);
        System.out.println("pose2_corrected: " + pose2_corrected);
    }
}
