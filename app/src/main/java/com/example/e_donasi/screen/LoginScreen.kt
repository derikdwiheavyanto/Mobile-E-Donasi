package com.example.e_donasi.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e_donasi.model.request.LoginRequest
import com.example.e_donasi.navigation.Screen
import kotlinx.coroutines.launch
import com.example.e_donasi.R
import com.example.e_donasi.model.viewModel.AuthViewModel
import com.example.e_donasi.utils.PrefrenceManager


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {

    var valueEmail by remember { mutableStateOf("") }
    var valuePassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FDFB))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_login),
            contentDescription = "Ilustrasi Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome back!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF066E6D)
            )
        )
        Text(
            text = "Enter your credentials to continue.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = valueEmail,
            onValueChange = {
                valueEmail = it
                emailError = false
            },
            isError = emailError,
            placeholder = { Text("Username") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        if (emailError) {
            Text(
                text = "Email tidak boleh kosong",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = valuePassword,
            onValueChange = {
                valuePassword = it
                passwordError = false
            },
            isError = passwordError,
            placeholder = { Text("Password") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        if (passwordError) {
            Text(
                text = "Password tidak boleh kosong",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot Password?",
            color = Color(0xFF066E6D),
            modifier = Modifier
                .align(Alignment.End)
                .clickable { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                emailError = valueEmail.isBlank()
                passwordError = valuePassword.isBlank()

                if (!emailError && !passwordError) {
                    coroutineScope.launch {

                        val loginRequest = LoginRequest(
                            username = valueEmail,
                            password = valuePassword
                        )

                        viewModel.login(loginRequest, context)
                    }
                }

            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF066E6D))
        ) {
            Text(text = "Log in", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "By logging, you are agreeing with our",
            color = Color.Gray,
            fontSize = 12.sp
        )
        Row {
            Text(
                text = "Terms of Use",
                fontSize = 12.sp,
                color = Color(0xFF066E6D),
                modifier = Modifier.clickable { }
            )
            Text(text = " and ", fontSize = 12.sp, color = Color.Gray)
            Text(
                text = "Privacy Policy",
                fontSize = 12.sp,
                color = Color(0xFF066E6D),
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Don't have an account?", color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Register",
                color = Color(0xFF066E6D),
                fontSize = 13.sp,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
    }

    // Loading dialog
    if (isLoading) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Loading") },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Sedang login...")
                }
            }
        )
    }

    // Navigasi saat login sukses
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            val role = PrefrenceManager.getUserRole(context)

            if (role == "pengurus"){
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }

            viewModel.resetState()
        }
    }

    // Tampilkan pesan error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }
}
