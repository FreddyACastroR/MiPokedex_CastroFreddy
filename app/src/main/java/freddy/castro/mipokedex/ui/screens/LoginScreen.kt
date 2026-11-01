package freddy.castro.mipokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.ui.components.PokedexCard
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.viewmodel.LOGIN_ERROR

// Opacidad de la selección de texto, la misma que usa Material por defecto
private const val SELECTION_ALPHA = 0.4f

/**
 * Inicio de sesión: la pokébola con el nombre de la app sobre el fondo amarillo y, abajo, la
 * tarjeta blanca con el formulario. Si al abrirla ya había una sesión guardada no muestra
 * nada y avisa con [onLoggedIn] para irse directo al menú. Quien revisa si el correo y la
 * contraseña coinciden es el ViewModel; aquí solo se muestra [errorDescription] cuando hay
 * [loginError].
 */
@Composable
fun LoginScreen(
    isLoggedIn: Boolean,
    loginError: Boolean,
    errorDescription: String,
    onLoginClick: (mail: String, password: String) -> Unit,
    onLoggedIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoggedIn) {
        // Navegar es un efecto: se hace fuera de la composición y una sola vez
        LaunchedEffect(Unit) { onLoggedIn() }
    } else {
        // Los espacios flexibles centran la pokébola en lo amarillo y mandan la tarjeta al
        // fondo. Con el teclado abierto o la pantalla acostada se encogen a cero y todo se
        // desliza
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.weight(1f))
            LoginHeader()
            Spacer(Modifier.weight(1f))
            PokedexCard {
                LoginForm(
                    loginError = loginError,
                    errorDescription = errorDescription,
                    onLoginClick = onLoginClick,
                    modifier = Modifier
                        .widthIn(max = Dimens.LoginFormMaxWidth)
                        .padding(Dimens.MarginLarge),
                )
            }
        }
    }
}

@Composable
private fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.MarginMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_pokeball),
            contentDescription = null,
            modifier = Modifier.size(Dimens.LoginLogoSize),
        )
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

/**
 * Correo y contraseña. "Siguiente" en el teclado pasa a la contraseña y "Listo" equivale a
 * tocar "Iniciar sesión". Los campos se marcan para que el gestor de contraseñas los pueda
 * llenar.
 */
@Composable
private fun LoginForm(
    loginError: Boolean,
    errorDescription: String,
    onLoginClick: (mail: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Lo escrito sobrevive al giro de pantalla
    val email = rememberTextFieldState()
    val password = rememberTextFieldState()
    // Solo cambia cómo se ve el campo, por eso no está en el ViewModel
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    // Sin correo o sin contraseña no tiene caso intentar
    val canLogin = email.text.isNotBlank() && password.text.isNotEmpty()
    val focusManager = LocalFocusManager.current
    val login = {
        // Se esconde el teclado para que se vea el error debajo del botón
        focusManager.clearFocus()
        onLoginClick(email.text.toString(), password.text.toString())
    }
    val fieldColors = loginFieldColors()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        OutlinedTextField(
            state = email,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.EmailAddress },
            label = { Text(stringResource(R.string.login_email)) },
            isError = loginError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            colors = fieldColors,
        )
        // Oculta lo escrito y no deja copiarlo
        OutlinedSecureTextField(
            state = password,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Password },
            label = { Text(stringResource(R.string.login_password)) },
            trailingIcon = {
                PasswordVisibilityButton(
                    visible = passwordVisible,
                    onClick = { passwordVisible = !passwordVisible },
                )
            },
            isError = loginError,
            textObfuscationMode = if (passwordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            onKeyboardAction = { if (canLogin) login() },
            colors = fieldColors,
        )
        // Rojo como el botón de capturar
        Button(
            onClick = login,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.MarginMedium),
            enabled = canLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
        ) {
            Text(stringResource(R.string.login_button))
        }
        if (loginError) {
            Text(
                text = errorDescription,
                color = MaterialTheme.colorScheme.error,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/** El ojo dice lo que va a pasar al tocarlo: abierto muestra la contraseña, tachado la oculta. */
@Composable
private fun PasswordVisibilityButton(visible: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(
                if (visible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            ),
            contentDescription = stringResource(
                if (visible) R.string.cd_hide_password else R.string.cd_show_password,
            ),
        )
    }
}

/** El amarillo del tema casi no se ve sobre blanco: el campo con el foco se marca en gris. */
@Composable
private fun loginFieldColors(): TextFieldColors {
    val focused = MaterialTheme.colorScheme.onSurface
    return OutlinedTextFieldDefaults.colors(
        cursorColor = focused,
        selectionColors = TextSelectionColors(
            handleColor = focused,
            backgroundColor = focused.copy(alpha = SELECTION_ALPHA),
        ),
        focusedBorderColor = focused,
        focusedLabelColor = focused,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    MiPokedexTheme {
        LoginScreen(
            isLoggedIn = false,
            loginError = false,
            errorDescription = "",
            onLoginClick = { _, _ -> },
            onLoggedIn = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenErrorPreview() {
    MiPokedexTheme {
        LoginScreen(
            isLoggedIn = false,
            loginError = true,
            errorDescription = LOGIN_ERROR,
            onLoginClick = { _, _ -> },
            onLoggedIn = {},
        )
    }
}
