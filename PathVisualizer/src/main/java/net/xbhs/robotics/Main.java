package net.xbhs.robotics;

import net.xbhs.robotics.bots.Prod1;
import net.xbhs.robotics.bots.Prod2;
import net.xbhs.robotics.bots.Prod3;
import net.xbhs.robotics.utils.Runner;

public class Main {
    public static void main(String[] args) {
        Runner.run(new Prod1(), new Prod2(), new Prod3());
    }
}