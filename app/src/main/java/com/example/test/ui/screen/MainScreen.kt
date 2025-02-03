package com.example.test.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.test.R
import com.example.test.data.model.Task
import com.example.test.data.model.DailyData
import com.example.test.ui.dialog.CalendarDialog
import com.example.test.ui.dialog.EditTaskDialog
import com.example.test.ui.dialog.EditDailyDataDialog
import com.example.test.ui.dialog.DeleteConfirmDialog
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.test.ui.navigation.NavigationItem
import com.example.test.ui.screen.TodoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    tasks: List<Task>,
    dailyData: DailyData,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit,
    onSaveTask: (Task) -> Unit,
    showEditDialog: Boolean,
    currentEditTask: Task?,
    onDismissDialog: () -> Unit,
    currentDate: Int,
    onDateSelected: (Long) -> Unit,
    onUpdateDailyData: (DailyData) -> Unit,
    todoTasks: List<Task> = emptyList()
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showCalendarDialog by remember { mutableStateOf(false) }
    var selectedNavigation by remember { mutableStateOf(NavigationItem.TODAY) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(onClose = {
                scope.launch {
                    drawerState.close()
                }
            })
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showCalendarDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calendar),
                                contentDescription = stringResource(R.string.calendar)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddTask) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_task)
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    NavigationItem.values().forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedNavigation == item,
                            onClick = { selectedNavigation = item }
                        )
                    }
                }
            }
        ) { paddingValues ->
            when (selectedNavigation) {
                NavigationItem.TODAY -> TodayContent(
                    modifier = Modifier.padding(paddingValues),
                    tasks = tasks,
                    dailyData = dailyData,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask,
                    onToggleStart = onToggleStart,
                    onToggleEnd = onToggleEnd,
                    onEditDailyData = onUpdateDailyData
                )
                NavigationItem.TODO -> TodoScreen(
                    tasks = todoTasks,
                    onAddTask = onAddTask,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask,
                    onToggleStart = onToggleStart,
                    onToggleEnd = onToggleEnd,
                    onSaveTask = onSaveTask,
                    showEditDialog = showEditDialog,
                    currentEditTask = currentEditTask,
                    onDismissDialog = onDismissDialog
                )
                NavigationItem.ME -> com.example.test.ui.screen.MeScreen()
            }
        }
    }

    if (showEditDialog) {
        EditTaskDialog(
            task = currentEditTask,
            onDismiss = onDismissDialog,
            onConfirm = onSaveTask,
            currentDate = currentDate
        )
    }

    if (showCalendarDialog) {
        CalendarDialog(
            onDismiss = { showCalendarDialog = false },
            onDateSelected = { date ->
                onDateSelected(date.time)
                showCalendarDialog = false
            }
        )
    }
}

@Composable
private fun TodayContent(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    dailyData: DailyData,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit,
    onEditDailyData: (DailyData) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        DailyHeader(
            dailyData = dailyData,
            onEditDailyData = onEditDailyData,
            modifier = Modifier.padding(6.dp)
        )

        // 任务列表
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onEdit = { onEditTask(task) },
                    onDelete = { onDeleteTask(task.id) },
                    onToggleStart = { onToggleStart(task) },
                    onToggleEnd = { onToggleEnd(task) }
                )
            }
        }
    }
}

@Composable
private fun DailyHeader(
    dailyData: DailyData,
    onEditDailyData: (DailyData) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showEditDialog = true }
            .padding(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 新增日期信息
            DateDisplay(dailyData.date)
            
            // 原有图标部分
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = dailyData.mood.icon,
                    contentDescription = "心情状态",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Icon(
                    imageVector = dailyData.weather.icon,
                    contentDescription = "天气状况",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Icon(
                    imageVector = dailyData.sleep.icon,
                    contentDescription = "睡眠质量",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            IncomeExpenseDisplay(
                income = dailyData.income,
                expenditure = dailyData.expenditure
            )
        }
    }

    if (showEditDialog) {
        EditDailyDataDialog(
            dailyData = dailyData,
            onDismiss = { showEditDialog = false },
            onConfirm = { 
                onEditDailyData(it)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun DateDisplay(date: Int) {
    val calendar = remember {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, date / 10000)
            set(Calendar.MONTH, (date % 10000) / 100 - 1)
            set(Calendar.DAY_OF_MONTH, date % 100)
        }
    }

    // val dateFormat = remember { SimpleDateFormat("M月d日 EEEE", Locale.CHINA) }
    val dateFormat = remember { 
        SimpleDateFormat("MM/dd EEE", Locale.US)
    }
    
    Text(
        text = dateFormat.format(calendar.time),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun IncomeExpenseDisplay(income: Double, expenditure: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 收入
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = "收入",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.income, income),
                style = MaterialTheme.typography.labelSmall
            )
        }
        
        // 支出
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoneyOff,
                contentDescription = "支出",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.expenditure, expenditure),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun NavigationDrawerContent(onClose: () -> Unit) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 用户信息头部
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_account),
                    contentDescription = "用户头像",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.user_nickname),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 功能菜单
        Column(modifier = Modifier.padding(16.dp)) {
            NavigationMenuItem(
                icon = R.drawable.ic_info,
                text = stringResource(R.string.about_us),
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(context.getString(R.string.about_us_url))
                        }
                    )
                }
            )
            
            NavigationMenuItem(
                icon = R.drawable.github_mark,
                text = stringResource(R.string.repository),
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(context.getString(R.string.github_repo_url))
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun NavigationMenuItem(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleStart: () -> Unit,
    onToggleEnd: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { swipeValue ->
            if (swipeValue == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false
            } else true
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.25f }
    )

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            }
        )
    }

    SwipeToDismissBox(
        state = state,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when (state.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                            else -> Color.Transparent
                        }
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (task.planStart < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) Color.Gray.copy(alpha = 0.2f)
                    else Color.LightGray.copy(alpha = 0.1f)
                )
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { onEdit() }
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 开始时间
            Row(
                modifier = Modifier.width(80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.startTime != null,
                    onCheckedChange = { onToggleStart() }
                )
                if (task.startTime != null) {
                    Text(
                        text = formatTime(task.startTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "Start",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // 结束时间
            Row(
                modifier = Modifier.width(100.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.endTime != null,
                    onCheckedChange = { onToggleEnd() }
                )
                if (task.endTime != null) {
                    Text(
                        text = formatTime(task.endTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "End",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // 任务名称和描述
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            state.reset()
        }
    }
}

private fun formatTime(timeString: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val date = format.parse(timeString)
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        timeString
    }
}

private fun formatDateInfo(dailyData: DailyData): String {
    val calendar = Calendar.getInstance().apply {
        set(dailyData.date / 10000, (dailyData.date % 10000) / 100 - 1, dailyData.date % 100)
    }
    val dateFormat = SimpleDateFormat("M.d, EEEE", Locale.getDefault())
    return "${dateFormat.format(calendar.time)}, ${dailyData.weather}, ${dailyData.mood}"
}

@Composable
private fun TodoScreen(
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit,
    onSaveTask: (Task) -> Unit,
    showEditDialog: Boolean,
    currentEditTask: Task?,
    onDismissDialog: () -> Unit
) {
    com.example.test.ui.screen.TodoScreen(
        tasks = tasks,
        onAddTask = onAddTask,
        onEditTask = onEditTask,
        onDeleteTask = onDeleteTask,
        onToggleStart = onToggleStart,
        onToggleEnd = onToggleEnd,
        onSaveTask = onSaveTask,
        showEditDialog = showEditDialog,
        currentEditTask = currentEditTask,
        onDismissDialog = onDismissDialog
    )
} 