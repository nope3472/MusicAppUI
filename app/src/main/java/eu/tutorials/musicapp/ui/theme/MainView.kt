package eu.tutorials.musicapp.ui.theme

import Library
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.tutorials.musicapp.AccountDialog
import eu.tutorials.musicapp.AccountView
import eu.tutorials.musicapp.Bottom
import eu.tutorials.musicapp.BrowseScreen
import eu.tutorials.musicapp.Home
import eu.tutorials.musicapp.MainViewModel
import eu.tutorials.musicapp.R
import eu.tutorials.musicapp.Screen
import eu.tutorials.musicapp.Subscription
import eu.tutorials.musicapp.bottomdrawer

import eu.tutorials.musicapp.screendrawer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val viewModel: MainViewModel = viewModel()
    val SheetFullScreen by remember { mutableStateOf(false) }

    val modifier = if (SheetFullScreen) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxWidth()
    }
    val currentScreen = remember {
        viewModel.current.value
    }
    val title = remember { mutableStateOf(currentScreen.title) }
    val dialogOpen = remember {
        mutableStateOf(false)
    }
    val modalSheetState= androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it!= ModalBottomSheetValue.HalfExpanded })
    val roundedCornerShape= if(SheetFullScreen)0.dp else 12.dp

    val bottombar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen is Bottom) {
            BottomNavigation(Modifier.wrapContentSize()) {
                bottomdrawer.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.droute,
                        onClick = { controller.navigate(item.droute)
                                  title.value=item.dtitle},

                        icon = {
                            Icon(painter = painterResource(id = item.icon), contentDescription = item.dtitle)
                        },
                        label = { Text(text = item.dtitle)},
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(

        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topEnd = roundedCornerShape, topStart = roundedCornerShape),
        sheetContent = {
        MoreBottomSheet(modifier=Modifier)


    }) {

        Scaffold(
            bottomBar = bottombar,
            topBar = {
                TopAppBar(
                    title = { Text(text = title.value) },
                    actions = {
                              IconButton(onClick = {
                                  scope.launch(){
                                      if(modalSheetState.isVisible){
                                          modalSheetState.hide()
                                      }
                                      else{
                                          modalSheetState.show()
                                      }
                                  }
                              }) {
                              Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                              }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                        }
                    })
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screendrawer) { item ->
                        DrawerItem(item = item, selected = currentRoute == item.route) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if (item.route == "add_account") {
                                dialogOpen.value = true
                            } else {
                                controller.navigate(item.route)
                                title.value = item.title
                            }
                        }
                    }
                }
            }

        ) {
            Navigation(navController = controller, viewModel = viewModel, paddingValues = it)
            AccountDialog(dialogopen = dialogOpen)
        }
    }
    }




@Composable
fun MoreBottomSheet(modifier: Modifier) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(MaterialTheme.colors.primarySurface)){
       Column(modifier=Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
          Row(modifier = Modifier.padding(16.dp)) {
               Icon(modifier=Modifier.padding(8.dp),painter = painterResource(id = R.drawable.baseline_settings_24) , contentDescription ="" )
              Text(text = "Settings", fontSize = 20.sp, color = Color.White)
          }
           Row(modifier = Modifier.padding(16.dp)) {
               Icon(modifier=Modifier.padding(8.dp),painter = painterResource(id = R.drawable.baseline_share_24) , contentDescription ="" )
              Text(text = "Share", fontSize = 20.sp, color = Color.White)
          }
           Row(modifier = Modifier.padding(16.dp)) {
               Icon(modifier=Modifier.padding(8.dp),painter = painterResource(id = R.drawable.baseline_help_outline_24) , contentDescription ="" )
              Text(text = "Help", fontSize = 20.sp, color = Color.White)
          }

       }
    }

}

@Composable
fun DrawerItem(item: Screen.DrawerScreen, selected: Boolean, onDrawerItemClicked: () -> Unit) {
    val backgroundColor = if (selected) Color.DarkGray else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(backgroundColor)
            .clickable { onDrawerItemClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            modifier = Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.title,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, paddingValues: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }
        composable(Bottom.Home.droute){
             Home()
        }
        composable(Bottom.library.droute){
            Library()
        }
        composable(Bottom.Browse.droute){
             BrowseScreen()
        }

        composable(Screen.DrawerScreen.Subscription.route) {
            Subscription()
        }
    }
}
