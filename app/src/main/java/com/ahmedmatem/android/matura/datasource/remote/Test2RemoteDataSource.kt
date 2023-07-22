package com.ahmedmatem.android.matura.datasource.remote

import android.content.ContentResolver
import android.net.Uri
import com.ahmedmatem.android.matura.infrastructure.BASE_API_URL
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.models.Test2
import com.ahmedmatem.android.matura.network.services.Test2Api
import com.ahmedmatem.android.matura.network.services.Test2ApiService
import com.ahmedmatem.android.matura.utils.providers.ContentResolverProvider
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent
import java.io.File

class Test2RemoteDataSource {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val apiService: Test2ApiService = Test2Api.retrofitService
    private val firebaseStorage: FirebaseStorage by lazy { Firebase.storage }

    fun createTest2() : Flow<Result<Test2>> = flow {
        val result = safeApiCall(dispatcher) {
            apiService.createTest2()
        }
        emit(result)
    }

    fun getMockTest() : Flow<Result<Test2>> = flow {
        val result = safeApiCall(dispatcher) {
            apiService.getMockTest()
        }
        emit(result)
    }

    fun uploadSolution(path: String) : UploadTask {
        val fileName = path.split("/").last()
        val ref = firebaseStorage.reference.child("${SOLUTION_REF}/fileName")
        return ref.putFile(Uri.parse(path))
    }

    companion object {
        const val SOLUTION_REF = "solutions"
    }
}