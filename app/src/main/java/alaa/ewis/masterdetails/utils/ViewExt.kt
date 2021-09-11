package alaa.ewis.masterdetails.utils

import alaa.ewis.masterdetails.R
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.annotation.DrawableRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

// Set Check if view visible to user
fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

// Set view visible
fun View.toVisible() {
    this.visibility = View.VISIBLE
}

// Set View invisible
fun View.toGone() {
    this.visibility = View.GONE
}

// Use to load airline image from uri
fun ShapeableImageView.loadImage(url: String) = Picasso.get().load(url).placeholder(R.drawable.airplane).error(
    R.drawable.airplane).into(this)

// Change floating action button image
fun FloatingActionButton.changeImage(@DrawableRes resId: Int) =
    this.setImageResource(resId)

// Check internet connection
fun isOnline(context: Context): Boolean {
    val conMgr = (context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    val networkInfo = conMgr.activeNetworkInfo ?: return false
    return if (!networkInfo.isConnected) false else networkInfo.isAvailable
}



