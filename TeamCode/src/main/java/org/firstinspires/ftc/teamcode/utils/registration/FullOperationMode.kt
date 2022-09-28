package org.firstinspires.ftc.teamcode.utils.registration

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

data class FullOperationMode(val meta: OpModeMeta, val clazz: Class<out OperationMode>)
