public class Localizer {
    public double x, y, azimuth, vX, vY, vAzimuth, aX = 0, aY = 0, aAzimuth = 0;

    public void setX(double x) {
        setPose(x, y, azimuth);
    }

    public void setY(double y) {
        setPose(x, y, azimuth);
    }

    public void setAzimuth(double azimuth) {
        setPose(x, y, azimuth);
    }

    public void setPose(double x, double y) {
        setPose(x, y, azimuth);
    }

    public void setPose(double x, double y, double azimuth, double vX, double vY, double vAzimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
        this.vX = vX;
        this.vY = vY;
        this.vAzimuth = vAzimuth;
    }

    public void setVelocity(double vX, double vY, double vAzimuth) {
        this.vX = vX;
        this.vY = vY;
        this.vAzimuth = vAzimuth;
    }

    public void setAcceleration(double aX, double aY, double aAzimuth) {
        this.aX = aX;
        this.aY = aY;
        this.aAzimuth = aAzimuth;
    }

    public void setPose(double x, double y, double azimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
    }

    public void setPose(Localizer pose) {
        this.x = pose.x;
        this.y = pose.y;
        this.azimuth = pose.azimuth;
    }

    public Localizer() {
        this.x = 0;
        this.y = 0;
        this.azimuth = 0;
        this.vX = 0;
        this.vY = 0;
        this.vAzimuth = 0;
        this.aX = 0;
        this.aY = 0;
        this.aAzimuth = 0;
    }

    public Localizer(double x, double y, double azimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
        this.vX = 0;
        this.vY = 0;
        this.vAzimuth = 0;
        this.aX = 0;
        this.aY = 0;
        this.aAzimuth = 0;
    }

    public Localizer(double x, double y, double azimuth, double vX, double vY, double vAzimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
        this.vX = vX;
        this.vY = vY;
        this.vAzimuth = vAzimuth;
        this.aX = 0;
        this.aY = 0;
        this.aAzimuth = 0;
    }

    public Localizer(double x, double y, double azimuth, double vX, double vY, double vAzimuth, double aX, double aY, double aAzimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
        this.vX = vX;
        this.vY = vY;
        this.vAzimuth = vAzimuth;
        this.aX = aX;
        this.aY = aY;
        this.aAzimuth = aAzimuth;
    }

    @Override
    public String toString() {
        return "Localizer{" +
                "x=" + x +
                ", y=" + y +
                ", azimuth=" + azimuth +
                ", vX=" + vX +
                ", vY=" + vY +
                ", vAzimuth=" + vAzimuth +
                ", aX=" + aX +
                ", aY=" + aY +
                ", aAzimuth=" + aAzimuth +
                '}';
    }
}
