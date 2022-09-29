package org.firstinspires.ftc.teamcode.features

import com.michaell.looping.ScriptRunner.DuplicateScriptException
import com.michaell.looping.ScriptTemplate
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter.Companion.jloopingRunner

abstract class BlankFeature(name: String?, needsInit: Boolean) : ScriptTemplate(name, needsInit) {
    companion object {
        @JvmStatic
        @Throws(DuplicateScriptException::class)
        fun registerFeature(feature: BlankFeature) {
            jloopingRunner!!.addScript(feature)
        }
    }
}