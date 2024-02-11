package com.pavelshelkovenko.shelkovenko.presentation.components

import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ripple.rememberRipple
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .widthIn(160.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (buttonStatus == ButtonStatus.Unselected) {
                MaterialTheme.colorScheme.secondary
            } else MaterialTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        Text(
            text = title,
            maxLines = 1,
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

