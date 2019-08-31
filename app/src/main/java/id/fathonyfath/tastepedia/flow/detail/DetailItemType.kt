package id.fathonyfath.tastepedia.flow.detail

sealed class DetailItemType {
    data class Header(val title: String) : DetailItemType() {
        companion object {
            const val ITEM_TYPE = 1
        }
    }

    data class Content(val content: String) : DetailItemType() {
        companion object {
            const val ITEM_TYPE = 2
        }
    }

    data class UriContent(val uri: String) : DetailItemType() {
        companion object {
            const val ITEM_TYPE = 3
        }
    }
}