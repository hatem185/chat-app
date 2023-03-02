package com.example.chatapp.model.contacts

import android.util.Log
import com.example.chatapp.model.ChatMessage
import com.example.chatapp.model.Contact
import com.example.chatapp.util.FirebaseConstants
import com.example.chatapp.util.FirebaseConstants.CHAT_ROOM_REFRNCE
import com.example.chatapp.util.FirebaseConstants.CONTACTS_REFRNCE
import com.example.chatapp.util.MessageConstants
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
import kotlinx.coroutines.tasks.await
import java.util.*

class ContactsRepoImpl(
    private val fbAuth: FirebaseAuth,
    private val fbDb: FirebaseDatabase
) : ContactsRepo {
    override suspend fun loadContactsList(): Flow<Resource<List<Contact>>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                val myUUID = fbAuth.currentUser?.uid
                val fbDbRef = fbDb.getReference(CONTACTS_REFRNCE)
                val contactListener =
                    object : ValueEventListener {
                        val contactsList = mutableSetOf<Contact>()
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                val contact = child.getValue(Contact::class.java)
                                if (contact != null && contact.contactUserUUID != myUUID) {
                                    contactsList.add(contact)
                                }
                            }
                            Log.d("LIST_CONTACT", contactsList.toString())
                            if (contactsList.isNotEmpty())
                                this@callbackFlow.trySendBlocking(Resource.Success(contactsList.toList()))
                            else
                                this@callbackFlow.trySendBlocking(Resource.Success(emptyList()))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            this@callbackFlow.trySendBlocking(Resource.Error(error.message))
                        }

                    }
                fbDbRef.addValueEventListener(contactListener)
                awaitClose {
                    fbDbRef.removeEventListener(contactListener)
                    channel.close()
                    cancel()
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Resource.Error(e.message.toString()))
            }
        }
    }

    override suspend fun isRoomChatExists(requesterUUID: String): Flow<Resource<String>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                val userUUID = fbAuth.currentUser?.uid.toString()
                val frindeChatRef =
                    fbDb.getReference(FirebaseConstants.PROFILES_REFERENCE)
                        .child(userUUID)
                        .child(FirebaseConstants.FRINDE_CAHTS)
                        .child(requesterUUID)
                val frindeChatListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val frindeChatExists = snapshot.exists()
                        Log.d("WELCOME", "frinde Chat $frindeChatExists")
                        val chatRoomUUID = if (frindeChatExists)
                            snapshot.getValue(String::class.java) ?: ""
                        else ""
                        this@callbackFlow.trySendBlocking(Resource.Success(chatRoomUUID))
                    }

                    override fun onCancelled(e: DatabaseError) {
                        this@callbackFlow.trySendBlocking(Resource.Error(e.message))
                    }
                }
                frindeChatRef.addListenerForSingleValueEvent(frindeChatListener)
                awaitClose {
                    frindeChatRef.removeEventListener(frindeChatListener)
                    channel.close()
                    cancel()
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(
                    Resource.Error(
                        e.message ?: MessageConstants.ERROR_MESSAGE
                    )
                )
            }
        }
    }

    override suspend fun createChatRoomWithFrinde(contactUUID: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            val userUUID = fbAuth.currentUser?.uid.toString()
            val frindeChatMeRef =
                fbDb.getReference(FirebaseConstants.PROFILES_REFERENCE).child(userUUID).child(
                    FirebaseConstants.FRINDE_CAHTS
                ).child(contactUUID)

            val frindeChatRequserRef =
                fbDb.getReference(FirebaseConstants.PROFILES_REFERENCE).child(contactUUID).child(
                    FirebaseConstants.FRINDE_CAHTS
                ).child(userUUID)

            val cahtRoomUUID = UUID.randomUUID().toString()
            frindeChatMeRef.setValue(cahtRoomUUID).await()
            frindeChatRequserRef.setValue(cahtRoomUUID).await()
            val chatRooms = fbDb.getReference(CHAT_ROOM_REFRNCE).child(cahtRoomUUID)
            chatRooms.setValue(
                listOf(
                    ChatMessage(
                        profileUUID = userUUID,
                        message = "hello",
                        date = System.currentTimeMillis(),
                        status = "Read"
                    )
                )
            )

            emit(Resource.Success(cahtRoomUUID))
        }.catch {
            emit(Resource.Error(it.message ?: MessageConstants.ERROR_MESSAGE))
        }
    }
}