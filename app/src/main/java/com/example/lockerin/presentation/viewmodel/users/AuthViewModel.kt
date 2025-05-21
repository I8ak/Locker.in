package com.example.lockerin.presentation.viewmodel.users

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth // Asegúrate de tener esta importación si usas getInstance()
import com.google.firebase.auth.ktx.auth // Asegúrate de tener esta importación si usas Firebase.auth
import com.google.firebase.FirebaseException // Importar FirebaseException para logging más detallado
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider


enum class AuthState{
    LOGGED_IN,
    LOGGED_OUT,
    LOADING,
    ERROR
}

class AuthViewModel : ViewModel(){
    private val firebaseAuth= Firebase.auth
    private val firestore = FirebaseFirestore.getInstance()


    // Propiedad para obtener solo el UID del usuario actual
    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState: StateFlow<AuthState> = _authState
    init {
        Log.d("AuthViewModel", "ViewModel init: Estableciendo idioma a 'es'")
        firebaseAuth.setLanguageCode("es")
        Log.d("AuthViewModel", "ViewModel init: Idioma establecido. Idioma actual reportado por Auth: ${firebaseAuth.languageCode}")

        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = if (auth.currentUser != null) {
                AuthState.LOGGED_IN
            } else {
                AuthState.LOGGED_OUT
            }
        }
    }
    fun createUser(
        name: String,
        email: String,
        password: String,
        role: String,
        onComplete: (Boolean, String?) -> Unit
    ){
        _authState.value= AuthState.LOADING
        Log.d("AuthViewModel", "createUser: $name, $email, $password, $role")
         if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             onComplete(false, "Formato de correo electrónico inválido.")
             return
         }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val firebaseUser = firebaseAuth.currentUser
                    firebaseUser?.let {
                        Log.d("AuthViewModel", "Guardando datos en Firestore para UID: ${it.uid}")
                        val newUser= User(
                            userID = it.uid,
                            name = name,
                            email = email,
                            role = role
                        )
                        firestore.collection("users").document(it.uid).set(newUser)
                            .addOnSuccessListener {
                                Log.d("AuthViewModel", "Datos de usuario guardados en Firestore.")
                                _authState.value = AuthState.LOGGED_IN
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                Log.e("AuthViewModel", "Error al guardar en Firestore: ${e.message}", e)
                                _authState.value = AuthState.LOGGED_OUT
                                onComplete(false, e.message)
                            }
                    }?:run {
                        Log.e("AuthViewModel", "Error interno: Usuario de Auth nulo después de createUser exitoso.")
                        _authState.value = AuthState.LOGGED_OUT
                        onComplete(false, "Error al obtener el usuario")
                    }
                }else{
                    val exception = task.exception
                    val errorMessage = when (exception) {
                        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo."
                        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña inválidos."
                        else -> exception?.message ?: "Error desconocido."
                    }

                    Log.e("AuthViewModel", "Error al crear usuario: $errorMessage", exception)
                    _authState.value = AuthState.LOGGED_OUT
                    onComplete(false, errorMessage)
                }
            }
    }
    fun signIn(
        email: String,
        password: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        if (email.isEmpty()) {
            onComplete(false, "El correo electrónico no puede estar vacío.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onComplete(false, "Formato de correo electrónico inválido.")
            return
        }

        _authState.value = AuthState.LOADING

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.LOGGED_IN
                    onComplete(true, null)
                } else {
                    _authState.value = AuthState.LOGGED_OUT

                    val errorMessage = when {
                        task.exception?.message?.contains("The supplied auth credential is incorrect", ignoreCase = true) == true ||
                                task.exception?.message?.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) == true -> {
                            "La cuenta no existe o la contraseña y/o el email son incorrectos."
                        }

                        task.exception?.message?.contains("network error", ignoreCase = true) == true ||
                                task.exception?.message?.contains("A network error", ignoreCase = true) == true -> {
                            "No hay conexión a internet."
                        }

                        else -> {
                            "Ocurrió un error inesperado: ${task.exception?.localizedMessage ?: "desconocido"}"
                        }
                    }

                    onComplete(false, errorMessage)
                }
            }
    }

    fun signInWithGoogle(idToken: String, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        _authState.value = AuthState.LOADING

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val userRef = firestore.collection("users").document(user.uid)

                        userRef.get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val newUser = User(
                                    userID = user.uid,
                                    name = user.displayName ?: "",
                                    email = user.email ?: "",
                                    role = "user"
                                )
                                userRef.set(newUser)
                                    .addOnSuccessListener {
                                        _authState.value = AuthState.LOGGED_IN
                                        onComplete(true, null)
                                    }
                                    .addOnFailureListener { e ->
                                        _authState.value = AuthState.LOGGED_OUT
                                        onComplete(false, "Error al guardar usuario: ${e.message}")
                                    }
                            } else {
                                _authState.value = AuthState.LOGGED_IN
                                onComplete(true, null)
                            }
                        }.addOnFailureListener { e ->
                            _authState.value = AuthState.LOGGED_OUT
                            onComplete(false, "Error al verificar usuario: ${e.message}")
                        }
                    } else {
                        _authState.value = AuthState.LOGGED_OUT
                        onComplete(false, "No se pudo obtener el usuario de Firebase.")
                    }
                } else {
                    _authState.value = AuthState.LOGGED_OUT
                    onComplete(false, "Error de autenticación con Google: ${task.exception?.localizedMessage}")
                }
            }
    }


    fun signOut(){
        viewModelScope.launch {
            firebaseAuth.signOut()
            _authState.value = AuthState.LOGGED_OUT
        }
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = firebaseAuth.currentUser
        val email = user?.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onComplete(true, null)
                                } else {
                                    onComplete(false, task.exception?.message)
                                }
                            }
                    } else {
                        onComplete(false, "Contraseña actual incorrecta")
                    }
                }
        } else {
            onComplete(false, "Usuario no autenticado")
        }
    }


    fun deleteUser(onComplete: (Boolean, String?) -> Unit){
        val user = firebaseAuth.currentUser
        if(user != null){
            user.delete()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        firestore.collection("users").document(user.uid).delete()
                            .addOnSuccessListener {
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    }else{
                        onComplete(false, task.exception?.message)
                    }
                }
        }else{
            onComplete(false, "Usuario no autenticado")
        }
    }

    fun sendPasswordResetEmail(
        email: String,
        onComplete:  (Boolean, String?) -> Unit
    ) {
        if (email.isEmpty()) {
            onComplete(false, "El correo electrónico no puede estar vacío.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onComplete(false, "Formato de correo electrónico inválido.")
            return
        }

        Log.d("AuthViewModel", "Intentando enviar correo de reseteo a: $email")

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Correo de reseteo enviado exitosamente a $email")
                    onComplete(true, null)
                } else {
                    Log.e("AuthViewModel", "Error al enviar correo de reseteo a $email: ${task.exception?.message}", task.exception)
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "No hay usuario registrado con este correo."
                        is FirebaseAuthInvalidCredentialsException -> "El formato del correo es inválido."
                        else -> task.exception?.message ?: "Error desconocido al enviar correo."
                    }
                    onComplete(false, errorMessage)
                }
            }
    }



}