package social.firefly.core.image

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache

@Suppress("MagicNumber")
object EmojiImageLoader {
    private var _imageLoader: ImageLoader? = null
    fun imageLoader(context: Context): ImageLoader {
        _imageLoader?.let { return it }
        _imageLoader = ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("emoji_image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
        return _imageLoader!!
    }
}