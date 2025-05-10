package com.martvalley.suvidha_u.utils

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.martvalley.suvidha_u.R
import java.io.ByteArrayOutputStream
import java.lang.IndexOutOfBoundsException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.util.Base64
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget


fun Bitmap.bitmapToBase64(): String {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val byteArr = bytes.toByteArray()
    val media = android.util.Base64.encodeToString(byteArr, android.util.Base64.DEFAULT)
    return ("data:image/png;base64,$media")
}

fun isValidIMEI(imei: String): Boolean {
    if (!imei.matches(Regex("\\d{15}"))) return false

    var sum = 0
    for (i in 0 until 14) {
        var digit = Character.getNumericValue(imei[i])
        if (i % 2 == 1) { // double every second digit (index 1, 3, 5...)
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
    }
    val checkDigit = (10 - (sum % 10)) % 10
    return checkDigit == Character.getNumericValue(imei[14])
}


fun Context.compressImageFromUriAndGetBase64( imageUri: Uri, callback: (String?) -> Unit) {
    Glide.with(this)
        .asBitmap()
        .load(imageUri)
        .apply(RequestOptions().override(512, 384)) // Resize the image as needed
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                // Compress the Bitmap to ByteArray
                val byteArrayOutputStream = ByteArrayOutputStream()
                resource.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream) // Quality (0-100)
                val byteArray = byteArrayOutputStream.toByteArray()

                // Encode ByteArray to Base64
                val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

                // Pass the Base64 string to the callback
                callback(base64String)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                showToast("Image loading failed")
            }
        })
}

fun String.base64ToBitmap(): Bitmap {
    val imageBytes = android.util.Base64.decode(this.replace("\\", ""), android.util.Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return decodedImage
}

fun CheckBox.setSpan(context: Context, listner: () -> Unit) {
    val txt = this.text
    val spannable = SpannableStringBuilder(txt)
    spannable.setSpan(
        object : ClickableSpan() {
            override fun onClick(view: View) {
                listner()
            }
        }, 7, // start
        16, // end
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(context.resources.getColor(R.color.blue)), 7, // start
        16, // end
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    spannable.setSpan(
        StyleSpan(Typeface.BOLD), 7, // start
        16, // end
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
//    spannable.setSpan(
//        ForegroundColorSpan(context.resources.getColor(R.color.blue)), 20, // start
//        25, // end
//        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
//    )
//    spannable.setSpan(
//        StyleSpan(Typeface.BOLD), 20, // start
//        25, // end
//        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
//    )

    this.text = spannable
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = resources.getColor(R.color.blue)
}

fun openWhatsAppConversationUsingUri(
    context: Context, numberWithCountryCode: String, message: String
) {
    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numberWithCountryCode&text=$message")
    val sendIntent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(sendIntent)
}

fun Context.showStatusChangeAlertDialog(listenerOk: () -> Unit, listenerCancel: () -> Unit) {
    AlertDialog.Builder(this).setTitle("Status change")
        .setMessage("Are you sure you want to change status.")
        .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
            listenerOk()
        }.setCancelable(false)
        .setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            listenerCancel()
        }.show()
}

fun View.fadeVisibility(visibility: Int, duration: Long = 400) {
    val transition: Transition = Fade()
    transition.duration = duration
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.visibility = visibility
}

fun expand(v: View) {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = v.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    v.layoutParams.height = 1
    v.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            v.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
    v.startAnimation(a)
}

fun collapse(v: View) {
    val initialHeight = v.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
    v.startAnimation(a)
}


fun linearGradientBackground(list: ArrayList<String>): GradientDrawable {
    return GradientDrawable().apply {
        colors = intArrayOf(Color.parseColor("#30${list[0]}"), Color.parseColor("#${list[1]}"))
        gradientType = GradientDrawable.LINEAR_GRADIENT
        orientation = GradientDrawable.Orientation.TOP_BOTTOM
    }
}


private const val SECOND = 1
private const val MINUTE = 60 * SECOND
private const val HOUR = 60 * MINUTE
private const val DAY = 24 * HOUR
private const val MONTH = 30 * DAY
private const val YEAR = 12 * MONTH

fun Uri.getBase64String(contentResolver: ContentResolver): String? {
    val string = contentResolver.openInputStream(this).use { it!!.readBytes() }
    return android.util.Base64.encodeToString(string, android.util.Base64.DEFAULT)
}

fun Bitmap.toBase64StringBitmap(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun String.convertToBase64(): String {   // does not work if used for external storage files like download folder
    val bm = BitmapFactory.decodeFile(this)
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
    val b: ByteArray = baos.toByteArray()
    return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
}


fun currentDate(): Long {
    val calendar = Calendar.getInstance()
    return calendar.timeInMillis
}

// Long: time in millisecond
fun Long.toTimeAgo(): String {
    val time = this * 1000
    val now = currentDate()

    // convert back to second
    val diff = (now - time) / 1000

    return when {
        diff < MINUTE -> "Just now"
        diff < 2 * MINUTE -> "1 minute ago"
        diff < 60 * MINUTE -> "${diff / MINUTE} minutes ago"
        diff < 2 * HOUR -> "1 hour ago"
        diff < 24 * HOUR -> "${diff / HOUR} hours ago"
        diff < 2 * DAY -> "1 day ago"
        diff < 30 * DAY -> "${diff / DAY} days ago"
        diff < 2 * MONTH -> "1 month ago"
        diff < 12 * MONTH -> "${diff / MONTH} months ago"
        diff < 2 * YEAR -> "1 year ago"
        else -> "${diff / YEAR} years ago"
    }
}


// Long: time in millisecond
fun String.toMonthAgo(): String {
    val time = this.toLong() * 1000
    val now = currentDate()
    // convert back to second
    val diff = (now - time) / 1000

    return when {
        diff < MINUTE -> "Just now"
        diff < 2 * MINUTE -> "1 minute ago"
        diff < 60 * MINUTE -> "${diff / MINUTE} minutes ago"
        diff < 2 * HOUR -> "1 hour ago"
        diff < 24 * HOUR -> "${diff / HOUR} hours ago"
        diff < 2 * DAY -> "1 day ago"
        diff < 30 * DAY -> "${diff / DAY} days ago"
        else -> this.getDate() ?: ""
    }
}

fun String.getDate(): String? {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val netDate = Date(this.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context).load(url)
//        .centerCrop()
//        .error(R.drawable.ic_broken_image)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .placeholder(R.drawable.movie_loading_animation)
        .into(this)
}

/*---------------------------- log -----------------------------------------------*/

fun <T> T.logd(tag: String = "TAG") {
    Log.d(tag, "$tag :: $this")
}

fun <T> T.loge(tag: String = "TAG") {
    Log.e(tag, "$tag :: $this")
}

/*--------------------------------------------------------------------------------*/


/*----------------------------- views --------------------------------------------*/

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisibile(): Boolean = visibility == View.VISIBLE
fun View.isGone(): Boolean = visibility == View.GONE
fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/*--------------------------------------------------------------------------------*/


/*-------------------------- toast -----------------------------------------------*/

fun Fragment.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.requireContext(), msg, length).show()
}

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, length).show()
}


fun Context.showNetworkToast() {
    showToast("Oops, your connection seems off, check your connection and try again.")
}

fun Context.showApiErrorToast() {
    showToast("Oops! There is a problem connecting to the server. Please try again")
}


/*--------------------------------------------------------------------------------*/


/*------------------------ internet check ----------------------------------------*/
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkAvailable(): Boolean {
    try {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            return true
        }
    } catch (e: Exception) {
        e.message.loge("exc")
    }
    return false
}


inline fun Fragment.withNetwork(block: () -> Unit) {
    if (this.requireContext().isNetworkAvailable()) {
        block()
    } else {
        this.requireContext().showNetworkToast()
    }
}

inline fun Context.withNetwork(block: () -> Unit) {
    if (this.isNetworkAvailable()) {
        block()
    } else {
        this.showNetworkToast()
    }
}

/*--------------------------------------------------------------------------------*/


/*---------------------- dates ---------------------------------------------------*/


private const val TIME_STAMP_FORMAT = "EEEE, MMMM d, yyyy - hh:mm:ss a"
private const val DATE_FORMAT = "yyyy-MM-dd"

fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

@Throws(ParseException::class)
fun String.getDateUnixTime(): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}


//The usage of it:
//
//val currentTime = System.currentTimeMillis()
//println(currentTime.getTimeStamp())
//println(currentTime.getYearMonthDay())
//println("2020-09-20".getDateUnixTime())
//The output:
//
//Sunday, September 20, 2020 - 10:48:26 AM
//2020-09-20
//1600549200000

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

/**
 * Checks if dates are same
 */
fun Date.isSame(to: Date): Boolean {
    val sdf = SimpleDateFormat("yyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(to)
}

/**
 * Converts raw string to date object using [SimpleDateFormat]
 */
fun String.convertStringToDate(simpleDateFormatPattern: String): Date? {
    val simpleDateFormat = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
    var value: Date? = null
//    justTry {
    value = simpleDateFormat.parse(this)
//    }
    return value
}

fun Long.getYearMonthDayandTime(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy \n hh:mm a", Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}


fun Long.getTime(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(" HH:mm ", Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

/*--------------------------------------------------------------------------------*/


/*---------------------------- ImageExtensions ------------------------------------*/

/**
 * Convert byte array to bitmap
 */
fun ByteArray.convertBytesToBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

/**
 * Convert bitmap to a byte array
 */
fun Bitmap.convertBitmapToBytes(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0, stream)
    return stream.toByteArray()
}

/**
 * Make ImageView image GrayScale
 */
fun ImageView.makeGrayscale() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    colorFilter = ColorMatrixColorFilter(matrix)
}
/*--------------------------------------------------------------------------------*/


/*--------------------------------------------------------------------------------*/


fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPhoneValid(): Boolean {
    return this.length == 10 && this.toDouble() != 0.0 && !this.startsWith("0") && (this.startsWith(
        "9"
    ) || this.startsWith("8") || this.startsWith("7") || this.startsWith(
        "6"
    ))
}

/*--------------------------------------------------------------------------------*/


fun String.convertISOTimeToDate(): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var convertedDate: Date? = null
    var formattedDate: String? = null
    try {
        convertedDate = sdf.parse(this)
        formattedDate = SimpleDateFormat("yyyy-MM-dd").format(convertedDate)/*hh:mm a"*/
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedDate
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return String.format("%04d-%02d-%02d", year, month, day)
}


fun String.convertISOTimeToDateTime(): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")

    var convertedDate: Date? = null
    var formattedDate: String? = null
    try {
        convertedDate = sdf.parse(this)
        formattedDate = SimpleDateFormat(
            "yyyy-MM-dd hh:mm a", Locale.getDefault()
        ).format(convertedDate)/*hh:mm a"*/
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedDate
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)

    showToast("Text copied to clipboard")
}

fun viewInGoogleMaps(latitude: String, longitude: String, context: Context) {
    try {
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude (my devie)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        context.startActivity(intent)
    } catch (e:IndexOutOfBoundsException){
        e.printStackTrace()
    }
}

fun ViewPager.autoScroll(interval: Long) {

    val handler = Handler()
    var scrollPosition = 0

    val runnable = object : Runnable {

        override fun run() {

            /**
             * Calculate "scroll position" with
             * adapter pages count and current
             * value of scrollPosition.
             */
            val count = adapter?.count ?: 0
            setCurrentItem(scrollPosition++ % count, true)

            handler.postDelayed(this, interval)
        }
    }

    addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            // Updating "scroll position" when user scrolls manually
            scrollPosition = position + 1
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Not necessary
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Not necessary
        }
    })

    handler.post(runnable)
}