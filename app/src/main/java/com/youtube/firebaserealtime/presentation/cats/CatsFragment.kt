package com.youtube.firebaserealtime.presentation.cats

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.popAnimation
import com.youtube.firebaserealtime.extensions.slideAnimation
import com.youtube.firebaserealtime.extensions.zipLiveData

class CatsFragment : Fragment() {
    private val viewModel by viewModels<CatsViewModel>()
    private lateinit var myCL: ConstraintLayout
    private lateinit var myToolbar: Toolbar
    private lateinit var tvName: TextView
    private lateinit var lvLoading: LottieAnimationView
    private lateinit var likeButton: ImageButton
    private lateinit var tvLikesCounter: TextView
    private lateinit var backButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var vpCats: ViewPager2
    private var likeLD: LiveData<Int>? = null
    private var myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            likeLD?.removeObservers(viewLifecycleOwner)
            viewModel.changePage(position)
            likeLD = viewModel.getLikesLD()
            likeLD?.observe(viewLifecycleOwner, Observer {
                tvLikesCounter.text = getString(R.string.likes, it)
            })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cats, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
        with(viewModel) {
            catsLD.observe(viewLifecycleOwner, Observer {
                vpCats.adapter = CatsAdapter(requireContext(), it)
            })
            arrowsLD.observe(viewLifecycleOwner, Observer {
                backButton.isVisible = it.backEnable
                nextButton.isVisible = it.nextEnable
            })
            catNameLD.observe(viewLifecycleOwner, Observer {
                tvName.text = it
            })
            val myZipLD = zipLiveData(animationEndsLD, catsLD)
            myZipLD.observe(viewLifecycleOwner, Observer {
                lvLoading.isVisible = false
                lvLoading.removeAllAnimatorListeners()
                myCL.isInvisible = false
                myZipLD.removeObservers(viewLifecycleOwner)
            })
            likeLD = getLikesLD()
            likeLD?.observe(viewLifecycleOwner, Observer {
                tvLikesCounter.text = getString(R.string.likes, it)
            })
            goToLoginLD.observe(viewLifecycleOwner) {
                findNavController().navigate(
                    CatsFragmentDirections.actionToLogin(),
                    popAnimation(R.id.catsFragment).build()
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()
        vpCats.registerOnPageChangeCallback(myPageChangeCallback)
    }
    override fun onPause() {
        super.onPause()
        vpCats.unregisterOnPageChangeCallback(myPageChangeCallback)
    }
    private fun setupUI(view: View) {
        myCL = view.findViewById(R.id.myCL)
        myCL.isInvisible = true
        lvLoading = view.findViewById(R.id.lvLoading)
        lvLoading.playAnimation()
        lvLoading.speed = 3f
        lvLoading.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                viewModel.animationEnds()
            }
        })
        myToolbar = view.findViewById(R.id.myToolbar)
        myToolbar.bringToFront()
        tvName = view.findViewById(R.id.tvName)
        likeButton = view.findViewById(R.id.ivLike)
        tvLikesCounter = view.findViewById(R.id.tvLikesCounter)
        backButton = view.findViewById(R.id.ivBack)
        nextButton = view.findViewById(R.id.ivNext)
        vpCats = view.findViewById(R.id.vpCats)
        backButton.setOnClickListener {
            vpCats.currentItem = vpCats.currentItem.minus(1)
        }
        nextButton.setOnClickListener {
            vpCats.currentItem = vpCats.currentItem.plus(1)
        }
        likeButton.setOnClickListener { viewModel.like() }
        view.findViewById<ImageButton>(R.id.btLogout).setOnClickListener {
            viewModel.logout()
        }
    }
}
