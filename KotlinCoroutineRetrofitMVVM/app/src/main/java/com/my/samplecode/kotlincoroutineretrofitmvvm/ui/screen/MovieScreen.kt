package com.my.samplecode.kotlincoroutineretrofitmvvm.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.samplecode.kotlincoroutineretrofitmvvm.vm.MainViewModel

@Composable
fun MovieScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    LazyColumn (
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp), //리스트 시작/끝의 상,하,좌,우 패딩
        verticalArrangement = Arrangement.spacedBy(4.dp), //item 간의 간격 (리스트 아이템간의 간격)
    ) {
        itemsIndexed(items = mainViewModel.movieList) { index, item ->
            Log.d("TEST", "index: $index") //보여지는 아이템들의 인덱스와 아이템
            MovieItem(movie = item)
        }
    }
}