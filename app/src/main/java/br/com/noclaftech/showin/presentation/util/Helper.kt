package br.com.noclaftech.showin.presentation.util

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import br.com.noclaftech.domain.model.Brand
import br.com.noclaftech.domain.model.Category
import br.com.noclaftech.showin.R
import storage.Singleton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Helper {
    companion object {
        private lateinit var activity : Activity

        fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = view.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(left, top, right, bottom)
                view.requestLayout()
            }
        }

        fun dpToInt(dp: Int, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                context.resources.displayMetrics).toInt()
        }

        @Throws(ParseException::class)
        fun validateDate(date : String) : Boolean {
            val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            df.isLenient = false
            return try {
                df.parse(date)
                true
            } catch (e: ParseException) {
                false
            }
        }

        @Throws(ParseException::class)
        fun validateHour(hour : String) : Boolean{
            val df = SimpleDateFormat("HH:mm", Locale.getDefault())
            df.isLenient = false
            return try {
                df.parse(hour)
                true
            } catch (e: ParseException){
                false
            }
        }
        @Throws(ParseException::class)
        fun validateCurrentDate(date : String, hour : String) : Boolean{
            val dateFormat = "dd/MM/yyyy HH:mm"
            val sdf = SimpleDateFormat(dateFormat)

            return try {
                val d = sdf.parse("$date $hour")
                val calendar = Calendar.getInstance()
                if (d.before(calendar.time)){
                    false
                }
                calendar.add(Calendar.HOUR, 1)
                d.after(calendar.time)

            }catch (e : ParseException){
                e.printStackTrace()
                false
            }
        }

        fun convertDateForApi(date: String, hour: String) : String{
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse("$date $hour:00")
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(sdf!!)
        }

        fun convertForApp(date : String) : String{
            return date.substring(8,date.length) + "/" + date.substring(5,7) + "/" + date.substring(0,4)
        }

        fun getHour(date : String) : String{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
            return SimpleDateFormat("HH:mm", Locale.getDefault()).format(sdf!!)
        }

//        fun getGender(activity: Activity) : ArrayList<String>{
//            this.activity = activity
//            val list : ArrayList<String> = ArrayList()
//
//            list.add(activity.getString(R.string.male))
//            list.add(activity.getString(R.string.female))
//            list.add(activity.getString(R.string.other))
//
//            return list
//        }

        fun getAgeRating() : ArrayList<String>{
            val list : ArrayList<String> = ArrayList()
            list.addAll(Singleton.instance.getConstants()?.blockingGet()!!.ageRating.map { it.age })
            return list
        }

        fun getGender() : ArrayList<String>{
            val list : ArrayList<String> = ArrayList()
            list.addAll(Singleton.instance.getConstants()?.blockingGet()!!.genders.map { it.name })
            return list
        }

        fun getGenderValue(gender : String) : String{
            return Singleton.instance.getConstants()?.blockingGet()!!.genders.find { it.name == gender }!!.value
        }

        fun getGenderName(gender : String?) : String{
            if(gender == null)
                return ""
            return Singleton.instance.getConstants()?.blockingGet()!!.genders.find { it.value == gender }!!.name
        }

        fun getRecordPosition(activity: Activity) : ArrayList<String>{
            this.activity = activity
            val list: ArrayList<String> = ArrayList()
            list.add(activity.getString(R.string.horizontal));
            list.add(activity.getString(R.string.vertical))
            return list
        }

//        fun converteRecordPositionForApp(position: String, activity: Activity) : String {
//            this.activity = activity
//            return when(position){
//                "HORIZONTAL" -> {
//                    activity.getString(R.string.horizontal)
//                }
//                "VERTICAL" -> {
//                    activity.getString(R.string.vertical)
//                }
//            }
//        }

        fun getAgeRating(age : String) : Int{
            return Singleton.instance.getConstants()?.blockingGet()!!.ageRating.find { it.age == age }!!.id
        }

        fun getDuration() : ArrayList<String> {
            val list : ArrayList<String> = ArrayList()
            list.addAll(Singleton.instance.getConstants()?.blockingGet()!!.showDurations.map { it.name })
            return list
        }

        fun getDuration(name : String) : Int{
            return Singleton.instance.getConstants()?.blockingGet()!!.showDurations.find { it.name == name }!!.minutes
        }

        private fun generateDuration() : HashMap<String, Int>{
            val hash = HashMap<String, Int>()

            hash["0:30h"] = 30
            hash["1:00h"] = 60
            hash["1:30h"] = 90
            hash["2:00h"] = 120
            hash["2:30h"] = 150
            hash["3:00h"] = 180
            hash["3:30h"] = 210
            hash["4:00h"] = 240
            hash["4:30h"] = 270
            hash["5:00h"] = 300
            hash["5:30h"] = 330
            hash["6:00h"] = 360
            hash["6:30h"] = 390

            return hash
        }


        fun convertGenderForApp(gender : String, activity: Activity) : String{
            this.activity = activity
            return when(gender){
                "MALE" -> {
                    activity.getString(R.string.male)
                }
                "FEMALE" -> {
                    activity.getString(R.string.female)
                }
                else->{
                    activity.getString(R.string.other)
                }
            }
        }

        fun convertGenderForApi(gender: String?) : String{
            if (gender == null){
                return ""
            }
            return when (gender) {
                activity.getString(R.string.male), "MALE" -> { "MALE" }
                activity.getString(R.string.female), "FEMALE" -> { "FEMALE" }
                else -> { "OTHER" }
            }
        }

        fun getCategories() : ArrayList<String>{
            val list = arrayListOf<Category>()
            list.addAll(Singleton.instance.getConstants()?.blockingGet()!!.categories)
            return list.map { it.name } as ArrayList<String>
        }

        fun getCategoriesObj() : List<Category>{
            return Singleton.instance.getConstants()?.blockingGet()!!.categories
        }

        fun getCategory(category : String) : Int{
            return Singleton.instance.getConstants()?.blockingGet()!!.categories.find { it.name == category }!!.id
        }

        fun getTimeZone(application: Application): String {
            val standard: String = application.getString(R.string.standard_timezone)
            val zone = TimeZone.getDefault().id.toString()

            return zone ?: standard
        }



        fun getBrands() : ArrayList<String>{
            val list = arrayListOf<Brand>()

            list.addAll(Singleton.instance.getConstants()?.blockingGet()!!.brands)

            return list.map { it.name } as ArrayList<String>
        }

        fun getBrandApiName(name : String) : String{
            return Singleton.instance.getConstants()?.blockingGet()!!.brands.find { it.name == name }!!.apiName
        }

        fun getDateTimeFormaterApp(date : String) : String{
            val hour  = getHour(date)
            val sdf = SimpleDateFormat("MMM", Locale.getDefault())


            val calendar: Calendar = GregorianCalendar(date.substring(0,4).toInt(), date.substring(5,7).toInt() - 1, date.substring(8, 10).toInt())

            return "${calendar.get(Calendar.DAY_OF_MONTH)} de ${sdf.format(calendar.time)} ${calendar.get(Calendar.YEAR)} | $hour"
        }

        fun getDateForMessage(date: String) : String{
            val d = date
            val sdf = SimpleDateFormat("MMM")
            val calendar: Calendar = GregorianCalendar(d.substring(0,4).toInt(), d.substring(5,7).toInt() - 1, d.substring(8, 10).toInt())
            return "${calendar.get(Calendar.DAY_OF_MONTH)} de ${sdf.format(calendar.time)} ${calendar.get(Calendar.YEAR)}"
        }

        fun getDateShow(date: String) : String{
//            val formatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z''Z'")
//            val dt: DateTime = formatter.parseDateTime(date)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)

            //val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)

            val d : Date = sdf!!

            val sd = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(d)
            return sd.toUpperCase(Locale.getDefault())
        }

        fun getDateString(date: Date) : String{
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        }

        @JvmStatic
        fun getValue(string: String): Int{
            if(string.isEmpty())
                return 0
            return string.toInt()
        }

        @JvmStatic
        fun getDateShowNoWeekDay(date: String?, dateFinish: String?): String{
            if(date.isNullOrBlank() || dateFinish.isNullOrBlank())
                return ""
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)

            //val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)

            val d : Date = sdf!!

            val sd = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(d)
            return "${sd.toUpperCase(Locale.getDefault())} | ${getHour(date)} - ${getHour(dateFinish)}"
        }

        @JvmStatic
        fun getDateForNotification(date: String?): String{
            if(date.isNullOrBlank())
                return ""
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
            val d : Date = sdf!!
            val sd = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(d)
            return "${sd.toLowerCase(Locale.getDefault())}"
        }

        @JvmStatic
        fun strToDate(date: String?): Date?{
            if(date.isNullOrBlank())
                return null
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
        }

        fun openTermsUse(activity: Activity){
            openPdf(activity, Singleton.instance.getConstants()?.blockingGet()!!.config.useTerms)
        }

        fun openPrivacyPolicy(activity: Activity){
            openPdf(activity, Singleton.instance.getConstants()?.blockingGet()!!.config.privacyPolicy)
        }

        fun openHowToCreateShow(activity: Activity){
            openPdf(activity, Singleton.instance.getConstants()?.blockingGet()!!.config.howToCreateShow!!)
        }

        fun openStreamTips(activity: Activity){
            openPdf(activity, Singleton.instance.getConstants()?.blockingGet()!!.config.streamTips!!)
        }

        fun openMarketingTips(activity: Activity){
            openPdf(activity, Singleton.instance.getConstants()?.blockingGet()!!.config.marketingTips!!)
        }

        fun whatsapp(activity: Activity, number : String){
            try {
                val nmb = number.replace(" ", "").replace("+", "")
                val sendIntent = Intent("android.intent.action.MAIN")
                sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(nmb) + "@s.whatsapp.net")
                activity.startActivity(sendIntent)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "ERROR_OPEN_MESSANGER$e")
            }
        }

        fun facebook(activity: Activity, profileId : String){
             try {
                activity.packageManager.getPackageInfo("com.facebook.katana", 0)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/n/?$profileId"))
                activity.startActivity(intent)
            } catch (e: java.lang.Exception) {
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/n/?$profileId"))
                 activity.startActivity(intent)
            }
        }

        fun instagram(activity: Activity, profile : String){
            val uri = Uri.parse("http://instagram.com/_u/$profile")
            val instagram  = Intent(Intent.ACTION_VIEW)
            instagram.setPackage("com.instagram.android")

            try {
                activity.startActivity(instagram)
            } catch (e: java.lang.Exception) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$profile")))
            }
        }

        fun linkedin(activity: Activity, linkedId: String) {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse("linkedin://add/%@$linkedId"))
            val packageManager: PackageManager = activity.packageManager
            val list: List<ResolveInfo> = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.isEmpty()) {
                intent = Intent(Intent.ACTION_VIEW,Uri.parse("http://www.linkedin.com/profile/view?id=$linkedId"))
            }
            activity.startActivity(intent)
        }

        fun twitter(activity: Activity, userId : String){
            var intent: Intent? = null
            try {
                intent = Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?screen_name=$userId"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.packageManager.getPackageInfo("com.twitter.android", 0)
            } catch (e: java.lang.Exception) {
                intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/$userId"))
            }
            activity.startActivity(intent)
        }

        @JvmStatic
        fun getAt(string: String?) : String{
            return if (string == null)
                ""
            else
                "@$string"
        }

        private fun openPdf(activity: Activity, pdfUrl : String){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
            activity.startActivity(browserIntent)
        }
    }
}