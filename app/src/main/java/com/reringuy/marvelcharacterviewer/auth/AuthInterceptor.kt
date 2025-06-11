package com.reringuy.marvelcharacterviewer.auth

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.collectToken().firstOrNull()
        }
        val mainRequest = chain.request().newBuilder()
        if (token != null)
            mainRequest.addHeader("Authorization", "Bearer $token")

        try {
            val response = chain.proceed(mainRequest.build())

            if (response.code() == 403 && token != null) {
                runBlocking {
                    tokenManager.clearToken()
                }
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}