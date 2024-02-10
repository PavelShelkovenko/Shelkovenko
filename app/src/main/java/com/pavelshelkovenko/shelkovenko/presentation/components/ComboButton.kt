package com.pavelshelkovenko.shelkovenko.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pavelshelkovenko.shelkovenko.presentation.theme.ShelkovenkoTheme

@Composable
fun ComboButton(
    modifier: Modifier = Modifier,
    firstButtonTitle: String,
    secondButtonTitle: String,
    firstButtonStatus: ButtonStatus,
    secondButtonStatus: ButtonStatus,
    firstButtonClick: () -> Unit,
    secondButtonClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        FilmsAppButton(
            title = firstButtonTitle,
            buttonStatus = firstButtonStatus,
            onClick = firstButtonClick
        )
        FilmsAppButton(
            title = secondButtonTitle,
            buttonStatus = secondButtonStatus,
            onClick = secondButtonClick
        )
    }
}

@Preview
@Composable
fun PreviewComboButton() {
    ShelkovenkoTheme {
        ComboButton(
            modifier = Modifier,
            firstButtonTitle = "nibh",
            secondButtonTitle = "senectus",
            firstButtonStatus = ButtonStatus.Unselected,
            secondButtonStatus =ButtonStatus.Selected,
            firstButtonClick = {},
            secondButtonClick = {}
        )
    }
}