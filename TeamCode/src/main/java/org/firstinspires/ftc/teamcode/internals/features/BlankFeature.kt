package org.firstinspires.ftc.teamcode.internals.features

import com.michaell.looping.ScriptRunner.DuplicateScriptException
import com.michaell.looping.ScriptTemplate
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.jloopingRunner

/*
    * This is an example of a feature. Features are scripts that are run in the background.
 */
abstract class BlankFeature(name: String?, needsInit: Boolean) : ScriptTemplate(name, needsInit) {
    companion object {
        @JvmStatic
        @Throws(DuplicateScriptException::class)
        fun registerFeature(feature: BlankFeature) {
            jloopingRunner!!.addScript(feature)
        }
    }
}