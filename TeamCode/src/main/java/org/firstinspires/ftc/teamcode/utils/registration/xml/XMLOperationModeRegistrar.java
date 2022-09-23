package org.firstinspires.ftc.teamcode.utils.registration.xml;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.utils.registration.*;

import static org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta.Flavor.AUTONOMOUS;
import static org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta.Flavor.TELEOP;

/**
 * This registers {@link OperationMode}s. It should not be instantiated. As a rule of thumb, most classes referring to registration should not be instantiated as registration follows functional patterns. That's just the Qualcomm Way™.
 * <br>
 * <br>
 * <em>For advanced users:</em>
 * <br>
 * OpModes are a pain to create. Not because they're a lot of work or particulary hard, but because it's easy to forget to annotate them so the default registrar can find and register them. So, {@link OperationMode}s were created to solve this. They contain all the information needed to be registered without an annotation. To do this without annotations, we need to make our own registrar which finds OpModes differently. To do this, we create a static method annotated with {@link OpModeRegistrar} so the app can find the method. The method takes a manager, and that manager is what's used to actually register the OpModes. Via reflection, {@link OperationMode}s are found. They are then instantiated and their getters called which the values returned from are stored locally in the method. Finally, the metadata received from the {@link OperationMode}s and their respective getters is used to register each OpMode via {@link OpModeManager#register(OpModeMeta, Class)}, assuming the OpMode is not disabled.
 */
public class XMLOperationModeRegistrar {

    private static final String KEY = "a7216e0b6a49850c6092991040467037d0fc899960bba2c08c4afafeb8b3bf1bfd748fe8050348f614c7e4421af2449a47db9d01de07b13a8c2fb060dac3e1ed5053643c4739479ff3fc665a9dba57e47d65803838d1617de4b1658a9e022e9bc0eaf71b";

    /**
     * Private constructor to prevent instantiation.
     */
    private XMLOperationModeRegistrar() {}

    /**
     * The method which acts as a functional registrar. Do <strong>NOT</strong> call this yourself unless you are absolutely sure of what you're doing!
     * @param manager The manager to register OpModes with, passed by the app itself as the app is supposed to call this method, <em>not you (probably)</em>
     */
    // @OpModeRegistrar
    public static void registerOperationModes(OpModeManager manager) {
        // log init and set up our store
        XMLOperationModeRegistrationLogger.log("Initializing service...", KEY);
        XMLOperationModeRegistrarStore.setManager(manager, KEY);
        XMLOperationModeRegistrarStore.purgeClasses(KEY);
        // process our classes, sending them off for registration
        XMLOperationModeRegistrationLogger.log("Processing classes...", KEY);
        XMLOperationModeRegistrationLogger.log("XML Files Found: " + XMLRoboscriptParser.getRoboscriptXMLFiles().size(), KEY);
        for (String fileName :
            XMLRoboscriptParser.getRoboscriptXMLFiles()) {
            XMLOperationModeRegistrationLogger.log("Processing file: " + fileName, KEY);
            XMLRoboscriptParser xmlParser = new XMLRoboscriptParser("roboscript/"+fileName);
            OpModeMeta.Flavor flavor;
            if (xmlParser.isTeleOp()) {
                flavor = TELEOP;
            } else {
                flavor = AUTONOMOUS;
            }
            OpModeMeta.Builder metaBuilder = new OpModeMeta.Builder();
            metaBuilder.setFlavor(flavor);
            metaBuilder.setGroup(xmlParser.getGroup());
            metaBuilder.setName(xmlParser.getName());
            metaBuilder.setSource(OpModeMeta.Source.EXTERNAL_LIBRARY);

            if (xmlParser.rootElement.hasAttribute("autoTransition")) {
                metaBuilder.setTransitionTarget(xmlParser.rootElement.getAttribute("autoTransition"));
            } else {
                metaBuilder.setTransitionTarget(null);
            }

            XMLOperationModeRegistrarStore.getManager(KEY).register(metaBuilder.build(),
                new XMLOpModeTemplate(xmlParser));
            XMLOperationModeRegistrationLogger.log("Operation Mode " + xmlParser.getName() + " from " + xmlParser.filePath +
                " registered! Moving on...", KEY);
        }
    }
}