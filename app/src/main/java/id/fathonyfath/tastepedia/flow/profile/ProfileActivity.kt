package id.fathonyfath.tastepedia.flow.profile

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.request.RequestOptions
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.extension.loadImage
import id.fathonyfath.tastepedia.flow.CoroutineActivity

class ProfileActivity : CoroutineActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        profileImageView = findViewById(R.id.profile_picture_image_view)

        profileImageView.loadImage(Uri.parse(PATH_TO_PICTURE), applier = {
            apply(RequestOptions.circleCropTransform())
        })
    }

    companion object {
        const val PATH_TO_PICTURE =
            "file:///android_asset/profile/524796_10205786048356004_3451887748232232010_n.jpg"
    }
}