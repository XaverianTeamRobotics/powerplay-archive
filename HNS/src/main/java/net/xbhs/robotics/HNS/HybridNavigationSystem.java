package net.xbhs.robotics.HNS;

import net.xbhs.robotics.HNS.roles.HNSRole_PrimaryNavigator;
import net.xbhs.robotics.HNS.roles.HNSRole_ZeroProvider;

import java.util.ArrayList;

public class HybridNavigationSystem extends NavigationSystem {
    private final ArrayList<Class<? extends NavigationSystem>> navClasses = new ArrayList<>();
    private final ArrayList<NavigationSystem> navSystems = new ArrayList<>();
    private boolean isStarted = false;
    private boolean primaryNavigatorFound = false;
    private final double zeroThreshold = 0.2;

    public HybridNavigationSystem() {
        super("Hybrid Navigation System");
    }

    public void addNavigationSystem(Class<? extends NavigationSystem> navClass) throws IllegalArgumentException {
        // If the class does not have the HNSCompatible annotation, throw an exception
        if (!navClass.isAnnotationPresent(HNSCompatible.class)) {
            throw new IllegalArgumentException("The class " + navClass.getName() + " is not compatible with the Hybrid Navigation System.");
        }
        // If the class already exists in the list, throw an exception
        if (navClasses.contains(navClass)) {
            throw new IllegalArgumentException("The class " + navClass.getName() + " is already in the Hybrid Navigation System.");
        }
        // If we have already started, throw a RuntimeException
        if (isStarted) {
            throw new RuntimeException("The Hybrid Navigation System has already been started.");
        }
        // If the class is a primary navigator, and we have already found one, throw an exception
        if (navClass.isAnnotationPresent(HNSRole_PrimaryNavigator.class) && primaryNavigatorFound) {
            throw new IllegalArgumentException("The class " + navClass.getName() + " is a primary navigator, but another primary navigator has already been added to the Hybrid Navigation System.");
        }
        // If the class is a primary navigator, set the primaryNavigatorFound flag to true
        else if (navClass.isAnnotationPresent(HNSRole_PrimaryNavigator.class)) {
            primaryNavigatorFound = true;
        }
        // The class must either be a primary navigator or a zero provider
        else if (!navClass.isAnnotationPresent(HNSRole_ZeroProvider.class)) {
            throw new IllegalArgumentException("The class " + navClass.getName() + " is not a primary navigator or a zero provider.");
        }
        navClasses.add(navClass);
    }

    @Override
    public void update() throws NavigationSystemException {
        // Update each instance
        for (NavigationSystem navSystem : navSystems) {
            navSystem.update();
        }

        // Get all zero providers and correct the classes that require a zero provider
        ArrayList<NavigationSystem> zeroProviders = new ArrayList<>();
        for (NavigationSystem navSystem : navSystems) {
            if (navSystem.getClass().isAnnotationPresent(HNSRole_ZeroProvider.class)) {
                zeroProviders.add(navSystem);
            }
        }

        // If half of the zeroProviders agree that the robot is at zero velocity and acceleration, then run the 'correct' method
        int zeroCount = 0;
        for (NavigationSystem navSystem : zeroProviders) {
            if (isZeroAccelAndVel(navSystem.getLocalizer())) {
                zeroCount++;
            }
        }
        if (zeroCount >= 1) {
            correct(new Localizer(localizer.x, localizer.y, localizer.azimuth));
        }

        // If we have not found a primary navigator, throw an exception
        if (!primaryNavigatorFound) {
            throw new NavigationSystemException("No primary navigator has been added to the Hybrid Navigation System.");
        }

        // Set our localizer to the localizer of the primary navigator
        for (NavigationSystem navSystem : navSystems) {
            if (navSystem.getClass().isAnnotationPresent(HNSRole_PrimaryNavigator.class)) {
                setLocalizer(navSystem.getLocalizer());
                break;
            }
        }
    }

    private boolean isZeroAccelAndVel(Localizer localizer) {
        return Math.abs(localizer.vX) <= zeroThreshold && Math.abs(localizer.vY) <= zeroThreshold && Math.abs(localizer.vAzimuth) <= zeroThreshold && Math.abs(localizer.aX) <= zeroThreshold && Math.abs(localizer.aY) <= zeroThreshold && Math.abs(localizer.aAzimuth) <= zeroThreshold;
    }

    @Override
    public void reset() throws NavigationSystemException {
        // Reset each instance
        for (NavigationSystem navSystem : navSystems) {
            navSystem.reset();
        }

        super.reset();
    }

    @Override
    public void start() throws NavigationSystemException {
        // If we have already started, throw a RuntimeException
        if (isStarted) {
            throw new RuntimeException("The Hybrid Navigation System has already been started.");
        }

        // Throw an exception if no primary navigator has been added
        if (!primaryNavigatorFound) {
            throw new NavigationSystemException("No primary navigator has been added to the Hybrid Navigation System.");
        }

        // Throw an exception if no zero providers have been added and one is required
        boolean zeroProviderRequired = false;
        for (Class<? extends NavigationSystem> navClass : navClasses) {
            if (navClass.isAnnotationPresent(HNSRole_PrimaryNavigator.class)) {
                zeroProviderRequired = true;
                break;
            }
        }
        for (Class<? extends NavigationSystem> navClass : navClasses) {
            if (navClass.isAnnotationPresent(HNSRole_ZeroProvider.class)) {
                zeroProviderRequired = false;
                break;
            }
        }
        if (zeroProviderRequired) {
            throw new NavigationSystemException("No zero providers have been added to the Hybrid Navigation System and one is required.");
        }

        // Create an instance of each class
        for (Class<? extends NavigationSystem> navClass : navClasses) {
            try {
                navSystems.add(navClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new NavigationSystemException("Could not create an instance of the class " + navClass.getName() + ".", e);
            }
        }

        // Start each instance
        for (NavigationSystem navSystem : navSystems) {
            navSystem.start();
        }
        isStarted = true;
    }

    @Override
    public void correct(Localizer localizer) throws NavigationSystemException {
        // Correct each instance
        for (NavigationSystem navSystem : navSystems) {
            navSystem.correct(localizer);
        }

        // Correct our localizer
        this.localizer = localizer;
    }
}
