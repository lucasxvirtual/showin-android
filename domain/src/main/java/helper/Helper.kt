package helper

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Helper {
    companion object {

        fun convertPassMd5(pass: String): String {
            var mPass = pass
            var password: String? = null
            val mdEnc: MessageDigest
            try {
                mdEnc = MessageDigest.getInstance("MD5")
                mdEnc.update(mPass.toByteArray(), 0, mPass.length)
                mPass = BigInteger(1, mdEnc.digest()).toString(16)
                while (mPass.length < 32) {
                    mPass = "0$mPass"
                }
                password = mPass
            } catch (e1: NoSuchAlgorithmException) {
                e1.printStackTrace()
            }

            return password!!.toUpperCase()
        }

        fun convertDateForApi(data: String): String {
            val aux1 = data.substring(0, 10)
            val aux2: String

            aux2 = aux1.substring(6, aux1.length) + "-" + aux1.substring(3, 5) + "-" + aux1.substring(0, 2)

            return aux2
        }
    }
}