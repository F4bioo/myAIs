import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.main.presentation.model.MainStateType

@Composable
internal fun CameraPreviewComponent(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    imageBitmap: Bitmap?,
    mainStateType: MainStateType,
    isShutterEffect: Boolean,
    onRestartCamera: () -> Unit,
    onShutdownCamera: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (mainStateType) {
            MainStateType.Analyze,
            MainStateType.Preview -> {
                AsyncImage(
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                    model = imageBitmap,
                    contentDescription = null,
                    onSuccess = {
                        onShutdownCamera()
                    }
                )
            }

            MainStateType.Camera -> {
                onRestartCamera()
                AndroidView(
                    modifier = Modifier.matchParentSize(),
                    factory = { previewView }
                )
            }
        }
        if (isShutterEffect) Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(PlutoTheme.opacity.frosted))
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CameraPreviewComponentPreview() {
    CameraPreviewComponent(
        previewView = PreviewView(LocalContext.current),
        imageBitmap = null,
        mainStateType = MainStateType.Camera,
        isShutterEffect = false,
        onRestartCamera = {},
        onShutdownCamera = {},
        content = {}
    )
}
