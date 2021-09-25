package com.example.movieapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.ui.modifierExtensions.dividerColor
import com.example.movieapp.ui.modifierExtensions.glassiness
import com.example.movieapp.ui.theme.MovieAppColors
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import com.google.accompanist.insets.statusBarsHeight


@Composable
fun MovieAppDrawer(
    navToHome: () -> Unit,
    navToProfile: () -> Unit,
    navToMovieList: () -> Unit,
    changeThemeColorTo: (MovieAppColors) -> Unit
) {

    Column(
        Modifier
            .padding(3.dp)
            .fillMaxSize()
            .glassiness(0.7F)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        DrawerTitle()
        Divider(color = dividerColor)
        DrawerProfile(navToProfile, "Simon Clement", "Maker of this app")
        Divider(color = dividerColor)
        DrawerItem(navToHome, "Home", "Display latest movies")
        Divider(color = dividerColor)
        DrawerItem(navToMovieList, "Saved Movies", "Reference to movies interested in")
        Divider(color = dividerColor)
        DrawerItem({}, "Lorem ipsum", "consectetur adipiscing elit")
        Divider(color = dividerColor)
        DrawerItem({}, "Massa placerat", "Magna eget est lorem ipsum dolor")
        Divider(color = dividerColor)
        DrawerItem({}, "Vel pretium lectus", "Magna eget est lorem ipsum dolor")
        Divider(color = dividerColor)
        DrawerItem({}, "Settings", "Set themes, topography ...")
    }
}

@Preview
@Composable
fun DrawerTitle() {
    Text(
        text = "TMDB Movie App",
        modifier = Modifier.padding(10.dp, 10.dp),
        style = MovieAppTypography.h5
    )
}

@Composable
fun DrawerProfile(onClick: () -> Unit, text: String, desc: String) {
    Row(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Image(
            painter = painterResource(id = R.drawable.male_user_black),
            contentDescription = "Profile",
            modifier = Modifier
                .padding(end = 3.dp)
                .weight(0.2F)
                .clip(CircleShape)
                .size(50.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Column(
            Modifier
                .padding(end = 3.dp)
                .weight(0.75F)
        ) {
            Text(text = text, style = MovieAppTypography.body1)
            Text(text = desc, style = MovieAppTypography.body2, color = Color.Black)
        }
    }
}

@Composable
fun DrawerItem(onClick: () -> Unit, text: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.add_black),
            contentDescription = "Profile",
            modifier = Modifier
                .weight(0.1F)
                .padding(vertical = 3.dp, horizontal = 5.dp)
                .size(30.dp, 30.dp)
        )
        Column(
            Modifier
                .padding(vertical = 3.dp, horizontal = 5.dp)
                .weight(0.85F)
        ) {
            Text(text = text, style = MovieAppTypography.body1)
            Text(
                text = desc,
                style = MovieAppTypography.body2,
                color = Color.Black.copy(alpha = 0.4F)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.forward_black),
            contentDescription = "Show Profile Details",
            modifier = Modifier
                .padding(vertical = 3.dp, horizontal = 5.dp)
                .size(20.dp, 20.dp)
        )
    }
}