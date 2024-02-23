package com.example.memgptmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.memgptmobile.repository.SettingsRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.memgptmobile.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel, drawerState: DrawerState, scope: CoroutineScope) {
    var ipAddress: String by remember { mutableStateOf("")}
    var port: String by remember { mutableStateOf("")}
    var userID: String by remember { mutableStateOf("")}
    var agentID: String by remember { mutableStateOf("")}
    var bearerToken: String by remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        ipAddress = settingsViewModel.settingsRepository.getIpAddress()
        port = settingsViewModel.settingsRepository.getPort()
        userID = settingsViewModel.settingsRepository.getUserID()
        agentID = settingsViewModel.settingsRepository.getAgentID()
        bearerToken = settingsViewModel.settingsRepository.getBearerToken()
    }
    // Scaffold offers a consistent structure for topBar, content, floatingActionButton, etc.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Chat",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("main")
                            scope.launch { drawerState.close() }
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = "Settings",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("settings")
                            scope.launch { drawerState.close() }
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )

                // Add more items here as needed
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    // or CenterAlignedTopAppBar or LargeTopAppBar based on your design
                    title = { Text("Settings") },
                    navigationIcon = {
                        // Hamburger menu icon
                        IconButton(onClick = {
                            // Toggle drawer state
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open() // Open drawer
                                } else {
                                    drawerState.close() // Close drawer
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp) // Additional padding for the Column content
            ) {
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("IP Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = userID,
                    onValueChange = { userID = it },
                    label = { Text("User ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = agentID,
                    onValueChange = { agentID = it },
                    label = { Text("Agent ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = bearerToken,
                    onValueChange = { bearerToken = it },
                    label = { Text("Bearer Token") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        settingsViewModel.settingsRepository.saveApiSettings(ipAddress, port)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
}