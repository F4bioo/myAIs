package com.fappslab.myais.remote.api

import com.fappslab.myais.remote.model.DriveFileMetadata
import com.fappslab.myais.remote.model.DriveFileResponse
import com.fappslab.myais.remote.model.DriveFilesResponse
import com.fappslab.myais.remote.model.DriveFolderRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

const val QUERY_FILES_ONLY = "'me' in owners and mimeType != 'application/vnd.google-apps.folder'"

private val fieldsProvider: String
    get() {
        val fieldsList = listOf(
            "id", "name", "mimeType", "description", "parents",
            "webViewLink", "webContentLink", "createdTime",
            "owners(photoLink,displayName,emailAddress)", "size", "thumbnailLink"
        )
        return "files(${fieldsList.joinToString(separator = ",")}),nextPageToken"
    }

internal interface DriveService {

    @GET("drive/v3/files")
    suspend fun listFiles(
        @Query("q") query: String = QUERY_FILES_ONLY,
        @Query("spaces") spaces: String,
        @Query("fields") fields: String = fieldsProvider,
        @Query("pageToken") pageToken: String? = null,
        @Query("pageSize") pageSize: Int = 10
    ): DriveFilesResponse

    @DELETE("drive/v3/files/{itemId}")
    suspend fun deleteItem(
        @Path("itemId") itemId: String
    ): Response<Unit>

    @Multipart
    @POST("upload/drive/v3/files?uploadType=multipart")
    suspend fun uploadFile(
        @Part("metadata") metadata: DriveFileMetadata,
        @Part file: MultipartBody.Part
    ): DriveFileResponse

    @POST("drive/v3/files")
    suspend fun createFolder(
        @Body request: DriveFolderRequest
    ): DriveFileResponse
}