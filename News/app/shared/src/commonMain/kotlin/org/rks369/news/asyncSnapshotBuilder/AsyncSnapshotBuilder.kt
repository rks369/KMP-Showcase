package org.rks369.news.asyncSnapshotBuilder

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow

sealed interface AsyncSnapshot<out T> {
    data object Idle : AsyncSnapshot<Nothing>
    data object Loading : AsyncSnapshot<Nothing>
    data class Success<T>(val data: T) : AsyncSnapshot<T>
    data class Error(val message: String) : AsyncSnapshot<Nothing>
}

@Composable
fun <T> AsyncSnapshotBuilder(
    snapshot: AsyncSnapshot<T>,
    onRetry: () -> Unit = {},
    idle: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = { CircularProgressIndicator() },
    error: @Composable (String) -> Unit = { message ->
        Column {
            Text("Error: $message")
            Button(onClick = onRetry) { Text("Retry") }
        }
    },
    success: @Composable (T) -> Unit
) {
    when (snapshot) {
        is AsyncSnapshot.Idle -> idle()
        is AsyncSnapshot.Loading -> loading()
        is AsyncSnapshot.Error -> error(snapshot.message)
        is AsyncSnapshot.Success -> success(snapshot.data)
    }
}

suspend fun <T> MutableStateFlow<AsyncSnapshot<T>>.load(
    block: suspend () -> T
) {
    value = AsyncSnapshot.Loading
    value = try {
        AsyncSnapshot.Success(block())
    } catch (e: Exception) {
        AsyncSnapshot.Error(e.message ?: "Something went wrong")
    }
}