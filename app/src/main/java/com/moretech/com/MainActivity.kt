package com.moretech.com

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.moretech.com.ui.theme.MoreTechTheme
import androidx.navigation.compose.composable
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.delay

// TODO
// data class User(val a: Int, @Optional val b: String = "42")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoreTechTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(resources, this)
                }
            }
        }
    }
}

@Composable
fun Navigation(resources: Resources, context: Context) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        composable("main_screen") {
            SignUp(navController = navController, resources = resources, context = context)
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(3000L)
        navController.navigate("main_screen")
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.spaces_logo),
            contentDescription = "Logo"
        )
    }
}


@Composable
fun SignUp(navController: NavController, resources: Resources, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getColor("#F5F9FF")),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.spaces_gray_background),
            contentDescription = "Background Spaces",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column (
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.egorka),
                contentDescription = "Egorka")
            Text(
                text = "Егорка",
                fontFamily = FontFamily(Font(R.font.geometria_bold)),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Привет! \n" +
                        "Я покажу на примерах, что инвестиции это просто",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.geometria)),
                fontSize = 18.sp
            )

            var nickname by remember{ mutableStateOf("Никнейм") }
            var age by remember{ mutableStateOf("Возраст") }
            var email by remember{ mutableStateOf("Почта") }
            var password by remember{ mutableStateOf("Пароль") }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(nickname) { nickname = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(age) { age = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(email) { email = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(password) { password = it }

            Spacer(modifier = Modifier.height(36.dp))
            AuthButton(text = "Зарегистрироваться") {
                val queue = Volley.newRequestQueue(context)
                //val url = resources.getString(R.string.server_ip);

                /*
                // Request a string response from the provided URL.
                val stringRequest = StringRequest(Request.Method.GET, url, { response ->
                    val obj = JSON.parse(MyModel.serializer(), response)
                    println(obj) // MyModel(a=42, b="42")

                },
                    {
                        textView.text = "That didn't work!"
                    })

                // Add the request to the RequestQueue.
                queue.add(stringRequest)
                */
            }

            Spacer(modifier = Modifier.height(20.dp))
            AuthButton(text = "Войти >") {

            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = null,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = {}
            ) {
                Text(
                    text = "Войти как гость",
                    fontFamily = FontFamily(Font(R.font.geometria)),
                    fontSize = 18.sp,
                    color = getColor("#757575")
                )
            }
        }
    }
}

@Composable
fun AuthButton(text: String, onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = getColor("#44AAFF")),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.geometria_bold)),
            fontSize = 18.sp,
            color = getColor("#F5F9FF")
        )
    }
}

@Composable
fun CardTextInput(nickname: String, onValueChange: (String) -> Unit) {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(45.dp),
        elevation = 3.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .background(color = getColor("#F5F9FF")),
            contentAlignment = Alignment.CenterStart) {
            BasicTextField(
                value = nickname,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.padding(15.dp, 1.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.geometria)),
                    color = getColor("#898D94")
                )
            )
        }
    }
}

fun getColor(colorString: String): Color {
    return Color(android.graphics.Color.parseColor(colorString))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoreTechTheme {
        // SignUp(navController = rememberNavController(), resources = null)
    }
}