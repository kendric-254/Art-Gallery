package com.example.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import coil.compose.rememberImagePainter
import com.example.gallery.ui.theme.GalleryTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        setContent {
            GalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthenticationScreen(
                        modifier = Modifier.padding(innerPadding),
                        onSignIn = { email, password -> signIn(email, password) },
                        onSignOut = { signOut() }
                    )
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in successful
                } else {
                    // Sign-in failed
                }
            }
    }

    private fun signOut() {
        auth.signOut()
    }
}

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    onSignIn: (String, String) -> Unit,
    onSignOut: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<FirebaseUser?>(FirebaseAuth.getInstance().currentUser) }

    val auth = FirebaseAuth.getInstance()

    DisposableEffect(auth) {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
        }
        auth.addAuthStateListener(authListener)
        onDispose { auth.removeAuthStateListener(authListener) }
    }

    Column(modifier = modifier.padding(16.dp)) {
        if (user == null) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onSignIn(email, password) }) {
                Text("Sign In")
            }
        } else {
            Text("Signed in as ${user?.email}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onSignOut() }) {
                Text("Sign Out")
            }
        }
    }
}

@Composable
fun GalleryScreen(modifier: Modifier = Modifier) {
    val imageUrls = listOf(
        "https://unsplash.com/photos/red-blue-and-white-flowers-5TK1F5VfdIk",
        "https://unsplash.com/photos/a-painting-of-a-castle-on-a-hill-sJr8LDyEf7k",
        "https://unsplash.com/photos/a-painting-on-the-ceiling-of-a-building-1rBg5YSi00c"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(imageUrls) { imageUrl ->
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        error(R.drawable.img) // Replace with your error drawablecustom image tu ndo nimeongeza
                        placeholder(R.drawable.img_1) // Replace with your loading drawable-nmeongeza just a random image for loading
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    GalleryTheme {
        AuthenticationScreen(onSignIn = { _, _ -> }, onSignOut = {})
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryScreenPreview() {
    GalleryTheme {
        GalleryScreen()
    }
}
