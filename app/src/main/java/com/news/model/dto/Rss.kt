package com.news.model.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
class Rss {
    @Element
    var channel: Channel? = null

    @Attribute
    var version: String? = null

    @Xml
    class Item {
        @PropertyElement
        var link: String = ""

        @PropertyElement
        var description: String? = null

        @PropertyElement
        var title: String? = null

        @PropertyElement
        var pubDate: String? = null

    }

    @Xml(name = "channel")
    class Channel {
        @Element
        var image: Image? = null

        @Element
        var item: List<Item>? = null

        @PropertyElement
        var link: String? = null

        @PropertyElement
        var description: String? = null

        @PropertyElement
        var title: String? = null

        @PropertyElement
        var managingEditor: String? = null

        @PropertyElement
        var webMaster: String? = null

    }

    @Xml
    class Image {
        @PropertyElement
        var link: String? = null

        @PropertyElement
        var title: String? = null

        @PropertyElement
        var url: String? = null

    }
}