package com.example.movieapp.ui.fragmentDetailedCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.ui.extensionsModifier.centreOffsetWithin
import com.example.movieapp.ui.extensionsModifier.pxToDp
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReviewDiscussionDropDown(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {

        val reviewOrDiscussion: MutableState<ReviewOrDiscussion> =
            remember { mutableStateOf(ReviewOrDiscussion.None) }

        var position1 by remember { mutableStateOf(Offset.Zero) }
        var position2 by remember { mutableStateOf(Offset.Zero) }

        var dimension1 by remember { mutableStateOf(IntSize.Zero) }
        var dimension2 by remember { mutableStateOf(IntSize.Zero) }

        val transition = updateTransition(reviewOrDiscussion.value, label = "")
        val aniPosition = transition.animateOffset(
            transitionSpec = {
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            },
            label = "",
            targetValueByState = { state: ReviewOrDiscussion ->
                when (state) {
                    ReviewOrDiscussion.None -> Offset.Zero
                    ReviewOrDiscussion.Review -> position1
                    ReviewOrDiscussion.Discussion -> position2
                }
            }
        )
        val aniDimension = transition.animateIntSize(
            transitionSpec = {
                spring(stiffness = Spring.StiffnessLow)
            },
            label = "",
            targetValueByState = { state: ReviewOrDiscussion ->
                when (state) {
                    ReviewOrDiscussion.None -> IntSize.Zero
                    ReviewOrDiscussion.Review -> dimension1
                    ReviewOrDiscussion.Discussion -> dimension2
                }
            }
        )

        Box(
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = 0.12F),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier.layout { measurable, constraints ->

                    val (parentWidth, parentHeight) = with(constraints) { maxWidth to maxHeight }

                    val placeable = measurable.measure(constraints)
                    dimension1 = with(placeable) { IntSize(width, height) }

                    val xPos = (parentWidth * 0.25) - (placeable.width * 0.5)
                    val yPos = 0
                    position1 = Offset(xPos.toFloat(), placeable.height.toFloat())

                    layout(placeable.width, placeable.height) {
                        placeable.place(xPos.toInt(), yPos)
                    }
                },
                content = {
                    Text(
                        text = "Reviews (114)",
                        modifier = Modifier.clickable {
                            when (reviewOrDiscussion.value) {
                                ReviewOrDiscussion.Review -> {
                                    reviewOrDiscussion.value = ReviewOrDiscussion.None
                                }
                                else -> { reviewOrDiscussion.value = ReviewOrDiscussion.Review }
                            }
                        },
                        style = MovieAppTypography.h6,
                        color = Color.Black.copy(
                            alpha = when (reviewOrDiscussion.value) {
                                ReviewOrDiscussion.Review -> 1.0F
                                else -> 0.5F
                            }
                        )
                    )
                }
            )
            Box(
                modifier = Modifier.layout { measurable, constraints ->

                    val (parentWidth, _) = with(constraints) { maxWidth to maxHeight }

                    val placeable = measurable.measure(constraints)
                    dimension2 = with(placeable) { IntSize(width, height) }

                    val xPos = (parentWidth * 0.75) - (placeable.width * 0.5)
                    val yPos = 0

                    position2 = Offset(xPos.toFloat(), placeable.height.toFloat())

                    layout(placeable.width, placeable.height) {
                        placeable.place(xPos.toInt(), yPos)
                    }
                },
                content = {
                    Text(
                        text = "Discussion (1028)",
                        modifier = Modifier.clickable {
                            when (reviewOrDiscussion.value) {
                                ReviewOrDiscussion.Discussion -> {
                                    reviewOrDiscussion.value = ReviewOrDiscussion.None
                                }
                                else -> {
                                    reviewOrDiscussion.value = ReviewOrDiscussion.Discussion
                                }
                            }
                        },
                        style = MovieAppTypography.h6,
                        color = Color.Black.copy(
                            alpha = when (reviewOrDiscussion.value) {
                                ReviewOrDiscussion.Discussion -> 1.0F
                                else -> 0.5F
                            }
                        )
                    )
                }
            )

            val positionDp = aniPosition.value.pxToDp()
            val dimensionDp = aniDimension.value.pxToDp()

            Box(
                modifier = Modifier
                    .offset(positionDp.first, positionDp.second - 3.dp)
                    .size(dimensionDp.first, 3.dp)
                    .background(Color.Blue, shape = RoundedCornerShape(5.dp))
            )
        }

        AnimatedVisibility(visible = ReviewOrDiscussion.None != reviewOrDiscussion.value) {

            Column(modifier = Modifier.padding(10.dp)) {

                when (reviewOrDiscussion.value) {
                    ReviewOrDiscussion.None -> { }
                    ReviewOrDiscussion.Review -> ReviewDropDown()
                    ReviewOrDiscussion.Discussion -> DiscussionDropDown()
                }
            }
        }
    }
}

@Composable
private fun ReviewDropDown() {
    AddNewReview()
    Spacer(modifier = Modifier.padding(5.dp))

    ReviewItem()
    Spacer(modifier = Modifier.padding(3.dp))
    Divider(modifier = Modifier)

    ReviewItem()
    Spacer(modifier = Modifier.padding(3.dp))
    Divider(modifier = Modifier)

    ReviewItem()
    Spacer(modifier = Modifier.padding(3.dp))
    Divider(modifier = Modifier)

    ReviewItem()
    Spacer(modifier = Modifier.padding(3.dp))
}

@Composable
private fun DiscussionDropDown() {
    AddNewDiscussion()
    Spacer(modifier = Modifier.padding(5.dp))

    DiscussionItem()
    Spacer(modifier = Modifier.padding(5.dp))
    Divider(modifier = Modifier)

    DiscussionItem()
    Spacer(modifier = Modifier.padding(5.dp))
    Divider(modifier = Modifier)

    DiscussionItem()
    Spacer(modifier = Modifier.padding(5.dp))
    Divider(modifier = Modifier)

    DiscussionItem()
    Spacer(modifier = Modifier.padding(5.dp))
}

@Preview
@Composable
private fun AddNewReview() {
    var newReview by remember { mutableStateOf("") }
    OutlinedTextField(
        value = newReview,
        onValueChange = { value -> newReview = value },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Write new review", style = MovieAppTypography.body1, color = Color.Gray)
        }
    )
}


@Composable
private fun AddNewDiscussion() {
    var newReview by remember { mutableStateOf("") }
    OutlinedTextField(
        value = newReview,
        onValueChange = { value -> newReview = value },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "What's on your mind", style = MovieAppTypography.body1, color = Color.Gray)
        }
    )
}

@Preview
@Composable
fun DiscussionProfileItem() {
    Box(modifier = Modifier
        .size(50.dp)
        .background(Color.Transparent)
        .drawWithContent {

            val radius = center.x
            val thickness = 0.2F * radius
            val padding = 1 * thickness

            drawCircle(
                color = Color.Black,
                radius = radius + thickness - padding,
                style = Fill
            )
            drawContent()
        },
        content = {
            Row(Modifier.centreOffsetWithin(0.5F, 0.5F), verticalAlignment = Alignment.Top) {

                val initials = listOf("SC", "CF", "IN", "RD", "HO", "IA")

                Text(
                    text = initials.random(),
                    style = MovieAppTypography.h6/*.copy(textAlign = TextAlign.End)*/,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MovieAppColorTheme.colors.ascent2,
                    modifier = Modifier
                )
            }
        }
    )
}


@Preview
@Composable
fun ReviewItem() {
    Column(Modifier.padding(10.dp)) {
        Row(Modifier, verticalAlignment = Alignment.CenterVertically) {

            val profilePic = listOf(R.drawable.icons_circled_female, R.drawable.icons_circled_male)

            Image(
                painter = painterResource(id = profilePic.random()),
                contentDescription = "Profile",
                modifier = Modifier.clip(CircleShape).size(100.dp).padding(end = 3.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Column(Modifier) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Jason Hayes", style = MovieAppTypography.h5, color = MovieAppColorTheme.colors.ascent2)
                    Spacer(modifier = Modifier.padding(5.dp))
                    StarRatingItem(Modifier.scale(0.6F))
                }
                Text(text = "October 23, 2020", style = MovieAppTypography.h6, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.sample_review),
            style = MovieAppTypography.body1,
            color = MovieAppColorTheme.colors.ascent2
        )
    }
}


@Preview
@Composable
fun DiscussionItem(/*isMe: Boolean = false, discuss: String*/) {

    val discussions = listOf(
        "Charge your portal guns Rick and Morty Returns Tonight",
        "This is insane",
        "Well good to see Season 4 is already way better than season 3"
    )

    OutlinedTextField(
        value = discussions.random(),
        onValueChange = {  },
        textStyle = MovieAppTypography.body1.copy(color = MovieAppColorTheme.colors.ascent2),
        readOnly = true,
        leadingIcon = { DiscussionProfileItem() },
        modifier = Modifier.fillMaxWidth()
    )
}


private sealed interface ReviewOrDiscussion {
    object Review : ReviewOrDiscussion
    object Discussion : ReviewOrDiscussion
    object None : ReviewOrDiscussion

    fun toggle() {
        when (this) {
            Discussion -> Review
            Review -> Discussion
            None -> None
        }
    }
}

@Composable
fun StarRatingItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color = Color.Black, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 20.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icons_star_filled_64px_1),
            contentDescription = "Profile",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "9.0",
            style = MovieAppTypography.h5,
            color = MovieAppColorTheme.colors.ascent2,
            modifier = Modifier
        )
    }
}