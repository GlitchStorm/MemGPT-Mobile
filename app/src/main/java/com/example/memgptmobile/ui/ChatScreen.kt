package com.example.memgptmobile.ui

import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.memgptmobile.ui.theme.MemGPTMobileTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.memgptmobile.viewmodel.ChatViewModel
import com.example.memgptmobile.viewmodel.MessageType
import com.example.memgptmobile.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

data class Message(val id: String, val content: String, val type: MessageType)

val LightBlue = Color(0xFFD7E8FA)
val DarkBlue = Color(0xFF2C3E50)
val LightGray = Color(0xFFF2F2F2)
val DarkGray = Color(0xFF333333)
val Green = Color(0xFF27AE60)
val Red = Color(0xFFC0392B)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemGPTMobileTheme {
                AppNavigation()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, chatViewModel: ChatViewModel, drawerState: DrawerState, scope: CoroutineScope) {
    var currentMessage by remember { mutableStateOf("") }
    val chatMessages = chatViewModel.chatMessages.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MemGPT Chat") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Display the list of chat messages
            ChatMessagesList(messages = chatMessages, modifier = Modifier.weight(1f).fillMaxWidth())

            // Area for sending new messages
            SendMessageArea(
                text = currentMessage,
                onTextChange = { newText -> currentMessage = newText },
                onMessageSent = { message ->
                    chatViewModel.sendMessage(message)
                    currentMessage = "" // Clear the input field after sending a message
                }
            )
        }
    }
}

@Composable
fun  ChatMessagesList(messages: State<List<com.example.memgptmobile.viewmodel.Message>>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier) {
        items(items = messages.value, key = {message ->
            message.id
        }) { message ->
            MessageView(message)
        }
    }
}

//fun <LazyItemScope> items(count: List<Message>, key: (index: Int) -> Unit, itemContent: LazyItemScope.(index: Int) -> Unit) {

//}

@Composable
fun MessageView(message: com.example.memgptmobile.viewmodel.Message){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = when(message.type){
            MessageType.USER -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(
                    color = when (message.type) {
                        MessageType.USER -> DarkBlue
                        MessageType.AI -> Green
                        MessageType.AI_THOUGHT -> LightBlue
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            color = when (message.type) {
                MessageType.USER -> Color.White
                MessageType.AI -> Color.White
                else -> Color.DarkGray
            }
        )
    }
}

@Composable
fun SendMessageArea(text: String, onTextChange: (String) -> Unit, onMessageSent: (String) -> Unit) {

    // This Row will hold the text field and send button
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                if (text.isNotBlank()) {
                    onMessageSent(text)
                }
            })
        )

        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onMessageSent(text)
                }
            },
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Icon(Icons.Filled.Send, contentDescription = "Send")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MemGPTMobileTheme {
//        ChatScreen()
//    }
//}