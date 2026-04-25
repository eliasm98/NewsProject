package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.data.Screens
import com.example.newsapp.screens.homeScreen.HomeScreen
import com.example.newsapp.screens.loginRegisterScreens.LoginScreen
import com.example.newsapp.screens.loginRegisterScreens.RegisterScreen
import com.example.newsapp.screens.profileScreen.ProfileScreen
import com.example.newsapp.screens.newsScreen.NewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                //remember the current screen and the previous screen (with .popBackState)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val screensWithoutBottomBar = listOf(
                    Screens.Login.name,
                    Screens.Register.name
                )

                Scaffold(
                    bottomBar = {
                        if (currentRoute !in screensWithoutBottomBar) {
                            NavigationBar() {

                                NavigationBarItem(
                                    selected = currentRoute == "News",
                                    onClick = { navController.navigate(Screens.News.name) },
                                    icon = { Text("News") }
                                )

                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = { navController.navigate(Screens.Home.name) },
                                    icon = { Text("Home") } // using text instead of icon
                                )

                                NavigationBarItem(
                                    selected = currentRoute == "Profile",
                                    onClick = { navController.navigate(Screens.Profile.name) },
                                    icon = { Text("Profile") }
                                )
                            }
                        }
                    }

                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Login.name,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                    {
                        composable(route = Screens.Home.name) {
                            HomeScreen(navController = navController)
                        }
                        composable(route = Screens.Profile.name) {
                            ProfileScreen(navController = navController)
                        }
                        composable(route = Screens.News.name) {
                            NewsScreen(navController = navController)
                        }
                        composable (route = Screens.Login.name){
                            LoginScreen(navController = navController)
                        }
                        composable (route = Screens.Register.name){
                            RegisterScreen(navController = navController)
                        }

                    }

                }
            }
        }
    }
}
