package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItem;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItemType;
import org.firstinspires.ftc.teamcode.internals.telemetry.TelemetryMenu;

import java.util.ArrayList;

public class TelemetryMenuTest extends OperationMode implements TeleOperation {
    TelemetryMenu menu;

    @Override
    public void construct() {
        menu = new TelemetryMenu();
        MenuItem item1, item2, item3, item4, item5;
        item1 = new MenuItem("Int test", MenuItemType.INT, true);
        item2 = new MenuItem("Double Test", MenuItemType.DOUBLE, true);
        item3 = new MenuItem("Bool test", MenuItemType.BOOLEAN, true);
        item4 = new MenuItem("Menu test", MenuItemType.MENU, true);
        item5 = new MenuItem("Boolean inside other menu", MenuItemType.BOOLEAN, true);

        ((ArrayList<MenuItem>) item4.getValue()).add(item5);

        menu.addMenuItem(item1);
        menu.addMenuItem(item2);
        menu.addMenuItem(item3);
        menu.addMenuItem(item4);

        menu.runInBackground();
    }

    @Override
    public void run() {
        menu.setAnnotation("Boolean inside the other menu " + ((boolean) menu.getMenuItem("Boolean inside other menu").getValue()));
    }
}
