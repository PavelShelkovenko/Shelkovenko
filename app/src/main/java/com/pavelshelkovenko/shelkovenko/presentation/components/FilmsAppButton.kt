package com.pavelshelkovenko.shelkovenko.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavelshelkovenko.shelkovenko.presentation.theme.ShelkovenkoTheme

@Composable
fun FilmsAppButton(
    title: String,
    buttonStatus: ButtonStatus,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.widthIn(170.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (buttonStatus == ButtonStatus.Unselected) {
                MaterialTheme.colorScheme.secondary
            } else MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = if (buttonStatus == ButtonStatus.Unselected) {
                MaterialTheme.colorScheme.primary
            } else MaterialTheme.colorScheme.onPrimary
        )
    }
}

enum class ButtonStatus {
    Selected, Unselected
}

@Preview
@Composable
fun PreviewFilmsAppButtonSelected() {
    ShelkovenkoTheme {
        FilmsAppButton(title = "deseruisse", buttonStatus = ButtonStatus.Selected, onClick = {})
    }
}

@Preview
@Composable
fun PreviewFilmsAppButtonUnselected() {
    ShelkovenkoTheme {
        FilmsAppButton(title = "deseruisse", buttonStatus = ButtonStatus.Unselected, onClick = {})
    }
}

