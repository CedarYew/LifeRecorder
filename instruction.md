我具有一定的开发基础，但是对Android开发不是很熟悉，所以需要你帮我完成一个Android项目。
我想完成一个Android项目，项目名称为“TimeRecorder”。
项目是一个事件记录软件，用户可以在这里记录自己的日常事项，并且可以查看历史事项。
为了方便，我们将事件记录称为task。
项目需要具备以下功能或属性：
1. 主页面是当天的事件列表，列表项包括事件名称、事件描述、开始时间和结束时间等。
2. 将一个列表项称为一个task，其数据结构为：
```kotlin
  data class Task(
      val id: Int = 0, // 主键
      val name: String, // 任务名称
      val description: String, // 任务描述
      val planStart: Int = 0, // 任务计划开始时间
      val startTime: String? = null, // 开始时间
      val endTime: String? = null, // 结束时间
      val creatTime: String? = null, // 创建时间
      val updateTime: String? = null, // 更新时间
  )
```
3. 对于某一天的数据，其结构为：
```kotlin
  // Task 用于表示单个任务
  import Task
  // DailyData 用于包装日期及任务记录
  data class DailyData(
      val date: String,
      val weather: String,
      val mood: String,
      val sleep: String,
      val income: Double,
      val expenditure: Double,
      val totalBalance: Double,
      val records: List<Task> // 任务记录
  )
```
4. 开始时间和结束时间前有一个checkbox，用户可以勾选checkbox来表示事件的开始和结束，布局大概如下。
```xml
  <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:orientation="horizontal"
      android:padding="8dp">
      <CheckBox
          android:id="@+id/startTimeButton"
          android:layout_width="70dp"
          android:layout_height="wrap_content"
          android:text="开始" />

      <CheckBox
          android:id="@+id/endTimeButton"
          android:layout_width="80dp"
          android:layout_height="wrap_content"
          android:text="结束" />

      <TextView
          android:id="@+id/taskName"
          android:layout_width="100dp"
          android:layout_height="wrap_content"
          android:ellipsize="end"
          android:maxLines="1"
          android:text="任务名称"
          android:textColor="#000000"
          android:textSize="16sp" />

      <TextView
          android:id="@+id/taskRemark"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:ellipsize="end"
          android:maxLines="1"
          android:text="任务备注"
          android:textColor="#666666"
          android:textSize="14sp" />
  </LinearLayout>
```
5. 用户勾选开始或结束时间的checkbox时，开始时间或结束时间会显示当前时间，并且开始时间或结束时间会显示为灰色。
6. 用户双击列表项，可以弹出一个窗口来编辑事件名称和事件描述以及事件计划开始时间。
7. 在事件列表的底部有一个按钮，用户可以点击按钮来添加一个事件。
8. 主页面布局为：
```xml
  <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="vertical">
      <header>
        <侧边栏按钮/>
        <TextView
          android:id="@+id/headerTitle"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:text="TimeRecorder" />
        <日历按钮/>
      </header>
      <content>
        <TextView>
          <!-- 日期，周几，天气，心情 -->
          4月1日，星期三，晴，开心
        <TextView/>
        <ListView
            android:id="@+id/taskList"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <Button
            android:id="@+id/addTaskButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="+" />
      </content>
  </LinearLayout>
```
9. 侧边栏按钮用于打开侧边栏，侧边栏包含用户信息、设置、退出登录等功能。
10. 日历按钮用于打开日历，用户选择指定的日期可以查看当日的历史记录。
11. 用户左滑动列表项，在列表项右侧出现一个红色的删除按钮，点击此按钮可以删除该任务项。
12. 用户长按列表项，可以拖动来调整该列表项在列表中的位置。
13. 任务计划开始时间在数据结构上是一个int整数，代表该事件计划开始的小时数，如17代表该任务计划于当日17点开始。
14. 任务列表中的任务项默认按任务的计划开始时间排序，如计划开始时间相同则按创建时间排序。
15. 系统实时获取当前系统时间，对于计划开始时间为当前系统时间之前的任务，任务背景颜色为灰色，反之为浅灰色。
16. 项目使用MVVM架构，项目中需要使用ViewModel和LiveData来实现数据绑定。
17. 项目中需要使用Room来实现数据库操作。
18. 项目中需要使用Navigation来实现页面跳转。

实现任务编辑对话框
添加滑动删除功能
实现拖拽排序
完善导航功能
添加日历选择功能