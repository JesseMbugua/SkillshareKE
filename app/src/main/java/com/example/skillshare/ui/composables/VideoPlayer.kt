package com.example.skillshare.ui.composables

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, videoUrl: String) {
    val context = LocalContext.current

    // Create and remember an ExoPlayer instance, keyed on the videoUrl.
    // This will create a new player whenever the URL changes.
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            // Add a listener to log errors
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.e("VideoPlayer", "ExoPlayer Error: ", error)
                }
            })
            prepare()
            playWhenReady = true // Start playing automatically
        }
    }

    // Dispose of the player when the composable is removed from the screen
    // or when the exoPlayer instance changes.
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed the ExoPlayer's PlayerView.
    // The update block will be called when the view is first created and
    // anytime the exoPlayer instance changes.
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it)
        },
        update = { playerView ->
            playerView.player = exoPlayer
        }
    )
}
