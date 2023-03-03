package org.firstinspires.ftc.teamcode.internals.image;

import org.openftc.easyopencv.OpenCvCameraFactory;

public class MultipleCameraManager {

    private static int[] arr = null;

    public static int[] get(int id) {
        if(arr == null) {
            arr = i_get(id);
        }
        return arr;
    }

    private static int[] i_get(int id) {
        return OpenCvCameraFactory.getInstance()
            .splitLayoutForMultipleViewports(
                id, //The container we're splitting
                2, //The number of sub-containers to create
                OpenCvCameraFactory.ViewportSplitMethod.HORIZONTALLY); //Whether to split the container vertically or horizontally
    }
}
