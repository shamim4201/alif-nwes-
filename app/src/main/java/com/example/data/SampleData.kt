package com.example.data

object SampleData {
    private val now: Long get() = System.currentTimeMillis()
    private const val MINUTE_MS = 60 * 1000L
    private const val HOUR_MS = 60 * MINUTE_MS
    private const val DAY_MS = 24 * HOUR_MS

    val initialArticles: List<NewsArticle> get() = listOf(
        // Hero Grid Articles
        NewsArticle(
            id = 1,
            title = "One man with courage makes a majority",
            subtitle = "How independent creators and journalists are reshaping digital narrative and public discourse across the nation.",
            content = """In an era dominated by algorithmic echo chambers, individual courage and clarity of purpose remain the ultimate arbiters of truth. A solitary voice, fortified by integrity and relentless pursuit of facts, has historically dismantled prevailing falsehoods.

From grassroots community initiatives to investigative journalism uncovering institutional failures, the courage to speak up when consensus is comfortable has repeatedly shifted historical tides. This comprehensive chronicle explores the modern pioneers who refuse to conform and in doing so, define our cultural future.""",
            category = "Life Style",
            author = "Tony Stark",
            publishedAt = now - 18 * MINUTE_MS, // 18 mins ago
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 28400,
            slug = "one-man-with-courage-makes-a-majority",
            metaDescription = "How independent creators and thinkers with courage reshape digital narratives and public discourse.",
            source = "Jannah News Features",
            readTimeMinutes = 5
        ),
        NewsArticle(
            id = 2,
            title = "Success is not a good teacher failure makes you humble",
            subtitle = "Lessons from high-stakes venture building, resilience, and learning from missteps in modern business.",
            content = """While triumphs celebrate execution, failures illuminate structural gaps. The most resilient entrepreneurs and artists view setbacks not as fatal verdicts, but as diagnostic milestones.

Psychological research underscores that individuals who navigate early professional failures with humility develop significantly higher adaptive intelligence than those blessed with immediate, effortless success.""",
            category = "Travel",
            author = "Tony Stark",
            publishedAt = now - 45 * MINUTE_MS, // 45 mins ago
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
            isBreaking = true,
            isBookmarked = true,
            viewCount = 31200,
            slug = "success-is-not-a-good-teacher-failure-makes-you-humble",
            metaDescription = "Why failure is a profound teacher and how humility cultivates long-term resilience and innovation.",
            source = "Jannah Travel & Life",
            readTimeMinutes = 4
        ),
        NewsArticle(
            id = 3,
            title = "Budget issues force the Tour to be cancelled",
            subtitle = "Global economic headwinds impact international sporting calendar as organizers review fiscal models.",
            content = """Financial shortfalls and escalating logistics costs have forced tournament directors to suspend this season's marquee road stages. Despite strong local patronage and broadcast interest, corporate sponsorship agreements failed to close before the contractual deadline.

Athletes and cycling federations voiced deep regret, calling for modernized revenue-sharing frameworks to safeguard regional sports ecosystems.""",
            category = "Travel",
            author = "Tony Stark",
            publishedAt = now - 2 * HOUR_MS, // 2 hours ago
            imageUrl = "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 19400,
            slug = "budget-issues-force-tour-cancelled",
            metaDescription = "Fiscal constraints and sponsorship gaps lead to unexpected cancellation of the international tour.",
            source = "Jannah Sports Desk",
            readTimeMinutes = 3
        ),
        NewsArticle(
            id = 4,
            title = "Instagram's big redesign goes live with black-and-white app",
            subtitle = "Minimalist interface places photography and user stories front and center, shedding retro skeuomorphism.",
            content = """In what design analysts call its most consequential visual overhaul, the visual network has discarded vintage skeuomorphic gradients in favor of an ultra-clean monochrome canvas.

By neutralizing the app chrome with pure white and black accents, the redesign directs 100% of viewer attention toward high-definition user photography and cinematic reels.""",
            category = "Technology",
            author = "Danny Rand",
            publishedAt = now - 3 * HOUR_MS - 30 * MINUTE_MS, // ~3.5 hours ago
            imageUrl = "https://images.unsplash.com/photo-1526778548025-fa2f459cd5c1?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 42100,
            slug = "instagram-big-redesign-black-and-white",
            metaDescription = "Instagram rolls out clean black-and-white interface redesign highlighting vibrant user photos and videos.",
            source = "Tech Chronicle Silicon Valley",
            readTimeMinutes = 3
        ),

        // Trending News Section
        NewsArticle(
            id = 5,
            title = "Not who has much is rich, but who gives much",
            subtitle = "Philosophical perspectives on generosity, community wealth, and redefined measures of human success.",
            content = """True prosperity is measured not by accumulation, but by distribution. The philanthropic landscape is experiencing a generational transition from legacy endowment bureaucracy to rapid-response direct giving.

Sociological surveys indicate that cultures prioritizing collective welfare and charitable sharing consistently report higher happiness and cohesion metrics than hyper-competitive economic systems.""",
            category = "Creative",
            author = "Tony Stark",
            publishedAt = now - 5 * HOUR_MS, // 5 hours ago
            imageUrl = "https://images.unsplash.com/photo-1516627145497-ae6968895b74?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 73877,
            slug = "not-who-has-much-is-rich-but-who-gives-much",
            metaDescription = "Exploring the true measure of wealth through generosity, altruism, and community upliftment.",
            source = "Jannah Opinion & Culture",
            readTimeMinutes = 4
        ),
        NewsArticle(
            id = 6,
            title = "The Top 10 Best Computer Speakers in the Market",
            subtitle = "A comprehensive acoustic benchmark covering studio monitors, Bluetooth desktop speakers, and audiophile DACs.",
            content = """From compact USB-powered units to bi-amplified studio reference monitors, this year's desktop audio category boasts remarkable leaps in transducer engineering and DSP frequency tuning.

We tested ten leading contenders across frequency accuracy, dynamic range, spatial imaging, and chassis build quality to crown the definitive champions for music production, gaming, and multimedia immersion.""",
            category = "Technology",
            author = "Danny Rand",
            publishedAt = now - 7 * HOUR_MS, // 7 hours ago
            imageUrl = "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 21500,
            slug = "top-10-best-computer-speakers-market",
            metaDescription = "Comprehensive testing and acoustic review of the ten best computer desktop speakers and monitors.",
            source = "Audio Gear Lab",
            readTimeMinutes = 6
        ),
        NewsArticle(
            id = 7,
            title = "Play This Game for Free on Steam This Weekend",
            subtitle = "Critically acclaimed tactical sci-fi multiplayer shooter opens servers to all players with double XP.",
            content = """Gamers looking for weekend thrills can dive into the futuristic warfare simulator without paying a cent. The promotional event features unlocked competitive playlists, access to all twelve operator loadouts, and exclusive cosmetic drop rewards for weekend participants.""",
            category = "Technology",
            author = "Tony Stark",
            publishedAt = now - 10 * HOUR_MS, // 10 hours ago
            imageUrl = "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 18900,
            slug = "play-this-game-free-on-steam-weekend",
            metaDescription = "Steam hosts free-to-play weekend for acclaimed multiplayer action shooter with double XP perks.",
            source = "Gaming Central Wire",
            readTimeMinutes = 2
        ),
        NewsArticle(
            id = 8,
            title = "At Value-Focused Hotels, the Free Breakfast Gets Bigger",
            subtitle = "Hospitality brands compete aggressively for family and budget travelers with gourmet morning buffet perks.",
            content = """The modest continental spread of dry bagels and instant coffee is officially obsolete. Major hospitality conglomerates are upgrading their value-tier properties with made-to-order Belgian waffles, artisanal cold brew, and hot breakfast protein stations to capture domestic tourism market share.""",
            category = "Foods",
            author = "Tony Stark",
            publishedAt = now - 14 * HOUR_MS, // 14 hours ago
            imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 15300,
            slug = "at-value-hotels-free-breakfast-gets-bigger",
            metaDescription = "Hotels elevate free breakfast amenities with gourmet hot bars and artisanal coffee to attract travelers.",
            source = "Hospitality Daily",
            readTimeMinutes = 3
        ),
        NewsArticle(
            id = 9,
            title = "There May Be No Consoles in the Future, EA Exec Says",
            subtitle = "Cloud streaming infrastructure and cross-platform subscriptions could render standalone hardware obsolete.",
            content = """During an industry keynote on interactive entertainment, senior leadership projected that ubiquitous low-latency 5G and fiber networks will transition AAA gaming entirely to cloud server clusters, freeing players from costly hardware upgrade cycles.""",
            category = "Technology",
            author = "Danny Rand",
            publishedAt = now - 18 * HOUR_MS, // 18 hours ago
            imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 29800,
            slug = "there-may-be-no-consoles-in-future",
            metaDescription = "EA executives forecast how cloud gaming infrastructure may replace physical consoles entirely.",
            source = "Tech Chronicle Silicon Valley",
            readTimeMinutes = 4
        ),
        NewsArticle(
            id = 10,
            title = "Failure is the condiment that gives success its flavor",
            subtitle = "Truman Capote's famous aphorism re-examined through contemporary artistic and culinary lens.",
            content = """Without bitter struggles, triumphant moments lack texture and resonance. Artists, athletes, and culinary masters consistently reflect on periods of doubt and experimental disasters as the crucible wherein authentic signature style is discovered.""",
            category = "Creative",
            author = "Tony Stark",
            publishedAt = now - 22 * HOUR_MS, // 22 hours ago
            imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 16700,
            slug = "failure-is-the-condiment-gives-success-flavor",
            metaDescription = "How creative missteps and tenacity enrich subsequent artistic triumphs and life purpose.",
            source = "Jannah Cultural Essays",
            readTimeMinutes = 3
        ),

        // Sports Section (Dark Showcase)
        NewsArticle(
            id = 11,
            title = "Les nouveaux maillots du Real Madrid pour la saison",
            subtitle = "Stay focused and remember we design the best sports uniforms and gear. It's the ones closest to you that want to see you triumph.",
            content = """The royal white kit receives gold filigree trim and aerodynamic moisture-wicking weave patterns inspired by neoclassical Bernabéu architecture.

Supporters queued outside flagships in Madrid and worldwide to celebrate the iconic visual identity, worn by European football's most decorated squad as they pursue back-to-back championship trophies.""",
            category = "Football",
            author = "Tony Stark",
            publishedAt = now - 1 * DAY_MS, // 1 day ago
            imageUrl = "https://images.unsplash.com/photo-1511886929837-354d827aae26?w=800&q=80",
            isBreaking = false,
            isBookmarked = true,
            viewCount = 24555,
            slug = "les-nouveaux-maillots-real-madrid-saison",
            metaDescription = "Real Madrid unveils their official new home match kits with gold accents and high-tech breathable textiles.",
            source = "Jannah European Football Wire",
            readTimeMinutes = 4
        ),
        NewsArticle(
            id = 12,
            title = "I enjoy hard work I love setting goals and achieving them",
            subtitle = "Stay focused and remember we design the best training routines and athletic gear for relentless competitors.",
            content = """High-intensity functional conditioning requires more than brute strength; it demands unwavering cognitive discipline. In this training breakdown, world-class fitness conditioning coaches share periodized dumbbell circuits, eccentric hypertrophy protocols, and post-session recovery nutrition.""",
            category = "Sports",
            author = "Tony Stark",
            publishedAt = now - 1 * DAY_MS - 5 * HOUR_MS, // ~1.2 days ago
            imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 17536,
            slug = "i-enjoy-hard-work-love-setting-goals",
            metaDescription = "A masterclass in daily conditioning, mental grit, and progressive overload for athletes.",
            source = "Jannah Sports Lab",
            readTimeMinutes = 3
        ),
        NewsArticle(
            id = 13,
            title = "Here What's in Battlefield 1's $80 Deluxe Edition",
            subtitle = "Exclusive vehicle skins, trench weapon packs, and five battlepacks detailed for eager frontline players.",
            content = """EA DICE reveals the full tier contents of the premium edition, showcasing historically authentic Lawrence of Arabia melee tools, Red Baron scout biplane liveries, and specialized multiplayer emblems.""",
            category = "Technology",
            author = "Danny Rand",
            publishedAt = now - 2 * DAY_MS, // 2 days ago
            imageUrl = "https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&q=80",
            isBreaking = false,
            isBookmarked = false,
            viewCount = 19200,
            slug = "whats-in-battlefield-1-80-deluxe-edition",
            metaDescription = "Complete breakdown of all weapons, skins, and expansion perks in the Battlefield 1 Deluxe Edition.",
            source = "Gaming Central Wire",
            readTimeMinutes = 3
        ),

        // What's New 2-Column Grid
        NewsArticle(
            id = 14,
            title = "25 Tricks That Will Increase Your Productivity",
            subtitle = "Stay focused and remember we design the best workflows and magazine reading experiences for your daily routine.",
            content = """From time-boxing and ultradian work sprints to zero-inbox email filters and physical clutter reduction, these twenty-five verified habits eliminate friction and multiply mental bandwidth across deep work sessions.""",
            category = "Technology",
            author = "Tony Stark",
            publishedAt = now - 2 * DAY_MS - 8 * HOUR_MS, // ~2.3 days ago
            imageUrl = "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800&q=80",
            isBreaking = true,
            isBookmarked = false,
            viewCount = 24360,
            slug = "25-tricks-increase-productivity",
            metaDescription = "Actionable strategies and psychological hacks to maximize personal efficiency and focus.",
            source = "Jannah Life Lab",
            readTimeMinutes = 5
        ),
        NewsArticle(
            id = 15,
            title = "The Renault Trezor Concept is a Formula E car for the road",
            subtitle = "Futuristic electric supercar boasts a clamshell roof that opens like a jewelry box and carbon-fiber monocoque.",
            content = """Unveiled at the Paris Motor Show, the Trezor captures breathtaking all-electric performance wrapped in sensuous hexagonal carbon textures, OLED touchscreens, and bespoke red leather interior trim.""",
            category = "Life Style",
            author = "Danny Rand",
            publishedAt = now - 3 * DAY_MS, // 3 days ago
            imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&q=80",
            isBreaking = false,
            isBookmarked = true,
            viewCount = 38192,
            slug = "renault-trezor-concept-formula-e-road",
            metaDescription = "Inside the radical Renault Trezor all-electric concept hypercar with clamshell entry and Formula E powertrain.",
            source = "Jannah Automotive Digest",
            readTimeMinutes = 4
        )
    )
}

