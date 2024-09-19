package com.padawanbr.systemmanager.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.padawanbr.systemmanager.model.Item

@Composable
fun InfoItem(item: Item) {
  Row {
    Text(
      modifier = Modifier,
      text = "${item.key}: ",
      fontSize = 16.sp,
      color = Color.Black,
      fontWeight = FontWeight.Normal
    )
    Text(
      modifier = Modifier,
      text = item.value,
      fontSize = 16.sp,
      color = Color.Black,
      fontWeight = FontWeight.Normal
    )
  }
  HorizontalDivider()
}

@Preview
@Composable
private fun InfoItemPreview() {
  InfoItem(item = Item(key = "Key", value = "Value"))
}