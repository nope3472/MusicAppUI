package eu.tutorials.musicapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val currentscreen : MutableState<Screen> = mutableStateOf(Screen.DrawerScreen.AddAccount)

    val current:MutableState<Screen>
    get() = currentscreen

    fun setscreen(screen: Screen){
        currentscreen.value=screen
    }

}