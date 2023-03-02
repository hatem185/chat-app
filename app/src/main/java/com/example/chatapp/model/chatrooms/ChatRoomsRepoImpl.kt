package com.example.chatapp.model.chatrooms

import android.util.Log
import com.example.chatapp.model.ChatMessage
import com.example.chatapp.util.FirebaseConstants.CHAT_ROOM_REFRNCE
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
                val fbDbChatRoomsRef = fbDb.getReference(CHAT_ROOM_REFRNCE).child(chatRoomUUID)
                val roomMessagesListner =
                    object : ValueEventListener {
                        val chatMessages = mutableSetOf<ChatMessage>()
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                val message = child.getValue(ChatMessage::class.java)
                                if (message != null) {
                                    chatMessages.add(message)
                                }
                            }
                            if (chatMessages.isNotEmpty()) {
                                Log.d("LIST_MESSAGE", chatMessages.toString())
                                this@callbackFlow.trySendBlocking(Resource.Success(chatMessages.toList()))
                            } else
                                this@callbackFlow.trySendBlocking(Resource.Success(emptyList()))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            this@callbackFlow.trySendBlocking(Resource.Error(error.message))
                        }

                    }
                fbDbChatRoomsRef.addValueEventListener(roomMessagesListner)
                awaitClose {
                    fbDbChatRoomsRef.removeEventListener(roomMessagesListner)
                    channel.close()
                    cancel()
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Resource.Error(e.message.toString()))
            }
        }
    }

    override suspend fun sendMessage(
        message: String,
        status: String,
        roomChatUIID: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val userUIID = fbAuth.currentUser?.uid.toString()
            val chatRoom = fbDb.getReference(CHAT_ROOM_REFRNCE).child(roomChatUIID)
            chatRoom.push().setValue(
                ChatMessage(
                    profileUUID = userUIID,
                    message = message,
                    status = status
                )
            )
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

}