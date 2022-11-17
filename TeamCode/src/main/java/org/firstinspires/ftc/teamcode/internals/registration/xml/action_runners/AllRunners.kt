package org.firstinspires.ftc.teamcode.internals.registration.xml.action_runners

import org.firstinspires.ftc.teamcode.internals.registration.xml.XMLCodeLine
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlActionMap
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlEvironment
import org.firstinspires.ftc.teamcode.internals.registration.xml.XmlVariable
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging.Companion.telemetry
import org.w3c.dom.Node

class VariableDeclarationRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val name = node.attributes.getNamedItem("name").nodeValue
        val type = node.attributes.getNamedItem("type").nodeValue
        val value = node.attributes.getNamedItem("value").firstChild

        XMLCodeLine(value).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.variables[name] = XmlVariable(name, computedValue)
    }
}

class IntegerConstantRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        env.cacheValue1 = node.nodeValue.toInt()
    }
}

class TelemetryLogLineRunner : BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val value = node.attributes.getNamedItem("value").firstChild
        XMLCodeLine(value).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        telemetry.addData("Log", computedValue)
    }
}

class VariableSetRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val name = node.attributes.getNamedItem("reference").nodeValue
        val value = node.attributes.getNamedItem("value").firstChild
        XMLCodeLine(value).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.variables[name]?.value = computedValue
    }
}

class VariableUsageRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val name = node.textContent
        env.cacheValue1 = env.variables[name]?.value
    }
}

class BinaryExpressionRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val left = node.childNodes.item(0)
        val right = node.childNodes.item(1)
        val operator = node.attributes.getNamedItem("operand").nodeValue

        XMLCodeLine(left).runAction(env)
        val computedLeft = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: left
        env.resetCachedValues()

        XMLCodeLine(right).runAction(env)
        val computedRight = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: right
        env.resetCachedValues()

        // Get the types of both the computed left and the computed right
        val leftType = computedLeft::class.simpleName
        val rightType = computedRight::class.simpleName

        // Try to parse the left and right as integers
        val leftInt = computedLeft.toString().toIntOrNull()
        val rightInt = computedRight.toString().toIntOrNull()

        // If both the left and right are integers, then we can do integer math
        if (leftInt != null && rightInt != null) {
            when (operator) {
                "+" -> env.cacheValue1 = leftInt + rightInt
                "-" -> env.cacheValue1 = leftInt - rightInt
                "*" -> env.cacheValue1 = leftInt * rightInt
                "/" -> env.cacheValue1 = leftInt / rightInt
                "%" -> env.cacheValue1 = leftInt % rightInt
                else -> throw Exception("Unknown operator $operator")
            }
        } else {
            // Otherwise, we can only do string concatenation
            when (operator) {
                "+" -> env.cacheValue1 = computedLeft.toString() + computedRight.toString()
                else -> throw Exception("Unknown operator $operator")
            }
        }
    }
}

class ParenthesisRunner: BaseRunner {
    override fun run(env: XmlEvironment, node: Node) {
        val value = node.firstChild
        XMLCodeLine(value).runAction(env)
        val computedValue = env.cacheValue1 ?: env.cacheValue2 ?: env.cacheValue3 ?: env.cacheValue4 ?: value
        env.resetCachedValues()

        env.cacheValue1 = computedValue
    }
}

fun addAllRunners(xmlEvironment: XmlEvironment) {
    xmlEvironment.functions[XmlActionMap.VARIABLE_DECLARE] = VariableDeclarationRunner()
    xmlEvironment.functions[XmlActionMap.INT_CONSTANT] = IntegerConstantRunner()
    xmlEvironment.functions[XmlActionMap.TELEMETRY_LOG_LINE] = TelemetryLogLineRunner()
    xmlEvironment.functions[XmlActionMap.VARIABLE_SET] = VariableSetRunner()
    xmlEvironment.functions[XmlActionMap.VARIABLE_USAGE] = VariableUsageRunner()
    xmlEvironment.functions[XmlActionMap.BINARY_EXPRESSION] = BinaryExpressionRunner()
    xmlEvironment.functions[XmlActionMap.PARENTHESES] = ParenthesisRunner()
}