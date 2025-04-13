package thai.phph48495.asm.service

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import thai.phph48495.asm.contants.Contants
import thai.phph48495.asm.product.Product
import thai.phph48495.asm.profile.User


interface AuthService {


    companion object {
        private var retrofitService: AuthService? = null
        fun getInstance(): AuthService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(AuthService::class.java)
            }
            return retrofitService!!
        }
    }
}