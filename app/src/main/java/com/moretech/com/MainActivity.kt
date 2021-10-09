package com.moretech.com

import android.content.Context
import android.content.SharedPreferences
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
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Profile(val balance: Float, val current_difficult: Int)

@Serializable
data class User(val username: String, val password: String, val is_staff: Boolean, val profile: Profile)

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}

class CustomJsonObjectRequestBasicAuth(
    method: Int,
    url: String,
    jsonObject: JSONObject?,
    listener: Response.Listener<JSONObject>,
    errorListener: Response.ErrorListener,
    credentials: String
) :
    JsonObjectRequest(method, url, jsonObject, listener, errorListener) {

    private var mCredentials: String = credentials

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val credentials: String = "username:password"
        val auth = "Basic " + Base64.encodeToString(mCredentials.toByteArray(), Base64.NO_WRAP)
        headers["Authorization"] = auth
        return headers
    }
}


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

    companion object {
        val PREFS_NAME = "APP_PREFS"
    }
}

@Composable
fun MainScreen() {
    Text("Hello!")
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

        composable("sign_up") {
            SignUp(navController = navController, resources = resources, context = context)
        }

        composable("sign_in") {
            SignIn(navController = navController, resources = resources, context = context)
        }

        composable("main_screen") {
            MainScreen()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(3000L)
        navController.navigate("sign_up")
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
fun SignIn(navController: NavController, resources: Resources, context: Context) {
    Box(modifier = Modifier.fillMaxSize().background(color = getColor("#F5F9FF"))) {
        Image(
            painter = painterResource(id = R.drawable.spaces_gray_background),
            contentDescription = "Background Spaces",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(modifier = Modifier.padding(25.dp, 35.dp)) {
            Button(
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                onClick = { navController.popBackStack() },
                elevation = null
            ){
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back arrow",
                    modifier = Modifier.height(24.dp).width(24.dp),
                    alignment = Alignment.TopStart
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.egorka),
                    contentDescription = "Egorka"
                )
                Text(
                    text = "Егорка",
                    fontFamily = FontFamily(Font(R.font.geometria_bold)),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Привет! \n" + "Рад тебя снова видеть",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.geometria)),
                    fontSize = 18.sp
                )

                var nickname by remember { mutableStateOf("Никнейм") }
                var password by remember { mutableStateOf("Пароль") }

                Spacer(modifier = Modifier.height(24.dp))
                CardTextInput(nickname, KeyboardType.Email) { nickname = it }

                Spacer(modifier = Modifier.height(24.dp))
                CardTextInput(password, KeyboardType.Password) { password = it }

                Spacer(modifier = Modifier.height(36.dp))
                AuthButton(text = "Войти") {
                    login(nickname, password, context, navController)
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
                text = "Привет! \n" + "Я покажу на примерах, что инвестиции это просто",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.geometria)),
                fontSize = 18.sp
            )

            var nickname by remember { mutableStateOf("Никнейм") }
            var age by remember { mutableStateOf("Возраст") }
            var email by remember { mutableStateOf("Почта") }
            var password by remember { mutableStateOf("Пароль") }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(nickname, KeyboardType.Text) { nickname = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(age, KeyboardType.Number) { age = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(email, KeyboardType.Email) { email = it }

            Spacer(modifier = Modifier.height(24.dp))
            CardTextInput(password, KeyboardType.Password) { password = it }

            Spacer(modifier = Modifier.height(36.dp))
            AuthButton(text = "Зарегистрироваться") {
                val profile = Profile(100000.0f, 1)
                val user = User(nickname, password, false, profile)

                val gson = Gson()
                val json = JSONObject(gson.toJson(user))

                sendRequest(Request.Method.POST, context, "api/register/", json) {
                    val sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putString("username", nickname)
                    editor.putString("password", password)
                    editor.apply()

                    navController.navigate("main_screen")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            AuthButton(text = "Войти") {
                navController.navigate("sign_in")
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

fun login(nickname: String, password: String, context: Context, navController: NavController) {
    val profile = Profile(100000.0f, 1)
    val user = User(nickname, password, false, profile)

    val gson = Gson()
    val json = JSONObject(gson.toJson(user))

    sendRequest(Request.Method.GET, context, "api/me/", json) {
        val sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("username", nickname)
        editor.putString("password", password)
        editor.apply()

        navController.navigate("main_screen")
    }
}

fun sendRequest(
    method: Int, context: Context, path: String, json: JSONObject?, onSuccess: (JSONObject) -> Unit)
{
    val url = "http://188.120.253.55:8002/$path"

    val credentials =
        if (path == "api/register/") {
            "admin:admin"
        } else {
            val username: String?
            val password: String?

            if (json == null) {
                val sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)

                username = sharedPreferences.getString("username", null)
                password = sharedPreferences.getString("password", null)
            } else {
                username = json.get("username").toString()
                password = json.get("password").toString()
            }

            "$username:$password"
        }


    val request = CustomJsonObjectRequestBasicAuth(method, url, json,
        { response ->
            try {
                onSuccess(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("OK", "Parse exception: $e")
            }
        }, {
            Log.d("OK", "Volley error: $it")
            Log.d("OK", "Volley error: ${it.networkResponse.statusCode}")
            Toast.makeText(context, "Некорректный ввод", Toast.LENGTH_SHORT).show()
        }, credentials
    )

    VolleySingleton.getInstance(context).addToRequestQueue(request)
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
fun CardTextInput(hint: String, keyboardType: KeyboardType, onValueChange: (String) -> Unit) {
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
                value = hint,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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