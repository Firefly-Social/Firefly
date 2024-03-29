package social.firefly.common.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

interface PainterFactory {
    @Composable
    fun build(): Painter
}

private data class Resource(
    @param:DrawableRes val resId: Int,
) : PainterFactory {
    @Composable
    override fun build(): Painter = painterResource(resId)
}

fun painterResourceFactory(
    @DrawableRes resId: Int,
): PainterFactory = Resource(resId)
