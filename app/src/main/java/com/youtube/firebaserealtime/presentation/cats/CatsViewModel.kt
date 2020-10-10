package com.youtube.firebaserealtime.presentation.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.youtube.firebaserealtime.models.Cat
import com.youtube.firebaserealtime.models.Like
import com.youtube.firebaserealtime.presentation.cats.uistates.ArrowsState
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

internal class CatsViewModel : ViewModel() {
    private val database = Firebase.database
    private val _catsMLD = MutableLiveData<List<Cat>>()
    private val _catNameMLD = MutableLiveData<String>()
    private val _arrowsMLD = MutableLiveData<ArrowsState>()
    private val _animationEndsMLD = MutableLiveData<Int>()
    private var currentPage = 0
    val catsLD: LiveData<List<Cat>>
        get() = _catsMLD
    val arrowsLD: LiveData<ArrowsState>
        get() = _arrowsMLD
    val animationEndsLD: LiveData<Int>
        get() = _animationEndsMLD
    val catNameLD: LiveData<String>
        get() = _catNameMLD

    init {
        readCatsOnce()
//        readCatsAlways()
    }
    fun changePage(page: Int) {
        currentPage = page
        val catsList = catsLD.value
        if(catsList!=null) {
            val catsLength = catsList.size
            val lastPage = catsLength-1
            var backIsVisible: Boolean
            var nextIsVisible: Boolean
            when(page) {
                0 -> {
                    backIsVisible = false
                    nextIsVisible = true
                    if (catsLength == lastPage) {
                        nextIsVisible = false
                    }
                }
                lastPage -> {
                    backIsVisible = true
                    nextIsVisible = false
                }
                in 1 until lastPage -> {
                    backIsVisible = true
                    nextIsVisible = true
                }
                else -> {
                    backIsVisible = false
                    nextIsVisible = false
                }
            }
            _arrowsMLD.value = ArrowsState(backIsVisible, nextIsVisible)
            _catNameMLD.value = catsList[page].name
        } else {
            _arrowsMLD.value = ArrowsState(false, false)
        }
    }
    fun getLikesLD(): LiveData<Int>? {
        _catsMLD.value?.also {
            val likesLD = MutableLiveData<Int>()
            val cat = it[currentPage]
            database.getReference("likes").child(cat.id)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        likesLD.value = snapshot.childrenCount.toInt()
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            return likesLD
        }
        return null
    }
    fun like() {
        _catsMLD.value?.also {
            val catID = it[currentPage].id
            val userID = UUID.randomUUID().toString()
            val likesCatRef = database.getReference("likes").child(catID).child(userID)
            val like = Like(userID, catID, currentDate())
            likesCatRef.setValue(like)
        }
    }
    fun animationEnds() {
        _animationEndsMLD.value = 0
    }
    private fun readCatsOnce() {
        val catsRef = database.getReference("cats")
        catsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = ArrayList<Cat>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Cat::class.java)?.also { temp.add(it) }
                }
                _catsMLD.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun readCatsAlways() {
        val catsRef = database.getReference("cats")
        catsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = ArrayList<Cat>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Cat::class.java)?.also { temp.add(it) }
                }
                _catsMLD.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun currentDate(): String {
        val format = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss")
        return format.format(Date())
    }
    /**
     * Helper function just to perfom a hello world in firebase realtime
     */
    fun helloWorld() {
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")
    }
}