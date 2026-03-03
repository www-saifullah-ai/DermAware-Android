package ai.saifullah.dermaware.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Handles converting images from URIs or file paths into the exact format
 * that the TFLite model expects:
 *   - Size: 224 x 224 pixels
 *   - Color: RGB (3 channels)
 *   - Values: normalized to [0, 1] as Float32
 *   - Layout: NHWC (batch=1, height=224, width=224, channels=3)
 *
 * This preprocessing MUST match exactly what was used during model training.
 */
class ImagePreprocessor {

    companion object {
        // The model expects 224x224 images — matches MobileNetV2 input size
        const val MODEL_INPUT_SIZE = 224

        // 4 bytes per float, 3 channels (R, G, B), 224x224 pixels
        private const val BYTE_BUFFER_SIZE = 4 * MODEL_INPUT_SIZE * MODEL_INPUT_SIZE * 3
    }

    /**
     * Load an image from a URI (from gallery or camera) and convert it to
     * a ByteBuffer ready for TFLite inference.
     *
     * @param context Android context needed to open the URI
     * @param imageUri URI of the image to process
     * @return ByteBuffer containing the preprocessed image data
     */
    fun preprocessUri(context: Context, imageUri: Uri): ByteBuffer {
        // Step 1: Load the bitmap from the URI
        val rawBitmap = loadBitmapFromUri(context, imageUri)

        // Step 2: Fix orientation — camera photos often have wrong EXIF rotation
        val orientedBitmap = fixOrientation(context, imageUri, rawBitmap)

        // Step 3: Resize and convert to ByteBuffer
        return bitmapToByteBuffer(orientedBitmap)
    }

    /**
     * Convert a Bitmap directly to a ByteBuffer.
     * Used when we already have the bitmap (e.g., from CameraX preview frame).
     */
    fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
        return bitmapToByteBuffer(bitmap)
    }

    /**
     * Load a Bitmap from a content URI.
     * Uses the modern ImageDecoder on Android 9+ or BitmapFactory on older devices.
     */
    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }
        } else {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            } ?: throw IOException("Cannot open image URI: $uri")
        }
    }

    /**
     * Read the EXIF rotation data from a photo and rotate the bitmap accordingly.
     * Camera photos often have rotation metadata instead of being pre-rotated.
     * If we don't fix this, photos will appear sideways to the model.
     */
    private fun fixOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val rotation = try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        } catch (e: Exception) {
            0f // If we can't read EXIF, assume no rotation needed
        }

        if (rotation == 0f) return bitmap

        val matrix = Matrix().apply { postRotate(rotation) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Core preprocessing: resize the bitmap to 224x224 and convert pixel values
     * to a normalized Float32 ByteBuffer in NHWC format.
     *
     * Each pixel's RGB values are divided by 255.0f to normalize from [0,255] to [0,1].
     * This matches the MobileNetV2 preprocessing used during training.
     */
    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // Resize to 224x224
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, true)

        // Allocate a ByteBuffer: 4 bytes (float) × 3 channels × 224 × 224
        val byteBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.rewind()

        // Extract each pixel and write R, G, B values as normalized floats
        val pixels = IntArray(MODEL_INPUT_SIZE * MODEL_INPUT_SIZE)
        resizedBitmap.getPixels(pixels, 0, MODEL_INPUT_SIZE, 0, 0, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE)

        for (pixel in pixels) {
            // Extract R, G, B from the packed int (ARGB format)
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        byteBuffer.rewind()
        return byteBuffer
    }
}
