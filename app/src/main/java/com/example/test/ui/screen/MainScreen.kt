package com.example.test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent()
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = { /* 打开侧边栏 */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* 打开日历 */ }) {
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
            }
        ) { paddingValues ->
            TaskList(
                modifier = Modifier.padding(paddingValues),
                tasks = tasks,
                onEditTask = onEditTask,
                onDeleteTask = onDeleteTask,
                onToggleStart = onToggleStart,
                onToggleEnd = onToggleEnd
            )
        }
    }
}

@Composable
private fun TaskList(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        // 日期和天气信息
        Text(
            text = getCurrentDateInfo(),
            modifier = Modifier.padding(16.dp)
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
private fun NavigationDrawerContent() {
    // 侧边栏内容实现
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Navigation Drawer")
        // 添加更多侧边栏内容
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleStart: () -> Unit,
    onToggleEnd: () -> Unit
) {
    val currentHour = LocalDateTime.now().hour
    val isOverdue = task.planStart < currentHour

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isOverdue) Color.Gray.copy(alpha = 0.2f)
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
        Checkbox(
            checked = task.startTime != null,
            onCheckedChange = { onToggleStart() }
        )
        Checkbox(
            checked = task.endTime != null,
            onCheckedChange = { onToggleEnd() }
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

private fun getCurrentDateInfo(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("M月d日，EEEE")
    return "${now.format(formatter)}，晴，开心" // 天气和心情可以从数据源获取
} 