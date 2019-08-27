package id.fathonyfath.tastepedia.flow.profile

import android.os.Bundle
import id.fathonyfath.tastepedia.R
import id.fathonyfath.tastepedia.flow.CoroutineActivity

class ProfileActivity : CoroutineActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}