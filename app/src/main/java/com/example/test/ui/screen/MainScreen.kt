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
import com.example.test.ui.component.TaskList

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

        TaskList(
            tasks = tasks,
            onEditTask = onEditTask,
            onDeleteTask = onDeleteTask,
            onToggleStart = onToggleStart,
            onToggleEnd = onToggleEnd,
            modifier = Modifier.weight(1f)
        )
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
