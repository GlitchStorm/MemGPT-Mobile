package com.example.memgptmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memgptmobile.viewmodel.ChatViewModel
import com.example.memgptmobile.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, scope, chatViewModel, settingsViewModel)
        }
    ) {
        NavHost(navController = navController, startDestination = "chat") {
            composable(route = "chat") {
                ChatScreen(navController = navController, chatViewModel = chatViewModel, drawerState = drawerState, scope = scope)
            }
            composable(route = "settings") {
                SettingsScreen(navController = navController, settingsViewModel = settingsViewModel, drawerState = drawerState, scope = scope)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    chatViewModel: ChatViewModel,
    settingsViewModel: SettingsViewModel
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        DrawerItem("Chat", "chat", navController, drawerState, scope)
        DrawerItem("Settings", "settings", navController, drawerState, scope)
        DrawerItem("Auth", "auth", navController, drawerState, scope)
        // Add more items here as needed
    }
}

@Composable
fun DrawerItem(
    label: String,
    route: String,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onClickAction: (() -> Unit)? = null
) {
    Text(
        text = label,
        modifier = Modifier
            .clickable {
                navController.navigate(route)
                onClickAction?.invoke()
                scope.launch { drawerState.close() }
            }
            .padding(8.dp),
        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
    )
}