package com.padawanbr.systemmanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager

@Composable
fun CardManagerSection(
  modifier: Modifier = Modifier,
  manager: Manager
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(
        RoundedCornerShape(10)
      )
  ) {
    Column(
      Modifier
        .fillMaxWidth()
        .heightIn(128.dp)
        .padding(16.dp)
    ) {
      Row {
        Text(
          modifier = Modifier,
          text = manager.title,
          style = MaterialTheme.typography.headlineLarge,
          fontSize = 24.sp,
          color = Color.Black,
          fontWeight = FontWeight.Bold,
        )
      }
      Column {
        manager.items.forEach { item ->
          InfoItem(item)
        }
      }
    }
  }
}


@Preview
@Composable
private fun CardManagerItemPreview() {
  val managerItens = Manager(
    title = "CPU Info",
    listOf(
      Item(key = "Key", value = "Value"),
      Item(key = "Key", value = "Value"),
    )
  )
  CardManagerSection(manager = managerItens)
}