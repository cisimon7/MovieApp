package com.example.movieapp.services.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailed(
    val adult: Boolean,
    val budget: Int,
    val genres: List<Genre>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val tagline: String,
    val vote_average: Float,
    val vote_count: Int,
    val status: String
) {
    fun format(): Movie = Movie(
        id = id,
        title = original_title,
        cover_url = poster_path,
        tagline = tagline,
        overview = overview,
        genres = genres.map { it.name },
        release_date = stringToLocalDate(release_date),
        vote_average = vote_average * 10,
        vote_count = vote_count,
        backdrop_url = backdrop_path,
        runtime = runtime,
        revenue = revenue,
        budget = budget,
        adult = adult,
        status = Status.valueOf(status.split(" ").joinToString(""))
    )
}

@Serializable
data class MovieBrief(
    val poster_path: String,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val id: Int,
    val original_title: String,
    val original_language: String,
    val vote_count: Int,
    val vote_average: Float
) {
    fun format(): Movie = Movie(
        id = id,
        title = original_title,
        cover_url = poster_path,
        tagline = "",
        overview = overview,
        release_date = stringToLocalDate(release_date),
        vote_average = vote_average * 10,
        vote_count = vote_count,
        genres = emptyList(),
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    )
}

@Serializable
data class Genre(val id: Int, val name: String)

@Serializable
enum class Status { Rumored, Planned, InProduction, PostProduction, Released, Canceled, Unknown }

val sampleMovies = listOf(
    Movie(
        id = 0,
        title = "Fight Club",
        cover_url = "R.drawable.test_image_0",
        tagline = "How much can you know about yourself if you've never been in a fight?",
        overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
        genres = listOf("Drama"),
        release_date = stringToLocalDate("1999-10-12"),
        vote_average = 78F,
        vote_count = 3439,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 1,
        title = "Secret Beyond the Door",
        cover_url = "test_image_1",
        tagline = "Some Men Destroy What They Love Most!",
        overview = "Fritz Lang’s psycho thriller tells the story of a woman who marries a stranger with a deadly hobby and through their love he attempts to fight off his obsessive-compulsive actions.",
        genres = listOf("Crime"),
        release_date = stringToLocalDate("1947-12-24"),
        vote_average = 65F,
        vote_count = 79,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 2,
        title = "À ma soeur!",
        cover_url = "R.drawable.test_image_2",
        tagline = "",
        overview = "Anaïs is twelve and bears the weight of the world on her shoulders. She watches her older sister, Elena, whom she both loves and hates. Elena is fifteen and devilishly beautiful. Neither more futile, nor more stupid than her younger sister, she cannot understand that she is merely an object of desire. And, as such, she can only be taken. Or had. Indeed, this is the subject: a girl's loss of virginity. And, that summer, it opens a door to tragedy.",
        genres = listOf("Drama"),
        release_date = stringToLocalDate("2001-03-07"),
        vote_average = 63F,
        vote_count = 132,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 3,
        title = "The Hours",
        cover_url = "R.drawable.test_image_3",
        tagline = "How much can you know about yourself if you've never been in a fight?",
        overview = "\\\"The Hours\\\" is the story of three women searching for more potent, meaningful lives. Each is alive at a different time and place, all are linked by their yearnings and their fears. Their stories intertwine, and finally come together in a surprising, transcendent moment of shared recognition.",
        genres = listOf("Drama"),
        release_date = stringToLocalDate("2002-12-27"),
        vote_average = 73F,
        vote_count = 1259,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 4,
        title = "Full Metal Jacket",
        cover_url = "R.drawable.test_image_4",
        tagline = "Vietnam can kill me, but it can’t make me care.",
        overview = "A pragmatic U.S. Marine observes the dehumanizing effects the U.S.-Vietnam War has on his fellow recruits from their brutal boot camp training to the bloody street fighting in Hue.",
        genres = listOf("Drama"),
        release_date = stringToLocalDate("1987-06-26"),
        vote_average = 81F,
        vote_count = 7857,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 5,
        title = "Ghostbusters",
        cover_url = "R.drawable.test_image_5",
        tagline = "They ain't afraid of no ghost.",
        overview = "After losing their academic posts at a prestigious university, a team of parapsychologists goes into business as proton-pack-toting \\\"ghostbusters\\\" who exterminate ghouls, hobgoblins and supernatural pests of all stripes. An ad campaign pays off when a knockout cellist hires the squad to purge her swanky digs of demons that appear to be living in her refrigerator.",
        genres = listOf("Comedy", "Fantasy"),
        release_date = stringToLocalDate("1984-06-08"),
        vote_average = 74F,
        vote_count = 6657,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 6,
        title = "The Wizard of Oz",
        cover_url = "R.drawable.test_image_6",
        tagline = "We're off to see the Wizard, the wonderful Wizard of Oz!",
        overview = "Young Dorothy finds herself in a magical world where she makes friends with a lion, a scarecrow and a tin man as they make their way along the yellow brick road to talk with the Wizard and ask for the things they miss most in their lives. The Wicked Witch of the West is the only thing that could stop them.",
        genres = listOf("Adventure", "Fantasy", "Family"),
        release_date = stringToLocalDate("1939-08-15"),
        vote_average = 76F,
        vote_count = 4245,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 7,
        title = "Boyz n the Hood",
        cover_url = "R.drawable.test_image_7",
        tagline = "Once upon a time in South Central L.A... It ain't no fairy tale.",
        overview = "Boyz n the Hood is the popular and successful film and social criticism from John Singleton about the conditions in South Central Los Angeles where teenagers are involved in gun fights and drug dealing on a daily basis.",
        genres = listOf("Drama", "Crime"),
        release_date = stringToLocalDate("1991-07-12"),
        vote_average = 81F,
        vote_count = 7857,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 8,
        title = "Catch Me If You Can",
        cover_url = "R.drawable.test_image_8",
        tagline = "The true story of a real fake.",
        overview = "A true story about Frank Abagnale Jr. who, before his 19th birthday, successfully conned millions of dollars worth of checks as a Pan Am pilot, doctor, and legal prosecutor. An FBI agent makes it his mission to put him behind bars. But Frank not only eludes capture, he revels in the pursuit.",
        genres = listOf("Drama", "Crime"),
        release_date = stringToLocalDate("2002-12-25"),
        vote_average = 76F,
        vote_count = 1268,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    ),
    Movie(
        id = 9,
        title = "Thunderball",
        cover_url = "R.drawable.test_image_9",
        tagline = "Look up!  Look down!  Look out!",
        overview = "A criminal organization has obtained two nuclear bombs and are asking for a 100 million pound ransom in the form of diamonds in seven days or they will use the weapons. The secret service sends James Bond to the Bahamas to once again save the world.",
        genres = listOf("Adventure", "Action", "Thriller"),
        release_date = stringToLocalDate("1965-12-11"),
        vote_average = 66F,
        vote_count = 1539,
        backdrop_url = "",
        runtime = 0,
        revenue = 0,
        budget = 0,
        adult = false,
        status = Status.Unknown
    )
)