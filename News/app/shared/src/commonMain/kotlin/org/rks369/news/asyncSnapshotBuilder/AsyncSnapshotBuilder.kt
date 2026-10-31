package org.rks369.news.asyncSnapshotBuilder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.rks369.news.AppException
import org.rks369.news.toAppException

sealed interface AsyncSnapshot<out T> {
    data object Idle : AsyncSnapshot<Nothing>
    data object Loading : AsyncSnapshot<Nothing>
    data class Success<T>(val data: T) : AsyncSnapshot<T>
    data class Error(val error: AppException) : AsyncSnapshot<Nothing>
}

@Composable
fun <T> AsyncSnapshotBuilder(
    snapshot: AsyncSnapshot<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    idle: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = { CircularProgressIndicator() },
    error: @Composable (AppException) -> Unit = { appException ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(appException.title)
            appException.description?.let { description ->
                Text(description)
            }
            Button(onClick = onRetry) { Text("Retry") }
        }
    },
    success: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = 400.dp),
        contentAlignment = Alignment.Center
    ) {
        when (snapshot) {
            is AsyncSnapshot.Idle -> idle()
            is AsyncSnapshot.Loading -> loading()
            is AsyncSnapshot.Error -> error(snapshot.error)
            is AsyncSnapshot.Success -> success(snapshot.data)
        }
    }
}

suspend fun <T> MutableStateFlow<AsyncSnapshot<T>>.load(
    block: suspend () -> T
) {
    value = AsyncSnapshot.Loading
    value = try {
        AsyncSnapshot.Success(block())
    } catch (e: Exception) {
        AsyncSnapshot.Error(e.toAppException())
    }
}