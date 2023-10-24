package com.example.voicehandler2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voicehandler2.MainViewModel
import com.example.voicehandler2.screens.CheckEmailLoginScreen


import com.example.voicehandler2.screens.CheckEmailScreen
import com.example.voicehandler2.screens.LogInScreen
import com.example.voicehandler2.screens.LogRegScreen
import com.example.voicehandler2.screens.RegistrationScreen
import com.example.voicehandler2.screens.StartScreen
import com.example.voicehandler2.utils.Constants


sealed class NavRoute(val route:String){
    object Start: NavRoute(Constants.Screens.START_SCREEN)
    object Main: NavRoute(Constants.Screens.MAIN_SCREEN)
    object Add: NavRoute(Constants.Screens.ADD_SCREEN)
    object Note: NavRoute(Constants.Screens.NOTE_SCREEN)
    object LogReg: NavRoute(Constants.Screens.LOGREG_SCREEN)
    object Registration: NavRoute(Constants.Screens.REGISTRATION_SCREEN)
    object LogIn: NavRoute(Constants.Screens.LOGIN_SCREEN)
    object CheckEmail: NavRoute(Constants.Screens.CHECK_EMAIL_SCREEN)
    object CheckEmailLogin: NavRoute(Constants.Screens.CHECK_EMAIL_LOGIN_SCREEN)
}

@Composable
fun NotesNavHost(mViewModel: MainViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoute.Start.route){
        composable(NavRoute.CheckEmailLogin.route){ CheckEmailLoginScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.CheckEmail.route){ CheckEmailScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.LogIn.route){ LogInScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.Registration.route){ RegistrationScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.LogReg.route){ LogRegScreen(navController = navController, viewModel = mViewModel)}
        composable(NavRoute.Start.route){ StartScreen(navController = navController, viewModel = mViewModel)}
    }
}

