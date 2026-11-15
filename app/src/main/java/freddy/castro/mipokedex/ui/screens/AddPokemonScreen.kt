package freddy.castro.mipokedex.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.components.SectionLabel
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.viewmodel.AddPokemonViewModel

private const val SELECTION_ALPHA = 0.4f

private const val DESCRIPTION_MAX_LINES = 3

private val DigitsOnly = InputTransformation {
    if (!asCharSequence().all { it.isDigit() }) revertAllChanges()
}

@Composable
fun AddPokemonScreen(
    innerPadding: PaddingValues,
    addPokemonViewModel: AddPokemonViewModel = viewModel(factory = AddPokemonViewModel.Factory),
) {
    val pokemonList by addPokemonViewModel.pokemons.collectAsStateWithLifecycle()
    AddPokemonContent(
        innerPadding = innerPadding,
        pokemonList = pokemonList,
        pokemonSaved = addPokemonViewModel.pokemonSaved,
        onSaveClick = addPokemonViewModel::savePokemon,
    )
}

@Composable
private fun AddPokemonContent(
    innerPadding: PaddingValues,
    pokemonList: List<Pokemon>,
    pokemonSaved: Boolean,
    onSaveClick: (name: String, type: String, number: Int, description: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .imePadding(),
        contentPadding = PaddingValues(vertical = Dimens.MarginMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        item {
            SectionLabel(stringResource(R.string.label_add_pokemon))
        }
        item {
            PokemonForm(pokemonSaved = pokemonSaved, onSaveClick = onSaveClick)
        }
        item {
            SectionLabel(
                text = stringResource(R.string.label_saved_pokemons),
                modifier = Modifier.padding(top = Dimens.MarginMedium),
            )
        }
        if (pokemonList.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.saved_empty),
                    modifier = Modifier.padding(horizontal = Dimens.MarginMedium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        items(pokemonList) { pokemon ->
            Text(
                text = pokemon.toString(),
                modifier = Modifier.padding(horizontal = Dimens.MarginMedium),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun PokemonForm(
    pokemonSaved: Boolean,
    onSaveClick: (name: String, type: String, number: Int, description: String) -> Unit,
) {
    val number = rememberTextFieldState()
    val name = rememberTextFieldState()
    val type = rememberTextFieldState()
    val description = rememberTextFieldState()
    val pokemonNumber = number.text.toString().toIntOrNull()?.takeIf { it > 0 }
    val canSave = pokemonNumber != null && name.text.isNotBlank()
    val focusManager = LocalFocusManager.current
    val save = {
        if (pokemonNumber != null && canSave) {
            onSaveClick(
                name.text.toString(),
                type.text.toString(),
                pokemonNumber,
                description.text.toString(),
            )
            listOf(number, name, type, description).forEach { it.clearText() }
            focusManager.clearFocus()
        }
    }
    val fieldColors = pokemonFieldColors()
    Column(
        modifier = Modifier.padding(horizontal = Dimens.MarginMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        PokemonField(
            state = number,
            label = R.string.add_number,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            inputTransformation = DigitsOnly,
        )
        PokemonField(
            state = name,
            label = R.string.add_name,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        PokemonField(
            state = type,
            label = R.string.add_type,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        OutlinedTextField(
            state = description,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.add_description)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = DESCRIPTION_MAX_LINES),
            colors = fieldColors,
        )
        Button(
            onClick = save,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.MarginSmall),
            enabled = canSave,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
        ) {
            Text(stringResource(R.string.add_save))
        }
        if (pokemonSaved) {
            Text(
                text = stringResource(R.string.add_saved),
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun PokemonField(
    state: TextFieldState,
    @StringRes label: Int,
    colors: TextFieldColors,
    keyboardOptions: KeyboardOptions,
    inputTransformation: InputTransformation? = null,
) {
    OutlinedTextField(
        state = state,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(label)) },
        inputTransformation = inputTransformation,
        keyboardOptions = keyboardOptions,
        lineLimits = TextFieldLineLimits.SingleLine,
        colors = colors,
    )
}

@Composable
private fun pokemonFieldColors(): TextFieldColors {
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

@Preview(showBackground = true)
@Composable
private fun AddPokemonScreenPreview() {
    MiPokedexTheme {
        AddPokemonContent(
            innerPadding = PaddingValues(),
            pokemonList = pokemonList.take(2),
            pokemonSaved = true,
            onSaveClick = { _, _, _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPokemonScreenEmptyPreview() {
    MiPokedexTheme {
        AddPokemonContent(
            innerPadding = PaddingValues(),
            pokemonList = emptyList(),
            pokemonSaved = false,
            onSaveClick = { _, _, _, _ -> },
        )
    }
}
