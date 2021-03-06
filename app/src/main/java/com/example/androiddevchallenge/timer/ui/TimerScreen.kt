package com.example.androiddevchallenge.timer

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androiddevchallenge.timer.ui.BackgroundGradient
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.timer.ext.formatDuration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun TimerScreen(timerViewModel: CountdownTimerViewModel, navController: NavController) {
    val viewStateObserver = timerViewModel.viewState.observeAsState(initial = TimerModel())
    val state = viewStateObserver.value
    val context = LocalContext.current

    LaunchedEffect(key1 = navController) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm_2)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        timerViewModel.viewEffectChannel.receiveAsFlow().collect { viewEffect ->
            when (viewEffect){
                TimerViewEffect.TimerFinished -> {
                    startVibrating(vibrator = vibrator)
                    playSound(mediaPlayer)
                }
                TimerViewEffect.TimerReset -> {
                    stopVibrating(vibrator = vibrator)
                    stopPlayingSound(mediaPlayer = mediaPlayer)
                }
            }
        }
    }
    Surface(color = MaterialTheme.colors.background) {
        BackgroundGradient()
        Column(modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
            when (state.timerViewState) {
                TimerViewState.RUNNING -> {
                    TimerText(timerText = state.durationRemaining.formatDuration())
                }
                TimerViewState.IDLE -> {
                    TimerText(timerText = state.timerDuration.formatDuration())
                }
                TimerViewState.FINISHED -> {
                    FlashingTimerText(timerText = "00:00")
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            TimerButton(state.timerViewState, onButtonClick = { timerViewModel.timerButtonPressed() })
        }
    }
}

fun stopVibrating(vibrator: Vibrator) {
    vibrator.cancel()
}

fun startVibrating(vibrator: Vibrator) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibratePattern = longArrayOf(0, 400, 800, 600, 800, 800, 800, 1000)
        val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)
        vibrator.vibrate(VibrationEffect.createWaveform(vibratePattern, amplitudes, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        //deprecated in API 26
        @Suppress("DEPRECATION")
        vibrator.vibrate(500)
    }
}

fun playSound(mediaPlayer: MediaPlayer) {
    mediaPlayer.setOnCompletionListener {
        mediaPlayer.release()
    }
    mediaPlayer.start()
}

fun stopPlayingSound(mediaPlayer: MediaPlayer){
    mediaPlayer.stop()
}

@Composable
fun FlashingTimerText(timerText: String){
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    TimerText(modifier = Modifier.alpha(alpha),
        timerText = timerText)
}

@Composable
fun TimerText(modifier: Modifier = Modifier, timerText: String) {
    Text(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        style = MaterialTheme.typography.h1,
        text = timerText,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TimerButton(timerViewState: TimerViewState, onButtonClick: () -> Unit){
    Button(
        elevation = null,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = {
            onButtonClick()
        }) {
        val text = when (timerViewState){
            TimerViewState.IDLE -> {
                stringResource(id = R.string.start_timer)
            }
            TimerViewState.RUNNING -> {
                stringResource(id = R.string.stop_timer)
            }
            TimerViewState.FINISHED -> {
                stringResource(id = R.string.restart_timer)
            }
        }
        Text(text = text)
    }
}