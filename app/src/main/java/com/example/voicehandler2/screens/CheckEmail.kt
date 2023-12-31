package com.example.voicehandler2.screens

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.voicehandler2.MainViewModel
import com.example.voicehandler2.MainViewModelFactory
import com.example.voicehandler2.navigation.NavRoute
import com.example.voicehandler2.utils.Constants
import com.example.voicehandler2.utils.DB_TYPE
import com.example.voicehandler2.utils.LOGIN
import com.example.voicehandler2.utils.PASSWORD
import com.example.voicehandler2.utils.TYPE_FIREBASE
import com.example.voicehandler2.utils.VERIFICATION_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage




fun sendEmailInBackground(toEmail: String, subject: String, messageText: String,viewModel: MainViewModel) {
    viewModel.viewModelScope.launch(Dispatchers.IO) {
        sendEmail2(toEmail, subject, messageText)
    }
}
fun sendEmail2(toEmail: String, subject: String, messageText: String) {
    val fromEmail = "vikden.99@yandex.ru" // Замените на ваш адрес Яндекс.Почты
    val password = "Heronero45" // Замените на пароль от вашей почты

    val properties = Properties()
    properties["mail.smtp.host"] = "smtp.yandex.ru" // SMTP-сервер Яндекса
    properties["mail.smtp.socketFactory.port"] = "465"
    properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.port"] = "465"

    val session = Session.getInstance(properties,
        object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(fromEmail, password)
            }
        })


    try {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(fromEmail))
        message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
        message.subject = subject
        message.setText(messageText)
        Log.d("checkData", "Session5: ${session}")

        Transport.send(message)
        Log.d("checkData", "Session6: ${session}")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CheckEmailScreen(navController: NavHostController, viewModel: MainViewModel) {
    var verification_code by remember { mutableStateOf(Constants.Keys.EMPTY) }
    var check by remember { mutableStateOf(Constants.Keys.EMPTY) }
    //val mAuth = FirebaseAuth.getInstance()
    //val currentUser = mAuth.currentUser


    // Используйте BackHandler для обработки нажатия кнопки "Назад"
//    BackHandler(
//        enabled = true,
//        onBack = {
//            currentUser?.delete()
//        }
//    )

    //var email = currentUser?.email
    var email = LOGIN
    if (email == null){
        email = "Null"
    }

    val email3 = "vikden.99@mail.ru"
    val toEmail = email3 // Замените на адрес получателя
    val subject = "Verification code (REGISTRATION)"
    val random = java.util.Random()
    val sixDigitNumber = 100000 + random.nextInt(900000)
    val messageText = "Почта: $LOGIN\nПароль: $PASSWORD"

    val message = "Аккаунт успешно создан!" // Замените это сообщение на то, которое вам нужно показать
    val message2 = "Аккаунт не был создан (Проверьте правильность почты и пароля)!"
    val message3 = "Неверный  VERIFICATION_CODE"
    val duration = Toast.LENGTH_LONG // Вы можете изменить длительность уведомления
    val toast = Toast.makeText(LocalContext.current, message, duration)
    val toast2 = Toast.makeText(LocalContext.current, message2, duration)
    val toast3 = Toast.makeText(LocalContext.current, message3, duration)
    toast.setGravity(Gravity.TOP, 0, 0)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 32.dp)
        ) {
            Text(
                text = Constants.Keys.CHECK_VERIFICATION_CODE,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Email: $email",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)

            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = verification_code,
                    onValueChange = { verification_code = it },
                    label = { Text(text = Constants.Keys.VERIFICATION_CODE) },
                    isError = verification_code.isEmpty() ,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(0.6f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .height(46.dp) // Установите желаемую высоту кнопки
                        .fillMaxWidth(0.4f),
                    onClick = {
                        check = "No empty"
                        sendEmailInBackground(toEmail, subject, messageText,viewModel)
                        sendEmailInBackground(toEmail, subject, messageText,viewModel)
                    },
                    enabled = check.isEmpty()
                ) {
                    Text(text = Constants.Keys.SEND)
                }

            }
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = verification_code.isNotEmpty()
            ) {
                Text(text = Constants.Keys.REGISTRATION,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp)
                    .size(100.dp, 40.dp),
                onClick = {
                    navController.navigate(NavRoute.LogReg.route)
                    {
                        popUpTo(NavRoute.LogReg.route) {
                            inclusive = true
                        }
                    }
                },
            ) {
                Text(text = Constants.Keys.BACK)
            }
        }
    }
}