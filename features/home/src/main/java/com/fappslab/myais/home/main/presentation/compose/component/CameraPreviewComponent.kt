import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.fappslab.myais.arch.camerax.model.RatioType
import com.fappslab.myais.design.theme.PlutoTheme
import com.fappslab.myais.home.main.presentation.model.MainStateType

@Composable
internal fun CameraPreviewComponent(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    imageBitmap: Bitmap?,
    ratioType: RatioType,
    mainStateType: MainStateType,
    isShutterEffect: Boolean,
    onRestartCamera: () -> Unit,
    onShutdownCamera: () -> Unit,
) {
    val boxIntSize = remember {
        mutableStateOf(IntSize(width = 0, height = 0))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratioType.toRatio())
            .clip(RoundedCornerShape(PlutoTheme.radius.large))
            .background(Color.Black)
            .onGloballyPositioned { coordinates ->
                boxIntSize.value = coordinates.size
            }
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
    }
}

@Preview(showBackground = true)
@Composable
private fun CameraPreviewComponentPreview() {
    Box(
        modifier = Modifier.padding(PlutoTheme.dimen.dp16)
    ) {
        CameraPreviewComponent(
            previewView = PreviewView(LocalContext.current),
            imageBitmap = null,
            ratioType = RatioType.RATIO_16_9,
            mainStateType = MainStateType.Camera,
            isShutterEffect = false,
            onRestartCamera = {},
            onShutdownCamera = {},
        )
    }
}
