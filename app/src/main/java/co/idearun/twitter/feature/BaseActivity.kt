package co.idearun.twitter.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.idearun.twitter.R
import co.idearun.twitter.common.extension.invisible
import splitties.alertdialog.appcompat.*

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun openAlertDialog(msgTxt: String) {

        alertDialog {
            message = msgTxt
            okButton { cancelButton() }
            cancelButton()
        }.onShow {
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_red_light))
            negativeButton.setTextColor(resources.getColor(android.R.color.darker_gray))
            negativeButton.invisible()
            positiveButton.text = getString(R.string.ok)

        }.show()

    }


//    fun fillScoresOnUserFollowingData(followingList: ArrayList<User>, item: User): User {
//        var sumFollowingScore = 0
//        var numberOfFollowings = 1
//
//        numberOfFollowings = if (followingList.isNotEmpty()) {
//            followingList.size
//        } else {
//            1
//        }
//
//        var hasVerifiedFollowing = false
//
//        for (u in followingList) {
//            u.score_personal?.let {
//                sumFollowingScore += it
//            }
//
//            if (u.verified != null && u.verified!!) {
//                hasVerifiedFollowing = true
//            }
//
//        }
//
//        val followingScore = sumFollowingScore / numberOfFollowings
//
////        var totalScore = calcPersonalScore(item) + followingScore
//
//        if (hasVerifiedFollowing) {
//            totalScore += 1
//        }
//
//        if (totalScore > 10) {
//            totalScore = 10
//        }
//
//        item.score_following = followingScore
//        item.score_total = totalScore
//
//        return item
//    }


    override fun onResume() {
        super.onResume()
    }

}