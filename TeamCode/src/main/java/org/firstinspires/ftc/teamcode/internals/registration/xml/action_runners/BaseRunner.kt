package org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners

import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlEvironment
import org.w3c.dom.Node

interface BaseRunner {
    fun run(env: XmlEvironment, node: Node)
}