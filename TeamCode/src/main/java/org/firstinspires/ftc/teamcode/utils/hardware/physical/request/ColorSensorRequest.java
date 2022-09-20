package org.firstinspires.ftc.teamcode.utils.hardware.physical.request;

import android.graphics.Color;
import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.Colors;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.Colors;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.Colors;

public class ColorSensorRequest extends ScriptParameters.Request {

    private final NormalizedColorSensor NORMALIZED_COLORS;
    private final ColorSensor RAW;

    public ColorSensorRequest(String name, HardwareMap hardwareMap) {
        super(name);
        NORMALIZED_COLORS = hardwareMap.get(NormalizedColorSensor.class, name);
        RAW = hardwareMap.get(ColorSensor.class, name);
    }

    @Override
    public Object issueRequest(Object o) {
        int[] vals = new int[4];
        vals[0] = (int) (NORMALIZED_COLORS.getNormalizedColors().red * 255);
        vals[1] = (int) (NORMALIZED_COLORS.getNormalizedColors().green * 255);
        vals[2] = (int) (NORMALIZED_COLORS.getNormalizedColors().blue * 255);
        vals[3] = (int) (NORMALIZED_COLORS.getNormalizedColors().alpha * 255);
        float[] colors = new float[3];
        Color.RGBToHSV(vals[0], vals[1], vals[2], colors);
        double[] colorsd = new double[3];
        colorsd[0] = colors[0];
        colorsd[1] = colors[1];
        colorsd[2] = colors[2];
        int gray = vals[0] + vals[1] + vals[2];
        return new Colors(vals, colorsd, gray, NORMALIZED_COLORS.getGain(), RAW.argb());
    }

    @Override
    public Class getOutputType() {
        return Colors.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
