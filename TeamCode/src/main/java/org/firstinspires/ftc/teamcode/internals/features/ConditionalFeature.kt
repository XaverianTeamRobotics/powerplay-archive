package org.firstinspires.ftc.teamcode.internals.features

import com.michaell.looping.ScriptRunner
import com.michaell.looping.builtin.ConditionalScriptTemplate
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

abstract class ConditionalFeature(name: String?, needsInit: Boolean) : ConditionalScriptTemplate(name, needsInit) {
    companion object {
        @JvmStatic
        @Throws(ScriptRunner.DuplicateScriptException::class)
        fun registerFeature(feature: BlankFeature) {
            HardwareGetter.jloopingRunner!!.addScript(feature)
        }
    }
}