package com.example.chatapp.model.chatrooms

import android.util.Log
import com.example.chatapp.model.ChatMessage
import com.example.chatapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRoomsRepoImpl @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val fbDb: FirebaseDatabase
) :
    ChatRoomsRepo {
    override suspend fun loadMessagesOf(chatRoomUUID: String): Flow<Resource<List<ChatMessage>>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                val myUUID = fbAuth.currentUser?.uid.toString()
//                val chatRoomsUUID =
//                    fbDb.getReference(PROFILES_REFERENCE).child(myUUID).child(FRINDE_CAHTS)
//                        .child()
                val fbDbChatRoomsRef = fbDb.getReference("Chat_room").child(chatRoomUUID)
                val contactListener =
                    fbDbChatRoomsRef.addValueEventListener(object : ValueEventListener {
                        val chatMessages = mutableSetOf<ChatMessage>()
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                val message = child.getValue(ChatMessage::class.java)
                                if (message != null) {
                                    chatMessages.add(message)
                                }
                            }
                            Log.d("LIST_CONTACT", chatMessages.toString())
                            if (chatMessages.isNotEmpty())
                                this@callbackFlow.trySendBlocking(Resource.Success(chatMessages.toList()))
                            else
                                this@callbackFlow.trySendBlocking(Resource.Success(emptyList()))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            this@callbackFlow.trySendBlocking(Resource.Error(error.message))
                        }

                    })
                fbDbChatRoomsRef.addValueEventListener(contactListener)
                awaitClose {
                    fbDbChatRoomsRef.removeEventListener(contactListener)
                    channel.close()
                    cancel()
                }

            } catch (e: Exception) {
            }
        }
    }
}