package org.firstinspires.ftc.teamcode.internals.hardware.devices;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

/**
 * Represents an encoder. Why is it i2c? Because we have practically unlimted i2c devices! We want to treat motors and encoders as different devices, when the SDK treats them as one. So, we have this dummy i2c device that defines what encoder = what motor.
 */
@I2cDeviceType
@DeviceProperties(name = "Encoder", description = "Represents a motor encoder. Yes this is not i2c, but this is the best way to do what we want to do.", xmlTag = "XBLIBENCODER")
public class EncoderI2C extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    protected EncoderI2C(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);
    }

    @Override
    protected boolean doInitialize() {
        return false;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

}
