package com.my.samplecode.contentprovider.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.samplecode.contentprovider.vm.MainViewModel

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp), //리스트 시작/끝의 상,하,좌,우 패딩
        verticalArrangement = Arrangement.spacedBy(4.dp), //item 간의 간격 (리스트 아이템간의 간격)
    ) {
        itemsIndexed(items = viewModel.contactsList) { _, item ->
            ContactItem(
                modifier = modifier,
                contact = item
            )
        }
    }
}