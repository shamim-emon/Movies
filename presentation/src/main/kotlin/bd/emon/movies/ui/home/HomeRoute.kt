package bd.emon.movies.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import bd.emon.movies.viewModels.HomeViewModel

@Composable
fun HomeRoute(){
    val viewModel : HomeViewModel = hiltViewModel()
    HomeScreen()
}