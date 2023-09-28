package com.finite.gdscphcet.repository

import com.finite.gdscphcet.model.TeamMember
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object TeamRepo {

    private val dbRef = FirebaseDatabase.getInstance().reference.child("team")

    suspend fun getLeadDetails(): TeamMember = suspendCoroutine { continuation ->
        dbRef.child("lead").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lead = snapshot.getValue(TeamMember::class.java)
                continuation.resume(lead ?: TeamMember())
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    suspend fun getSeniorCoreTeamDetails(): List<TeamMember> = suspendCoroutine { continuation ->
        val seniorMembers = mutableListOf<TeamMember>()

        dbRef.child("senior").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val seniorMember = it.getValue(TeamMember::class.java)
                    if (seniorMember != null) {
                        seniorMembers.add(seniorMember)
                    }
                }
                continuation.resume(seniorMembers)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    suspend fun getJuniorCoreTeamDetails(): List<TeamMember> = suspendCoroutine { continuation ->
        val juniorMembers = mutableListOf<TeamMember>()

        dbRef.child("junior").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val juniorMember = it.getValue(TeamMember::class.java)
                    if (juniorMember != null) {
                        juniorMembers.add(juniorMember)
                    }
                }
                continuation.resume(juniorMembers)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}