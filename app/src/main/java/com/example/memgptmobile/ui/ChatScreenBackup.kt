//package com.example.memgptmobile.ui
//
//import android.os.Bundle
//import androidx.compose.foundation.layout.padding
//import androidx.compose.ui.unit.dp
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.memgptmobile.ui.theme.MemGPTMobileTheme
//import androidx.compose.material3.Text
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.runtime.*
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import kotlinx.coroutines.launch
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.navigation.NavController
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.memgptmobile.viewmodel.ChatViewModel
//import com.example.memgptmobile.viewmodel.SettingsViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//data class Message(val content: String, val isUser: Boolean)
//
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MemGPTMobileTheme {
//                ChatScreen()
//            }
//        }
//    }
//}
//
//@Composable
//fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
//    var currentMessage by remember { mutableStateOf("") }
//    val chatMessages = viewModel.chatMessages.collectAsState()
//
//    // Navigation controller
//    val navController = rememberNavController()
//    val settingsViewModel: SettingsViewModel = hiltViewModel()
//    val settingsRepository = settingsViewModel.settingsRepository
//    val chatViewModel: ChatViewModel = hiltViewModel()
//
//    NavHost(navController = navController, startDestination = "main") {
//        composable("main") {
//            MainScreen(viewModel = viewModel, navController = navController)
//        }
//        composable("settings") {
//            SettingsScreen(navController, settingsRepository)
//        }
//    }
//}
//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun MainScreen(viewModel: ChatViewModel, navController: NavController) {
//        val scope = rememberCoroutineScope()
//        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        val messages = viewModel.chatMessages.collectAsState()
//
//        ModalNavigationDrawer(
//            drawerState = drawerState,
//            drawerContent = {
//                Column(
//                    modifier = Modifier
//                        .background(MaterialTheme.colorScheme.surface)
//                        .padding(8.dp)
//                        .fillMaxHeight()
//                ) {
//                    Text(
//                        text = "Chat",
//                        modifier = Modifier
//                            .clickable {
//                                navController.navigate("main")
//                                scope.launch { drawerState.close() }
//                            }
//                            .padding(8.dp),
//                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
//                    )
//                    Text(
//                        text = "Settings",
//                        modifier = Modifier
//                            .clickable {
//                                navController.navigate("settings")
//                                scope.launch { drawerState.close() }
//                            }
//                            .padding(8.dp),
//                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
//                    )
//
//                    // Add more items here as needed
//                }
//            }
//        ) {
//            Scaffold(
//                topBar = {
//                    CenterAlignedTopAppBar(
//                        title = { Text("MemGPT") },
//                        navigationIcon = {
//                            // Hamburger menu icon
//                            IconButton(onClick = {
//                                // Toggle drawer state
//                                scope.launch {
//                                    if (drawerState.isClosed) {
//                                        drawerState.open() // Open drawer
//                                    } else {
//                                        drawerState.close() // Close drawer
//                                    }
//                                }
//                            }) {
//                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
//                            }
//                        },
//                        actions = {
//                            // Here you can add actions for the top bar
//                        }
//                    )
//                },
//            ) { innerPadding ->
//                MainContent(padding = innerPadding, messages = messages) { messageContent ->
//                    viewModel.sendMessage(messageContent)
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun MainContent(padding: PaddingValues, messages: State<List<Message>>, handleSend: (String) -> Unit) {
//        var text by remember { mutableStateOf("")}
//
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//        ) {
//            ChatMessagesList(messages = messages.value, modifier = Modifier.weight(1f))
//            SendMessageArea(text, onTextChange = { text = it }, onMessageSent = { message ->
//                handleSend(message)
//                text = "" // Reset text field after sending
//            })
//        }
//    }
//
//    @Composable
//    fun ChatMessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
//        LazyColumn(modifier = modifier) {
//            items<Any>(messages, key = { message ->
//                message.id
//            }) { message ->
//                MessageView(message)
//            }
//        }
//    }
//
//fun <LazyItemScope> items(count: List<Message>, key: (index: Int) -> Unit, itemContent: LazyItemScope.(index: Int) -> Unit) {
//
//}
//
//@Composable
//    fun MessageView(message: Int) {
//        Row(
//            modifier = Modifier
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
//        ) {
//            Box(
//                contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart,
//                modifier = Modifier
//                    .background(
//                        color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .padding(PaddingValues(horizontal = 8.dp, vertical = 8.dp))
//            ) {
//                Text(
//                    text = message.content,
//                    color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
//                )
//            }
//        }
//    }
//
//@Composable
//fun SendMessageArea(text: String, onTextChange: (String) -> Unit, onMessageSent: (String) -> Unit) {
//
//    // This Row will hold the text field and send button
//    Row(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        OutlinedTextField(
//            value = text,
//            onValueChange = onTextChange,
//            modifier = Modifier.weight(1f),
//            placeholder = { Text("Type a message") },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
//            keyboardActions = KeyboardActions(onSend = {
//                if (text.isNotBlank()) {
//                    onMessageSent(text)
//                }
//            })
//        )
//
//        IconButton(
//            onClick = {
//                if (text.isNotBlank()) {
//                    onMessageSent(text)
//                }
//            },
//            modifier = Modifier
//                .padding(start = 8.dp)
//        ) {
//            Icon(Icons.Filled.Send, contentDescription = "Send")
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MemGPTMobileTheme {
//        ChatScreen()
//    }
//}