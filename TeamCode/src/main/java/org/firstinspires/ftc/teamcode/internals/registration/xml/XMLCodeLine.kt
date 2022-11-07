package org.firstinspires.ftc.teamcode.internals.registration.xml

import org.w3c.dom.Node

class XMLCodeLine(val node: Node) {
    fun runAction(env: XmlEvironment) {
        env.functions[XmlActionMap.mapFromMPSString(node.nodeName)]?.run(env, node)
    }
}