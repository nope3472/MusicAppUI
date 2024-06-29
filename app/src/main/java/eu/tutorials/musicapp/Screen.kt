package eu.tutorials.musicapp

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import eu.tutorials.musicapp.Screen.DrawerScreen.Account.dtitle
sealed class Bottom(val dtitle:String, val droute:String,@DrawableRes val icon: Int):Screen(dtitle,droute){

    object Home:Bottom("Home","home",R.drawable.baseline_music_video_24)

    object library:Bottom("Library","library",R.drawable.baseline_library_music_24)

    object Browse:Bottom("Browse","browse",R.drawable.baseline_search_24)

}




open class Screen(val title :String,val route:String) {

     sealed class DrawerScreen(val dtitle:String, val droute:String,@DrawableRes val icon: Int):Screen(dtitle,droute){
         object Account: DrawerScreen(
             dtitle = "Account",
             droute = "account",
             icon = R.drawable.baseline_account_circle_24
         )

         object Subscription: DrawerScreen(
             dtitle = "Subscription",
             droute = "subscribe",
             icon = R.drawable.baseline_subscriptions_24
         )

         object AddAccount: DrawerScreen(
             dtitle = "Add Account",
             droute = "add_account",
             icon = R.drawable.baseline_person_add_alt_1_24
         )
     }


}

val screendrawer= listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.AddAccount,
    Screen.DrawerScreen.Subscription
)

val bottomdrawer= listOf(
    Bottom.Home,
    Bottom.Browse,
    Bottom.library
)

