package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.mapper.JweServiceMapper
import com.nebrija.pushit.api.security.domain.exception.JwtEmailExtractionException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenExpiredException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenInvalidException
import com.nebrija.pushit.api.security.domain.model.Security
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

    override fun generateClaims(security: Security): Map<String, Any> {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)
        val generatedClaims = mapper.toClaims(security)
        jweLogger.info("Generated claims: $generatedClaims") //Debugging Purposes TODO: Remove
        return generatedClaims
    }

    override fun generateToken(security: Security): String {
        val claims = generateClaims(security)
        return encode(claims)
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
        } catch (ex: Exception) {
            true
        }
    }

    override fun extractEmail(token: String): String? {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        val claims = extractAllClaims(token)
        val email = claims?.get("email") as String?

        jweLogger.info("Extracted email: $email") //Debugging Purposes TODO: Remove

        return email
    }
    override fun extractAllClaims(token: String): Map<String, Any>? {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        val jsonObject = decode(token)
        val claims = jsonObject?.toMap()
        jweLogger.info("Extracted claims: $claims") //Debugging Purposes TODO: Remove

        return claims
    }

    override fun encode(claims: Map<String, Any>): String {
        val jweHeader = JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128CBC_HS256)
        val jwtClaimsSet = mapper.toClaimsSet(claims)
        val jweObject = JWEObject(jweHeader, Payload(jwtClaimsSet.toJSONObject()))

        jweObject.encrypt(RSAEncrypter(keyPair.public as RSAPublicKey))

        return jweObject.serialize()
    }

    override fun decode(token: String): Map<String, Any>? {
        val jweObject = JWEObject.parse(token)
        jweObject.decrypt(RSADecrypter(keyPair.private as RSAPrivateKey))

        return jweObject.payload.toJSONObject()
    }

    override fun validateToken(token: String, username: String) {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        val claims = extractAllClaims(token)
        val email = claims?.get("email") as String?

        if (email == null) {
            jweLogger.error("Email not found") //Debugging Purposes TODO: Remove
            throw JwtEmailExtractionException()
        }

        if (email != username) {
            jweLogger.error("Email does not match") //Debugging Purposes TODO: Remove
            throw JwtTokenInvalidException()
        }

        validateTokenNotExpired(token)
    }

    private fun validateTokenNotExpired(token: String) {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        val claims = extractAllClaims(token)
        val expirationDate = claims?.get("exp") as Date?

        if (expirationDate == null) {
            jweLogger.error("Expiration date not found") //Debugging Purposes TODO: Remove
            throw JwtTokenInvalidException()
        }

        if (expirationDate.before(Date())) {
            jweLogger.error("Token expired") //Debugging Purposes TODO: Remove
            throw JwtTokenExpiredException()
        }
    }
}