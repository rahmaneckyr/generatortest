#!/bin/bash

# Debug function to print messages
debug() {
    echo "[DEBUG] $1"
}

# Check if at least the module name and package name are provided
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Usage: $0 <ModuleName> <PackageName> [CommonPackageName]"
    exit 1
fi

# Assign variables based on arguments
MODULE_NAME=$1
PACKAGE_NAME=$2
COMMON_PACKAGE_NAME=$3
MODULE_DIR=$(echo "$MODULE_NAME" | tr '[:upper:]' '[:lower:]')
PACKAGE_DIR=$(echo "$PACKAGE_NAME" | tr '.' '/')

# Debug output
debug "Module Name: $MODULE_NAME"
debug "Package Name: $PACKAGE_NAME"
debug "Common Package Name: $COMMON_PACKAGE_NAME"
debug "Module Directory: $MODULE_DIR"
debug "Package Directory: $PACKAGE_DIR"

# Define base directory for module-specific files
BASE_DIR="app/src/main/java/$PACKAGE_DIR/$MODULE_DIR"

# Optionally define base directory for common files
if [ -n "$COMMON_PACKAGE_NAME" ]; then
    COMMON_PACKAGE_DIR=$(echo "$COMMON_PACKAGE_NAME" | tr '.' '/')
    COMMON_DIR="app/src/main/java/$COMMON_PACKAGE_DIR"
    # Create the common directory if specified
    mkdir -p "$COMMON_DIR"
    debug "Common Directory: $COMMON_DIR"
else
    COMMON_DIR=""
fi

# Create the necessary directories
mkdir -p "$BASE_DIR/view"
debug "Base Directory: $BASE_DIR"

# Function to create files with feedback
create_file() {
    local file_path=$1
    local content=$2
    echo "$content" > "$file_path"
    echo "Created $file_path"
}

# Function to create BaseViewModel if it does not exist and common directory is specified
create_base_view_model() {
    if [ -n "$COMMON_DIR" ]; then
        local file_path="$COMMON_DIR/BaseViewModel.kt"
        if [ ! -f "$file_path" ]; then
            create_file "$file_path" "package $COMMON_PACKAGE_NAME

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<STATE : State, EVENT, INTENT>(
    initialState: STATE,
) : ViewModel(), KoinComponent {
    private val loginPrefDataSource: LoginPrefDataSource by inject()

    abstract fun onIntent(intent: INTENT)

    // UI state
    private val _state = MutableStateFlow(initialState)
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState
        )
    val currentState: STATE get() = state.value
    protected fun setState(update: (old: STATE) -> STATE): STATE = _state.updateAndGet(update)

    // UI event
    val eventChannel = Channel<EVENT>()
    val event = eventChannel.receiveAsFlow()

    fun sendEvent(event: EVENT) = viewModelScope.launch {
        eventChannel.send(event)
    }

    // loading state
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    // connection Error
    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private val _downloadState = MutableStateFlow(false)
    val downloadState = _downloadState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    fun setDownloadCompleted(isShow: Boolean = true) {
        _downloadState.value = isShow
    }

    private fun setConnectionError(isConnected: Boolean = true) {
        _connectionState.value = isConnected
    }

    fun triggerToastConnection() = viewModelScope.launch {
        setConnectionError(true)
        delay(1000)
        setConnectionError(false)
    }

    fun triggerToastDownloadCompleted() = viewModelScope.launch {
        setDownloadCompleted(true)
        delay(3000)
        setDownloadCompleted(false)
    }

    fun setLoading(isLoading: Boolean = true) {
        _loadingState.value = isLoading
    }

    // error state
    private val _responseState = MutableStateFlow(StatusResponse())
    val responseState = _responseState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = StatusResponse()
        )

    fun setStatusResponse(statusResponse: StatusResponse = StatusResponse()) =
        viewModelScope.launch {
            // mapping for handle 401
            if (statusResponse.code == ResponseApi.authExpiredJwt ||
                statusResponse.code == ResponseApi.authInvalidJwt ||
                statusResponse.code == ResponseApi.authUnauthorizedDeviceId)
                loginPrefDataSource.clearLogin(isForceLogout = true)
            // update error state
            _responseState.value = statusResponse
        }
}"
            debug "BaseViewModel created in $COMMON_DIR"
        else
            debug "BaseViewModel already exists in $COMMON_DIR"
        fi
    fi
}

# Create BaseViewModel class if common directory is specified
create_base_view_model

# Create Event class
create_file "$BASE_DIR/${MODULE_NAME}Event.kt" "package $PACKAGE_NAME.$MODULE_DIR

sealed class ${MODULE_NAME}Event {
    data class Submit(val username: String, val password: String) : ${MODULE_NAME}Event()
    object Reset : ${MODULE_NAME}Event()
}"

# Create State class
create_file "$BASE_DIR/${MODULE_NAME}State.kt" "package $PACKAGE_NAME.$MODULE_DIR

data class ${MODULE_NAME}State(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)"

# Create ViewModel class with onIntent method
create_file "$BASE_DIR/${MODULE_NAME}ViewModel.kt" "package $PACKAGE_NAME.$MODULE_DIR

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ${MODULE_NAME}ViewModel() : BaseViewModel<${MODULE_NAME}State, ${MODULE_NAME}Event, ${MODULE_NAME}Intent>(${MODULE_NAME}State()) {

    override fun onIntent(intent: ${MODULE_NAME}Intent) {
        // Handle intents here
    }
}"

# Create Intent interface
create_file "$BASE_DIR/${MODULE_NAME}Intent.kt" "package $PACKAGE_NAME.$MODULE_DIR

interface ${MODULE_NAME}Intent {
    fun submitData(username: String, password: String)
    fun resetForm()
}"

# Create Screen composable
create_file "$BASE_DIR/view/${MODULE_NAME}Screen.kt" "package $PACKAGE_NAME.$MODULE_DIR.view

import androidx.compose.runtime.Composable
import $PACKAGE_NAME.$MODULE_DIR.${MODULE_NAME}ViewModel

@Composable
fun ${MODULE_NAME}Screen(viewModel: ${MODULE_NAME}ViewModel) {
    // Composable content
}"

# Create View composable
create_file "$BASE_DIR/view/${MODULE_NAME}View.kt" "package $PACKAGE_NAME.$MODULE_DIR.view

import androidx.compose.runtime.Composable

@Composable
fun ${MODULE_NAME}View() {
    // Composable content
}"

# Notify the user
echo "${MODULE_NAME} module files generated successfully in $BASE_DIR!"
