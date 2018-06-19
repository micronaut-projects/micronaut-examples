package example.mail

import example.api.v1.Email

import java.io.IOException

interface EmailService {
    fun send(email: Email)
}
