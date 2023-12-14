package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.mapper.JweServiceMapper
import com.nebrija.pushit.api.security.domain.exception.JwtEmailExtractionException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenExpiredException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenInvalidException
import com.nebrija.pushit.api.security.domain.service.IJweService
import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jose.crypto.RSAEncrypter
import com.nimbusds.jwt.JWTClaimsSet
import org.slf4j.LoggerFactory
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
        val expirationTime = Date(System.currentTimeMillis() + 3600 * 1000) // 1 hora de expiración

        val jwtClaims = JWTClaimsSet.Builder()
            .claim("email", username)
            .expirationTime(expirationTime)
            .apply {
                claims.forEach { (key, value) ->
                    claim(key, value)
                }
            }
            .build()
            .claims

        return encode(jwtClaims)
    }

    override fun isTokenValid(token: String, username: String): Boolean {
        return try {
            validateToken(token, username)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override fun isTokenExpired(token: String): Boolean {
        return try {
            validateTokenNotExpired(token)
            false
        } catch (ex: JwtTokenExpiredException) {
            true
        }
    }

    override fun extractEmail(token: String): String? {
        val claims = decode(token)
        val nestedClaims = claims?.get("claims") as? Map<String, Any>
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)
        jweLogger.info("Nested claims: $nestedClaims") //Debugging Purposes TODO: Remove
        return nestedClaims?.get("email") as? String
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
        return jwtClaimsSet.claims
    }

    override fun validateToken(token: String, username: String) {
        val email = extractEmail(token) ?: throw JwtEmailExtractionException()
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)
        jweLogger.info("Extracted email: $email") //Debugging Purposes TODO: Remove
        if (email != username) {
            jweLogger.error("Email does not match username") //Debugging Purposes TODO: Remove
            throw JwtTokenInvalidException()
        }
        try {
            validateTokenNotExpired(token)
        } catch (ex: JwtTokenExpiredException) {
            jweLogger.error("Token expired", ex) // Debugging Purposes TODO: Remove
            throw ex
        }
    }

    private fun validateTokenNotExpired(token: String) {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        val claims = decode(token)
        val nestedClaims = claims?.get("claims") as? Map<String, Any>
        val exp = nestedClaims?.get("exp") as? Long
        jweLogger.info("Expiration: $exp") //Debugging Purposes TODO: Remove
        val expiration = exp?.let { Date(it * 1000) }
        jweLogger.info("Expiration date: $expiration") //Debugging Purposes TODO: Remove
        val isExpired = expiration?.before(Date()) ?: true
        if (isExpired) {
            throw JwtTokenExpiredException()
        }
    }
}