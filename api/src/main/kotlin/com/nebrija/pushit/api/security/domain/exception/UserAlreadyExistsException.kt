package com.nebrija.pushit.api.security.domain.exception

class UserAlreadyExistsException : RuntimeException("User already exists") //Info leak, just for demo purposes