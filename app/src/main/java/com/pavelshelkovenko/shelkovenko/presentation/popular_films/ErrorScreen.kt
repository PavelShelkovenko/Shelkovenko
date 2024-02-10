package com.pavelshelkovenko.shelkovenko.presentation.popular_films

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavelshelkovenko.shelkovenko.R
import com.pavelshelkovenko.shelkovenko.presentation.components.ButtonStatus
import com.pavelshelkovenko.shelkovenko.presentation.components.FilmsAppButton
import com.pavelshelkovenko.shelkovenko.presentation.theme.ShelkovenkoTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(280.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_internet_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.internet_error),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(30.dp))
            FilmsAppButton(
                title = stringResource(id = R.string.repeat),
                buttonStatus = ButtonStatus.Selected,
                onClick = onClick
            )
        }
    }
}

@Preview
@Composable
fun PreviewErrorScreen() {
    ShelkovenkoTheme {
        ErrorScreen {}
    }
}