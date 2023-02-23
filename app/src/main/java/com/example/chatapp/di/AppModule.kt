package com.example.chatapp.di

import com.example.chatapp.model.auth.AuthRepo
import com.example.chatapp.model.auth.AuthRepoImpl
import com.example.chatapp.model.chatrooms.ChatRoomsRepo
import com.example.chatapp.model.chatrooms.ChatRoomsRepoImpl
import com.example.chatapp.model.contacts.ContactsRepo
import com.example.chatapp.model.contacts.ContactsRepoImpl
import com.example.chatapp.model.profleuser.ProfileUserRepo
import com.example.chatapp.model.profleuser.ProfileUserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseStoarge(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesRepoImpl(fbAuth: FirebaseAuth, fbDb: FirebaseDatabase): AuthRepo =
        AuthRepoImpl(fbAuth, fbDb)

    @Provides
    @Singleton
    fun providesContactsRepoImpl(fbAuth: FirebaseAuth, fbDb: FirebaseDatabase): ContactsRepo =
        ContactsRepoImpl(fbAuth, fbDb)
@Provides
    @Singleton
    fun providesChatRoomsRepoImpl(fbAuth: FirebaseAuth, fbDb: FirebaseDatabase): ChatRoomsRepo =
    ChatRoomsRepoImpl(fbAuth, fbDb)

    @Provides
    @Singleton
    fun provideProfileScreenRepository(
        fbAuth: FirebaseAuth,
        fbDb: FirebaseDatabase,
        fbStorage: FirebaseStorage
    ): ProfileUserRepo = ProfileUserRepoImpl(fbAuth, fbDb, fbStorage)

}