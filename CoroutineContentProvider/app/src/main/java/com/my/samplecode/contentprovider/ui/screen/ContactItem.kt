package com.my.samplecode.contentprovider.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.my.samplecode.contentprovider.model.Contact
import com.my.samplecode.contentprovider.R

@Composable
fun ContactItem(
    modifier: Modifier,
    contact: Contact
) {
    Card(
        modifier = modifier
            .padding(8.dp, 4.dp) //부모뷰로부트의 상하좌우 패딩
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Surface() {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(10.dp, 10.dp)
                        .size(60.dp)
                        .fillMaxHeight(),
                    painter = rememberImagePainter(
                        data = contact.image ?: R.drawable.ic_photo,
                        builder = {
                            scale(coil.size.Scale.FILL)
                            placeholder(R.drawable.placeholder)
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = "주소록"
                )
                Text(
                    modifier = Modifier
                        .padding(10.dp, 10.dp),
                    text = contact.name
                )
                Text(
                    modifier = Modifier.padding(10.dp, 10.dp),
                    text = contact.number
                )
            }
        }
    }
}