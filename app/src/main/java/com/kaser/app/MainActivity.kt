
package com.kaser.hadith

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.text.Editable
import android.text.TextWatcher

data class Hadith(
    val text: String,
    val source: String
)

class MainActivity : Activity() {

    private val hadiths = listOf(
        Hadith(
            "إنما الأعمال بالنيات، وإنما لكل امرئ ما نوى",
            "صحيح البخاري، حديث 1"
        ),
        Hadith(
            "الدين النصيحة",
            "صحيح مسلم، حديث 55"
        ),
        Hadith(
            "من كان يؤمن بالله واليوم الآخر فليقل خيرًا أو ليصمت",
            "صحيح البخاري، حديث 6018"
        ),
        Hadith(
            "لا يؤمن أحدكم حتى يحب لأخيه ما يحب لنفسه",
            "صحيح البخاري، حديث 13"
        ),
        Hadith(
            "تبسمك في وجه أخيك لك صدقة",
            "جامع الترمذي، حديث 1956"
        ),
        Hadith(
            "من لا يرحم لا يُرحم",
            "صحيح البخاري، حديث 5997"
        )
    )

    private val favorites = mutableSetOf<String>()
    private lateinit var list: LinearLayout
    private lateinit var search: EditText
    private var showFavorites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(7, 59, 50)
        window.navigationBarColor = Color.rgb(7, 59, 50)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundColor(Color.rgb(245, 242, 233))
        }

        val title = TextView(this).apply {
            text = "موسوعة الأحاديث النبوية"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(7, 59, 50))
            setPadding(0, 12, 0, 16)
        }

        search = EditText(this).apply {
            hint = "ابحث في الأحاديث..."
            textSize = 16f
            singleLine = true
        }

        val favoritesButton = Button(this).apply {
            text = "عرض المفضلة"
            setOnClickListener {
                showFavorites = !showFavorites
                text = if (showFavorites)
                    "عرض جميع الأحاديث"
                else
                    "عرض المفضلة"
                render()
            }
        }

        val scroll = ScrollView(this)
        list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(list)

        root.addView(title)
        root.addView(search)
        root.addView(favoritesButton)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                -1, 0, 1f
            )
        )

        setContentView(root)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int,
                before: Int, count: Int
            ) {
                render()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        render()
    }

    private fun render() {
        list.removeAllViews()

        val query = search.text.toString().trim()

        val filtered = hadiths.filter {
            it.text.contains(query, ignoreCase = true) &&
                (!showFavorites || favorites.contains(it.text))
        }

        if (filtered.isEmpty()) {
            list.addView(TextView(this).apply {
                text = "لا توجد أحاديث مطابقة للبحث"
                textSize = 17f
                setPadding(8, 24, 8, 24)
            })
        }

        filtered.forEach { hadith ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.WHITE)
            }

            val text = TextView(this).apply {
                this.text = hadith.text
                textSize = 20f
                gravity = Gravity.RIGHT
                setTextColor(Color.rgb(7, 59, 50))
            }

            val source = TextView(this).apply {
                this.text = "المصدر: ${hadith.source}"
                textSize = 13f
                setPadding(0, 12, 0, 4)
                setTextColor(Color.DKGRAY)
            }

            val button = Button(this).apply {
                text = if (favorites.contains(hadith.text))
                    "★ إزالة من المفضلة"
                else
                    "☆ إضافة للمفضلة"

                setOnClickListener {
                    if (!favorites.add(hadith.text)) {
                        favorites.remove(hadith.text)
                    }
                    render()
                }
            }

            card.addView(text)
            card.addView(source)
            card.addView(button)

            val params = LinearLayout.LayoutParams(-1, -2)
            params.setMargins(0, 0, 0, 12)
            list.addView(card, params)
        }
    }
}
