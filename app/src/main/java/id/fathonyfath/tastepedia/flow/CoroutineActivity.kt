package id.fathonyfath.tastepedia.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.fathonyfath.tastepedia.extension.MainScope
import kotlinx.coroutines.CoroutineScope

abstract class CoroutineActivity : AppCompatActivity() {
    private val _uiScope = MainScope()

    val uiScope: CoroutineScope
        get() = this._uiScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this._uiScope)
    }


}