package com.pavelshelkovenko.shelkovenko.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi
import com.pavelshelkovenko.shelkovenko.presentation.theme.Gray
import com.pavelshelkovenko.shelkovenko.presentation.theme.ShelkovenkoTheme

@Composable
fun FilmCard(
    filmUi: FilmUi,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .shadow(
                elevation = 12.dp,
                ambientColor = Color.DarkGray,
                spotColor = Color.DarkGray,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.width(40.dp),
                model = filmUi.posterUrl,
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = filmUi.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 240.dp)
                )
                Text(
                    text = "${filmUi.genre.replaceFirstChar(Char::titlecase)} (${filmUi.year})",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 240.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (filmUi.isFavorite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun FilmCardPreview() {
    ShelkovenkoTheme {
        FilmCard(
            filmUi = FilmUi(
                id = 2117,
                title = "tincidungdfgdfggdfgdfgdggdfgdfgdfgfdgdft",
                year = null,
                posterUrl = "https://duckduckgo.com/?q=dictas",
                genre = "nascetur",
                isFavorite = false
            ),
        )
    }
}