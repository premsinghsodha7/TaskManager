package com.compose.taskmanager.presentation.ui.screen.add

import android.os.Build
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.compose.taskmanager.R
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.presentation.ui.screen.home.HomeViewModel
import com.compose.taskmanager.presentation.util.MultiSelector
import com.compose.taskmanager.presentation.ui.theme.Black
import com.compose.taskmanager.presentation.ui.theme.Grey
import com.compose.taskmanager.presentation.ui.theme.White
import com.compose.taskmanager.presentation.ui.theme.priegoFont
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.input.DialogNumberPicker
import com.michaelflisar.composedialogs.dialogs.input.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogNumber
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.MinusCircle
import compose.icons.feathericons.PlusCircle
import es.dmoral.toasty.Toasty
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskContent(
    onHideSheet: (Boolean) -> Unit,
    addTaskViewModel: AddTaskViewModel = viewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by addTaskViewModel.taskFormState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        addTaskViewModel.validationEvents.collect { event ->
            when (event) {
                is AddTaskViewModel.ValidationEvent.Success -> {
                    Toasty.success(
                        context,
                        "Task is created successfully.",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()

                    val task = Task(
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate,
                        priority = state.selectedPriority
                    )

                    homeViewModel.insertTask(task)
                    onHideSheet(true)

                }
                is AddTaskViewModel.ValidationEvent.Error -> {
                    val errorMessage = event.errorMessage

                    Toasty.error(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 38.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomTextField(
            modifier = Modifier.height(48.dp),
            label = "Title",
            value = state.title,
            onValueChanged = { addTaskViewModel.onEvent(TaskFormEvent.TitleChanged(it)) },
            hint = "Enter task title"
        )

        CustomTextField(
            modifier = Modifier.height(150.dp),
            label = "Description",
            value = state.description,
            onValueChanged = { addTaskViewModel.onEvent(TaskFormEvent.DescriptionChanged(it)) },
            hint = "Enter task description",
            maxLines = 5
        )

        DateTimePickerSection(state, addTaskViewModel::onEvent)

        PrioritySelectorSection(state, addTaskViewModel::onEvent)
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    hint: String,
    maxLines: Int = 1
) {
    val localFocusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color =  White,
            fontFamily = priegoFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.alpha(0.7f)
        )

        BasicTextField(modifier = Modifier
            .background(Grey, RoundedCornerShape(12.dp))
            .padding(3.dp)
            .fillMaxWidth()
            .then(modifier),
            value = value,
            onValueChange = onValueChanged,
            maxLines = maxLines,
            cursorBrush = SolidColor(Color.Gray),
            keyboardActions = KeyboardActions {
                localFocusManager.clearFocus()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            textStyle = LocalTextStyle.current.copy(
                color =  White,
                fontFamily = priegoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = 14.5.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 13.dp)
                    ) {
                        if (value.isEmpty()) Text(
                            text = hint,
                            color = Color(0xFF6c6f77),
                            fontFamily = priegoFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                        innerTextField()
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerSection(
    state: TaskFormState,
    onEvent: (TaskFormEvent) -> Unit
) {
    val calendarState = rememberUseCaseState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH
        ),
        selection = CalendarSelection.Date { date ->
            onEvent(TaskFormEvent.DueDateChanged(date))
        }
    )

    val timeState = rememberDialogState()

    if (timeState.showing) {
        // special state for input dialog
        val value = rememberDialogNumber(number = state.estimateTask)

        // number dialog
        DialogNumberPicker(
            state = timeState,
            formatter = { "${it}h" },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Task Duration Estimation",
                        fontSize = 16.sp,
                        color = Black,
                        fontFamily = priegoFont,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            value = value,
            textStyle = TextStyle(
                fontFamily = priegoFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Black
            ),
            icon = {
                Icon(
                    imageVector = FeatherIcons.Clock,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(23.dp)
                )
            },
            iconUp = {
                Icon(
                    imageVector = FeatherIcons.PlusCircle,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(23.dp)
                )
            },
            iconDown = {
                Icon(
                    imageVector = FeatherIcons.MinusCircle,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(23.dp)
                )
            },
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    onEvent(TaskFormEvent.EstimateTaskChanged(value.value))
                }
            },
            setup = NumberPickerSetup(
                min = 1, max = 25, stepSize = 1
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 17.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Due date",
                fontSize = 14.sp,
                color =  White,
                fontFamily = priegoFont,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(bottom = 9.dp)
            )

            Box(modifier = Modifier
                .background(Grey, RoundedCornerShape(12.dp))
                .padding(3.dp)
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    calendarState.show()
                },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("dd MMM")
                            .format(state.dueDate),
                        color = White,
                        fontFamily = priegoFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun PrioritySelectorSection(
    state: TaskFormState,
    onEvent: (TaskFormEvent) -> Unit
) {
    val priorityOptions = listOf("Low", "Medium", "High")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Priority",
            fontSize = 14.sp,
            color =  White,
            fontFamily = priegoFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .alpha(0.7f)
                .padding(bottom = 9.dp)
        )

        MultiSelector(
            options = priorityOptions,
            selectedOption = state.selectedPriority,
            onOptionSelect = { option ->
                onEvent(TaskFormEvent.PriorityChanged(option))
            },
            selectedColor = Black,
            unselectedColor = White,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        )
    }
}
