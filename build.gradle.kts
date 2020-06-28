
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import java.awt.Color
import java.time.*

plugins {
    id("myaa.subkt")
}

subs {
    readProperties("sub.properties", "private.properties")
    episodes(getList("episodes"))

    merge {
        fromMergeTemplate(get("mergetemplate"))
    }

    chapters {
        from(get("chapters"))
        chapterMarker("chapter")
    }

    mux {
        title(get("title"))

        from(get("premux")) {
            tracks {
                include(track.type in listOf(TrackType.VIDEO, TrackType.AUDIO))
            }

            includeChapters(false)
        }

        from(merge.item()) {
            tracks {
                name("English")
                lang("eng")
                default(true)
            }
        }

        chapters(chapters.item()) {
            lang("eng")
        }

        forceCRC("ACABACAB")
        out(get("muxfile"))
    }

    torrent {
        trackers(getList("trackers"))

        from(mux.item())

        out(get("torrentfile"))
    }

    val uploadFiles by task<FTP> {
        from(mux.item())

        into(get("ftpfiledir"))

        host(get("ftphost"))
        port(getAs<Int>("ftpport"))
        username(get("ftpuser"))
        password(get("ftppass"))
    }

    nyaa {
        dependsOn(uploadFiles.item())
        from(torrent.item())
        username(get("torrentuser"))
        password(get("torrentpass"))
        category(NyaaCategories.ANIME_ENGLISH)
        hidden(true)
        information(get("torrentinfo"))
        torrentDescription(getFile("releasepost.txt"))
    }

    anidex {
        dependsOn(uploadFiles.item())
        from(torrent.item())
        apiKey(get("anidexkey"))
        category(Anidex.AnidexCategories.ANIME_SUB)
        group(0)
        hidden(true)
        torrentDescription(getFile("releasepost.txt"))
    }

    val startSeeding by task<FTP> {
        // upload files to seedbox and publish to nyaa before initiating seeding
        dependsOn(nyaa.item(), anidex.item())

        from(torrent.item())

        into(get("ftpfiledir"))

        host(get("ftphost"))
        port(getAs<Int>("ftpport"))
        username(get("ftpuser"))
        password(get("ftppass"))
    }
}
