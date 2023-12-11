package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.mapper.JweServiceMapper
import com.nebrija.pushit.api.security.domain.service.IJweService
import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jose.crypto.RSAEncrypter
import com.nimbusds.jwt.JWTClaimsSet
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Service
class JweService(
    private val mapper: JweServiceMapper
) : IJweService {

    private val keyPair: KeyPair = loadKeyPair()

    private fun loadKeyPair(): KeyPair {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

        val keyStoreFile = ClassPathResource("keystore.jks").inputStream
        keyStore.load(keyStoreFile, "push-it-project".toCharArray())

        val key = keyStore.getKey("push-it-security", "push-it-project".toCharArray())
        val publicKey = keyStore.getCertificate("push-it-security").publicKey
        val privateKey = key as RSAPrivateKey

        return KeyPair(publicKey, privateKey)
    }

    override fun generateToken(claims: Map<String, Any>, username: String): String {
        val expirationTime = Date(System.currentTimeMillis() + 3600 * 1000) // 1 Hour
        val jwtClaimsSetBuilder = JWTClaimsSet.Builder()
            .subject(username)
            .expirationTime(expirationTime)

        claims.forEach { (key, value) ->
            jwtClaimsSetBuilder.claim(key, value)
        }

        val jwtClaimsSet = jwtClaimsSetBuilder.build()
        return mapper.fromClaimsSet(jwtClaimsSet)?.let { encode(it) } ?: ""
    }

    override fun isTokenValid(token: String, username: String): Boolean {
        val email = extractEmail(token)
        return email == username && !isTokenExpired(token)
    }

    override fun isTokenExpired(token: String): Boolean {
        val claims = decode(token)
        val exp = claims?.get("exp") as? Long
        val expiration = exp?.let { Date(it * 1000) }
        return expiration?.before(Date()) ?: true
    }

    override fun extractEmail(token: String): String? {
        val jwtClaimsSet = decode(token)
        return jwtClaimsSet?.get("email") as String?
    }

    override fun extractAllClaims(token: String): Map<String, Any>? {
        return decode(token)
    }

    override fun encode(claims: Map<String, Any>): String {
        val jwtClaimsSet = mapper.toClaimsSet(claims)
        val jweHeader = JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).build()
        val jweObject = JWEObject(jweHeader, Payload(jwtClaimsSet.toJSONObject()))
        val encrypter = RSAEncrypter(keyPair.public as RSAPublicKey)
        jweObject.encrypt(encrypter)
        return jweObject.serialize()
    }

    override fun decode(token: String): Map<String, Any>? {
        val jweObject = JWEObject.parse(token)
        val decrypter = RSADecrypter(keyPair.private as RSAPrivateKey)
        jweObject.decrypt(decrypter)
        val jwtClaimsSet = JWTClaimsSet.parse(jweObject.payload.toString())
        return mapper.fromClaimsSet(jwtClaimsSet)
    }
}