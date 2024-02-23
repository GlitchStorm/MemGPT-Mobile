package com.example.memgptmobile.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.memgptmobile.ui.theme.MemGPTMobileTheme
import com.example.memgptmobile.viewmodel.ChatViewModel
import com.example.memgptmobile.viewmodel.MessageType
import com.example.memgptmobile.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
fun ChatScreen(
    chatViewModel: ChatViewModel,
    settingsViewModel: SettingsViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
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
            ChatMessagesList(
                messages = chatMessages,
                settingsViewModel.getShowInternalMonologue(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

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
fun ChatMessagesList(
    messages: State<List<com.example.memgptmobile.viewmodel.Message>>,
    showInternalMonologue: Boolean,
    modifier: Modifier = Modifier
) {
    val filteredMessages = remember(messages.value, showInternalMonologue) {
        if (showInternalMonologue) {
            messages.value
        } else {
            messages.value.filter { it.type != MessageType.AI_THOUGHT }
        }
    }
    LazyColumn(modifier = modifier) {
        items(items = filteredMessages, key = { message ->
            message.id
        }) { message ->
            MessageView(message)
        }
    }
}

//fun <LazyItemScope> items(count: List<Message>, key: (index: Int) -> Unit, itemContent: LazyItemScope.(index: Int) -> Unit) {

//}

@Composable
fun MessageView(message: com.example.memgptmobile.viewmodel.Message) {
    val maxWidthPercent = 0.8f // Bubbles will take up to 80% of the screen width
    val alignment = when (message.type) {
        MessageType.USER -> Alignment.CenterEnd
        else -> Alignment.CenterStart
    }
    val backgroundColor = when (message.type) {
        MessageType.USER -> DarkBlue
        MessageType.AI -> Green
        MessageType.AI_THOUGHT -> Color(0xFFD7E8FA) // A softer light blue
    }
    val textColor = when (message.type) {
        MessageType.USER -> Color.White
        MessageType.AI -> Color.White
        MessageType.AI_THOUGHT -> Color.DarkGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp), // Reduced vertical padding
        contentAlignment = alignment
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp) // Slightly larger corner rounding
                )
                .padding(12.dp) // Increased internal padding for a less blocky look
                .widthIn(max = (maxWidthPercent * LocalConfiguration.current.screenWidthDp).dp), // Max width constraint
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
        // Tail implementation can be added here if desired
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